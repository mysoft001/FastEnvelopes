package go.fast.fastenvelopes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.fragments.MainFragment;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;

public class MyAccountActivity extends BaseActivity implements OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    MainFragment fragment;
    //protected TextView userNick;// 用户昵称
    //protected TextView signature;// 签名
    // protected TextView account;// 签名
   // protected TextView commission;// 我的佣金
    protected TextView overage;// 账户余额

    protected TextView totalCount;// 总笔数


    private RelativeLayout commissonDetailLayout;// 佣金明细

    private RelativeLayout commissonInstuctLayout;// 佣金说明

    //protected ImageView userIcon;

    private String headUrl;
    private TextView extractText;// 提现
    private TextView rechargeText;// 充值

    private double commissionCount = 28.6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.myaccount_layout);
	initView();
    }

    private void initView() {
//	userNick = (TextView) findViewById(R.id.user_nick_tv);
//	signature = (TextView) findViewById(R.id.user_signature_tv);
	// account = (TextView) findViewById(R.id.user_account_tv);
	//commission = (TextView) findViewById(R.id.commission_tv);
	totalCount = (TextView) findViewById(R.id.totalcount_tv);
	overage = (TextView) findViewById(R.id.overage_tv);

	commissonDetailLayout = (RelativeLayout) findViewById(R.id.instruct_rl);
	extractText = (TextView) findViewById(R.id.extract_tv);
	rechargeText = (TextView) findViewById(R.id.recharge_tv);
	commissonDetailLayout.setOnClickListener(this);

	commissonInstuctLayout = (RelativeLayout) findViewById(R.id.commission_instruct_rl);
	commissonInstuctLayout.setOnClickListener(this);
//	userIcon = (ImageView) findViewById(R.id.iv_user_photo);
//	userIcon.setOnClickListener(this);
	extractText.setOnClickListener(this);
	rechargeText.setOnClickListener(this);
	String userAccount = PreferenceUtils.getInstance(this)
		.getSettingUserAccount();
	String headUrl = PreferenceUtils.getInstance(this).getSettingUserPic();
	String nickname = PreferenceUtils.getInstance(this)
		.getSettingUserNickName();
	
	overage.setText(PreferenceUtils.getInstance(this).getSettingTotalGoldenSize()+"");
	

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();//同步金币数
    }

    @Override
    public void initMiddle(TextView middleView) {
	// TODO Auto-generated method stub
	super.initMiddle(middleView);
	middleView.setText("我的账户");
    }


    public void sendPost(View v) {

	// sendRequest();
	return;
    }

    private void sendRequest() {

  	if (!CommonUtils.isNetWorkConnected(this)) {
  	    ToolUtils.showToast(this, "网络出问题了,无法同步金币总额");
  	    return;
  	}
  	RequestParams params = new RequestParams();
  	params.put("account", PreferenceUtils.getInstance(this)
  		.getSettingUserAccount());
  	DeviceUuidFactory uuid = new DeviceUuidFactory(this);
  	String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
  	params.put("access_token", uid);
  	
  	params.put("user_account", PreferenceUtils.getInstance(this)
  		.getSettingUserAccount());
  	// String Street =
  	HttpRestClient.post(Constant.GET_USERINFO, params,
  		new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

  		    @Override
  		    public void onSuccess(int statusCode,
  			    cz.msebera.android.httpclient.Header[] headers,
  			    String rawJsonResponse, UserInfo response) {
  			
  			if (response != null) {
  			    if (response.getStatus().equals("ok")) {
  				
  				overage.setText(response.goldCoin+"");
  				if(response.goldCoin>0)//保存用户获得的总金币
  				{
  				    PreferenceUtils.getInstance(MyAccountActivity.this).setSettingTotalGoldenSize(response.goldCoin);
  				}
  				
  			    } else if (response.getStatus().equals("login"))// 在其他地方登录
  			    {
  				HttpRequest.reLogin(MyAccountActivity.this);
  			    }

  			    else if (response.getStatus().equals("nothing")) {
  				// ToolUtils.showToastByStr(getActivity(),
  				// "用户信息不存在");
  			    }
  			}

  		    }

  		    @Override
  		    public void onFailure(int statusCode,
  			    cz.msebera.android.httpclient.Header[] headers,
  			    Throwable throwable, String rawJsonData,
									UserInfo errorResponse) {
  		    }

  		    @Override
  		    protected UserInfo parseResponse(String rawJsonData,
  			    boolean isFailure) throws Throwable {

  			return null;
  		    }
  		});

      }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
//getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int id = item.getItemId();
//	if (id == R.id.action_settings) {
//	    return true;
//	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {


	case R.id.instruct_rl:
	    ToolUtils.showToast(this, "还没有任何佣金明细");
	    
	    
//	    Intent commissionDetailIntent = new Intent(this,
//		    CommissionDetailActivity.class);
//	    startActivity(commissionDetailIntent);
//	    overridePendingTransition(R.anim.slide_in_from_left,
//		    R.anim.slide_out_to_right);
	    break;

	case R.id.commission_instruct_rl:
//	    Intent commissionInstructIntent = new Intent(this,
//		    CommissionInstrctActivity.class);
//	    startActivity(commissionInstructIntent);
//	    overridePendingTransition(R.anim.slide_in_from_left,
//		    R.anim.slide_out_to_right);
	    break;
	case R.id.extract_tv:
	    Intent intent = new Intent(this, GetMoneyActivity.class);
	    intent.putExtra("commission", commissionCount);
	    startActivity(intent);
	    overridePendingTransition(R.anim.slide_in_from_left,
		    R.anim.slide_out_to_right);
	    break;
	case R.id.recharge_tv:
	    Intent rechargeIntent = new Intent(this, RechargeActivity.class);
	    startActivity(rechargeIntent);
	    overridePendingTransition(R.anim.slide_in_from_left,
		    R.anim.slide_out_to_right);
	    break;

	}

    }
}
