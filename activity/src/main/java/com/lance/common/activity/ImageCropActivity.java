package com.lance.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.lance.common.util.ToastUtil;
import com.lance.common.widget.TopBar;

import java.io.Serializable;


/**
 * @author lindan
 *         图像裁剪页
 */
public class ImageCropActivity extends AppCompatActivity implements TopBar.OnClickListener {
    private static final String TAG = "ImageCropActivity";

    public static final String RESULT_SAVE_URI = "save_uri";

    private static final int DEFAULT_CROP_COMPRESS_QUALITY = 80;//默认裁切质量
    private static final int DEFAULT_MAX_OUTPUT_WIDTH = 400;//默认输出最大宽度
    private static final int DEFAULT_MAX_OUTPUT_HEIGHT = 400;//默认输出最大高度
    private static final int DEFAULT_RATIO_X = 1;
    private static final int DEFAULT_RATIO_Y = 1;

    private CropImageView cropImageView;

    private Uri sourceUri;
    private Uri saveUri;
    private CropParams cropParams;

    private LoadCallback loadCallback;
    private CropCallback cropCallback;
    private SaveCallback saveCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        initVariable();
        initView();
    }

    protected void initView() {
        setContentView(R.layout.activity_image_crop);

        cropImageView = (CropImageView) findViewById(R.id.view_crop_image);
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnClickTopBarListener(this);
        if (cropParams != null) {
            if (cropParams.ratioX > 0 && cropParams.ratioY > 0) {
                cropImageView.setCustomRatio(cropParams.ratioX, cropParams.ratioY);
            } else {
                cropImageView.setCustomRatio(DEFAULT_RATIO_X, DEFAULT_RATIO_Y);
            }
            cropImageView.setCompressFormat(Bitmap.CompressFormat.JPEG);
            if (cropParams.compressQuality > 0 && cropParams.compressQuality <= 100) {
                cropImageView.setCompressQuality(cropParams.compressQuality);
            } else {
                cropImageView.setCompressQuality(DEFAULT_CROP_COMPRESS_QUALITY);
            }
            if (cropParams.outputMaxWidth > 0 && cropParams.outputMaxHeight > 0) {
                cropImageView.setOutputMaxSize(cropParams.outputMaxWidth, cropParams.outputMaxHeight);
            } else {
                cropImageView.setOutputMaxSize(DEFAULT_MAX_OUTPUT_WIDTH, DEFAULT_MAX_OUTPUT_HEIGHT);
            }
            cropImageView.setCropMode(cropParams.cropMode);
        } else {
            cropImageView.setCustomRatio(DEFAULT_RATIO_X, DEFAULT_RATIO_Y);
            cropImageView.setCompressQuality(DEFAULT_CROP_COMPRESS_QUALITY);
            cropImageView.setOutputMaxSize(DEFAULT_MAX_OUTPUT_WIDTH, DEFAULT_MAX_OUTPUT_HEIGHT);
            cropImageView.setCropMode(CropImageView.CropMode.FREE);
        }
        cropImageView.startLoad(sourceUri, loadCallback);
    }

    protected void initVariable() {
        sourceUri = getIntent().getParcelableExtra("source_uri");
        saveUri = getIntent().getParcelableExtra("save_uri");
        cropParams = (CropParams) getIntent().getSerializableExtra("params");
        if (sourceUri == null) {
            ToastUtil.showShort(this, R.string.common_activity_crop_image_source_uri_not_found);
            finish();
            return;
        }
        if (saveUri == null) {
            ToastUtil.showShort(this, R.string.common_activity_crop_image_save_uri_not_found);
            finish();
        }

        loadCallback = new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                ToastUtil.showShort(ImageCropActivity.this, R.string.common_activity_crop_image_load_fail);
            }
        };

        cropCallback = new CropCallback() {
            @Override
            public void onSuccess(Bitmap cropped) {
                cropImageView.setImageBitmap(cropped);
            }

            @Override
            public void onError() {
                ToastUtil.showShort(ImageCropActivity.this, R.string.common_activity_crop_image_crop_fail);
            }
        };

        saveCallback = new SaveCallback() {
            @Override
            public void onSuccess(Uri outputUri) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_SAVE_URI, outputUri);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError() {
                ToastUtil.showShort(ImageCropActivity.this, R.string.common_activity_crop_image_save_fail);
            }
        };
    }

    @Override
    public void onClickLeft() {
        finish();
    }

    @Override
    public void onClickTitle() {

    }

    @Override
    public void onClickRight() {
        cropImageView.startCrop(saveUri, cropCallback, saveCallback);
    }

    public static class CropParams implements Serializable {
        public int ratioX;//X缩放比
        public int ratioY;//Y缩放比
        public int compressQuality;//压缩质量0-100  100为最佳
        public int outputMaxWidth;//最大输出宽度
        public int outputMaxHeight;//最大输出高度
        public CropImageView.CropMode cropMode;

        public CropParams() {
        }

        public CropParams(int ratioX, int ratioY, int compressQuality, int outputMaxWidth, int outputMaxHeight, CropImageView.CropMode cropMode) {
            this.ratioX = ratioX;
            this.ratioY = ratioY;
            this.compressQuality = compressQuality;
            this.outputMaxWidth = outputMaxWidth;
            this.outputMaxHeight = outputMaxHeight;
            this.cropMode = cropMode;
        }
    }

    public static void showForResult(Activity activity, Uri sourceUri, Uri saveUri, CropParams cropParams, int requestCode) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra("source_uri", sourceUri);
        intent.putExtra("save_uri", saveUri);
        intent.putExtra("params", cropParams);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void showForResult(Fragment fragment, Uri sourceUri, Uri saveUri, CropParams cropParams, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), ImageCropActivity.class);
        intent.putExtra("source_uri", sourceUri);
        intent.putExtra("save_uri", saveUri);
        intent.putExtra("params", cropParams);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showForResult(android.app.Fragment fragment, Uri sourceUri, Uri saveUri, CropParams cropParams, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), ImageCropActivity.class);
        intent.putExtra("source_uri", sourceUri);
        intent.putExtra("save_uri", saveUri);
        intent.putExtra("params", cropParams);
        fragment.startActivityForResult(intent, requestCode);
    }
}
