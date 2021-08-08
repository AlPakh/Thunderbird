package com.example.thunderbird;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Help extends AppCompatActivity {

    LinearLayout _layMenu, _layPopUp;
    LinearLayout _layHead, _layBody, _layBottom;
    LinearLayout _layHeadPU, _layBodyPU, _layBottomPU;
    LinearLayout _layMain, _layWaves, _layPowerups, _layContent;

    TextView _imgHead, _imgWaves, _imgPowerups, _imgContent, _imgBack, _imgBackPU, _imgHeadPU, _txtPopUp;
    TextView _txtAst, _txtClust, _txtRam, _txtObst, _txtSpam;
    TextView _txtContent;

    String infAst, infClust, infRam, infObst, infSpam, infDouble, infSpeedUp, infHealth;
    ImageView _imgPopUp, _imgHealth, _imgSpeedUp, _imgDouble;
    Drawable resAst, resClust, resRam, resObst, resSpam, resDouble, resSpeedUp, resHealth;
    boolean mainOpened = true, popUpShown = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        var:
        {
            _imgHead = (TextView) findViewById(R.id.imgHead);
            _imgWaves = (TextView) findViewById(R.id.imgWaves);
            _imgPowerups = (TextView) findViewById(R.id.imgPowerups);
            _imgContent = (TextView) findViewById(R.id.imgContent);
            _imgBack = (TextView) findViewById(R.id.imgBack);

            _layMenu = (LinearLayout) findViewById(R.id.layMenu);
            _layPopUp = (LinearLayout) findViewById(R.id.layPopUp);

            _layHead = (LinearLayout) findViewById(R.id.layHead);
            _layBody = (LinearLayout) findViewById(R.id.layBody);
            _layBottom = (LinearLayout) findViewById(R.id.layBottom);

            _layHeadPU = (LinearLayout) findViewById(R.id.layHeadPU);
            _layBodyPU = (LinearLayout) findViewById(R.id.layBodyPU);
            _layBottomPU = (LinearLayout) findViewById(R.id.layBottomPU);

            _layMain = (LinearLayout) findViewById(R.id.layMain);
            _layWaves = (LinearLayout) findViewById(R.id.layWaves);
            _layPowerups = (LinearLayout) findViewById(R.id.layPowerups);
            _layContent = (LinearLayout) findViewById(R.id.layContent);

            _layMain.setVisibility(View.VISIBLE);
            _layWaves.setVisibility(View.GONE);
            _layPowerups.setVisibility(View.GONE);
            _layContent.setVisibility(View.GONE);

            _txtAst = (TextView) findViewById(R.id.txtAst);
            _txtClust = (TextView) findViewById(R.id.txtClust);
            _txtRam = (TextView) findViewById(R.id.txtRam);
            _txtObst = (TextView) findViewById(R.id.txtObst);
            _txtSpam = (TextView) findViewById(R.id.txtSpam);

            _txtContent = (TextView) findViewById(R.id.txtContent);

            _imgHeadPU = (TextView)findViewById(R.id.imgHeadPU);
            _txtPopUp = (TextView)findViewById(R.id.txtPopUp);
            _imgBackPU = (TextView)findViewById(R.id.imgBackPU);

            _imgPopUp = (ImageView)findViewById(R.id.imgPopUp);

            _imgHealth = (ImageView)findViewById(R.id.imgHealth);
            _imgSpeedUp = (ImageView)findViewById(R.id.imgSpeedUp);
            _imgDouble = (ImageView)findViewById(R.id.imgDouble);
        }

        strings:
        {
            infAst = getResources().getString(R.string.infAst);
            infClust = getResources().getString(R.string.infClust);
            infRam = getResources().getString(R.string.infRam);
            infObst = getResources().getString(R.string.infObst);
            infSpam = getResources().getString(R.string.infSpam);
            infDouble = getResources().getString(R.string.infDouble);
            infSpeedUp = getResources().getString(R.string.infSpeedUp);
            infHealth = getResources().getString(R.string.infHealth);
        }

        visibility:
        {
            _layPopUp.setVisibility(View.GONE);
            _layMenu.setVisibility(View.VISIBLE);

            _layMain.setVisibility(View.VISIBLE);

            _layPowerups.setVisibility(View.GONE);
            _layContent.setVisibility(View.GONE);
            _layWaves.setVisibility(View.GONE);
        }

        resourses:
        {
            resAst = getResources().getDrawable(R.drawable.info_ast);
            resClust = getResources().getDrawable(R.drawable.info_clust);
            resRam = getResources().getDrawable(R.drawable.info_ram);
            resObst = getResources().getDrawable(R.drawable.info_obst);
            resSpam = getResources().getDrawable(R.drawable.info_spam);

            resDouble = getResources().getDrawable(R.drawable.info_double);
            resSpeedUp = getResources().getDrawable(R.drawable.info_speed_up);
            resHealth = getResources().getDrawable(R.drawable.info_health);
        }

        main_menu_buttons:
        {
            _imgPowerups.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgPowerups.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _imgPowerups.setTextColor(getResources().getColor(R.color.c2));
                            _layMain.setVisibility(View.GONE);
                            mainOpened = false;
                            _imgBack.setText("← BACK");
                            _layPowerups.setVisibility(View.VISIBLE);


                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgWaves.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgWaves.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _imgWaves.setTextColor(getResources().getColor(R.color.c2));
                            _layMain.setVisibility(View.GONE);
                            mainOpened = false;
                            _imgBack.setText("← BACK");
                            _layWaves.setVisibility(View.VISIBLE);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgContent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgContent.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _imgContent.setTextColor(getResources().getColor(R.color.c2));
                            _layMain.setVisibility(View.GONE);
                            mainOpened = false;
                            _imgBack.setText("← BACK");
                            _layContent.setVisibility(View.VISIBLE);

                            _txtContent.setText(getResources().getString(R.string.txt_content));
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }

        waves_list:
        {
            _txtAst.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _txtAst.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _txtAst.setTextColor(getResources().getColor(R.color.c2));
                            showPopUpWindow("Asteroids", resAst, infAst);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
            _txtClust.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _txtClust.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _txtClust.setTextColor(getResources().getColor(R.color.c2));
                            showPopUpWindow("Cluster", resClust, infClust);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
            _txtRam.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _txtRam.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _txtRam.setTextColor(getResources().getColor(R.color.c2));
                            showPopUpWindow("Ram", resRam, infRam);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
            _txtObst.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _txtObst.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _txtObst.setTextColor(getResources().getColor(R.color.c2));
                            showPopUpWindow("Obstacles", resObst, infObst);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
            _txtSpam.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _txtSpam.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            _txtSpam.setTextColor(getResources().getColor(R.color.c2));
                            showPopUpWindow("Spammer", resSpam, infSpam);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }


        powerup_list:
        {
            _imgHealth.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            break;


                        case MotionEvent.ACTION_UP:

                            showPopUpWindow("Health", resHealth, infHealth);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgSpeedUp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            break;


                        case MotionEvent.ACTION_UP:

                            showPopUpWindow("Speed Up", resSpeedUp, infSpeedUp);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
            _imgDouble.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            break;


                        case MotionEvent.ACTION_UP:

                            showPopUpWindow("Double", resDouble, infDouble);

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }

        return_buttons:
        {
            _imgBack.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgBack.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            if (mainOpened) {
                                finish();
                            } else {
                                _layWaves.setVisibility(View.GONE);
                                _layPowerups.setVisibility(View.GONE);
                                _layContent.setVisibility(View.GONE);

                                _layMain.setVisibility(View.VISIBLE);
                                _imgBack.setText("← MENU");
                                mainOpened = true;
                            }
                            _imgBack.setTextColor(getResources().getColor(R.color.c2));
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

            _imgBackPU.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            _imgBackPU.setTextColor(getResources().getColor(R.color.c1));
                            break;


                        case MotionEvent.ACTION_UP:
                            if (popUpShown) {
                                _layPopUp.setVisibility(View.GONE);
                                _layMenu.setVisibility(View.VISIBLE);
                                popUpShown = false;
                            }
                            _imgBackPU.setTextColor(getResources().getColor(R.color.c2));
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
    }

    public void showPopUpWindow(String name, Drawable res, String info)
    {
        popUpShown = true;
        _layPopUp.setVisibility(View.VISIBLE);
        _layMenu.setVisibility(View.GONE);

        _imgPopUp.setImageDrawable(res);
        _imgHeadPU.setText(name);
        _txtPopUp.setText(info);
    }
}
