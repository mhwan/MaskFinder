package com.mhwan.mask.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mhwan.mask.Item.Store;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.AppUtility;

import java.util.ArrayList;

public class StoreListRecyclerAdapter extends RecyclerView.Adapter<StoreListRecyclerAdapter.Viewholder> {
    private Context context;
    private ArrayList<Store> storeList;
    public StoreListRecyclerAdapter(Context context, ArrayList storelist) {
        this.context = context;
        this.storeList = storelist;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_list_store_info, null);
        return new StoreListRecyclerAdapter.Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Store store = storeList.get(position);
        holder.name.setText(store.getName());
        holder.address.setText(store.getAddr());
        String s = store.getRemainStat();
        if (s != null) {
            if (s.equals("plenty")) {
                holder.count.setBackgroundResource(R.drawable.bg_outerbox_green);
                holder.count.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                holder.count.setText("100개 이상");
            } else if (s.equals("some")) {
                holder.count.setBackgroundResource(R.drawable.bg_outerbox_yellow);
                holder.count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentYellow));
                holder.count.setText("30 ~ 100개");
            } else if (s.equals("few")) {
                holder.count.setBackgroundResource(R.drawable.bg_outerbox_red);
                holder.count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentRed));
                holder.count.setText("30개 이하");
            } else if (s.equals("empty")) {
                holder.count.setBackgroundResource(R.drawable.bg_outerbox_grey);
                holder.count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLightGrey));
                holder.count.setText("재고 없음");
            } else if (s.equals("break")) {
                holder.count.setBackgroundResource(R.drawable.bg_outerbox_grey);
                holder.count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLightGrey));
                holder.count.setText("판매 중지");
            }
        } else {
            holder.count.setBackgroundResource(R.drawable.bg_outerbox_grey);
            holder.count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLightGrey));
            holder.count.setText("정보 없음");
        }
        if (store.getDistanceMeters() != AppUtility.NO_DISTANCE_ERR)
            holder.distance.setText(AppUtility.getAppinstance().getDistanceString(store.getDistanceMeters()));
        else
            holder.distance.setText("거리를 알수없음");

        String l = (store.getStockAt() == null)? "정보없음" : store.getStockAt();
        holder.lastStock.setText("최근 입고 : "+l);
        String u = (store.getCreatedAt() == null)? "정보없음" : store.getCreatedAt();
        holder.update.setText("정보 업데이트 : "+u);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name, address, distance, update, lastStock, count;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.list_info_name);
            address = (TextView) itemView.findViewById(R.id.list_info_address);
            distance = (TextView) itemView.findViewById(R.id.list_info_distance);
            update = (TextView) itemView.findViewById(R.id.list_info_update);
            lastStock = (TextView) itemView.findViewById(R.id.list_info_lastStock);
            count = (TextView) itemView.findViewById(R.id.list_info_count);
        }
    }
}
