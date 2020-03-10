package com.mhwan.mask.Util.NetworkUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mhwan.mask.GetMaskListener;
import com.mhwan.mask.Item.MaskStoreItem;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.AppUtility;

import java.util.ArrayList;

public class GetMaskInfoTask extends AsyncTask<String, Void, Object> {
    private Context context;
    private GetMaskListener listener;
    public GetMaskInfoTask(Context context, GetMaskListener listener){
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected Object doInBackground(String... strings) {

        if (!isCancelled()) {
            Object result;
            if (strings.length == 3)
                result = GetMaskInfo.getInstance().getStoresByGeo(strings[0], strings[1], strings[2]);
            else
                result = GetMaskInfo.getInstance().getStoresByAddress(strings[0]);

            return result;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (!AppUtility.getAppinstance().isNetworkConnection()){
            cancel(true);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (listener != null)
            listener.onDataReceived((ArrayList<MaskStoreItem>) o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context, context.getString(R.string.cancel_mask_finding), Toast.LENGTH_SHORT).show();
    }
}
