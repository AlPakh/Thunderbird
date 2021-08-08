package com.example.thunderbird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends AppCompatActivity {

        ImageView myImg, _imgColl, _imgVictim, _imgPass; // Переменные, учавствующая в добавлении на экран изображений
        ImageView imgHealthUpd, imgHealth, imgRam, imgCluster, imgAsteroid, imgProjectile, imgPup, imgStar, imgSpammer, imgSpammerShoot; // Переменные, нужные для генерации изображений

        String s;
        String[] errorStr = {"You're hearing weird noise", "Sheating is shaking", "\"" + "What is that red button?"+ "\""};

        Intent _intent;

        Button _btnResume, _btnExit, _btnMusic, _btnSound;

        LinearLayout _layPause;

        TextView _lblScore;

        TextView _lblMessage; // Текстовое поле, предназначенное для вывода сообщений на экран

        ImageView _imgShip, _imgCount;

        ConstraintLayout _clay;

        ConstraintLayout.LayoutParams _layParam, _lp;

        boolean playerDead = false, gamePaused = false, doubleFire = false, soundOn, musicOn, gameAtFirtStart = false, messageShown;
        boolean wavAst = false, wavClust = false, wavTaran = false, wavObst = false, wavSpammer = false, spammerExists = false, spammerShooting = false;

        int score, countDown = -1, health = 2, spammerShots, projKol, projAm, taranAngle = 14, taranRotation = 15;

        int playerShootPeriod = 700; // Переменная, определяющая период таймера _tmPlayerShoot

        int starTypeInt; //Переменная для определения типа звёзд: на переднем и занем плане

        int pUpTypeInt; //Переменная для определения типа усилителя

        int projectileSpeed = 25, objectSpeed = 9, backgroundSpeed = 6, foregroundSpeed = 7; // переменные, отвечаюющие за скорость передвижения объектов по экрану
        int  spammerSpeed = 9; // Является отдельной переменной, т.к. может иметь как положительное, так отрицательое значение

        float x = 400, y = 500, x1 = 200, y1 = 400, dX, dY, evRX, evRY;

        Random rnd = new Random();

        SoundPool _snd;
        int streamId, sndShoot2Id = 1, sndDamageId = 2, sndCollectId = 3, sndCountdownId = 4, sndStartId = 5, sndEnemyDeathId = 6, sndImpactId = 7;

        MediaPlayer _playerBackground;

        int mus = 0, snd = 0;

        Timer _tmStars, _tmObjPass, _tmPowerUp, _tmPlayerShoot, _tmCollisionCheck, _tmStart, _tmDead;

        TaskStarsAdd _taskSA; // Task, наследованный от TimerTask, предназначенный для добавления звёзд
        TaskObjPass _taskSP; // Task, наследованный от TimerTask, предназначенный для вертикального смещения звёзд
        TaskPwUpAdd _taskPU; // Task, наследованный от TimerTask, предназначенный для появления PowerUp-а раз в минуту
        TaskPlayerShoot _taskPlayerShoot; // Task, наследованный от TimerTask, предназначенный для создания снарядов
        TaskCollisionCheck _taskCollisionCheck; // Task, наследованный от TimerTask, предназначенный для проверки на столкновения
        TaskGameStart _taskStart; // Task, наследованный от TimerTask, предназначенный для обратного отсчёта при запуске формы
        TaskPlayerDead _taskDead;  // Task, наследованный от TimerTask, предназначенный для остановки таймеров и завершения игры

        Timer _tmEnemies, _tmAsteroids, _tmCluster, _tmTaran, _tmTaranCheck, _tmSpammer, _tmSpammerShooting;

        TaskEnemies _taskEnemies;
        TaskAsteroids _taskAsteroids;
        TaskCluster _taskCluster;
        TaskTaran _taskTaran;
        TaskTaranCheck _taskTaranCheck;
        TaskSpammer _taskSpammer;
        TaskSpammerShooting _taskSpammerShooting;

        LayoutInflater _inf;

        int[] countArr = {R.drawable.img_three, R.drawable.img_two, R.drawable.img_one}; // Обратный отсчёт
        int[] healthArr = {R.drawable.hp_empty, R.drawable.hp_one, R.drawable.hp_two, R.drawable.hp_three}; // Индикатор здоровья


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objects:
//Привязка переменных к объектам
        {
            _clay = (ConstraintLayout) findViewById(R.id.clay);
            _imgShip = (ImageView) findViewById(R.id.imgShip);
            _imgCount = (ImageView) findViewById(R.id.imgCount);
            _lblScore = (TextView) findViewById(R.id.txtScore);
            _lblMessage = (TextView) findViewById(R.id.lblMessage);
            _btnResume = (Button) findViewById(R.id.btnResume);
            _btnSound = (Button) findViewById(R.id.btnSound);
            _btnMusic = (Button) findViewById(R.id.btnMusic);
            _btnExit = (Button) findViewById(R.id.btnExit);
            _layPause = (LinearLayout) findViewById(R.id.layPause);
        }


        music:
//Загрузка музыки в MediaPlayer
        {
            _playerBackground = MediaPlayer.create(this, R.raw.music_background);
        }

        _snd = new SoundPool(15, AudioManager.STREAM_MUSIC, 100);

        soundpool_loading_sounds:
        //Загрузка в SoundPool всех звуков
        {
            _snd.load(this, R.raw.snd_laser2, 1);
            _snd.load(this, R.raw.snd_damage, 1);
            _snd.load(this, R.raw.snd_collect, 1);
            _snd.load(this, R.raw.snd_countdown_a, 1);
            _snd.load(this, R.raw.snd_countdown_b, 1);
            _snd.load(this, R.raw.snd_enemy_death, 1);
            _snd.load(this, R.raw.snd_impact, 1);
        }

        for (int i = 0; i < 7; i++) {
            soundPlay(i);
        }

        _imgShip.setVisibility(View.INVISIBLE);
        _lblScore.setVisibility(View.INVISIBLE);
        _layPause.setVisibility(View.INVISIBLE);

        startGame(); // Метод, отвечающий за запуск игры

        _lblScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGamePaused();
                _layPause.setVisibility(View.VISIBLE);
                _playerBackground.stop();
            }
        });

        _lblMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_lblMessage.getText() == "GAME OVER") //На сообщение можно будет нажать только если здоровье на нуле
                {
                    _intent = new Intent(getApplication(), Main.class);

                    if (soundOn) {
                        snd = 1;
                    } else if (!soundOn) {
                        snd = 0;
                    }

                    if (musicOn) {
                        mus = 1;
                    } else if (!musicOn) {
                        mus = 0;
                    }

                    _intent.putExtra("sound", snd);
                    _intent.putExtra("music", mus);

                    startActivity(_intent);
                    _clay.removeAllViews();
                    finish();
                }
                _lblMessage.setVisibility(View.INVISIBLE);
            }
        });

        pause_menu_buttons:
        {
            _btnResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame();
                    soundPlay(sndCountdownId);
                    if (musicOn) {
                        _playerBackground.start();
                    } else {
                        _playerBackground.stop();
                    }
                }
            });

            _btnSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (soundOn) {
                        soundOn = false;
                        //myToast.toastStatic(getApplication(), "sound_stopped");
                        //_intent.removeExtra("sound");
                        //_intent.putExtra("sound", "0");
                        _btnSound.setTextColor(getResources().getColor(R.color.txtDisColor));
                    } else if (!soundOn) {
                        soundOn = true;
                        //myToast.toastStatic(getApplication(), "sound_starts");
                        //_intent.removeExtra("sound");
                        //_intent.putExtra("sound", "1");
                        _btnSound.setTextColor(getResources().getColor(R.color.txtEnColor));
                    }
                    soundPlay(sndCountdownId);
                }
            });

            _btnMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (musicOn) {
                        musicOn = false;
                        //_intent.removeExtra("music");
                        // _intent.putExtra("music", "0");
                        if (_playerBackground.isPlaying()) _playerBackground.pause();
                        //myToast.toastStatic(getApplication(), "music_stopped");
                        _btnMusic.setTextColor(getResources().getColor(R.color.txtDisColor));
                    } else if (!musicOn) {
                        musicOn = true;
                        //_intent.removeExtra("music");
                        //_intent.putExtra("music", "1");
                        //myToast.toastStatic(getApplication(), "music_starts");
                        _btnMusic.setTextColor(getResources().getColor(R.color.txtEnColor));
                    }
                    soundPlay(sndCountdownId);
                }
            });

            _btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soundPlay(sndCountdownId);
                    _intent = new Intent(getApplication(), Menu.class);
                    startActivity(_intent);
                    finish();
                }
            });
        }

        if (!soundOn) _btnSound.setTextColor(getResources().getColor(R.color.txtDisColor));
        if (!musicOn) _btnMusic.setTextColor(getResources().getColor(R.color.txtDisColor));

        Setting_ships_position:
        {
            String xC = String.valueOf(_clay.getWidth());
            String yC = String.valueOf(_clay.getHeight());

            String xS = String.valueOf(_imgShip.getWidth());
            String yS = String.valueOf(_imgShip.getHeight());

            _imgShip.animate() // Разместить корабль по центру
                    .x((Float.parseFloat(xC) - Float.parseFloat(xS)) / 2)
                    .y((Float.parseFloat(yC) * 2 / 3 - Float.parseFloat(yS) / 2))
                    .setDuration(1);
        }

    }

    @Override
    protected void onPause() // Останавливает звук при выходе из приложения
    {
        if (_playerBackground.isPlaying()) {
            _playerBackground.pause();
            musicOn = false;
            soundOn = false;

            _btnMusic.setTextColor(getResources().getColor(R.color.txtDisColor));
            _btnSound.setTextColor(getResources().getColor(R.color.txtDisColor));

            if(_tmCollisionCheck!=null)_tmCollisionCheck.cancel();

            setGamePaused();
        }
        super.onPause();
    }

    @Override
    protected void onResume() // Запускает звук при запуске приложения из трея
    {
        super.onResume();
        if(musicOn && gameAtFirtStart)
        {
            _playerBackground.start();
            _btnMusic.setTextColor(getResources().getColor(R.color.txtEnColor));
        }
    }






    //TimerTasks - different =======================================
    class TaskGameStart extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDown++;
                    gameAtFirtStart = false;
                    if(countDown >=3)
                    {                             //Обратный отсчёт в начае игры
                        _imgCount.setVisibility(View.INVISIBLE);
                        _lblScore.setVisibility(View.VISIBLE);

                        settingTimers:
                        {
                            if (_tmStars != null)
                                _tmStars.cancel();                      //Добавление звёзд на экран
                            _tmStars = new Timer();
                            _taskSA = new TaskStarsAdd();
                            _tmStars.schedule(_taskSA, 300, 180);

                            if (_tmObjPass != null)
                                _tmObjPass.cancel();                  //Перемещение объектов
                            _tmObjPass = new Timer();
                            _taskSP = new TaskObjPass();
                            _tmObjPass.schedule(_taskSP, 300, 25);

                            if (_tmPowerUp != null)
                                _tmPowerUp.cancel();                //Добавление усилителей
                            _tmPowerUp = new Timer();
                            _taskPU = new TaskPwUpAdd();
                            _tmPowerUp.schedule(_taskPU, 15000, 15000);

                            if (_tmPlayerShoot != null) _tmPlayerShoot.cancel();          //Стрельба
                            _tmPlayerShoot = new Timer();
                            _taskPlayerShoot = new TaskPlayerShoot();
                            _tmPlayerShoot.schedule(_taskPlayerShoot, playerShootPeriod, playerShootPeriod);

                            if (_tmCollisionCheck != null)
                                _tmCollisionCheck.cancel();    //Проверка на столкновения
                            _tmCollisionCheck = new Timer();
                            _taskCollisionCheck = new TaskCollisionCheck();
                            _tmCollisionCheck.schedule(_taskCollisionCheck, 300, 25);

                            if (_tmEnemies != null) _tmEnemies.cancel();              //Волны врагов
                            _tmEnemies = new Timer();
                            _taskEnemies = new TaskEnemies();
                            _tmEnemies.schedule(_taskEnemies, 4000, 5000);
                        }

                        ship_move(_imgShip); //Метод, отвечающий за перетаскивание корабля

                        healthAdd(); //Добавление полоски здоровья

                        _lblScore.setVisibility(View.VISIBLE);

                        soundPlay(sndStartId); //Высокий звук, означающий начало игры

                        if(musicOn)  // Проверка, должна ли играть музыка
                        {
                            _playerBackground.start();
                        }

                        countDown = -1; // Переменная, отвечающая за обратный отсчёт обнуляется

                        String xC = String.valueOf(_clay.getWidth());
                        String yC = String.valueOf(_clay.getHeight());

                        String xS = String.valueOf(_imgShip.getWidth());
                        String yS = String.valueOf(_imgShip.getHeight());

                        if(_clay.getHeight()>=900){
                            projectileSpeed = 26;
                            objectSpeed = 11;
                            backgroundSpeed = 8;
                            foregroundSpeed = 9;
                            taranRotation = 19;
                            taranAngle = 16;
                        }
                        else if(_clay.getHeight()<900){
                            spammerSpeed = 13;
                            taranRotation = 15;
                            taranAngle = 14;
                            projectileSpeed = 24;
                            objectSpeed = 7;
                            backgroundSpeed  = 4;
                            foregroundSpeed = 5;
                        }

                        if (!gameAtFirtStart)
                        {
                            _imgShip.animate() // Разместить корабль по центру
                                    .x((Float.parseFloat(xC)-Float.parseFloat(xS))/2)
                                    .y((Float.parseFloat(yC)*2/3-Float.parseFloat(yS)/2))
                                    .setDuration(1);
                            _imgShip.setVisibility(View.VISIBLE);
                            _imgShip.setTag("imgShip");
                            gameAtFirtStart = true;
                        }

                        showMessage(xC + " " + yC);

                        if(_tmStart !=null) _tmStart.cancel();
                    }
                    else
                    {
                        _imgCount.setVisibility(View.VISIBLE); //Вывод цифр обратного отсчёта
                        _imgCount.setImageResource(countArr[countDown]);
                        soundPlay(sndCountdownId);
                    }

                }
            });
        }
    }

    class TaskStarsAdd extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _inf = LayoutInflater.from(Main.this);
                    imgStar = (ImageView)_inf.inflate(R.layout.lay_star, null);

                    starTypeInt = rnd.nextInt(2); // Появляется два типа звёзд: на дальнем плане и на переднем

                    imgStar.setImageResource(R.drawable.star);

                    int a = _clay.getWidth();
                    int b = _clay.getHeight();

                    _lp = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    if(starTypeInt == 0) // Для звёзд на дальнем плане
                    {
                        imgStar.setTag("imgStarFar");
                        _lp.width = a/130;
                        _lp.height = b/130;
                    }
                    if(starTypeInt == 1) // Для звёзд на переднем плане
                    {
                        imgStar.setTag("imgStarClose");
                        _lp.width = a/90;
                        _lp.height = b/90;
                    }
                    _layParam = (ConstraintLayout.LayoutParams)imgStar.getLayoutParams();

                    imgStar.setLayoutParams(_lp);

                    imgStar.setX(rnd.nextInt(_clay.getWidth() - imgStar.getWidth()*2) + imgStar.getWidth());
                    imgStar.setY(0 - imgStar.getHeight());

                    _clay.addView(imgStar);
                }
            });
        }
    }

    class TaskPwUpAdd extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _inf = LayoutInflater.from(Main.this);
                    imgPup = (ImageView)_inf.inflate(R.layout.lay_powerup, null);

                    if(doubleFire || score < 15000) pUpTypeInt = rnd.nextInt(2);
                    if(!doubleFire && score >= 15000) pUpTypeInt = rnd.nextInt(3);

                    if(pUpTypeInt == 0) {imgPup.setTag("imgPUp_speed"); imgPup.setImageResource(R.drawable.pw_up_speed);}
                    if(pUpTypeInt == 1) {imgPup.setTag("imgPUp_health"); imgPup.setImageResource(R.drawable.pw_up_life);}
                    if(pUpTypeInt == 2) {imgPup.setTag("imgPUp_double"); imgPup.setImageResource(R.drawable.pw_up_double);}

                    int a = _clay.getWidth();
                    int b = _clay.getHeight();

                    _lp = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    _lp.width = a/10;
                    _lp.height = b/10;
                    _layParam = (ConstraintLayout.LayoutParams)imgPup.getLayoutParams();
                    imgPup.setLayoutParams(_lp);

                    imgPup.setX(rnd.nextInt(_clay.getWidth() - imgPup.getWidth()*2) + imgPup.getWidth());
                    imgPup.setY(0 - _lp.height);

                    _clay.addView(imgPup);
                }
            });
        }
    }

    class TaskPlayerShoot extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float[] projectileXArr = {_imgShip.getX() + _imgShip.getWidth()/2 - _clay.getWidth()/80,
                            _imgShip.getX() + _imgShip.getWidth()*5/16 - _clay.getWidth()/80,
                            _imgShip.getX() + _imgShip.getWidth()*11/16 - _clay.getWidth()/80,
                            _imgShip.getX() + _imgShip.getWidth()*5/16 - _clay.getWidth()/80};

                    if(!doubleFire)
                    { // Без усилителя
                        projKol = 1;
                        projAm = 0;
                    }
                    else
                    { // При наличии усилителя идёт стрельба двумя знарядами за выстрел

                        projKol = 3;
                        projAm = 1;
                    }

                    for(int i = projAm; i < projKol; i++)
                    {
                        _inf = LayoutInflater.from(Main.this);
                        imgProjectile = (ImageView)_inf.inflate(R.layout.lay_projectile, null);

                        int a = _clay.getWidth();
                        int b = _clay.getHeight();

                        _lp = new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT);

                        _lp.width = a/40;
                        _lp.height = b/20;
                        _layParam = (ConstraintLayout.LayoutParams)imgProjectile.getLayoutParams();
                        imgProjectile.setLayoutParams(_lp);

                        imgProjectile.setImageResource(R.drawable.projectile);

                        imgProjectile.setX(projectileXArr[i]);
                        imgProjectile.setY(_imgShip.getY()- _lp.height);
                        imgProjectile.setTag("imgProjectile");
                        if(imgProjectile.getY()>imgProjectile.getHeight())
                        {
                            _clay.addView(imgProjectile);
                            soundPlay(sndShoot2Id);
                        }
                    }
                }
            });
        }
    }

    class TaskObjPass extends TimerTask{
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < _clay.getChildCount(); i++) {
                            Movement:
                            {
                                if ((_clay.getChildAt(i).getTag() != "imgShip"
                                        && _clay.getChildAt(i).getTag() != "imgProjectile"
                                        && _clay.getChildAt(i).getY() > _clay.getHeight())          || _clay.getChildAt(i).getTag() == "yiy")
                                {
                                    _clay.removeViewAt(i);
                                    break;
                                }//Убирает изображение, вышедшее за пределы экрана


                                if ((_clay.getChildAt(i).getX() > _clay.getWidth() - _clay.getChildAt(i).getWidth())
                                        && _clay.getChildAt(i).getTag() != "imgShip"
                                        && _clay.getChildAt(i).getTag() != "imgTaranSet"
                                        && _clay.getChildAt(i).getTag() != "imgProjectile"
                                        && _clay.getChildAt(i).getTag() != "imgSpammerInv"
                                        && _clay.getChildAt(i).getTag() != "imgSpammerVu") {
                                    _clay.getChildAt(i).setX(rnd.nextInt(_clay.getWidth() - _clay.getChildAt(i).getWidth() * 2) + _clay.getChildAt(i).getWidth());
                                } //Если изображение не уместилось в правую границу, ему добавляется ллучайная Х координата

                                if (_clay.getChildAt(i).getTag() == "imgEnemyProjectile" && _clay.getChildAt(i) != null) {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += projectileSpeed;
                                    _imgPass.setY(y);
                                }

                                if (_clay.getChildAt(i).getTag() == "imgStarClose") // Проверка по тегу
                                {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += foregroundSpeed;
                                    _imgPass.setY(y);
                                }
                                if (_clay.getChildAt(i).getTag() == "imgStarFar") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += backgroundSpeed;
                                    _imgPass.setY(y);
                                }
                                if (_clay.getChildAt(i).getTag() == "imgPUp_double"
                                        || _clay.getChildAt(i).getTag() == "imgPUp_health"
                                        || _clay.getChildAt(i).getTag() == "imgPUp_speed") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += objectSpeed;
                                    _imgPass.setY(y);
                                }
                                if (_clay.getChildAt(i).getTag() == "imgProjectile") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y -= projectileSpeed;
                                    _imgPass.setY(y);

                                    if ((y < -5 - _imgPass.getHeight())) { //Убирает изображение, вышедшее за пределы экрана
                                        _clay.removeViewAt(i);
                                    }
                                }

                                if (_clay.getChildAt(i).getTag() == "imgAsteroid") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += objectSpeed + 4;

                                    _imgPass.setRotation(_imgPass.getRotation() + 6);

                                    _imgPass.setY(y);
                                }

                                if (_clay.getChildAt(i).getTag() == "imgCluster"
                                || _clay.getChildAt(i).getTag() == "imgClusterSe"
                               || _clay.getChildAt(i).getTag() == "imgClusterThi") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += objectSpeed + 4;

                                    _imgPass.setRotation(_imgPass.getRotation() + 6);

                                    _imgPass.setY(y);
                                }

                                if (_clay.getChildAt(i).getTag() == "imgTaranReady") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    if (_imgPass.getY() <= _imgPass.getHeight()) {
                                        y = _imgPass.getY();
                                        y += objectSpeed;
                                        _imgPass.setY(y);
                                    }
                                    if (_imgPass.getY() > _imgPass.getHeight()) {
                                        _imgPass.setTag("imgTaranSet");
                                    }
                                }
                                if (_clay.getChildAt(i).getTag() == "imgTaranGo") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);

                                    y = _imgPass.getY();
                                    y += objectSpeed * 4;
                                    _imgPass.setY(y);

                                    if (_imgPass.getY() <= _imgShip.getY() + _imgShip.getHeight()) {
                                        if (_imgPass.getX() + _imgPass.getWidth() / 2 >= _imgShip.getX() && _imgPass.getX() < _imgShip.getX() + _imgShip.getWidth() / 2) {
                                            _imgPass.setRotation(0);
                                        } else if (_imgPass.getX() < _imgShip.getX()) {
                                            _imgPass.setX(_imgPass.getX() + taranAngle);
                                            _imgPass.setRotation(360 - taranRotation);
                                        } else if (_imgPass.getX() > _imgShip.getX()) {
                                            _imgPass.setX(_imgPass.getX() - taranAngle);
                                            _imgPass.setRotation(taranRotation);
                                        }
                                    } else {
                                        float rot = _imgPass.getRotation();
                                        _imgPass.setRotation(13 + rot);

                                        if (_imgPass.isClickable()) {
                                            score += 1000;
                                            _imgPass.setClickable(false);
                                            soundPlay(sndEnemyDeathId);
                                        }

                                    }
                                }
                                if (_clay.getChildAt(i).getTag() == "imgSpammerHorizontal") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);
                                    if (_imgPass.getY() <= _imgPass.getHeight()) {
                                        y = _imgPass.getY();
                                        y += objectSpeed;
                                        _imgPass.setY(y);
                                    }
                                    if (_imgPass.getY() > _imgPass.getHeight()) {
                                        _imgPass.setTag("imgSpammerInv");
                                        spammerShooting = true;
                                    }
                                }
                                if (_clay.getChildAt(i).getTag() == "imgSpammerInv" || _clay.getChildAt(i).getTag() == "imgSpammerVu") {
                                    _imgPass = (ImageView) _clay.getChildAt(i);
                                    float xSp = _imgPass.getX();

                                    if (xSp <= 0 || xSp >= _clay.getWidth() - _imgPass.getWidth()) {
                                        spammerSpeed = spammerSpeed * (-1);
                                    }

                                    if (_clay.getChildAt(i).getTag() == "imgSpammerInv" && spammerShots > 15) {
                                        _imgPass.setTag("imgSpammerVu");
                                        _imgPass.setImageResource(R.drawable.e_double_vu);
                                    }

                                    xSp += spammerSpeed;
                                    _imgPass.setX(xSp);
                                }
                            }
                        }
                    }
                    catch(NullPointerException e){
                       myToast.toastStatic(getApplication(), "Space is unstable ");
                    }
                }
            });
        }
    }

    class TaskCollisionCheck extends TimerTask{
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < _clay.getChildCount(); i++) {
                            Collision:
                            {
                                float shipX = _imgShip.getX();
                                float shipY = _imgShip.getY();

                                float shipWidth = _imgShip.getWidth();
                                float shipHeight = _imgShip.getHeight();

                                float objX = _clay.getChildAt(i).getX();
                                float objY = _clay.getChildAt(i).getY();

                                float objWidth = _clay.getChildAt(i).getWidth();
                                float objHeight = _clay.getChildAt(i).getHeight();



                                if (_clay.getChildAt(i).getTag() == "imgProjectile") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);

                                    for (int j = 0; j < _clay.getChildCount(); j++) {

                                        if (_clay.getChildAt(j).getTag() == "imgAsteroid"
                                                && (_clay.getChildAt(j).getY() + _clay.getChildAt(j).getHeight()) > 0) //Проверка на столкновение с астероидом
                                        {
                                            _imgVictim = (ImageView) _clay.getChildAt(j);

                                            //Параметры объекта, по которому попадает снаряд
                                            float victimX = _imgVictim.getX();
                                            float victimY = _imgVictim.getY();

                                            float victimWidth = _imgVictim.getWidth();
                                            float victimHeight = _imgVictim.getHeight();

                                            if (objX < (victimX + victimWidth)
                                                    && (objX + objWidth) > victimX
                                                    && objY < (victimY + victimHeight)
                                                    && (objY + objHeight) > victimY) {
                                                _imgVictim.setVisibility(View.INVISIBLE);
                                                _imgVictim.setY(_clay.getHeight() + _imgVictim.getHeight() + _imgVictim.getHeight());

                                                _imgColl.setVisibility(View.INVISIBLE);
                                                _imgColl.setY(0 - _imgColl.getHeight()*4);
                                                score += 500;
                                                soundPlay(sndEnemyDeathId);
                                            }

                                        }
                                        if (_clay.getChildAt(j).getTag() == "imgCluster"
                                            && (_clay.getChildAt(j).getY() + _clay.getChildAt(j).getHeight()) > 0) {
                                            _imgVictim = (ImageView) _clay.getChildAt(j);

                                            float victimX = _imgVictim.getX();
                                            float victimY = _imgVictim.getY();

                                            float victimWidth = _imgVictim.getWidth();
                                            float victimHeight = _imgVictim.getHeight();

                                            if (objX < (victimX + victimWidth) && (objX + objWidth) > victimX && objY < (victimY + victimHeight) && (objY + objHeight) > victimY && _clay.getChildAt(i).getTag() == "imgProjectile") {
                                                _imgColl.setY(0 - _imgColl.getHeight()*4);
                                                _imgColl.setTag("yiy");
                                                _imgColl.setVisibility(View.INVISIBLE);

                                                _clay.getChildAt(j).setTag("imgClusterSe");
                                                soundPlay(sndImpactId);
                                            }
                                        }
                                        if (_clay.getChildAt(j).getTag() == "imgClusterSe"
                                            && (_clay.getChildAt(j).getY() + _clay.getChildAt(j).getHeight()) > 0) {
                                            _imgVictim = (ImageView) _clay.getChildAt(j);

                                            float victimX = _imgVictim.getX();
                                            float victimY = _imgVictim.getY();

                                            float victimWidth = _imgVictim.getWidth();
                                            float victimHeight = _imgVictim.getHeight();

                                            if (objX < (victimX + victimWidth) && (objX + objWidth) > victimX && objY < (victimY + victimHeight) && (objY + objHeight) > victimY && _clay.getChildAt(i).getTag() == "imgProjectile") {
                                                _imgColl.setVisibility(View.INVISIBLE);
                                                _imgColl.setTag("yiy");
                                                _imgColl.setY(0 - _imgColl.getHeight()*4);

                                                _clay.getChildAt(j).setTag("imgClusterThi");
                                                soundPlay(sndImpactId);
                                            }
                                        }
                                        if (_clay.getChildAt(j).getTag() == "imgClusterThi"
                                            && (_clay.getChildAt(j).getY() + _clay.getChildAt(j).getHeight()) > 0) {
                                            _imgVictim = (ImageView) _clay.getChildAt(j);

                                            //Параметры объекта, по которому попадает снаряд
                                            float victimX = _imgVictim.getX();
                                            float victimY = _imgVictim.getY();

                                            float victimWidth = _imgVictim.getWidth();
                                            float victimHeight = _imgVictim.getHeight();

                                            if (objX < (victimX + victimWidth)
                                                    && (objX + objWidth) > victimX
                                                    && objY < (victimY + victimHeight)
                                                    && (objY + objHeight) > victimY) {
                                                _imgVictim.setVisibility(View.INVISIBLE);
                                                _imgColl.setTag("yiy");
                                                _imgVictim.setY(_clay.getHeight() + _imgShip.getHeight() + _imgShip.getHeight());
                                                _imgColl.setVisibility(View.INVISIBLE);
                                                _imgColl.setY(0 - _imgColl.getHeight()*4);

                                                score += 500;
                                                soundPlay(sndEnemyDeathId);
                                            }

                                        }
                                        if ((_clay.getChildAt(j).getTag() == "imgTaranReady"
                                                || _clay.getChildAt(j).getTag() == "imgTaranSet"
                                                || _clay.getChildAt(j).getTag() == "imgTaranGo"
                                                || _clay.getChildAt(j).getTag() == "imgSpammerInv")
                                                && (_clay.getChildAt(j).getY() + _clay.getChildAt(j).getHeight()) > 0) {
                                            _imgVictim = (ImageView) _clay.getChildAt(j);

                                            //Параметры объекта, по которому попадает снаряд
                                            float victimX = _imgVictim.getX();
                                            float victimY = _imgVictim.getY();

                                            float victimWidth = _imgVictim.getWidth();
                                            float victimHeight = _imgVictim.getHeight();

                                            if (objX < (victimX + victimWidth) && (objX + objWidth) > victimX && objY < (victimY + victimHeight) && (objY + objHeight) > victimY && _clay.getChildAt(i).getTag() == "imgProjectile") {
                                                _imgColl.setVisibility(View.INVISIBLE);
                                                _imgColl.setTag("yiy");
                                                _imgColl.setY(0 - _imgColl.getHeight()*4);

                                                soundPlay(sndImpactId);
                                            }

                                        }
                                        if (_clay.getChildAt(j).getTag() == "imgSpammerVu") {
                                            _imgVictim = (ImageView) _clay.getChildAt(j);

                                            //Параметры объекта, по которому попадает снаряд
                                            float victimX = _imgVictim.getX();
                                            float victimY = _imgVictim.getY();

                                            float victimWidth = _imgVictim.getWidth();
                                            float victimHeight = _imgVictim.getHeight();

                                            if (objX < (victimX + victimWidth) && (objX + objWidth) > victimX && objY < (victimY + victimHeight) && (objY + objHeight) > victimY) {
                                                _imgVictim.setVisibility(View.INVISIBLE);
                                                _imgVictim.setY(_clay.getHeight() + _imgShip.getHeight() + _imgShip.getHeight());
                                                _imgColl.setVisibility(View.INVISIBLE);
                                                _imgColl.setTag("yiy");
                                                _imgColl.setY(0 - _imgColl.getHeight()*4);

                                                score += 5000;
                                                spammerExists = false;
                                                spammerShots = 0;
                                                soundPlay(sndEnemyDeathId);
                                            }

                                        }
                                    }
                                }



                                else if (_clay.getChildAt(i).getTag() == "imgPUp_double") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth) && (shipX + shipWidth) > objX && shipY < (objY + objHeight) && (shipY + shipHeight) > objY) {
                                        _clay.removeViewAt(i);
                                        score += 500;
                                        soundPlay(sndCollectId);
                                        doubleFire = true;
                                    }

                                }
                                else if (_clay.getChildAt(i).getTag() == "imgPUp_health") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth) && (shipX + shipWidth) > objX && shipY < (objY + objHeight) && (shipY + shipHeight) > objY) {
                                        _clay.removeViewAt(i);
                                        score += 200;
                                        soundPlay(sndCollectId);
                                        if (health < 3) {
                                            health++;
                                            updateHealthImg();
                                        }
                                    }

                                }
                                else if (_clay.getChildAt(i).getTag() == "imgPUp_speed") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth) && (shipX + shipWidth) > objX && shipY < (objY + objHeight) && (shipY + shipHeight) > objY) {
                                        _clay.removeViewAt(i);
                                        score += 100;
                                        soundPlay(sndCollectId);

                                        if (!(playerShootPeriod <= 300)) playerShootPeriod -= 100;

                                        if (_tmPlayerShoot != null)
                                            _tmPlayerShoot.cancel();          // Перезапуск таймера с новым периодом
                                        _tmPlayerShoot = new Timer();
                                        _taskPlayerShoot = new TaskPlayerShoot();
                                        _tmPlayerShoot.schedule(_taskPlayerShoot, 0, playerShootPeriod);
                                    }

                                }

                                else if (_clay.getChildAt(i).getTag() == "imgAsteroid") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth)
                                            && (shipX + shipWidth) > objX
                                            && shipY < (objY + objHeight)
                                            && (shipY + shipHeight) > objY) {
                                        _imgColl.setVisibility(View.INVISIBLE);
                                        _imgColl.setY(_clay.getHeight() + _imgColl.getHeight() + _imgColl.getHeight());
                                        if (health > 0) {
                                            lowerHealth();
                                        }
                                    }

                                }
                                else if (_clay.getChildAt(i).getTag() == "imgCluster"
                                || _clay.getChildAt(i).getTag() == "imgClusterSe"
                                || _clay.getChildAt(i).getTag() == "imgClusterThi") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth)
                                            && (shipX + shipWidth) > objX
                                            && shipY < (objY + objHeight)
                                            && (shipY + shipHeight) > objY) {
                                        _imgColl.setVisibility(View.INVISIBLE);
                                        _imgColl.setY(_clay.getHeight() + _imgColl.getHeight() + _imgColl.getHeight());
                                        if (health > 0) {
                                            lowerHealth();
                                        }
                                    }

                                }
                                else if (_clay.getChildAt(i).getTag() == "imgEnemyProjectile") {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth)
                                            && (shipX + shipWidth) > objX
                                            && shipY < (objY + objHeight)
                                            && (shipY + shipHeight) > objY) {
                                        _imgColl.setVisibility(View.INVISIBLE);
                                        _imgColl.setY(_clay.getHeight() + _imgColl.getHeight() + _imgColl.getHeight());
                                        if (health > 0) {
                                            lowerHealth();
                                        }
                                    }

                                }

                                else if (_clay.getChildAt(i).getTag() == "imgTaranReady"
                                        || _clay.getChildAt(i).getTag() == "imgTaranSet"
                                        || _clay.getChildAt(i).getTag() == "imgTaranGo") // Проверка по тегу
                                {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth) && (shipX + shipWidth) > objX && shipY < (objY + objHeight) && (shipY + shipHeight) > objY) {
                                        _imgColl.setVisibility(View.INVISIBLE);
                                        _imgColl.setY(_clay.getHeight() + _imgColl.getHeight() + _imgColl.getHeight());
                                        if (health > 0) {
                                            lowerHealth();
                                        }
                                    }

                                }

                                else if (_clay.getChildAt(i).getTag() == "imgSpammerInv"
                                        || _clay.getChildAt(i).getTag() == "imgSpammerVu") // Проверка по тегу
                                {
                                    _imgColl = (ImageView) _clay.getChildAt(i);
                                    if (shipX < (objX + objWidth) && (shipX + shipWidth) > objX && shipY < (objY + objHeight) && (shipY + shipHeight) > objY) {
                                        _imgColl.setVisibility(View.INVISIBLE);
                                        _imgColl.setY(_clay.getHeight() + _imgColl.getHeight() + _imgColl.getHeight());
                                        spammerShots = 0;
                                        spammerExists = false;
                                        if (health > 0) {
                                            lowerHealth();
                                        }
                                    }

                                }
                            }
                        }
                    }
                    catch(NullPointerException e){
                        int randomStr = rnd.nextInt(3);
                        myToast.toastStatic(getApplication(), errorStr[randomStr]);
                    }
                    finally {
                        if (health <= 0) {
                            setPlayerDead();
                        }
                        if (musicOn && !_playerBackground.isPlaying()) {
                            _playerBackground.start();
                        }

                        _lblScore.setText("Score: " + score);
                    }
                }
            });
        }
    }

    class TaskPlayerDead extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    s = "GAME OVER";

                    _lblMessage.setVisibility(View.VISIBLE);
                    showMessage(s);
                    gameAtFirtStart = false;
                    if(_tmDead !=null) _tmDead.cancel();
                }
            });
        }
    }
    //===============================================================

    //TimerTasks - enemies ≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈
    class TaskEnemies extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(messageShown)
                    {
                        messageShown = false;
                        _lblMessage.setVisibility(View.INVISIBLE);
                    }

                    if(score>50000 && !wavSpammer)
                    {
                            if(_tmAsteroids !=null) _tmAsteroids.cancel();
                            if(_tmCluster !=null) _tmCluster.cancel();                      //Начало волны
                            if(_tmTaran !=null) _tmTaran.cancel();
                            if(_tmSpammer !=null) _tmSpammer.cancel();
                            if(_tmSpammerShooting !=null) _tmSpammerShooting.cancel();
                            if(_tmTaranCheck !=null) _tmTaranCheck.cancel();

                            _tmSpammer = new Timer();
                            _taskSpammer = new TaskSpammer();
                            _tmSpammer.schedule(_taskSpammer, 0, 3000);

                            _tmSpammerShooting = new Timer();
                            _taskSpammerShooting = new TaskSpammerShooting();
                            _tmSpammerShooting.schedule(_taskSpammerShooting, 0, 550);

                            if(_tmTaranCheck !=null) _tmTaranCheck.cancel();              //Проверка тега у тарана
                            _tmTaranCheck = new Timer();
                            _taskTaranCheck = new TaskTaranCheck();
                            _tmTaranCheck.schedule(_taskTaranCheck, 3000, 5000);

                            wavClust = false;
                            wavAst = false;
                            wavTaran = false;
                            wavObst = false;
                            wavSpammer = true;


                            showMessage("Spammer");
                            soundPlay(sndStartId);
                    }
                    else if(score>20000 && score<=50000 && !wavObst)
                    {
                        if(_tmAsteroids !=null) _tmAsteroids.cancel();
                        if(_tmCluster !=null) _tmCluster.cancel();                      //Начало волны
                        if(_tmTaran !=null) _tmTaran.cancel();
                        if(_tmSpammer !=null) _tmSpammer.cancel();
                        if(_tmSpammerShooting !=null) _tmSpammerShooting.cancel();
                        if(_tmTaranCheck !=null) _tmTaranCheck.cancel();

                        _tmTaran = new Timer();
                        _taskTaran = new TaskTaran();
                        _tmTaran.schedule(_taskTaran, 0, 8000);

                        if(_tmTaranCheck !=null) _tmTaranCheck.cancel();              //Проверка тега у тарана
                        _tmTaranCheck = new Timer();
                        _taskTaranCheck = new TaskTaranCheck();
                        _tmTaranCheck.schedule(_taskTaranCheck, 3000, 1200);

                        _tmCluster = new Timer();
                        _taskCluster = new TaskCluster();
                        _tmCluster.schedule(_taskCluster, 0, 1000);

                        _tmAsteroids = new Timer();
                        _taskAsteroids = new TaskAsteroids();
                        _tmAsteroids.schedule(_taskAsteroids, 0, 3000);

                        wavClust = false;
                        wavAst = false;
                        wavTaran = false;
                        wavObst = true;
                        wavSpammer = false;

                        showMessage("Obstacles");
                        soundPlay(sndStartId);
                    }
                    else if(score>12000 && score<=20000 && !wavTaran)
                    {
                        if(_tmAsteroids !=null) _tmAsteroids.cancel();
                        if(_tmCluster !=null) _tmCluster.cancel();                      //Начало волны
                        if(_tmTaran !=null) _tmTaran.cancel();
                        if(_tmSpammer !=null) _tmSpammer.cancel();
                        if(_tmSpammerShooting !=null) _tmSpammerShooting.cancel();
                        if(_tmTaranCheck !=null) _tmTaranCheck.cancel();

                        _tmTaran = new Timer();
                        _taskTaran = new TaskTaran();
                        _tmTaran.schedule(_taskTaran, 0, 8000);

                        if(_tmTaranCheck !=null) _tmTaranCheck.cancel();              //Проверка тега у тарана
                        _tmTaranCheck = new Timer();
                        _taskTaranCheck = new TaskTaranCheck();
                        _tmTaranCheck.schedule(_taskTaranCheck, 3000, 1200);

                        wavClust = false;
                        wavAst = false;
                        wavTaran = true;
                        wavObst = false;
                        wavSpammer = false;

                        showMessage("Ram");
                        soundPlay(sndStartId);
                    }
                    else if(score>5000 && score<=12000 && !wavClust)
                    {
                        if(_tmAsteroids !=null) _tmAsteroids.cancel();                      //Начало волны
                        if(_tmCluster !=null) _tmCluster.cancel();
                        if(_tmTaran !=null) _tmTaran.cancel();
                        if(_tmSpammer !=null) _tmSpammer.cancel();
                        if(_tmSpammerShooting !=null) _tmSpammerShooting.cancel();
                        if(_tmTaranCheck !=null) _tmTaranCheck.cancel();

                        _tmCluster = new Timer();
                        _taskCluster = new TaskCluster();
                        _tmCluster.schedule(_taskCluster, 0, 1000);

                        wavClust = true;
                        wavAst = false;
                        wavTaran = false;
                        wavObst = false;
                        wavSpammer = false;

                        showMessage("Cluster");
                        soundPlay(sndStartId);
                    }
                    else if(score<=5000 && !wavAst)
                    {
                        if(_tmCluster !=null) _tmCluster.cancel();
                        if(_tmAsteroids !=null) _tmAsteroids.cancel();  //Начало волны
                        if(_tmTaran !=null) _tmTaran.cancel();
                        if(_tmSpammer !=null) _tmSpammer.cancel();
                        if(_tmSpammerShooting !=null) _tmSpammerShooting.cancel();
                        if(_tmTaranCheck !=null) _tmTaranCheck.cancel();

                        _tmAsteroids = new Timer();
                        _taskAsteroids = new TaskAsteroids();
                        _tmAsteroids.schedule(_taskAsteroids, 0, 3000);

                        wavAst = true;
                        wavClust = false;
                        wavTaran = false;
                        wavObst = false;
                        wavSpammer = false;

                        showMessage("Asteroids");
                        soundPlay(sndStartId);
                    }
                }
            });
        }
    }

    class TaskAsteroids extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _inf = LayoutInflater.from(Main.this);
                    imgAsteroid = (ImageView)_inf.inflate(R.layout.lay_asteroid, null);

                    _clay.addView(imgAsteroid);

                    imgAsteroid.setImageResource(R.drawable.e_ast);

                    int a = _clay.getWidth();
                    int b = _clay.getHeight();

                    _lp = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    imgAsteroid.setTag("imgAsteroid");
                    _lp.width = a/8;
                    _lp.height = b/8;

                    imgAsteroid.setLayoutParams(_lp);

                    imgAsteroid.setX(rnd.nextInt(a - imgAsteroid.getWidth()*2) + imgAsteroid.getWidth());
                    imgAsteroid.setY(0 - _lp.height - 5);
                }
            });
        }
    }

    class TaskCluster extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _inf = LayoutInflater.from(Main.this);
                    imgCluster = (ImageView)_inf.inflate(R.layout.lay_asteroid, null);

                    _clay.addView(imgCluster);

                    imgCluster.setImageResource(R.drawable.e_ast);

                    int a = _clay.getWidth();
                    int b = _clay.getHeight();

                    _lp = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    imgCluster.setTag("imgCluster");
                    _lp.width = a/6;
                    _lp.height = b/6;

                    _layParam = (ConstraintLayout.LayoutParams)imgCluster.getLayoutParams();

                    imgCluster.setLayoutParams(_lp);

                    imgCluster.setX(rnd.nextInt(a - imgCluster.getWidth()*2) + imgCluster.getWidth());
                    imgCluster.setY(0 - b/6 - 5);
                }
            });
        }
    }

    class TaskTaran extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _inf = LayoutInflater.from(Main.this);
                    imgRam = (ImageView)_inf.inflate(R.layout.lay_ship, null);

                    _clay.addView(imgRam);

                    imgRam.setImageResource(R.drawable.e_taran);

                    int a = _clay.getWidth();
                    int b = _clay.getHeight();

                    _lp = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    imgRam.setTag("imgTaranReady");
                    _lp.width = a/7;
                    _lp.height = b/7;

                    imgRam.setClickable(true);

                    _layParam = (ConstraintLayout.LayoutParams)imgRam.getLayoutParams();

                    imgRam.setLayoutParams(_lp);

                    imgRam.setX(rnd.nextInt(a - imgRam.getWidth()*3) + imgRam.getWidth()*2);
                    imgRam.setY(0 - b/6 - 5);
                }
            });
        }
    }

    class TaskTaranCheck extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i<_clay.getChildCount(); i++){
                        if(_clay.getChildAt(i).getTag()=="imgTaranSet"){
                            _clay.getChildAt(i).setTag("imgTaranGo");
                        }
                    }

                    if(_lblMessage.getVisibility() == View.VISIBLE)
                    {
                        messageShown = true;
                    }

                    if(spammerExists){
                        for (int i = 0; i < _clay.getChildCount(); i++) {
                            if(_clay.getChildAt(i).getTag() == "imgSpammerVu") {
                                ImageView _imgVa = (ImageView) _clay.getChildAt(i);
                                _clay.getChildAt(i).setTag("imgSpammerHorizontal");
                                spammerShots = 0;
                                spammerShooting = true;
                                _imgVa.setImageResource(R.drawable.e_double);
                            }
                        }
                    }
                }
            });
        }
    }

    class TaskSpammer extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!spammerExists) {
                        _inf = LayoutInflater.from(Main.this);
                        imgSpammer = (ImageView) _inf.inflate(R.layout.lay_ship, null);

                        _clay.addView(imgSpammer);
                        spammerExists = true;

                        imgSpammer.setImageResource(R.drawable.e_double);

                        int a = _clay.getWidth();
                        int b = _clay.getHeight();

                        _lp = new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT);

                        imgSpammer.setTag("imgSpammerHorizontal");
                        _lp.width = a / 7;
                        _lp.height = b / 7;

                        imgSpammer.setLayoutParams(_lp);

                        imgSpammer.setX(rnd.nextInt(a - imgSpammer.getWidth() * 3) + imgSpammer.getWidth() * 2);
                        imgSpammer.setY(0 - b / 6 - 5);
                    }
                }
            });
        }
    }

    class TaskSpammerShooting extends TimerTask {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int findSpammer = 0; findSpammer < _clay.getChildCount(); findSpammer++)// Находит стреляющего для присвоения координат
                    {
                        if(_clay.getChildAt(findSpammer).getTag() == "imgSpammerInv")
                        {
                            imgSpammerShoot = (ImageView) _clay.getChildAt(findSpammer);

                                _inf = LayoutInflater.from(Main.this);
                                imgProjectile = (ImageView)_inf.inflate(R.layout.lay_projectile, null);

                                int a = _clay.getWidth();
                                int b = _clay.getHeight();

                                _lp = new ConstraintLayout.LayoutParams(
                                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                        ConstraintLayout.LayoutParams.WRAP_CONTENT);

                                _lp.width = a/40;
                                _lp.height = b/20;
                                _layParam = (ConstraintLayout.LayoutParams)imgProjectile.getLayoutParams();
                                imgProjectile.setLayoutParams(_lp);

                                imgProjectile.setImageResource(R.drawable.en_projectile);

                                imgProjectile.setX(imgSpammerShoot.getX() + imgSpammerShoot.getWidth()/2);
                                imgProjectile.setY(imgSpammerShoot.getY()+ imgSpammerShoot.getHeight());
                                imgProjectile.setTag("imgEnemyProjectile");

                                if(imgProjectile.getY()>imgProjectile.getHeight())
                                {
                                    _clay.addView(imgProjectile);
                                    soundPlay(sndShoot2Id);
                                }
                            spammerShots++;
                            break;
                        }
                    }


                }
            });
        }
    }
    //≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈




    @SuppressLint("ClickableViewAccessibility")
    public void ship_move(final ImageView _img) // Метод, отвечающий за перемещение корабля при перетаскивании
    {
        _img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                evRX = event.getRawX();
                evRY = event.getRawY();

                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX()-evRX;
                        dY = v.getY()-evRY;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        v.animate()
                                .x(evRX + dX)
                                .y(evRY + dY)
                                .setDuration(0)
                                .start();
                        x1 = v.getX();
                        y1 = v.getY();

                        break;

                    case MotionEvent.ACTION_UP:
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setGamePaused() // Использовать для установления игру на паузу и при смерти игрока
    {
        if(_tmStars !=null) _tmStars.cancel(); // Звёзды останавливаются

        if(_tmObjPass !=null) _tmObjPass.cancel(); // Объекты останавливаются

        if(_tmEnemies !=null) _tmEnemies.cancel(); // Таймер, определяющий вид врагов останавливаются

        if(_tmAsteroids !=null) _tmAsteroids.cancel(); // Астероиды останавливаются

        if(_tmCluster !=null) _tmCluster.cancel(); // Поток астероидов останавливаются

        if(_tmTaran !=null) _tmTaran.cancel(); // перемещение таранов останавливаются
        if(_tmTaranCheck !=null) _tmTaranCheck.cancel(); // проверка тегов таранов останавливаются

        if(_tmSpammer !=null) _tmSpammer.cancel(); // Снаряды спаммеров останавливаются

        wavAst = false; wavClust = false;  wavTaran = false; wavObst = false; wavSpammer = false; //ПЕременные, отвечающие за смену волн

        if(_tmPowerUp !=null) _tmPowerUp.cancel(); /// Усилители останавливаются

        if(_tmPlayerShoot !=null) _tmPlayerShoot.cancel(); // Снаряды останавливаются

        if(_tmSpammerShooting !=null) _tmSpammerShooting.cancel(); // Снаряды спаммеров останавливаются

        if(_tmCollisionCheck !=null) _tmCollisionCheck.cancel(); // Проверка на столкновения останавливается

        _imgShip.setOnTouchListener(new View.OnTouchListener() //Корабль не может передвигаться
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        _lblScore.setVisibility(View.INVISIBLE); // Счёт не виден
        _lblMessage.setVisibility(View.INVISIBLE); // Текст сообщений не виден

        for(int i=0; i<_clay.getChildCount(); i++){
            if(_clay.getChildAt(i).getTag() == "imgHealthBar") _clay.removeView(_clay.getChildAt(i));
        }

        _playerBackground.pause();
    }

    public void setPlayerDead() // Метод, запускающий таймер _tmDead
    {
        setGamePaused();

        _playerBackground.stop();

        playerDead = true;

        if(_tmDead !=null) _tmDead.cancel();                      //Вывод сообщения о смерти по таймеру
        _tmDead = new Timer();
        _taskDead = new TaskPlayerDead();
        _tmDead.schedule(_taskDead, 0, 1000);
    }

    public void showMessage(String inputMessage) // Использовать для вывода на экран сообщений о усилениях, смерти и т.д.
    {
        _lblMessage.setVisibility(View.VISIBLE);
        _lblMessage.setText(inputMessage);
        messageShown = true;
    }

    public void healthAdd() // Добавление индикатора здоровья на экран
    {
        _inf = LayoutInflater.from(Main.this);
        imgHealth = (ImageView)_inf.inflate(R.layout.lay_health_bar, null);

        int a = _clay.getWidth();
        int b = _clay.getHeight();

        _lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        _lp.width = a/3;
        _lp.height = b/20;
        _layParam = (ConstraintLayout.LayoutParams)imgHealth.getLayoutParams();
        imgHealth.setLayoutParams(_lp);

        imgHealth.setImageResource(healthArr[health]);

        imgHealth.setX(0);
        imgHealth.setY(b - _lp.height);
        imgHealth.setTag("imgHealthBar");

        imgHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score+=5000;
            }
        });

        _clay.addView(imgHealth);
    }

    public void updateHealthImg() //Обновление полоски здоровья при изменении его значения
    {
        for(int i = 0; i < _clay.getChildCount(); i++)
        {
            if(_clay.getChildAt(i).getTag() == "imgHealthBar"
                    && !(health<0)
                    && !(health>3))
            {
                imgHealthUpd = (ImageView) _clay.getChildAt(i);
                imgHealthUpd.setImageResource(healthArr[health]);
            }
        }
    }

    public void startGame() // Использовать при выходе из меню паузы и начале игры
    {
        try {
            intent_extras:
            {
                if(!gameAtFirtStart) {
                    snd = Integer.valueOf(getIntent().getExtras().get("sound").toString());
                    mus = Integer.valueOf(getIntent().getExtras().get("music").toString());;

                    if (snd == 0) soundOn = false;
                    else if (snd == 1) soundOn = true;

                    if (mus == 0) musicOn = false;
                    else if (mus == 1) musicOn = true;
                }
                else{
                    if(musicOn) _playerBackground.start();
                }
            }
        }
        catch(NullPointerException e){
            //myToast.toastStatic(getApplication(), "oops");
        }
        finally {
            if (_tmStart != null) _tmStart.cancel();                      //Начало игры
            _tmStart = new Timer();
            _taskStart = new TaskGameStart();
            _tmStart.schedule(_taskStart, 1000, 995);

            _lblMessage.setVisibility(View.INVISIBLE);
            _layPause.setVisibility(View.INVISIBLE);

            for (int i = 0; i < _clay.getChildCount(); i++) // Делает полоску здоровья видимой
            {
                if (_clay.getChildAt(i).getTag() == "imgHealthBar")
                    _clay.getChildAt(i).setVisibility(View.VISIBLE);
            }
            gameAtFirtStart = false;
        }
    }

    public void lowerHealth() // Получение урона
    {
        health--;
        updateHealthImg();
        doubleFire = false;
        if(playerShootPeriod <= 600) playerShootPeriod += 100;
        soundPlay(sndDamageId);
    }


    public void soundPlay(int sndId) // Проигрывание звуков
    {
        if(soundOn) //Должен ли звук играть
        {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float leftVolume = curVolume / maxVolume;
            float rightVolume = curVolume / maxVolume;
            int no_loop = 0;
            float normal_playback_rate = 1f;
            int priority = 1;

            streamId = _snd.play(sndId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
        }

    }
}
