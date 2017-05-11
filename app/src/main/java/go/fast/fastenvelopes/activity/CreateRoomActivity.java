package go.fast.fastenvelopes.activity;

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

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.view.NiceSpinner;

/**
 * Created by hanwei on 2017/1/14.
 */

public class CreateRoomActivity extends BaseActivity {


    private EditText roomNameEdit;
    private EditText roomPsdEdit;
    private EditText roomMaxSizeEdit;
    private NiceSpinner roomPermissionN;
    private TextView envelopeTypeText;
    private Button createRoomBtn;
    private int envelopeType;


    List<String> permissList = new ArrayList<String>(Arrays.asList(
            "关注者可见", "所有人可见"));

    private String permissStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room_layout);

        initView();
        setInitView();
    }

    private  void initView()
    {
        envelopeType=getIntent().getIntExtra("type",1);

        envelopeTypeText=(TextView) findViewById(R.id.envelope_type_tv);
        roomNameEdit=(EditText) findViewById(R.id.room_name_et);
        roomPsdEdit=(EditText) findViewById(R.id.room_psd_et);
        roomMaxSizeEdit=(EditText) findViewById(R.id.room_person_size_et);
        roomPermissionN=(NiceSpinner)findViewById(R.id.room_cansee_nsp);
        createRoomBtn=(Button) findViewById(R.id.create_btn);

    }

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        middleView.setText("创建房间");
    }

    private void setInitView()
    {

        if(envelopeType==1)
        {
            envelopeTypeText.setText("猜金额红包");
        }
        else
        if(envelopeType==2)
        {
            envelopeTypeText.setText("自由猜红包");
        }
        else
        if(envelopeType==3)
        {
            envelopeTypeText.setText("近者得红包");
        }

        roomPermissionN.attachDataSource(permissList);
        permissStr = permissList.get(0);
        roomPermissionN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                permissStr= permissList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoomNow();
            }
        });
    }

    private void createRoomNow()
    {
if(ToolUtils.isNullOrEmpty(roomNameEdit.getText().toString()))
{
    ToolUtils.showToast(this,"请输入房间名称");
    return;
}
        int maxSize=-1;
        if(!ToolUtils.isNullOrEmpty(roomMaxSizeEdit.getText().toString()))
        {
            maxSize= Integer.valueOf(roomMaxSizeEdit.getText().toString());

        }

        int permission=permissStr.equals(permissList.get(0))?1:2;
        createRoom(roomNameEdit.getText().toString(), roomPsdEdit.getText().toString(), maxSize,
                permission);

    }


    private void createRoom(String roomName, String roomPsd, int maxSize,
                             int roomPermission) {
        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(CreateRoomActivity.this);
     String    uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("type", envelopeType);
        params.put("account", PreferenceUtils.getInstance(this)
                .getSettingUserAccount());
        params.put("name", roomName);
        if(!ToolUtils.isNullOrEmpty(roomPsd))
        {
            params.put("password", roomPsd);
        }
        if(maxSize>0)
        {
            params.put("maxCounts", maxSize);
        }

        params.put("mode", roomPermission);

        HttpRestClient.post(Constant.CREATE_ROOM, params,
                new BaseJsonHttpResponseHandler<BaseInfo>(
                        BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {
                        System.out.println("CREATE_ROOM------"
                                + rawJsonResponse);

                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            ToolUtils.showToast(CreateRoomActivity.this,"创建房间成功");
                            finish();
                        }
                        else
                        {
                            if(response.getCode().equals("1999"))
                            {
                                HttpRequest.reLogin(CreateRoomActivity.this);
                            }
                            else
                            {
                                ToolUtils.showToast(CreateRoomActivity.this,response.msg);
                            }

                        }

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        ToolUtils.showToast(CreateRoomActivity.this,"创建房间失败");

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
