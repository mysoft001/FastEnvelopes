package go.fast.fastenvelopes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import go.fast.fastenvelopes.MainActivity;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.AttentionListAdapter;
import go.fast.fastenvelopes.db.AttentionUserDB;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.AttentionListInfo;
import go.fast.fastenvelopes.info.AttentionMeListInfo;
import go.fast.fastenvelopes.info.AttentionObj;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

public class ShowAttentionActivity extends BaseActivity implements
	IXListViewListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    private AttentionListAdapter adapter;

    private List<AttentionObj> attentionList;

    private XListView livingList;

    private boolean isMyAttention;// 是否是我关注的用户列表

    private View emptyView;

    public static final int FRIST_GET_DATE = 111;// 首次加载
    public static final int REFRESH_GET_DATE = 112;// 刷新数据
    public static final int LOADMORE_GET_DATE = 113;// 获取更多数据

    private boolean isFromNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.show_attention_layout);
	initView();

	setInitView();
    }

    private void initView() {

	isFromNotify = getIntent().getBooleanExtra("fromNotify", false);
	isMyAttention = getIntent().getBooleanExtra("isMe", false);

	emptyView = findViewById(R.id.empty_layout);

	livingList = (XListView) findViewById(R.id.living_lv);
	attentionList = new ArrayList<>();

	adapter = new AttentionListAdapter(this, R.layout.attention_list_item,
		attentionList, isMyAttention);

	if (isMyAttention && middleView != null) {
	    middleView.setText("我关注的人");

	} else {
	    middleView.setText("关注我的人");
	}

    }

    public void sendRequest(final int ACTION) {
	switch (ACTION) {
	case FRIST_GET_DATE:// 第一次加载数据
	    if (isMyAttention) {
		requestParams(FRIST_GET_DATE);
	    } else {
		requestAttenttionMeParams(FRIST_GET_DATE);
	    }
	    break;
	case REFRESH_GET_DATE:// 刷新数据
	    if (isMyAttention) {
		requestParams(REFRESH_GET_DATE);
	    } else {
		requestAttenttionMeParams(REFRESH_GET_DATE);
	    }
	    break;
	case LOADMORE_GET_DATE:// 获取更多数据
	    if (isMyAttention) {
		requestParams(LOADMORE_GET_DATE);
	    } else {
		requestAttenttionMeParams(LOADMORE_GET_DATE);
	    }
	    break;
	}

    }

    @Override
    public void onRefresh() {
	sendRequest(REFRESH_GET_DATE);
    }

    @Override
    public void onLoadMore() {
	sendRequest(LOADMORE_GET_DATE);
    }

    private void onLoad() {// 防止多个线程同时调用
	// TODO Auto-generated method stub
	livingList.stopRefresh();
	livingList.stopLoadMore();
	SimpleDateFormat formatter = new SimpleDateFormat(
		"yyyy年MM月dd日   HH:mm:ss");
	Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
	String str = formatter.format(curDate);
	livingList.setRefreshTime(str);
	adapter.notifyDataSetChanged();
	emptyView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		((TextView) emptyView.findViewById(R.id.empty_tv))
			.setText("加载数据中...");
		sendRequest(FRIST_GET_DATE);// 请求数据
	    }
	});
	if (attentionList == null || attentionList.size() == 0) {

	    emptyView.setVisibility(View.VISIBLE);
	    if (CommonUtils.isNetWorkConnected(this)) {
		if (isMyAttention) {
		    ((TextView) emptyView.findViewById(R.id.empty_tv))
			    .setText("你还没关注任何人");
		} else {
		    ((TextView) emptyView.findViewById(R.id.empty_tv))
			    .setText("还没有人关注你");
		}

	    } else {
		((TextView) emptyView.findViewById(R.id.empty_tv))
					.setText("好像没有联上网哦，点此刷新");
	    }

	} else {
	    emptyView.setVisibility(View.GONE);
	}

    }

    @Override
    public void onBackPressed() {

	if (isFromNotify) {
	    Intent intent = new Intent(ShowAttentionActivity.this,
		    MainActivity.class);
	    startActivity(intent);

	}
	super.onBackPressed();

    }

    @Override
    public void initLeft(ImageView imageView, TextView textView, View parent)

    {

	parent.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

		if (isFromNotify) {
		    Intent intent = new Intent(ShowAttentionActivity.this,
			    MainActivity.class);
		    startActivity(intent);

		} else {
		}
		back();
	    }
	});
    };

    private void setInitView() {
	livingList.setPullLoadEnable(false);
	livingList.setPullRefreshEnable(false);
	livingList.setXListViewListener(this);
	livingList.setAdapter(adapter);

	livingList.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {

			UserInfo user = attentionList.get(position - 1);
		Intent userInfoIntent = new Intent(ShowAttentionActivity.this,
			UserInfoActivity.class);
		userInfoIntent.putExtra("account", user.account);
		userInfoIntent.putExtra("headurl", user.headurl);
		userInfoIntent.putExtra("nickname", user.nickname);
		startActivity(userInfoIntent);
		overridePendingTransition(R.anim.slide_in_from_left,
			R.anim.slide_out_to_right);

	    }
	});
	((TextView) emptyView.findViewById(R.id.empty_tv)).setText("正在加载中...");
	livingList.setEmptyView(emptyView);
	emptyView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		((TextView) emptyView.findViewById(R.id.empty_tv))
			.setText("加载数据中...");
		sendRequest(FRIST_GET_DATE);// 请求数据
	    }
	});
	if (CommonUtils.isNetWorkConnected(this)) {
	    sendRequest(FRIST_GET_DATE);// 请求数据
	} else {

	    ((TextView) emptyView.findViewById(R.id.empty_tv))
		    .setText("好像没有联上网哦，点此刷新");
	}
	// test();

    }

    TextView middleView;

    @Override
    public void initMiddle(TextView middleView) {
	super.initMiddle(middleView);
	middleView.setText("关注的人");
	this.middleView = middleView;
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

    private void requestParams(final int action) {
	if (!CommonUtils.isNetWorkConnected(this)) {
	    ToolUtils.showToast(this, "好像没有网络哦~~~检查下网络稍后再试吧");
	    return;
	}
	RequestParams params = new RequestParams();
	DeviceUuidFactory uuid = new DeviceUuidFactory(this);
	String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
	params.put("account", PreferenceUtils.getInstance(this)
		.getSettingUserAccount());
	params.put("access_token", uid);

	HttpRestClient.post(Constant.GETATTENTION_LIST, params,
		new BaseJsonHttpResponseHandler<AttentionListInfo>(
			AttentionListInfo.class) {

		    @Override
		    public void onSuccess(int statusCode, Header[] headers,
			    String rawJsonResponse, AttentionListInfo response) {

			switch (action) {
			case FRIST_GET_DATE:

			case REFRESH_GET_DATE:

			    if (HttpResponseUtil.isResponseOk(response)) {
				initAttentionList(response);

			    } else
				{
					if (response.getCode().equals("1999"))// 在其他地方登录
					{
						// 有缓存账号不跳登录界面

						HttpRequest.reLogin(ShowAttentionActivity.this);
					} else {
						ToolUtils.showToast(ShowAttentionActivity.this, response.msg);
					}
				}
			    break;

			case LOADMORE_GET_DATE:

			}

			onLoad();

		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers,
			    Throwable throwable, String rawJsonData,
			    AttentionListInfo errorResponse) {
			ToolUtils.showToastByStr(ShowAttentionActivity.this,
				"获取关注列表失败~~稍后再试吧");

		    }

		    @Override
		    protected AttentionListInfo parseResponse(
			    String rawJsonData, boolean isFailure)
			    throws Throwable {
			// TODO Auto-generated method stub
			return null;
		    }

		});

    }

    private void initAttentionList(AttentionListInfo response) {
	attentionList.clear();
	attentionList.addAll(response.getAttentionOthersList());
	for (int i = 0; i < attentionList.size(); i++) {
	    attentionList.get(i).isAttention = true;
	}
    }

    private void updateAttentionList(AttentionMeListInfo response) {
	attentionList.clear();
	attentionList.addAll(response.getAttentionMeList());

	List<String> attentionedL = AttentionUserDB.getInstance(this)
		.getAttentionList();
	System.out.println(" updateAttentionList      " + attentionedL.size());
	for (int i = 0; i < attentionList.size(); i++) {
	    if (attentionedL.contains(attentionList.get(i).account)) {

		System.out.println(" updateAttentionList      "
			+ attentionList.get(i));
		attentionList.get(i).isAttention = true;
	    }
	}
    }

    private void requestAttenttionMeParams(final int action) {
	if (!CommonUtils.isNetWorkConnected(this)) {
	    ToolUtils.showToast(this, "好像没有网络哦~~~检查下网络稍后再试吧");
	    return;
	}
	RequestParams params = new RequestParams();
	DeviceUuidFactory uuid = new DeviceUuidFactory(this);
	String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
	params.put("account", PreferenceUtils.getInstance(this)
		.getSettingUserAccount());
	params.put("access_token", uid);

	HttpRestClient.post(Constant.GETATTENTION_ME_LIST, params,
		new BaseJsonHttpResponseHandler<AttentionMeListInfo>(
			AttentionMeListInfo.class) {

		    @Override
		    public void onSuccess(int statusCode, Header[] headers,
			    String rawJsonResponse, AttentionMeListInfo response) {

			switch (action) {
			case FRIST_GET_DATE:

			case REFRESH_GET_DATE:

			    if (HttpResponseUtil.isResponseOk(response)) {

				updateAttentionList(response);

			    } else if (response.getStatus().equals("login"))// 在其他地方登录
			    {
				HttpRequest.reLogin(ShowAttentionActivity.this);
			    }

			    else {

				ToolUtils.showToastByStr(
					ShowAttentionActivity.this, "获取关注列表失败");
			    }

			    break;

			case LOADMORE_GET_DATE:

			}

			onLoad();

		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers,
			    Throwable throwable, String rawJsonData,
			    AttentionMeListInfo errorResponse) {
			ToolUtils.showToastByStr(ShowAttentionActivity.this,
				"获取关注列表失败~~稍后再试吧");

		    }

		    @Override
		    protected AttentionMeListInfo parseResponse(
			    String rawJsonData, boolean isFailure)
			    throws Throwable {
			// TODO Auto-generated method stub
			return null;
		    }

		});

    }

}
