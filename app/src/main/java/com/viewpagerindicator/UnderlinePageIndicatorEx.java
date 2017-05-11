package com.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import go.fast.fastenvelopes.R;


//继承这个然后重写
public class UnderlinePageIndicatorEx extends UnderlinePageIndicator {

      public UnderlinePageIndicatorEx(Context context) {  
         super(context, null);         
      }  

      public UnderlinePageIndicatorEx(Context context, AttributeSet attrs) {  
          super(context, attrs, R.attr.vpiUnderlinePageIndicatorStyle);
      }  

      public UnderlinePageIndicatorEx(Context context, AttributeSet attrs, int defStyle) {  
          super(context, attrs, defStyle);          
      }  
       
      //自身�?ViewPage传�?过去  
      @Override  
      public void setViewPager(ViewPager viewPager) {  
          if (mViewPager == viewPager){  
              return;  
          }  
             
//        if (mViewPager != null) {  
//            mViewPager.setOnPageChangeListener(null);  
//        }  
            
          if (viewPager.getAdapter() == null) {  
              throw new IllegalStateException(" pager 没有 加入 adapter");  
          }  
            
          mViewPager = viewPager;  
//        mViewPager.setOnPageChangeListener(this);  
          invalidate();  
            
          post(new Runnable(){  
              @Override public void run() {  
                  if (mFades) {  
                      post(mFadeRunnable);  
                  }  
              }        
          });   
      }  
        
} 