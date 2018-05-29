package com.example.administrator.testnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button testNotify;
    private TextView title;
    private TextView current;
    private TextView max;
    private TextView checkAllow;
    private AudioManager mAudioManager;
    private String packageName;//要获取的应用的包名
    private String appName;
    private boolean isThisApp;//是否是本应用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        packageName = getIntent().getStringExtra("packageName");
        isThisApp = TextUtils.equals(packageName, getPackageName());
        appName = getIntent().getStringExtra("appName");
        title = findViewById(R.id.title);
        title.setText(appName);
        current = findViewById(R.id.current);
        max = findViewById(R.id.max);
        checkAllow = findViewById(R.id.check_allow);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        testNotify = findViewById(R.id.testNotify);
        testNotify.setVisibility(isThisApp ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVoice();
    }

    //<editor-fold desc="功能抽取">
    //获取声音大小
    private void getVoice() {
        //提示声音音量
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        max.setText(String.format("系统通知最大音量: %d", streamMaxVolume));
        current.setText(String.format("当前系统通知音量: %d", streamVolume));
        if (isThisApp && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkAllow.setVisibility(View.VISIBLE);
            checkAllow.setText(String.format("系统是否允许接收通知: %s", NotificationsUtils.isNotificationEnabled(this)));
        } else {
            checkAllow.setVisibility(View.GONE);
        }
    }

    //更改声音大小
    private void changeVoice(boolean add) {
        mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_SYSTEM,
                add ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_PLAY_SOUND);
        getVoice();
    }

    //发出一个测试通知
    private void testNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)//设置默认提示音,灯光,和震动
                .setAutoCancel(true)
                .setContentTitle("测试通知")
                .build();
        manager.notify(111, notification);
    }
    //</editor-fold>

    //<editor-fold desc="点击事件">

    /**
     * 跳转到万师傅的系统设置
     */
    public void justToAPPDetailInfo(View view) {
        NotificationsUtils.jumpToAPPDetailInfo(this, 11, packageName);
    }

    /**
     * 跳转到通知设置页面
     */
    public void justToNotifySetting(View view) {
        NotificationsUtils.jumpToNotifySetting(this, 11, packageName);
    }

    /**
     * 增加通知音量
     */
    public void add(View view) {
        changeVoice(true);
    }

    /**
     * 减小通知音量
     */
    public void remove(View view) {
        changeVoice(false);
    }

    /**
     * 测试通知
     */
    public void testNofify(View view) {
        testNotify();
    }

    /**
     * 刷新通知相关信息显示
     */
    public void refresh(View view) {
        getVoice();
    }
    //</editor-fold>
}
