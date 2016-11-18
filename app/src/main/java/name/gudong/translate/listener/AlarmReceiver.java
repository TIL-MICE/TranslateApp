package name.gudong.translate.listener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.orhanobut.logger.Logger;

import me.gudong.translate.R;
import name.gudong.translate.GDApplication;
import name.gudong.translate.mvp.model.entity.translate.Result;
import name.gudong.translate.ui.activitys.MainActivity;
import name.gudong.translate.util.SpUtils;
import name.gudong.translate.util.Utils;

import static name.gudong.translate.GDApplication.mContext;

/**
 * Created by drakeet on 7/1/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getBooleanExtra("checkWords",false)){
            LiteOrm liteOrm = GDApplication.getAppComponent().getLiteOrm();
            QueryBuilder builder = new QueryBuilder(Result.class);
            WhereBuilder whereBuilder = new WhereBuilder(Result.class);
            whereBuilder.lessThan(Result.COL_CREATE_TIME,System.currentTimeMillis()).and().greaterThan(Result.COL_CREATE_TIME,System.currentTimeMillis());
            builder.where(whereBuilder);
            long count = liteOrm.queryCount(builder);
            Logger.i("count is "+count);
        }


        if (SpUtils.isNotifyDayline(context)) {
            if(Utils.isSDKHigh5()){
                Intent resultIntent = new Intent(mContext, MainActivity.class);
                resultIntent.putExtra("flag",1);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(mContext)
                                .setSmallIcon(R.drawable.icon_notification)
                                .setContentIntent(resultPendingIntent)
                                .setContentTitle("咕咚翻译")
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setVibrate(new long[]{0l})
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setContentText("还没有查看今天的每日一句吧,我已经准备好了。");

                NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification note = mBuilder.build();
                mNotificationManager.notify(this.hashCode(), note);
            }
        }
    }
}
