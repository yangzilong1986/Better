package com.ikok.teachingwebsite.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.R;

import java.util.List;

/**
 * Created by Anonymous on 2016/4/11.
 */
public class CourseAdapter extends BaseAdapter {

    private List<Course> courseList;
    private LayoutInflater mInflater;
    private Context mContext;

    private CourseImgLoader mCourseImgLoader;

    /**
     * 课程 listview 的数据适配器
     * @param context 上下文
     */
    public CourseAdapter(Context context, List<Course> courseList) {
        this.courseList = courseList;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mCourseImgLoader = new CourseImgLoader();

    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        // 当视图为空时，创建 viewholder，并绑定控件
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_course_list_item,null);
            // 绑定控件
            viewHolder.courseName = (TextView) convertView.findViewById(R.id.id_course_item_name);
            viewHolder.courseLearnTime = (TextView) convertView.findViewById(R.id.id_course_item_learn_time);
            viewHolder.courseUpdateTime = (TextView) convertView.findViewById(R.id.id_course_item_update_time);
            viewHolder.courseImg = (ImageView) convertView.findViewById(R.id.id_course_item_img);
            // 设置tag，方便下次直接取出
            convertView.setTag(viewHolder);
        } else {
            // 当视图存在时，使用先创建好的 viewholder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置视图的显示
        viewHolder.courseName.setText(courseList.get(position).getCourseName());
        viewHolder.courseLearnTime.setText(courseList.get(position).getCourseLearnTime().toString() + "分钟");
        viewHolder.courseUpdateTime.setText(courseList.get(position).getCourseUpdateTime().toString().substring(0,9));
        // 设置每一项课程的图片显示
        // 图片地址
        String fileUrl = null;
        // 课程有图片时,使用有的图片
        if (courseList.get(position).getCourseImg() != null) {
            fileUrl = courseList.get(position).getCourseImg().getFileUrl(mContext);
            // 为每个item的图片空间设置图片地址为它的标志
            viewHolder.courseImg.setTag(fileUrl);
//            Log.d("Anonymous","viewHolder getTag is: " + viewHolder.courseImg.getTag());
            mCourseImgLoader.loadCourseImg(fileUrl,viewHolder.courseImg);

        } else {
            // 课程没有图片时，使用默认的图片

        }

        return convertView;
    }

    class ViewHolder{
        public ImageView courseImg;
        public TextView courseName;
        public TextView courseLearnTime;
        public TextView courseUpdateTime;
    }

}
