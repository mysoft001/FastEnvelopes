package go.fast.fastenvelopes.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import go.fast.fastenvelopes.R;

/**
 * 说明
 * @author hanwei
 *
 */
public class HelpActivity extends BaseActivity implements OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.help_layout);
	initView();
    }

    private void initView() {

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
