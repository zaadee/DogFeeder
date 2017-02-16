package com.rmuti.project.dogfeeder.screen;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rmuti.project.dogfeeder.R;
import com.rmuti.project.dogfeeder.utils.ShareData;

import java.net.InetAddress;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSetIPServer extends Fragment {
    private View rootView;

    private EditText edtIPCam, edtControlFood;
    private Button btnConnect;

    public ScreenSetIPServer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.screen_set_ip_server, container, false);
        edtIPCam = (EditText) rootView.findViewById(R.id.edt_ip_cam);
        edtControlFood = (EditText) rootView.findViewById(R.id.edt_ip_control);
        btnConnect = (Button) rootView.findViewById(R.id.btn_connect);

        edtControlFood.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnConnect.callOnClick();
                }
                return false;
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipCam = edtIPCam.getText().toString().trim();
                String ipControlFood = edtControlFood.getText().toString().trim();
                if (isIPValid(ipCam, ipControlFood)) {
                    new ShareData(getContext()).setAllIP(ipCam, ipControlFood);
                    replaceFragment(new ScreenMain(), getString(R.string.fragment_tag_main));
                }
            }
        });

        return rootView;
    }

    private boolean isIPValid(String ipCam, String ipControlFood) {
        if (!ipCam.isEmpty() && !ipControlFood.isEmpty()) {
            if (Patterns.IP_ADDRESS.matcher(ipCam).matches()) {
                if (Patterns.IP_ADDRESS.matcher(ipControlFood).matches()) {
                    return true;
                }
                showSnackBar(getString(R.string.err3));
                return false;
            }
            showSnackBar(getString(R.string.err2));
            return false;
        }
        showSnackBar(getString(R.string.err1));
        return false;

    }

    private void replaceFragment(Fragment fragment, String tag) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main, fragment, tag)
                .commit();
    }

    private void showSnackBar(String message) {
        if (rootView != null) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
        }

    }


}
