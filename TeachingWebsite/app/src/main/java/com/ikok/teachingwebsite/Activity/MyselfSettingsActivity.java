package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CourseImgLoader;
import com.ikok.teachingwebsite.Util.DataCleanManager;
import com.ikok.teachingwebsite.Util.UserImgLoader;
import com.kyo.imagecrop.CropFileUtils;
import com.kyo.imagecrop.CropUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/21.
 */
public class MyselfSettingsActivity extends Activity implements View.OnClickListener {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    /*
     * 设置页其他控件
     */
    private CircleImageView mUserImg;
    private TextView mChangeNickname;
    private TextView mChangeUserintro;
    private TextView mChangePassword;
    private RelativeLayout mClearCache;
    private TextView mCacheSize;
    private TextView mUpdate;
    private TextView mFeedback;
    private TextView mAbout;
    private Button mQuitBtn;
    /**
     * 图片缩放
     */
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_ALBUM = 2;
    private Uri mCameraImageUri;
    /**
     * 图片加载
     */
    private CourseImgLoader mImgLoader;
    /**
     * 是否从裁剪图片的活动跳转过来
     */
    private Boolean isFromCropActivity = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myself_settings);
//        StatusBarUtil.setColor(MyselfSettingsActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 得到当前用户
        User user = BmobUser.getCurrentUser(this,User.class);
        // 初始化所有控件
        initViews();

        //
        // 接收返回的裁剪好的图片，并显示在头像位置
        Intent intent = getIntent();
        // 得到标志位，如果是从裁剪图片的活动跳转过来，则直接显示附带传递的数据
        // 如果从个人界面点击设置进来，则直接从网络加载
        isFromCropActivity = intent.getBooleanExtra("from_crop_activity",false);

        if (isFromCropActivity){
            // 直接使用传递过来的数据转码显示
            InputStream input = null;
            try {
                if (intent != null && intent.getData() != null) {
                    input = getContentResolver().openInputStream(intent.getData());
                    mUserImg.setImageBitmap(BitmapFactory.decodeStream(input));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                CropUtils.closeSilently(input);
            }
        } else {

            // 进入该活动时加载头像，如果没有头像，则直接使用默认的头像
            // 如果有头像，从网络加载头像
            if (user.getProfileImg() == null){

            } else {
                String fileUrl = user.getProfileImg().getFileUrl(this);
//                Log.d("Anonymous","fileUrl is: " + fileUrl);
                new UserImgLoader(mUserImg).execute(fileUrl);
            }

        }



        // 设置顶部标题
        mTopbarTitle.setText("设置");
        // 得到缓存大小并显示
        try {
            mCacheSize.setText(DataCleanManager.getCacheSize(this.getCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 绑定相关监听器事件
        initEvents();
    }

    /**
     * 初始化所有控件
     */
    private void initViews() {
        mImgLoader = new CourseImgLoader();
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mUserImg = (CircleImageView) findViewById(R.id.id_settings_change_userImg);
        mChangeNickname = (TextView) findViewById(R.id.id_settings_change_nickname);
        mChangeUserintro = (TextView) findViewById(R.id.id_settings_change_userintro);
        mChangePassword = (TextView) findViewById(R.id.id_settings_change_password);
        mClearCache = (RelativeLayout) findViewById(R.id.id_settings_clear_cache);
        mCacheSize = (TextView) findViewById(R.id.id_settings_cache_size);
        mUpdate = (TextView) findViewById(R.id.id_settings_update);
        mFeedback = (TextView) findViewById(R.id.id_settings_feedback);
        mAbout = (TextView) findViewById(R.id.id_settings_about);
        mQuitBtn = (Button) findViewById(R.id.id_settings_quit);
    }

    /**
     * 为控件绑定事件
     */
    private void initEvents() {
        mTopbarBackBtn.setOnClickListener(this);
        mUserImg.setOnClickListener(this);
        mChangeNickname.setOnClickListener(this);
        mChangeUserintro.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mQuitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 顶部条的返回图片
            case R.id.id_top_bar_img:
                finish();
                break;
            // 修改头像
            case R.id.id_settings_change_userImg:
                changeUserImg();
                break;
            // 修改昵称
            case R.id.id_settings_change_nickname:
                Intent intent1 = new Intent(MyselfSettingsActivity.this,ChangeNicknameActivity.class);
                startActivity(intent1);
                break;
            // 修改签名
            case R.id.id_settings_change_userintro:
                Intent intent2 = new Intent(MyselfSettingsActivity.this,ChangeUserIntroActivity.class);
                startActivity(intent2);
                break;
            // 修改密码
            case R.id.id_settings_change_password:
                Intent intent3 = new Intent(MyselfSettingsActivity.this,ChangePasswordActivity.class);
                startActivity(intent3);
                break;
            // 清理缓存
            case R.id.id_settings_clear_cache:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("清理缓存");
                builder.setMessage("清理缓存后，部分图片需要重新加载，但不会影响应用使用，要清理吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.cleanInternalCache(MyselfSettingsActivity.this);
                        Toast.makeText(MyselfSettingsActivity.this,"清理成功",Toast.LENGTH_SHORT).show();
                        // 刷新缓存大小
                        try {
                            mCacheSize.setText(DataCleanManager.getCacheSize(MyselfSettingsActivity.this.getCacheDir()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();

                break;
            // 意见反馈
            case R.id.id_settings_update:
                Intent intent7 = new Intent(MyselfSettingsActivity.this,UpdateActivity.class);
                startActivity(intent7);
                break;
            // 意见反馈
            case R.id.id_settings_feedback:
                Intent intent4 = new Intent(MyselfSettingsActivity.this,FeedbackActivity.class);
                startActivity(intent4);
                break;
            // 关于Better
            case R.id.id_settings_about:
                Intent intent5 = new Intent(MyselfSettingsActivity.this,AboutActivity.class);
                startActivity(intent5);
                break;
            // 关于Better
            case R.id.id_settings_quit:
                // 清除缓存用户对象
                BmobUser.logOut(MyselfSettingsActivity.this);
                // 跳转到登录界面，给用户登录其他账号
                Intent intent6 = new Intent(MyselfSettingsActivity.this,LoginActivity.class);
                startActivity(intent6);
                break;
        }
    }

    /**
     * 修改头像
     * 创建对话框，提示是拍照还是从相册选取
     */
    private void changeUserImg() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
//        String[] lists = { "拍照上传", "相册选取", "取消" };
//        builder.setItems(lists, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        onSelectCamera();
//                        break;
//                    case 1:
//                        onSelectAlbum();
//                        break;
//                    case 2:
//
//                        break;
//                }
//            }
//        });

        String[] lists = { "相册选取", "取消" };
        builder.setItems(lists, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        onSelectAlbum();
                        break;
                    case 1:

                        break;
                }
            }
        });

        builder.setCancelable(true);
        builder.create().show();
    }

    /**
     * 修改头像的操作
     * @param requestCode   请求码
     * @param resultCode    结果码
     * @param data 数据意图
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                startCropper(REQUEST_CODE_CAMERA, data);
                break;
            case REQUEST_CODE_ALBUM:
                startCropper(REQUEST_CODE_ALBUM, data);
                break;
            default:
                break;
        }

    }

    /**
     * 拍照选取图片
     */
    private void onSelectCamera() {
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, R.string.camera_not_supported,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

            mCameraImageUri = CropFileUtils
                    .createRandomFileUri(getApplicationContext(), "ImageCropSample");
            if (mCameraImageUri == null) {
                return;
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 从相册选取图片
     */
    private void onSelectAlbum() {
        if (Build.VERSION.SDK_INT <= 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        }

    }

    /**
     * 选择好图片进行缩放
     * @param requestCode 请求码，是相机拍照，还是相册选取
     * @param data 图片数据
     */
    private void startCropper(int requestCode, Intent data) {
        Uri uri = null;
        if (requestCode == REQUEST_CODE_CAMERA) {
            uri = mCameraImageUri;
        } else if (data != null && data.getData() != null) {
            uri = data.getData();
        } else {
            return;
        }
        Intent intent = new Intent(this, CropActivity.class);
        intent.setData(uri);
        intent.putExtra("outputX", CropUtils.dip2px(this, 150));
        intent.putExtra("outputY", CropUtils.dip2px(this, 150));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivity(intent);
        // 防止修改头像后，顶部返回再次返回本页面，先把该活动从栈中移除
        this.finish();
    }


}
