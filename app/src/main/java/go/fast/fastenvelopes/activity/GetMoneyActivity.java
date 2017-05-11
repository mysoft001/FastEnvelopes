package go.fast.fastenvelopes.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.view.NiceSpinner;
import go.fast.fastenvelopes.widgets.CoolTextDialog;

/**
 * 取款界面
 * 
 * @author hanwei
 *
 */
public class GetMoneyActivity extends BaseActivity implements OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";
    List<String> dataset = new ArrayList<String>(Arrays.asList("支付宝", "微信支付",
	    "银行卡"));
    private String choosedType;
    private TextView totalMoney;
    private EditText getMoneyEdit;// 键入取款金额

    private EditText moneyAccountEdit;// 键入取款账号

    private NiceSpinner getMoneyType;// 获取取款方式

    private TextView moneyCanText;
    private double commissionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.getmoney);
	initView();
    }

    private void initView() {

	commissionCount = getIntent().getDoubleExtra("commission", 26.6);
	totalMoney = (TextView) findViewById(R.id.commission_tv);
	getMoneyEdit = (EditText) findViewById(R.id.getmoney_et);
	moneyAccountEdit = (EditText) findViewById(R.id.account_et);
	getMoneyType = (NiceSpinner) findViewById(R.id.moneytype_nsp);

	moneyCanText = (TextView) findViewById(R.id.money_tv);

	getMoneyType.attachDataSource(dataset);
	totalMoney.setText(String.valueOf(commissionCount));
	choosedType = dataset.get(0);
	getMoneyType.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		choosedType = dataset.get(position);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> parent) {

	    }
	});

	totalMoney.setText(PreferenceUtils.getInstance(this)
		.getSettingTotalGoldenSize() + "");

	getMoneyEdit.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {

		if (s.length() > 1)// 说明是liang位数
		{
		    int goldSize = Integer.valueOf(getMoneyEdit.getText()
			    .toString());

		    moneyCanText.setText(goldSize / 10 + "");

		} else {
		    moneyCanText.setText("0");
		}

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {

	    }

	    @Override
	    public void afterTextChanged(Editable s) {

	    }
	});

    }

    @Override
    public void initMiddle(TextView middleView) {
	// TODO Auto-generated method stub
	super.initMiddle(middleView);
	middleView.setText("提现");
    }

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {
	// TODO Auto-generated method stub
	super.initRight(imageView, textView, parent);
	parent.setVisibility(View.VISIBLE);
	textView.setVisibility(View.VISIBLE);
	textView.setText("提交");
	parent.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		getMoney();
	    }
	});

    }

    private void getMoney() {

	if (ToolUtils.isNullOrEmpty(getMoneyEdit.getText().toString())) {
	    ToolUtils.showToast(this, "请输入要变现的金币数");
	    return;
	}
	int goldSize = 0;

	goldSize = Integer.valueOf(getMoneyEdit.getText().toString());

	if (goldSize > PreferenceUtils.getInstance(this)
		.getSettingTotalGoldenSize()) {
	    ToolUtils.showToast(this, "输入的金币数超过可用金币数");
	    return;
	}

	if (PreferenceUtils.getInstance(this).getSettingTotalGoldenSize() < 1000) {
	    ToolUtils.showToast(this, "金币总数过少，无法提现。");
	    return;
	}

	if (ToolUtils.isNullOrEmpty(moneyAccountEdit.getText().toString())) {
	    ToolUtils.showToast(this, "请输入取款账号");
	    return;
	}

	int type = getMoneyType.getSelectedIndex() + 1;

	// 请求参数

	sendRequest(goldSize, type, moneyAccountEdit.getText().toString());
    }

    public void sendPost(View v) {

	// sendRequest();
	return;
    }

    private void sendRequest(int goldSize, int type, String moneyAccount) {

	RequestParams params = new RequestParams();
	params.put("account", PreferenceUtils.getInstance(this)
		.getSettingUserAccount());
	params.put("gold_coin", goldSize);
	params.put("type", type);
	DeviceUuidFactory uuid = new DeviceUuidFactory(this);
	String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
	params.put("access_token", uid);
	params.put("card_num", moneyAccount);
	// String Street =
	HttpRestClient.post(Constant.USER_GETMONEY, params,
		new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

		    @Override
		    public void onSuccess(int statusCode,
			    cz.msebera.android.httpclient.Header[] headers,
			    String rawJsonResponse, UserInfo response) {
			if (response != null) {
			    if (response.getStatus().equals("ok")) {
				showDialog();
			    } else {
				ToolUtils.showToast(GetMoneyActivity.this,
					"提现申请不成功，请稍后再试");
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

    private void showDialog() {

	final CoolTextDialog cancelShow = new CoolTextDialog(this);
	cancelShow.setTitle("提现提示");
	cancelShow.setContent("您的提现申请已经提交，请耐心等待。我们审核通过后会在一周内将款打到您的账户，请注意查收。");

	cancelShow.setOnPostiveListener("确定", new OnClickListener() {

	    @Override
	    public void onClick(View v) {
	    }
	});
	cancelShow.show();
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
	case R.id.iv_user_photo:
	    break;

	case R.id.instruct_rl:
	    break;

	case R.id.commission_instruct_rl:
	    break;
	}

    }
}
