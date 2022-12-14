package com.excellence.tvprovider.sample.content;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.excellence.basetoolslibrary.utils.FileIOUtils;
import com.excellence.tvprovider.sample.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.tvprovider.media.tv.Channel;
import androidx.tvprovider.media.tv.ChannelLogoUtils;
import androidx.tvprovider.media.tv.PreviewProgram;
import androidx.tvprovider.media.tv.TvContractCompat;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2022/12/7
 *     desc   : 通道 Row，一行中的title和列表集合
 *              https://github.com/googlearchive/leanback-showcase/blob/master/app/src/main/java/android/support/v17/leanback/supportleanbackshowcase/app/rows/PublishChannelFragment.java
 *              https://developer.android.com/training/tv/discovery/recommendations-channel?hl=zh-cn
 * </pre> 
 */
public class ChannelContentHelper {

    private static final String TAG = ChannelContentHelper.class.getSimpleName();

    private static final int ADD_CHANNEL_REQUEST = 1;

    private static List<ChannelContents> sChannelContents;

    private ChannelContentHelper() {
    }

    public static void initializePlaylists(Context context) {
        if (sChannelContents == null) {
            sChannelContents = new ArrayList<>();

            String json = FileIOUtils.readFile2String(context.getResources().openRawResource(R.raw.movie), null);

            sChannelContents = new Gson().fromJson(json,
                    new TypeToken<List<ChannelContents>>() {
                    }.getType());
        }
    }

    public static List<ChannelContents> getChannelContents() {
        return sChannelContents;
    }

    /**
     * 主动触发：添加到主屏列表
     */
    public static final class CreateChannelInMainScreen extends AsyncTask<ChannelContents, Void, Long> {

        private Activity mActivity;

        public CreateChannelInMainScreen(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected Long doInBackground(ChannelContents... params) {
            return addChannelAction(mActivity, params[0]);
        }

        private Long addChannelAction(Context context, ChannelContents channelContents) {
            String inputId = createInputId(context);
            // 1.创建Channel通道

            Channel channel = new Channel.Builder()
                    .setDisplayName(channelContents.getCategory())
                    .setDescription(channelContents.getDescription())
                    .setType(TvContractCompat.Channels.TYPE_PREVIEW)
                    /**
                     * 通道输入ID，作为查询的标识，如果为空则下面更新Channel的操作会出现异常：NOT NULL constraint failed: channels.input_id
                     */
                    .setInputId(inputId)
                    // 1.1点击MainScreen左边上的分类，跳转
                    .setAppLinkIntent(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()))
                    .setInternalProviderId(channelContents.getChannelId())
                    .build();

            Uri channelUri = null;
            if (checkChannel(context, channelContents)) {
                channelUri = TvContractCompat.buildChannelUri(channelContents.getId());
                /**
                 * 已存在时，需要更新Channel的操作
                 *
                 * Selection not allowed for content://android.media.tv/channel 异常查询：https://stackoverflow.com/questions/31377712/live-channels-query-returns-security-exception
                 * 可以更换TvContractCompat.Channels.CONTENT_URI为content://android.media.tv/channel/150
                 */
                int result = context.getContentResolver().update(
                        Uri.parse(String.format("content://android.media.tv/channel/%s", channelContents.getId())),
                        channel.toContentValues(), null, null);
                if (result < 1) {
                    Log.e(TAG, "addChannel: update failed");
                } else {
                    Log.i(TAG, "addChannelAction: update success");
                }
            } else {
                channelUri = context.getContentResolver().insert(TvContractCompat.Channels.CONTENT_URI,
                        channel.toContentValues());
            }

            if (channelUri == null || channelUri.equals(Uri.EMPTY)) {
                Log.e(TAG, "addChannel: failed");
                return null;
            }

            /**
             * 2.这个是通道的ID
             */
            long channelId = ContentUris.parseId(channelUri);
            channelContents.publishId(channelId);
            /**
             * 3.在MainScreen上左侧显示的图标
             */
            createChannelLogo(context, channelId, R.drawable.logo);
            addProgramAction(context, channelId, channelContents.getVideos());

            return channelId;
        }

        private void addProgramAction(Context context, long channelId, List<Videos> videosList) {
            int result = context.getContentResolver().delete(TvContractCompat.buildPreviewProgramsUriForChannel(channelId), null, null);
            if (result < 1) {
                Log.i(TAG, "addProgramAction: empty list");
            } else {
                Log.i(TAG, "addProgramAction: clear");
            }

            addPrograms(context, channelId, videosList);
        }

        private void addPrograms(Context context, long channelId, List<Videos> videosList) {
            for (Videos clip : videosList) {
                String clipId = clip.getVideoId();

                Intent programIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                programIntent.putExtra("clipId", clipId);

                PreviewProgram program = new PreviewProgram.Builder()
                        .setChannelId(channelId)
                        .setTitle(clip.getTitle())
                        .setDescription(clip.getDescription())
                        .setPosterArtUri(Uri.parse(clip.getCard()))
                        // 4.点击MainScreen上的节目，跳转播放
                        .setIntent(programIntent)
                        // 5.预览，MainScreen选中的时候能自动播放
                        .setPreviewVideoUri(Uri.parse(clip.getPreview()))
                        .setInternalProviderId(clipId)
                        .setWeight(videosList.size())
                        .setPosterArtAspectRatio(TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9)
                        .setType(TvContractCompat.PreviewPrograms.TYPE_MOVIE)
                        .build();

                Uri programUri = context.getContentResolver().insert(TvContractCompat.PreviewPrograms.CONTENT_URI,
                        program.toContentValues());
                if (programUri == null || programUri.equals(Uri.EMPTY)) {
                    Log.e(TAG, "addProgram: failed");
                } else {
                    long programId = ContentUris.parseId(programUri);
                }
            }
        }

        /**
         * 检查Channel是否已添加
         */
        private boolean checkChannel(Context context, ChannelContents channelContents) {
            Cursor cursor = context.getContentResolver().query(TvContractCompat.Channels.CONTENT_URI, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Channel channel = Channel.fromCursor(cursor);
                    /**
                     * 根据 数据的Id标识 判别是否已添加
                     */
                    if (channelContents.getChannelId().equals(channel.getInternalProviderId())) {
                        /**
                         * 由于通道Id是空的，需要赋值
                         */
                        channelContents.publishId(channel.getId());
                        return true;
                    }

                } while (cursor.moveToNext());
            }
            return false;
        }

        private void createChannelLogo(Context context, long channelId, int drawableId) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            ChannelLogoUtils.storeChannelLogo(context, channelId, bitmap);
        }

        private String createInputId(Context context) {
            return TvContractCompat.buildInputId(new ComponentName(context.getPackageName(), ""));
        }

        @Override
        protected void onPostExecute(Long channelId) {
            super.onPostExecute(channelId);

            /**
             * 1.不执行Intent时，没有提示框提醒，默认可以去customize channels 那里手动添加到MainScreen
             *
             * 2.执行了Intent，则显示提示框，然后确认添加到MainScreen
             */

//            try {
//                if (channelId == null) {
//                    Log.e(TAG, "onPostExecute: failed to add channel on home screen");
//                    return;
//                }
//
//                Intent intent = new Intent(TvContract.ACTION_REQUEST_CHANNEL_BROWSABLE);
//                intent.putExtra(TvContractCompat.EXTRA_CHANNEL_ID, channelId);
//                mActivity.startActivityForResult(intent, ADD_CHANNEL_REQUEST);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            TvContractCompat.requestChannelBrowsable(mActivity, channelId);
        }
    }

    /**
     * 在本应用内主动触发：移除主屏列表
     */
    public static final class RemoveChannelInMainScreen extends AsyncTask<ChannelContents, Void, Void> {

        private Context mContext;

        public RemoveChannelInMainScreen(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(ChannelContents... params) {
            ChannelContents playlist = params[0];
            deleteChannel(mContext, playlist.getId());
            return null;
        }

        private void deleteChannel(Context context, long id) {
            int rowsDeleted = context.getContentResolver().delete(TvContractCompat.buildChannelUri(id), null, null);
            for (ChannelContents item : sChannelContents) {
                if (item.getId() == id) {
                    item.unPublish();
                }
            }

            if (rowsDeleted < 1) {
                Log.e(TAG, "deleteChannel: failed");
            }
        }
    }

}
