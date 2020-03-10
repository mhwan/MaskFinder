package com.mhwan.mask;

import com.mhwan.mask.Item.MaskStoreItem;

import java.util.ArrayList;

public interface GetMaskListener {
    void onDataReceived(ArrayList<MaskStoreItem> list);
}
