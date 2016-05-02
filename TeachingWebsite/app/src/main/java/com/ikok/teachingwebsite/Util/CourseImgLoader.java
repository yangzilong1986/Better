package com.ikok.teachingwebsite.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Anonymous on 2016/4/14.
 */
public class CourseImgLoader {

    // LRU缓存，键值对，类似Map
    private LruCache<String,Bitmap> mCache;

    public CourseImgLoader() {

        // 获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 设置缓存大小
        int cacheSize = maxMemory/4;
        mCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 在每次存入缓存的时候调用，存入的对象有多大
                return value.getByteCount();
            }
        };
    }


    // 加载课程的图片
    public void loadCourseImg(String url,ImageView imageView){
//        Log.d("Anonymous"," loadCourseImg tag is: " + url);
        // 从缓存中取出对应的图片
        Bitmap bitmap = getBitMapFromCache(url);
//        Log.d("Anonymous"," loadCourseImg bitmap is: " + bitmap);
        // 如果缓存中没有，从网络上下载
        if (bitmap == null){
            new CourseImgLoaderTask(imageView).execute(url);
        } else {
            // 如果有，直接使用缓存中的图片
            imageView.setImageBitmap(bitmap);
        }
    }

    // 增加到缓存，增加前检验是否存在
    public void addBitMap2Cache(String url, Bitmap bitmap){
        if (getBitMapFromCache(url) == null){
            mCache.put(url,bitmap);
        }
    }

    // 从缓存中读取数据
    public Bitmap getBitMapFromCache(String url){
//        Log.d("Anonymous","zzz getBitMapFromCache tag is: " + url );
        return mCache.get(url);
    }


    /**
     * 从网络图片地址中获取Bitmap
     */
    class CourseImgLoaderTask extends AsyncTask<String,Void,Bitmap>{

        private ImageView mView;

        public CourseImgLoaderTask(ImageView imageView) {
            mView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            bitmap = getBitmap(url, bitmap);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mView != null){
                mView.setImageBitmap(bitmap);
            }

        }
    }

    /**
     * 从网络图片地址中获取图片，转成Bitmap
     * @param url 网络图片地址
     * @param bitmap 空的Bitmap
     * @return 网络图片的bitmap
     */
    private Bitmap getBitmap(String url, Bitmap bitmap) {
        try {
            //加载一个网络图片
            InputStream is = new URL(url).openStream();
            // 转码
            bitmap = BitmapFactory.decodeStream(is);

            if (bitmap != null){
                // 如果图片下载下来了，将不在缓存中的图片加入到缓存中
//                Log.d("Anonymous","addBitMap2Cache is: " + url + " " + bitmap);
                addBitMap2Cache(url,bitmap);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
