package com.example.mohamed.bakingapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.mohamed.bakingapp.Model.Recipes;
import com.example.mohamed.bakingapp.R;
import com.example.mohamed.bakingapp.fragmentAdapter.SectionPageAdapter;
import com.example.mohamed.bakingapp.fragments.video_detail_fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity  {

    private SectionPageAdapter mSectionPageAdapter;
    public Recipes recipes=new Recipes();
    public  int pos;
    private static final java.lang.String POS_TAG ="pos" ;
    public @BindView(R.id.tabs)TabLayout tabLayout;
    @BindView(R.id.container)ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        mSectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        ButterKnife.bind(this);
        tabLayout.setupWithViewPager(mViewPager);




        Intent i =getIntent();
        pos=i.getIntExtra(RecipeDetails.ADAPTER_POS_TAG,-1);
        if(i.hasExtra(RecipeDetails.ARRAY_TAG_TO_VIDEO_ACTIVITY)) {
            recipes = (Recipes) i.getSerializableExtra(RecipeDetails.ARRAY_TAG_TO_VIDEO_ACTIVITY);
        }

        setupViewPager();



    }

    public void setupViewPager()
    {

        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());

        for(int i=0;i<recipes.getRecipeSteps().size();i++)
        {

            video_detail_fragment video_detail_fragmen=new video_detail_fragment();
            video_detail_fragmen.setVideoAndImage(recipes);
            Bundle b = new Bundle();
            b.putInt(POS_TAG,i);
            video_detail_fragmen.setArguments(b);
            adapter.addFragment(video_detail_fragmen,"Step "+i);

        }

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(pos);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



                if(position!=recipes.getRecipeSteps().size()-1) {
                    video_detail_fragment frag2 = (video_detail_fragment) mViewPager
                            .getAdapter()
                            .instantiateItem(mViewPager, position + 1);
                    frag2.releasePlayer();
                  //  Log.v("yaw","forward stop");
                }
                if(position!=0) {
                    video_detail_fragment frag1 = (video_detail_fragment) mViewPager
                            .getAdapter()

                            .instantiateItem(mViewPager, position - 1);
                    frag1.releasePlayer();
                  //  Log.v("yaw","prev stop");
                }
                video_detail_fragment fragmain = (video_detail_fragment) mViewPager
                        .getAdapter()
                        .instantiateItem(mViewPager, position);



                fragmain.initializePlayer(Uri.parse(recipes.getRecipeSteps().get(position).getVideoURL()));
                if(fragmain.mExoPlayer!=null&&fragmain.seekTo!=null) {
                    fragmain.mExoPlayer.seekTo(fragmain.seekTo);
                    fragmain.mExoPlayer.setPlayWhenReady(fragmain.PlayWhenReady);
                }
                fragmain.seekTo=null;
                fragmain.PlayWhenReady=true;

              //  Log.v("yaw","current plauing ");
                fragmain.getActivity().setTitle(recipes.getRecipeSteps().get(position).getShortDescription());
              //  Log.v("yaw","happen when on VIDEO_ACTIVITY");



            }

            @Override
            public void onPageSelected(int position) {



                }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
