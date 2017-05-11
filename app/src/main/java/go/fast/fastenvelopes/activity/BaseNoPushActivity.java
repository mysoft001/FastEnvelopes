package go.fast.fastenvelopes.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.interfaces.ITitleLayout;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.PreferenceUtil;

public class BaseNoPushActivity extends FragmentActivity implements ITitleLayout {

    protected TextView mTitleView;
    protected View mRightView;
    protected View mLeftView;
    protected ImageView mRefushView;
    protected View titleView;
    protected TextView mTitleBottomView;

    protected View titleMiddleLayout;


    public void back() {
        // hideSoftInput();

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public void setContentView(int layoutResID) {
        titleView = getLayoutInflater().inflate(R.layout.layout_topbar, null);

        mLeftView = titleView.findViewById(R.id.layout_title_left);
        mRightView = titleView.findViewById(R.id.layout_title_right);
        mTitleView = (TextView) titleView.findViewById(R.id.text_title);
        mTitleBottomView = (TextView) titleView.findViewById(R.id.text_title_bottom_tv);
        titleMiddleLayout = titleView.findViewById(R.id.text_title_rl);
        mRefushView = (ImageView) titleView.findViewById(R.id.refresh);
        initLeft((ImageView) mLeftView.findViewById(R.id.view_image),
                (TextView) mLeftView.findViewById(R.id.view_text), mLeftView);
        initRight((ImageView) mRightView.findViewById(R.id.view_imagea),
                (TextView) mRightView.findViewById(R.id.view_texta), mRightView);
        initMiddle(mTitleView);
        initMiddle(mTitleView, mTitleBottomView,titleMiddleLayout);
        initRefushImageView(mRefushView);
        View contentView = getLayoutInflater().inflate(layoutResID, null);
        RelativeLayout layout = new RelativeLayout(this);
        LayoutParams params = (LayoutParams) contentView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.topbar);
            params.topMargin = getResources().getDimensionPixelSize(
                    R.dimen.title_top_margin);
        }
        LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
        if (titleParams == null) {
            titleParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    PixelUtil.dp2Pixel(66, this)
                            - PreferenceUtil.getInstance(this).getInt("titleH",
                            40));
        }
        layout.addView(contentView, params);
        layout.addView(titleView, titleParams);
        layout.setClipToPadding(true);
        layout.setFitsSystemWindows(true);
        layout.setBackgroundColor(Color.parseColor("#FF7256"));
        super.setContentView(layout);

        getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
//	getWindow().addFlags(
//		WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void initLeft(ImageView imageView, TextView textView, View parent) {
        parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {

    }

    @Override
    public void initMiddle(TextView middleView) {

    }

    public void initMiddle(TextView middleView, TextView mTitleBottomView,View parentView) {

    }

    @Override
    public void initRefushImageView(ImageView imageView) {

    }

    @Override
    public void initRightFlag(ImageView imageView) {

    }

    @Override
    public void initLeftFlag(ImageView imageView) {

    }

    @Override
    public void initCenterImv(ImageView imageView) {

    }

}
