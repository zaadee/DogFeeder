package com.rmuti.project.dogfeeder.screen;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.snackbar.Snackbar;
import com.rmuti.project.dogfeeder.R;
import com.rmuti.project.dogfeeder.config.RequestCode;
import com.rmuti.project.dogfeeder.connections.Connections;
import com.rmuti.project.dogfeeder.listener.BitmapListener;
import com.rmuti.project.dogfeeder.utils.BitmapUtil;
import com.rmuti.project.dogfeeder.utils.ShareData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMain extends Fragment implements View.OnClickListener {

    private final String TAG = "ScreenMain";
    private View rootView;

    private ShareData shareData;
    private RelativeLayout layoutAutoFood, layoutDogFeeder, layoutDogWater,
            layoutCapture, layoutSetQuality, layoutSettingIP;

    private WebView streamView;
    private ProgressDialog progressDialog;

    public ScreenMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.screen_main, container, false);
        shareData = new ShareData(getContext());

        initToolbar();
        initComponent();
        initWebView();

        layoutDogFeeder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://press
                        // Press hold 500ms for Start
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                onGiveFood(true);
                            }
                        }, 500);
                        break;
                    case MotionEvent.ACTION_UP://release
                        // End
                        onGiveFood(false);
                        break;
                }
                return false;
            }
        });
        layoutDogWater.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://press
                        // Press hold 500ms for Start
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                onWater(true);
                            }
                        }, 500);
                        break;
                    case MotionEvent.ACTION_UP://release
                        // End
                        onWater(false);
                        break;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        Log.e("GGGGGGG", String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.layout_auto_food:
                Log.e("GGGGGGG", "KKKKK");
                setAutoFood();
                break;
            case R.id.layout_capture:
                onSaveImage();
                break;
            case R.id.layout_set_quality_video:
                setQualityVideo();
                break;
            case R.id.layout_setting_ip:
                intentFragment(new ScreenSetting(), getString(R.string.fragment_tag_setting));
                break;
            default:
                //
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.PREMISSION_EXTERNAL) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //Permission denied
                } else {
                    Log.i(TAG, "Permission granted");
                    saveImage();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            startStreamView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        if (rootView != null) {
            Toolbar toolbar = rootView.findViewById(R.id.toolbar);
            if (toolbar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
            }
        }
    }

    private void initComponent() {
        if (rootView != null) {
            layoutAutoFood = rootView.findViewById(R.id.layout_auto_food);
            layoutDogFeeder = rootView.findViewById(R.id.layout_dog_feeder);
            layoutDogWater = rootView.findViewById(R.id.layout_dog_water);
            layoutCapture = rootView.findViewById(R.id.layout_capture);
            layoutSetQuality = rootView.findViewById(R.id.layout_set_quality_video);
            layoutSettingIP = rootView.findViewById(R.id.layout_setting_ip);

            layoutAutoFood.setOnClickListener(this);
            //layoutDogFeeder.setOnClickListener(this);
            //layoutDogWater.setOnClickListener(this);
            layoutCapture.setOnClickListener(this);
            layoutSetQuality.setOnClickListener(this);
            layoutSettingIP.setOnClickListener(this);

        }
    }

    private void initWebView() {
        if (rootView != null) {
            streamView = rootView.findViewById(R.id.stream_view);
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            double width, height;
            double widthDp = ((double) metrics.widthPixels / (double) metrics.density);
            double heightDp = ((double) metrics.heightPixels / (double) metrics.density);

            if (widthDp < heightDp) {
                width = (widthDp * metrics.density);
                height = (int) ((widthDp / (float) 1.333) * metrics.density);
            } else {
                width = ((int) (((heightDp - 136) * 1.333) * metrics.density));
                height = ((int) ((heightDp - 136) * metrics.density));
            }

            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams((int) width, (int) height);
            streamView.setLayoutParams(param);

            streamView.getSettings().setJavaScriptEnabled(true);
            streamView.loadUrl("file:///android_asset/video.html");
            streamView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    //streamView.loadUrl("javascript:stream(\"192.168.1.252\",2)");
                    startStreamView();
                }
            });
        }

    }

    private void startStreamView() {
        if (streamView != null) {
            if (!shareData.getIPCam().equals("")) {
                Log.e(TAG, "start stream video at: " + shareData.getIPCam() + "/stream");
                streamView.loadUrl("javascript:streamWithAjax(\""
                        + shareData.getIPCam() + "\","
                        + shareData.getQualityVideo() + ")");
            } else {
                showSnackBar(getString(R.string.inputIPCam));
            }
        }
    }


    private void intentFragment(Fragment fragment, String tag) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }


    private void setAutoFood() {
        setDisableControl();
        setTitleToolbar(getString(R.string.set_food_auto) + "...");
        new Connections(getContext()).setAutoFood(new Connections.ConnectionsListener() {
            @Override
            public void onServerResponse(String message) {
                setEnableControl();
                setDefaultTitleToolbar();
                Log.e(TAG, "onServerResponse message: " + message);
                if (!message.equals("")) {
                    try {
                        JSONObject object = new JSONObject(message);
                        if (object.getInt("statusCode") == 200) {
                            //
                        }
                        showSnackBar(object.getString("statusMessage"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar(getString(R.string.err5));
                }
            }
        });

    }

    private void onGiveFood(boolean giveFood) {
        setDisableControl();
        setTitleToolbar(getString(R.string.set_give_food) + "...");
        new Connections(getContext()).giveFood(giveFood, new Connections.ConnectionsListener() {
            @Override
            public void onServerResponse(String message) {
                setEnableControl();
                setDefaultTitleToolbar();
                Log.e(TAG, "onServerResponse message: " + message);
                if (!message.equals("")) {
                    try {
                        JSONObject object = new JSONObject(message);
                        if (object.getInt("statusCode") == 200) {
                            //for give food success
                        }
                        showSnackBar(object.getString("statusMessage"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar(getString(R.string.err5));
                }
            }
        });
    }

    private void onWater(boolean value) {
        setDisableControl();
        setTitleToolbar(getString(R.string.set_water) + "...");
        new Connections(getContext()).water(value, new Connections.ConnectionsListener() {
            @Override
            public void onServerResponse(String message) {
                setEnableControl();
                setDefaultTitleToolbar();
                Log.e(TAG, "onServerResponse message: " + message);
                if (!message.equals("")) {
                    try {
                        JSONObject object = new JSONObject(message);
                        if (object.getInt("statusCode") == 200) {
                            //for water success
                        }
                        showSnackBar(object.getString("statusMessage"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar(getString(R.string.err5));
                }
            }
        });
    }

    private void setEnableControl() {
        layoutAutoFood.setEnabled(true);
        layoutDogFeeder.setEnabled(true);
        layoutDogWater.setEnabled(true);
    }

    private void setDisableControl() {
        layoutAutoFood.setEnabled(false);
        layoutDogFeeder.setEnabled(false);
        layoutDogWater.setEnabled(false);
    }

    private void setQualityVideo() {
        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.set_quality_video))
                .items(R.array.qualityArray)
                .itemsCallbackSingleChoice(shareData.getQualityVideo(),
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                shareData.setQualityVideo(which);
                                //re connect video
                                //startStreamView();
                                return true;
                            }
                        })
                .positiveText(R.string.choose)
                .negativeText(R.string.cancel)
                .show();

    }

    private void onSaveImage() {
        if (checkPermissionExternal()) {
            saveImage();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RequestCode.PREMISSION_EXTERNAL);
        }
    }

    private void saveImage() {
        Log.e(TAG, "saveImage");
        BitmapUtil bitmapUtil = new BitmapUtil(getContext(), new BitmapListener() {
            @Override
            public void onSaveBitmapSuccess(Uri uri) {
                showLoadImSuccessSnackBar(uri);
            }
        });
        bitmapUtil.saveImageFromWebview(streamView);
    }


    private boolean checkPermissionExternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void showLoadImSuccessSnackBar(final Uri uri) {
        if (rootView != null) {
            Snackbar.make(rootView, R.string.loadIMSuccess,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.open, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "image/*");
                            startActivity(intent);
                        }
                    }).show();
        }

    }

    private void showSnackBar(String message) {
        if (rootView != null) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
        }

    }

    private void setTitleToolbar(String message) {
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.app_name) + " " + message);
    }

    private void setDefaultTitleToolbar() {
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.app_name));
    }

    private void showProgress(String msg) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
