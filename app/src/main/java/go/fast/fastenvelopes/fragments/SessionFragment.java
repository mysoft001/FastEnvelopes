package go.fast.fastenvelopes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.PersonalLetterActivity;
import go.fast.fastenvelopes.adapters.SessionListAdapter;
import go.fast.fastenvelopes.db.CoolDBHelper;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.SearchUserInfo;
import go.fast.fastenvelopes.info.SessionItemInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.view.CoolDialogView;
import me.maxwin.view.XListView;


/**
 * 会话页面Fragment
 */
public class SessionFragment extends BaseFragment implements XListView.IXListViewListener,View.OnClickListener {
    private static final String TAG = "FontsFragment";


private 	XListView sessionListv;
	private EditText searchEdit;
	private Button searchBtn;
	View headView;
	private View emptyView;
	private List<SessionItemInfo> sessionItemInfoList;

	private List<SessionItemInfo> searchSessionList;

	SessionListAdapter sessionListAdapter;

    public static SessionFragment newInstance() {
	return new SessionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

    }


	@Override
	public void initMiddle(TextView middleView) {
		super.initMiddle(middleView);
		ViewGroup.LayoutParams lp= middleView.getLayoutParams();
		lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
		lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
		middleView.setText("会话");
		middleView.setBackgroundResource(R.color.transparent_color);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.session_fragment_layout, null);
		headView= inflater.inflate(R.layout.header_search_edit, null);
		sessionListv=(XListView)rootView.findViewById(R.id.list_lv) ;
		searchEdit=(EditText)headView.findViewById(R.id.et_sendmessage);
		searchBtn=(Button)headView.findViewById(R.id.btn_send);
		emptyView = rootView.findViewById(R.id.empty_layout);
	setInitView();
	return rootView;
    }

	@Override
	public void onResume() {
		super.onResume();
		getSessionList();
	}

	private void setInitView() {
		sessionItemInfoList=new ArrayList<>();
		sessionListv.setPullRefreshEnable(false);
		sessionListv.setPullLoadEnable(false);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchUserList();
			}
		});

		 sessionListAdapter=new SessionListAdapter(getActivity(),R.layout.room_list_item,sessionItemInfoList);
		sessionListv.addHeaderView(headView);
		sessionListv.setAdapter(sessionListAdapter);
		sessionListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println(" 点击了----会话       "+position);
				UserInfo userInfo=new UserInfo();
				userInfo.account=sessionItemInfoList.get(position-2).account;
				userInfo.headurl=sessionItemInfoList.get(position-2).headurl;
				userInfo.nickName=sessionItemInfoList.get(position-2).nickname;
				Intent personalIntent = new Intent(getActivity(), PersonalLetterActivity.class);
				personalIntent.putExtra("userInfo", userInfo);
				startActivity(personalIntent);
			}
		});

		sessionListv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				showLongClick(position-2);
				return true;
			}
		});

		searchEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {//搜索内容发生改变删除搜索结果



			}

			@Override
			public void afterTextChanged(Editable s) {
				if(ToolUtils.isNullOrEmpty(searchEdit.getText().toString()))
				{
					if(searchSessionList!=null&&searchSessionList.size()>0)
					{
						sessionItemInfoList.removeAll(searchSessionList);
						searchSessionList.clear();
						sessionListAdapter.notifyDataSetChanged();
					}
				}
			}
		});

    }

	private void getSessionList() {

		List<SessionItemInfo> sessionInfoList= CoolDBHelper.getSessionList(getActivity(),30);

		if (sessionInfoList.size() > 0) {//如果有拉取到新的数据那么刷新

			sessionItemInfoList.clear();
			sessionItemInfoList.addAll(sessionInfoList);
			sessionListAdapter.notifyDataSetChanged();

		} else {


		}

		if(sessionItemInfoList.size()>0)
		{
			sessionListv.setBackgroundResource(R.color.white);
			emptyView.setVisibility(View.GONE);
		}
		else
		{
			sessionListv.setBackgroundResource(R.color.transparent);
			emptyView.setVisibility(View.VISIBLE);
			((TextView) emptyView.findViewById(R.id.empty_tv)).setText("还没有和任何人联系过");
		}
	}

	int choosedPostion;
	private void showLongClick(int postion)
	{
		choosedPostion=postion;
		CoolDialogView dialog = new CoolDialogView();
		// dialog.addNormalButton("现在就拍", this, 4);
		dialog.addNormalButton("删除该会话", this, 5);
		dialog.addNormalButton("取消", this, 3);
		dialog.show( getActivity());



	}


	private void searchUserList()
	{

		if(ToolUtils.isNullOrEmpty(searchEdit.getText().toString()))
		{
			ToolUtils.showToast(getActivity(),"请输入要搜索的用户账号或者昵称");
			return;
		}
		HttpRequest.getSearchUserList(getActivity(), searchEdit.getText().toString(), new HttpRequest.onRequestCallback() {
			@Override
			public void onSuccess(BaseInfo response) {

				SearchUserInfo roomListInfo= (SearchUserInfo) response;
				if(HttpResponseUtil.isResponseOk(response))
				{
					if(roomListInfo.userInfoList==null||roomListInfo.userInfoList.size()==0)
					{
						sessionItemInfoList.removeAll(searchSessionList);
						searchSessionList.clear();
						sessionListAdapter.notifyDataSetChanged();
						ToolUtils.showToast(getActivity(),"未找到任何用户");
					}
					else
					{
						if(searchSessionList!=null&&searchSessionList.size()>0)//先将之前的结果去掉
						{
							sessionItemInfoList.removeAll(searchSessionList);
							searchSessionList.clear();
						}
						searchSessionList=roomListInfo.userInfoList;
						setSearchSessionList( searchSessionList,sessionItemInfoList);
						sessionItemInfoList.addAll(0,searchSessionList);
						sessionListAdapter.notifyDataSetChanged();
					}
				}
				else
				{
					if (response.getCode().equals("1999"))// 在其他地方登录
					{
						// 有缓存账号不跳登录界面

						HttpRequest.reLogin(getActivity());
					} else {
						ToolUtils.showToast(getActivity(), response.msg);
					}
				}


			}

			@Override
			public void onFailure(String rawJsonData) {

			}
		});


	}

	/**
	 * 对搜索结果比对整理

	 */
	private void setSearchSessionList( List<SessionItemInfo> searchSessionList, List<SessionItemInfo> sessionItemInfoList)
	{

		for(int i=0;i<searchSessionList.size();i++)
		{
			for(int s=0;s<sessionItemInfoList.size();s++)
			{
				if(sessionItemInfoList.get(s).account.equals(searchSessionList.get(i).account))//如果列表里已有了
				{
					searchSessionList.get(i).lastContent=sessionItemInfoList.get(s).lastContent;
					searchSessionList.get(i).lastPublishTime=sessionItemInfoList.get(s).lastPublishTime;
					break;
				}
			}

		}



	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
    }


	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case 5://删除指定会话

				if(choosedPostion>-1)
				{
					//删除该用户本地所有的聊天记录
					CoolDBHelper.deleteAllMsgById(getActivity(), sessionItemInfoList.get(choosedPostion).account);
					//删除本地会话表中的会话记录
					CoolDBHelper.deleteASessionByAccount(getActivity(),sessionItemInfoList.get(choosedPostion).account);
					sessionItemInfoList.remove(choosedPostion);
					sessionListAdapter.notifyDataSetChanged();
					choosedPostion=-1;
					if(sessionItemInfoList.size()>0)
					{
						sessionListv.setBackgroundResource(R.color.white);
						emptyView.setVisibility(View.GONE);
					}
					else
					{
						sessionListv.setBackgroundResource(R.color.transparent);
						emptyView.setVisibility(View.VISIBLE);
						((TextView) emptyView.findViewById(R.id.empty_tv)).setText("还没有和任何人联系过");
					}
				}

				break;
			case 4:
				break;
		}
	}
}
