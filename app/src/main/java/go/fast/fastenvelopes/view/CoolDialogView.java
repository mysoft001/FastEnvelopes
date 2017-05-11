package go.fast.fastenvelopes.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.utils.PixelUtil;


public class CoolDialogView implements OnClickListener {

	private static final int TYPE_NORMAL = 0x00;
	private static final int TYPE_CANCEL = 0x01;
	private static final int TYPE_DELETE = 0x02;
	private static final int ID_CANCEL = 0x00;
	private LinearLayout mPopLayout;
	public ArrayList<ButtonEntity> entitys = new ArrayList<ButtonEntity>();
	private DialogPlus changSexDialog;

	public void cleanAllView(){
		if(mPopLayout!=null)
			mPopLayout.removeAllViewsInLayout();
		entitys.clear();
	}
	
	public void show(Activity ac){
		int margin=(int) PixelUtil.dp2Pixel(20, ac);
		LinearLayout mPopLayout = (LinearLayout)LayoutInflater.from(ac).inflate(R.layout.alert_dialog, null);
		int length=entitys.size();
		for (int i = 0; i < length; i++) {
			final ButtonEntity entity =entitys.get(i);
			Button button = new Button(ac);
			button.setId(entity.id);
			int top=margin;
			switch (entity.type ) {
			case TYPE_CANCEL:
			    button.setPadding((int)PixelUtil.dp2Pixel(20, ac), (int)PixelUtil.dp2Pixel(15, ac),
				    (int)PixelUtil.dp2Pixel(20, ac), (int)PixelUtil.dp2Pixel(15, ac));
				button.setTextColor(ac.getResources().getColor(R.color.cancel_btn_text));
				button.setBackgroundResource(R.drawable.setting_itembg_selector);
				break;
			case TYPE_NORMAL:
			    button.setPadding((int)PixelUtil.dp2Pixel(20, ac), (int)PixelUtil.dp2Pixel(15, ac),
				    (int)PixelUtil.dp2Pixel(20, ac), (int)PixelUtil.dp2Pixel(15, ac));
				button.setTextColor(ac.getResources().getColor(R.color.writing_time));
				button.setBackgroundResource(R.drawable.setting_itembg_selector);
				top=margin/4*3;
				break;
			case TYPE_DELETE:
			    button.setPadding((int)PixelUtil.dp2Pixel(20, ac), (int)PixelUtil.dp2Pixel(15, ac),
				    (int)PixelUtil.dp2Pixel(20, ac), (int)PixelUtil.dp2Pixel(15, ac));
				button.setTextColor(ac.getResources().getColor(R.color.graytext));
				button.setBackgroundResource(R.drawable.setting_itembg_selector);
				break;

			default:
				break;
			}
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			if(i==length-1){
				params.setMargins(0, top, 0, 0);
			}else{
				params.setMargins(0, top, 0, 0);
			}
			
			button.setLayoutParams(params);
			
			button.setText(entity.text);
			mPopLayout.addView(button);
			
		}
		
		DialogPlusBuilder dialog= DialogPlus.newDialog(ac);
		dialog.setContentHolder(new dialogplus.ViewHolder(mPopLayout));
		
		 changSexDialog=dialog.create();
		 changSexDialog.show();
		 View rootView=changSexDialog.getHolderView();
		 for(int m = 0; m < length; m++)
		 {
		     final ButtonEntity entity =entitys.get(m);
		     rootView.findViewById(entity.id).setOnClickListener(new OnClickListener() {
		        
		        @Override
		        public void onClick(View v) {
		            entity.listener.onClick(v);
		            changSexDialog.dismiss();
		        }
		    });
		 }
		 
	}
	
	
	public void dismiss(){
	    changSexDialog.dismiss();
	}

	public void addButton(String text, OnClickListener listener,int id, int type) {
		entitys.add(new ButtonEntity(text, listener, type,id));
	}

	public void addCancelButton(String text) {
		addButton(text, this, TYPE_CANCEL,ID_CANCEL);
	}
	public void addNormalButton(String text,OnClickListener listener,int id) {
		addButton(text, listener, TYPE_NORMAL,id);
	}
	public void addDeleteButton(String text,OnClickListener listener,int id) {
		addButton(text, listener, TYPE_DELETE,id);
	}

	class ButtonEntity {
		public String text;
		public OnClickListener listener;
		public int type;
		public int id;

		public ButtonEntity() {

		}

		public ButtonEntity(String text, OnClickListener listener, int id, int type) {
			super();
			this.text = text;
			this.id=id;
			this.listener = listener;
			this.type = type;
		}
	}

	public void onClick(View arg0) {
	    changSexDialog.dismiss();
	}
}
