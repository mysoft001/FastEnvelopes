package go.fast.fastenvelopes.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.utils.ToolUtils;


public class CoolTextDialog {
    
    private String content;
    
    private String title="提示";
    
    OnClickListener postivelistener;
    OnClickListener negativelistener;
    
    private String posBtnText="确定";
    
    private String negBtnText="取消";
    
    private Context context;
    
    private DialogPlus dialog;
    private boolean isCancelable=true;
    
    
    public void setTitle(String text)
    {
	title=text;
    }
    public void setContent(String content)
    {
	this.content=content;
    }
    
    public void setCancelable(boolean isCancelable)
    {
	this.isCancelable=isCancelable;
    }
    
    public void setOnPostiveListener(String postiveText, OnClickListener listener)
    {
	posBtnText=postiveText;
	postivelistener=listener;
    }
    
    public void setOnNegtiveListener(String negtiveText, OnClickListener listener)
    {
	negBtnText=negtiveText;
	negativelistener=listener;
    }
	public CoolTextDialog(Context context)  
	    {  
	    
	    this.context=context;
	    
	    }  
	public void show()
	{
	    
	    DialogPlusBuilder dialogPlusB=DialogPlus.newDialog(context);
	    
	    dialogPlusB.setContentHolder(new ViewHolder(R.layout.cool_text_dialog));
	    dialogPlusB.setContentBackgroundResource(android.R.color.transparent);
	    dialogPlusB.setGravity(Gravity.CENTER);
	    dialogPlusB.setCancelable(isCancelable);
	     dialog=  dialogPlusB.create();
	    ((TextView)dialog.getHolderView().findViewById(R.id.title_tv)).setText(title);
	    ((TextView)dialog.getHolderView().findViewById(R.id.content_tv)).setText(content);
	    TextView postiveText=((TextView)dialog.getHolderView().findViewById(R.id.postive_tv));
	    TextView negtiveText=((TextView)dialog.getHolderView().findViewById(R.id.negtive_tv));
	    
	    if(!ToolUtils.isNullOrEmpty(posBtnText))
	    {
		  postiveText.setText(posBtnText);
		    postiveText.setOnClickListener(new OnClickListener() {
		        
		        @Override
		        public void onClick(View v) {
		            postivelistener.onClick(v);
		            dialog.dismiss();
		        }
		    });
	    }
	    else
	    {
		postiveText.setVisibility(View.GONE);
	    }
	  
	    if(ToolUtils.isNullOrEmpty(negBtnText))
	    {
		negtiveText.setVisibility(View.GONE);
	    }
	    else
	    {
		  negtiveText.setText(negBtnText);
		    negtiveText.setOnClickListener(new OnClickListener() {
		        
		        @Override
		        public void onClick(View v) {
		            if(negativelistener!=null)
		            {
		        	negativelistener.onClick(v);
		            }
		            dialog.dismiss();
		        }
		    });
	    }
	  
	    
	    dialog.show();
	}

    
    
}
