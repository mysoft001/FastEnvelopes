package go.fast.fastenvelopes.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import kankan.wheel.widget.ArrayWheelAdapter;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class SettingActivity extends BaseActivity implements OnClickListener,
	OnValueChangeListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    protected TextView sex;// 性别
    protected TextView age;// 年龄

    protected RelativeLayout sexLayout;
    protected RelativeLayout ageLayout;
    protected RelativeLayout notifyLayout;
    protected RelativeLayout loginLayout;
    protected RelativeLayout feedbackLayout;

    protected DialogPlus changAgeDialog;
    
    protected TextView notifyText;// 通知文本

    private Dialog pd;// 添加loading条

    protected DialogPlus changSexDialog;// 更改年龄的对话框
    protected EditText dialogEdit;

    protected NumberPicker numberPick;
    
     String[] chooseItem=new String[]{"只有声音","只有震动","声音和震动","无提示"};

    String uid;
    private BaseJsonHttpResponseHandler<UserInfo> responseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.settings_layout);
	initView();
	setInitView();
    }

    private void initView() {
	DeviceUuidFactory uuid = new DeviceUuidFactory(this);
	uid = uuid.getDeviceUuid().toString();
	pd = ToolUtils.createLoadingDialog(this, "正在上传请稍后");
	sex = (TextView) findViewById(R.id.sex_tv);
	age = (TextView) findViewById(R.id.age_tv);
	
	
	
	notifyText = (TextView) findViewById(R.id.notify_type_tv);
	
	feedbackLayout = (RelativeLayout) findViewById(R.id.feedback_rl);
	loginLayout = (RelativeLayout) findViewById(R.id.login_rl);
	sexLayout = (RelativeLayout) findViewById(R.id.sex_rl);
	ageLayout = (RelativeLayout) findViewById(R.id.age_rl);
	notifyLayout = (RelativeLayout) findViewById(R.id.notice_in_rl);
	sexLayout.setOnClickListener(this);
	ageLayout.setOnClickListener(this);
	loginLayout.setOnClickListener(this);
	notifyLayout.setOnClickListener(this);
	feedbackLayout.setOnClickListener(this);

    }

    
    private void setUserInfo()
    {
	notifyText.setText(chooseItem[PreferenceUtil.getInstance(SettingActivity.this).getInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, 2)]);
	
	sex.setText(PreferenceUtils.getInstance(this).getSettingUserSex());
	age.setText(PreferenceUtils.getInstance(this).getSettingUserAge());
    }
    private void setInitView() {
	
	setUserInfo();
	responseHandler = new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

	    @Override
	    public void onSuccess(int statusCode, Header[] headers,
		    String rawJsonResponse, UserInfo response) {
		System.out.println("修改用户信息----------" + rawJsonResponse);
		pd.dismiss();
		if (response != null) {
		    if (response.getStatus().equals("ok")) {
			setUserToLocal(response);
			ToolUtils.showToastByStr(SettingActivity.this,
				"更改用户信息成功");

		    }

		    else if (response.getStatus().equals("rename"))// 昵称重复
		    {
			ToolUtils.showToastByStr(SettingActivity.this,
				"该昵称已存在，请更换后再试");
		    } else if (response.getStatus().equals("login"))// 在其他地方登录
		    {
			HttpRequest.reLogin(SettingActivity.this);
		    } else {
			ToolUtils.showToast(SettingActivity.this, "更改用户信息失败");
		    }
		}
	    }

	    @Override
	    public void onFailure(int statusCode, Header[] headers,
		    Throwable throwable, String rawJsonData, UserInfo errorResponse) {
		pd.dismiss();
	    }

	    @Override
	    protected UserInfo parseResponse(String rawJsonData, boolean isFailure)
		    throws Throwable {
		return null;
	    }
	};
    }

    private void setUserToLocal(UserInfo user) {
	PreferenceUtils.getInstance(this).setSettingUserSex(
		String.valueOf(user.sex));
	PreferenceUtils.getInstance(this).setSettingUserAge(
		String.valueOf(user.age));
	//PreferenceUtils.getInstance(this).setSettingUserID(user.userId);

	PreferenceUtils.getInstance(this).setSettingUserLevel(user.level);
	
	setUserInfo();
    }

    protected void changeSex() {

	DialogPlusBuilder dialog = DialogPlus.newDialog(this);
	dialog.setGravity(Gravity.CENTER);
	dialog.setContentHolder(new ViewHolder(R.layout.change_sex_layout));
	changSexDialog = dialog.create();
	changSexDialog.show();
	View setView = changSexDialog.getHolderView();
	setView.findViewById(R.id.male_tv).setOnClickListener(this);
	setView.findViewById(R.id.female_tv).setOnClickListener(this);
	setView.findViewById(R.id.changesex_tv).setOnClickListener(this);
    }

    protected void changeAge(int currentAge) {
	DialogPlusBuilder dialog = DialogPlus.newDialog(this);
	dialog.setContentHolder(new ViewHolder(
		R.layout.dialog_numberchoose_layout));
	dialog.setGravity(Gravity.CENTER);
	changAgeDialog = dialog.create();
	changAgeDialog.show();
	View setView = changAgeDialog.getHolderView();
	setView.findViewById(R.id.numok_tv).setOnClickListener(this);
	numberPick = (NumberPicker) setView.findViewById(R.id.input_np);
	setView.findViewById(R.id.numcancel_tv).setOnClickListener(this);
	numberPick.setOnValueChangedListener(this);
	numberPick.setMaxValue(100);
	numberPick.setMinValue(6);
	numberPick.setValue(currentAge);
    }

    protected void updateSex(String result) {
	sex.setText(result);
    }

    protected void updateAge(String result) {
	age.setText(result);
    }

    @Override
    public void initMiddle(TextView middleView) {
	super.initMiddle(middleView);
	middleView.setText("设置");
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
	case R.id.sex_rl:
	    changeSex();
	    break;
	case R.id.age_rl:
	    changeAge(Integer.valueOf(PreferenceUtils.getInstance(this)
		    .getSettingUserAge()));

	    break;

	case R.id.numok_tv:
	    changAgeDialog.dismiss();
	    changeUserInfo(new String[] { "age" }, new String[] { ""
		    + numberPick.getValue() });
	    updateAge("" + numberPick.getValue());
	    break;
	case R.id.numcancel_tv:
	    changAgeDialog.dismiss();
	    break;

	case R.id.male_tv:
	    changeUserInfo(new String[] { "sex" }, new String[] { "1" });
	    changSexDialog.dismiss();
	    // updateSex("男");
	    break;
	case R.id.female_tv:
	    changeUserInfo(new String[] { "sex" }, new String[] { "0" });
	    changSexDialog.dismiss();
	    // updateSex("女");
	    break;
	case R.id.changesex_tv:
	    changSexDialog.dismiss();

	    break;
	    
	case R.id.feedback_rl:
	    startActivity(new Intent(this, FeedbackActivity.class));
	    this.overridePendingTransition(R.anim.slide_in_from_left,
		    R.anim.slide_out_to_right);
	    break;
	case R.id.login_rl:
	    Intent loginIntent = new Intent(this, LoginActivity.class);
	    startActivity(loginIntent);
	    this.overridePendingTransition(R.anim.slide_in_from_left,
		    R.anim.slide_out_to_right);
	    break;
	    
	case R.id.notice_in_rl://声音和震动
	    
	    notifySetting();
	    break;
	}

    }
    
    private void notifySetting()
    {
	DialogPlusBuilder dialog = DialogPlus.newDialog(this);
	dialog.setContentHolder(new ViewHolder(
		R.layout.dialog_notifychoose_layout));
	dialog.setGravity(Gravity.BOTTOM);
	changAgeDialog = dialog.create();
	changAgeDialog.show();
	View setView = changAgeDialog.getHolderView();
	WheelView notifyChooseV=(WheelView) setView.findViewById(R.id.notify_wv);
	notifyChooseV.setAdapter(new ArrayWheelAdapter<String>(chooseItem));
	
	notifyChooseV.setCurrentItem(PreferenceUtil.getInstance(SettingActivity.this).getInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, 2));
	
	notifyChooseV.addChangingListener(new OnWheelChangedListener() {
	    
	    @Override
	    public void onChanged(WheelView wheel, int oldValue, int newValue) {
		
		PreferenceUtil.getInstance(SettingActivity.this).putInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, newValue);
		
		notifyText.setText(chooseItem[newValue]);
	    }
	});
    }

    private void changeUserInfo(String[] keys, Object[] values) {
	RequestParams params = new RequestParams();
	params.put("access_token", uid);

	params.put("account", PreferenceUtils.getInstance(this)
		.getSettingUserAccount());

	pd.show();
	for (int i = 0; i < keys.length; i++) {
	    params.put(keys[i], values[i]);
	}
	HttpRestClient.post(Constant.SETTING_USERINFO, params, responseHandler);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
	// TODO Auto-generated method stub

    }

}
