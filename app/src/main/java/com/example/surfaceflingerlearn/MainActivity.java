package com.example.surfaceflingerlearn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View v) {
        View a = getWindow().peekDecorView();
        SurfaceControl tmpParent = null;
        try {
            Class pw = Class.forName("com.android.internal.policy.DecorView");
            Class vric = Class.forName("android.view.ViewRootImpl");
            Method gvrlm = pw.getMethod("getViewRootImpl");
            Method gblm = vric.getMethod("getBoundsLayer");
            Object ob = gvrlm.invoke(a);
            tmpParent = (SurfaceControl) gblm.invoke(ob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final SurfaceControl parent = tmpParent;
        Runnable r = () -> {
            SurfaceControl surfaceControl =
                    new SurfaceControl.Builder()
                            .setName("rejectliu")
                            .setBufferSize(/* width= */ 1000, /* height= */ 1000)
                            .setOpaque(true)
                            .setFormat(PixelFormat.RGBA_8888)
                            // must have a parent, otherwise will be add to offscreen layer
                            .setParent(parent)
                            .build();
            new SurfaceControl.Transaction().setLayer(surfaceControl, 0x40000000).setAlpha(surfaceControl, 1.0f).setGeometry(surfaceControl, null, null, Surface.ROTATION_0).setVisibility(surfaceControl,true).apply();
            Surface sf = new Surface(surfaceControl);
            while (true) {
                Rect rc = new Rect(0, 0, 1000, 1000);
                Canvas cv = sf.lockCanvas(rc);
                Paint p = new Paint();
                p.setColor(Color.RED);
                cv.drawRect(rc, p);
                sf.unlockCanvasAndPost(cv);
//                Bitmap bitmap = Bitmap.createBitmap(rc.width(), rc.height(), Bitmap.Config.RGB_565);
//                PixelCopy.request(sf, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
//                    @Override
//                    public void onPixelCopyFinished(int copyResult) {
//                        bitmap.recycle();
//                    }
//                }, new Handler());
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }
}