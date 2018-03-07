package com.fala.emba.team.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fala.emba.team.R;

import com.fala.emba.team.bean.PushCustomData;
import com.fala.emba.team.bean.PushListComm;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送历史列表适配器
 * @author Administrator
 */
public class PushHistoryListAdapter extends BaseAdapter {
    private Context context;
    private List<PushListComm.NoticesBean> pushCustomData = new ArrayList<>();
    public PushHistoryListAdapter(Context context, List<PushListComm.NoticesBean> pushCustomData) {
        this.context = context;
        this.pushCustomData = pushCustomData;
    }
    @Override
    public int getCount() {
        return pushCustomData.size();
    }
    @Override
    public Object getItem(int position) {
        return pushCustomData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_push_list, null);
            holder.push_title = convertView.findViewById(R.id.push_title);
            holder.push_content = convertView.findViewById(R.id.push_content);
            holder.send_time = convertView.findViewById(R.id.send_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.push_title.setText(pushCustomData.get(position).getTitle());
        holder.push_content.setText(pushCustomData.get(position).getContent());
        holder.send_time.setText(pushCustomData.get(position).getSendTime());
        return convertView;
    }
    private static final class ViewHolder {
        private TextView push_title;
        private TextView push_content;
        private TextView send_time;
    }
}