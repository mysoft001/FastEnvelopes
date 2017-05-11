package go.fast.fastenvelopes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dialogplus.DialogPlus;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.PublicViewUtils;
import go.fast.fastenvelopes.utils.ToolUtils;

/**
 * Created by hanwei on 2017/1/14.
 */

public class CreateFreeEnvelopeActivity extends BaseActivity {


    private EditText goldSizeEdit;//红包大小
    private EditText envelopeSizeEdit;//红包个数
//    private EditText roomPsdEdit;
//    private EditText roomMaxSizeEdit;
   // private NiceSpinner envelopePermissionN;//红包权限选择
    private TextView totalPersonText;
    private TextView envelopeTypeText;
    private Button createEnvelopeBtn;
    private int envelopeType;
    private RoomItemInfo roomInfo;


//    List<String> permissList = new ArrayList<String>(Arrays.asList(
//            "所有人可抢", "参与者可抢"));

    private String permissStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_free_envelope_layout);

        initView();
        setInitView();
    }

    private  void initView()
    {
        envelopeType=getIntent().getIntExtra("type",1);
        roomInfo= (RoomItemInfo) getIntent().getSerializableExtra("roomInfo");
        envelopeTypeText=(TextView) findViewById(R.id.envelope_type_tv);
        envelopeTypeText.setText("自由猜红包");
        totalPersonText=(TextView) findViewById(R.id.total_person_tv);
        goldSizeEdit=(EditText) findViewById(R.id.gold_size_et);
        envelopeSizeEdit=(EditText) findViewById(R.id.envelope_size_et);
      //  envelopePermissionN=(NiceSpinner)findViewById(R.id.permission_nsp);
        createEnvelopeBtn=(Button) findViewById(R.id.create_btn);
    }

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        middleView.setText("发起红包");
    }

    private void setInitView()
    {


//        envelopePermissionN.attachDataSource(permissList);
//        permissStr = permissList.get(0);
//        envelopePermissionN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                permissStr= permissList.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        if(roomInfo!=null)
        {
            totalPersonText.setText(roomInfo.roomCounts+"");
        }


        createEnvelopeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crateEnvelope();
            }
        });


    }

    private void crateEnvelope()
    {
        final String goldSize=goldSizeEdit.getText().toString();
       final  String envelopesSize=envelopeSizeEdit.getText().toString();
        int roomPersonalSize=5;
        if(roomInfo!=null)
        {
            roomPersonalSize=roomInfo.roomCounts;

        }
        if(ToolUtils.isNullOrEmpty(goldSize))
        {
            ToolUtils.showToast(this,"请输入红包总金额");
            return;
        }
        if(ToolUtils.isNullOrEmpty(envelopesSize))
        {
            ToolUtils.showToast(this,"请输入红包个数");
            return;
        }

        int goldSizeInt= Integer.valueOf(goldSize);
        int envelopesSizeInt=Integer.valueOf(envelopesSize);
//        if(envelopesSizeInt>roomPersonalSize)
//        {
//            ToolUtils.showToast(this,"红包个数不能大于当前房间的人数");
//            return;
//        }
        if(goldSizeInt<envelopesSizeInt)
        {
            ToolUtils.showToast(this,"平均每个红包至少1个金币。请增加红包金额或减少红包个数。");
            return;
        }


        PublicViewUtils.showConsumeGold(this, "支付" + goldSize + "金币发起"+"自由猜红包"+"。",goldSizeInt, new PublicViewUtils.OnPayGoldListener() {
            @Override
            public void payGold(DialogPlus dialogPlus) {

                createEnvelope(roomInfo.roomId, Integer.valueOf(goldSize), Integer.valueOf(envelopesSize) );
                dialogPlus.dismiss();
            }
        });
    }


    private void createEnvelope(String roomId, int goldSize,
                                int envelopeSize) {
        RequestParams params = new RequestParams();

        DeviceUuidFactory uuid = new DeviceUuidFactory(CreateFreeEnvelopeActivity.this);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(this)
                .getSettingUserAccount());
        params.put("roomId", roomId);

        params.put("maxCounts", envelopeSize);
        params.put("money", goldSize);
        params.put("type", CommonUtils.ENVELOPE_TYPE_FREE);//红包类型
       // params.put("envelopeSize", envelopeSize);

        HttpRestClient.post(Constant.CREATE_FREE_ENVELOPE, params,
                new BaseJsonHttpResponseHandler<BaseInfo>(
                        BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {
                        System.out.println("CREATE_ENVELOPE------"
                                + rawJsonResponse);
                        if (HttpResponseUtil.isResponseOk(response)) {
                            ToolUtils.showToast(CreateFreeEnvelopeActivity.this, "发起红包成功");
                            finish();
                        } else {

                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                Intent loginIntent = new Intent(CreateFreeEnvelopeActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                ToolUtils.showToast(CreateFreeEnvelopeActivity.this, "请先登录");
                                //finish();
                            }
                            else
                            if(response.getCode().equals("3000"))//当前已有红包
                            {
                                ToolUtils.showToast(CreateFreeEnvelopeActivity.this, response.msg);
                            }
                            else
                            if(response.getCode().equals("2005"))//余额不足
                            {
                                ToolUtils.showToast(CreateFreeEnvelopeActivity.this, response.msg);
                            }
                            else
                            {
                                ToolUtils.showToast(CreateFreeEnvelopeActivity.this, response.msg);
                            }

                        }
                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        System.out.println("CREATE_ENVELOPE-----onFailure   -"
                                + rawJsonData);
                        ToolUtils.showToast(CreateFreeEnvelopeActivity.this, "发起红包失败");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        // TODO Auto-generated method stub
                        return null;
                    }

                });

    }



    @Override
    protected void onResume() {
        super.onResume();
    }
}
