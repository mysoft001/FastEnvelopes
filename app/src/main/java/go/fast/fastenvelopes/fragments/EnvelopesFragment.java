package go.fast.fastenvelopes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.EnvelopesListAdapter;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.EnvelopeTotalInfo;
import go.fast.fastenvelopes.info.EnvelopesInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PixelUtil;
import me.maxwin.view.XListView;


/**
 * 红包页面Fragment
 */
public class EnvelopesFragment extends BaseFragment implements XListView.IXListViewListener {

    List<EnvelopesInfo> envelopesInfoList;
    private XListView envelopesListv;


    public static EnvelopesFragment newInstance() {
	return new EnvelopesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

    }

private void setEnvelopesInfoList()
{
    String[] envelopesStr=new String[]{"猜金额红包","自由猜红包","近者得红包"};
    envelopesInfoList=new ArrayList<>();
    for (int i=0;i<2;i++)
    {
        EnvelopesInfo envelopesInfo=new EnvelopesInfo();
        envelopesInfo.envelopeName=envelopesStr[i];
        envelopesInfo.envelopeType=i+1;
        envelopesInfoList.add(envelopesInfo);
    }
}

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
      ViewGroup.LayoutParams lp= middleView.getLayoutParams();
        lp.height= PixelUtil.dp2Pixel(20,getActivity());
        lp.width= PixelUtil.dp2Pixel(45,getActivity());
        middleView.setText("");
        middleView.setBackgroundResource(R.drawable.envelope_icon_w);
    }

    private EnvelopesListAdapter envelopesListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.envelopes_fragment_layout, null);
        setEnvelopesInfoList();
        envelopesListAdapter = new EnvelopesListAdapter(getActivity(),R.layout.envelopes_list_item,envelopesInfoList);
        envelopesListv=(XListView)rootView.findViewById(R.id.list_lv);
        envelopesListv.setPullLoadEnable(false);
        envelopesListv.setPullRefreshEnable(false);
	setInitView();
	return rootView;
    }

    private void setInitView() {
        envelopesListv.setAdapter(envelopesListAdapter);
        getEnvelopeRoomInfo();
    }



    private void getEnvelopeRoomInfo()
    {
        HttpRequest.getEnvelopeRoomSize(getActivity(), new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {

                EnvelopeTotalInfo envelopeTotalInfo= (EnvelopeTotalInfo) response;

                if(HttpResponseUtil.isResponseOk(response))
                {
                    setData(envelopeTotalInfo);
                }



            }

            @Override
            public void onFailure(String rawJsonData) {

            }
        });
    }

    private void  setData(EnvelopeTotalInfo envelopeTotalInfo)
    {
        if(envelopeTotalInfo!=null)
        {
            envelopesInfoList.get(0).totalPeoples=envelopeTotalInfo.guess.totalPeoples;
            envelopesInfoList.get(0).totalRooms=envelopeTotalInfo.guess.totalRooms;
            envelopesInfoList.get(1).totalPeoples=envelopeTotalInfo.free.totalPeoples;
            envelopesInfoList.get(1).totalRooms=envelopeTotalInfo.free.totalRooms;
            envelopesListAdapter.notifyDataSetChanged();
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


}
