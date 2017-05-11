/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package go.fast.fastenvelopes.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.CreateRoomActivity;
import go.fast.fastenvelopes.activity.EnvelopeRoomAcitvity;
import go.fast.fastenvelopes.info.EnvelopesInfo;

/**
 * adpater
 */
public class EnvelopesListAdapter extends ArrayAdapter<EnvelopesInfo>
        implements OnClickListener {

    private Context context;

    private long lastShowTime;

    public EnvelopesListAdapter(Context context, int resource,
                                List<EnvelopesInfo> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    private LayoutInflater inflater;

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.envelopes_list_item, parent,
                    false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {


            holder = new ViewHolder();
            holder.createRoomText = (TextView) convertView.findViewById(R.id.create_tv);
            holder.envelopeTypeText = (TextView) convertView.findViewById(R.id.envelope_type_tv);

            holder.joinRoomText = (TextView) convertView
                    .findViewById(R.id.join_tv);
            holder.personSizeText = (TextView) convertView
                    .findViewById(R.id.personsize_tv);
            holder.roomSizeText = (TextView) convertView
                    .findViewById(R.id.roomsize_tv);
            holder.envelopeImg = (ImageView) convertView
                    .findViewById(R.id.envelopes_iv);
            convertView.setTag(holder);
        }

        final EnvelopesInfo envelopesInfo = getItem(position);
        holder.personSizeText.setText(getSizeString(envelopesInfo.totalPeoples));
        holder.roomSizeText.setText(getSizeString(envelopesInfo.totalRooms));
        holder.envelopeTypeText.setText(envelopesInfo.envelopeName);

        holder.createRoomText.setOnClickListener(this);
        holder.createRoomText.setTag(position);
        holder.joinRoomText.setOnClickListener(this);
        holder.joinRoomText.setTag(position);
        if(envelopesInfo.envelopeType==1)
        {
            holder.envelopeImg.setImageResource(R.drawable.guess_envelope_icon);
        }
        else
        if(envelopesInfo.envelopeType==2)
        {
            holder.envelopeImg.setImageResource(R.drawable.free_envelope_icon);
        }
        else
        if(envelopesInfo.envelopeType==3)
        {
            holder.envelopeImg.setImageResource(R.drawable.near_envelope_icon);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView createRoomText;
        TextView joinRoomText;
        ImageView envelopeImg;
        TextView roomSizeText;
        TextView personSizeText;


        public TextView envelopeTypeText;
    }

    private String getSizeString(int count) {
        if (count < 50) {
            return "50+";
        } else {
            return 10 * count / 10 + "+";
        }
    }


    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()) {

            case R.id.create_tv:// 创建群组
                Intent crateIntent=new Intent(context, CreateRoomActivity.class);
                crateIntent.putExtra("type",position+1);//红包类型
                context.startActivity(crateIntent);

                break;
            case R.id.join_tv:// 加入已有红包群组

                Intent envelopeIntent=new Intent(context, EnvelopeRoomAcitvity.class);
                envelopeIntent.putExtra("envelopeType",getItem(position).envelopeType);
                context.startActivity(envelopeIntent);
                break;

        }

    }

}
