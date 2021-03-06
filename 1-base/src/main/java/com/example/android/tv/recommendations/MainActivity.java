/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example.android.tv.recommendations;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.media.tv.TvContractCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.android.tv.recommendations.model.MockDatabase;
import com.example.android.tv.recommendations.model.Subscription;
import com.example.android.tv.recommendations.util.TvUtil;
import java.util.Arrays;
import java.util.List;

/*
 * Displays subscriptions that can be added to the main launcher's channels.
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int MAKE_BROWSABLE_REQUEST_CODE = 9001;

    private Button mTvSubscribeButton;
    private Button mVideoClipSubscribeButton;
    private Button mCatVideosSubscribeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvSubscribeButton = (Button) findViewById(R.id.subscribe_tv_button);
        mVideoClipSubscribeButton = (Button) findViewById(R.id.subscribe_video_button);
        mCatVideosSubscribeButton = (Button) findViewById(R.id.subscribe_cat_videos_button);

        final Subscription tvShowSubscription =
                MockDatabase.getTvShowSubscription(getApplicationContext());
        setupButtonState(mTvSubscribeButton, tvShowSubscription);

        final Subscription videoSubscription =
                MockDatabase.getVideoSubscription(getApplicationContext());
        setupButtonState(mVideoClipSubscribeButton, videoSubscription);

        final Subscription catVideosSubscription =
                MockDatabase.getCatVideosSubscription(getApplicationContext());
        setupButtonState(mCatVideosSubscribeButton, catVideosSubscription);

        TvUtil.scheduleSyncingChannel(this);
    }

    private void setupButtonState(Button button, final Subscription subscription) {
        boolean channelExists = subscription.getChannelId() > 0L;
        button.setEnabled(!channelExists);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AddChannelTask(getApplicationContext()).execute(subscription);
                    }
                });
    }

    private class AddChannelTask extends AsyncTask<Subscription, Void, Long> {

        private final Context mContext;

        AddChannelTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected Long doInBackground(Subscription... varArgs) {
            List<Subscription> subscriptions = Arrays.asList(varArgs);
            if (subscriptions.size() != 1) {
                return -1L;
            }
            Subscription subscription = subscriptions.get(0);
            // TODO: step 17 create channel. Replace declaration with code from code lab.
            long channelId = -1L;

            subscription.setChannelId(channelId);
            MockDatabase.saveSubscription(mContext, subscription);
            // Scheduler listen on channel's uri. Updates after the user interacts with the system
            // dialog.
            TvUtil.scheduleSyncingProgramsForChannel(getApplicationContext(), channelId);
            return channelId;
        }

        @Override
        protected void onPostExecute(Long channelId) {
            super.onPostExecute(channelId);
            promptUserToDisplayChannel(channelId);
        }
    }

    private void promptUserToDisplayChannel(long channelId) {
        // TODO: step 18 prompt user.

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO step 19 handle response

    }
}