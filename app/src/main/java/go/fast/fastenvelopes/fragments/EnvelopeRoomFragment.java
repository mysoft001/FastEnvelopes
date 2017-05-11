package go.fast.fastenvelopes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.ChatEnvelopeActivity;
import go.fast.fastenvelopes.adapters.RoomListAdapter;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.info.RoomListInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;


/**
 * 抢红包页面Fragment
 */
public class EnvelopeRoomFragment extends BaseFragment implements XListView.IXListViewListener {
    private static final String TAG = "FontsFragment";


private 	XListView sessionListv;
	private EditText searchEdit;
	private Button searchBtn;
	private View emptyView;
	private List<RoomItemInfo> roomList;
	RoomListAdapter roomListAdapter;
	View headView;

	private String[] IMG_URL_LIST = {
			"https://pic4.zhimg.com/02685b7a5f2d8cbf74e1fd1ae61d563b_xll.jpg",
			"https://pic4.zhimg.com/fc04224598878080115ba387846eabc3_xll.jpg",
			"https://pic3.zhimg.com/d1750bd47b514ad62af9497bbe5bb17e_xll.jpg",
			"https://pic4.zhimg.com/da52c865cb6a472c3624a78490d9a3b7_xll.jpg",
			"https://pic3.zhimg.com/0c149770fc2e16f4a89e6fc479272946_xll.jpg",
			"https://pic1.zhimg.com/76903410e4831571e19a10f39717988c_xll.png",
			"https://pic3.zhimg.com/33c6cf59163b3f17ca0c091a5c0d9272_xll.jpg",
			"https://pic4.zhimg.com/52e093cbf96fd0d027136baf9b5cdcb3_xll.png",
			"https://pic3.zhimg.com/f6dc1c1cecd7ba8f4c61c7c31847773e_xll.jpg",
	};

    public static EnvelopeRoomFragment newInstance() {
	return new EnvelopeRoomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.room_fragment_layout, null);
		emptyView = rootView.findViewById(R.id.empty_layout);
		 headView= inflater.inflate(R.layout.header_search_edit, null);
		sessionListv=(XListView)rootView.findViewById(R.id.list_lv) ;
		sessionListv.setPullRefreshEnable(true);
		sessionListv.setPullLoadEnable(false);
		sessionListv.setXListViewListener(this);
		searchEdit=(EditText)headView.findViewById(R.id.et_sendmessage);
		searchBtn=(Button)headView.findViewById(R.id.btn_send);
		searchEdit.setHint("请输入搜索的房间号或房间名");
	setInitView();
	return rootView;
    }

    private void setInitView() {
		roomList=new ArrayList<>();

		 List<String> mPostList1=new ArrayList<>();

		for(int i=0;i<4;i++){
			mPostList1.add(IMG_URL_LIST[i]);
		}

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
					if(searchInfoList!=null&&searchInfoList.size()>0)
					{
						roomList.removeAll(searchInfoList);
						searchInfoList.clear();
						roomListAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		 roomListAdapter=new RoomListAdapter(getActivity(),R.layout.room_list_item,roomList);
		sessionListv.addHeaderView(headView);
		sessionListv.setAdapter(roomListAdapter);




		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchRoomList();
			}
		});

		sessionListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position>1)
				{
					InRoom(roomList.get(position-2));
				}
			}
		});

		((TextView) emptyView.findViewById(R.id.empty_tv)).setText("正在加载中...");
		sessionListv.setEmptyView(emptyView);
		emptyView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((TextView) emptyView.findViewById(R.id.empty_tv))
						.setText("加载数据中...");

				requestRoomList();
			}
		});
		if (CommonUtils.isNetWorkConnected(getActivity())) {
			requestRoomList();
		} else {

			((TextView) emptyView.findViewById(R.id.empty_tv))
					.setText("好像没有联上网哦，点此刷新~~~~");

		}



    }
	private void InRoom(RoomItemInfo roomInfo)
	{
		if(roomInfo.roomIsPass==1)
		{
			if(PreferenceUtils.getInstance(getActivity()).getSettingUserAccount().equals(roomInfo.roomCreatedAccount))//是创建者自己不需要输入密码
			{
				goChatActivity(roomInfo);
			}
			else
			{
				showPsdDialog(roomInfo);
			}
		}
		else
		{
			goChatActivity(roomInfo);
		}


	}


	private List<RoomItemInfo> searchInfoList;
	private void searchRoomList()
	{

		if(ToolUtils.isNullOrEmpty(searchEdit.getText().toString()))
		{
			ToolUtils.showToast(getActivity(),"请输入要搜索的房间号或者名称");
			return;
		}
		HttpRequest.getSearchRoomList(getActivity(), searchEdit.getText().toString(), new HttpRequest.onRequestCallback() {
			@Override
			public void onSuccess(BaseInfo response) {

				RoomListInfo roomListInfo= (RoomListInfo) response;
				if(HttpResponseUtil.isResponseOk(response))
				{
					if(roomListInfo.list==null||roomListInfo.list.size()==0)
					{
						ToolUtils.showToast(getActivity(),"未搜索到任何结果");
					}
					else
					{
						if(searchInfoList!=null&&searchInfoList.size()>0)//先将之前的结果去掉
						{
							roomList.removeAll(searchInfoList);
							searchInfoList.clear();
						}
						searchInfoList=roomListInfo.list;
						roomList.addAll(0,searchInfoList);
						roomListAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(String rawJsonData) {

			}
		});


	}

	private void requestRoomList()
	{
		HttpRequest.getRoomList(getActivity(), new HttpRequest.onRequestCallback() {
			@Override
			public void onSuccess(BaseInfo response) {
				RoomListInfo roomListInfo=(RoomListInfo) response;

				if(HttpResponseUtil.isResponseOk(response))
				{
					if(roomListInfo.list!=null&&roomListInfo.list.size()>0)
					{

						roomList.clear();
						roomList.addAll(roomListInfo.list);
						roomListAdapter.notifyDataSetChanged();
					}
					else
					{
						ToolUtils.showToast(getActivity(),"获取房间列表失败");
					}
				}

				onLoad();
			}

			@Override
			public void onFailure(String rawJsonData) {
				onLoad();
			}
		});
	}


	private void onLoad() {// 防止多个线程同时调用
		// TODO Auto-generated method stub
		sessionListv.stopRefresh();
		sessionListv.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日   HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		sessionListv.setRefreshTime(str);

		if (roomList == null || roomList.size() == 0) {
			emptyView.setVisibility(View.VISIBLE);
			if (CommonUtils.isNetWorkConnected(getActivity())) {
				((TextView) emptyView.findViewById(R.id.empty_tv))
						.setText("还没有房间");
			} else {
				((TextView) emptyView.findViewById(R.id.empty_tv))
						.setText("好像没有联上网哦，点此刷新");
			}

		} else {

			emptyView.setVisibility(View.GONE);
		}
	}
	 DialogPlus dialog;
	private void showPsdDialog(final RoomItemInfo roomInfo)
	{
		DialogPlusBuilder dialogPlusB= DialogPlus.newDialog(getActivity());

		dialogPlusB.setContentHolder(new ViewHolder(R.layout.show_input_psd_dialog));
		dialogPlusB.setContentBackgroundResource(android.R.color.transparent);
		dialogPlusB.setGravity(Gravity.CENTER);
		//dialogPlusB.setCancelable(isCancelable);
	 dialog=  dialogPlusB.create();
		final EditText psdEdit=((EditText) dialog.getHolderView().findViewById(R.id.input_psd_et));
		TextView postiveText=((TextView)dialog.getHolderView().findViewById(R.id.postive_tv));
		TextView negtiveText=((TextView)dialog.getHolderView().findViewById(R.id.negtive_tv));
		postiveText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(ToolUtils.isNullOrEmpty(psdEdit.getText().toString()))
				{
					ToolUtils.showToast(getActivity(),"请输入房间密码");
					return;
				}
				checkPsd(psdEdit.getText().toString(),roomInfo);
				//dialog.dismiss();
			}
		});
		negtiveText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	private void checkPsd(String psd, final RoomItemInfo roomInfo)
	{
HttpRequest.checkRoomPsd(getActivity(), roomInfo.roomId, psd, new HttpRequest.onRequestCallback() {
	@Override
	public void onSuccess(BaseInfo response) {
		if(HttpResponseUtil.isResponseOk(response))//密码正确
		{
			goChatActivity(roomInfo);
			dialog.dismiss();
		}
		else
		{
      if(response.getCode().equals("2008"))
	  {
		  ToolUtils.showToast(getActivity(),"密码不正确，请检查后重试");
	  }
			else  if(response.getCode().equals("2007"))
	  {
		  dialog.dismiss();
		  ToolUtils.showToast(getActivity(),"房间不存在");
	  }

		}
	}

	@Override
	public void onFailure(String rawJsonData) {
		dialog.dismiss();
ToolUtils.showToast(getActivity(),"验证密码出错，请稍后再试或联系我们");
	}
});
	}


private void goChatActivity(RoomItemInfo roomInfo)
{
	Intent chatIntent=new Intent(getActivity(), ChatEnvelopeActivity.class);
	chatIntent.putExtra("roomInfo",roomInfo);
	startActivity(chatIntent);
}


	@Override
	public void initMiddle(TextView middleView) {
		super.initMiddle(middleView);
		ViewGroup.LayoutParams lp= middleView.getLayoutParams();
		lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
		lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
		middleView.setText("正在抢");
		middleView.setBackgroundResource(R.color.transparent_color);
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
		requestRoomList();
	}

	@Override
	public void onLoadMore() {

	}
}
