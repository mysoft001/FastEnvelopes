package go.fast.fastenvelopes.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class ForcedTextView extends TextView {
	
	public ForcedTextView(Context context, AttributeSet attrs)  
    {  
        super(context, attrs);  
    }  
      
	  @Override  
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)  
    {  
        if(focused)
        {
        	super.onFocusChanged(focused, direction, previouslyFocusedRect);  
        }
    }  
      
    public void onWindowFocusChanged(boolean hasWindowFocus)  
    {  
        if(hasWindowFocus) 
        	{
        	super.onWindowFocusChanged(hasWindowFocus);  
        	}
    }  
      
    public boolean isFocused()  
    {  
        return true;  
    }  
}
