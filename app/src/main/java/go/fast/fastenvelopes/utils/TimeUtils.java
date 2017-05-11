package go.fast.fastenvelopes.utils;

import android.content.Context;

import java.util.Calendar;

public class TimeUtils {

    public static int getCurrentDay() {
	Calendar now = Calendar.getInstance();
	return now.get(Calendar.DAY_OF_MONTH);
    }
    
    public static boolean isCurrentDay(Context context,String key)
    {
	 int lastDay=AutoPreferenceUtils.getInstance(context).getInt(key, 0);
	
	if(lastDay==getCurrentDay())
	{
	    return true;
	}
	return false;
	
    }

	public static String[] getCountDownTimes(long totalMillSecond,long startTimeStamp)

	{
		String[] countDownTimes=new String[2];
		long lastTime = System.currentTimeMillis() - startTimeStamp;

	int lastMin = ToolUtils
				.getTimeByMS(lastTime)[0];
		if(lastMin<10)
		{
			countDownTimes[0]="0"+lastMin;
		}
		else
		{
			countDownTimes[0]=""+lastMin;
		}

		int lastSec = ToolUtils
				.getTimeByMS(lastTime)[1];
		if(lastSec<10)
		{
			countDownTimes[1]="0"+lastSec;
		}
		else
		{
			countDownTimes[1]=""+lastSec;
		}

return countDownTimes;
	}
	public static int[] getDelayDownTimes(long totalMillSecond,long startTimeStamp)

	{
		int[] countDownTimes=new int[2];
		long lastTime = totalMillSecond-(System.currentTimeMillis() - startTimeStamp);
		if(lastTime<0)
		{
			lastTime=0;
		}

		int lastMin = ToolUtils
				.getTimeByMS(lastTime)[0];
		countDownTimes[0]=lastMin;
		int lastSec = ToolUtils
				.getTimeByMS(lastTime)[1];
		countDownTimes[1]=lastSec;

		return countDownTimes;
	}
    
}
