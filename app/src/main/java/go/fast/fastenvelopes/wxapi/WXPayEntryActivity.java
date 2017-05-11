package go.fast.fastenvelopes.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.BaseActivity;
import go.fast.fastenvelopes.activity.MyAccountActivity;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.PayUtils;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.WXPayInfo;

public class WXPayEntryActivity extends BaseActivity implements
	IWXAPIEventHandler, OnClickListener {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    private TextView payResultText;

    private TextView goldSizeText;

    private LinearLayout payResDetailLayout;

    private Button okBtn;

    private TextView showMoneyText;
	int goldSize;

	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.pay_result);
	initView();
	api = WXAPIFactory.createWXAPI(this, PayUtils.WXAPP_ID);
	api.handleIntent(getIntent(), this);
    }

    private void initView() {
	payResultText = (TextView) findViewById(R.id.result_tv);
	goldSizeText = (TextView) findViewById(R.id.goldsize_tv);
	showMoneyText = (TextView) findViewById(R.id.showmoney_tv);
	showMoneyText.setOnClickListener(this);
	okBtn = (Button) findViewById(R.id.btn_ikow);
	okBtn.setOnClickListener(this);
	payResDetailLayout = (LinearLayout) findViewById(R.id.result_detail_ll);

	 goldSize = (int) (PreferenceUtil.getInstance(this).getFloat(
		"rechargeSize", 0) * 10);

	goldSizeText.setText(goldSize + "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	setIntent(intent);
	api.handleIntent(intent, this);
    }

    @Override
    public void initMiddle(TextView middleView) {
	// TODO Auto-generated method stub
	super.initMiddle(middleView);

	middleView.setText("充值结果");
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
	Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

	System.out.println("  onResp   " + resp.errCode + "      "
		+ resp.transaction + "    " + resp.errStr);
	if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
	    if (resp.errCode == 0)// 支付成功
	    {
		payResultConfirm();
	    } else if (resp.errCode == -2)// 支付取消
	    {
			 findViewById(R.id.result_detail_ll).setVisibility(View.GONE);
			 ((TextView) findViewById(R.id.result_tv)).setText("您取消了充值");

	    } else {
		
	
			 findViewById(R.id.result_detail_ll).setVisibility(View.GONE);
			 ((TextView) findViewById(R.id.result_tv)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			((TextView) findViewById(R.id.result_tv)).setText("充值不成功，请确认是否安装了微信，如有疑问请联系我们");

	    }

	}
    }

    private void payResultConfirm() {

	HttpRequest.payResultConfirm(this, new HttpRequest.onRequestCallback() {

	    @Override
	    public void onSuccess(BaseInfo response) {

		WXPayInfo payInfo = (WXPayInfo) response;

		if (payInfo.result_code.equals("SUCCESS")) {


		    ((TextView) findViewById(R.id.result_tv)).setText("充值成功");

		int currentGoldSize=	PreferenceUtils.getInstance(WXPayEntryActivity.this).getSettingTotalGoldenSize();

			PreferenceUtils.getInstance(WXPayEntryActivity.this).setSettingTotalGoldenSize(currentGoldSize+goldSize);
		}
	    }

	    @Override
	    public void onFailure(String rawJsonData) {

		findViewById(R.id.result_detail_ll).setVisibility(View.GONE);
		 ((TextView) findViewById(R.id.result_tv)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		((TextView) findViewById(R.id.result_tv)).setText("充值出现错误，如有疑问请联系我们");
	    }
	});

    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	case R.id.showmoney_tv:
	    Intent showMyAccountIntent = new Intent(this,
		    MyAccountActivity.class);
	    startActivity(showMyAccountIntent);
	    finish();
	    break;

	case R.id.btn_ikow:
	    finish();
	    break;
	}
    }
}
