package go.fast.fastenvelopes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import go.fast.fastenvelopes.view.NiceSpinner;

/**
 * Created by hanwei on 2017/1/14.
 */

public class CreateGuessEnvelopeActivity extends BaseActivity {


    private EditText goldSizeEdit;
    //    private EditText roomPsdEdit;
//    private EditText roomMaxSizeEdit;
    private NiceSpinner envelopePermissionN;//红包权限选择
    private TextView envelopeTypeText;
    private Button createRoomBtn;
    private int envelopeType;

    private RoomItemInfo roomInfo;

    private TextView envelopeInstructText;

    List<String> permissList = new ArrayList<String>(Arrays.asList(
            "所有人可抢", "参与者可抢"));

    private String permissStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_envelope_layout);

        initView();
        setInitView();
    }

    private void initView() {
        envelopeType = getIntent().getIntExtra("type", 1);
        roomInfo = (RoomItemInfo) getIntent().getSerializableExtra("roomInfo");
        envelopeInstructText = (TextView) findViewById(R.id.envelope_instruct_tv);

        envelopeTypeText = (TextView) findViewById(R.id.envelope_type_tv);
        goldSizeEdit = (EditText) findViewById(R.id.gold_size_et);
        envelopePermissionN = (NiceSpinner) findViewById(R.id.permission_nsp);
        createRoomBtn = (Button) findViewById(R.id.create_btn);
    }

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        middleView.setText("发起红包");
    }

    private void setInitView() {


        envelopePermissionN.attachDataSource(permissList);
        permissStr = permissList.get(0);

        envelopeInstructText.setText(getResources().getString(R.string.envelope_guess_instruct_all));

        envelopePermissionN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    envelopeInstructText.setText(getResources().getString(R.string.envelope_guess_instruct_all));
                } else {
                    envelopeInstructText.setText(getResources().getString(R.string.envelope_guess_instruct_join));
                }
                permissStr = permissList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crateEnvelope();
            }
        });


    }

    private void crateEnvelope() {
        final String goldSize = goldSizeEdit.getText().toString();
        if (ToolUtils.isNullOrEmpty(goldSize)) {
            ToolUtils.showToast(this, "请输入参与的红包金额");
            return;
        }

        PublicViewUtils.showConsumeGold(this, "消费" + goldSize + "金币创建并加入猜金额红包。", Integer.valueOf(goldSize), new PublicViewUtils.OnPayGoldListener() {
            @Override
            public void payGold(DialogPlus dialogPlus) {

                dialogPlus.dismiss();
                createEnvelope(roomInfo.roomId, Integer.valueOf(goldSize),
                        permissStr.equals(permissList.get(0)) ? CommonUtils.ENVELOPE_PERMISSION_ALL : CommonUtils.ENVELOPE_PERMISSION_JOIN);

            }
        });
    }


    private void createEnvelope(String roomId, int goldSize,
                                int roomPermission) {
        RequestParams params = new RequestParams();

        DeviceUuidFactory uuid = new DeviceUuidFactory(CreateGuessEnvelopeActivity.this);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(this)
                .getSettingUserAccount());
        params.put("roomId", roomId);

        //  params.put("maxCounts", maxSize);
        params.put("money", goldSize);
        params.put("type", CommonUtils.ENVELOPE_TYPE_GUESS);//红包类型
        params.put("power", roomPermission);

        HttpRestClient.post(Constant.CREATE_ENVELOPE, params,
                new BaseJsonHttpResponseHandler<BaseInfo>(
                        BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {
                        System.out.println("CREATE_ENVELOPE------"
                                + rawJsonResponse);
                        if (HttpResponseUtil.isResponseOk(response)) {
                            ToolUtils.showToast(CreateGuessEnvelopeActivity.this, "发起红包成功");
                            finish();
                        } else {

                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                Intent loginIntent = new Intent(CreateGuessEnvelopeActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                ToolUtils.showToast(CreateGuessEnvelopeActivity.this, "请先登录");
                                //finish();
                            }
                            else
                            if (response.getCode().equals("3000"))//当前已有红包
                            {
                                ToolUtils.showToast(CreateGuessEnvelopeActivity.this, response.msg);
                            } else if (response.getCode().equals("2005"))//余额不足
                            {
                                ToolUtils.showToast(CreateGuessEnvelopeActivity.this, response.msg);
                            }
                            else
                            {
                                ToolUtils.showToast(CreateGuessEnvelopeActivity.this, response.msg);
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
                        ToolUtils.showToast(CreateGuessEnvelopeActivity.this, "发起红包失败");
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
