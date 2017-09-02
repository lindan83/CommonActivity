package com.lance.common.activity.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.lance.common.activity.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindan on 16-11-11.
 * 大图显示辅助类
 * 支持传递多张大图
 * 可定义是否允许删除图片
 * 可接收操作结果
 * 可定义是否显示缩放图
 */

public class ImagePagerHelper {
    public static final String EXTRA_RESULT = ImagePagerActivity.INTENT_IMG_URLS;

    public static class ImagePagerParams {
        private boolean showDelete;//是否显示删除按钮
        private boolean showIndex;//是否显示索引
        private boolean showGuideView;//是否显示GuideView
        private int initPosition;//多图显示时初始化位置
        private ImagePagerActivity.ImageSize imageSize;//缩放图显示大小
        private List<String> imageUrls;//图片集合
        private boolean returnResult;//是否获取操作结果
        private int requestCode;//请求码，仅当obtainResult为true时有效
    }

    public static class ImagePagerParamsBuilder {
        private ImagePagerParams params;

        private ImagePagerParamsBuilder() {
            params = new ImagePagerParams();
        }

        public ImagePagerParamsBuilder showDelete(boolean show) {
            builder.params.showDelete = show;
            return builder;
        }

        public ImagePagerParamsBuilder showGuideView(boolean show) {
            builder.params.showGuideView = show;
            return builder;
        }

        public ImagePagerParamsBuilder showIndex(boolean show) {
            builder.params.showIndex = show;
            return builder;
        }

        public ImagePagerParamsBuilder initPosition(int position) {
            builder.params.initPosition = position;
            return builder;
        }

        public ImagePagerParamsBuilder imageSize(ImagePagerActivity.ImageSize imageSize) {
            builder.params.imageSize = imageSize;
            return builder;
        }

        public ImagePagerParamsBuilder imageList(List<String> imageList) {
            builder.params.imageUrls = imageList;
            return builder;
        }

        public ImagePagerParamsBuilder imageSingle(String imageUrl) {
            ArrayList<String> imageList = new ArrayList<>(1);
            imageList.add(imageUrl);
            return imageList(imageList);
        }

        public ImagePagerParamsBuilder returnResult(boolean returnResult) {
            builder.params.returnResult = returnResult;
            return builder;
        }

        public ImagePagerParamsBuilder requestCode(int requestCode) {
            builder.params.requestCode = requestCode;
            return builder;
        }

        /**
         * 显示大图页面
         *
         * @param object 必须为Context/Activity/Fragment之一
         */
        public void show(Object object) {
            if (!(object instanceof Activity)
                    && !(object instanceof Fragment)
                    && !(object instanceof android.support.v4.app.Fragment)
                    && !(object instanceof Context)) {
                throw new IllegalArgumentException("object must be Context or Activity or Fragment instance");
            }
            Intent intent = createIntent(object);
            if (builder.params.returnResult) {
                if (object instanceof Activity) {
                    ((Activity) object).startActivityForResult(intent, builder.params.requestCode);
                } else if (object instanceof Fragment) {
                    ((Fragment) object).startActivityForResult(intent, builder.params.requestCode);
                } else if (object instanceof android.support.v4.app.Fragment) {
                    ((android.support.v4.app.Fragment) object).startActivityForResult(intent, builder.params.requestCode);
                } else {
                    throw new IllegalArgumentException("context can not call startActivityForResult method");
                }
            } else {
                if (object instanceof Activity) {
                    ((Activity) object).startActivity(intent);
                } else if (object instanceof Fragment) {
                    ((Fragment) object).startActivity(intent);
                } else if (object instanceof android.support.v4.app.Fragment) {
                    ((android.support.v4.app.Fragment) object).startActivity(intent);
                } else {
                    ((Context) object).startActivity(intent);
                }
            }
        }

        private Intent createIntent(Object object) {
            Intent intent = null;
            if (object instanceof Activity) {
                intent = new Intent((Activity) object, ImagePagerActivity.class);
            } else if (object instanceof Fragment) {
                intent = new Intent(((Fragment) object).getActivity(), ImagePagerActivity.class);
            } else if (object instanceof android.support.v4.app.Fragment) {
                intent = new Intent(((android.support.v4.app.Fragment) object).getActivity(), ImagePagerActivity.class);
            } else if (object instanceof Context) {
                intent = new Intent((Context) object, ImagePagerActivity.class);
            }
            if (intent != null) {
                intent.putExtra(ImagePagerActivity.INTENT_IMG_URLS, new ArrayList<>(builder.params.imageUrls));
                intent.putExtra(ImagePagerActivity.INTENT_POSITION, builder.params.initPosition);
                intent.putExtra(ImagePagerActivity.INTENT_SHOW_INDEX, builder.params.showIndex);
                intent.putExtra(ImagePagerActivity.INTENT_SHOW_DELETE, builder.params.showDelete);
                intent.putExtra(ImagePagerActivity.INTENT_SHOW_GUIDE_VIEWS, builder.params.showGuideView);
                intent.putExtra(ImagePagerActivity.INTENT_IMAGE_SIZE, builder.params.imageSize);
                intent.putExtra(ImagePagerActivity.INTENT_RETURN_RESULTS, builder.params.returnResult);
            }
            return intent;
        }
    }

    private static ImagePagerParamsBuilder builder;

    public static ImagePagerParamsBuilder create() {
        builder = new ImagePagerParamsBuilder();
        return builder;
    }

    /**
     * 获取返回的操作结果
     *
     * @param resultCode resultCode
     * @param data       Intent data
     * @return 操作结果
     */
    public static List<String> getResults(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            return data.getStringArrayListExtra(ImagePagerActivity.INTENT_IMG_URLS);
        }
        return null;
    }
}
