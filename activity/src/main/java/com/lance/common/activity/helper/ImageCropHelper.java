package com.lance.common.activity.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.isseiaoki.simplecropview.CropImageView;
import com.lance.common.activity.ImageCropActivity;

import java.io.File;
import java.io.IOException;

/**
 * 图片裁剪辅助工具
 */
public class ImageCropHelper {
    /**
     * 图片裁切参数构建工具类
     */
    public static class ImageCropParamsBuilder {
        private ImageCropActivity.CropParams cropParams;

        private ImageCropParamsBuilder() {
            cropParams = new ImageCropActivity.CropParams();
        }

        public static ImageCropParamsBuilder create() {
            return new ImageCropParamsBuilder();
        }

        public ImageCropParamsBuilder ratio(int ratioX, int ratioY) {
            cropParams.ratioX = ratioX;
            cropParams.ratioY = ratioY;
            return this;
        }

        public ImageCropParamsBuilder compressQuality(int compressQuality) {
            cropParams.compressQuality = compressQuality;
            return this;
        }

        public ImageCropParamsBuilder outputMaxSize(int maxWidth, int maxHeight) {
            cropParams.outputMaxWidth = maxWidth;
            cropParams.outputMaxHeight = maxHeight;
            return this;
        }

        public ImageCropParamsBuilder cropMode(CropImageView.CropMode cropMode) {
            cropParams.cropMode = cropMode;
            return this;
        }

        public ImageCropActivity.CropParams build() {
            return cropParams;
        }
    }

    /**
     * 裁切指定路径下的图片为头像
     *
     * @param activity    Activity
     * @param path        路径
     * @param requestCode 请求码
     */
    public static void startCropAvatar(Activity activity, String path, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        ImageCropActivity.showForResult(activity, fileUri, saveUri, new ImageCropActivity.CropParams(1, 1, 100, 300, 300, CropImageView.CropMode.CIRCLE), requestCode);
    }

    /**
     * 裁切指定路径下的图片为头像
     *
     * @param activity    Activity
     * @param path        路径
     * @param params      裁切参数
     * @param requestCode 请求码
     */
    public static void startCropAvatar(Activity activity, String path, ImageCropActivity.CropParams params, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        ImageCropActivity.showForResult(activity, fileUri, saveUri, params, requestCode);
    }

    /**
     * 裁切指定路径下的图片为头像
     *
     * @param fragment    Fragment
     * @param path        路径
     * @param requestCode 请求码
     */
    public static void startCropAvatar(Fragment fragment, String path, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        ImageCropActivity.showForResult(fragment, fileUri, saveUri, new ImageCropActivity.CropParams(1, 1, 100, 300, 300, CropImageView.CropMode.CIRCLE), requestCode);
    }

    /**
     * 裁切指定路径下的图片为头像
     *
     * @param fragment    Fragment
     * @param path        路径
     * @param params      裁切参数
     * @param requestCode 请求码
     */
    public static void startCropAvatar(Fragment fragment, String path, ImageCropActivity.CropParams params, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        ImageCropActivity.showForResult(fragment, fileUri, saveUri, params, requestCode);
    }

    /**
     * 裁切指定路径下的图片为头像
     *
     * @param fragment    Fragment
     * @param path        路径
     * @param requestCode 请求码
     */
    public static void startCropAvatar(android.app.Fragment fragment, String path, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        ImageCropActivity.showForResult(fragment, fileUri, saveUri, new ImageCropActivity.CropParams(1, 1, 100, 300, 300, CropImageView.CropMode.CIRCLE), requestCode);
    }

    /**
     * 裁切指定路径下的图片为头像
     *
     * @param fragment    Fragment
     * @param path        路径
     * @param params      裁切参数
     * @param requestCode 请求码
     */
    public static void startCropAvatar(android.app.Fragment fragment, String path, ImageCropActivity.CropParams params, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        ImageCropActivity.showForResult(fragment, fileUri, saveUri, params, requestCode);
    }

    /**
     * 自由裁切指定路径下的图片
     *
     * @param object      context
     * @param path        路径
     * @param params      裁切参数
     * @param requestCode 请求码
     */
    public static void startCropFree(Object object, String path, ImageCropActivity.CropParams params, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int dotIndex = path.lastIndexOf(".");
        int nameIndex = path.lastIndexOf("/") + 1;
        String suffix = path.substring(path.lastIndexOf("."));
        String fileName = path.substring(nameIndex, dotIndex) + "_crop";
        String savePath = path.substring(0, nameIndex) + fileName + suffix;
        Uri fileUri = Uri.fromFile(new File(path));
        Uri saveUri = Uri.fromFile(new File(savePath));
        if (object instanceof Activity) {
            ImageCropActivity.showForResult((Activity) object, fileUri, saveUri, params, requestCode);
        } else if (object instanceof Fragment) {
            ImageCropActivity.showForResult((Fragment) object, fileUri, saveUri, params, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ImageCropActivity.showForResult((android.app.Fragment) object, fileUri, saveUri, params, requestCode);
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return 旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param degree 角度
     * @param bitmap 位圖
     * @return Bitmap 旋转后的位图
     */
    public static Bitmap rotateImageView(int degree, Bitmap bitmap) {
        //旋转图片
        if (degree == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 处理图片旋转
     *
     * @param path 路径
     * @return 旋转后的结果
     */
    public static Bitmap handleImageRotation(String path) {
        int degree = readPictureDegree(path);
        if (degree == 0) {
            return null;
        }
        Bitmap srcBitmap = BitmapFactory.decodeFile(path);
        return rotateImageView(degree, srcBitmap);
    }

    /**
     * 用于裁切图片后向Activity或Fragment返回结果
     *
     * @param resultCode 结果码，只有RESULT_OK才表示正确裁切
     * @param data       返回的数据，内含图片URI
     * @return 图片URI
     */
    public static Uri onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            return data.getParcelableExtra(ImageCropActivity.RESULT_SAVE_URI);
        }
        return null;
    }
}