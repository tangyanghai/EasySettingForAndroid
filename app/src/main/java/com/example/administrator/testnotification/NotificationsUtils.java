package com.example.administrator.testnotification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Administrator
 * @time : 9:21
 * @for :
 */
public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        return manager.areNotificationsEnabled();
    }

    /**
     * 跳转到通知设置
     *
     * @param activity    需要跳转到设置页面的activity
     * @param requestCode 请求码
     */
    public static void jumpToNotifySetting(Activity activity, int requestCode) {
        jumpToNotifySetting(activity,requestCode,null);
    }

    /**
     * 跳转到通知设置
     *
     * @param activity    需要跳转到设置页面的activity
     * @param requestCode 请求码
     */
    public static void jumpToNotifySetting(Activity activity, int requestCode, String packageName) {
        if (packageName == null) {
            packageName = activity.getPackageName();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", packageName);
            PackageManager pm = activity.getPackageManager();
            try {
                @SuppressLint("WrongConstant") ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
                intent.putExtra("app_uid", ai.uid);
                activity.startActivityForResult(intent, requestCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + packageName));
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 跳转到引用的系统详情页面
     */
    public static void jumpToAPPDetailInfo(Activity activity, int requestCode) {
        jumpToAPPDetailInfo(activity, requestCode, null);
    }

    /**
     * 跳转到引用的系统详情页面
     */
    public static void jumpToAPPDetailInfo(Activity activity, int requestCode, String packageName) {
        if (packageName == null) {
            packageName = activity.getPackageName();
        }

        Intent intent =
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取所有的第三方应用列表
     *
     * @param context
     */
    public static List<ResolveInfo> getAllThirdPartApp(Context context) {
        //获取全部应用：
        PackageManager packageManager = context.getPackageManager();
        Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> listAllApps = packageManager.queryIntentActivities(mIntent, 0);
        List<ResolveInfo> resolveInfos = new ArrayList<>();
        for (int i = 0; i < listAllApps.size(); i++) {
            //判断是否系统应用：
            ResolveInfo appInfo = listAllApps.get(i);
            String pkgName = appInfo.activityInfo.packageName;//获取包名
            //根据包名获取PackageInfo mPackageInfo;（需要处理异常）
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, 0);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    //第三方应用
                    resolveInfos.add(appInfo);
                } else {
                    //系统应用 丢掉不要了...
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return resolveInfos;
    }

    /**
     * 获取ResolveInfo信息详情
     */
    public static ThirdPartActivity.AppInfoBean getInfo(ResolveInfo info, PackageManager packageManager) {
        ThirdPartActivity.AppInfoBean bean = new ThirdPartActivity.AppInfoBean();
        //获取包名：
        bean.mPackageName = info.activityInfo.packageName;
        //获取icon：
        bean.mdrawable = info.loadIcon(packageManager);
        //获取应用名：
        bean.nName = info.loadLabel(packageManager).toString();
        return bean;
    }

}
