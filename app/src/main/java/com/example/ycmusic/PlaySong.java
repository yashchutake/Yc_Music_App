package com.example.ycmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    //Control O for override
    @Override
    protected void onDestroy() {//for destroy the activity on back button//stop media player
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();//Stop the Thread Not chalta raha hamesha
    }

    TextView textView;
    ImageView previous,play,next;
    ArrayList<File> songs;//array list to store a song//Array list coming from Intent For that one
    MediaPlayer mediaPlayer;
    String textContent;//for song name
    int position;
    SeekBar seekBar;

    Thread updateSeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        textView=findViewById(R.id.textView);
        previous=findViewById(R.id.previous);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);

        Intent intent=getIntent();//give me a intent
        Bundle bundle=intent.getExtras();//get the bundle object
        songs=(ArrayList)bundle.getParcelableArrayList("songList");//taking the passed array list
        textContent= intent.getStringExtra("currentSong");//taking the name of song and store in String
        textView.setText(textContent);//setting name

        textView.setSelected(true);//for horizontally scrolling the view LOOK WELL

        position=intent.getIntExtra("position",0);//position of a song
        Uri uri= Uri.parse(songs.get(position).toString());//actual location of song we have to Play

        mediaPlayer=MediaPlayer.create(this,uri);//Giving url of song which we want to play
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());//set maximum val of seek bar


        //seek BAR
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());//when user touch the seek bar
            }
        });

        updateSeek =new Thread(){
            @Override
            public void run() {
                //seekBar ko Update karte jao karte jao
                int currentPosition=0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()){//update time to if Piche chut jaye toh
                        currentPosition=mediaPlayer.getCurrentPosition();//update seek propperly to seek Well updated
                        seekBar.setProgress(currentPosition);//set seek bar
                        sleep(800);//sleep for some millis for low use Resources
                    }
                }
                catch (Exception e){
                    e.printStackTrace();//Traditional way to catch exception in java
                }

            }
        };
        updateSeek.start();
        //Butoons working setting
        //play
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        //previous
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();//stop the song first
                if (position != 0){
                    position=position-1;//for previous song
                }
                else{
                    position=songs.size() - 1;//if 1st song playing and click on previous the play last one in the list
                }
                //After changing start the song at that postion
                Uri uri= Uri.parse(songs.get(position).toString());//actual location of song we have to Play
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);//Giving url of song which we want to play
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);//set image after the next or previous LOOK WELL
                seekBar.setMax(mediaPlayer.getDuration());//set maximum val of seek bar
                //To change the name in TextView
                textContent=songs.get(position).getName();//.toString();
                textView.setText(textContent);

            }
        });
        //next
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();//stop the song first
                if (position != songs.size() - 1){
                    position=position+1;//for previous song
                }
                else{
                    position=0;//if 1st song playing and click on previous the play last one in the list
                }
                //After changing start the song at that postion
                Uri uri= Uri.parse(songs.get(position).toString());//actual location of song we have to Play
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);//Giving url of song which we want to play
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);//set image after the next or previous LOOK WELL
                seekBar.setMax(mediaPlayer.getDuration());//set maximum val of seek bar
               //To change the name in TextView
                textContent=songs.get(position).getName();//.toString();
                textView.setText(textContent);
            }
        });


    }
}