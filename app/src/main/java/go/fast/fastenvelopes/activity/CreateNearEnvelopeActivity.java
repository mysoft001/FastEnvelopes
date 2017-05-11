package go.fast.fastenvelopes.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import dialogplus.DialogPlus;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.utils.PublicViewUtils;
import go.fast.fastenvelopes.utils.ToolUtils;

/**
 * Created by hanwei on 2017/1/14.
 */

public class CreateNearEnvelopeActivity extends BaseActivity {


    private EditText goldSizeEdit;//红包大小
    private EditText joinSizeEdit;//最大参与的人数
    private EditText myselfAnswerEdit;//设定红包答案
//    private EditText roomPsdEdit;
//    private EditText roomMaxSizeEdit;
   // private NiceSpinner envelopePermissionN;//红包权限选择
    private TextView totalPersonText;
    private TextView envelopeTypeText;
    private RadioGroup answerTypeRG;
    private Button createEnvelopeBtn;
    private int envelopeType;


//    List<String> permissList = new ArrayList<String>(Arrays.asList(
//            "所有人可抢", "参与者可抢"));

    private String permissStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_near_envelope_layout);

        initView();
        setInitView();
    }

    private  void initView()
    {
        envelopeType=getIntent().getIntExtra("type",1);
        answerTypeRG=(RadioGroup)findViewById(R.id.answer_rg);
        envelopeTypeText=(TextView) findViewById(R.id.envelope_type_tv);
        envelopeTypeText.setText("近者得红包");
        totalPersonText=(TextView) findViewById(R.id.total_person_tv);
        goldSizeEdit=(EditText) findViewById(R.id.gold_size_et);
        joinSizeEdit=(EditText) findViewById(R.id.max_join_et);
        myselfAnswerEdit=(EditText) findViewById(R.id.answer_myselfset_et);
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
        SpannableString ss = new SpannableString("请输入最大参与人数（不输入将无限制）");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(10,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        joinSizeEdit.setHint(new SpannedString(ss));
        answerTypeRG.check(R.id.random_rb);//默认系统分配答案
        myselfAnswerEdit.setVisibility(View.GONE);
        answerTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.print("选中的是----"+checkedId);
             if(checkedId==R.id.myself_rb)//本人设定
             {
                 myselfAnswerEdit.setVisibility(View.VISIBLE);
             }
                else
             {
                 myselfAnswerEdit.setVisibility(View.GONE);
             }
            }
        });

        createEnvelopeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crateEnvelope();
            }
        });


    }

    private void crateEnvelope()
    {
        String goldSize=goldSizeEdit.getText().toString();
        String maxJoinSize=joinSizeEdit.getText().toString();
        if(ToolUtils.isNullOrEmpty(goldSize))
        {
            ToolUtils.showToast(this,"请输入参与的红包金额");
            return;
        }

        if(ToolUtils.isNullOrEmpty(maxJoinSize))
        {
//            ToolUtils.showToast(this,"请输入最大参与的人数");
//            return;
        }
        else
        {
            int JoinSize=Integer.valueOf(maxJoinSize);
        }
        String answer="-1";
        if(answerTypeRG.getCheckedRadioButtonId()==R.id.myself_rb)//自己设定红包领取的答案
        {
            String myselfAnswer=myselfAnswerEdit.getText().toString();
            if(ToolUtils.isNullOrEmpty(myselfAnswer))
            {
            ToolUtils.showToast(this,"请输入红包答案，以此来决定红包被谁领走");
            return;
            }
            else
            {
                answer=myselfAnswer;
            }
        }


        PublicViewUtils.showConsumeGold(this, "支付" + goldSize + "金币发起"+"近者得红包"+"。", Integer.valueOf(goldSize), new PublicViewUtils.OnPayGoldListener() {
            @Override
            public void payGold(DialogPlus dialogPlus) {
                ToolUtils.showToast(CreateNearEnvelopeActivity.this,"发红包成功");
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
    }
}
