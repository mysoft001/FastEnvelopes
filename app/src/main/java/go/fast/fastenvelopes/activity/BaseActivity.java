package go.fast.fastenvelopes.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.interfaces.ITitleLayout;
import go.fast.fastenvelopes.receiver.NetBroadcastReceiver;
import go.fast.fastenvelopes.receiver.PushMsgOperating;
import go.fast.fastenvelopes.service.PushIntentService;
import go.fast.fastenvelopes.utils.NetUtlis;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.ToolUtils;


public class BaseActivity extends FragmentActivity implements ITitleLayout,
		NetBroadcastReceiver.NetEvevt {

    protected TextView mTitleView;
	protected TextView mTitleBottomView;
    protected View mRightView;
    protected View mLeftView;
    protected ImageView mRefushView;
    protected View titleView;



    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    private int netMobile;

    public void back() {
	// hideSoftInput();

	finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	evevt = this;
	inspectNet();
    }

    @SuppressLint("NewApi")
    @Override
    public void setContentView(int layoutResID) {
	titleView = getLayoutInflater().inflate(R.layout.layout_topbar, null);
	mLeftView = titleView.findViewById(R.id.layout_title_left);
	mRightView = titleView.findViewById(R.id.layout_title_right);
		mTitleBottomView = (TextView) titleView.findViewById(R.id.text_title_bottom_tv);

	mTitleView = (TextView) titleView.findViewById(R.id.text_title);
	mRefushView = (ImageView) titleView.findViewById(R.id.refresh);
	initLeft((ImageView) mLeftView.findViewById(R.id.view_image),
		(TextView) mLeftView.findViewById(R.id.view_text), mLeftView);
	initRight((ImageView) mRightView.findViewById(R.id.view_imagea),
		(TextView) mRightView.findViewById(R.id.view_texta), mRightView);
	initMiddle(mTitleView);

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
		//layout.setBackgroundColor(Color.parseColor("#79CDCD"));
	layout.setBackgroundResource(R.color.title_tab_bg_color);
	super.setContentView(layout);

	getWindow()
		.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	// 透明导航栏
	// getWindow().addFlags(
	// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	IntentFilter filter = new IntentFilter();
	filter.addAction(PushIntentService.SEND_ATTENTION_ACTION);
	registerReceiver(receiver, filter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	setIntent(intent);
    }

    protected void setRefushImage() {
//	titleView.post(new Runnable() {
//	    @Override
//	    public void run() {
//		rotateTool.refreshingImpl();
//	    }
//	});
//	// 当无任何返回时超过30秒自动
//	titleView.postDelayed(new Runnable() {
//
//	    @Override
//	    public void run() {
//		if (rotateTool != null) {
//		    rotateTool.resetImpl();
//		}
//	    }
//	}, 30000);
    }

    protected void removeRefushImageView() {
//	if (rotateTool != null) {
//	    rotateTool.resetImpl();
//	}
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	unregisterReceiver(receiver);
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
	parent.setOnClickListener(new View.OnClickListener() {
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

    BroadcastReceiver receiver = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {

	    if (intent.getAction().equals(PushIntentService.SEND_ATTENTION_ACTION)) {
		PushMsgInfo pushMsgInfo = (PushMsgInfo) intent
			.getSerializableExtra("content");
		PushMsgOperating.pushMsgDoing(BaseActivity.this, pushMsgInfo);
	    }
		else
			if(intent.getAction().equals(PushIntentService.RECEIVE_MSG))
		{

			ToolUtils.showToast(BaseActivity.this,"  BaseActivity 收到 一条广播   ");
			PushMsgInfo pushMsgInfo = (PushMsgInfo) intent
					.getSerializableExtra("content");
			PushMsgOperating.pushMsgDoing(BaseActivity.this, pushMsgInfo);
		}

	}
    };

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
	this.netMobile = NetUtlis.getNetWorkState(BaseActivity.this);
	return isNetConnect();

    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
	// TODO Auto-generated method stub
	this.netMobile = netMobile;
	isNetConnect();

    }

    /**
     * 判断有无网络 。
     * 
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
	if (netMobile == 1) {
	    return true;
	} else if (netMobile == 0) {
	    return true;
	} else if (netMobile == -1) {
	    return false;

	}
	return false;
    }
}
