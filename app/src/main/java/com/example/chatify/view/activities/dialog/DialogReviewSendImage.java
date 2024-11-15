package com.example.chatify.view.activities.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.chatify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;

import java.util.Objects;

public class DialogReviewSendImage {
    private Context context;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private ZoomageView image;
    private FloatingActionButton button;

    public DialogReviewSendImage(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
        this.progressDialog = new ProgressDialog(context);
        this.dialog = new Dialog(context);
        initialize();
    }

    public void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_re_view_image);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        image = dialog.findViewById(R.id.imageView);
        button = dialog.findViewById(R.id.send);


    }

    public void show(OnCallBack onCallBack) {
        image.setImageBitmap(bitmap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallBack.onButtonSendClick();
            }
        });
    }

    public interface OnCallBack {
        void onButtonSendClick();

    }
}
