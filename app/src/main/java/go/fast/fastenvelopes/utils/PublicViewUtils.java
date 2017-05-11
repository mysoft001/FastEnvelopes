package go.fast.fastenvelopes.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.RechargeActivity;

/**
 * Created by hanwei on 2017/1/15.
 */

public class PublicViewUtils {



    public  static void  showConsumeGold(final Activity context, String customerGoldContent, int customerSize, final OnPayGoldListener onPayGoldListener){

       final DialogPlus rechargeDialog;
        View commentLayout = LayoutInflater.from(context).inflate(
                R.layout.paygold_dialog_layout, null);

        DialogPlusBuilder dialogBulider = DialogPlus.newDialog(context);
        dialogBulider.setContentHolder(new dialogplus.ViewHolder(commentLayout));
        //dialogBulider.setContentHeight(PixelUtil.dp2Pixel(320, this));
        dialogBulider.setGravity(Gravity.BOTTOM);
        rechargeDialog = dialogBulider.create();

        TextView orders_detail_tv=(TextView) commentLayout.findViewById(R.id.orders_detail_tv);
        orders_detail_tv.setText(customerGoldContent);
        TextView orders_money_tv=(TextView) commentLayout.findViewById(R.id.orders_money_tv);
        orders_money_tv.setText(customerSize+"");
        TextView total_gold_tv=(TextView) commentLayout.findViewById(R.id.total_gold_tv);
        total_gold_tv.setText(PreferenceUtils.getInstance(context).getSettingTotalGoldenSize()+"");
        commentLayout.findViewById(R.id.recharge_tv).setOnClickListener(new View.OnClickListener() {//立即充值
            @Override
            public void onClick(View v) {

                Intent rechargeIntent=new Intent(context, RechargeActivity.class);
                context.startActivity(rechargeIntent);
            }
        });
        TextView paynow_title=(TextView) commentLayout.findViewById(R.id.paynow_title);

        paynow_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               if(onPayGoldListener!=null)
               {

                   onPayGoldListener.payGold(rechargeDialog);
               }
            }
        });

        rechargeDialog.show();

    }


    public interface  OnPayGoldListener
    {
        public void payGold(DialogPlus dialog);
    }


}
