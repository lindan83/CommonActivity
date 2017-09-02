package com.lance.common.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 图片选择器
 * Created by lindan
 */
public class MultiImageSelector {
    public static final String EXTRA_RESULT = MultiImageSelectorActivity.EXTRA_RESULT;

    private boolean showCamera = true;
    private int maxCount = 9;
    private int mode = MultiImageSelectorActivity.MODE_MULTI;
    private ArrayList<String> originData;
    private static MultiImageSelector selector;

    private MultiImageSelector() {
    }

    public static MultiImageSelector create() {
        if (selector == null) {
            selector = new MultiImageSelector();
        }
        return selector;
    }

    public MultiImageSelector showCamera(boolean show) {
        showCamera = show;
        return selector;
    }

    public MultiImageSelector count(int count) {
        maxCount = count;
        return selector;
    }

    public MultiImageSelector single() {
        mode = MultiImageSelectorActivity.MODE_SINGLE;
        return selector;
    }

    public MultiImageSelector multi() {
        mode = MultiImageSelectorActivity.MODE_MULTI;
        return selector;
    }

    public MultiImageSelector origin(ArrayList<String> images) {
        originData = images;
        return selector;
    }

    public void start(Activity activity, int requestCode) {
        if (hasPermission(activity)) {
            activity.startActivityForResult(createIntent(activity), requestCode);
        } else {
            Toast.makeText(activity, R.string.multi_image_selector_error_no_permission, Toast.LENGTH_SHORT).show();
        }
    }

    public void start(Fragment fragment, int requestCode) {
        final Context context = fragment.getContext();
        if (hasPermission(context)) {
            fragment.startActivityForResult(createIntent(context), requestCode);
        } else {
            Toast.makeText(context, R.string.multi_image_selector_error_no_permission, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermission(Context context) {
        // Permission was added in API Level 16
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private Intent createIntent(Context context) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxCount);
        if (originData != null) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, originData);
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mode);
        return intent;
    }
}