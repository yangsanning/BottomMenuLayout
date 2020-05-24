package ysn.com.demo.bottommenulayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yangsanning
 * @ClassName BottomMenuLayout
 * @Description 一句话概括作用
 * @Date 2020/5/24
 */
public class BottomMenuLayout extends LinearLayout implements ViewPager.OnPageChangeListener {

    private static final String BUNDLE_INSTANCE = "BUNDLE_INSTANCE";
    private static final String BUNDLE_CURRENT_POSITION = "BUNDLE_CURRENT_POSITION";

    private int currentPosition;
    private boolean smoothScroll;
    private List<MenuItemView> menuItemViewList = new ArrayList<>();

    private OnMenuItemSelectedListener onMenuItemSelectedListener;

    private ViewPager viewPager;

    public BottomMenuLayout(Context context) {
        this(context, null);
    }

    public BottomMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomMenuLayout);

        smoothScroll = typedArray.getBoolean(R.styleable.BottomMenuLayout_bml_smooth_scroll, false);

        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        initView();
    }

    private void initView() {
        menuItemViewList.clear();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        if (viewPager != null) {
            if (viewPager.getAdapter().getCount() != childCount) {
                throw new IllegalArgumentException("BottomMenuLayout Item 个数 于 ViewPager 子数量不一致");
            }
        }

        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof MenuItemView) {
                MenuItemView menuItemView = (MenuItemView) getChildAt(i);
                menuItemViewList.add(menuItemView);
                // 设置点击监听
                menuItemView.setOnClickListener(new MenuItemOnClickListener(i));
            } else {
                throw new IllegalArgumentException("BottomMenuLayout 的子 View 必须是 MenuItemView");
            }
        }

        if (currentPosition < menuItemViewList.size()) {
            menuItemViewList.get(currentPosition).refreshItem(true);
        }

        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
        }
    }

    /**
     * 增加Item
     */
    public void addItem(MenuItemView menuItemView) {
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        menuItemView.setLayoutParams(layoutParams);
        addView(menuItemView);
        initView();
    }

    /**
     * 根据索引移除Item
     */
    public void removeItem(int position) {
        if (position >= 0 && position < menuItemViewList.size()) {
            MenuItemView item = menuItemViewList.get(position);
            if (menuItemViewList.contains(item)) {
                resetCurrentItem();
                removeViewAt(position);
                initView();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetCurrentItem();
        menuItemViewList.get(position).refreshItem(true);
        if (onMenuItemSelectedListener != null) {
            onMenuItemSelectedListener.onItemSelected(getMenuItem(position), currentPosition, position);
        }
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MenuItemOnClickListener implements OnClickListener {

        private int thisPosition;

        public MenuItemOnClickListener(int thisPosition) {
            this.thisPosition = thisPosition;
        }

        @Override
        public void onClick(View v) {
            if (viewPager != null) {
                if (thisPosition == currentPosition) {
                    // 重复点击, 使用 setCurrentItem 不会回调 onPageSelected(), 所以在此处需要回调点击监听
                    if (onMenuItemSelectedListener != null) {
                        onMenuItemSelectedListener.onItemSelected(getMenuItem(thisPosition), currentPosition, thisPosition);
                    }
                } else {
                    viewPager.setCurrentItem(thisPosition, smoothScroll);
                }
            } else {
                if (onMenuItemSelectedListener != null) {
                    onMenuItemSelectedListener.onItemSelected(getMenuItem(thisPosition), currentPosition, thisPosition);
                }
                selectItem(thisPosition);
            }
        }
    }

    /**
     * 选中 Item
     */
    private void selectItem(int position) {
        resetCurrentItem();
        currentPosition = position;
        menuItemViewList.get(currentPosition).refreshItem(true);
    }

    /**
     * 重置当前选中 Item 的状态
     */
    private void resetCurrentItem() {
        if (currentPosition < menuItemViewList.size()) {
            menuItemViewList.get(currentPosition).refreshItem(false);
        }
    }

    /**
     * 设置当前选中
     */
    public void setCurrentPosition(int currentPosition) {
        if (viewPager != null) {
            viewPager.setCurrentItem(currentPosition, smoothScroll);
        } else {
            if (onMenuItemSelectedListener != null) {
                onMenuItemSelectedListener.onItemSelected(getMenuItem(currentPosition), this.currentPosition, currentPosition);
            }
            selectItem(currentPosition);
        }
    }

    /**
     * 设置未读数
     */
    public void setUnreadNum(int position, int unreadNum) {
        menuItemViewList.get(position).setUnreadNum(unreadNum);
    }

    /**
     * 显示提示消息
     */
    public void visibleMsg(int position, String msg) {
        menuItemViewList.get(position).visibleMsg(msg);
    }

    /**
     * 隐藏提示消息
     */
    public void goneMsg(int position) {
        menuItemViewList.get(position).goneMsg();
    }

    /**
     * 显示小红点
     */
    public void visibleMsgPoint(int position) {
        menuItemViewList.get(position).visibleMsgPoint();
    }

    /**
     * 隐藏小红点
     */
    public void goneMsgPoint(int position) {
        menuItemViewList.get(position).goneMsgPoint();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }

    public MenuItemView getMenuItem(int position) {
        return menuItemViewList.get(position);
    }

    /**
     * 当 View 被销毁的时候, 保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(BUNDLE_CURRENT_POSITION, currentPosition);
        return bundle;
    }

    /**
     * 恢复数据
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentPosition = bundle.getInt(BUNDLE_CURRENT_POSITION);
            // 重置所有按钮状态
            resetCurrentItem();
            // 恢复点击的条目颜色
            menuItemViewList.get(currentPosition).refreshItem(true);
            super.onRestoreInstanceState(bundle.getParcelable(BUNDLE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener onMenuItemSelectedListener) {
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }

    public interface OnMenuItemSelectedListener {

        /**
         * 选中回调
         *
         * @param menuItemView     item view
         * @param previousPosition 上一个选中
         * @param currentPosition  当前选中
         */
        void onItemSelected(MenuItemView menuItemView, int previousPosition, int currentPosition);
    }
}
