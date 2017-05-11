package go.fast.fastenvelopes.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.FeedbackListAdapter;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.FeedbackItemInfo;
import go.fast.fastenvelopes.info.FeedbackItemObj;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

public class FeedbackListActivity extends BaseActivity implements
	IXListViewListener {

    private EditText feedbackEdit;

    private XListView livingList;
    public static final int FRIST_GET_DATE = 111;// 首次加载
    public static final int REFRESH_GET_DATE = 112;// 刷新数据
    public static final int LOADMORE_GET_DATE = 113;// 获取更多数据
    FeedbackListAdapter feedbackListAdapter;
    private int page = 1;

    private View emptyView;

    List<FeedbackItemObj> feedbackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.feedbacklist_layout);
	initView();
    }

    private void initView() {

	emptyView = findViewById(R.id.empty_layout);
	feedbackEdit = (EditText) findViewById(R.id.input_et);
	livingList = (XListView) findViewById(R.id.living_lv);
	feedbackList = new ArrayList<FeedbackItemObj>();
	feedbackListAdapter = new FeedbackListAdapter(this,
		R.layout.feedback_list_item, feedbackList);

	livingList.setAdapter(feedbackListAdapter);

	livingList.setPullLoadEnable(true);
	livingList.setPullRefreshEnable(true);
	livingList.setXListViewListener(this);
	// test();
	((TextView) emptyView.findViewById(R.id.empty_tv)).setText("正在加载中...");
	livingList.setEmptyView(emptyView);
	emptyView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		sendRequest(FRIST_GET_DATE);// 请求数据
	    }
	});
	if (CommonUtils.isNetWorkConnected(this)) {
	    sendRequest(FRIST_GET_DATE);// 请求数据
	} else {

	    ((TextView) emptyView.findViewById(R.id.empty_tv))
		    .setText("好像没有联上网哦，点此刷新~~~~");

	}
    }

    @Override
    public void initMiddle(TextView middleView) {
	super.initMiddle(middleView);
	middleView.setText("反馈列表");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
//	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    public void sendRequest(final int ACTION) {
	switch (ACTION) {
	case FRIST_GET_DATE:// 第一次加载数据
	    requestParams(FRIST_GET_DATE);
	    break;
	case REFRESH_GET_DATE:// 刷新数据
	    requestParams(REFRESH_GET_DATE);
	    break;
	case LOADMORE_GET_DATE:// 获取更多数据
	    requestParams(LOADMORE_GET_DATE);
	    break;
	}

    }

    @Override
    public void onRefresh() {
	sendRequest(REFRESH_GET_DATE);
	// testRequest();
    }

    @Override
    public void onLoadMore() {
	sendRequest(LOADMORE_GET_DATE);
	// testRequest();
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
	feedbackListAdapter.notifyDataSetChanged();

	if (feedbackList.size() == 0) {
	    ((TextView) emptyView.findViewById(R.id.empty_tv))
		    .setText("没有反馈消息");
	}

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
	    onLoad();
	    return;
	}
	System.out.println("registerToServer----TalkingFragment");
	RequestParams params = new RequestParams();
	DeviceUuidFactory uuid = new DeviceUuidFactory(this);
	String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
	params.put("access_token", uid);
	params.put("account", PreferenceUtils.getInstance(this)
		.getSettingUserAccount());
	if (LOADMORE_GET_DATE == action) {
	    params.put("page", page);// 当前页
	}

	// String Street =
	HttpRestClient.post(Constant.GET_FEEDBACKLIST, params,
		new BaseJsonHttpResponseHandler<FeedbackItemInfo>(
			FeedbackItemInfo.class) {

		    @Override
		    public void onSuccess(int statusCode, Header[] headers,
			    String rawJsonResponse, FeedbackItemInfo response) {

			if (HttpResponseUtil.isResponseOk(response)) {

			    switch (action) {
			    case FRIST_GET_DATE:

			    case REFRESH_GET_DATE:
				page = 2;
				feedbackList.clear();
				feedbackList.addAll(response.questionList);
				break;

			    case LOADMORE_GET_DATE:
				if (response.questionList == null
					|| response.questionList.size() == 0) {
				    ToolUtils.showToastByStr(
					    FeedbackListActivity.this,
					    "已经是最后一页啦~~~~~");
				    onLoad();
				    return;
				}
				feedbackList.addAll(response.questionList);
				page++;
				break;

			    }

			    // talkingAdapter.notifyDataSetChanged();

			}
			else if(response.getStatus().equals("login"))//在其他地方登录
			{
			    HttpRequest.reLogin(FeedbackListActivity.this);
			}
			else {

			    ToolUtils.showToastByStr(FeedbackListActivity.this,
				    "获取反馈列表失败");
			}
			onLoad();
		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers,
			    Throwable throwable, String rawJsonData,
			    FeedbackItemInfo errorResponse) {
			ToolUtils.showToastByStr(FeedbackListActivity.this,
				"获取反馈列表失败");
			onLoad();

		    }

		    @Override
		    protected FeedbackItemInfo parseResponse(
			    String rawJsonData, boolean isFailure)
			    throws Throwable {
			// TODO Auto-generated method stub
			return null;
		    }

		});

    }

}
