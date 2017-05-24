package com.rmuti.project.dogfeeder.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.rmuti.project.dogfeeder.listener.BitmapListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class BitmapUtil {
    private Context context;
    private BitmapListener bitmapListener;

    public BitmapUtil(Context context, BitmapListener bitmapListener) {
        this.context = context;
        this.bitmapListener = bitmapListener;
    }

    public void saveImageFromWebview(WebView webView) {
        new SaveBitmapTask().execute(getBitmapFromWebView(webView));
    }

    private class SaveBitmapTask extends AsyncTask<Bitmap, Void, Uri> {

        @Override
        protected void onPreExecute() {
            Log.i("BitmapUtil", "onPreExecute");
            //super.onPreExecute();
        }

        @Override
        protected Uri doInBackground(Bitmap... params) {
            return saveBitmapTpFile(params[0], context);
        }

        @Override
        protected void onPostExecute(Uri result) {
            Log.i("BitmapUtil", "onPostExecute");
            //notificationSnackBar(rootLayout,getString(R.string.loadIMSuccess));
            bitmapListener.onSaveBitmapSuccess(result);

        }

    }


    private Uri saveBitmapTpFile(Bitmap bitmap, Context context) {
        Log.i("BitmapUtil", "saveBitmapTpFile");
        String imageName = "SG_IMG" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
        bitmap.recycle();
        file = null;
        return contentUri;
    }

    private Bitmap getBitmapFromWebView(WebView webView) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webView.draw(canvas);
            return bitmap;
        } catch (Exception e) {

        }
        return null;
    }

}
