package com.example.mohamed.bakingapp.fragments;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.annotation.Nullable;

import com.example.mohamed.bakingapp.Activities.MainActivity;
import com.example.mohamed.bakingapp.Activities.VideoActivity;
import com.example.mohamed.bakingapp.Model.RecipeSteps;
import com.example.mohamed.bakingapp.Model.Recipes;
import com.example.mohamed.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Mohamed on 8/16/2018.
 */

public class video_detail_fragment extends Fragment implements ExoPlayer.EventListener {

    private static final java.lang.String TAG = video_detail_fragment.class.getSimpleName();
    private static final java.lang.String POS_TAG ="pos" ;
    public Recipes mrecipes = new Recipes();
    public static final String RECIPE_STEP_ARRAY = "rec_step";
    public String title;
   public SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    public int pos;
    public  MediaSource mediaSource;
    public LinearLayout.LayoutParams params;
    public static boolean PlayWhenReady=true;
    public static Long seekTo;
    public static final String SEEK_TO_KEY="seekto";
    public static final String PLAY_WHEN_READY_KEY="playwhenready";
    @BindView(R.id.thumnail) ImageView thumnail;
    @BindView(R.id.step_long_desc)TextView step;
    @BindView(R.id.horizontalHalf)Guideline guideline;
    @BindView(R.id.playerView)SimpleExoPlayerView mPlayerView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_detail_fragment, container, false);

        ButterKnife.bind(this,view);
        if (savedInstanceState != null) {
            mrecipes = (Recipes) savedInstanceState.getSerializable(RECIPE_STEP_ARRAY);
            seekTo=savedInstanceState.getLong(SEEK_TO_KEY);
            PlayWhenReady=savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
        Bundle bundle = this.getArguments();
        pos = bundle.getInt(POS_TAG, -1);
        thumnail.setVisibility(View.GONE);
        if(tabletSize) {
            mrecipes = (Recipes) bundle.getSerializable("yaw");
        }

        Log.v("yaw",pos+"");


        setStepText();

       if (mrecipes.getRecipeSteps().get(pos).getVideoURL() != null && !mrecipes.getRecipeSteps().get(pos).getVideoURL().equals("")) {
           if(tabletSize) {
               initializePlayer(Uri.parse(mrecipes.getRecipeSteps().get(pos).getVideoURL()));
           }
            initializeMediaSession();
            mPlayerView.setVisibility(View.VISIBLE);
        }

        else if(mrecipes.getRecipeSteps().get(pos).getThumnailUrl() != null&&!mrecipes.getRecipeSteps().get(pos).getThumnailUrl().equals("")) {
            releasePlayer();
            Log.v("yaw","yawwe");
           mPlayerView.setVisibility(View.GONE);
           thumnail.setVisibility(View.VISIBLE);

           Picasso.get().load(mrecipes.getRecipeSteps().get(pos).getThumnailUrl())
                   .error(R.drawable.nothum)
                   .into(thumnail)

           ;


        }
        else
       {
           releasePlayer();
           mPlayerView.setVisibility(View.GONE);
       }


        return view;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Bundle bundle = this.getArguments();
            pos = bundle.getInt(POS_TAG, -1);


        }

    }

    public void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(false);

    }
    public void initializePlayer(Uri mediaUri) {
        //MediaSource mediaSource=null;
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
             mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);

           mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }

    }
    public void releasePlayer() {

        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer=null;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        seekTo=null;


    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer!=null) {
            seekTo = mExoPlayer.getCurrentPosition();
            PlayWhenReady=mExoPlayer.getPlayWhenReady();
        }
        releasePlayer();

    }




    @Override
    public void onResume() {
        super.onResume();

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE&&!tabletSize)
                {


            params.height = params.MATCH_PARENT;
            params.width = params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            guideline.setVisibility(View.GONE);
            ((VideoActivity) getActivity()).tabLayout.setVisibility(View.GONE);


        }





    }



    public void setVideoAndImage(Recipes recipes) {

        mrecipes = recipes;
    }
    public void setStepText()
    {

       step.setText(mrecipes.getRecipeSteps().get(pos).getDescription());
    }

    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable(RECIPE_STEP_ARRAY, mrecipes);
        if(seekTo!=null) {
            currentState.putLong(SEEK_TO_KEY, seekTo);
        }
            currentState.putBoolean(PLAY_WHEN_READY_KEY,PlayWhenReady);






    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }



    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY&&playWhenReady)){

            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,

                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){

            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }



    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver
    {

        public MediaReceiver()
        {

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession,intent);
        }
    }
}
