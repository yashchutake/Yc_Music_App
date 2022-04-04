package com.example.ycmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

///method 1
//        Thread thread=new Thread(){
//            //when we want to do more things at time
//         public void run(){
//             try{
//                sleep(68);//3sec
//             }
//             catch(Exception e){
//                 e.printStackTrace();//print any exception
//             }
//             finally{
//                 Intent intent=new Intent(SplashActivity.this,MainActivity.class);//for go from one activity to other
//                startActivity(intent);
//                finish();
//             }
//         }
//
//
//        };
//        thread.start();//start the thread
//    }
//}
        //Method 2
        //Parrel Threading //asysrnous Way
//
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);//for go from one activity to other
                startActivity(intent);
                finish();
            }
           
        },928);


    }
}