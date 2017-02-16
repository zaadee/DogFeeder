package com.rmuti.project.dogfeeder.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rmuti.project.dogfeeder.R;
import com.rmuti.project.dogfeeder.screen.ScreenMain;
import com.rmuti.project.dogfeeder.screen.ScreenSetIPServer;
import com.rmuti.project.dogfeeder.utils.ShareData;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private ShareData shareData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareData = new ShareData(this);

        if (isHaveIP()) {
            replaceFragment(new ScreenMain(), getString(R.string.fragment_tag_main));
        } else {
            replaceFragment(new ScreenSetIPServer(), getString(R.string.fragment_tag_set_ip));
        }

    }

    /**
     * check ip camera and ip control food (Arduino Cam, Node MCU)
     *
     * @return
     */
    private boolean isHaveIP() {
        if (!shareData.getIPCam().equals("") && !shareData.getIPControlFood().equals("")) {
            return true;
        }
        return false;
    }

    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main, fragment, tag)
                .commit();
    }
    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main, fragment, tag)
                .commit();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment.getTag().equals(getString(R.string.fragment_tag_main))) {
                Log.e(TAG, "fragment tag: " + fragment.getTag());
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


}
