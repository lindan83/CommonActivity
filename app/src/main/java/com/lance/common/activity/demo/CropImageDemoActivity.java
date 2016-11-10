package com.lance.common.activity.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.isseiaoki.simplecropview.CropImageView;
import com.lance.common.activity.ImageCropActivity;
import com.lance.common.activity.MultiImageSelector;
import com.lance.common.activity.helper.ImageCropHelper;

import java.util.ArrayList;

public class CropImageDemoActivity extends AppCompatActivity {
    private static final String TAG = "CropImageDemoActivity";
    private ImageView mIvImageCropResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image_demo);

        mIvImageCropResult = (ImageView) findViewById(R.id.iv_image_crop_result);
    }

    public void pickImage(View v) {
        MultiImageSelector.create()
                .single()
                .showCamera(true)
                .start(this, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (result != null && !result.isEmpty()) {
                    String path = result.get(0);
                    ImageCropActivity.CropParams params = ImageCropHelper.ImageCropParamsBuilder
                            .create()
                            .compressQuality(100)
                            .cropMode(CropImageView.CropMode.FIT_IMAGE)
                            .outputMaxSize(800, 800)
                            .build();
                    ImageCropHelper.startCropFree(CropImageDemoActivity.this, path, params, 101);
                }
            }
        } else if (requestCode == 101) {
            Uri resultUri = ImageCropHelper.onActivityResult(resultCode, data);
            mIvImageCropResult.setImageURI(resultUri);
        }
    }
}
