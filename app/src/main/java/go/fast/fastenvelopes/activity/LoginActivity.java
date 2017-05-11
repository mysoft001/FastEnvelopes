package go.fast.fastenvelopes.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DataUtils;
import go.fast.fastenvelopes.utils.FileUtils;
import go.fast.fastenvelopes.utils.ShareUtils;
import go.fast.fastenvelopes.utils.ToolUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";

//    private PasteEditText accountEdit;
//    private PasteEditText psdEdit;

   // private Button loginBtn;



    private Dialog pd;// 添加loading条

    private boolean isRelogin;


    private Button QQloginBtn;

    private Button WXloginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.login_layout);
	initView();
    }

    private void initView() {

	isRelogin = getIntent().getBooleanExtra("relogin", true);
	if (isRelogin) {
	    // leftView.setVisibility(View.GONE);
	    middleView.setText("账号登录");
	}

	pd = ToolUtils.createLoadingDialogCanCancel(this, "正在登录请稍后");


	QQloginBtn = (Button) findViewById(R.id.qq_login_btn);
	WXloginBtn = (Button) findViewById(R.id.weixin_login_btn);

	QQloginBtn.setOnClickListener(this);
	WXloginBtn.setOnClickListener(this);

    }

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {
	super.initRight(imageView, textView, parent);
	// parent.setVisibility(View.VISIBLE);
	// textView.setVisibility(View.VISIBLE);
	// textView.setText("登录");
	// parent.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// login();
	// }
	// });
    }

    private View leftView;
    private TextView middleView;

    private long exitTime;

    @Override
    public void initLeft(ImageView imageView, TextView textView, View parent) {
	// TODO Auto-generated method stub
	super.initLeft(imageView, textView, parent);
	leftView = parent;
	if (isRelogin) {
	    leftView.setVisibility(View.GONE);
	}
    }

    @Override
    public void initMiddle(TextView middleView) {
	super.initMiddle(middleView);
	middleView.setText("账号切换");
	this.middleView = middleView;
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    //
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    //
    // System.out.println("     "+pd.isShowing()+"    "+isRelogin);
    // if (isRelogin) {
    //
    // // if (event.getAction() == KeyEvent.ACTION_DOWN) {
    // // if ((System.currentTimeMillis() - exitTime) > 2000) {
    // //
    // // Toast.makeText(getApplicationContext(), "再按一次退出程序",
    // // Toast.LENGTH_SHORT).show();
    // // exitTime = System.currentTimeMillis();
    // // } else {
    // // finish();
    // // System.exit(0);
    // // }
    // // return true;
    // // }
    //
    // System.out.println("     "+pd.isShowing());
    //
    // if(pd!=null&&pd.isShowing())
    // {
    // pd.dismiss();
    //
    // return true;
    // }
    //
    // }
    // }
    // return super.onKeyDown(keyCode, event);
    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	//getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public void onBackPressed() {

	super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int id = item.getItemId();
//	if (id == R.id.action_settings) {
//	    return true;
//	}
	return super.onOptionsItemSelected(item);
    }








    /**
     * 
     * @param user
     * 
     * 
     *            第三方返回的用户信息
     * 
     * 
     * @param type
     * 
     * 
     * 
     *            第三方登录的类型
     */
    private void thirdLogin(UserInfo user, final int type) {

	HttpRequest.thirdLoginGo(this, user, type, new HttpRequest.onRequestCallback() {

	    @Override
	    public void onSuccess(BaseInfo response) {
		if (pd != null) {
		    pd.dismiss();
		}
			UserInfo userInfo = (UserInfo) response;
		System.out.println("   login      " + userInfo.account + "    "
			+ userInfo.headurl + "   uid   " + userInfo.uid
			+ "  昵称：   " + userInfo.nickName);
		setUserToLocal(userInfo, type);
		// Intent loginIntent=new Intent(LoginActivity.this,
		// MainActivity.class);
		// loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(loginIntent);
		finish();
	    }

	    @Override
	    public void onFailure(String rawJsonData) {
		pd.dismiss();
			System.out.println("登录失败-----"+rawJsonData);
		ToolUtils.showToast(LoginActivity.this, "登录失败，请稍后再试"+rawJsonData);
	    }
	}, pd);

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	super.onActivityResult(arg0, arg1, arg2);
	UMShareAPI mShareAPI = UMShareAPI.get(this);
	mShareAPI.onActivityResult(arg0, arg1, arg2);
	System.out.println(" onActivityResult     " + arg0 + "   " + arg1
		+ "    " + arg2);
    }



    private void setUserToLocal(UserInfo user, int type) {

	DataUtils.setUserToLocal(getBaseContext(), user, type);

		PushManager.getInstance().initialize(this.getApplicationContext(), go.fast.fastenvelopes.service.PushInitService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), go.fast.fastenvelopes.service.PushIntentService.class);


    }






    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub

	switch (v.getId()) {
	case R.id.weixin_login_btn:

	case R.id.qq_login_btn:

	    pd.show();
	    ShareUtils.getUserInfoByOther(LoginActivity.this,
		    v.getId() == R.id.weixin_login_btn ? SHARE_MEDIA.WEIXIN
			    : SHARE_MEDIA.QQ, new ShareUtils.UserInfoListener() {

			@Override
			public void onError(SHARE_MEDIA arg0, int arg1) {
				ToolUtils.showToast(LoginActivity.this,"登录出错："+arg1);
			    if (pd != null) {
				pd.dismiss();
			    }
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
								   UserInfo user) {
				ToolUtils.showToast(LoginActivity.this,"登录成功："+arg0);
			    sendUserInfoToServer(arg0, arg1, user);
			    System.out.println("   onComplete      " + user.uid
				    + "  昵称：   " + user.nickName);

			}

			@Override
			public void onCancel(SHARE_MEDIA arg0, int arg1) {

			    if (pd != null) {
				pd.dismiss();
			    }
			}
		    });

	    break;

	}

    }

    private void sendUserInfoToServer(final SHARE_MEDIA arg0, int arg1,
	    final UserInfo user) {
	Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if (arg0.equals(SHARE_MEDIA.QQ)) {

		    thirdLogin(user, CommonUtils.LOGIN_TYPE_QQ);

		} else if (arg0.equals(SHARE_MEDIA.WEIXIN)) {

			thirdLogin(user, CommonUtils.LOGIN_TYPE_WEIXIN);
		}

	    }
	};

	if (user != null && user.headurl != null) {

	    FileUtils.download(user.headurl, handler);

	} else {
	    handler.sendEmptyMessage(1);
	}

    }

}
