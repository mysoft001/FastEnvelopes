package go.fast.fastenvelopes.activity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.PayUtils;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.utils.WXPayInfo;

/**
 * 充值界面
 * 
 * @author hanwei
 *
 */
public class RechargeActivity extends BaseActivity implements OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    private int[] moneyList = new int[] { 10, 20, 50, 100, 200 };
    private Button rechargeBtn;
    private int choosedMoney;
    private RadioGroup chooseMoneyRG;
    private TextView glodenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.recharge_layout);
	initView();
    }

    private void initView() {

	rechargeBtn = (Button) findViewById(R.id.recharge_btn);
	rechargeBtn.setOnClickListener(this);
	chooseMoneyRG = (RadioGroup) findViewById(R.id.chooseMoney_rg);
	glodenText = (TextView) findViewById(R.id.gloden_tv);

	chooseMoneyRG.check(R.id.money_20_rb);
	choosedMoney = moneyList[1];
	glodenText.setText("" + moneyList[1] * 10);
	chooseMoneyRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup group, int checkedId) {

		int pos = 0;
		switch (checkedId) {
		case R.id.money_200_rb:
		    pos = 4;
		    break;
		case R.id.money_100_rb:
		    pos = 3;

		    break;
		case R.id.money_50_rb:
		    pos = 2;

		    break;
		case R.id.money_20_rb:
		    pos = 1;

		    break;
		case R.id.money_10_rb:
		    pos = 0;

		    break;

		}
		glodenText.setText("" + moneyList[pos] * 10);
		choosedMoney = moneyList[pos];
	    }
	});
    }

    @Override
    public void initMiddle(TextView middleView) {
	// TODO Auto-generated method stub
	super.initMiddle(middleView);
	middleView.setText("充值");
    }

    // @Override
    // public void initRight(ImageView imageView, TextView textView, View
    // parent) {
    // // TODO Auto-generated method stub
    // super.initRight(imageView, textView, parent);
    // parent.setVisibility(View.VISIBLE);
    // textView.setVisibility(View.VISIBLE);
    // textView.setText("提交");
    // parent.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    //
    // }
    // });
    //

    // }

    private void setInitView(UserInfo userInfo) {

    }

    public void sendPost(View v) {

	// sendRequest();
	return;
    }

    private void sendRequest(String account) {

	RequestParams params = new RequestParams();
	params.put("account", account);
	// String Street =
	HttpRestClient.post(Constant.GET_USERINFO, params,
		new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

		    @Override
		    public void onSuccess(int statusCode,
			    cz.msebera.android.httpclient.Header[] headers,
			    String rawJsonResponse, UserInfo response) {
			if (response != null) {
			    if (response.getStatus().equals("ok")) {
				setInitView(response);
			    } else if (response.getStatus().equals("nothing")) {
				ToolUtils.showToastByStr(RechargeActivity.this,
					"用户信息不存在");
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
	case R.id.recharge_btn:
	    
	    showPayChoose();
	    
	    break;

	}

    }
    
    DialogPlus rechargeDialog;
    protected void showPayChoose() {


 	    View commentLayout = LayoutInflater.from(this).inflate(
 		    R.layout.recharge_choose_dialog_layout, null);

 	    DialogPlusBuilder dialogBulider = DialogPlus.newDialog(this);
 	    dialogBulider.setContentHolder(new dialogplus.ViewHolder(commentLayout));
 	    //dialogBulider.setContentHeight(PixelUtil.dp2Pixel(320, this));
 	    dialogBulider.setGravity(Gravity.BOTTOM);
 	   rechargeDialog = dialogBulider.create();

 	   TextView orders_detail_tv=(TextView) commentLayout.findViewById(R.id.orders_detail_tv);
 	  orders_detail_tv.setText("充值"+choosedMoney*10+"金币");
 	  TextView orders_money_tv=(TextView) commentLayout.findViewById(R.id.orders_money_tv);
 	 orders_money_tv.setText(choosedMoney+"元");
 	 TextView paynow_title=(TextView) commentLayout.findViewById(R.id.paynow_title);
 	 
 	   paynow_title.setOnClickListener(new OnClickListener() {

 		@Override
 		public void onClick(View v) {
 		   requestPay(choosedMoney);
 	    }
 	   });

 	rechargeDialog.show();

     }

    private void requestPay(final float money) {

	// choosedMoney
	HttpRequest.prePay(this, money, new HttpRequest.onRequestCallback() {

	    @Override
	    public void onSuccess(BaseInfo response) {

		WXPayInfo payInfo = (WXPayInfo) response;
		PreferenceUtil.getInstance(RechargeActivity.this).putFloat(
			"rechargeSize", money);
		PayUtils.startPayForWX(RechargeActivity.this, payInfo);

	    }

	    @Override
	    public void onFailure(String rawJsonData) {

	    }
	});
    }

}
