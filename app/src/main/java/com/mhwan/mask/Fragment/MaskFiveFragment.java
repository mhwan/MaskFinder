package com.mhwan.mask.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mhwan.mask.CustomUI.CustomEntryEdittext;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.AppContext;
import com.mhwan.mask.Util.AppUtility;
import com.mhwan.mask.Util.SignetureUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaskFiveFragment extends Fragment implements CustomEntryEdittext.onFinishListerner {
    private View view;
    private CustomEntryEdittext entryEdittext;
    private TextView result_text;
    private String[] day_array;

    public MaskFiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mask_five, container, false);
        initView();
        return view;
    }

    private void initView(){
        day_array = AppContext.getContext().getResources().getStringArray(R.array.day_array);
        result_text = (TextView) view.findViewById(R.id.result_text);
        entryEdittext = (CustomEntryEdittext) view.findViewById(R.id.custom_unique_edittext);
        entryEdittext.setOnFinishListerner(this);
    }

    private void setResultText(String result) {
        result_text.setText(String.format(AppContext.getContext().getString(R.string.mask_day_result_format), result));
        result_text.setVisibility(View.VISIBLE);
    }
    @Override
    public void onFinish(String enteredText) {
        Log.d("entered", enteredText);
        int i  = Character.getNumericValue(enteredText.charAt(3));
        int result = AppUtility.getAppinstance().getMaskDay(i);

        Log.d("result", result+"");

        setResultText(day_array[result]);

    }

    @Override
    public void onNotFinished() {
        result_text.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), AppContext.getContext().getString(R.string.invalid_birth_input), Toast.LENGTH_SHORT).show();
    }
}
