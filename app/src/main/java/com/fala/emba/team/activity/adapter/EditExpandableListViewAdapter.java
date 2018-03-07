package com.fala.emba.team.activity.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.util.DisplayUtils;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2017/12/8
 * 编辑个人信息页展开列表适配器
 */

public class EditExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<UserComm.UserBean.AssociationsBean> mAssociationsBeans;
    public EditExpandableListViewAdapter(Context context, List<UserComm.UserBean.AssociationsBean> associationsBeans) {
        this.mContext = context;
        this.mAssociationsBeans = associationsBeans;
    }
    @Override
    public int getGroupCount() { //获取分组的个数
        return 1;
    }
    @Override
    public int getChildrenCount(int groupPosition) {//获取指定分组中的子选项的个数
        return mAssociationsBeans.size();
    }
    @Override
    public Object getGroup(int groupPosition) { //获取指定的分组数据
        return mAssociationsBeans.get(0).getName();   //显示第一个
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) { //获取指定分组中的指定子选项数据
        return mAssociationsBeans.get(childPosition).getName();   //只有一个分组
    }
    @Override
    public long getGroupId(int groupPosition) { //获取指定分组的ID, 这个ID必须是唯一的
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) { //获取子选项的ID, 这个ID必须是唯一的
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {  //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
        return true;
    }
    //获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_edit_group, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.group_text = convertView.findViewById(R.id.group_text);
            viewHolder.group_title = convertView.findViewById(R.id.group_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(isExpanded){//展开状态
            Drawable drawable=mContext.getResources().getDrawable(R.mipmap.icon_down);
            drawable.setBounds(0, 0, DisplayUtils.dp2px(mContext,20), DisplayUtils.dp2px(mContext,20));
            viewHolder.group_text.setCompoundDrawables(null, null, drawable, null);
            viewHolder.group_text.setText("");
        }else{//收起状态
            Drawable drawable=mContext.getResources().getDrawable(R.mipmap.icon_go);
            drawable.setBounds(0, 0, DisplayUtils.dp2px(mContext,20), DisplayUtils.dp2px(mContext,20));
            viewHolder.group_text.setCompoundDrawables(null, null, drawable, null);
            viewHolder.group_text.setText(mAssociationsBeans.get(groupPosition).getName());   //展开栏的默认值
        }
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_edit_group, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.group_text = convertView.findViewById(R.id.group_text);
            viewHolder.group_title = convertView.findViewById(R.id.group_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.group_text.setCompoundDrawables(null, null, null, null);
        viewHolder.group_text.setText(mAssociationsBeans.get(childPosition).getName());
        viewHolder.group_title.setText(String.valueOf(childPosition+1));
        return convertView;
    }
    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public static class ViewHolder {
        TextView group_title;
        TextView group_text;
    }
}
