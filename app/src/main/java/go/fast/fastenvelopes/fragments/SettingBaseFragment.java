package go.fast.fastenvelopes.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;
import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.AboutActivity;
import go.fast.fastenvelopes.activity.HelpActivity;
import go.fast.fastenvelopes.activity.LoginActivity;
import go.fast.fastenvelopes.activity.MyAccountActivity;
import go.fast.fastenvelopes.activity.RechargeActivity;
import go.fast.fastenvelopes.activity.SettingActivity;
import go.fast.fastenvelopes.activity.ShowAttentionActivity;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.rewardanim.utils.FlakeView;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.PublicViewUtils;
import go.fast.fastenvelopes.utils.ShareUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.view.CoolDialogView;

/**
 * 我的设置Fragment
 */
public class SettingBaseFragment extends BaseFragment implements
	OnClickListener {
    protected static final String TAG = "LivingFragment";

    protected ListView livingList;
    protected ViewPager livingPager;

    protected final int CHANGE_SIGNAUTRE = 1;
    protected final int CHANGE_NICKNAME = 2;

    protected TextView userNick;// 用户昵称
    protected TextView signature;// 签名
    protected TextView account;// 账号

    protected RelativeLayout fansLayout;// 我的粉丝
    protected RelativeLayout attentionLayout;// 关注
   // protected RelativeLayout zanLayout;// 点赞

    protected TextView fansText;// 我的粉丝数

	protected TextView rechargeText;//充值
    protected TextView attentionText;// 关注数
   // protected TextView zanText;// 点赞数

    protected TextView level;// 等级
	protected TextView lastPlayCountsTV;// 当日剩余可玩次数
	protected TextView lastPlayCountsRechargeTv;// 等级


    protected RelativeLayout changeSignature;
    protected RelativeLayout changeUserNick;
    protected RelativeLayout envelopesLayout;//红包
    protected RelativeLayout settingLayout;// 设置选项

	protected RelativeLayout inviteFriendLayout;// 邀请好友
	protected RelativeLayout helpLayout;// 帮助中心

	protected RelativeLayout aboutLayout;// 关于我们

    protected RelativeLayout showLoginLayout;// 显示登录选项


    private Dialog pd;// 添加loading条

    protected DialogPlus changSignaureDialog;
    protected ImageView userIcon;
    protected EditText dialogEdit;
    protected File cameraFile;// 照相机拍照的图片
    // 头像uri
    protected static String IMAGE_FILE_LOCATION = null;
    protected Uri imageUri = null;
    protected static final String IMAGE_FILE_LOCATION_TEAST = "file:///sdcard/wholewriter/temp.jpg";// temp
    // file
    Uri imageUritest = Uri.parse(IMAGE_FILE_LOCATION_TEAST);// The Uri to store
							    // the big bitmap
    public static final int USERPIC_REQUEST_CODE_LOCAL = 101;

    public static final int USER_LOGIN = 109;
    public static final int USERPIC_REQUEST_CODE_LOCAL_19 = 101;
    public static final int USERPIC_REQUEST_CODE_CAMERA = 102;
    public static final int USERPIC_REQUEST_CODE_CUT = 103;

    protected int currentChange;
    BaseJsonHttpResponseHandler<UserInfo> responseHandler;
    String uid;
	protected TextView goldSizeText;
    protected FlakeView flakeView;

   // private RelativeLayout deleteArticleLayout;

    // protected NumberPicker numberPick;

    public static SettingBaseFragment newInstance() {
	return new SettingBaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
	super.onResume();
	flakeView.resume();
    }

    @Override
    public void onPause() {
	super.onPause();
	flakeView.pause();
    }

    public void setCustomEdit(EditText customEdit) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View rootView = inflater.inflate(R.layout.myself_settings_fragment,
		null);
	initView(rootView);
	setInitView();
	return rootView;
    }

    protected void initView(View rootView) {

	flakeView = new FlakeView(getActivity());
	DeviceUuidFactory uuid = new DeviceUuidFactory(getActivity());
	uid = uuid.getDeviceUuid().toString();
	userNick = (TextView) rootView.findViewById(R.id.user_nick_tv);
	signature = (TextView) rootView.findViewById(R.id.user_signature_tv);
	account = (TextView) rootView.findViewById(R.id.user_account_tv);
	level = (TextView) rootView.findViewById(R.id.level_tv);
		goldSizeText = (TextView) rootView.findViewById(R.id.gold_tv);

		rechargeText = (TextView) rootView.findViewById(R.id.recharge_tv);
	showLoginLayout = (RelativeLayout) rootView
		.findViewById(R.id.show_login_rl);
		lastPlayCountsTV= (TextView) rootView.findViewById(R.id.lastplaycounts_tv);
		lastPlayCountsRechargeTv= (TextView) rootView.findViewById(R.id.lastplaycounts_recharge_tv);
	fansLayout = (RelativeLayout) rootView.findViewById(R.id.fans_rl);
	attentionLayout = (RelativeLayout) rootView.findViewById(R.id.keep_rl);
	attentionText = (TextView) rootView.findViewById(R.id.keeping_tv);

	fansText = (TextView) rootView.findViewById(R.id.fans_tv);

	pd = ToolUtils.createLoadingDialog(getActivity(), "正在上传请稍后");
		inviteFriendLayout = (RelativeLayout) rootView
				.findViewById(R.id.share_rl);
		helpLayout = (RelativeLayout) rootView
				.findViewById(R.id.help_rl);
		aboutLayout = (RelativeLayout) rootView
				.findViewById(R.id.about_rl);
		envelopesLayout = (RelativeLayout) rootView
		.findViewById(R.id.envelope_rl);
//	myAccountLayout = (RelativeLayout) rootView
//		.findViewById(R.id.my_account_rl);

	settingLayout = (RelativeLayout) rootView.findViewById(R.id.setting_rl);
	changeSignature = (RelativeLayout) rootView
		.findViewById(R.id.user_user_signature_rl);

	changeUserNick = (RelativeLayout) rootView
		.findViewById(R.id.user_nick_rl);
	userIcon = (ImageView) rootView.findViewById(R.id.iv_user_photo);

	userIcon.setOnClickListener(this);

    }

    protected void setInitView() {

	changeSignature.setOnClickListener(this);
	changeUserNick.setOnClickListener(this);
		envelopesLayout.setOnClickListener(this);
		inviteFriendLayout.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);
	attentionLayout.setOnClickListener(this);
	fansLayout.setOnClickListener(this);
	settingLayout.setOnClickListener(this);
		rechargeText.setOnClickListener(this);
		lastPlayCountsRechargeTv.setOnClickListener(this);
	responseHandler = new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

	    @Override
	    public void onSuccess(int statusCode, Header[] headers,
		    String rawJsonResponse, UserInfo response) {
		pd.dismiss();
		if (response != null) {
		    if (response.getStatus().equals("ok")) {
			setUserToLocal(response);
			setInitViews();
			ToolUtils.showToastByStr(getActivity(), "更改用户信息成功");

		    }

		    else if (response.getStatus().equals("rename"))// 昵称重复
		    {
			ToolUtils
				.showToastByStr(getActivity(), "该昵称已存在，请更换后再试");
		    } else if (response.getStatus().equals("login"))// 在其他地方登录
		    {
			HttpRequest.reLogin(getActivity());
		    } else {
			ToolUtils.showToast(getActivity(), "更改用户信息失败");
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

	setInitViews();

    }

    protected void setInitViews() {

	if (PreferenceUtils.getInstance(getActivity()).getSettingUserAccount()
		.equals("999")) {
	    showLoginLayout.setVisibility(View.VISIBLE);
	    showLoginLayout.setOnClickListener(this);
	} else {

	    showLoginLayout.setVisibility(View.GONE);
	}

	userNick.setText(PreferenceUtils.getInstance(getActivity())
		.getSettingUserNickName());
	signature.setText(PreferenceUtils.getInstance(getActivity())
		.getSettingUserQianming());
	account.setText(PreferenceUtils.getInstance(getActivity())
		.getSettingUserAccount());

	level.setText(PreferenceUtils.getInstance(getActivity())
		.getSettingUserLevel());

		lastPlayCountsTV.setText(PreferenceUtils.getInstance(getActivity())
				.getSettingUserLastPlayCounts()+"");
	// int
	// sexType=Integer.valueOf(PreferenceUtils.getInstance(getActivity()).getSettingUserSex());
	ImageLoader.getInstance().displayImage(
		PreferenceUtils.getInstance(getActivity()).getSettingUserPic(),
		userIcon,
		ImageOptions
			.getOptionsTouxiangByRound(PixelUtil.dp2Pixel(40,
				getActivity())));

	int fansCount = PreferenceUtils.getInstance(getActivity())
		.getSettingUserFans();
	int attentionCount = PreferenceUtils.getInstance(getActivity())
		.getSettingUserAttention();
	int zanCount = PreferenceUtils.getInstance(getActivity())
		.getSettingUserZan();


	if (fansCount > 0) {
	    fansText.setText(fansCount + "");
	} else {
	    fansText.setText("0");
	}

	if (attentionCount > 0) {
	    attentionText.setText(attentionCount + "");
	} else {
	    attentionText.setText("0");
	}

    }

    private void setUserToLocal(UserInfo user) {
	PreferenceUtils.getInstance(getActivity()).setSettingUserPic(
		user.headurl);
	PreferenceUtils.getInstance(getActivity()).setSettingUserNickName(
		user.nickName);
	PreferenceUtils.getInstance(getActivity()).setSettingUserSex(
		String.valueOf(user.sex));
	PreferenceUtils.getInstance(getActivity()).setSettingUserAge(
		String.valueOf(user.age));
	PreferenceUtils.getInstance(getActivity()).setSettingUserQianming(
		user.signature);

	PreferenceUtils.getInstance(getActivity()).setSettingUserAccount(
		user.account);
	PreferenceUtils.getInstance(getActivity()).setSettingUserLevel(
		user.level);

		PreferenceUtils.getInstance(getActivity()).setSettingUserLastPlayCounts(
				user.lastCounts);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
    }

    protected void updateSignature(String result, TextView targetText) {
	targetText.setText(result);
    }

    protected void changeSignature(TextView targetText, final int maxSize) {
	DialogPlusBuilder dialog = DialogPlus.newDialog(getActivity());
	dialog.setContentHolder(new ViewHolder(R.layout.dialog_edit_layout));

	changSignaureDialog = dialog.create();
	changSignaureDialog.show();
	View setView = changSignaureDialog.getHolderView();
	final TextView currentCountText = (TextView) setView
		.findViewById(R.id.currentcount_tv);
	final TextView totalCountText = (TextView) setView
		.findViewById(R.id.totalcount_tv);
	totalCountText.setText(String.valueOf(maxSize));
	setView.findViewById(R.id.editsend_tv).setOnClickListener(this);
	dialogEdit = (EditText) setView.findViewById(R.id.input_et);
	dialogEdit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
		maxSize) });
	dialogEdit.setText(targetText.getText());
	dialogEdit.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {

		if (maxSize - s.length() < 6) {
		    currentCountText.setTextColor(Color.parseColor("#ff0000"));
		} else {
		    currentCountText.setTextColor(Color.parseColor("#999999"));
		}
		currentCountText.setText(String.valueOf(maxSize - s.length()));

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {

	    }

	    @Override
	    public void afterTextChanged(Editable s) {

	    }
	});
	setView.findViewById(R.id.editcancel_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {

	case R.id.envelope_rl:// 我的账号
	    startActivity(new Intent(getActivity(), MyAccountActivity.class));
	    getActivity().overridePendingTransition(R.anim.slide_in_from_left,
		    R.anim.slide_out_to_right);
	    break;


	case R.id.iv_user_photo:// 更改用户头像
	    change_headurl();
	    break;
	case R.id.user_nick_rl:// 更改用户昵称
	    currentChange = CHANGE_NICKNAME;
	    changeSignature(userNick, 12);
	    break;
	case R.id.user_user_signature_rl:
	    currentChange = CHANGE_SIGNAUTRE;
	    changeSignature(signature, 36);
	    break;
	case R.id.setting_rl:

	    Intent settingIntent = new Intent(getActivity(),
		    SettingActivity.class);
	    startActivity(settingIntent);
	    break;
	case R.id.editsend_tv:
	    changSignaureDialog.dismiss();
	    InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
		    .getSystemService(Context.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(
		    dialogEdit.getWindowToken(), 0);
	    if (currentChange == CHANGE_SIGNAUTRE) {

		if (ToolUtils.isNullOrEmpty(dialogEdit.getText().toString())) {
		    updateSignature("还没有签名", signature);
		    changeUserInfo(new String[] { "signature" },
			    new String[] { "还没有签名" });
		} else {
		    changeUserInfo(new String[] { "signature" },
			    new String[] { dialogEdit.getText().toString() });
		}
	    } else {
		if (ToolUtils.isNullOrEmpty(dialogEdit.getText().toString())) {
		    updateSignature("英雄不问出处", userNick);
		    changeUserInfo(new String[] { "nickName" },
			    new String[] { "英雄不问出处" });
		} else {
		    changeUserInfo(new String[] { "nickName" },
			    new String[] { dialogEdit.getText().toString() });
		    // updateSignature(dialogEdit.getText().toString(),userNick);
		}
	    }

	    break;
	case R.id.editcancel_tv:

	    changSignaureDialog.dismiss();
	    break;

	case 4:// 选择拍照
	    selectPicFromCamera();
	    break;

	case 5:// 选择本地相册
	    selectPicFromLocal();
	    break;

	case R.id.keep_rl:// 查看我关注的人

	    Intent keepIntent = new Intent(getActivity(),
		    ShowAttentionActivity.class);
	    keepIntent.putExtra("isMe", true);
	    startActivity(keepIntent);
	    break;
	case R.id.fans_rl:// 查看我的粉丝列表

	    Intent fansIntent = new Intent(getActivity(),
		    ShowAttentionActivity.class);
	    fansIntent.putExtra("isMe", false);
	    startActivity(fansIntent);
	    break;

	case R.id.show_login_rl:// 登录

	    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
	    startActivity(loginIntent);
	    break;
		case R.id.share_rl:// 邀请好友

			ShareUtils.shareApp(getActivity());
			break;
		case R.id.help_rl:// 帮助中心
			Intent helpIntent = new Intent(getActivity(),HelpActivity.class);
			startActivity(helpIntent);
			break;
		case R.id.about_rl:// 关于我们

			Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
			startActivity(aboutIntent);
			break;

		case R.id.recharge_tv:// 跳转到充值
			Intent rechargeIntent = new Intent(getActivity(), RechargeActivity.class);
			startActivity(rechargeIntent);
			getActivity().overridePendingTransition(R.anim.slide_in_from_left,
					R.anim.slide_out_to_right);
			break;

		case R.id.lastplaycounts_recharge_tv:// 充值可玩次数

			showPlayCountsPay(20);
			break;

	}

    }


private void showPlayCountsPay(int goldSize)
{
	PublicViewUtils.showConsumeGold(getActivity(), "消费" + goldSize + "金币来购买10次。", goldSize, new PublicViewUtils.OnPayGoldListener() {
	@Override
	public void payGold(DialogPlus dialogPlus) {

		buyPlayCounts();
		dialogPlus.dismiss();

	}
});
}


	private void buyPlayCounts()
	{

HttpRequest.rechargePlayCounts(getActivity(), new HttpRequest.onRequestCallback() {
	@Override
	public void onSuccess(BaseInfo response) {
		UserInfo userInfo= (UserInfo) response;
		if(HttpResponseUtil.isResponseOk(response))
		{
			if(userInfo.lastCounts>PreferenceUtils.getInstance(getActivity()).getSettingUserLastPlayCounts())
			{
				PreferenceUtils.getInstance(getActivity()).setSettingUserLastPlayCounts(
						userInfo.lastCounts);
				lastPlayCountsTV.setText(userInfo.lastCounts+"");
				ToolUtils.showToast(getActivity(),"当日可玩次数已经增加成功");
			}


		}
		else
		{
			ToolUtils.showToast(getActivity(),response.msg);
		}
	}

	@Override
	public void onFailure(String rawJsonData) {

	}
});

	}

    /**
     * 更改头像
     */
    public void change_headurl() {

	if (!CommonUtils.isExitsSdcard()) {
	    Toast.makeText(getActivity(), "SD卡不存在，不能更改头像",Toast.LENGTH_LONG).show();
	    return;
	}
	cameraFile = new File(CommonUtils.getBaseFilePath());
	if (cameraFile == null || !cameraFile.exists()) {// 如果文件存在就不在创建

	    if (!cameraFile.mkdirs()) {
		cameraFile = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath());
		System.out.println("change_headurl-失败----------------"
			+ cameraFile);
	    }
	}
	cameraFile = new File(cameraFile.getAbsolutePath(),
		System.currentTimeMillis() + ".jpg");
	CoolDialogView dialog = new CoolDialogView();
	dialog.addNormalButton("现在就拍", this, 4);
	dialog.addNormalButton("相册选取", this, 5);
	dialog.addNormalButton("取消", this, 3);
	dialog.show(getActivity());

    }

    // --------------------------------------------------------------------------------------------------------------------
    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
	Intent intent;
	if (Build.VERSION.SDK_INT < 19) {
	    intent = new Intent(Intent.ACTION_GET_CONTENT);
	    intent.setType("image/*");

	} else {
	    intent = new Intent(
		    Intent.ACTION_PICK,
		    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	    // startActivityForResult(intent, USERPIC_REQUEST_CODE_LOCAL_19);
	}
	startActivityForResult(intent, USERPIC_REQUEST_CODE_LOCAL);
    }

    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {

	// cameraFile = new File(PathUtil.getInstance().getImagePath(),
	// DemoApplication.getInstance().getUserName()

	// ActivityCompat.requestPermissions(getActivity(),
	// new
	// String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
	// 1);
	startActivityForResult(
		new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
			MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
		USERPIC_REQUEST_CODE_CAMERA);
    }

    /**
     * onActivityResult
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == USERPIC_REQUEST_CODE_CAMERA) { // 获取照片
	    if (cameraFile != null && cameraFile.exists()) {
		// 改成返回到指定的uri imageUri = Uri.fromFile(cameraFile);
		cropImageUri(Uri.fromFile(cameraFile),
			PixelUtil.dp2Pixel(100, getActivity()),
			PixelUtil.dp2Pixel(100, getActivity()),
			USERPIC_REQUEST_CODE_CUT);

	    }
	} else if (requestCode == USERPIC_REQUEST_CODE_LOCAL) { // 获取本地图片
	    if (data != null) {
		Uri selectedImage = data.getData();
		if (selectedImage != null) {
		    cropImageUri(selectedImage,
			    PixelUtil.dp2Pixel(80, getActivity()),
			    PixelUtil.dp2Pixel(100, getActivity()),
			    USERPIC_REQUEST_CODE_CUT);
		    // Log.d("log","selectedImage"+selectedImage);

		}
	    }
	} else if (requestCode == USERPIC_REQUEST_CODE_CUT) {// 裁剪图片
	    // 从剪切图片返回的数据
	    if (data != null) {
		Bitmap bitmap = data.getParcelableExtra("data");
		if (bitmap != null) {
		    userIcon.setImageBitmap(bitmap);

		    File file = saveJPGE_After(bitmap, cameraFile); // 获取截取图片后的数据

		    if (file.exists()) {
			sendUserImg(file);
		    } else {
			Toast toast = Toast.makeText(getActivity(),
				"无法获取图片，请检查SD卡是否存在", Toast.LENGTH_SHORT);
		    }
		}

	    } else if (requestCode == USER_LOGIN && resultCode == 8) {

		setInitViews();

	    }
	}
    }

    private void sendUserImg(File file) {

	RequestParams params = new RequestParams();
	if (file.exists()) {
	    try {
		params.put("headurl", file, "image/jpeg");
		params.put("access_token", uid);
		params.put("account", PreferenceUtils
			.getInstance(getActivity()).getSettingUserAccount());
		pd.show();
		HttpRestClient.post(Constant.SETTING_USERINFO, params,
			responseHandler);
	    } catch (FileNotFoundException e) {
		pd.dismiss();
		e.printStackTrace();
	    }
	} else {
	    Toast toast = Toast.makeText(getActivity(), "无法获取图片，请检查SD卡是否存在",
		    Toast.LENGTH_SHORT);
	}
    }

    /**
     * 裁剪图片
     * 
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {

	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	intent.putExtra("crop", "true");
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	intent.putExtra("outputX", outputX);
	intent.putExtra("outputY", outputY);
	// intent.putExtra("scale", true);
	intent.putExtra("return-data", true);
	intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
	intent.putExtra("noFaceDetection", false); // no face detection
	startActivityForResult(intent, requestCode);
    }

    /**
     * 保存Bitmap为文件
     * 
     * @param baseBitmap
     */
    public void save(Bitmap baseBitmap) {
	try {
	    /*
	     * BufferedOutputStream bos = new BufferedOutputStream(new
	     * FileOutputStream(cutFile));
	     * baseBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
	     * bos.flush(); bos.close();
	     */
	    OutputStream stream = new FileOutputStream(Uritofile(imageUri));
	    baseBitmap.compress(CompressFormat.JPEG, 100, stream);
	    stream.close();
	    /*
	     * // 模拟一个广播，通知系统sdcard被挂载 Intent intent = new Intent();
	     * intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
	     * intent.setData(Uri.fromFile(Environment
	     * .getExternalStorageDirectory())); sendBroadcast(intent);
	     */
	    Toast.makeText(getActivity(), "保存图片成功", Toast.LENGTH_LONG).show();
	} catch (Exception e) {
	    Toast.makeText(getActivity(), "保存图片失败",Toast.LENGTH_LONG).show();
	    e.printStackTrace();
	}
    }

    /**
     * bitmap 转 file 保存图片为JPEG
     * 
     * @param bitmap
     * @param cameraFile2
     */
    public File saveJPGE_After(Bitmap bitmap, File cameraFile2) {
	// File file = new File(cameraFile2);
	try {
	    FileOutputStream out = new FileOutputStream(cameraFile2);
	    if (bitmap.compress(CompressFormat.JPEG, 100, out)) {
		out.flush();
		out.close();
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return cameraFile2;
    }

    /**
     * 根据uri获取图片地址
     * 
     * @param selectedImage
     */
    private File Uritofile(Uri selectedImage) {
	File file = null;
	Cursor cursor = getActivity().getContentResolver().query(selectedImage,
		null, null, null, null);
	if (cursor != null) {
	    cursor.moveToFirst();
	    int columnIndex = cursor.getColumnIndex("_data");
	    String picturePath = cursor.getString(columnIndex);
	    cursor.close();
	    cursor = null;

	    if (picturePath == null || picturePath.equals("null")) {
		Toast toast = Toast.makeText(getActivity(), "找不到图片",
			Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return null;
	    }
	    file = new File(picturePath);
	    // sendPicture(picturePath);
	} else {
	    file = new File(selectedImage.getPath());
	    if (!file.exists()) {
		Toast toast = Toast.makeText(getActivity(), "找不到图片",
			Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return null;
	    }

	}

	return file;

    }

    /**
     * 根据图库图片uri获取图片
     * 
     * @param selectedImage
     */
    private String getpathfromUri(Uri selectedImage) {
	// String[] filePathColumn = { MediaStore.Images.Media.DATA };
	Cursor cursor = getActivity().getContentResolver().query(selectedImage,
		null, null, null, null);
	if (cursor != null) {
	    cursor.moveToFirst();
	    int columnIndex = cursor.getColumnIndex("_data");
	    String picturePath = cursor.getString(columnIndex);
	    cursor.close();
	    cursor = null;

	    if (picturePath == null || picturePath.equals("null")) {
		Toast toast = Toast.makeText(getActivity(), "找不到图片",
			Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return null;
	    }
	    return picturePath;
	    // sendPicture(picturePath);
	} else {
	    File file = new File(selectedImage.getPath());
	    if (!file.exists()) {
		Toast toast = Toast.makeText(getActivity(), "找不到图片",
			Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return null;

	    }
	    return file.getAbsolutePath();
	}

    }

    private void changeUserInfo(String[] keys, Object[] values) {
	RequestParams params = new RequestParams();
	params.put("access_token", uid);

	params.put("account", PreferenceUtils.getInstance(getActivity())
		.getSettingUserAccount());

	pd.show();
	for (int i = 0; i < keys.length; i++) {
	    params.put(keys[i], values[i]);
	}
	HttpRestClient.post(Constant.SETTING_USERINFO, params, responseHandler);
    }

}
