package com.excellence.tvprovider.sample.content;

import java.util.List;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2022/12/7
 *     desc   : 通道 Row 实体类
 * </pre>
 */
public class ChannelContents {

    /**
     * channelId : 0
     * category : Google+
     * videos : [{"description":"Jon introduces Instant Upload with a few thoughts on how we remember the things that matter. Check out some ways we've been rethinking real-life sharing for the web at plus.google.com","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","title":"Instant Upload","category":"Google+","videoId":"0"},{"description":"With Google+ Instant Upload, every picture you take on your phone is instantly backed up to a private Google+ album. It's a simple way to make sure you never lose another memory.","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20New%20Dad.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20New%20Dad.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20New%20Dad/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20New%20Dad/bg.jpg","title":"New Dad","category":"Google+","videoId":"1"},{"description":"Laugh, share news, celebrate, learn something new or stay in touch with Hangouts. And with Hangouts on your phone, you can drop in from wherever you are.","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Say%20more%20with%20Hangouts.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Say%20more%20with%20Hangouts.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Say%20more%20with%20Hangouts/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Say%20more%20with%20Hangouts/bg.jpg","title":"Say more with Hangouts","category":"Google+","videoId":"2"},{"description":"Search on Google+ helps you get advice from the people you know -- sometimes when you least expect it. Check out some ways we've been rethinking real-life sharing for the web at plus.google.com.","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Search.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Search.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Search/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Search/bg.jpg","title":"Google+ Search","category":"Google+","videoId":"3"},{"description":"New ways of sharing the right things with the right people. Join at http://google.com/+","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Sharing%20but%20like%20real%20life.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Sharing%20but%20like%20real%20life.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Sharing%20but%20like%20real%20life/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Sharing%20but%20like%20real%20life/bg.jpg","title":"Sharing but like real life","category":"Google+","videoId":"4"},{"description":"Jed introduces Circles with a few thoughts on the nature of friendship. Check out some ways we've been rethinking real-life sharing for the web at plus.google.com.","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Circles.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Circles.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Circles/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Circles/bg.jpg","title":"Google+ Circles","category":"Google+","videoId":"5"},{"description":"Aimee introduces Hangouts with a few thoughts on the spontaneous get-together. Check out some ways we've been rethinking real-life sharing for the web at plus.google.com.","source":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Hangouts.mp4","preview":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Hangouts.mp4","card":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Hangouts/card.jpg","background":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Hangouts/bg.jpg","title":"Google+ Hangouts","category":"Google+","videoId":"6"}]
     */

    /**
     * 字符串标识，而非通道标识
     */
    private String channelId;
    private String category;
    private List<Videos> videos;

    /**
     * The channel's description shown in the main screen after adding it
     */
    private final String mDescription = "Demo To Show how to add channel from App to main screen";

    /*****************************************************************************/
    /**
     * 唯一通道标识
     */
    private long mId;

    /**
     * Is current channel published or not
     * This is the symbol to toggle add/ remove behavior
     */
    private boolean mChannelPublished;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Videos> getVideos() {
        return videos;
    }

    public void setVideos(List<Videos> videos) {
        this.videos = videos;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isChannelPublished() {
        return mChannelPublished;
    }

    public long getId() {
        return mId;
    }

    public void publishId(long id) {
        mId = id;
        mChannelPublished = true;
    }

    public void unPublish() {
        mChannelPublished = false;
    }
}
