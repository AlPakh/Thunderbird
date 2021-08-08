package com.example.thunderbird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    TextView _imgStart, _imgExit, _imgHelp;
    ImageView _imgSound, _imgMusic;

    Intent _intent;

    boolean musicOn = true, soundOn = true;
    int mus = 1, snd = 1;
    String boolVariables;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        _imgStart = (TextView)findViewById(R.id.imgStart);
        _imgHelp = (TextView)findViewById(R.id.imgHelp);
        _imgExit = (TextView)findViewById(R.id.imgExit);

        _imgSound = (ImageView)findViewById(R.id.imgSound);
        _imgMusic = (ImageView)findViewById(R.id.imgMusic);

        menu_buttons:
        {
            _imgStart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgStart.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _imgStart.setTextColor(getResources().getColor(R.color.c2));
                            _intent = new Intent(getApplication(), Main.class);
                            _intent.putExtra("sound", snd);
                            _intent.putExtra("music", mus);
                            startActivity(_intent);
                            finish();
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgExit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgExit.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _imgExit.setTextColor(getResources().getColor(R.color.c2));
                            finish();
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgHelp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgHelp.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _imgHelp.setTextColor(getResources().getColor(R.color.c2));
                            _intent = new Intent(getApplication(), Help.class);
                            startActivity(_intent);
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }

        music_buttons:
        {
            _imgMusic.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            break;


                        case MotionEvent.ACTION_UP:
                            if (musicOn) {
                                musicOn = false;
                                mus = 0;
                                //myToast.toastStatic(getApplication(), String.valueOf(mus));
                                _imgMusic.setBackgroundResource(R.drawable.music_off);
                            } else if (!musicOn) {
                                musicOn = true;
                                mus = 1;
                                //myToast.toastStatic(getApplication(), String.valueOf(mus));
                                _imgMusic.setBackgroundResource(R.drawable.music_on);
                            }
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgSound.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            break;


                        case MotionEvent.ACTION_UP:
                            if (soundOn) {
                                soundOn = false;
                                snd = 0;
                                //myToast.toastStatic(getApplication(), String.valueOf(snd));
                                _imgSound.setBackgroundResource(R.drawable.sound_off);
                            } else if (!soundOn) {
                                soundOn = true;
                                snd = 1;
                                //myToast.toastStatic(getApplication(), String.valueOf(snd));
                                _imgSound.setBackgroundResource(R.drawable.sound_on);
                            }
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
    }

}
