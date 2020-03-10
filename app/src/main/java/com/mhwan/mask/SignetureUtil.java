package com.mhwan.mask;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// 액티비티에서 Signeture(키 해시값) 가져오기
public class SignetureUtil {

    // 액티비티에서 Signeture(키 해시값) 가져오기
    public static String getSigneture(Context activityContext){
        PackageManager pm = activityContext.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(activityContext.getPackageName(), PackageManager.GET_SIGNATURES);

            for(int i = 0; i < packageInfo.signatures.length; i++){
                Signature signature = packageInfo.signatures[i];
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }

        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

}
