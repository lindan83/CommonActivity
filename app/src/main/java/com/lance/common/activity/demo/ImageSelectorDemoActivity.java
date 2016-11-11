package com.lance.common.activity.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lance.common.activity.MultiImageSelector;
import com.lance.common.activity.helper.ImagePagerHelper;
import com.lance.common.widget.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示多图选择
 */
public class ImageSelectorDemoActivity extends AppCompatActivity {
    private SeekBar mSbPickAmount;
    private TextView mTvPickAmount;
    private MultiImageView mIvImages;

    private List<String> mSelectResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector_demo);

        mSbPickAmount = (SeekBar) findViewById(R.id.sb_pick_amount);
        mTvPickAmount = (TextView) findViewById(R.id.tv_pick_amount);
        mIvImages = (MultiImageView) findViewById(R.id.miv_images);

        mIvImages.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImagePagerHelper.create()
                        .showGuideView(true)
                        .showIndex(true)
                        .showDelete(true)
                        .imageList(mIvImages.getList())
                        .initPosition(position)
                        .returnResult(true)
                        .requestCode(101)
                        .show(ImageSelectorDemoActivity.this);
            }
        });

        mSbPickAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mTvPickAmount.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void pickImage(View v) {
        int pickAmount = mSbPickAmount.getProgress();
        if (pickAmount > 1) {
            MultiImageSelector.create()
                    .count(pickAmount)
                    .multi()
                    .showCamera(true)
                    .start(this, 100);
        } else {
            MultiImageSelector.create()
                    .single()
                    .showCamera(true)
                    .start(this, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (result != null && !result.isEmpty()) {
                    mSelectResult = new ArrayList<>(result);
                    mIvImages.setList(mSelectResult);
                }
            }
        } else if (requestCode == 101) {
            List<String> result = ImagePagerHelper.getResults(resultCode, data);
            if (result != null) {
                mIvImages.setList(result);
            }
        }
    }
}
