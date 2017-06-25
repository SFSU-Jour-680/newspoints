package newspoints.sfsu.com.newsp.ui.customview;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Custom TextView representation with Tags
 */
public class TagTextView extends TextView {
    private Context mContext;
    private int mTextColor;
    private int mTextUnit;
    private float mTextSize;
    private int mBackground;
    private Drawable mDrawableBackground;
    private int mLeftPadding, mRightPadding, mTopPadding, mBottomPadding;
    private ViewGroup.LayoutParams layoutParams;
    private String mText;

    public TagTextView(Context context) {
        super(context);
        mContext = context;
        //init();
    }

    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //init();
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        //init();
    }

    private TagTextView(Context mContext, TextViewBuilder textViewBuilder) {
        super(mContext);
        this.setLayoutParams(textViewBuilder.layoutParams);
        if (textViewBuilder.mBackground != 0) {
            this.setBackgroundColor(textViewBuilder.mBackground);
        } else if (textViewBuilder.mDrawableBackground != null) {
            this.setBackground(textViewBuilder.mDrawableBackground);
        }
        // try to keep in the last
        this.setPadding(textViewBuilder.mLeftPadding, textViewBuilder.mTopPadding, textViewBuilder.mRightPadding,
                textViewBuilder.mBottomPadding);
        this.setTextSize(textViewBuilder.mTextUnit, textViewBuilder.mTextSize);
        this.setTextColor(textViewBuilder.mTextColor);
        this.setText(textViewBuilder.text);


    }

    public String getmText() {
        return mText;
    }

    public void setmText(String text) {
        this.mText = text;
    }

    public int getmTextUnit() {
        return mTextUnit;
    }

    public void setmTextUnit(int mTextUnit) {
        this.mTextUnit = mTextUnit;
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public float getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getmBackground() {
        return mBackground;
    }

    public void setmBackground(int mBackground) {
        this.mBackground = mBackground;
    }

    public Drawable getmDrawableBackground() {
        return mDrawableBackground;
    }

    public void setmDrawableBackground(Drawable mDrawableBackground) {
        this.mDrawableBackground = mDrawableBackground;
    }

    public int getmLeftPadding() {
        return mLeftPadding;
    }

    public void setmLeftPadding(int mLeftPadding) {
        this.mLeftPadding = mLeftPadding;
    }

    public int getmRightPadding() {
        return mRightPadding;
    }

    public void setmRightPadding(int mRightPadding) {
        this.mRightPadding = mRightPadding;
    }

    public int getmTopPadding() {
        return mTopPadding;
    }

    public void setmTopPadding(int mTopPadding) {
        this.mTopPadding = mTopPadding;
    }

    public int getmBottomPadding() {
        return mBottomPadding;
    }

    public void setmBottomPadding(int mBottomPadding) {
        this.mBottomPadding = mBottomPadding;
    }

    /**
     * Provides Builder pattern to build custom TextView depending on the requirement
     */
    public static class TextViewBuilder {
        private final Context mContext;
        private final ViewGroup.LayoutParams layoutParams;
        private int mTextColor;
        private float mTextSize;
        private int mTextUnit;
        private int mBackground;
        private Drawable mDrawableBackground;
        private int mLeftPadding, mRightPadding, mTopPadding, mBottomPadding;
        private String text;

        public TextViewBuilder(Context mContext, ViewGroup.LayoutParams params) {
            this.mContext = mContext;
            this.layoutParams = params;
        }

        public TextViewBuilder textColor(int textColor) {
            this.mTextColor = textColor;
            return this;
        }

        public TextViewBuilder backgroundColor(int backgroundColor) {
            this.mBackground = backgroundColor;
            return this;
        }

        public TextViewBuilder backgroundDrawable(Drawable backgroundDrawable) {
            this.mDrawableBackground = backgroundDrawable;
            return this;
        }

        public TextViewBuilder padding(int left, int top, int right, int bottom) {
            this.mLeftPadding = left;
            this.mRightPadding = right;
            this.mTopPadding = top;
            this.mBottomPadding = bottom;
            return this;
        }

        public TextViewBuilder textSize(float textSize) {
            this.mTextSize = textSize;
            return this;
        }

        public TextViewBuilder textSize(int unit, float size) {
            this.mTextUnit = unit;
            this.mTextSize = size;
            return this;
        }

        public TextViewBuilder addText(String text) {
            this.text = text;
            return this;
        }

        public TagTextView build() {
            return new TagTextView(mContext, this);
        }
    }
}

