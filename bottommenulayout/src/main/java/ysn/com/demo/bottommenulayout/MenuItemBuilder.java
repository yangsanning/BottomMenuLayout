package ysn.com.demo.bottommenulayout;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @Author yangsanning
 * @ClassName MenuItemBuilder
 * @Description 菜单 item 的 builder
 * @Date 2020/5/24
 */
public class MenuItemBuilder {

    Context context;

    /**
     * normalIcon： 默认图标
     * selectedIcon： 选中图标
     * iconWidth： 图标的宽
     * iconHeight： 图标的高
     */
    Drawable normalIcon;
    Drawable selectedIcon;
    int iconWidth;
    int iconHeight;

    /**
     * text: 文本
     * textSize: 文本字体大小
     * textNormalColor: 文本默认颜色
     * textSelectedColor: 文本的选中颜色
     */
    String text;
    int textSize;
    int textNormalColor;
    int textSelectedColor;

    /**
     * maxUnreadNum: 最大显示未读数
     * unreadTextSize: 未读数字体大小
     * unreadTextColor: 未读数字体颜色
     * unreadTextBg: 未读数字体背景
     */
    int maxUnreadNum;
    int unreadTextSize;
    int unreadTextColor;
    Drawable unreadTextBg;

    /**
     * msgTextSize: 提示消息默认字体大小
     * msgTextColor: 提示消息文字颜色
     * msgTextBg: 提示消息文字背景
     * msgPointBg: 小红点背景
     */
    int msgTextSize;
    int msgTextColor;
    Drawable msgTextBg;
    Drawable msgPointBg;

    /**
     * enabledClickDrawable: 是否启用点击效果
     * clickDrawable: 点击效果
     */
    boolean enabledClickDrawable = false;
    Drawable clickDrawable;

    /**
     * marginTop: 文字和图标的距离,默认0dp
     * itemPadding: item 的 padding
     */
    int marginTop = 0;
    int itemPadding;

    public MenuItemBuilder(Context context) {
        this.context = context;
        textSize = sp2px(12);
        textNormalColor = getColor(R.color.menu_item_view_text_normal_color);
        textSelectedColor = getColor(R.color.menu_item_view_text_selected_color);

        maxUnreadNum = 99;
        unreadTextSize = sp2px(10);
        unreadTextColor = getColor(R.color.menu_item_view_text_msg_color);

        msgTextSize = sp2px(7);
        msgTextColor = getColor(R.color.menu_item_view_text_msg_color);
    }

    /**
     * 设置默认图标
     */
    public MenuItemBuilder normalIcon(int normalIconResId) {
        return normalIcon(getDrawable(normalIconResId));
    }

    /**
     * 设置默认图标
     */
    public MenuItemBuilder normalIcon(Drawable resId) {
        this.normalIcon = resId;
        return this;
    }

    /**
     * 设置选中图标
     */
    public MenuItemBuilder selectedIcon(int resId) {
        return selectedIcon(getDrawable(resId));
    }

    /**
     * 设置选中图标
     */
    public MenuItemBuilder selectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
        return this;
    }

    /**
     * 图标宽度
     */
    public MenuItemBuilder iconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
        return this;
    }

    /**
     * 图标高度
     */
    public MenuItemBuilder iconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
        return this;
    }

    /**
     * 设置标题
     */
    public MenuItemBuilder text(int textId) {
        return text(context.getString(textId));
    }

    /**
     * 设置标题
     */
    public MenuItemBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * 设置字体大小
     */
    public MenuItemBuilder textSize(int textSize) {
        this.textSize = sp2px(textSize);
        return this;
    }

    /**
     * 设置文本的默认颜色
     */
    public MenuItemBuilder textNormalColor(int textNormalColor) {
        this.textNormalColor = getColor(textNormalColor);
        return this;
    }

    /**
     * 设置文本的选中颜色
     */
    public MenuItemBuilder textSelectedColor(int textSelectedColor) {
        this.textSelectedColor = getColor(textSelectedColor);
        return this;
    }

    /**
     * 设置文字和图标的距离
     */
    public MenuItemBuilder marginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    /**
     * 是否是否启用点击效果
     */
    public MenuItemBuilder enabledClickDrawable(boolean enabledClickDrawable) {
        this.enabledClickDrawable = enabledClickDrawable;
        return this;
    }

    /**
     * 设置点击效果
     */
    public MenuItemBuilder clickDrawable(Drawable clickDrawable) {
        this.clickDrawable = clickDrawable;
        return this;
    }

    /**
     * item 的 padding
     */
    public MenuItemBuilder itemPadding(int itemPadding) {
        this.itemPadding = itemPadding;
        return this;
    }

    /**
     * 设置未读数字体大小
     */
    public MenuItemBuilder unreadTextSize(int unreadTextSize) {
        this.unreadTextSize = sp2px(unreadTextSize);
        return this;
    }

    /**
     * 设置最大显示未读数
     */
    public MenuItemBuilder maxUnreadNum(int maxUnreadNum) {
        this.maxUnreadNum = maxUnreadNum;
        return this;
    }

    /**
     * 设置提示消息默认字体大小
     */
    public MenuItemBuilder msgTextSize(int msgTextSize) {
        this.msgTextSize = sp2px(msgTextSize);
        return this;
    }

    /**
     * 设置未读数字体背景
     */
    public MenuItemBuilder unreadTextBg(Drawable unreadTextBg) {
        this.unreadTextBg = unreadTextBg;
        return this;
    }

    /**
     * 设置未读数字体颜色
     */
    public MenuItemBuilder unreadTextColor(int unreadTextColor) {
        this.unreadTextColor = getColor(unreadTextColor);
        return this;
    }

    /**
     * 设置消息文字颜色
     */
    public MenuItemBuilder msgTextColor(int msgTextColor) {
        this.msgTextColor = getColor(msgTextColor);
        return this;
    }

    /**
     * 设置消息文字背景
     */
    public MenuItemBuilder msgTextBg(Drawable msgTextBg) {
        this.msgTextBg = msgTextBg;
        return this;
    }

    /**
     * 设置小红点背景
     */
    public MenuItemBuilder msgPointBg(Drawable msgPointBg) {
        this.msgPointBg = msgPointBg;
        return this;
    }

    /**
     * 排空
     */
    public void checkNotNull() {
        if (normalIcon == null) {
            throw new NullPointerException("默认图标不能为空!");
        }

        if (selectedIcon == null) {
            throw new NullPointerException("选中图标不能为空");
        }

        if (enabledClickDrawable && clickDrawable == null) {
            // 如果有开启触摸背景效果但是没有传对应的drawable
            throw new IllegalStateException("点击背景不能为空");
        }

        if (unreadTextBg == null) {
            unreadTextBg = context.getResources().getDrawable(R.drawable.bg_unread_text);
        }

        if (msgTextBg == null) {
            msgTextBg = context.getResources().getDrawable(R.drawable.bg_msg_text);
        }

        if (msgPointBg == null) {
            msgPointBg = context.getResources().getDrawable(R.drawable.bg_msg_point);
        }
    }

    /**
     * 动态创建MenuItemView
     */
    public MenuItemView create() {
        return new MenuItemView(context).create(this);
    }

    private int getColor(int colorId) {
        return context.getResources().getColor(colorId);
    }

    private int sp2px(float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private Drawable getDrawable(int resId) {
        return context.getResources().getDrawable(resId);
    }
}

