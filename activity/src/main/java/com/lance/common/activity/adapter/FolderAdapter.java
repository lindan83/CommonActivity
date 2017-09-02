package com.lance.common.activity.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lance.common.activity.R;
import com.lance.common.activity.bean.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * 文件夹Adapter
 * Created by lindan
 */
public class FolderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Folder> folders = new ArrayList<>();
    private int lastSelected = 0;

    public FolderAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //int imageSize = this.context.getResources().getDimensionPixelOffset(R.dimen.multi_image_selector_folder_cover_size);
    }

    /**
     * 设置数据集
     *
     * @param folders Folders of List
     */
    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            this.folders = folders;
        } else {
            this.folders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return folders.size() + 1;
    }

    @Override
    public Folder getItem(int i) {
        if (i == 0) return null;
        return folders.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_multi_image_selector_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (i == 0) {
                holder.name.setText(R.string.multi_image_selector_folder_all);
                holder.path.setText("/sdcard");
                holder.size.setText(String.format(Locale.getDefault(), "%d%s",
                        getTotalImageSize(), context.getResources().getString(R.string.multi_image_selector_photo_unit)));
                if (folders.size() > 0) {
                    Folder f = folders.get(0);
                    if (f != null) {
                        Glide.with(context)
                                .load(new File(f.cover.path))
                                .error(R.mipmap.icon_default_error)
                                .override(context.getResources().getDimensionPixelSize(R.dimen.multi_image_selector_folder_cover_size)
                                        , context.getResources().getDimensionPixelSize(R.dimen.multi_image_selector_folder_cover_size))
                                .centerCrop()
                                .into(holder.cover);
                    } else {
                        holder.cover.setImageResource(R.mipmap.icon_default_error);
                    }
                }
            } else {
                holder.bindData(getItem(i));
            }
            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (folders != null && folders.size() > 0) {
            for (Folder f : folders) {
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView cover;
        TextView name;
        TextView path;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            path = (TextView) view.findViewById(R.id.path);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            if (data == null) {
                return;
            }
            name.setText(data.name);
            path.setText(data.path);
            if (data.images != null) {
                size.setText(String.format(Locale.getDefault(), "%d%s", data.images.size(), context.getResources().getString(R.string.multi_image_selector_photo_unit)));
            } else {
                size.setText(TextUtils.concat("*", context.getResources().getString(R.string.multi_image_selector_photo_unit)));
            }
            if (data.cover != null) {
                // 显示图片
                Glide.with(context)
                        .load(new File(data.cover.path))
                        .placeholder(R.mipmap.icon_default_error)
                        .override(context.getResources().getDimensionPixelSize(R.dimen.multi_image_selector_folder_cover_size)
                                , context.getResources().getDimensionPixelSize(R.dimen.multi_image_selector_folder_cover_size))
                        .centerCrop()
                        .into(cover);
            } else {
                cover.setImageResource(R.mipmap.icon_default_error);
            }
        }
    }
}