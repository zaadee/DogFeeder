package com.rmuti.project.dogfeeder.screen;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rmuti.project.dogfeeder.R;
import com.rmuti.project.dogfeeder.utils.ShareData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSetting extends Fragment implements View.OnClickListener {

    private final int IP_CAM = 1;
    private final int IP_CONTROL_FOOD = 2;

    private View rootView;
    private RelativeLayout layoutSetIPCam, layoutSetControlFood;
    private TextView tvIPCam, tvIPControlFood;
    private ShareData shareData;


    public ScreenSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.screen_setting, container, false);

        shareData = new ShareData(getContext());
        initToolbar();
        initComponent();


        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        if (rootView != null) {
            layoutSetIPCam = (RelativeLayout) rootView.findViewById(R.id.layout_set_ip_cam);
            layoutSetControlFood = (RelativeLayout) rootView.findViewById(R.id.layout_set_ip_control_food);

            tvIPCam = (TextView) rootView.findViewById(R.id.value_set_ip_cam);
            tvIPControlFood = (TextView) rootView.findViewById(R.id.value_set_ip_control_food);
            tvIPCam.setText(shareData.getIPCam());
            tvIPControlFood.setText(shareData.getIPControlFood());

            layoutSetIPCam.setOnClickListener(this);
            layoutSetControlFood.setOnClickListener(this);


        }


    }

    private void initToolbar() {
        if (rootView != null) {
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.set_ip));
        }

    }

    private void onEditIP(final int ipType) {
        String title = getString(R.string.setting) + " ";
        String preFill = "";
        if (ipType == IP_CAM) {
            title += getString(R.string.ip_camera);
            preFill = shareData.getIPCam();
        } else if (ipType == IP_CONTROL_FOOD) {
            title += getString(R.string.iP_control_food);
            preFill = shareData.getIPControlFood();
        }
        new MaterialDialog.Builder(getContext())
                .title(title)
                .inputType(InputType.TYPE_CLASS_PHONE)
                .input(getString(R.string.eg_IP), preFill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        validIP(ipType, input.toString());
                    }
                }).show();
    }

    private void validIP(int ipType, String ip) {
        if (!ip.isEmpty()) {
            if (Patterns.IP_ADDRESS.matcher(ip).matches()) {
                if (ipType == IP_CAM) {
                    if (!ip.equals(shareData.getIPCam())) {
                        shareData.setIPCam(ip);
                        tvIPCam.setText(ip);

                    } else {
                        showSnackBar("no change");
                    }


                } else {
                    if (!ip.equals(shareData.getIPControlFood())) {
                        shareData.setIPControlFood(ip);
                        tvIPControlFood.setText(ip);

                    } else {
                        showSnackBar("no change");
                    }

                }
            } else {
                showSnackBar(getString((ipType == IP_CAM) ? R.string.err2 : R.string.err3));
            }


        } else {
            showSnackBar(getString(R.string.err4));
        }


    }

    private void showSnackBar(String message) {
        if (rootView != null) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_set_ip_cam:
                onEditIP(IP_CAM);
                break;
            case R.id.layout_set_ip_control_food:
                onEditIP(IP_CONTROL_FOOD);
                break;
            default:
                break;
        }

    }

    public interface SettingListener {
        public void onSuccess(boolean isChanges);
    }

}
