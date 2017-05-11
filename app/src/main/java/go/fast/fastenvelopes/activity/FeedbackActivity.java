package go.fast.fastenvelopes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.ToolUtils;

public class FeedbackActivity extends BaseActivity {

    private EditText feedbackEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.feedback_layout);
	initView();
    }

    private void initView() {
	feedbackEdit = (EditText) findViewById(R.id.input_et);
    }

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {
	super.initRight(imageView, textView, parent);
	parent.setVisibility(View.VISIBLE);
	textView.setVisibility(View.VISIBLE);
	textView.setText("提交");
	parent.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		feedback();
	    }
	});
    }

    @Override
    public void initMiddle(TextView middleView) {
	super.initMiddle(middleView);
	middleView.setText("问题反馈");
	middleView.setOnLongClickListener(new OnLongClickListener() {
	    
	    @Override
	    public boolean onLongClick(View v) {
		
		Intent feedbackIntent =new Intent(FeedbackActivity.this,FeedbackListActivity.class);
		startActivity(feedbackIntent);
		return false;
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
//	getMenuInflater().inflate(R.menu.main, menu);
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
    
    
    private void hideSoftInput() {
	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(feedbackEdit.getWindowToken(), 0);
	feedbackEdit.setText("");
    }

    private void feedback() {
	String feedbackStr = feedbackEdit.getText().toString();
	if (ToolUtils.isNullOrEmpty(feedbackStr)) {
	    ToolUtils.showToast(this, "反馈内容为空");
	    return;
	}

	HttpRequest.userFeedback(this, feedbackStr, new HttpRequest.onRequestCallback() {

	    @Override
	    public void onSuccess(BaseInfo response) {
		ToolUtils.showToast(FeedbackActivity.this, "您的反馈我们已经收到");
		hideSoftInput();
	    }

	    @Override
	    public void onFailure(String rawJsonData) {
		ToolUtils.showToast(FeedbackActivity.this, "反馈失败，稍后再试吧");
	    }
	});

    }

}
