package ysn.com.demo.bottommenulayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

/**
 * @Author yangsanning
 * @ClassName MenuItemView
 * @Description 菜单 item
 * @Date 2020/5/24
 */
public class MenuItemView extends LinearLayout {

    private MenuItemBuilder builder;

    /**
     * 图标
     */
    private ImageView iconImageView;

    /**
     * 文本
     */
    private TextView textTextView;

    /**
     * 未读数
     */
    private TextView unreadTextView;

    /**
     * 提示消息
     */
    private TextView msgTextView;

    /**
     * 红点
     */
    private TextView msgPointTextView;

    public MenuItemView(Context context) {
        super(context);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        builder = new MenuItemBuilder(context);

        initAttrs(context, attrs);
        initView(context);
    }

    public void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);

        builder.normalIcon = typedArray.getDrawable(R.styleable.MenuItemView_miv_icon_normal);
        builder.selectedIcon = typedArray.getDrawable(R.styleable.MenuItemView_miv_icon_selected);
        builder.iconWidth = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_icon_width, 0);
        builder.iconHeight = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_icon_height, 0);

        builder.text = typedArray.getString(R.styleable.MenuItemView_miv_text);
        builder.textSize = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_text_size, builder.textSize);
        builder.textNormalColor = typedArray.getColor(R.styleable.MenuItemView_miv_text_color_normal, builder.textNormalColor);
        builder.textSelectedColor = typedArray.getColor(R.styleable.MenuItemView_miv_text_color_selected, builder.textSelectedColor);

        builder.maxUnreadNum = typedArray.getInteger(R.styleable.MenuItemView_miv_max_unread_num, builder.maxUnreadNum);
        builder.unreadTextSize = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_unread_text_size, builder.unreadTextSize);
        builder.unreadTextColor = typedArray.getColor(R.styleable.MenuItemView_miv_unread_text_color, builder.unreadTextColor);
        builder.unreadTextBg = typedArray.getDrawable(R.styleable.MenuItemView_miv_unread_text_bg);

        builder.msgTextSize = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_msg_text_size, builder.msgTextSize);
        builder.msgTextColor = typedArray.getColor(R.styleable.MenuItemView_miv_msg_text_color, builder.msgTextColor);
        builder.msgTextBg = typedArray.getDrawable(R.styleable.MenuItemView_miv_msg_text_bg);
        builder.msgPointBg = typedArray.getDrawable(R.styleable.MenuItemView_miv_msg_point_bg);

        builder.enabledClickDrawable = typedArray.getBoolean(R.styleable.MenuItemView_miv_enabled_click_drawable, builder.enabledClickDrawable);
        builder.clickDrawable = typedArray.getDrawable(R.styleable.MenuItemView_miv_click_drawable);

        builder.marginTop = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_item_margin_top, builder.marginTop);
        builder.itemPadding = typedArray.getDimensionPixelSize(R.styleable.MenuItemView_miv_item_padding, 0);

        typedArray.recycle();
    }

    public void initView(Context context) {
        builder.checkNotNull();

        View view = View.inflate(context, R.layout.view_menu_item, null);
        if (builder.itemPadding != 0) {
            view.setPadding(builder.itemPadding, builder.itemPadding, builder.itemPadding, builder.itemPadding);
        }
        iconImageView = view.findViewById(R.id.menu_item_view_icon);
        textTextView = view.findViewById(R.id.menu_item_view_text);
        unreadTextView = view.findViewById(R.id.menu_item_view_unread_num);
        msgTextView = view.findViewById(R.id.menu_item_view_msg);
        msgPointTextView = view.findViewById(R.id.menu_item_view_msg_point);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        iconImageView.setImageDrawable(builder.normalIcon);

        if (builder.iconWidth != 0 && builder.iconHeight != 0) {
            FrameLayout.LayoutParams imageLayoutParams = (FrameLayout.LayoutParams) iconImageView.getLayoutParams();
            imageLayoutParams.width = builder.iconWidth;
            imageLayoutParams.height = builder.iconHeight;
            iconImageView.setLayoutParams(imageLayoutParams);
        }

        textTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.textSize);
        textTextView.setTextColor(builder.textNormalColor);
        textTextView.setText(builder.text);
        LinearLayout.LayoutParams textLayoutParams = (LayoutParams) textTextView.getLayoutParams();
        textLayoutParams.topMargin = builder.marginTop;
        textTextView.setLayoutParams(textLayoutParams);

        unreadTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.unreadTextSize);
        unreadTextView.setTextColor(builder.unreadTextColor);
        unreadTextView.setBackground(builder.unreadTextBg);

        msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.msgTextSize);
        msgTextView.setTextColor(builder.msgTextColor);
        msgTextView.setBackground(builder.msgTextBg);

        msgPointTextView.setBackground(builder.msgPointBg);

        if (builder.enabledClickDrawable) {
            setBackground(builder.clickDrawable);
        }

        addView(view);
    }

    public ImageView getIconImageView() {
        return iconImageView;
    }

    public TextView getTextTextView() {
        return textTextView;
    }

    /**
     * 设置默认图标
     */
    public void setNormalIcon(int resId) {
        builder.normalIcon(resId);
        refreshItem();
    }

    /**
     * 设置默认图标
     */
    public void setNormalIcon(Drawable normalIcon) {
        builder.normalIcon = normalIcon;
        refreshItem();
    }

    /**
     * 设置选中图标
     */
    public void setSelectedIcon(int resId) {
        builder.selectedIcon(resId);
        refreshItem();
    }

    /**
     * 设置选中图标
     */
    public void setSelectedIcon(Drawable selectedIcon) {
        builder.selectedIcon = selectedIcon;
        refreshItem();
    }

    /**
     * 刷新Item
     */
    public void refreshItem(boolean isSelected) {
        setSelected(isSelected);
        refreshItem();
    }

    /**
     * 刷新Item
     */
    public void refreshItem() {
        iconImageView.setImageDrawable(isSelected() ? builder.selectedIcon : builder.normalIcon);
        textTextView.setTextColor(isSelected() ? builder.textSelectedColor : builder.textNormalColor);
    }

    /**
     * 获取最大未读数
     */
    public int getMaxUnreadNum() {
        return builder.maxUnreadNum;
    }

    /**
     * 设置最大未读数
     */
    public void setMaxUnreadNum(int maxUnreadNum) {
        this.builder.maxUnreadNum = maxUnreadNum;
    }

    /**
     * 设置未读数
     */
    public void setUnreadNum(int unreadNum) {
        setTextViewVisible(unreadTextView);
        if (unreadNum <= 0) {
            unreadTextView.setVisibility(GONE);
        } else if (unreadNum <= builder.maxUnreadNum) {
            unreadTextView.setText(String.valueOf(unreadNum));
        } else {
            unreadTextView.setText(String.format(Locale.CHINA, "%d+", builder.maxUnreadNum));
        }
    }

    /**
     * 显示提示消息
     */
    public void visibleMsg(String msg) {
        setTextViewVisible(msgTextView);
        msgTextView.setText(msg);
    }

    /**
     * 隐藏提示消息
     */
    public void goneMsg() {
        msgTextView.setVisibility(GONE);
    }

    /**
     * 显示小红点
     */
    public void visibleMsgPoint() {
        setTextViewVisible(msgPointTextView);
    }

    /**
     * 隐藏小红点
     */
    public void goneMsgPoint() {
        msgPointTextView.setVisibility(GONE);
    }

    /**
     * 未读、消息、小红点仅能显示一个
     */
    private void setTextViewVisible(TextView textView) {
        unreadTextView.setVisibility(GONE);
        msgTextView.setVisibility(GONE);
        msgPointTextView.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
    }

    public MenuItemView create(MenuItemBuilder builder) {
        this.builder = builder;
        this.initView(builder.context);
        return this;
    }
}
