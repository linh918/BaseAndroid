package bkdev.android.base_mvp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bkdev.android.base_mvp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Setter;
import lombok.experimental.Accessors;


public class HeaderBarCustom extends LinearLayout {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;

    @BindView(R.id.imgLeft)
    ImageView mImgLeft;


    @Setter
    @Accessors(prefix = "m")
    private HeaderBarListener mHeaderBarListener;

    public HeaderBarCustom(Context context) {
        this(context, null);
    }

    public HeaderBarCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderBarCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View v = inflate(context, R.layout.custom_header_bar, this);
        ButterKnife.bind(this, v);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBarCustom);
        String title = typedArray.getString(R.styleable.HeaderBarCustom_hb_title);
        int srcImgLeft = typedArray.getResourceId(R.styleable.HeaderBarCustom_hb_src_left, 0);
        int backgroundColor = typedArray.getColor(R.styleable.HeaderBarCustom_hb_background, 0);
        typedArray.recycle();

        mTvTitle.setText(title);
        mImgLeft.setImageResource(srcImgLeft);

        setImgLeft(srcImgLeft);

        if (backgroundColor == 0) {
            v.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        } else {
            v.setBackgroundColor(backgroundColor);
        }
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }


    public void setTitleColor(int color) {
        mTvTitle.setTextColor(color);
    }

    public void setImgLeft(int srcLeft) {
        if (srcLeft != 0) {
            mImgLeft.setVisibility(VISIBLE);
            mImgLeft.setImageResource(srcLeft);
        } else {
            mImgLeft.setVisibility(GONE);
        }
    }

    @OnClick(R.id.imgLeft)
    void onClickImgLeft() {
        if (mHeaderBarListener != null) {
            mHeaderBarListener.onClickImgLeft();
        }
    }

    @OnClick(R.id.tvTitle)
    void onClickTitle() {
        if (mHeaderBarListener != null) {
            mHeaderBarListener.onClickTvTitle();
        }
    }

    public interface HeaderBarListener {
        default void onClickImgLeft() {
        }

        default void onClickTvTitle() {
        }
    }
}
