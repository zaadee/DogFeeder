package com.rmuti.project.dogfeeder.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sitthiphong on 2/5/2017 AD.
 */

public class ShareData {
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public ShareData(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("dog_feeder_data", Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public String getIPCam() {
        return sp.getString("ipCam", "");
    }

    public void setIPCam(String ipCam) {
        spEditor.putString("ipCam", ipCam);
        spEditor.commit();
    }

    public String getIPControlFood() {
        return sp.getString("ipControlFood", "");
    }

    public void setIPControlFood(String ipControlFood) {
        spEditor.putString("ipControlFood", ipControlFood);
        spEditor.commit();
    }

    public void setAllIP(String ipCam, String ipControlFood) {
        spEditor.putString("ipCam", ipCam);
        spEditor.putString("ipControlFood", ipControlFood);
        spEditor.commit();
    }

    /**
     * for set quality off video
     * 0 is 160x120
     * 1 is 176x144
     * 2 is 320x240
     * 3 is 352x288
     * 4 is 640x480
     * 5 is 800x600
     * 6 is 1024x768
     * 7 is 1280x1024
     * 8 is 1600x1200
     *
     * @return
     */
    public int getQualityVideo() {
        return sp.getInt("quality", 2);
    }

    public void setQualityVideo(int qualityVideo) {
        spEditor.putInt("quality", qualityVideo);
        spEditor.commit();
    }

    public boolean getAutoFood() {
        return sp.getBoolean("autoFood", false);
    }

    public void setAutoFood(boolean autoFood) {
        spEditor.putBoolean("autoFood", autoFood);
        spEditor.commit();
    }
}
