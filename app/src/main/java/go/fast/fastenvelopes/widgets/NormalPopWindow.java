package go.fast.fastenvelopes.widgets;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.NormalPopAdapter;
import go.fast.fastenvelopes.utils.PixelUtil;


public class NormalPopWindow extends PopupWindow {
    private View mMenuView;
    private OnItemClickListener listener;

    private int backgroundId;

    private int popWidth;

    public NormalPopWindow(Context context, OnItemClickListener listener,
	    int resourceId) {
	super(context);
	this.listener = listener;
	backgroundId = resourceId;
    }

    public NormalPopWindow(Context context, OnItemClickListener listener) {
	super(context);
	this.listener = listener;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
	super.showAtLocation(parent, gravity, x, y);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
	this.listener = listener;
    }

    public void setBackground(int resourceId) {
	backgroundId = resourceId;
    }

    public void setPopWidth(int popWidth) {

	this.popWidth = popWidth;

    }

    public void showAsDropDown(View anchor, int xoff, int yoff,
	    String[] itemText, int[] itemImage) {
	Context context = anchor.getContext();
	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	setFocusable(true);
	setOutsideTouchable(true);
	mMenuView = inflater.inflate(R.layout.gamedetail_report_pop, null);

	RelativeLayout rootView = (RelativeLayout) mMenuView
		.findViewById(R.id.root_rl);
	this.setContentView(mMenuView);

	if (popWidth > 0) {
	    this.setWidth((int) PixelUtil.dp2Pixel(
		    popWidth, context));
	} else {
	    this.setWidth((int) PixelUtil.dp2Pixel(
		    140, context));
	}

	this.setHeight(LayoutParams.WRAP_CONTENT);
	this.setAnimationStyle(R.style.GamedetailReportAnimation);
	ColorDrawable dw = new ColorDrawable(Color.parseColor("#00ff0000"));
	this.setBackgroundDrawable(dw);
	//this.setBackground(android.R.color.transparent);
	if (backgroundId != 0) 
	{
	    rootView.setBackgroundResource(backgroundId);
	} else 
	{
	    rootView.setBackgroundResource(color.transparent);
	}

	ListView popList = ((ListView) mMenuView
		.findViewById(R.id.report_pop_lv));
	setPopList(context, popList, itemText, itemImage);// 对popList赋值
	super.showAsDropDown(anchor, xoff, yoff);
    }

    @SuppressLint("NewApi")
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity,
	    String[] itemText, int[] itemImage) {
	Context context = anchor.getContext();
	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	setFocusable(true);
	setOutsideTouchable(true);
	mMenuView = inflater.inflate(R.layout.gamedetail_report_pop, null);
	this.setContentView(mMenuView);
	RelativeLayout rootView = (RelativeLayout) mMenuView
		.findViewById(R.id.root_rl);
	if (popWidth > 0) {
	    this.setWidth((int) PixelUtil.dp2Pixel(
		    popWidth, context));
	} else {
	    this.setWidth((int)PixelUtil.dp2Pixel(
		    140, context));
	}

	this.setHeight(LayoutParams.WRAP_CONTENT);
	this.setAnimationStyle(R.style.GamedetailReportAnimation);
	ColorDrawable dw = new ColorDrawable(Color.parseColor("#00FF0000"));
	this.setBackgroundDrawable(dw);
	if (backgroundId != 0) {
	    rootView.setBackgroundResource(backgroundId);
	} else {
	    rootView.setBackgroundResource(color.white);
	}

	ListView popList = ((ListView) mMenuView
		.findViewById(R.id.report_pop_lv));

	setPopList(context, popList, itemText, itemImage);// 对popList赋值
	if (Build.VERSION.SDK_INT >= 19) {
	    super.showAsDropDown(anchor, xoff, yoff, gravity);
	} else {
	    super.showAsDropDown(anchor, xoff, yoff);
	}
    }

    private void setPopList(Context context, ListView popList,
	    String[] itemText, int[] itemImage) {
	List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
	for (int i = 0; i < itemText.length; i++) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("itemImage", itemImage[i]);
	    map.put("itemText", itemText[i]);
	    contents.add(map);
	}
	NormalPopAdapter adapter = new NormalPopAdapter(context, contents,
		R.layout.normal_pop_item, new String[] { "itemText",
			"itemImage" }, new int[] { R.id.report_tv,
			R.id.report_iv });
	popList.setAdapter(adapter);
	popList.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		dismiss();
		listener.onItemClick(arg0, arg1, arg2, arg3);
	    }
	});
    }

}