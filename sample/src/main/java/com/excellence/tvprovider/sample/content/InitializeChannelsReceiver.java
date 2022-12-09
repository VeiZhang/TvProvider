package com.excellence.tvprovider.sample.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2022/12/9
 *     desc   : Initializes channels and programs at installation time.
 *              https://github.com/googlecodelabs/tv-recommendations/blob/master/3-programs/src/main/java/com/example/android/tv/recommendations/InitializeChannelsReceiver.java
 *              官网的JobService启动需要时间，未验证，我们可以自己去创建线程启动
 * </pre> 
 */
public class InitializeChannelsReceiver extends BroadcastReceiver {

    private static final String TAG = InitializeChannelsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive(): " + intent);

    }
}
