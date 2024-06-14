package bizmessage.in.touchswitch.ui.others.youtube;



import android.content.Context;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.model.YoutubeRequestResponse;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;

import bizmessage.in.touchswitch.ui.others.youtube.adapter.YoutubeRecyclerAdapter;
import bizmessage.in.touchswitch.ui.others.youtube.model.YoutubeVideo;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeHelp extends AppCompatActivity {

    @BindView(R.id.recyclerViewFeed)
    RecyclerView recyclerViewFeed;
    // private YoutubeListAdapter youtubeListAdapter;
    YoutubeRecyclerAdapter mRecyclerAdapter;
    ArrayList mYoutubeVideo;
    YoutubeVideo video1,video2,video3,video4,video5,video6,video7,video8,video9,video10,video11,video12,video13,video14,video15;
Context context;


    private final ArrayList<String> desc = new ArrayList<>();

    YoutubeRequestResponse youtubeRequestResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_list);

        ButterKnife.bind(this);

        // prepare data for list
        List<YoutubeVideo> youtubeVideos = prepareList();
        mRecyclerAdapter = new YoutubeRecyclerAdapter(youtubeVideos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFeed.setLayoutManager(mLayoutManager);
        recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeed.setAdapter(mRecyclerAdapter);



    }

    private List<YoutubeVideo> prepareList() {

        mYoutubeVideo = new ArrayList();
        video1 = new YoutubeVideo();
        video2 = new YoutubeVideo();
        video3 = new YoutubeVideo();
        video4 = new YoutubeVideo();
        video5 = new YoutubeVideo();
        video6 = new YoutubeVideo();
        video7 = new YoutubeVideo();
        video8 = new YoutubeVideo();
        video9 = new YoutubeVideo();
        video10 = new YoutubeVideo();
        video11 = new YoutubeVideo();
        video12 = new YoutubeVideo();
        video13 = new YoutubeVideo();
        video14 = new YoutubeVideo();
        video15 = new YoutubeVideo();


        if (!Utility.isNetworkAvailable(this)) {
         // checkConnection(YoutubeHelp.this,getApplicationContext());
        } else {
            Utility.showProgress(this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<YoutubeRequestResponse> responseCall = service.getMyYoutubeData("all", "all", PreferenceData.getlanguage());
            responseCall.enqueue(new Callback<YoutubeRequestResponse>() {
                @Override
                public void onResponse(Call<YoutubeRequestResponse> call, Response<YoutubeRequestResponse> response) {
                    if (response.isSuccessful()) {
                         youtubeRequestResponse = response.body();
                           desc.clear();


                        if (youtubeRequestResponse.getSuccess() == 1) {


                            for (int i=0;i<youtubeRequestResponse.getEsps().size();i++){
                                String imageURL = (String) ((LinkedTreeMap) response.body().getEsps().get(i)).get("imageURL");
                                String title = (String) ((LinkedTreeMap) response.body().getEsps().get(i)).get("title");
                                String tubeid = (String) ((LinkedTreeMap) response.body().getEsps().get(i)).get("tubeid");







                                if(i==0) {

                                    video1.setId(1L);
                                    video1.setTitle(title);
                                    video1.setImageUrl(imageURL);
                                    video1.setVideoId(tubeid);
                                    mYoutubeVideo.add(video1);
                                }

                                if(i==1) {
                                     video2.setId(2L);
                                    video2.setTitle(title);
                                   video2.setImageUrl(imageURL);
                                    video2.setVideoId(tubeid);
                                    mYoutubeVideo.add(video2);
                                }

                                if(i==2) {
                                     video3.setId(3L);
                                    video3.setTitle(title);
                                     video3.setImageUrl(imageURL);
                                    video3.setVideoId(tubeid);
                                    mYoutubeVideo.add(video3);
                                }


                                if(i==3) {
                                     video4.setId(4L);
                                    video4.setTitle(title);
                                     video4.setImageUrl(imageURL);
                                    video4.setVideoId(tubeid);
                                    mYoutubeVideo.add(video4);
                                }

                                if(i==4) {
                                     video5.setId(5L);
                                    video5.setTitle(title);
                                     video5.setImageUrl(imageURL);
                                    video5.setVideoId(tubeid);
                                    mYoutubeVideo.add(video5);
                                }
                                if(i==5) {
                                     video6.setId(6L);
                                    video6.setTitle(title);
                                     video6.setImageUrl(imageURL);
                                    video6.setVideoId(tubeid);
                                    mYoutubeVideo.add(video6);
                                }
                                if(i==6) {
                                     video7.setId(7L);
                                    video7.setTitle(title);
                                     video7.setImageUrl(imageURL);
                                    video7.setVideoId(tubeid);
                                    mYoutubeVideo.add(video7);
                                }
                                if(i==7) {
                                     video8.setId(8L);
                                    video8.setTitle(title);
                                     video8.setImageUrl(imageURL);
                                    video8.setVideoId(tubeid);
                                    mYoutubeVideo.add(video8);
                                }
                                if(i==8) {
                                     video9.setId(9L);
                                    video9.setTitle(title);
                                     video9.setImageUrl(imageURL);
                                    video9.setVideoId(tubeid);
                                    mYoutubeVideo.add(video9);
                                }
                                if(i==9) {
                                     video10.setId(10L);
                                    video10.setTitle(title);
                                     video10.setImageUrl(imageURL);
                                    video10.setVideoId(tubeid);
                                    mYoutubeVideo.add(video10);
                                }
                                if(i==10) {
                                     video11.setId(11L);
                                    video11.setTitle(title);
                                     video11.setImageUrl(imageURL);
                                    video11.setVideoId(tubeid);
                                    mYoutubeVideo.add(video4);
                                }
                                if(i==11) {
                                     video12.setId(12L);
                                    video12.setTitle(title);
                                     video12.setImageUrl(imageURL);
                                    video12.setVideoId(tubeid);
                                    mYoutubeVideo.add(video12);
                                }
                                if(i==12) {
                                     video13.setId(13L);
                                    video13.setTitle(title);
                                     video13.setImageUrl(imageURL);
                                    video13.setVideoId(tubeid);
                                    mYoutubeVideo.add(video13);
                                }
                                if(i==13) {
                                     video14.setId(14L);
                                    video14.setTitle(title);
                                     video14.setImageUrl(imageURL);
                                    video14.setVideoId(tubeid);
                                    mYoutubeVideo.add(video14);
                                }

                                if(i==14) {

                                    video15.setId(15L);
                                    video15.setTitle(title);
                                     video15.setImageUrl(imageURL);
                                    video15.setVideoId(tubeid);
                                    mYoutubeVideo.add(video15);
                                }

                            }
                            //               alertListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        //             Utility.showSnackBar(binding.cardWebView,response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<YoutubeRequestResponse> call, Throwable t) {

                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast("Website Not reachable, Please check internet");
                    }
                }

            });

        }



        // add first item as default if not it will crash.??

      video1.setId(1L);
        video1.setTitle("IoT Technology Trends in 2021");
        video1.setImageUrl("http://www.ilmc.xyz/onlook/dashimage/iot.JPG");
        video1.setVideoId("nt00cm7irVE");
        mYoutubeVideo.add(video1);



        return mYoutubeVideo;
    }



}

