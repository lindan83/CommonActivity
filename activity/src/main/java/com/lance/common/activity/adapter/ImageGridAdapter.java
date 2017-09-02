package com.lance.common.activity.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lance.common.activity.R;
import com.lance.common.activity.bean.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 * Created by lindan
 */
public class ImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context context;

    private LayoutInflater inflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> images = new ArrayList<>();
    private List<Image> selectedImages = new ArrayList<>();

    private final int gridWidth;

    public ImageGridAdapter(Context context, boolean showCamera, int column) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width;
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        width = size.x;
        gridWidth = width / column;
    }

    /**
     * 显示选择指示器
     *
     * @param b boolean
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     *
     * @param image Image
     */
    public void select(Image image) {
        if (selectedImages.contains(image)) {
            selectedImages.remove(image);
        } else {
            selectedImages.add(image);
        }
        notifyDataSetChanged();
    }

    /**
     * 通过图片路径设置默认选择
     *
     * @param resultList 默认选择列表
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                selectedImages.add(image);
            }
        }
        if (selectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path) {
        if (images != null && images.size() > 0) {
            for (Image image : images) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     *
     * @param images Images of List
     */
    public void setData(List<Image> images) {
        selectedImages.clear();

        if (images != null && images.size() > 0) {
            this.images = images;
        } else {
            this.images.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? images.size() + 1 : images.size();
    }

    @Override
    public Image getItem(int i) {
        if (showCamera) {
            if (i == 0) {
                return null;
            }
            return images.get(i - 1);
        } else {
            return images.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (isShowCamera()) {
            if (i == 0) {
                view = inflater.inflate(R.layout.item_list_multi_image_selector_camera, viewGroup, false);
                return view;
            }
        }

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_multi_image_selector_image, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (holder != null) {
            holder.bindData(getItem(i));
        }

        return view;
    }

    private class ViewHolder {
        ImageView image;
        ImageView indicator;
        View mask;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;
            // 处理单选和多选状态
            if (showSelectIndicator) {
                indicator.setVisibility(View.VISIBLE);
                if (selectedImages.contains(data)) {
                    // 设置选中状态
                    indicator.setImageResource(R.mipmap.icon_btn_selected);
                    mask.setVisibility(View.VISIBLE);
                } else {
                    // 未选择
                    indicator.setImageResource(R.mipmap.icon_btn_unselected);
                    mask.setVisibility(View.GONE);
                }
            } else {
                indicator.setVisibility(View.GONE);
            }
            File imageFile = new File(data.path);
            if (imageFile.exists()) {
                // 显示图片
                Glide.with(context)
                        .load(imageFile)
                        .placeholder(R.mipmap.icon_default_error)
                        .override(gridWidth, gridWidth)
                        .centerCrop()
                        .into(image);
            } else {
                image.setImageResource(R.mipmap.icon_default_error);
            }
        }
    }
}