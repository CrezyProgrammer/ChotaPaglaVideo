package com.pagalbeta.cartoonvideos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ddd.androidutils.DoubleClick;
import com.ddd.androidutils.DoubleClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pagalbeta.cartoonvideos.adapter.FacebookAdsRecyclerviewAdapter2;
import com.pagalbeta.cartoonvideos.databinding.ActivityPlayBinding;
import com.pagalbeta.cartoonvideos.entity.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;



public final class PlayActivity extends AppCompatActivity {

    private ActivityPlayBinding binding;
    private ArrayList<Video> item;
    private final FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    private YouTubePlayer player;
    private float duration;




    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(this.getLayoutInflater());

       setContentView(binding.getRoot());
       addYoutubePlayer();
      showRecycler();
       item = new ArrayList<Video>();




       binding.recommendRecycler.setLayoutManager(new LinearLayoutManager(this));
       binding.textView.setText(this.getIntent().getStringExtra("title"));
        binding.share.setOnClickListener(it -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = getIntent().getStringExtra("title");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                PlayActivity.this.startActivity(Intent.createChooser(shareIntent, (CharSequence)"choose one"));
            } catch (Exception var4) {
            }

        });
       doubleTapListener();
    }

    private final void doubleTapListener() {
        Log.i("123321", "135");

        DoubleClick doubleClick = new DoubleClick((new DoubleClickListener() {
            public void onDoubleClickEvent(@Nullable View view) {
                Log.i("123321", "137");
                    player.seekTo(duration + 10);
               binding.forwardText.setText("+10 sec");
                (new Handler()).postDelayed((Runnable)(new Runnable() {
                    public final void run() {
                        binding.forwardText.setText((CharSequence)"");
                    }
                }), 3000L);
            }

            public void onSingleClickEvent(@Nullable View view) {
                Log.i("123321", "135");

                binding.youtubePlayerView.performClick();
         binding.youtubePlayerView.getPlayerUiController().showUi(true);
            }
        }),300);

        binding.forward.setOnClickListener(doubleClick);
        DoubleClick doubleClickback = new DoubleClick((new DoubleClickListener() {
            public void onDoubleClickEvent(@Nullable View view) {
               player.seekTo(duration - 10);


                binding.backwardText.setText("-10 sec");
                (new Handler()).postDelayed((new Runnable() {
                    public final void run() {
                       binding.backwardText.setText("");
                    }
                }), 3000L);
            }

            public void onSingleClickEvent(@Nullable View view) {
                binding.youtubePlayerView.performClick();
               player.pause();


              player.play();


            }
        }), 300);
       binding.backward.setOnClickListener((OnClickListener)doubleClickback);
    }

    private final void addYoutubePlayer() {
        Log.i("123321", "22:" + this.getIntent().getStringExtra("id"));
        String id = getIntent().getStringExtra("id");

        final YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener((new AbstractYouTubePlayerListener() {
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                player=youTubePlayer;
                youTubePlayer.loadVideo(id, 0.0F);
                youTubePlayer.play();
            }

            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float duration2) {
                }

            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
             duration=second;
            }
        }));
        youTubePlayerView.addFullScreenListener((new YouTubePlayerFullScreenListener() {
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
               fullScreenHelper.enterFullScreen();
            }

            public void onYouTubePlayerExitFullScreen() {
                youTubePlayerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
               fullScreenHelper.exitFullScreen();
            }
        }));
    }

    public void onBackPressed() {

        if (binding.youtubePlayerView.isFullScreen()) {
           binding.youtubePlayerView.exitFullScreen();
        } else {


                super.onBackPressed();

        }

    }

    private final void showRecycler() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("videos");
        ref.addValueEventListener((ValueEventListener) (new ValueEventListener() {
            public void onCancelled(@NotNull DatabaseError p0) {

            }

            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    item.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Video video = new Video(dataSnapshot.child("id").getValue().toString(),
                                dataSnapshot.child("title").getValue().toString(),
                                dataSnapshot.child("duration").getValue().toString(), "popular",
                                dataSnapshot.child("date").getValue().toString(),
                                dataSnapshot.child("views").getValue().toString());
                        item.add(video);


                    }
                }
                Collections.shuffle(item);

                binding.recommendRecycler.setAdapter((
                        new FacebookAdsRecyclerviewAdapter2(
                                PlayActivity.this, item)));
            }

        }));

    }



}
