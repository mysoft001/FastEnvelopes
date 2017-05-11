package go.fast.fastenvelopes.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.utils.CommonUtils;

/**
 * 说明
 * @author hanwei
 *
 */
public class PlayHelpActivity extends BaseActivity implements OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    int envelopeType;
    private RelativeLayout guessHelpText;
    private RelativeLayout freeHelpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.play_help_layout);
	initView();
    }

    private void initView() {
        envelopeType=getIntent().getIntExtra("envelopeType", CommonUtils.ENVELOPE_TYPE_GUESS);
        guessHelpText=(RelativeLayout)findViewById(R.id.guess_envelope_rl);
        freeHelpText=(RelativeLayout)findViewById(R.id.free_envelope_get_rl);

        if(envelopeType==CommonUtils.ENVELOPE_TYPE_GUESS)
        {
            freeHelpText.setVisibility(View.GONE);
        }
        else  if(envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
        {
            guessHelpText.setVisibility(View.GONE);
        }


    }

    @Override
    public void initMiddle(TextView middleView) {
	// TODO Auto-generated method stub
	super.initMiddle(middleView);
	middleView.setText("帮助中心");
    }


    public void sendPost(View v) {

	// sendRequest();
	return;
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

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	case R.id.iv_user_photo:
	    break;

	case R.id.instruct_rl:
	    break;

	case R.id.commission_instruct_rl:
	    break;
	}

    }
}
