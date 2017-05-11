package go.fast.fastenvelopes.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.receiver.PushMsgOperating;
import go.fast.fastenvelopes.service.PushIntentService;
import go.fast.fastenvelopes.utils.CommonUtils;


/**
 * 会话页面Fragment
 */
public class PersonalLetterActivity extends PersonalLetterBaseActivity implements View.OnClickListener {




    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setInitView();

    }


	private void setInitView() {

        sendBtn.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushIntentService.SEND_ATTENTION_ACTION);
        registerReceiver(receiver, filter);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.startEnvelopeBtn:
                break;

            case R.id.btn_send:

                sendMsg();
                break;
        }

    }


    private void showPsdDialog()
    {
        DialogPlusBuilder dialogPlusB= DialogPlus.newDialog(this);

        dialogPlusB.setContentHolder(new ViewHolder(R.layout.show_input_psd_dialog));
        dialogPlusB.setContentBackgroundResource(android.R.color.transparent);
        dialogPlusB.setGravity(Gravity.CENTER);
        //dialogPlusB.setCancelable(isCancelable);
        final DialogPlus dialog=  dialogPlusB.create();
        EditText psdEdit=((EditText) dialog.getHolderView().findViewById(R.id.input_psd_et));
        TextView postiveText=((TextView)dialog.getHolderView().findViewById(R.id.postive_tv));
        TextView negtiveText=((TextView)dialog.getHolderView().findViewById(R.id.negtive_tv));
        postiveText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(PushIntentService.SEND_ATTENTION_ACTION)) {

                PushMsgInfo pushMsgInfo = (PushMsgInfo) intent
                        .getSerializableExtra("content");
                if(pushMsgInfo.type== CommonUtils.PUSH_MSG_TYPE_LETTER)
                {
                    MessageInfo msgInfo = new MessageInfo();
                    msgInfo.account = pushMsgInfo.account;
                    msgInfo.content = pushMsgInfo.content;
                    msgInfo.headurl =pushMsgInfo.headurl;
                    msgInfo.nickname =pushMsgInfo.nickname;
                    msgInfo.createdAt =pushMsgInfo.createdAt;
                    chatInfoList.add(msgInfo);
                    personalLetterListAdapter.notifyDataSetChanged();
                    chatListv.smoothScrollToPosition(chatInfoList.size());


//                    CoolDBHelper.insertMessage(PersonalLetterActivity.this,userInfo.account,msgInfo);//更新聊天记录表
//                    SessionItemInfo sessionItemInfo=new SessionItemInfo();
//                    sessionItemInfo.lastContent= msgEdit.getText().toString();
//                    sessionItemInfo.lastPublishTime=pushMsgInfo.getCreatedAt();
//                    sessionItemInfo.account=userInfo.account;
//                    sessionItemInfo.nickname=userInfo.nickName;
//                    sessionItemInfo.headurl=userInfo.headurl;
//                    sessionItemInfo.sex=userInfo.sex;
//                    sessionItemInfo.level=userInfo.level;
//                    CoolDBHelper.insertOrUpdateSessionListByAccount(PersonalLetterActivity.this,sessionItemInfo);//更新会话表


                }
                else
                {
                    PushMsgOperating.pushMsgDoing(PersonalLetterActivity.this, pushMsgInfo);
                }
            }
            else
            if(intent.getAction().equals(PushIntentService.RECEIVE_MSG))//收到关于房间会话的聊天信息
            {

                PushMsgInfo pushMsgInfo = (PushMsgInfo) intent
                        .getSerializableExtra("content");
                PushMsgOperating.pushMsgDoing(PersonalLetterActivity.this, pushMsgInfo);
            }

        }
    };

}
