package com.ikok.teachingwebsite.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Comment;
import com.ikok.teachingwebsite.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anonymous on 2016/4/18.
 */
public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mCommentList = new ArrayList<Comment>();
    private LayoutInflater mInflater;

    public CommentAdapter(Context context, List<Comment> list) {
        this.mContext = context;
        this.mCommentList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_chat_comment_list_item,parent,false);

            viewHolder.mUserImg = (CircleImageView) convertView.findViewById(R.id.id_chat_post_item_comment_img);
            viewHolder.mUserName = (TextView) convertView.findViewById(R.id.id_chat_post_item_comment_name);
            viewHolder.mCommentTime = (TextView) convertView.findViewById(R.id.id_chat_post_item_comment_time);
            viewHolder.mCommentContent = (TextView) convertView.findViewById(R.id.id_chat_post_item_comment_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置显示
        if (mCommentList.get(position).getCommentUser().getProfileImg() != null){
            new UserImgLoader(viewHolder.mUserImg).execute(mCommentList.get(position).getCommentUser().getProfileImg().getFileUrl(mContext));
        }
        viewHolder.mUserName.setText(mCommentList.get(position).getCommentUser().getUsername());
        viewHolder.mCommentTime.setText(mCommentList.get(position).getCommentPublishTime());
        viewHolder.mCommentContent.setText(mCommentList.get(position).getCommentContent());

        return convertView;
    }

    class ViewHolder {
        public CircleImageView mUserImg;
        public TextView mUserName;
        public TextView mCommentTime;
        public TextView mCommentContent;
    }

}
