package ysn.com.demo.bottommenulayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yangsanning
 * @ClassName BottomMenuLayout
 * @Description 一句话概括作用
 * @Date 2020/5/24
 */
public class BottomMenuLayout extends LinearLayout {

    private static final String BUNDLE_INSTANCE = "BUNDLE_INSTANCE";
    private static final String BUNDLE_CURRENT_POSITION = "BUNDLE_CURRENT_POSITION";

    private Bind bind = Bind.NONE;
    private int currentPosition;
    private boolean smoothScroll;
    private List<MenuItemView> menuItemViewList = new ArrayList<>();

    private OnMenuItemSelectedListener onMenuItemSelectedListener;
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
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
    };

    private ViewPager viewPager1;
    private ViewPager2 viewPager2;

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

    public void setViewPager1(ViewPager viewPager1) {
        this.bind = Bind.VIEW_PAGE_1;
        this.viewPager1 = viewPager1;
        initView();
    }

    public void setViewPager2(ViewPager2 viewPager2) {
        this.bind = Bind.VIEW_PAGE_2;
        this.viewPager2 = viewPager2;
        initView();
    }

    private void initView() {
        menuItemViewList.clear();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        // 检查个数是否一致
        checkCount(childCount);

        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof MenuItemView) {
                MenuItemView menuItemView = (MenuItemView) getChildAt(i);
                menuItemViewList.add(menuItemView);
                menuItemView.setOnClickListener(new MenuItemOnClickListener(i));
            } else {
                throw new IllegalArgumentException("BottomMenuLayout 的子 View 必须是 MenuItemView");
            }
        }

        if (currentPosition < menuItemViewList.size()) {
            menuItemViewList.get(currentPosition).refreshItem(true);
        }

        addOnPageChangeListener();
    }

    private void addOnPageChangeListener() {
        switch (bind) {
            case VIEW_PAGE_1:
                viewPager1.addOnPageChangeListener(onPageChangeListener);
                break;
            case VIEW_PAGE_2:
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        onPageChangeListener.onPageSelected(position);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 检查个数是否一致
     */
    private void checkCount(int childCount) {
        switch (bind) {
            case VIEW_PAGE_1:
                if (viewPager1.getAdapter().getCount() != childCount) {
                    throw new IllegalArgumentException("BottomMenuLayout Item 个数 于 ViewPager 子数量不一致");
                }
                break;
            case VIEW_PAGE_2:
                if (viewPager2.getAdapter().getItemCount() != childCount) {
                    throw new IllegalArgumentException("BottomMenuLayout Item 个数 于 ViewPager2 子数量不一致");
                }
                break;
            default:
                break;
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

    private class MenuItemOnClickListener implements OnClickListener {

        private int thisPosition;

        public MenuItemOnClickListener(int thisPosition) {
            this.thisPosition = thisPosition;
        }

        @Override
        public void onClick(View v) {
            switch (bind) {
                case VIEW_PAGE_1:
                    if (thisPosition == currentPosition) {
                        // 重复点击, 使用 setCurrentItem 不会回调 onPageSelected(), 所以在此处需要回调点击监听
                        if (onMenuItemSelectedListener != null) {
                            onMenuItemSelectedListener.onItemSelected(getMenuItem(thisPosition), currentPosition, thisPosition);
                        }
                    } else {
                        viewPager1.setCurrentItem(thisPosition, smoothScroll);
                    }
                    break;
                case VIEW_PAGE_2:
                    if (thisPosition == currentPosition) {
                        // 重复点击, 使用 setCurrentItem 不会回调 onPageSelected(), 所以在此处需要回调点击监听
                        if (onMenuItemSelectedListener != null) {
                            onMenuItemSelectedListener.onItemSelected(getMenuItem(thisPosition), currentPosition, thisPosition);
                        }
                    } else {
                        viewPager2.setCurrentItem(thisPosition, smoothScroll);
                    }
                    break;
                default:
                    if (onMenuItemSelectedListener != null) {
                        onMenuItemSelectedListener.onItemSelected(getMenuItem(thisPosition), currentPosition, thisPosition);
                    }
                    selectItem(thisPosition);
                    break;
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
        if (viewPager1 != null) {
            viewPager1.setCurrentItem(currentPosition, smoothScroll);
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

    private enum Bind {

        /**
         * 无绑定
         */
        NONE,

        /**
         * 绑定ViewPage1
         */
        VIEW_PAGE_1,

        /**
         * 绑定ViewPage2
         */
        VIEW_PAGE_2,
    }
}
