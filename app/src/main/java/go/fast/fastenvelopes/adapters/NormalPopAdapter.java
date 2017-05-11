package go.fast.fastenvelopes.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.utils.PixelUtil;


public class NormalPopAdapter extends SimpleAdapter {

	private Context context;
	public NormalPopAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context=context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=super.getView(position, convertView, parent);
		RelativeLayout rootLayout=(RelativeLayout)convertView.findViewById(R.id.pop_rl);
		RelativeLayout inLayout=(RelativeLayout) convertView.findViewById(R.id.report_inside_rl);
		TextView contentT=(TextView)convertView.findViewById(R.id.report_tv);
		ImageView contentImg=(ImageView)convertView.findViewById(R.id.report_iv);
		if(position==0)
		{
			rootLayout.setBackgroundResource(R.drawable.normal_up_selector);
			contentT.setPadding(0, (int) PixelUtil.dp2Pixel(8, context), 0, 0);
			
			//inLayout.setPadding((int)PixelUtil.dp2Pixel(15, context), (int)PixelUtil.dp2Pixel(8, context), (int)PixelUtil.dp2Pixel(15, context), (int)PixelUtil.dp2Pixel(0, context));
			
		}else if(position==getCount()-1)
		{
			rootLayout.setBackgroundResource(R.drawable.normal_bottom_selector);
			contentT.setPadding(0, 0, 0, 0);
			inLayout.setPadding((int)PixelUtil.dp2Pixel(15, context), (int)PixelUtil.dp2Pixel(14, context), (int)PixelUtil.dp2Pixel(14, context), (int)PixelUtil.dp2Pixel(14, context));
		}
		else
		{
		    contentT.setPadding(0, 0, 0, 0);
			rootLayout.setBackgroundResource(R.drawable.normal_middle_selector);
			inLayout.setPadding((int)PixelUtil.dp2Pixel(15, context), (int)PixelUtil.dp2Pixel(14, context),(int)PixelUtil.dp2Pixel(14, context), (int)PixelUtil.dp2Pixel(14, context));
		}
		// TODO Auto-generated method stub
		return convertView;
	}
}
