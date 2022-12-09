package com.excellence.tvprovider.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.excellence.tvprovider.sample.content.ChannelContentHelper;
import com.excellence.tvprovider.sample.content.ChannelContents;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2022/12/7
 *     desc   :
 * </pre> 
 */
public class MainFragment extends GuidedStepSupportFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int ADD_CHANNEL_REQUEST = 1;

    private List<ChannelContents> mChannelContents;

    @Override
    public int onProvideTheme() {
        return R.style.Theme_Leanback_GuidedStep;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CHANNEL_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "channel added");

                /**
                 * Every time when add channel activity is finished, the LoadAddedChannels async task
                 * will be executed to fetch channels' publish status
                 */
                Toast.makeText(this.getActivity(), "Channel Added!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "could not add channel");
            }
        }
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = "MainScreen Channels";
        String breadcrumb = "Channel Customization";
        String description = "Toggle the check box";
        Drawable icon = getActivity().getDrawable(R.drawable.ic_launcher_background);
        return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        ChannelContentHelper.initializePlaylists(requireActivity());
        mChannelContents = ChannelContentHelper.getChannelContents();

        for (ChannelContents item : mChannelContents) {
            GuidedAction guidedAction = new GuidedAction.Builder(requireActivity())
                    .title(item.getCategory())
                    .description(item.getDescription())
                    .checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
                    .icon(R.drawable.ic_launcher_foreground)
                    .build();
            guidedAction.setId(item.getId());
            /**
             * Set checkbox status to false initially
             */
            guidedAction.setChecked(item.isChannelPublished());
            actions.add(guidedAction);
        }
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        super.onGuidedActionClicked(action);

        int id = (int) action.getId();
        for (ChannelContents channelContents : mChannelContents) {
            if (channelContents.getId() == id) {
                if (action.isChecked()) {
                    new ChannelContentHelper.CreateChannelInMainScreen(requireActivity()).execute(channelContents);
                } else {
                    new ChannelContentHelper.RemoveChannelInMainScreen(requireActivity()).execute(channelContents);
                }
                break;
            }
        }
    }

}
