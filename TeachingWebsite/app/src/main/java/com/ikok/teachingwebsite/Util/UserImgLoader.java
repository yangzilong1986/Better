package com.ikok.teachingwebsite.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Anonymous on 2016/4/22.
 */
public class UserImgLoader extends AsyncTask<String,Void,Bitmap> {

    private ImageView mImageView;

    public UserImgLoader(ImageView imageView) {
        this.mImageView = imageView;
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
        if (mImageView != null){
            mImageView.setImageBitmap(bitmap);
        }

    }


    /**
     * 从网络图片地址中获取图片，转成Bitmap
     * @param url 网络图片地址
     * @return 网络图片的bitmap
     */
    private Bitmap getBitmap(String url,Bitmap bitmap) {
        try {
            //加载一个网络图片
            InputStream is = new URL(url).openStream();
            // 转码
            bitmap = BitmapFactory.decodeStream(is);

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
