package com.lance.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lance.common.widget.photoview.PhotoView;
import com.lance.common.widget.photoview.PhotoViewAttacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 大图查看页面
 */
public class ImagePagerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String INTENT_IMG_URLS = "img_urls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGE_SIZE = "image_size";
    public static final String INTENT_SHOW_DELETE = "show_delete";
    public static final String INTENT_SHOW_GUIDE_VIEWS = "show_guide_views";
    public static final String INTENT_SHOW_INDEX = "show_index";
    public static final String INTENT_RETURN_RESULTS = "return_results";

    private ViewPager viewPager;
    //索引相关
    private TextView tvIndex;
    private TextView tvTotalCount;

    //Guide Views相关
    private List<View> guideViewList = new ArrayList<>();

    public ImageSize imageSize;//缩略图大小
    private int startPos;//第一张显示的位置
    private boolean showDelete;//是否显示删除
    private boolean showGuideViews;//是否显示Guide View
    private boolean showIndex;//是否显示索引
    private boolean returnResults;//是否返回操作结果
    private ArrayList<String> imgUrls = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ViewPager.OnPageChangeListener onPagerChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        initVariable();
        initView();
    }

    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        LinearLayout llIndexViews = (LinearLayout) findViewById(R.id.ll_index_views);
        tvIndex = (TextView) findViewById(R.id.tv_index);
        tvTotalCount = (TextView) findViewById(R.id.tv_total_count);
        View ivDelete = findViewById(R.id.iv_delete);
        LinearLayout guideGroup = (LinearLayout) findViewById(R.id.ll_guide_group);

        ivDelete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
        ivDelete.setOnClickListener(showDelete ? this : null);

        if (showIndex) {
            llIndexViews.setVisibility(View.VISIBLE);
            tvIndex.setText(String.valueOf(startPos + 1));
            tvTotalCount.setText(String.valueOf(imgUrls.size()));
        } else {
            llIndexViews.setVisibility(View.GONE);
        }
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(startPos);

        if (showGuideViews) {
            guideGroup.setVisibility(View.VISIBLE);
            addGuideView(guideGroup, startPos, imgUrls);
            viewPager.addOnPageChangeListener(onPagerChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tvIndex.setText(String.valueOf(position + 1));
                    startPos = position;
                    for (int i = 0; i < guideViewList.size(); i++) {
                        guideViewList.get(i).setSelected(i == position);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            guideGroup.setVisibility(View.GONE);
            guideGroup.removeAllViews();
            viewPager.removeOnPageChangeListener(onPagerChangeListener);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.iv_delete) {
            //删除
            deleteImage();
        }
    }


    private void deleteImage() {
        imgUrls.remove(startPos);
        viewPager.setAdapter(null);
        imageAdapter.setData(imgUrls);
        viewPager.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
        if (startPos >= imgUrls.size()) {
            startPos = imgUrls.size() - 1;
        }
        viewPager.setCurrentItem(startPos);
        tvIndex.setText(String.valueOf(startPos + 1));
        tvTotalCount.setText(String.valueOf(imgUrls.size()));

        if (imgUrls.isEmpty()) {
            makeImagesReturn();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (returnResults) {
            makeImagesReturn();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (returnResults) {
                makeImagesReturn();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void makeImagesReturn() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(INTENT_IMG_URLS, imgUrls);
        setResult(RESULT_OK, intent);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        //第一张显示的位置
        startPos = intent.getIntExtra(INTENT_POSITION, 1);
        //图片集合
        imgUrls.addAll(intent.getStringArrayListExtra(INTENT_IMG_URLS));
        //缩略图大小
        imageSize = (ImageSize) intent.getSerializableExtra(INTENT_IMAGE_SIZE);
        //是否显示删除
        showDelete = intent.getBooleanExtra(INTENT_SHOW_DELETE, false);
        //是否显示Guide View
        showGuideViews = intent.getBooleanExtra(INTENT_SHOW_GUIDE_VIEWS, true);
        ////是否显示索引
        showIndex = intent.getBooleanExtra(INTENT_SHOW_INDEX, true);
        //是否返回操作结果
        returnResults = intent.getBooleanExtra(INTENT_RETURN_RESULTS, false);
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.image_pager_selector_guide);
                view.setSelected(i == startPos);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.image_pager_guide_view_width),
                        getResources().getDimensionPixelSize(R.dimen.image_pager_guide_view_height));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    protected void initVariable() {
        imageAdapter = new ImageAdapter(this);
        imageAdapter.setData(imgUrls);
        imageAdapter.setImageSize(imageSize);

        getIntentData();
    }

    private static class ImageAdapter extends PagerAdapter {
        private List<String> data = new ArrayList<>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;

        public void setData(List<String> data) {
            if (data != null)
                this.data = data;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (data == null) return 0;
            return data.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
                final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv_image);
                imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        ((Activity) context).finish();
                    }
                });

                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.width, imageSize.height);
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                final String imgUrl = data.get(position);

                Glide.with(context)
                        .load(imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.mipmap.icon_notpic)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                            }
                        });

                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    public static class ImageSize implements Serializable {
        public int width;
        public int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
