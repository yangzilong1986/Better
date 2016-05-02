package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.kyo.imagecrop.CropLayout;
import com.kyo.imagecrop.CropUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import qiu.niorgai.StatusBarCompat;


public class CropActivity extends Activity {

	private CropLayout mCropLayout;
	private TextView mCancelBtn;
	private TextView mDoneBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_settings_crop);
//        StatusBarUtil.setColor(CropActivity.this, getResources().getColor(R.color.white));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorCrop));
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}

		Uri sourceUri = intent.getData();
		int outputX = intent
			.getIntExtra("outputX", CropUtils.dip2px(this, 200));
		int outputY = intent
			.getIntExtra("outputY", CropUtils.dip2px(this, 200));
		String outputFormat = intent.getStringExtra("outputFormat");

        mDoneBtn = (TextView) this.findViewById(R.id.id_settings_crop_done);
        mDoneBtn.setOnClickListener(mOnClickListener);
        mCancelBtn = (TextView) this.findViewById(R.id.id_settings_crop_cancel);
        mCancelBtn.setOnClickListener(mOnClickListener);

        // bellow
		mCropLayout = (CropLayout) this.findViewById(R.id.id_settings_crop);
		mCropLayout.setOnCropListener(mOnCropListener);
		mCropLayout.startCropImage(sourceUri, outputX, outputY);
		mCropLayout.setOutputFormat(outputFormat);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.id_settings_crop_done: {
					mCropLayout.requestCropResult();
					break;
				}
                case R.id.id_settings_crop_cancel: {
                    finish();
                    break;
                }
				default:
					break;
			}
		}
	};

	private CropLayout.OnCropListener mOnCropListener = new CropLayout.OnCropListener() {

		@Override
		public void onCropResult(Uri data) {
            InputStream is = null;
            Bitmap bitmap = null;
            try {
                is = getContentResolver().openInputStream(data);
                bitmap = BitmapFactory.decodeStream(is);
                try {
                    saveFile(bitmap,"UserImg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(CropActivity.this, MyselfSettingsActivity.class);
			intent.setData(data);
            intent.putExtra("from_crop_activity",true);
			startActivity(intent);
            finish();
		}

		@Override
		public void onCropFailed(String errmsg) {

		}

		@Override
		public void onLoadingStateChanged(boolean isLoading) {
			if (mDoneBtn != null) {
                mDoneBtn.setEnabled(!isLoading);
			}
		}
	};



    /**
     * 保存Bitmap为文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        // 得到缓存文件夹
        String path = CropActivity.this.getCacheDir().getAbsolutePath();
        Log.d("Anonymous","path is: " + path);
        File dirFile = new File(path);
        // 文件夹不存在时，创建该文件夹
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        // 创建临时头像图片文件
        File myCaptureFile = new File(path + fileName +".jpg");
        // 保存临时头像图片文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        Log.d("Anonymous","myCaptureFile is: " + myCaptureFile.getAbsolutePath());
        // 得到图片路径
        String filePath = myCaptureFile.getAbsolutePath();
        // 得到当前用户
        final User user = BmobUser.getCurrentUser(this,User.class);
        Log.d("Anonymous","user nickname is: " + user.getNickName());
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        // 上传该临时头像文件
        bmobFile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Log.d("Anonymous","头像上传成功");
                User newUser = new User();
                newUser.setProfileImg(bmobFile);
                // 把该头像文件设置为该用户的头像
                newUser.update(CropActivity.this, user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("Anonymous","头像更新成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("Anonymous","头像更新失败：" + s + "错误码：" + i);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("Anonymous","头像上传失败：" + s + "错误码：" + i);
            }
        });

    }

}
