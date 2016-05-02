package com.ikok.teachingwebsite.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Post;
import com.ikok.teachingwebsite.R;

import java.util.List;

/**
 * Created by Anonymous on 2016/4/16.
 */
public class PostAdapter extends BaseAdapter {

    private List<Post> mPostList;
    private LayoutInflater mInflater;
    private Context mContext;


    public PostAdapter(Context context, List<Post> postList) {
        this.mContext = context;
        this.mPostList = postList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPostList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostList.get(position);
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
            convertView = mInflater.inflate(R.layout.activity_chat_post_list_item,parent,false);
            viewHolder.mPostTitle = (TextView) convertView.findViewById(R.id.id_chat_post_title);
            viewHolder.mPostTime = (TextView) convertView.findViewById(R.id.id_chat_post_time);
            viewHolder.mPostContent = (TextView) convertView.findViewById(R.id.id_chat_post_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mPostTitle.setText(mPostList.get(position).getPostTitle());
        viewHolder.mPostTime.setText(mPostList.get(position).getPostPublishTime());
        viewHolder.mPostContent.setText(mPostList.get(position).getPostContent());

        return convertView;
    }

    class ViewHolder {
        public TextView mPostTitle;
        public TextView mPostTime;
        public TextView mPostContent;
    }
}
