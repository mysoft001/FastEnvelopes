package go.fast.fastenvelopes.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.UUID;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import go.fast.fastenvelopes.R;

public class PayUtils {

    public static String KEY = "132006250b4c09247ec02edce69f6a2d";
    
    
    public static String WXAPP_ID = "wxab82a0376412c9ac";
    
    public static String partnerId = "wxab82a0376412c9ac";
    
    public static String LASTPAYID = "lastPayId";

    public static void startPayForWX(Context context, WXPayInfo wxPayInfo) {

	final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
	// 将该app注册到微信
	msgApi.registerApp(CommentValues.WXAPP_ID);

	
	System.out.println("调起微信         appId   "+CommentValues.WXAPP_ID+"    partnerId   "+ wxPayInfo.partnerid
		+"    prepayId   "+ wxPayInfo.prepayid
		+"    packageValue   "+ wxPayInfo.extension
		+"    nonceStr   "+ wxPayInfo.noncestr
		+"    timestamp   "+ wxPayInfo.timestamp
		
		+"    sign   "+ wxPayInfo.sign
		
		);
	
	PreferenceUtil.getInstance(context).putString(LASTPAYID, wxPayInfo.orderId);//缓存支付账号用于确认使用
	PayReq request = new PayReq();
	request.appId = CommentValues.WXAPP_ID;
	request.partnerId = wxPayInfo.partnerid;
	request.prepayId = wxPayInfo.prepayid;
	request.packageValue = wxPayInfo.extension;
	request.nonceStr = wxPayInfo.noncestr;
	request.timeStamp = wxPayInfo.timestamp;
	request.sign = wxPayInfo.sign;
	msgApi.sendReq(request);
	//System.out.println(" 调起微信支付-----------------------------------  ");

    }

//    public static String getSign(String[] key, String[] values) {
//	SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
//
//	for (int i = 0; i < key.length; i++) {
//	    parameters.put(key[i], values[i]);
//	}
//	String characterEncoding = "UTF-8";
//
//	return createSign(characterEncoding, parameters);
//
//    }

    /**
     * 微信支付签名算法sign
     * 

     * @return
     */
    @SuppressWarnings("unchecked")
//    public static String createSign(String characterEncoding,
//	    SortedMap<Object, Object> parameters) {
//	StringBuffer sb = new StringBuffer();
//	Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
//	Iterator it = es.iterator();
//	while (it.hasNext()) {
//	    Map.Entry entry = (Map.Entry) it.next();
//	    String k = (String) entry.getKey();
//	    Object v = entry.getValue();
//	    if (null != v && !"".equals(v) && !"sign".equals(k)
//		    && !"key".equals(k)) {
//		sb.append(k + "=" + v + "&");
//	    }
//	}
//	sb.append("key=" + KEY);
//	String sign = MD5Utlis.MD5Encode(sb.toString(), characterEncoding)
//		.toUpperCase();
//	return sign;
//    }
    
    private static String create_nonce_str() {
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }


	public static void showPayChoose(Context context,int goldSize,String instruct) {
		final DialogPlus rechargeDialog;

		View commentLayout = LayoutInflater.from(context).inflate(
				R.layout.paygold_dialog_layout, null);

		DialogPlusBuilder dialogBulider = DialogPlus.newDialog(context);
		dialogBulider.setContentHolder(new dialogplus.ViewHolder(commentLayout));
		//dialogBulider.setContentHeight(PixelUtil.dp2Pixel(320, this));
		dialogBulider.setGravity(Gravity.BOTTOM);
		rechargeDialog = dialogBulider.create();

		TextView orders_detail_tv=(TextView) commentLayout.findViewById(R.id.orders_detail_tv);
		orders_detail_tv.setText(instruct);
		TextView orders_money_tv=(TextView) commentLayout.findViewById(R.id.orders_money_tv);
		orders_money_tv.setText(goldSize+"金币");
		TextView paynow_title=(TextView) commentLayout.findViewById(R.id.paynow_title);

		paynow_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//requestPay(choosedMoney);
				rechargeDialog.dismiss();
			}
		});

		rechargeDialog.show();

	}

}
