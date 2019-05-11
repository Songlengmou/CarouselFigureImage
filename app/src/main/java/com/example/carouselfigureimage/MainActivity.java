package com.example.carouselfigureimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author admin
 * desc:轮播图
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * 轮播图Viewpager
     */
    private ViewPager BannerViewPager;
    /**
     * 轮播图标题
     */
    private TextView BannerTittle;
    private LinearLayout BannerPointLayout;
    private ArrayList<ImageView> mImageViews;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = (BannerViewPager.getCurrentItem() + 1);
            BannerViewPager.setCurrentItem(item);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    private final int[] imageIds = {
            R.drawable.d1,
            R.drawable.d2,
            R.drawable.d3,
            R.drawable.d4,
    };
    private final String[] textStrings = {
            "今夏最美",
            "欢度元宵",
            "情人节",
            "花开半夏",
    };

    /**
     * 上次高亮显示的位置
     */
    private int prePosition = 0;
    /**
     * 是否已经滚动
     */
    private boolean isDragging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BannerViewPager = findViewById(R.id.BannerViegPager);
        BannerTittle = findViewById(R.id.BannerTittle);
        BannerPointLayout = findViewById(R.id.BannerPointLayout);
        mImageViews = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView mImageView = new ImageView(this);
            mImageView.setImageResource(imageIds[i]);
            mImageViews.add(mImageView);
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);

            if (i == 0) {
                //显示红色
                point.setEnabled(true);
            } else {
                //显示灰色
                point.setEnabled(false);
                layoutParams.leftMargin = 20;
            }
            point.setLayoutParams(layoutParams);
            BannerPointLayout.addView(point);
        }
        BannerViewPager.setAdapter(new MyPagerAdapter());
        BannerTittle.setText(textStrings[prePosition]);
        //设置viewpager页面的改变
        BannerViewPager.addOnPageChangeListener(new MyOnPageChangeLister());
        //设置中间位置
        BannerViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mImageViews.size());
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    class MyOnPageChangeLister implements ViewPager.OnPageChangeListener {
        /**
         * 功能：当页面滚动时回掉这个方法
         *
         * @param position             当前页面位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 功能：当页面选中停留时回掉这个方法
         *
         * @param position 当前页面位置
         *                 返回值：空
         */
        @Override
        public void onPageSelected(int position) {
            Log.e("textStrings", "position==" + position);
            Log.e("textStrings", "position%" + "%" + imageIds.length + "==" + position % imageIds.length);
            //设置对应页面的文本信息
            BannerTittle.setText(textStrings[position % imageIds.length]);
            //把上一个高亮设置默认灰色
            BannerPointLayout.getChildAt(prePosition).setEnabled(false);
            //把当前设置为高亮红色
            BannerPointLayout.getChildAt(position % imageIds.length).setEnabled(true);
            prePosition = position % imageIds.length;
        }

        /**
         * 功能：当页面滚动状态变化的时候回掉这个方法
         * 静止->滑动
         * 滑动->静止
         * 静止->拖拽
         *
         * @param state 当前页面位置
         *              返回值：空
         */
        @Override
        public void onPageScrollStateChanged(int state) {
       /* SCROLL_STATE_IDLE：空闲状态
        SCROLL_STATE_DRAGGING：滑动状态
        SCROLL_STATE_SETTLING：滑动后自然沉降的状态*/
            if (ViewPager.SCROLL_STATE_DRAGGING == state) {
                isDragging = true;
                mHandler.removeCallbacksAndMessages(null);
                Log.e(TAG, "SCROLL_STATE_DRAGGING-----------");
            } else if (ViewPager.SCROLL_STATE_IDLE == state) {
                Log.e(TAG, "SCROLL_STATE_IDLE-----------");
            } else if (ViewPager.SCROLL_STATE_SETTLING == state && isDragging) {
                isDragging = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(0, 4000);
                Log.e(TAG, "SCROLL_STATE_SETTLING------------");
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        /**
         * 功能：
         *
         * @param container Viewpager自身
         * @param position  当前实例化页面的位置
         */
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem" + position);
            ImageView mImageView = mImageViews.get(position % imageIds.length);
            container.addView(mImageView);
            mImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.e("motionEvent", "" + motionEvent);
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.e("setOnTouchListener", "手指按下");
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.e("setOnTouchListener", "手指移动");
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e("setOnTouchListener", "手指移开");
                            mHandler.sendEmptyMessageDelayed(0, 4000);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            mImageView.setTag(position);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "点击事件");
                    int position = (int) v.getTag() % imageIds.length;
                    String text = textStrings[position];
                    Toast.makeText(MainActivity.this, "text==" + text, Toast.LENGTH_SHORT).show();
                }
            });
            return mImageView;
        }

        /**
         * 释放资源
         *
         * @param container Viewpager自身
         * @param position  释放的位置
         * @param object    释放的界面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e(TAG, "destroyItem" + position);
            container.removeView((View) object);
        }

        /**
         * 功能：得到viewPager总数
         * 无参
         * 返回值：空
         */
        @Override
        public int getCount() {
            //为了设置无限轮滑
            return Integer.MAX_VALUE;
        }

        /**
         * 比较View和 object是否同一实例
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
