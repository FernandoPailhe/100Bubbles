package com.example.hundred;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.LTGRAY;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static com.example.hundred.Utilities.AUTO_TUTORIAL_NUMBER;
import static com.example.hundred.Utilities.ENABLED_GAME_OVER_BUTTON;
import static com.example.hundred.Utilities.INTERVAL_RUSH;
import static com.example.hundred.Utilities.MODULO_Y;
import static com.example.hundred.Utilities.NUMBER_OF_ADVICE;
import static com.example.hundred.Utilities.NUM_COLUMNS;
import static com.example.hundred.Utilities.RUSH_UP;

public class MainActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        Runnable {

    private static final float SWIPE_VELOCITY = 20;
    private static final float SWIPE_THRESHOLD = 70;
    //private static final float SWIPE_THRESHOLD = 50;
    private static final float ALPHA_BASE = 0.7f;
    private static final float ALPHA_ATRAS = 0.05f;
    private static final float ALPHA_ANTERIOR = 0.5f;
    private static final int ANIMATION_VELOCITY = 250;
    private static final int ANIMATION_VELOCITY_SWIPE = 350;
    private static final int ANIMATION_VELOCITY_TUTORIAL = 3500;


    static final int NUM_COLUMNAS = 5;
    static final int NUM_COLUMNAS_BASE = 7;
    static final int RC_SIGN_IN = 101;

    //Declaración Objetos Lógicos de Gráfica
    private TextView[] casObj = new TextView[71];
    private TextView[] casObjAccion = new TextView[4 * NUM_COLUMNAS_BASE];
    private Button[] casObjAccionbtn = new Button[4 * NUM_COLUMNAS_BASE];
    private RelativeLayout[] piso = new RelativeLayout[10];
    private RelativeLayout[] pisoAccion = new RelativeLayout[3];
    private TextView txtPuntaje;
    private TextView txtMax;
    private TextView txtJugadaMayor;
    private TextView[] txtCombo = new TextView[4];
    private TextView txtComboFijo;
    private TextView txtMaxScoreThis;
    private TextView txtMaxBubbleThis;
    private TextView txtBestGame;

    //Grafica panel botones
    private RelativeLayout rlButtonMenu;
    private ImageView imgResetButton;
    private ImageView imgSound;
    private TextView txtRulesButton;
    private TextView txtBonusSwipe;
    private TextView txtBonusPlus;
    private TextView txtBonusExchange;

    private TextView txtResetButtonGameOver;
    private ImageView imgAnimacionSwipe;
    private ImageView imgSwipeLeftTutorial;
    private ImageView imgSwipeRightTutorial;
    private ImageView imgSwipeUpTutorial;
    private ImageView imgSwipeDownTutorial;
    private ImageView imgCasilleroResplandor;
    private ImageView imgCasilleroSeleccionado;
    private ImageView imgExplode;
    private ImageView imgRotarShinning;
    private ImageView imgResplandorSwipe;
    private ImageView imgShade1;
    private ImageView imgShade2;

    private ImageView imgTutorial;
    private ImageView imgTapTutorial1;
    private ImageView imgBonusPlusAnimacion;
    private ImageView imgAnimatedSing;

    private Button btnSwipeZone;
    private Button btnSwipeAccion1;
    private Button btnSwipeAccion2;
    private TextView txtContinue;

    private LinearLayout rlUserInfo;
    private TextView txtNewCredits;
    private TextView txtCredits;
    private TextView txtUserLevel;
    private TextView txtUserNameProfile;
    private TextView txtAreYouSure;
    private TextView txtYes;
    private TextView txtNo;
    private TextView txtTutorial;

    //Leaderboard Graphics Object
    private RelativeLayout[] rank = new RelativeLayout[4];
    private TextView[] txtRank = new TextView[4];
    private TextView[] txtUserName = new TextView[4];
    private TextView[] txtMaxBubble = new TextView[4];
    private TextView[] txtMaxScore = new TextView[4];

    private ImageView nubesAbajo;
    private ImageView imgNubesAbajoAtras;
    private ImageView[] bonusSwipeAnimacion = new ImageView[5];
    private ImageView imgNubesSep;
    private ImageView imgFondoGameOver;
    private ImageView imgFondoBN;
    private ImageView[] imgAnimatedArrow = new ImageView[4];
    private ImageView[] imgAnimatedCombo = new ImageView[16];
    private ImageView[] imgAnimatedCoin = new ImageView[7];
    private ImageView imgAnimatePlus;
    private ImageView imgGradientCombo;

    private FirebaseAnalytics mFirebaseAnalytics;

    private GestureDetector gestureDetector;

    private float[] mapPosicion = new float[10];

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Declaración de clases
    GameManager gameManager = new GameManager();
    Utilities utilities = new Utilities();
    SoundPlayer soundPlayer = new SoundPlayer();
    //SavedLeaderBoardNote noteGlobal[] = new SavedLeaderBoardNote[99];
    //SavedLeaderBoardNote noteClassic[] = new SavedLeaderBoardNote[99];

    String[] strCas = new String[70];
    String strGameOver = "GAME OVER";
    float[] columnaX = new float[7];
    float buttonPosY;
    float centroX;
    float moduloX;
    float comboFloat;
    int mejorPartidaSQLite;
    int jugadaMayorBestSQLite;
    int livesUsesBestSQLite = 0;
    int[] pisosAnimados = new int[7];
    int tutorialBonusSide = 1;

    int casSeleccionado;
    int casAnterior;
    int progreso = 0;
    int jugadaColumnaTap;
    int jugadaColumnaPosible;
    int combo = 0;
    int comboActual = 0;
    int bonusSwipeTutorial;
    int accionPiso;
    int jugadaColumnaX;
    int actualBonusSwipe;
    int actualBonusPlus;
    int actualBonusExchange;
    int typeBonusTutorial;

    int rowSwipeFirst;
    int rowFirstExchange;
    int columnFirstExchange;

    boolean singleTapBonusImage = false;
    boolean longPress;
    boolean longPressChangeLevel;
    boolean longPressPlus;
    boolean singleTapExchange;
    boolean firstBubble;
    boolean singleTap;
    boolean singleTapSwipeZone;
    boolean swipeZone = true;
    boolean startWithTutorial = false;
    boolean isTutorialRunning = false;
    boolean isGetUpAnimationFinish = true;
    int tutorialPlay;
    int firstTutorial = 0;
    boolean isAnimationMapRunning = false;
    boolean syncSecondCheck = false;
    boolean isGameOver = false;
    boolean isSavedMap = false;
    boolean isSavedGame = false;
    boolean isHighScore = false;
    boolean isFirstTime = true;


    boolean tutorialSwipeShow = false;
    boolean tutorialPlusShow = false;
    boolean tutorialExchangeShow = false;

    int swipeLeft, swipeRight, swipeUp;
    int casilleroPlus;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this, this);

        utilities = new Utilities();


        imgShade2 = findViewById(R.id.shade_middle);
        imgShade1 = findViewById(R.id.shade_top);
        txtPuntaje = findViewById(R.id.puntaje);
        txtMax = findViewById(R.id.max);
        txtCombo[0] = findViewById(R.id.combo);
        txtCombo[1] = findViewById(R.id.combo2);
        txtCombo[2] = findViewById(R.id.combo3);
        txtCombo[3] = findViewById(R.id.combo4);
        txtComboFijo = findViewById(R.id.comboFijo);
        txtJugadaMayor = findViewById(R.id.jugadaMayor);

        rlUserInfo = findViewById(R.id.userInfo);
        txtUserNameProfile = findViewById(R.id.userNameProfile);
        txtNewCredits = findViewById(R.id.newCredits);
        txtCredits = findViewById(R.id.credits);
        txtUserLevel = findViewById(R.id.userLevel);

        txtAreYouSure = findViewById(R.id.areyousure);
        txtYes = findViewById(R.id.yes);
        txtNo = findViewById(R.id.no);
        txtTutorial = findViewById(R.id.go_to_100);
        txtYes.setEnabled(true);
        txtNo.setEnabled(true);



        txtMaxBubbleThis = findViewById(R.id.jugadaMayorBest);
        txtMaxScoreThis = findViewById(R.id.puntajeBest);
        txtBestGame = findViewById(R.id.bestGame);

        imgAnimacionSwipe = findViewById(R.id.BonusImage);

        //Grafica panel de botones
        rlButtonMenu = findViewById(R.id.buttonMenu);
        txtBonusSwipe = findViewById(R.id.bonusSwipe);
        txtBonusPlus = findViewById(R.id.bonusPlus);
        txtBonusExchange = findViewById(R.id.bonusExchange);
        txtRulesButton = findViewById(R.id.rulesButton);
        imgSound = findViewById(R.id.Sound);
        imgResetButton = findViewById(R.id.Reset_button);


        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstTutorial();
            }
        });

        txtResetButtonGameOver = findViewById(R.id.Reset_button_2);

        imgAnimatedArrow[0] = findViewById(R.id.animatedArrow);
        imgAnimatedArrow[1] = findViewById(R.id.animatedArrow2);
        imgAnimatedArrow[2] = findViewById(R.id.animatedArrow3);
        imgAnimatedArrow[3] = findViewById(R.id.animatedArrow4);
        imgAnimatePlus = findViewById(R.id.animatePlus);
        imgAnimatedSing = findViewById(R.id.animateSing);

        nubesAbajo = findViewById(R.id.nubes);
        imgNubesAbajoAtras = findViewById(R.id.nubesAbajoAtras);
        imgNubesSep = findViewById(R.id.nubesSep);

        imgSwipeLeftTutorial = findViewById(R.id.SwipeLeft);
        imgSwipeRightTutorial = findViewById(R.id.SwipeRight);
        imgSwipeUpTutorial = findViewById(R.id.SwipeUp);
        imgSwipeDownTutorial = findViewById(R.id.SwipeDown);
        imgTapTutorial1 = findViewById(R.id.TapIcon1);

        imgCasilleroResplandor = findViewById(R.id.CasilleroResplandor);
        imgCasilleroSeleccionado = findViewById(R.id.CasilleroSeleccionado);
        imgExplode = findViewById(R.id.explode);
        imgRotarShinning = findViewById(R.id.rotarShinning);
        imgResplandorSwipe = findViewById(R.id.ResplandorSwipe);
        imgTutorial = findViewById(R.id.Tutorial);

        imgFondoGameOver = findViewById(R.id.fondoGameOver);
        imgFondoGameOver.setAlpha(0f);
        imgFondoBN = findViewById(R.id.fondo_BN);
        imgFondoBN.setAlpha(0f);


        imgGradientCombo = findViewById(R.id.gradientCombo);

        bonusSwipeAnimacion[0] = findViewById(R.id.BonusImageAnimacion_0);
        bonusSwipeAnimacion[1] = findViewById(R.id.BonusImageAnimacion_1);
        bonusSwipeAnimacion[2] = findViewById(R.id.BonusImageAnimacion_2);
        bonusSwipeAnimacion[3] = findViewById(R.id.BonusImageAnimacion_3);
        bonusSwipeAnimacion[4] = findViewById(R.id.BonusImageAnimacion_4);
        imgAnimatedCoin[0] = findViewById(R.id.animated_coin0);
        imgAnimatedCoin[1] = findViewById(R.id.animated_coin1);
        imgAnimatedCoin[2] = findViewById(R.id.animated_coin2);
        imgAnimatedCoin[3] = findViewById(R.id.animated_coin3);
        imgAnimatedCoin[4] = findViewById(R.id.animated_coin4);
        imgAnimatedCoin[5] = findViewById(R.id.animated_coin5);
        imgAnimatedCoin[6] = findViewById(R.id.animated_coin6);
        imgAnimatedCombo[0] = findViewById(R.id.animated_combo_bubble0);
        imgAnimatedCombo[1] = findViewById(R.id.animated_combo_bubble1);
        imgAnimatedCombo[2] = findViewById(R.id.animated_combo_bubble2);
        imgAnimatedCombo[3] = findViewById(R.id.animated_combo_bubble3);
        imgAnimatedCombo[4] = findViewById(R.id.animated_combo_bubble4);
        imgAnimatedCombo[5] = findViewById(R.id.animated_combo_bubble5);
        imgAnimatedCombo[6] = findViewById(R.id.animated_combo_bubble6);
        imgAnimatedCombo[7] = findViewById(R.id.animated_combo_bubble7);
        imgAnimatedCombo[8] = findViewById(R.id.animated_combo_bubble8);
        imgAnimatedCombo[9] = findViewById(R.id.animated_combo_bubble9);
        imgAnimatedCombo[10] = findViewById(R.id.animated_combo_bubble10);
        imgAnimatedCombo[11] = findViewById(R.id.animated_combo_bubble11);
        imgAnimatedCombo[12] = findViewById(R.id.animated_combo_bubble12);
        imgAnimatedCombo[13] = findViewById(R.id.animated_combo_bubble13);
        imgAnimatedCombo[14] = findViewById(R.id.animated_combo_bubble14);
        imgAnimatedCombo[15] = findViewById(R.id.animated_combo_bubble15);
        imgBonusPlusAnimacion = findViewById(R.id.BonusPlusAnimacion);
        txtContinue = findViewById(R.id.Continue_button);
        btnSwipeZone = findViewById(R.id.swipeZone);
        btnSwipeAccion1 = findViewById(R.id.swipeAccion1);
        btnSwipeAccion2 = findViewById(R.id.swipeAccion2);

        pisoAccion[0] = findViewById(R.id.pisoAccion0);


        rank[0] = findViewById(R.id.rank0);
        rank[1] = findViewById(R.id.rank1);
        rank[2] = findViewById(R.id.rank2);
        rank[3] = findViewById(R.id.rank3);
        txtRank[0] = findViewById(R.id.textRank0);
        txtRank[1] = findViewById(R.id.textRank1);
        txtRank[2] = findViewById(R.id.textRank2);
        txtRank[3] = findViewById(R.id.textRank3);
        txtUserName[0] = findViewById(R.id.textUserName0);
        txtUserName[1] = findViewById(R.id.textUserName1);
        txtUserName[2] = findViewById(R.id.textUserName2);
        txtUserName[3] = findViewById(R.id.textUserName3);
        txtMaxBubble[0] = findViewById(R.id.textMaxBubble0);
        txtMaxBubble[1] = findViewById(R.id.textMaxBubble1);
        txtMaxBubble[2] = findViewById(R.id.textMaxBubble2);
        txtMaxBubble[3] = findViewById(R.id.textMaxBubble3);
        txtMaxScore[0] = findViewById(R.id.textMaxScore0);
        txtMaxScore[1] = findViewById(R.id.textMaxScore1);
        txtMaxScore[2] = findViewById(R.id.textMaxScore2);
        txtMaxScore[3] = findViewById(R.id.textMaxScore3);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        btnSwipeZone.setOnTouchListener(this);
        pisoAccion[0].setOnTouchListener(this);
        btnSwipeAccion1.setOnTouchListener(this);
        btnSwipeAccion2.setOnTouchListener(this);
        imgResetButton.setOnTouchListener(this);
        txtYes.setOnTouchListener(this);
        txtNo.setOnTouchListener(this);

        imgResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areYouSure();
            }
        });

        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(true);
            }
        });

        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areYouSureOff();
            }
        });



        txtBonusSwipe.setOnTouchListener(this);
        txtBonusPlus.setOnTouchListener(this);
        txtBonusExchange.setOnTouchListener(this);
        txtRulesButton.setOnTouchListener(this);
        txtResetButtonGameOver.setOnTouchListener(this);
        txtContinue.setOnTouchListener(this);


        imgAnimacionSwipe.bringToFront();

        rlUserInfo.setAlpha(1f);
        rlUserInfo.bringToFront();
        txtAreYouSure.setAlpha(0f);
        txtYes.setAlpha(0);
        txtNo.setAlpha(0);

        nubesAbajo.bringToFront();
        imgNubesSep.bringToFront();
        imgNubesSep.setAlpha(0.5f);
        txtMax.bringToFront();
        txtPuntaje.bringToFront();
        pisoAccion[0].bringToFront();
        txtAreYouSure.bringToFront();
        txtYes.bringToFront();
        txtNo.bringToFront();


        txtContinue.setAlpha(0f);
        txtContinue.setEnabled(false);
        txtResetButtonGameOver.setAlpha(0f);
        txtResetButtonGameOver.setEnabled(false);


        mFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();


        asigPisos();

        asigCasilleros();

        getScreen();

        cambiarTipo();

        checkFirstTime();

        if (isTutorialRunning) {

        } else if (!isFirstTime) {

            consultarGame();

        }

        soundPlayer.createMediaPlayer(this);

        checkSettings();

    }

    @Override
    public void onStart() {
        super.onStart();
        constantAnimation();
        syncUserData(true, gameManager.isGameOVer());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (syncSecondCheck == false) {
                    syncUserData(true, false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (syncSecondCheck == false) {
                                syncUserData(true, false);
                            }
                        }
                    }, 4000);

                }
            }
        }, 4000);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (isSavedMap == false) {
            saveMap();
            if (isSavedGame == false) {
                actualizarDbGame();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (isSavedMap == false) {
            saveMap();
            if (isSavedGame == false) {
                actualizarDbGame();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isSavedMap == false) {
            saveMap();
            if (isSavedGame == false) {
                actualizarDbGame();
            }
        }

    }

    private void googleSingIn() {


        Bundle params = new Bundle();
        params.putLong("max_bubble", gameManager.player.getMaxBubble());
        mFirebaseAnalytics.logEvent("late_log_in", params);

        Intent mainIntent = new Intent(MainActivity.this, LogoActivity.class);
        startActivity(mainIntent);
        finish();


    }

    public void playArrows() {

        if (gameManager.player.getBonusSwipe() > 0) {


            Drawable drawableArrow1 = imgAnimatedArrow[0].getDrawable();
            Drawable drawableArrow2 = imgAnimatedArrow[1].getDrawable();
            Drawable drawableArrow3 = imgAnimatedArrow[2].getDrawable();
            Drawable drawableArrow4 = imgAnimatedArrow[3].getDrawable();


            if (drawableArrow1 instanceof AnimatedVectorDrawableCompat) {

                AnimatedVectorDrawableCompat avdArrow1 = (AnimatedVectorDrawableCompat) drawableArrow1;
                AnimatedVectorDrawableCompat avdArrow2 = (AnimatedVectorDrawableCompat) drawableArrow2;
                AnimatedVectorDrawableCompat avdArrow3 = (AnimatedVectorDrawableCompat) drawableArrow3;
                AnimatedVectorDrawableCompat avdArrow4 = (AnimatedVectorDrawableCompat) drawableArrow4;
                avdArrow1.setAlpha(255);
                avdArrow2.setAlpha(255);
                avdArrow3.setAlpha(255);
                avdArrow4.setAlpha(255);
                avdArrow1.start();
                avdArrow2.start();
                avdArrow3.start();
                avdArrow4.start();


            } else if (drawableArrow1 instanceof AnimatedVectorDrawable) {

                AnimatedVectorDrawable avdArrow1 = (AnimatedVectorDrawable) drawableArrow1;
                AnimatedVectorDrawable avdArrow2 = (AnimatedVectorDrawable) drawableArrow2;
                AnimatedVectorDrawable avdArrow3 = (AnimatedVectorDrawable) drawableArrow3;
                AnimatedVectorDrawable avdArrow4 = (AnimatedVectorDrawable) drawableArrow4;
                avdArrow1.setAlpha(255);
                avdArrow2.setAlpha(255);
                avdArrow3.setAlpha(255);
                avdArrow4.setAlpha(255);
                avdArrow1.start();
                avdArrow2.start();
                avdArrow3.start();
                avdArrow4.start();

            }

        }
    }

    public void playArrowsTutorial() {

        if (gameManager.player.getBonusSwipe() > 0) {


            Drawable drawableArrow1 = imgAnimatedArrow[0].getDrawable();
            Drawable drawableArrow2 = imgAnimatedArrow[1].getDrawable();
            Drawable drawableArrow3 = imgAnimatedArrow[2].getDrawable();
            Drawable drawableArrow4 = imgAnimatedArrow[3].getDrawable();


            if (drawableArrow1 instanceof AnimatedVectorDrawableCompat) {

                AnimatedVectorDrawableCompat avdArrow1 = (AnimatedVectorDrawableCompat) drawableArrow1;
                AnimatedVectorDrawableCompat avdArrow2 = (AnimatedVectorDrawableCompat) drawableArrow2;
                AnimatedVectorDrawableCompat avdArrow3 = (AnimatedVectorDrawableCompat) drawableArrow3;
                AnimatedVectorDrawableCompat avdArrow4 = (AnimatedVectorDrawableCompat) drawableArrow4;
                avdArrow1.setAlpha(255);
                avdArrow3.setAlpha(255);
                avdArrow1.start();
                avdArrow3.start();


            } else if (drawableArrow1 instanceof AnimatedVectorDrawable) {

                AnimatedVectorDrawable avdArrow1 = (AnimatedVectorDrawable) drawableArrow1;
                AnimatedVectorDrawable avdArrow2 = (AnimatedVectorDrawable) drawableArrow2;
                AnimatedVectorDrawable avdArrow3 = (AnimatedVectorDrawable) drawableArrow3;
                AnimatedVectorDrawable avdArrow4 = (AnimatedVectorDrawable) drawableArrow4;
                avdArrow1.setAlpha(255);
                avdArrow3.setAlpha(255);
                avdArrow1.start();
                avdArrow3.start();

            }

        }
    }

    public void stopArrows() {

        Drawable drawableArrow1 = imgAnimatedArrow[0].getDrawable();
        Drawable drawableArrow2 = imgAnimatedArrow[1].getDrawable();
        Drawable drawableArrow3 = imgAnimatedArrow[2].getDrawable();
        Drawable drawableArrow4 = imgAnimatedArrow[3].getDrawable();

        if (drawableArrow1 instanceof AnimatedVectorDrawableCompat) {

            AnimatedVectorDrawableCompat avdArrow1 = (AnimatedVectorDrawableCompat) drawableArrow1;
            AnimatedVectorDrawableCompat avdArrow2 = (AnimatedVectorDrawableCompat) drawableArrow2;
            AnimatedVectorDrawableCompat avdArrow3 = (AnimatedVectorDrawableCompat) drawableArrow3;
            AnimatedVectorDrawableCompat avdArrow4 = (AnimatedVectorDrawableCompat) drawableArrow4;
            avdArrow1.setAlpha(0);
            avdArrow2.setAlpha(0);
            avdArrow3.setAlpha(0);
            avdArrow4.setAlpha(0);
            avdArrow1.stop();
            avdArrow2.stop();
            avdArrow3.stop();
            avdArrow4.stop();
        } else if (drawableArrow1 instanceof AnimatedVectorDrawable) {

            AnimatedVectorDrawable avdArrow1 = (AnimatedVectorDrawable) drawableArrow1;
            AnimatedVectorDrawable avdArrow2 = (AnimatedVectorDrawable) drawableArrow2;
            AnimatedVectorDrawable avdArrow3 = (AnimatedVectorDrawable) drawableArrow3;
            AnimatedVectorDrawable avdArrow4 = (AnimatedVectorDrawable) drawableArrow4;
            avdArrow1.setAlpha(0);
            avdArrow2.setAlpha(0);
            avdArrow3.setAlpha(0);
            avdArrow4.setAlpha(0);
            avdArrow1.stop();
            avdArrow2.stop();
            avdArrow3.stop();
            avdArrow4.stop();

        }

    }

    public void stopConstantAnimation() {

        Drawable drawableRotarShinning = imgRotarShinning.getDrawable();
        Drawable drawableCasilleroSeleccionado = imgCasilleroSeleccionado.getDrawable();
        if (drawableCasilleroSeleccionado instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avdConstantRotation = (AnimatedVectorDrawableCompat) drawableCasilleroSeleccionado;
            AnimatedVectorDrawableCompat avdRotarShinning = (AnimatedVectorDrawableCompat) drawableRotarShinning;
            avdConstantRotation.stop();
            avdRotarShinning.stop();
        } else if (drawableCasilleroSeleccionado instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avdConstantRotation = (AnimatedVectorDrawable) drawableCasilleroSeleccionado;
            AnimatedVectorDrawable avdRotarShinning = (AnimatedVectorDrawable) drawableRotarShinning;
            avdConstantRotation.stop();
            avdRotarShinning.stop();
        }

    }

    public void constantAnimation() {

        Drawable drawableCasilleroSeleccionado = imgCasilleroSeleccionado.getDrawable();
        if (drawableCasilleroSeleccionado instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avdConstantRotation = (AnimatedVectorDrawableCompat) drawableCasilleroSeleccionado;
            avdConstantRotation.start();
        } else if (drawableCasilleroSeleccionado instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avdConstantRotation = (AnimatedVectorDrawable) drawableCasilleroSeleccionado;
            avdConstantRotation.start();
        }

    }

    public void playSwipeHands(int hand) {

        if (hand == 1 || hand == 5) {
            Drawable drawableSwipeRight = imgSwipeRightTutorial.getDrawable();
            if (drawableSwipeRight instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avdSwipeRightTutorial = (AnimatedVectorDrawableCompat) drawableSwipeRight;
                avdSwipeRightTutorial.start();
            } else if (drawableSwipeRight instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avdSwipeRightTutorial = (AnimatedVectorDrawable) drawableSwipeRight;
                avdSwipeRightTutorial.start();
            }
        }
        if (hand == 2 || hand == 5) {
            Drawable drawableSwipeLeft = imgSwipeLeftTutorial.getDrawable();
            if (drawableSwipeLeft instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avdSwipeLeftTutorial = (AnimatedVectorDrawableCompat) drawableSwipeLeft;
                avdSwipeLeftTutorial.start();
            } else if (drawableSwipeLeft instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avdSwipeLeftTutorial = (AnimatedVectorDrawable) drawableSwipeLeft;
                avdSwipeLeftTutorial.start();
            }
        }
        if (hand == 3) {
            Drawable drawableSwipeUp = imgSwipeUpTutorial.getDrawable();
            if (drawableSwipeUp instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avdSwipeUpTutorial = (AnimatedVectorDrawableCompat) drawableSwipeUp;
                avdSwipeUpTutorial.start();
            } else if (drawableSwipeUp instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avdSwipeUpTutorial = (AnimatedVectorDrawable) drawableSwipeUp;
                avdSwipeUpTutorial.start();
            }
        }
        if (hand == 4) {
            Drawable drawableSwipeDown = imgSwipeDownTutorial.getDrawable();
            if (drawableSwipeDown instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avdSwipeDownTutorial = (AnimatedVectorDrawableCompat) drawableSwipeDown;
                avdSwipeDownTutorial.start();
            } else if (drawableSwipeDown instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avdSwipeDownTutorial = (AnimatedVectorDrawable) drawableSwipeDown;
                avdSwipeDownTutorial.start();
            }
        }
        if (hand == 6) {
            Drawable drawableTap = imgTapTutorial1.getDrawable();
            if (drawableTap instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avdTap = (AnimatedVectorDrawableCompat) drawableTap;
                avdTap.start();
            } else if (drawableTap instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avdTap = (AnimatedVectorDrawable) drawableTap;
                avdTap.start();
            }
        }
    }

    private void rotarShinningAlpha(boolean on) {
        if (on) {
            if (comboFloat == 0) {
                comboFloat = 0.6f;
            } else if (comboFloat > 0.7) {
                comboFloat = comboFloat + 0.04f;
            } else if (comboFloat < 0.9) {
                comboFloat = comboFloat + 0.02f;
            }
        } else {
            comboFloat = 0;
        }
    }

    private void rotarShinningStart() {
        Drawable drawableRotarShinning = imgRotarShinning.getDrawable();
        if (drawableRotarShinning instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avdRotarShinning = (AnimatedVectorDrawableCompat) drawableRotarShinning;
            avdRotarShinning.start();
        } else if (drawableRotarShinning instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avdRotarShinning = (AnimatedVectorDrawable) drawableRotarShinning;
            avdRotarShinning.start();
        }
    }

    public void rotarShinningStop() {

        Drawable drawableRotarShinning = imgRotarShinning.getDrawable();
        if (drawableRotarShinning instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avdRotarShinning = (AnimatedVectorDrawableCompat) drawableRotarShinning;
            avdRotarShinning.start();
        } else if (drawableRotarShinning instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avdRotarShinning = (AnimatedVectorDrawable) drawableRotarShinning;
            avdRotarShinning.start();
        }

    }

    public void changeSound(View view) {

        if (soundPlayer.isSoundPlayable == true) {

            SharedPreferences.Editor editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();

            if (soundPlayer.sound == false) {

                editor.putBoolean("sound", true);
                soundPlayer.sound = true;
                imgSound.setBackgroundResource(R.drawable.ic_bubble_sound_on);
                imgSound.setAlpha(ALPHA_BASE);

            } else if (soundPlayer.sound == true) {

                editor.putBoolean("sound", false);
                soundPlayer.sound = false;
                imgSound.setBackgroundResource(R.drawable.ic_bubble_sound_off);
                imgSound.setAlpha(ALPHA_ANTERIOR);

            }

            editor.apply();

        }


    } //Switchea entre on/off

    public void checkFirstTime() {

        SharedPreferences tutorialCount = getSharedPreferences("tutorialCount", Context.MODE_PRIVATE);

        if (tutorialCount.getBoolean("firstTime", true)) {

            SharedPreferences.Editor editor = tutorialCount.edit();
            editor.putInt("bonusSwipe", 0);
            editor.putInt("bonusPlus", 0);
            editor.putInt("bonusExchange", 0);
            editor.commit();
            crearDbBest();
            firstTutorial();

        } else {

            isFirstTime = false;

        }

    }

    private void updateTutorialViews(int typeTutorial) {

        SharedPreferences tutorialCount = getSharedPreferences("tutorialCount", Context.MODE_PRIVATE);

        if (typeTutorial == 1) {

            bonusSwipeTutorial = tutorialCount.getInt("bonusSwipe", 0);

            if (bonusSwipeTutorial < AUTO_TUTORIAL_NUMBER) {

                bonusSwipeTutorial = bonusSwipeTutorial + 10;
                SharedPreferences.Editor editor = tutorialCount.edit();
                editor.putInt("bonusSwipe", bonusSwipeTutorial);
                editor.commit();

            }

        } else if (typeTutorial == 2) {

            int bonusPlusTutorial = tutorialCount.getInt("bonusPlus", 0);

            if (bonusPlusTutorial < AUTO_TUTORIAL_NUMBER) {

                bonusPlusTutorial = bonusPlusTutorial + 6;
                SharedPreferences.Editor editor = tutorialCount.edit();
                editor.putInt("bonusPlus", bonusPlusTutorial);
                editor.commit();

            }

        } else if (typeTutorial == 7) {

            int bonusExchangeTutorial = tutorialCount.getInt("bonusExchange", 0);

            if (bonusExchangeTutorial < AUTO_TUTORIAL_NUMBER) {

                bonusExchangeTutorial = bonusExchangeTutorial + 6;
                SharedPreferences.Editor editor = tutorialCount.edit();
                editor.putInt("bonusExchange", bonusExchangeTutorial);
                editor.commit();

            }

        }

    }

    public void checkTutorialViews(int typeTutorial) {

        SharedPreferences tutorialCount = getSharedPreferences("tutorialCount", Context.MODE_PRIVATE);

        boolean isFirstTutorial = false;

        if (typeTutorial == 1 && tutorialSwipeShow == false) {

            bonusSwipeTutorial = tutorialCount.getInt("bonusSwipe", 0);

            if (bonusSwipeTutorial < AUTO_TUTORIAL_NUMBER) {

                if (bonusSwipeTutorial < 1) {
                    isFirstTutorial = true;
                }
                bonusSwipeTutorial++;
                SharedPreferences.Editor editor = tutorialCount.edit();
                editor.putInt("bonusSwipe", bonusSwipeTutorial);
                editor.commit();
                tutorialBonusSwipeAuto(isFirstTutorial);

            }

        } else if (typeTutorial == 2 && tutorialPlusShow == false) {

            int bonusPlusTutorial = tutorialCount.getInt("bonusPlus", 0);

            if (bonusPlusTutorial < AUTO_TUTORIAL_NUMBER) {

                if (bonusPlusTutorial < 1) {
                    isFirstTutorial = true;
                }
                bonusPlusTutorial++;
                SharedPreferences.Editor editor = tutorialCount.edit();
                editor.putInt("bonusPlus", bonusPlusTutorial);
                editor.commit();
                tutorialPlusAuto(isFirstTutorial);

            }

        } else if (typeTutorial == 7 && tutorialExchangeShow == false) {

            int bonusExchangeTutorial = tutorialCount.getInt("bonusExchange", 0);

            if (bonusExchangeTutorial < AUTO_TUTORIAL_NUMBER) {

                if (bonusExchangeTutorial < 1) {
                    isFirstTutorial = true;
                }
                bonusExchangeTutorial++;
                SharedPreferences.Editor editor = tutorialCount.edit();
                editor.putInt("bonusExchange", bonusExchangeTutorial);
                editor.commit();
                tutorialExchangeAuto(isFirstTutorial);

            }
        }

    }

    public void checkSettings() {


        if (soundPlayer.isSoundPlayable == false) {

            soundPlayer.sound = false;

        } else {

            SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

            if (settings.getBoolean("sound", true)) {

                soundPlayer.sound = true;

                imgSound.setBackgroundResource(R.drawable.ic_bubble_sound_on);
                if (isTutorialRunning == false) {
                    imgSound.setAlpha(ALPHA_BASE);
                }

            } else {

                soundPlayer.sound = false;

                imgSound.setBackgroundResource(R.drawable.ic_bubble_sound_off);
                if (isTutorialRunning == false) {
                    imgSound.setAlpha(ALPHA_ANTERIOR);
                }

            }

        }

    } //Checkea la configuracion

    public void cambiarTipo() {

        Typeface fontDigital = Typeface.createFromAsset(getAssets(),
                "fonts/digital7.otf");
        txtResetButtonGameOver.setTypeface(fontDigital);
        txtContinue.setTypeface(fontDigital);
        txtPuntaje.setTypeface(fontDigital);
        txtMax.setTypeface(fontDigital);
        txtJugadaMayor.setTypeface(fontDigital);
        txtMaxBubbleThis.setTypeface(fontDigital);
        txtMaxScoreThis.setTypeface(fontDigital);
        txtBestGame.setTypeface(fontDigital);
        txtUserNameProfile.setTypeface(fontDigital);
        txtNewCredits.setTypeface(fontDigital);
        txtCredits.setTypeface(fontDigital);
        txtUserLevel.setTypeface(fontDigital);
        txtAreYouSure.setTypeface(fontDigital);
        txtYes.setTypeface(fontDigital);
        txtNo.setTypeface(fontDigital);
        txtTutorial.setTypeface(fontDigital);
        txtCombo[0].setTypeface(fontDigital);
        txtCombo[1].setTypeface(fontDigital);
        txtCombo[2].setTypeface(fontDigital);
        txtCombo[3].setTypeface(fontDigital);
        txtComboFijo.setTypeface(fontDigital);
        txtComboFijo.setTextColor(WHITE);

        txtCombo[0].setTextColor(WHITE);
        txtCombo[1].setTextColor(WHITE);
        txtCombo[2].setTextColor(WHITE);
        txtCombo[3].setTextColor(WHITE);


        for (int i = 1; i < 71; i++) {
            casObj[i].setTypeface(fontDigital);
        }


    } //Cambia la tipografia de todas los textview

    public void asigPisos() {
        for (int i = 0; i < 10; i++) {
            String pObj = "piso" + (i) + "new";
            int resIDmt = getResources().getIdentifier(pObj, "id", getPackageName());
            piso[i] = findViewById(resIDmt);
        }
    }//Asigna los objetos logicos piso a los objetos graficos.

    public void asigCasilleros() {

        for (int i = 1; i < 71; i++) {
            String cObj = "c" + (i) + "new";
            int resIDmt = getResources().getIdentifier(cObj, "id", getPackageName());
            casObj[i] = findViewById(resIDmt);
            casObj[i].setText("");
            casObj[i].setEnabled(false);
        }
    } //Asigna los objetos lógico casillero a los objetos gráficos

    public void asigCasillerosAccion() {
        for (int i = 8; i <= (2 * NUM_COLUMNAS_BASE); i++) {
            String cObj = "ca" + (i);
            int resIDmt = getResources().getIdentifier(cObj, "id", getPackageName());
            casObjAccion[i] = findViewById(resIDmt);
            casObjAccion[i].setOnTouchListener(this);
            casObjAccion[i].bringToFront();
            casObjAccion[i].setText("");
            casObjAccion[i].setAlpha(0f);
            casObjAccion[i].setBackgroundResource(R.drawable.ic_bubble_dont);
        }
        for (int i = 15; i <= (3 * NUM_COLUMNAS_BASE - (NUM_COLUMNAS_BASE - NUM_COLUMNAS)); i++) {
            String cObj = "ca" + (i);
            int resIDmt = getResources().getIdentifier(cObj, "id", getPackageName());
            casObjAccionbtn[i] = findViewById(resIDmt);
            casObjAccionbtn[i].setOnTouchListener(this);
            casObjAccionbtn[i].setBackgroundResource(R.drawable.bubble_ok);
            casObjAccionbtn[i].setY(mapPosicion[4]);
            casObjAccionbtn[i].bringToFront();
            casObjAccionbtn[i].setEnabled(true);
            casObjAccionbtn[i].setAlpha(0);
        }
        for (int i = 22; i <= (4 * NUM_COLUMNAS_BASE - (NUM_COLUMNAS_BASE - NUM_COLUMNAS)); i++) {
            String cObj = "ca" + (i);
            int resIDmt = getResources().getIdentifier(cObj, "id", getPackageName());
            casObjAccionbtn[i] = findViewById(resIDmt);
            casObjAccionbtn[i].setOnTouchListener(this);
            casObjAccionbtn[i].setBackgroundResource(R.drawable.bubble_ok);
            casObjAccionbtn[i].bringToFront();
            casObjAccionbtn[i].setY(mapPosicion[5]);
            casObjAccionbtn[i].setEnabled(true);
            casObjAccionbtn[i].setAlpha(0);


        }

    } //Asigna los objetos lógico de casilleros de accion a los objetos gráficos invisibles de acción.

    public void getScreen() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        utilities.WIDTHSCREEN = dm.widthPixels;
        utilities.HEIGHTSCREEN = dm.heightPixels;

        int densityDpi = (int) (dm.density * 160f);

        utilities.MODULO_Y = utilities.HEIGHTSCREEN / 12;

        float aspectRatio = utilities.HEIGHTSCREEN / utilities.WIDTHSCREEN;

        float ubicacionPrimera = (float) (utilities.HEIGHTSCREEN) / 2 + utilities.MODULO_Y; //Antes era divido 2


        for (int i = 0; i < 10; i++) {
            mapPosicion[i] = ubicacionPrimera - (i * utilities.MODULO_Y) + (2 * utilities.MODULO_Y);
        }

        btnSwipeZone.setY(mapPosicion[2]);
        buttonPosY = mapPosicion[8] - utilities.MODULO_Y / 3;

        asigCasillerosAccion();

        restablecerAccionPosicion();

        centroX = utilities.WIDTHSCREEN / 2;


        if (densityDpi <= 420) {

            moduloX = utilities.WIDTHSCREEN / 14;

        } else {

            moduloX = utilities.WIDTHSCREEN / 13;

        }

        float centerXNumbers = centroX - moduloX;

        columnaX[0] = centerXNumbers - moduloX * 4;
        columnaX[1] = centerXNumbers - moduloX * 2;
        columnaX[2] = centerXNumbers;
        columnaX[3] = centerXNumbers + moduloX * 2;
        columnaX[4] = centerXNumbers + moduloX * 4;
        columnaX[5] = 0;
        columnaX[6] = 0;


        ubicarCasilleros();

        ubicarCasillerosAccion();

        jugadaColumnaX = 3;

        ubicarArrows();

        txtUserNameProfile.setTranslationX(utilities.WIDTHSCREEN);
        txtUserLevel.setTranslationX(utilities.WIDTHSCREEN);
        txtCredits.setTranslationX(utilities.WIDTHSCREEN);
        txtNewCredits.setY(buttonPosY);
        txtPuntaje.setY(mapPosicion[6]);
        txtMax.setY(mapPosicion[7] + utilities.MODULO_Y / 2);
        txtMax.setTextColor(BLUE);
        txtMax.setAlpha(ALPHA_BASE);
        txtMax.setTextSize(utilities.NUM_SIZE);
        txtAreYouSure.setY(mapPosicion[1] - utilities.MODULO_Y / 2);
        txtJugadaMayor.setY(mapPosicion[8] + utilities.MODULO_Y / 3);
        txtYes.setY(mapPosicion[0] + utilities.MODULO_Y / 2);
        txtNo.setY(mapPosicion[0] + utilities.MODULO_Y / 2);

        imgAnimacionSwipe.setX(columnaX[0] - moduloX * 2);
        imgAnimacionSwipe.setY(mapPosicion[4]);


        //rlUserInfo.setY(MODULO/24);


        //Set position button panel
        rlButtonMenu.setAlpha(ALPHA_BASE);
        rlButtonMenu.setY(buttonPosY);
        txtBonusSwipe.setX(columnaX[0]);
        txtBonusSwipe.setBackgroundResource(R.drawable.ic_fondo_casillero_bonus_vector_0);
        txtBonusSwipe.setTextColor(BLACK);
        txtBonusPlus.setX(columnaX[1]);
        txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus_0);
        txtBonusPlus.setTextColor(BLACK);
        txtBonusExchange.setX(columnaX[2]);
        txtBonusExchange.setBackgroundResource(R.drawable.ic_bubble_exchange_0);
        txtBonusExchange.setTextColor(BLACK);
        txtRulesButton.setX(columnaX[2]);
        txtRulesButton.setY(buttonPosY);
        txtRulesButton.setEnabled(true);
        txtRulesButton.setAlpha(1f);
        imgSound.setX(columnaX[3]);
        imgResetButton.setX(columnaX[4]);
        txtNewCredits.setX(columnaX[4]);


        imgTutorial.setY(buttonPosY);
        imgTutorial.setX(columnaX[1]);
        imgTutorial.setAlpha(0f);


        nubesAbajo.setY(mapPosicion[2] + utilities.MODULO_Y / 2);
        imgNubesAbajoAtras.setY(mapPosicion[3] + utilities.MODULO_Y / 2);
        imgNubesAbajoAtras.setScaleX(-1);

        txtTutorial.setY(mapPosicion[4]);
        ubicarLeaderboard();

        imgNubesSep.setY(mapPosicion[8] - utilities.MODULO_Y / 2);

        txtResetButtonGameOver.setY(mapPosicion[1]);
        txtContinue.setY(mapPosicion[1]);
        txtContinue.bringToFront();


    } //Obtiene el tamaño de la pantalla y define el MODULO de movimiento.

    private void ubicarLeaderboard() {


        Typeface fontDigital = Typeface.createFromAsset(getAssets(),
                "fonts/digital7.otf");

        for (int i = 0; i < 4; i++) {

            float you = 0;
            txtRank[i].setTextColor(BLACK);
            txtRank[i].setY(mapPosicion[7 - i] - utilities.MODULO_Y / 2);
            txtRank[i].setTypeface(fontDigital);
            txtRank[i].setTextSize(utilities.LB_NUM_SIZE);
            txtRank[i].setX(columnaX[0] - moduloX);
            txtUserName[i].setY(mapPosicion[7 - i] - utilities.MODULO_Y / 2);
            txtMaxBubble[i].setY(mapPosicion[8 - i]);
            txtMaxScore[i].setY(mapPosicion[7 - i] + you);
            txtUserName[i].setTypeface(fontDigital);
            txtUserName[i].setTextColor(BLUE);
            txtUserName[i].setX(columnaX[1] + moduloX / 2);
            txtUserName[i].setTextSize(utilities.LB_TEXT_SIZE);
            txtMaxBubble[i].setTypeface(fontDigital);
            txtMaxBubble[i].setTextColor(BLACK);
            txtMaxBubble[i].setX(columnaX[0] - moduloX / 2);
            txtMaxBubble[i].setTextSize(utilities.LB_MEDAL_SIZE);
            txtMaxScore[i].setTypeface(fontDigital);
            txtMaxScore[i].setTextColor(DKGRAY);
            txtMaxScore[i].setX(columnaX[1] + moduloX / 2);
            txtMaxScore[i].setTextSize(utilities.LB_NUM_SIZE);

        }

    }

    private void hideLeaderboard() {
        for (int i = 0; i < 4; i++) {
            txtRank[i].setAlpha(0f);
            txtUserName[i].setAlpha(0f);
            txtMaxBubble[i].setAlpha(0f);
            txtMaxScore[i].setAlpha(0f);
        }
        txtTutorial.setAlpha(0f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    txtRank[i].setAlpha(0f);
                    txtUserName[i].setAlpha(0f);
                    txtMaxBubble[i].setAlpha(0f);
                    txtMaxScore[i].setAlpha(0f);
                }
                txtTutorial.setAlpha(0f);
            }
        }, ANIMATION_VELOCITY);
    }

    private void restoreGameView() {
        hideLeaderboard();
        continuePlaying(true);
    }

    public void ubicarArrows() {

        imgAnimatedArrow[0].setY(mapPosicion[4]);
        imgAnimatedArrow[0].setX(columnaX[4] + moduloX);
        imgAnimatedArrow[1].setY(mapPosicion[5]);
        imgAnimatedArrow[1].setX(columnaX[4] + moduloX);
        imgAnimatedArrow[2].setY(mapPosicion[4]);
        imgAnimatedArrow[2].setScaleX(-1);
        imgAnimatedArrow[2].setX(columnaX[0] - moduloX);
        imgAnimatedArrow[3].setY(mapPosicion[5]);
        imgAnimatedArrow[3].setScaleX(-1);
        imgAnimatedArrow[3].setX(columnaX[0] - moduloX);

    }

    public void ubicarCasilleros() {

        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 2 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 3 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 4 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 5 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 6 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 7 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 8 + j].setX(columnaX[j - 1]);
        }
        for (int j = 1; j <= NUM_COLUMNAS_BASE; j++) {
            casObj[NUM_COLUMNAS_BASE * 9 + j].setX(columnaX[j - 1]);
        }
    }

    public void ubicarCasillerosAccion() {

        for (int i = 0; i < 5; i++) {

            casObjAccion[8 + i].setX(columnaX[i]);
            casObjAccionbtn[15 + i].setX(columnaX[i]);
            casObjAccionbtn[22 + i].setX(columnaX[i]);

        }
    }

    public void restablecerCasilleros() {

        Typeface fontDigital = Typeface.createFromAsset(getAssets(),
                "fonts/digital7.otf");

        for (int i = 1; i < 71; i++) {
            String cObj = "c" + (i) + "new";
            int resIDmt = getResources().getIdentifier(cObj, "id", getPackageName());
            casObj[i] = findViewById(resIDmt);
            casObj[i].setTypeface(fontDigital);
            casObj[i].setTextSize(utilities.NUM_SIZE);
            if ((i - 1) == NUM_COLUMNAS || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 2 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 3 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 4 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 5 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 6 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 7 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 8 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 9) {
                casObj[i].setOnClickListener(null);
                casObj[i].setVisibility(View.GONE);
                casObj[i + (NUM_COLUMNAS_BASE - NUM_COLUMNAS - 1)].setVisibility(View.GONE);
                casObj[i + (NUM_COLUMNAS_BASE - NUM_COLUMNAS - 1)].setOnClickListener(null);
                i = i + (NUM_COLUMNAS_BASE - NUM_COLUMNAS - 1);
            } else {
                casObj[i].setText("    ");
                casObj[i].setTextColor(BLACK);
                casObj[i].setAlpha(ALPHA_BASE);
                //casObj[i].setBackgroundResource(R.drawable.ic_fondo_casillero_base_vector);
            }
        }
    } // Default a todos los casilleros.

    public void restablecerCasRecuperados() {
        for (int i = 1; i < 71; i++) {
            String cObj = "c" + (i) + "new";
            int resIDmt = getResources().getIdentifier(cObj, "id", getPackageName());
            casObj[i] = findViewById(resIDmt);
            casObj[i].setTextSize(utilities.NUM_SIZE);
            if ((i - 1) == NUM_COLUMNAS || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 2 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 3 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 4 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 5 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 6 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 7 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 8 || (i - 1) == NUM_COLUMNAS + NUM_COLUMNAS_BASE * 9) {
                casObj[i].setOnClickListener(null);
                casObj[i].setVisibility(View.GONE);
                casObj[i + (NUM_COLUMNAS_BASE - NUM_COLUMNAS - 1)].setVisibility(View.GONE);
                casObj[i + (NUM_COLUMNAS_BASE - NUM_COLUMNAS - 1)].setOnClickListener(null);
                i = i + (NUM_COLUMNAS_BASE - NUM_COLUMNAS - 1);
            } else {
                casObj[i].setTextColor(BLACK);
                casObj[i].setAlpha(ALPHA_BASE);
                //casObj[i].setTypeface(Typeface.DEFAULT);
                casObj[i].setBackgroundResource(R.drawable.ic_fondo_casillero_base_vector);
                casObj[i].setWidth(utilities.BUBBLES_SIZE);
                casObj[i].setHeight(utilities.BUBBLES_SIZE);
            }
        }
    } // Default a todos los casilleros.

    public void restablecerMapaAlpha(int pisoInicial) {


        int rowsRecovered[] = new int[10];


        for (int i = 0; i < 10; i++) {

            rowsRecovered[i] = (pisoInicial + i + 10) % 10;

        }

        piso[rowsRecovered[0]].setAlpha(ALPHA_BASE);
        piso[rowsRecovered[1]].setAlpha(ALPHA_BASE);
        piso[rowsRecovered[2]].setAlpha(ALPHA_BASE);
        piso[rowsRecovered[3]].setAlpha(ALPHA_BASE);
        piso[rowsRecovered[4]].setAlpha(0f);
        piso[rowsRecovered[5]].setAlpha(0f);
        piso[rowsRecovered[6]].setAlpha(0f);
        piso[rowsRecovered[7]].setAlpha(0f);
        if (gameManager.player.getProgress() > 6) {
            piso[rowsRecovered[8]].setAlpha(ALPHA_ATRAS);
            piso[rowsRecovered[9]].setAlpha(ALPHA_ATRAS);
        } else {
            piso[rowsRecovered[8]].setAlpha(0f);
            piso[rowsRecovered[9]].setAlpha(0f);
        }

        for (int i = 0; i < 4; i++) {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(piso[rowsRecovered[i]], "alpha", ALPHA_BASE);
            fadeIn.setDuration(500);
            fadeIn.start();

        } // Hace aparecer el mapa

    } //Dibuja el mapa con las primeras 4 filas.

    public void restablecerAccionPosicion() {
        pisoAccion[0].setY(mapPosicion[3]);
        btnSwipeAccion1.setY(mapPosicion[4]);
        btnSwipeAccion2.setY(mapPosicion[5]);
    } //Restablece la posición de los pisos de acción.

    public void restablecerMapaPosicion(int pisoInicial) {

        int rowsRecovered[] = new int[10];


        for (int i = 0; i < 10; i++) {

            rowsRecovered[i] = (pisoInicial + i + 10) % 10;

        }

        piso[rowsRecovered[0]].setY(mapPosicion[9] - utilities.MODULO_Y);
        piso[rowsRecovered[1]].setY(mapPosicion[9] - utilities.MODULO_Y * 2);
        piso[rowsRecovered[2]].setY(mapPosicion[9] - utilities.MODULO_Y * 3);
        piso[rowsRecovered[3]].setY(mapPosicion[9] - utilities.MODULO_Y * 4);
        imgCasilleroSeleccionado.setY(mapPosicion[9] - utilities.MODULO_Y);
        imgRotarShinning.setY(mapPosicion[9] - utilities.MODULO_Y - 4);

        moverMapGroupInicial(pisoInicial);


        piso[rowsRecovered[4]].setY(mapPosicion[7]);
        piso[rowsRecovered[5]].setY(mapPosicion[7]);
        piso[rowsRecovered[6]].setY(mapPosicion[8]);
        piso[rowsRecovered[7]].setY(mapPosicion[9]);
        piso[rowsRecovered[8]].setY(mapPosicion[0]);
        piso[rowsRecovered[9]].setY(mapPosicion[1]);

    }//Ubica el mapa en el comienzo

    public void mapaComienzo() {
        casObj[1].setAlpha(0f);
        casObj[2].setAlpha(0f);
        casObj[3].setBackgroundResource(R.drawable.ic_bubble_1);
        imgCasilleroSeleccionado.setX(columnaX[2]);
        imgRotarShinning.setX(columnaX[2] - 4);
        comboFloat = 0f;
        imgRotarShinning.setAlpha(comboFloat);
        casObj[4].setAlpha(0f);
        casObj[5].setAlpha(0f);
        casObj[8].setAlpha(0f);
        casObj[9].setBackgroundResource(R.drawable.ic_bubble_2);
        casObj[10].setBackgroundResource(R.drawable.ic_bubble_2);
        casObj[11].setBackgroundResource(R.drawable.ic_bubble_2);
        casObj[12].setAlpha(0f);
        casObj[15].setBackgroundResource(R.drawable.ic_bubble_3);
        casObj[16].setBackgroundResource(R.drawable.ic_bubble_3);
        casObj[17].setBackgroundResource(R.drawable.ic_bubble_3);
        casObj[18].setBackgroundResource(R.drawable.ic_bubble_3);
        casObj[19].setBackgroundResource(R.drawable.ic_bubble_3);
        piso[9].setAlpha(0f);
        piso[8].setAlpha(0f);
    } //Borra los casilleros que no se usan al comienzo

    private void startMap() {

        printRow(gameManager.getFirstRow());
        printRow(gameManager.getNextRow());
        printRow(gameManager.getThirdRow());
        printRow(gameManager.getFourthRow());
        casSeleccionado = 3;
        imgCasilleroSeleccionado.setX(columnaX[2]);
        imgRotarShinning.setX(columnaX[2] - 4);
        comboFloat = 0f;
        imgRotarShinning.setAlpha(comboFloat);
        casObj[1].setAlpha(0f);
        casObj[2].setAlpha(0f);
        casObj[4].setAlpha(0f);
        casObj[5].setAlpha(0f);
        casObj[8].setAlpha(0f);
        casObj[12].setAlpha(0f);
        piso[9].setAlpha(0f);
        piso[8].setAlpha(0f);

    }

    private void syncMap(int firstRow) {

        for (int k = 0; k < 10; k++) {
            int thisRow = (firstRow + k + 10) % 10;
            for (int i = 0; i < NUM_COLUMNS; i++) {
                int casillero = 1 + i + (thisRow * NUM_COLUMNAS_BASE);
                //casObj[casillero].setText(Integer.toString(casillero));
                casObj[casillero].setText(gameManager.row[thisRow].column[i].getString());
                printBubble(thisRow, casillero, gameManager.row[thisRow].column[i].getValue(), gameManager.row[thisRow].column[i].getState());
            }
        }

    }

    private void txtGameData(boolean isRestoreGame) {

        if (isRestoreGame == false) {
            if (gameManager.getIsMultiple100() == false) {
                animationCombo(jugadaColumnaX);
            }
        }
        txtComboFijo.setY(mapPosicion[6] + utilities.MODULO_Y / 2);
        if (isTutorialRunning == false) {
            DecimalFormat decim = new DecimalFormat("###,###,###");
            txtPuntaje.setText(decim.format(gameManager.player.getScore()));
            txtMax.setText(Long.toString(gameManager.player.getMaxBubble()));
            txtJugadaMayor.setText("Max " + Long.toString(gameManager.player.getMaxBubble()));

            if (gameManager.player.getCombo() > 2) {
                rotarShinningStart();
                if (comboActual == 1) {

                    comboActual = 0;

                } else {

                    comboActual = comboActual + 1;

                }
            } else {
                txtCombo[comboActual].setText("");
                txtComboFijo.setAlpha(0f);
                txtComboFijo.setText("");
                rotarShinningStop();
            }

        }

        if (isRestoreGame) {
            if (gameManager.player.getBonusSwipe() > 0) {
                txtBonusSwipe.setText(Integer.toString(gameManager.player.getBonusSwipe()));
                txtBonusSwipe.setBackgroundResource(R.drawable.ic_bubble_swipe);
            } else {
                txtBonusSwipe.setText("");
                txtBonusSwipe.setBackgroundResource(R.drawable.ic_fondo_casillero_bonus_vector_0);
            }

            if (gameManager.player.getBonusPlus() > 0) {
                txtBonusPlus.setText(Integer.toString(gameManager.player.getBonusPlus()));
                txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus);
            } else {
                txtBonusPlus.setText("");
                txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus_0);
            }

            if (gameManager.player.getBonusExchange() > 0) {
                txtBonusExchange.setText(Integer.toString(gameManager.player.getBonusExchange()));
                txtBonusExchange.setBackgroundResource(R.drawable.ic_bubble_exchange);
            } else {
                txtBonusExchange.setText("");
                txtBonusExchange.setBackgroundResource(R.drawable.ic_bubble_exchange_0);
            }
        }

    } //Actualiza los view de la partida.

    public void gameOverOff() {

        hideLeaderboard();
        imgFondoGameOver.setAlpha(0f);
        txtPuntaje.setTextColor(BLACK);
        txtPuntaje.setTextSize(26);
        txtPuntaje.setY(mapPosicion[6]);
        txtPuntaje.setAlpha(ALPHA_BASE);
        txtMax.setAlpha(ALPHA_BASE);
        txtJugadaMayor.setAlpha(0f);
        txtMaxScoreThis.setAlpha(0f);
        txtMaxBubbleThis.setAlpha(0f);
        txtBestGame.setAlpha(0f);

        txtContinue.setAlpha(0f);
        txtContinue.setEnabled(false);
        txtResetButtonGameOver.setAlpha(0.0f);
        txtResetButtonGameOver.setEnabled(false);

        playability(true);

        rlButtonMenu.setAlpha(ALPHA_BASE);

        imgNubesSep.bringToFront();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                txtContinue.setAlpha(0f);
                txtContinue.setEnabled(false);
                txtResetButtonGameOver.setAlpha(0.0f);
                txtResetButtonGameOver.setEnabled(false);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        txtContinue.setAlpha(0f);
                        txtContinue.setEnabled(false);
                        txtResetButtonGameOver.setAlpha(0.0f);
                        txtResetButtonGameOver.setEnabled(false);

                    }
                }, ANIMATION_VELOCITY);

            }
        }, ANIMATION_VELOCITY);

    } //Deshabilita el cartel de Game Over.

    private void continuePlaying(boolean isContinuePlaying) {

        if (isContinuePlaying == true) {
            txtPuntaje.setTextColor(BLACK);
            txtPuntaje.setTextSize(26);
            txtPuntaje.setY(mapPosicion[6]);
            txtPuntaje.setAlpha(ALPHA_BASE);

            txtJugadaMayor.setAlpha(0f);
            txtMax.setAlpha(ALPHA_BASE);
            txtMaxScoreThis.setAlpha(0f);
            txtMaxBubbleThis.setAlpha(0f);
            txtBestGame.setAlpha(0f);

            txtAreYouSure.setAlpha(0f);
            txtYes.setAlpha(0f);
            txtNo.setAlpha(0f);

            imgFondoGameOver.setAlpha(0f);

            txtContinue.setAlpha(0f);
            txtContinue.setEnabled(false);
            txtResetButtonGameOver.setAlpha(0.0f);
            txtResetButtonGameOver.setEnabled(false);
            txtYes.setEnabled(false);
            txtNo.setEnabled(false);

            rlButtonMenu.setAlpha(ALPHA_BASE);

        }
    }

    private void areYouSureOff() {

        continuePlaying(true);

        playability(true);

        startWithTutorial = false;

        imgNubesSep.bringToFront();

    } //Deshabilita el cartel de are you sure.

    private void areYouSure() {


        if (gameManager.player.getProgress() <= 4) {

            startNewGame(true);

        } else {


            soundPlayer.playSoundMenu(this);


            imgFondoGameOver.bringToFront();
            txtAreYouSure.bringToFront();
            txtYes.bringToFront();
            txtNo.bringToFront();
            txtYes.setEnabled(true);
            txtNo.setEnabled(true);

            ObjectAnimator fadeInGame = ObjectAnimator.ofFloat(imgFondoGameOver, "alpha", 0.3f);
            fadeInGame.setDuration(500);

            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(txtAreYouSure, "alpha", 1.0f);
            fadeIn.setDuration(500);

            ObjectAnimator fadeInYes = ObjectAnimator.ofFloat(txtYes, "alpha", 1.0f);
            fadeInYes.setDuration(500);

            ObjectAnimator fadeInNo = ObjectAnimator.ofFloat(txtNo, "alpha", 1.0f);
            fadeInNo.setDuration(500);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(fadeIn).with(fadeInGame).with(fadeInNo).with(fadeInYes);

            animatorSet.start();

            txtAreYouSure.setTextColor(BLUE);
            txtAreYouSure.setText(R.string.restart);
            txtAreYouSure.setTextSize(45);
            txtYes.setTextSize(45);
            txtNo.setTextSize(45);
            txtYes.setTextColor(WHITE);
            txtNo.setTextColor(WHITE);
            txtYes.setText(R.string.yes);
            txtNo.setText(R.string.no);

            txtPuntaje.setTextColor(WHITE);
            txtPuntaje.setTextSize(52);
            txtPuntaje.setY(mapPosicion[7] + utilities.MODULO_Y / 2);
            txtPuntaje.bringToFront();
            txtJugadaMayor.setTextSize(40);
            txtJugadaMayor.setTextColor(LTGRAY);
            txtJugadaMayor.bringToFront();
            txtJugadaMayor.setAlpha(ALPHA_BASE);
            txtMax.setAlpha(0f);


            playability(false);

        }
    }//Habilita el cartel de are you sure.

    private void areYouSureTutorial(View view) {

        if (progreso <= 4) {

            startWithTutorial = true;
            //tutorialPrimeraPartida();

        } else {

            soundPlayer.playSoundMenu(this);

            imgFondoGameOver.bringToFront();
            txtAreYouSure.bringToFront();
            txtYes.bringToFront();
            txtNo.bringToFront();
            txtYes.setEnabled(true);
            txtNo.setEnabled(true);

            ObjectAnimator fadeInGame = ObjectAnimator.ofFloat(imgFondoGameOver, "alpha", 0.6f);
            fadeInGame.setDuration(500);

            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(txtAreYouSure, "alpha", 1.0f);
            fadeIn.setDuration(500);

            ObjectAnimator fadeInYes = ObjectAnimator.ofFloat(txtYes, "alpha", 1.0f);
            fadeInYes.setDuration(500);

            ObjectAnimator fadeInNo = ObjectAnimator.ofFloat(txtNo, "alpha", 1.0f);
            fadeInNo.setDuration(500);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(fadeIn).with(fadeInGame).with(fadeInNo).with(fadeInYes);

            animatorSet.start();

            txtAreYouSure.setText("the tutorial will restart the game");
            txtAreYouSure.setTextColor(BLUE);
            txtAreYouSure.setTextSize(40);
            txtYes.setTextSize(30);
            txtNo.setTextSize(30);
            txtYes.setTextColor(LTGRAY);
            txtNo.setTextColor(WHITE);
            txtYes.setText("tutorial");
            txtNo.setText("  play  ");

            startWithTutorial = true;


            txtPuntaje.setTextColor(WHITE);
            txtPuntaje.setTextSize(52);
            txtPuntaje.setY(mapPosicion[7] + utilities.MODULO_Y / 2);
            txtPuntaje.bringToFront();
            txtJugadaMayor.setTextSize(40);
            txtJugadaMayor.setTextColor(LTGRAY);
            txtJugadaMayor.bringToFront();
            txtJugadaMayor.setAlpha(ALPHA_BASE);
            txtMax.setAlpha(0f);


            playability(false);

        }

    }//Habilita el cartel de are you sure.

    private void playability(boolean playability) {

        pisoAccion[0].setEnabled(playability);
        btnSwipeAccion1.setEnabled(playability);
        btnSwipeAccion2.setEnabled(playability);
        btnSwipeZone.setEnabled(playability);
        casObjAccion[8].setEnabled(playability);
        casObjAccion[9].setEnabled(playability);
        casObjAccion[10].setEnabled(playability);
        casObjAccion[11].setEnabled(playability);
        casObjAccion[12].setEnabled(playability);
        casObjAccionbtn[15].setEnabled(playability);
        casObjAccionbtn[16].setEnabled(playability);
        casObjAccionbtn[17].setEnabled(playability);
        casObjAccionbtn[18].setEnabled(playability);
        casObjAccionbtn[19].setEnabled(playability);
        casObjAccionbtn[22].setEnabled(playability);
        casObjAccionbtn[23].setEnabled(playability);
        casObjAccionbtn[24].setEnabled(playability);
        casObjAccionbtn[25].setEnabled(playability);
        casObjAccionbtn[26].setEnabled(playability);
        imgResetButton.setEnabled(playability);
        if (isTutorialRunning == false) {
            txtBonusSwipe.setEnabled(playability);
            txtBonusPlus.setEnabled(playability);
            txtBonusExchange.setEnabled(playability);
        }
        imgTutorial.setEnabled(playability);

        if (playability == false) {

            stopArrows();

            if (isTutorialRunning == false) {

                stopConstantAnimation();

            } else if (isTutorialRunning == true) {

                imgRotarShinning.setAlpha(0f);

            }

        } else {

            playArrows();

            constantAnimation();

            if (combo > 4) {

                rotarShinningStart();

            }

        }
    }

    private void animateGameOver(boolean firstCheck) {

        if (isHighScore) {
            getLeaderBoard();
        } else if ( !firstCheck) {
            getSavedLeaderBoard();
        }


        final int pisoDesaparece = gameManager.getThirdRow();
        final int pisoDesaparece2 = gameManager.getFourthRow();
        playability(false);
        calculateLiveCost();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                txtResetButtonGameOver.setEnabled(true);

            }
        }, ENABLED_GAME_OVER_BUTTON);



        txtContinue.setX(columnaX[3] - moduloX);
        txtResetButtonGameOver.setX(columnaX[1]);

        if (firstCheck == false) {

            soundPlayer.playSoundGameOver(this);
            int casRojo = casSeleccionado + NUM_COLUMNAS_BASE;

            if (casRojo > 70) {
                casRojo = casRojo - 70;
            }

            final int casRed = casRojo;
            //casObj[casRed].setBackgroundResource(R.drawable.ic_bubble_dont);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateLeaderboardTransition(pisoDesaparece, pisoDesaparece2);
                }
            }, 175);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameOverOn();
                }
            }, ANIMATION_VELOCITY * 2);
        } else if (firstCheck == true) {

            animateLeaderboardTransition(pisoDesaparece, pisoDesaparece2);
            gameOverOn();

        }

    }

    private void animateLeaderboardTransition(final int pisoDesaparece, final int pisoDesaparece2) {

        piso[pisoDesaparece].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        piso[pisoDesaparece2].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        txtTutorial.setTextSize(30);
        txtTutorial.setY(mapPosicion[9] + (utilities.MODULO_Y / 3) * 2);
        txtTutorial.setTranslationX(1200);
        txtTutorial.setScaleY(1);
        txtTutorial.setScaleX(1);
        txtTutorial.bringToFront();
        txtTutorial.setAlpha(1f);
        txtTutorial.setTextColor(BLACK);


        ObjectAnimator moveX = ObjectAnimator.ofFloat(piso[pisoDesaparece], "translationX", -1200);
        moveX.setDuration(ANIMATION_VELOCITY * 2);
        moveX.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator moveX2 = ObjectAnimator.ofFloat(piso[pisoDesaparece2], "translationX", -1200);
        moveX2.setDuration(ANIMATION_VELOCITY * 2);
        moveX2.setStartDelay(ANIMATION_VELOCITY / 4);
        moveX2.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator moveTitle = ObjectAnimator.ofFloat(txtTutorial, "translationX", 0);
        moveTitle.setDuration(ANIMATION_VELOCITY * 2);
        moveTitle.setInterpolator(new OvershootInterpolator());

        ObjectAnimator moveRank0 = ObjectAnimator.ofFloat(rank[0], "translationX", 0);
        moveRank0.setDuration(ANIMATION_VELOCITY * 2);
        moveRank0.setInterpolator(new OvershootInterpolator());

        ObjectAnimator moveRank1 = ObjectAnimator.ofFloat(rank[1], "translationX", 0);
        moveRank1.setDuration(ANIMATION_VELOCITY * 2);
        moveRank1.setStartDelay(ANIMATION_VELOCITY / 4);
        moveRank1.setInterpolator(new OvershootInterpolator());

        ObjectAnimator moveRank2 = ObjectAnimator.ofFloat(rank[2], "translationX", 0);
        moveRank2.setDuration(ANIMATION_VELOCITY * 2);
        moveRank2.setStartDelay(ANIMATION_VELOCITY / 2);
        moveRank2.setInterpolator(new OvershootInterpolator());

        ObjectAnimator moveRank3 = ObjectAnimator.ofFloat(rank[3], "translationX", 0);
        moveRank3.setDuration(ANIMATION_VELOCITY * 2);
        moveRank3.setStartDelay((ANIMATION_VELOCITY / 4) * 3);
        moveRank3.setInterpolator(new OvershootInterpolator());


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(moveX).with(moveX2).with(moveTitle).with(moveRank0).with(moveRank1).with(moveRank2).with(moveRank3);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoDesaparece].setLayerType(View.LAYER_TYPE_NONE, null);
                piso[pisoDesaparece].setAlpha(0f);
                piso[pisoDesaparece].setTranslationX(0);
                piso[pisoDesaparece2].setLayerType(View.LAYER_TYPE_NONE, null);
                piso[pisoDesaparece2].setAlpha(0f);
                piso[pisoDesaparece2].setTranslationX(0);
            }
        });

    }

    public void gameOverOn() {

        ObjectAnimator fadeInGame = ObjectAnimator.ofFloat(imgFondoGameOver, "alpha", 0.1f);
        fadeInGame.setDuration(500);
        fadeInGame.start();

        imgFondoGameOver.bringToFront();
        rank[0].bringToFront();
        rank[1].bringToFront();
        rank[2].bringToFront();
        rank[3].bringToFront();

        txtBestGame.setEnabled(false);
        txtNo.setEnabled(false);
        txtYes.setEnabled(false);

        txtBestGame.setTextColor(BLUE);
        if (strGameOver == "Best Score") {
            txtBestGame.setText(R.string.new_high_score);
        } else {
            txtBestGame.setText(R.string.this_game);
        }
        txtBestGame.bringToFront();
        txtPuntaje.setTextColor(WHITE);
        txtPuntaje.setTextSize(52);
        txtPuntaje.setY(mapPosicion[7] + utilities.MODULO_Y / 2);
        txtPuntaje.bringToFront();
        txtJugadaMayor.setTextSize(40);
        txtJugadaMayor.setTextColor(LTGRAY);
        txtJugadaMayor.bringToFront();
        txtJugadaMayor.setAlpha(ALPHA_BASE);
        txtMax.setAlpha(0f);

        //txtGameOver.setAlpha(0f);
        txtJugadaMayor.setAlpha(0f);
        txtPuntaje.setAlpha(0f);

        txtMaxScoreThis.setTextColor(BLACK);
        txtMaxScoreThis.setAlpha(ALPHA_BASE);
        txtMaxScoreThis.setTextSize(32);
        txtMaxScoreThis.bringToFront();
        txtMaxBubbleThis.setTextSize(32);
        txtMaxBubbleThis.setTextColor(DKGRAY);
        txtMaxBubbleThis.bringToFront();
        txtMaxBubbleThis.setAlpha(ALPHA_BASE);
        txtBestGame.setTextSize(32);
        txtBestGame.setAlpha(ALPHA_BASE);

        rlButtonMenu.setAlpha(0f);
        txtNewCredits.setAlpha(0f);

        ObjectAnimator moveYReset = ObjectAnimator.ofFloat(txtResetButtonGameOver, "alpha", 1f);
        moveYReset.setDuration(ENABLED_GAME_OVER_BUTTON);
        moveYReset.start();

        ObjectAnimator moveYContinue = ObjectAnimator.ofFloat(txtContinue, "alpha", 1f);
        moveYContinue.setDuration(ENABLED_GAME_OVER_BUTTON);
        moveYContinue.start();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                txtResetButtonGameOver.setEnabled(true);

            }
        }, ENABLED_GAME_OVER_BUTTON);

        if (gameManager.user.isLoggedIn) {
            txtResetButtonGameOver.setText(Long.toString(gameManager.user.newCredits));
        } else {
            txtResetButtonGameOver.setText("");
        }

        txtResetButtonGameOver.bringToFront();

        playability(false);
        //imgNubesSep.setAlpha(0.2f);

    }//Habilita el cartel de Game Over.

    public void startNewGame(boolean isResetGame) {

        if (startWithTutorial == true) {

            txtYes.setAlpha(0f);
            txtNo.setAlpha(0f);
            txtAreYouSure.setAlpha(0f);

            consultarDbBest();

            gameOverOff();

        } else {

            consultarDbBest();

            if (isResetGame) {
                if (gameManager.user.getNewCredits() > 0) {
                    animationCoins(5, 6);
                }
                gameManager.user.getWonCredits(true);

            }

            limpiarDbGame();
            soundPlayer.playSoundReset(this);

            txtAreYouSure.setAlpha(0f);
            txtYes.setAlpha(0f);
            txtNo.setAlpha(0f);
            txtPuntaje.setText("");
            txtMax.setText("");
            txtComboFijo.setText("");
            actualBonusSwipe = 0;
            actualBonusPlus = 0;
            actualBonusExchange = 0;

            if (isTutorialRunning) {
                gameManager.createTutorial();
            } else {
                gameManager.newGame();
                saveLeaderBoard();
                saveClassicLeaderBoard();
            }

            txtCredits.setText(Long.toString(gameManager.user.getCredits()));
            txtUserLevel.setText("Level " + Long.toString(gameManager.user.userLevel / utilities.DIVISION_FACTOR_LEVEL));
            txtBonusSwipe.setText("");
            txtBonusPlus.setText("");
            txtBonusExchange.setText("");
            txtBonusSwipe.setBackgroundResource(R.drawable.ic_fondo_casillero_bonus_vector_0);
            txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus_0);
            txtBonusExchange.setBackgroundResource(R.drawable.ic_bubble_exchange_0);

            limpiarDbMapa();
            gameOverOff();


            restablecerCasilleros();
            restablecerMapaAlpha(gameManager.getFirstRow());
            restablecerMapaPosicion(gameManager.getFirstRow());

            startMap();

            //habilitarCol(gameManager.player.getLastColumnPlay());
            swipeLeft = gameManager.player.getLastColumnPlay() - 1;
            swipeUp = gameManager.player.getLastColumnPlay();
            swipeRight = gameManager.player.getLastColumnPlay() + 1;

            adviceAnimation();


        }

    }

    public void crearInicial(View view) {

        startNewGame(true);

    } // Crea una partida nueva.

    private void saveMap() {

        int previous2 = (gameManager.getFirstRow() + 18) % 10;
        int previous1 = (gameManager.getFirstRow() + 19) % 10;
        List<Integer> row = new ArrayList<>(6);
        row.add(previous2);
        row.add(previous1);
        row.add(gameManager.getFirstRow());
        row.add(gameManager.getNextRow());
        row.add(gameManager.getThirdRow());
        row.add(gameManager.getFourthRow());

        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        dbWriter.beginTransaction();
        try {
            dbWriter.delete("mapa", null, null);
            for (int j = 0; j < 6; j++) {
                int extraBonus = 0;
                if (gameManager.row[row.get(j)].isHasExtraBonus()){
                    extraBonus = 1;
                }
                for (int i = 0; i < NUM_COLUMNS; i++) {
                    ContentValues cv = new ContentValues();
                    cv.put("fila", row.get(j));
                    cv.put("casNum", i);
                    cv.put("casVal", gameManager.row[row.get(j)].column[i].getValue());
                    cv.put("state", gameManager.row[row.get(j)].column[i].getState());
                    cv.put("casBonus", extraBonus);
                    dbWriter.insert("mapa", null, cv);
                }
            }
            dbWriter.setTransactionSuccessful();
        } finally {
            dbWriter.endTransaction();
            isSavedMap = true;
        }

    }

    public void limpiarDbGame() {
        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();
        dbWriter.delete("game", "filaActual", null);
        dbWriter.close();
    } //Limpia la base de datos de la partida.

    public void actualizarDbGame() {

        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();


        ContentValues cv = new ContentValues();

        cv.put("filaActual", gameManager.player.getProgress());
        cv.put("jugadaValor", gameManager.player.getPlayValue());
        cv.put("jugadaColumna", gameManager.player.getLastColumnPlay());
        cv.put("puntaje", gameManager.player.getScore());
        cv.put("jugadaMayor", gameManager.player.getMaxBubble());
        cv.put("combo", gameManager.player.getCombo());
        cv.put("bonusSwipe", gameManager.player.getBonusSwipe());
        cv.put("bonusPlus", gameManager.player.getBonusPlus());
        cv.put("lives", gameManager.user.getLivesUses());
        cv.put("isMaxBubbleCreditsWon", gameManager.user.getIsMaxBubbleCreditsWon());
        cv.put("isComboCreditsWon", gameManager.user.getIsComboCreditsWon());
        cv.put("bonusExchange", gameManager.player.getBonusExchange());
        dbWriter.beginTransaction();
        try {
            dbWriter.delete("game", null, null);
            dbWriter.insert("game", null, cv);
            dbWriter.setTransactionSuccessful();
        } finally {
            dbWriter.endTransaction();
            isSavedGame = true;
        }

    } // Actualiza la base de datos de la partida.

    private void crearDbBest() {

        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        dbWriter.delete("best", "jugadaMayor", null);
        cv.put("jugadaMayor", 1);
        cv.put("puntaje", 0);
        cv.put("partidas", 0);
        dbWriter.insert("best", null, cv);
        dbWriter.close();

    } //Implementar esto en vez que firstScore

    public void actualizarDbBest(boolean isClassicScore, boolean isGlobalScore) {

        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb", null, 1);


        if (isGlobalScore) {

            SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            dbWriter.delete("best", "jugadaMayor", null);
            cv.put("jugadaMayor", gameManager.player.getMaxBubble());
            cv.put("puntaje", gameManager.player.getScore());
            cv.put("partidas", gameManager.user.getLivesUses());
            dbWriter.insert("best", null, cv);
            dbWriter.close();


            gameManager.user.setMaxBubble(gameManager.player.getMaxBubble());
            gameManager.user.setMaxScore(gameManager.player.getScore());

            Bundle bundle = new Bundle();
            bundle.putLong(FirebaseAnalytics.Param.SCORE, gameManager.player.getScore());
            bundle.putLong(FirebaseAnalytics.Param.QUANTITY, gameManager.player.getMaxBubble());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle);

            Map<String, Object> newScore = new HashMap<>();
            newScore.put("maxBubble", gameManager.player.getMaxBubble());
            newScore.put("maxScore", gameManager.player.getScore());
            newScore.put("livesUses", gameManager.user.livesUses);

            updateUsers(newScore);

        }

        if (isClassicScore) {

            SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            dbWriter.delete("best", "jugadaMayor", null);
            cv.put("maxBubbleClassic", gameManager.player.getMaxBubble());
            cv.put("maxScoreClassic", gameManager.player.getScore());
            dbWriter.insert("best", null, cv);
            dbWriter.close();


            gameManager.user.setMaxBubbleClassic(gameManager.player.getMaxBubble());
            gameManager.user.setMaxScoreClassic(gameManager.player.getScore());

            Map<String, Object> newScore = new HashMap<>();
            newScore.put("maxBubbleClassic", gameManager.player.getMaxBubble());
            newScore.put("maxScoreClassic", gameManager.player.getScore());

            updateUsers(newScore);

        }

    }

    private void syncUserData(final boolean firstCheck, final boolean gameOver) {

        if (gameManager.user.isLoggedIn == false) {
            SharedPreferences userSP = getSharedPreferences("userSP", Context.MODE_PRIVATE);
            gameManager.user.isLoggedIn = userSP.getBoolean("logged in", false);
            gameManager.user.userMail = userSP.getString("email", "defaultStringIfNothingFound");
            gameManager.user.userName = userSP.getString("name", "defaultStringIfNothingFound");
        }
        if (gameManager.user.isLoggedIn == true) {
            mFirestore.collection("users").document(gameManager.user.userMail).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (firstCheck) {
                                gameManager.user.maxBubbleCount = documentSnapshot.getLong("maxBubbleCount");
                                gameManager.user.maxScoreCount = documentSnapshot.getLong("maxScoreCount");
                                gameManager.user.countMoves = documentSnapshot.getLong("countMoves");
                                gameManager.user.setCredits(documentSnapshot.getLong("credits"));
                                gameManager.user.newCredits = documentSnapshot.getLong("newCredits");
                                gameManager.user.spendCredits = documentSnapshot.getLong("spendCredits");
                                gameManager.user.maxCombo = documentSnapshot.getLong("maxCombo");
                                gameManager.user.maxBubbleCredits = documentSnapshot.getLong("maxBubbleCredits");
                                gameManager.user.livesUses = documentSnapshot.getLong("livesUses");
                                gameManager.user.userLevel = documentSnapshot.getLong("level");
                                gameManager.user.over100 = documentSnapshot.getLong("over100");
                                gameManager.user.over200 = documentSnapshot.getLong("over200");
                                gameManager.user.newGames = documentSnapshot.getLong("newGames");
                                gameManager.user.totalLivesUses = documentSnapshot.getLong("totalLivesUses");
                                txtCredits.setText(Long.toString(gameManager.user.credits));
                                txtNewCredits.setText(Long.toString(gameManager.user.newCredits));
                                txtUserNameProfile.setText(gameManager.user.userName);
                                txtUserLevel.setText(getString(R.string.level) + " " + Long.toString(gameManager.user.userLevel / utilities.DIVISION_FACTOR_LEVEL));
                                animateInUserProfile();
                                syncSecondCheck = true;
                            }
                            gameManager.user.userMail = documentSnapshot.getString("email");
                            gameManager.user.userName = documentSnapshot.getString("name");
                            gameManager.user.setMaxBubble(documentSnapshot.getLong("maxBubble"));
                            gameManager.user.setMaxScore(documentSnapshot.getLong("maxScore"));
                            gameManager.user.setMaxBubbleClassic(documentSnapshot.getLong("maxBubbleClassic"));
                            gameManager.user.setMaxScoreClassic(documentSnapshot.getLong("maxScoreClassic"));
                            if (gameOver && !isFirstTime) {
                                calculateLiveCost();
                                getLeaderBoard();
                                consultarDbBest();
                                animateGameOver(firstCheck);
                            }
                            getLocalDbBest();
                            syncDataBasesBest(gameManager.user.getMaxBubble(), gameManager.user.getMaxScore());
                        }
                    });
        }
    }

    private void syncDataBasesBest(long maxBubble, long maxScore) {

        SharedPreferences userSP = getSharedPreferences("userSP", Context.MODE_PRIVATE);
        gameManager.user.isLoggedIn = userSP.getBoolean("logged in", false);


        Map<String, Object> newScore = new HashMap<>();
        if (livesUsesBestSQLite == 0) {
            if (gameManager.user.maxBubble != 0) {
                if (gameManager.user.maxBubbleClassic < jugadaMayorBestSQLite) {
                    newScore.put("maxBubbleClassic", jugadaMayorBestSQLite);
                    newScore.put("maxScoreClassic", mejorPartidaSQLite);
                    updateUsers(newScore);
                } else if (gameManager.user.maxBubbleClassic == jugadaMayorBestSQLite) {
                    if (gameManager.user.maxScoreClassic < mejorPartidaSQLite) {
                        newScore.put("maxBubbleClassic", jugadaMayorBestSQLite);
                        newScore.put("maxScoreClassic", mejorPartidaSQLite);
                        updateUsers(newScore);
                    }
                }
            }
        }

        if (gameManager.user.maxBubble != 0) {
            if (gameManager.user.maxBubble < jugadaMayorBestSQLite) {
                newScore.put("maxBubble", jugadaMayorBestSQLite);
                newScore.put("maxScore", mejorPartidaSQLite);
                newScore.put("livesUses", livesUsesBestSQLite);
                updateUsers(newScore);
            } else if (gameManager.user.maxBubble == jugadaMayorBestSQLite) {
                if (gameManager.user.maxScore < mejorPartidaSQLite) {
                    newScore.put("maxBubble", jugadaMayorBestSQLite);
                    newScore.put("maxScore", mejorPartidaSQLite);
                    newScore.put("livesUses", livesUsesBestSQLite);
                    updateUsers(newScore);
                }
            }
        }

    }

    private void getLocalDbBest() {
        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        Cursor cursor = dbWriter.rawQuery("select jugadaMayor, puntaje, partidas from best", null);


        if (cursor.moveToFirst()) {
            jugadaMayorBestSQLite = cursor.getInt(0);
            mejorPartidaSQLite = cursor.getInt(1);
            livesUsesBestSQLite = cursor.getInt(2);
        }
        dbWriter.close();
    }

    private void updateUsers(Map updateHashMap) {

        if (gameManager.user.isLoggedIn == true) {
            mFirestore.collection("users").document(gameManager.user.userMail).update(updateHashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Sync with server failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    private void saveLeaderBoard(){

        SharedPreferences globalLB = getSharedPreferences("globalLB", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = globalLB.edit();

        editor.clear();

        mFirestore.collection("users")
                .orderBy("maxBubble", Query.Direction.DESCENDING)
                .orderBy("maxScore", Query.Direction.DESCENDING)
                .orderBy("livesUses", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int i = 0;
                        int j = 0;

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            LeaderBoardNote note = documentSnapshot.toObject(LeaderBoardNote.class);

                            String emailLB = note.getEmail();

                            if ( i < 4) {

                                editor.putString("email" + Integer.toString(i), note.getEmail());
                                editor.putString("name" + Integer.toString(i), note.getName());
                                editor.putLong("maxBubble"+ Integer.toString(i), note.getMaxBubble());
                                editor.putLong("maxScore"+ Integer.toString(i), note.getMaxScore());

                                i++;
                            }
                            if (Objects.equals(gameManager.user.userMail, (emailLB))){
                                editor.putLong("rank", j);
                            }
                            j++;
                        }
                        editor.commit();

                    }


                });

    }

    private void saveClassicLeaderBoard(){

        SharedPreferences classicLB = getSharedPreferences("classicLB", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editorClassic = classicLB.edit();

        editorClassic.clear();

        mFirestore.collection("users")
                .orderBy("maxBubbleClassic", Query.Direction.DESCENDING)
                .orderBy("maxScoreClassic", Query.Direction.DESCENDING)
                .orderBy("livesUses", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int i = 0;
                        int j = 0;

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            LeaderBoardNote note2 = documentSnapshot.toObject(LeaderBoardNote.class);

                            String emailLB = note2.getEmail();

                            if ( i < 4) {

                                editorClassic.putString("email" + Integer.toString(i), note2.getEmail());
                                editorClassic.putString("name" + Integer.toString(i), note2.getName());
                                editorClassic.putLong("maxBubbleClassic"+ Integer.toString(i), note2.getMaxBubbleClassic());
                                editorClassic.putLong("maxScoreClassic"+ Integer.toString(i), note2.getMaxScoreClassic());

                                i++;

                            }
                            if (Objects.equals(gameManager.user.userMail, (emailLB))){
                                editorClassic.putLong("rank", j);
                            }
                            j++;
                        }
                        editorClassic.commit();
                    }
                });

    }

    private void getSavedLeaderBoard(){

        for (int i = 0; i < 4; i++) {
            rank[i].setTranslationX(1500);
        }

        boolean isClassic = false;
        String leaderBoardMode = "classicLB";

        if (gameManager.user.livesUses == 0) {
            isClassic = true;
            txtTutorial.setText(R.string.survival);
        } else {
            txtTutorial.setText(R.string.chill);
            leaderBoardMode = "globalLB";
        }
        final boolean isClassicFinal = isClassic;

        SharedPreferences globalLB = getSharedPreferences(leaderBoardMode, Context.MODE_PRIVATE);

        DecimalFormat decim = new DecimalFormat("###,###,###");

        boolean isHighScoreShow = false;

        for (int i = 0; i < 4; i ++) {

            String emailLB = globalLB.getString("mail" + Integer.toString(i), "defaultStringIfNothingFound");

            if (i < 4) {
                if (i == globalLB.getLong("rank", 3) && isHighScoreShow == false) {
                    txtUserName[i].setTextColor(RED);
                    isHighScoreShow = true;
                } else {
                    txtUserName[i].setTextColor(BLUE);
                }
                if (i < 3) {
                    if (isClassicFinal) {
                        String userName = globalLB.getString("name" + Integer.toString(i), "defaultStringIfNothingFound");
                        long maxBubble = globalLB.getLong("maxBubbleClassic" + Integer.toString(i), 0);
                        long maxScore = globalLB.getLong("maxScoreClassic" + Integer.toString(i), 0);
                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                        txtMaxScore[i].setText(decim.format(maxScore));
                        txtUserName[i].setText(userName);
                    } else {
                        String userName = globalLB.getString("name" + Integer.toString(i), "defaultStringIfNothingFound");
                        long maxBubble = globalLB.getLong("maxBubble" + Integer.toString(i), 0);
                        long maxScore = globalLB.getLong("maxScore" + Integer.toString(i), 0);
                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                        txtMaxScore[i].setText(decim.format(maxScore));
                        txtUserName[i].setText(userName);
                    }

                    txtUserName[i].setAlpha(ALPHA_BASE);
                    txtMaxBubble[i].setAlpha(1f);
                    txtMaxScore[i].setAlpha(ALPHA_BASE);
                }
                txtRank[2].setAlpha(0f);
                if (i == 3 && isHighScoreShow == true) {
                    if (isClassicFinal) {
                        String userName = globalLB.getString("name" + Integer.toString(i), "defaultStringIfNothingFound");
                        long maxBubble = globalLB.getLong("maxBubbleClassic" + Integer.toString(i), 0);
                        long maxScore = globalLB.getLong("maxScoreClassic" + Integer.toString(i), 0);
                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                        txtMaxScore[i].setText(decim.format(maxScore));
                        txtUserName[i].setText(userName);
                    } else {
                        String userName = globalLB.getString("name" + Integer.toString(i), "defaultStringIfNothingFound");
                        long maxBubble = globalLB.getLong("maxBubble" + Integer.toString(i), 0);
                        long maxScore = globalLB.getLong("maxScore" + Integer.toString(i), 0);
                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                        txtMaxScore[i].setText(decim.format(maxScore));
                        txtUserName[i].setText(userName);
                    }
                    txtUserName[i].setAlpha(ALPHA_BASE);
                    txtMaxBubble[i].setAlpha(1f);
                    txtMaxScore[i].setAlpha(ALPHA_BASE);
                    txtRank[i].setAlpha(0f);
                }
            }

        }

        if (gameManager.user.isLoggedIn && !isHighScoreShow) {
                txtUserName[3].setTextColor(RED);
                txtUserName[3].setText(gameManager.user.userName);
                txtUserName[3].setTextColor(RED);
                txtUserName[3].setAlpha(ALPHA_BASE);
                txtUserName[3].bringToFront();
                if (isClassicFinal) {
                    txtMaxScore[3].setText(decim.format(gameManager.user.getMaxScoreClassic()));
                    txtMaxBubble[3].setText(Long.toString(gameManager.user.getMaxBubbleClassic()));
                } else {
                    txtMaxScore[3].setText(decim.format(gameManager.user.getMaxScore()));
                    txtMaxBubble[3].setText(Long.toString(gameManager.user.getMaxBubble()));
                }
                txtMaxBubble[3].setAlpha(1f);
                txtMaxBubble[3].bringToFront();
                txtMaxScore[3].setAlpha(ALPHA_BASE);
                txtMaxScore[3].bringToFront();
                txtRank[3].setText(Long.toString(globalLB.getLong("rank", 8) + 1) + ".");
                txtRank[3].setGravity(Gravity.LEFT);
                txtRank[3].setAlpha(ALPHA_BASE);
                txtRank[3].bringToFront();
                isHighScoreShow = true;
        } else if (!isHighScoreShow ){
            txtUserName[3].setTextColor(RED);
            txtUserName[3].setText(R.string.not_log_leaderboard);
            txtUserName[3].setTextColor(RED);
            txtUserName[3].setAlpha(ALPHA_BASE);
            txtUserName[3].bringToFront();
            txtMaxBubble[3].setText(Long.toString(gameManager.player.getMaxBubble()));
            txtMaxBubble[3].setAlpha(1f);
            txtMaxBubble[3].bringToFront();
        }
    }

    private void getLeaderBoard(){

        for (int i = 0; i < 4; i++) {
            rank[i].setTranslationX(1500);
        }

        boolean isClassic = false;
        String typeBubble = "maxBubble";
        String typeScore = "maxScore";

        if (gameManager.user.livesUses == 0) {
            typeBubble = "maxBubbleClassic";
            typeScore = "maxScoreClassic";
            isClassic = true;
            txtTutorial.setText(R.string.survival);
        } else {
            txtTutorial.setText(R.string.chill);
        }
        final boolean isClassicFinal = isClassic;

        mFirestore.collection("users")
                .orderBy(typeBubble, Query.Direction.DESCENDING)
                .orderBy(typeScore, Query.Direction.DESCENDING)
                .orderBy("livesUses", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        DecimalFormat decim = new DecimalFormat("###,###,###");
                        int i = 0;
                        boolean isHighScoreShow = false;

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            LeaderBoardNote note = documentSnapshot.toObject(LeaderBoardNote.class);

                            String emailLB = note.getEmail();

                            if (i < 4) {
                                if (Objects.equals(gameManager.user.userMail, (emailLB)) && isHighScoreShow == false) {
                                    txtUserName[i].setTextColor(RED);
                                    isHighScoreShow = true;
                                } else {
                                    txtUserName[i].setTextColor(BLUE);
                                }
                                if (i < 3) {
                                    String userName = note.getName();
                                    if (isClassicFinal) {
                                        long maxBubble = note.getMaxBubbleClassic();
                                        long maxScore = note.getMaxScoreClassic();
                                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                                        txtMaxScore[i].setText(decim.format(maxScore));
                                    } else {
                                        long maxBubble = note.getMaxBubble();
                                        long maxScore = note.getMaxScore();
                                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                                        txtMaxScore[i].setText(decim.format(maxScore));
                                    }
                                    txtUserName[i].setText(userName);
                                    txtUserName[i].setAlpha(ALPHA_BASE);
                                    txtMaxBubble[i].setAlpha(1f);
                                    txtMaxScore[i].setAlpha(ALPHA_BASE);
                                }
                                txtRank[2].setAlpha(0f);
                                if (i == 3 && isHighScoreShow == true) {
                                    String userName = note.getName();
                                    if (isClassicFinal) {
                                        long maxBubble = note.getMaxBubbleClassic();
                                        long maxScore = note.getMaxScoreClassic();
                                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                                        txtMaxScore[i].setText(decim.format(maxScore));
                                    } else {
                                        long maxBubble = note.getMaxBubble();
                                        long maxScore = note.getMaxScore();
                                        txtMaxBubble[i].setText(Long.toString(maxBubble));
                                        txtMaxScore[i].setText(decim.format(maxScore));
                                    }
                                    txtUserName[i].setText(userName);
                                    txtUserName[i].setAlpha(ALPHA_BASE);
                                    txtMaxBubble[i].setAlpha(1f);
                                    txtMaxScore[i].setAlpha(ALPHA_BASE);
                                    txtRank[i].setAlpha(0f);
                                }
                            } else {
                                if (gameManager.user.isLoggedIn) {
                                    if (Objects.equals(gameManager.user.userMail, (emailLB)) && isHighScoreShow == false) {
                                        txtUserName[3].setTextColor(RED);
                                        txtUserName[3].setText(gameManager.user.userName);
                                        txtUserName[3].setTextColor(RED);
                                        txtUserName[3].setAlpha(ALPHA_BASE);
                                        txtUserName[3].bringToFront();
                                        if (isClassicFinal) {
                                            txtMaxScore[3].setText(decim.format(gameManager.user.getMaxScoreClassic()));
                                            txtMaxBubble[3].setText(Long.toString(gameManager.user.getMaxBubbleClassic()));
                                        } else {
                                            txtMaxScore[3].setText(decim.format(gameManager.user.getMaxScore()));
                                            txtMaxBubble[3].setText(Long.toString(gameManager.user.getMaxBubble()));
                                        }
                                        txtMaxBubble[3].setAlpha(1f);
                                        txtMaxBubble[3].bringToFront();
                                        txtMaxScore[3].setAlpha(ALPHA_BASE);
                                        txtMaxScore[3].bringToFront();
                                        txtRank[3].setText(Integer.toString(i + 1) + ".");
                                        txtRank[3].setGravity(Gravity.LEFT);
                                        txtRank[3].setAlpha(ALPHA_BASE);
                                        txtRank[3].bringToFront();
                                        isHighScoreShow = true;
                                    }
                                } else {
                                    txtUserName[3].setTextColor(RED);
                                    txtUserName[3].setText(R.string.not_log_leaderboard);
                                    txtUserName[3].setTextColor(RED);
                                    txtUserName[3].setAlpha(ALPHA_BASE);
                                    txtUserName[3].bringToFront();
                                    txtMaxBubble[3].setText(Long.toString(gameManager.player.getMaxBubble()));
                                    txtMaxBubble[3].setAlpha(1f);
                                    txtMaxBubble[3].bringToFront();
                                }
                            }
                            i++;
                        }

                        if (!gameManager.isGameOVer()){
                            gameOverOff();
                        }

                        if (isHighScoreShow == false) {
                        }
                    }
                });

    }

    public void consultarDbBest() {

        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        Cursor cursor = dbWriter.rawQuery("select * from best", null);

        cursor.moveToFirst();

        jugadaMayorBestSQLite = cursor.getInt(0);
        mejorPartidaSQLite = cursor.getInt(1);
        strGameOver = "GAME OVER";
        boolean isClassicScore = false;
        boolean isGlobalScore = false;
        if (gameManager.user.getLivesUses() == 0) {
            if (gameManager.player.getMaxBubble() > gameManager.user.getMaxBubbleClassic() && gameManager.player.getMaxBubble() > gameManager.user.getMaxBubbleClassic()) {
                isClassicScore = true;
            } else if (gameManager.player.getMaxBubble() == gameManager.user.getMaxBubbleClassic() && gameManager.player.getScore() > gameManager.user.getMaxScoreClassic()) {
                isClassicScore = true;
            }
        }
        if (gameManager.player.getMaxBubble() > gameManager.user.getMaxBubble()) {
            isGlobalScore = true;
            strGameOver = "Best Score";
            //actualizarDbBest(false, true);
        } else if (gameManager.player.getMaxBubble() == gameManager.user.getMaxBubble() && gameManager.player.getScore() > gameManager.user.getMaxScore()) {
            isGlobalScore = true;
            strGameOver = "Best Score";
        }
        if (isClassicScore || isGlobalScore){
            isHighScore = true;
        } else {
            isHighScore = false;
        }
        actualizarDbBest(isClassicScore, isGlobalScore);
        dbWriter.close();
        syncUserData(false, false);


        DecimalFormat puntajeFormat = new DecimalFormat("###,###,###,###");
        txtMaxScoreThis.setText(puntajeFormat.format(gameManager.player.getScore()));
        txtMaxBubbleThis.setText("Max " + gameManager.player.getMaxBubble());


    }

    public void limpiarDbMapa() {
        limpiarDbGame();
        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        //int eliminar = dbWriter.delete("mapa", "fila", null);
        dbWriter.delete("mapa", null, null);
        dbWriter.close();

    } //Limpia la base de datos del mapa.

    public void consultarGame() {


        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        Cursor cursor = dbWriter.rawQuery
                ("select * from game", null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 6) {
                    restablecerCasRecuperados();
                    restoreMap();
                    gameManager.player.restoreGame(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(11));
                    gameManager.restoreGame(cursor.getInt(2));

                    casSeleccionado = (gameManager.player.getLastColumnPlay() + 1) + (gameManager.getFirstRow() * NUM_COLUMNAS_BASE);

                    actualBonusSwipe = gameManager.player.getBonusSwipe();
                    actualBonusPlus = gameManager.player.getBonusSwipe();
                    actualBonusExchange = gameManager.player.getBonusExchange();

                    gameManager.user.livesUses = cursor.getInt(8);
                    gameManager.user.isMaxBubbleCreditsWon = cursor.getInt(9);
                    gameManager.user.isComboCreditsWon = cursor.getInt(10);

                    //habilitarCol(gameManager.player.getLastColumnPlay());
                    //valorColumnas(gameManager.getFirstRow());
                    restablecerMapaAlpha(gameManager.getFirstRow());
                    restablecerMapaPosicion(gameManager.getFirstRow());
                    casAnterior = 0;
                    if (gameManager.player.getCombo() > 0) {
                        comboFloat = 0.6f;
                        imgRotarShinning.setAlpha(comboFloat);
                    }
                    jugadaColumnaX = gameManager.player.getLastColumnPlay();

                    marcarJugada(casSeleccionado, casAnterior);

                    txtGameData(true);

                    swipeUp = gameManager.player.getLastColumnPlay();
                    swipeLeft = swipeUp - 1;
                    swipeRight = swipeUp + 1;


                    isGameOver(false);
                    showPossibilities();

                    soundPlayer.playSoundReset(this);

                    saveLeaderBoard();
                    saveClassicLeaderBoard();


                } else {

                    startNewGame(false);

                }

            } else {

                startNewGame(false);

            }
        } catch (Exception e) {
            startNewGame(false);
        }
        dbWriter.close();


    } //Consulta si hay una partida iniciada.

    private void restoreMap() {

        MyOpenHelper dbHelper = new MyOpenHelper
                (this, "100bubblesdb.sqlite", null, 1);
        SQLiteDatabase dbWriter = dbHelper.getWritableDatabase();

        Cursor cursor = dbWriter.rawQuery
                ("select fila, casNum, casVal, state, casBonus from mapa", null);

        cursor.moveToLast();
        gameManager.restoreRowCount(cursor.getInt(0));
        for (int j = 5; j > -1; j--) {
            int thisRow = cursor.getInt(0);
            if (cursor.getInt(4) == 1){
                gameManager.row[thisRow].setHasExtraBonus(true);
            } else {
                gameManager.row[thisRow].setHasExtraBonus(false);
            }
            for (int k = 4; k > -1; k--) {
                //int thisRow = cursor.getInt(0);
                int value = cursor.getInt(2);
                gameManager.row[thisRow].column[k].setValue(cursor.getInt(2));
                int casillero = (thisRow * NUM_COLUMNAS_BASE) + k + 1;
                casObj[casillero].setText(gameManager.row[thisRow].column[k].getString());
                int state = cursor.getInt(3);
                gameManager.row[thisRow].column[k].setState(cursor.getInt(3));
                if (j < 3) {
                    state = 0;
                }
                printBubble(thisRow, casillero, value, state);
                cursor.moveToPrevious();
            }
        }  //Recupera los valores
        dbWriter.close();

    }

    private void printBubble(int pisoNuevo, int casilleroObj, int columnValue, int state) {


        casObj[casilleroObj].setAlpha(ALPHA_BASE);
        casObj[casilleroObj].setTextColor(BLACK);
        //casObj[casilleroObj].setText("");

        switch (state) {

            case 1:
                casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_swipe);
                break;
            case 2:
                casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_plus);
                break;
            case 3:
                casObj[casilleroObj].setAlpha(0f);
                casObj[casilleroObj].setText("");
                break;
            case 4:
                casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_bomb);
                casObj[casilleroObj].setTextColor(WHITE);
                break;
            case 5:
                casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_rush);
                break;
            case 6:
                casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_comodin);
                casObj[casilleroObj].setText("");
                break;
            case 7:
                casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_exchange);
                break;
            default:
                switch (columnValue % 10) {

                    case 1:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_1);
                        break;
                    case 2:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_2);
                        break;
                    case 3:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_3);
                        break;
                    case 4:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_4);
                        break;
                    case 5:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_5);
                        break;
                    case 6:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_6);
                        break;
                    case 7:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_7);
                        break;
                    case 8:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_8);
                        break;
                    case 9:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_9);
                        break;
                    case 0:
                        casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_0);
                        break;

                }
        }

        if (columnValue % INTERVAL_RUSH == 0 && gameManager.player.getMaxBubble() < columnValue) {

            casObj[casilleroObj].setBackgroundResource(R.drawable.ic_bubble_cas_100);

        }


    }

    private void checkAction() {

        if (gameManager.isAnEvent()) {
            if (gameManager.getColumnBomb() >= 0) {
                shakeAnimation();
                int columnBomb = gameManager.getColumnBomb();
                final int casillero1 = gameManager.getNextRow() * NUM_COLUMNAS_BASE + columnBomb + 1;
                final int casillero2 = gameManager.getThirdRow() * NUM_COLUMNAS_BASE + columnBomb + 1;
                final int casillero3 = gameManager.getFourthRow() * NUM_COLUMNAS_BASE + columnBomb + 1;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        printBubble(gameManager.getNextRow(), casillero1, 0, 3);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                printBubble(gameManager.getThirdRow(), casillero2, 0, 3);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        printBubble(gameManager.getFourthRow(), casillero3, 0, 3);


                                    }
                                }, ANIMATION_VELOCITY / 3);


                            }
                        }, ANIMATION_VELOCITY / 3);


                    }
                }, ANIMATION_VELOCITY / 3);

                casObj[casSeleccionado].setBackgroundResource(R.drawable.ic_bubble_0);
            }
            if (gameManager.isRushUp()) {
                //Rush Up animation

                int anteUltimateRow = (gameManager.getFirstRow() + 18) % 10;
                int lastRow = (gameManager.getFirstRow() + 19) % 10;
                printRow(anteUltimateRow);
                printRow(lastRow);
                piso[anteUltimateRow].setAlpha(ALPHA_ATRAS);
                piso[lastRow].setAlpha(ALPHA_ATRAS);
                printRow(gameManager.getFirstRow());
                casObj[casSeleccionado].setBackgroundResource(R.drawable.ic_bubble_0);
                marcarJugada(casSeleccionado, casAnterior);
                continueMap(false);
                soundPlayer.playSoundAtajo(this);
                animationText(4);

            }
        }

    }

    private void printRow(int printRow) {

        for (int i = 1; i <= NUM_COLUMNS; i++) {
            int casillero = i + (printRow * NUM_COLUMNAS_BASE);
            int state = gameManager.row[printRow].column[i - 1].getState();
            casObj[casillero].setText(gameManager.row[printRow].column[i - 1].getString());
            casObj[casillero].setAlpha(ALPHA_BASE);
            printBubble(printRow, casillero, gameManager.row[printRow].column[i - 1].getValue(), state);

        }

    }

    private void continueMap(boolean isContinue) {

        imgGradientCombo.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        imgGradientCombo.setAlpha(0.5f);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imgGradientCombo, "alpha", 0);
        fadeOut.setDuration(ANIMATION_VELOCITY);
        fadeOut.setInterpolator(new FastOutSlowInInterpolator());
        fadeOut.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgGradientCombo.setLayerType(View.LAYER_TYPE_NONE, null);
            }

        });

        int[] pisoNew = new int[3];

        pisoNew[0] = gameManager.getNextRow();
        pisoNew[1] = gameManager.getThirdRow();
        pisoNew[2] = gameManager.getFourthRow();

        for (int i = 0; i < 3; i++) {
            piso[pisoNew[i]].setAlpha(ALPHA_BASE);
            for (int j = 0; j < NUM_COLUMNAS; j++) {
                int casillero = (pisoNew[i] * NUM_COLUMNAS_BASE) + j + 1;
                int state = gameManager.row[pisoNew[i]].column[j].getState();
                casObj[casillero].setText(gameManager.row[pisoNew[i]].column[j].getString());
                printBubble(pisoNew[i], casillero, gameManager.row[pisoNew[i]].column[j].getValue(), state);
                if (i == 0) {
                    if (j < gameManager.player.getLastColumnPlay() - 1 || j > gameManager.player.getLastColumnPlay() + 1) {
                        casObj[casillero].setAlpha(ALPHA_ATRAS);
                    } else {
                        animateBubbleIn(i, casillero, state, j);
                    }
                } else {
                    animateBubbleIn(i, casillero, state, j);
                }
            }
        }

        showPossibilities();
        restoreGameView();
        playability(true);

    }

    public void continueButton() {

        if (gameManager.user.credits >= gameManager.user.liveCost) {

            long actualBubble = gameManager.player.getMaxBubble();

            Bundle params = new Bundle();
            params.putLong("actual_bubble", actualBubble);
            params.putLong("lives_uses", gameManager.user.livesUses);
            params.putLong("total_lives_uses", gameManager.user.totalLivesUses);
            params.putLong("difference_cost", gameManager.user.credits - gameManager.user.liveCost);
            mFirebaseAnalytics.logEvent("continue", params);

            gameManager.user.updateLivesUses(false);
            gameManager.user.spentCredits(gameManager.user.liveCost);
            txtCredits.setText(Long.toString(gameManager.user.credits));
            txtNewCredits.setText(Long.toString(gameManager.user.newCredits));
            soundPlayer.playSoundAtajo(this);

            gameManager.continueGame();
            continueMap(true);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    txtContinue.setAlpha(0f);
                    txtContinue.setEnabled(false);
                    txtResetButtonGameOver.setAlpha(0.0f);
                    txtResetButtonGameOver.setEnabled(false);

                }
            }, ANIMATION_VELOCITY);

        }
    }

    private void animateBubbleIn(int piso, final int casillero, final int state, int columna) {

        casObj[casillero].setScaleX(0);
        casObj[casillero].setScaleY(0);
        casObj[casillero].setTextScaleX(0);
        casObj[casillero].setRotation(-480);
        casObj[casillero].setBackgroundResource(R.drawable.avd_rotar_shinning);
        casObj[casillero].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        long delayCasillero = columna * 30;

        long startDelay = (piso * ANIMATION_VELOCITY) + delayCasillero;


        int comboBubble = (piso + 1) * (columna + 1);

        if (comboBubble < 16) {

            if (columna == 0 || columna == 4) {
                columna = 2;
            }
            animationContinueBubbles(columna, comboBubble);

        }

        ObjectAnimator rotateCas = ObjectAnimator.ofFloat(casObj[casillero], "rotation", 0);
        rotateCas.setDuration(ANIMATION_VELOCITY * 2);
        rotateCas.setStartDelay(startDelay);
        rotateCas.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleCasX = ObjectAnimator.ofFloat(casObj[casillero], "scaleX", utilities.BUBBLE_SCALE);
        scaleCasX.setDuration(ANIMATION_VELOCITY + ANIMATION_VELOCITY / 2);
        scaleCasX.setStartDelay(startDelay);
        scaleCasX.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleTextX = ObjectAnimator.ofFloat(casObj[casillero], "textScaleX", utilities.BUBBLE_SCALE);
        scaleTextX.setDuration(ANIMATION_VELOCITY / 2);
        scaleTextX.setStartDelay(startDelay + ANIMATION_VELOCITY + ANIMATION_VELOCITY / 2);
        scaleTextX.setInterpolator(new OvershootInterpolator());

        ObjectAnimator scaleCasY = ObjectAnimator.ofFloat(casObj[casillero], "scaleY", utilities.BUBBLE_SCALE);
        scaleCasY.setDuration(ANIMATION_VELOCITY + ANIMATION_VELOCITY / 2);
        scaleCasY.setStartDelay(startDelay);
        scaleCasY.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotateCas).with(scaleCasX).with(scaleCasY).with(scaleTextX);

        scaleCasY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_shinning);
            }

        });

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[casillero].setLayerType(View.LAYER_TYPE_NONE, null);
                switch (state) {

                    case 1:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_swipe);
                        break;
                    case 2:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_plus);
                        break;
                    case 3:
                        casObj[casillero].setAlpha(0f);
                        casObj[casillero].setText("");
                        break;
                    case 4:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_bomb);
                        casObj[casillero].setTextColor(WHITE);
                        break;
                    case 5:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_rush);
                        break;
                    case 6:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_comodin);
                        casObj[casillero].setText("");
                        break;
                    case 7:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_exchange);
                        break;
                    default:
                        casObj[casillero].setBackgroundResource(R.drawable.ic_bubble_0);
                        break;
                }

            }

        });

        animatorSet.start();
    }

    private void printMovedRow(final int actionRow, final int actionSwipe) {

        imgResplandorSwipe.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator fadeInResplandor = ObjectAnimator.ofFloat(imgResplandorSwipe, "alpha", 0.0f);
        fadeInResplandor.setDuration(ANIMATION_VELOCITY_SWIPE * 2);
        fadeInResplandor.setStartDelay(100);
        fadeInResplandor.setInterpolator(new OvershootInterpolator());

        fadeInResplandor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ubicarCasilleros();
                imgResplandorSwipe.setLayerType(View.LAYER_TYPE_NONE, null);
                playArrows();
                for (int i = 1; i <= NUM_COLUMNS; i++) {
                    int casillero = i + (actionRow * NUM_COLUMNAS_BASE);
                    int state = gameManager.row[actionRow].column[i - 1].getState();
                    casObj[casillero].setText(gameManager.row[actionRow].column[i - 1].getString());
                    casObj[casillero].setAlpha(ALPHA_BASE);
                    printBubble(actionRow, casillero, gameManager.row[actionRow].column[i - 1].getValue(), state);
                    //updateDbMap(actionRow, i - 1, gameManager.row[actionRow].column[i - 1].getValue(), gameManager.row[actionRow].column[i - 1].getState(),  gameManager.row[actionRow].column[i - 1].getState());

                }

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeInResplandor);
        animatorSet.start();

    }

    private void moveRow(int actionSwipe, int direction, boolean isAutoTutorial) {

        isSavedGame = false;
        isSavedMap = false;
        int actionRow = gameManager.getThirdRow();
        if (actionSwipe == 3) {
            actionRow = gameManager.getFourthRow();
        }
        gameManager.moveRow(actionRow, direction, isAutoTutorial);
        swipeRowAnimation(actionRow, direction);
        printMovedRow(actionRow, actionSwipe);
        updateTutorialViews(1);
        actualBonusSwipe = gameManager.player.getBonusSwipe();
        txtBonusSwipe.setText(Integer.toString(gameManager.player.getBonusSwipe()));
        if (gameManager.player.getBonusSwipe() == 0) {
            txtBonusSwipe.setBackgroundResource(R.drawable.ic_fondo_casillero_bonus_vector_0);
            txtBonusSwipe.setText("");
        }


    }

    private void plusAction(int actionPlus, int actionBubble, int operation, boolean isAutoTutorial) {

        isSavedGame = false;
        isSavedMap = false;
        int actionRow = gameManager.getThirdRow();
        if (actionPlus == 3) {
            actionRow = gameManager.getFourthRow();
        }
        if (gameManager.row[actionRow].column[actionBubble].getState() == 3) {
            soundPlayer.playSoundDont(this);
        } else {
            gameManager.bonusPlus(actionRow, actionBubble, operation, isAutoTutorial);
            printPlus(actionRow, actionBubble, operation, actionPlus);
            if (isAutoTutorial == false) {
                updateTutorialViews(2);
                actualBonusPlus = gameManager.player.getBonusPlus();
            }
            if (gameManager.player.getBonusPlus() == 0) {

                txtBonusPlus.setText("");
                txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus_0);

            } else {

                txtBonusPlus.setText(Integer.toString(gameManager.player.getBonusPlus()));

            }
        }

    }

    private void preExchangeBubble(int actionExchange, int actionBubble) {

        if (gameManager.player.getBonusExchange() > 0) {
            int actionRow = gameManager.getThirdRow();
            if (actionExchange == 3) {
                actionRow = gameManager.getFourthRow();
            }
            if (gameManager.row[actionRow].column[actionBubble].getState() != 3) {
                firstBubble = true;
                rowSwipeFirst = actionExchange;
                rowFirstExchange = actionRow;
                columnFirstExchange = actionBubble;
                final int casilleroObj1 = (columnFirstExchange + 1) + (rowFirstExchange * NUM_COLUMNAS_BASE);
                casObj[casilleroObj1].setScaleY(1.1f);
                casObj[casilleroObj1].setScaleX(1.1f);
                casObj[casilleroObj1].setAlpha(1f);
                soundPlayer.playSoundDown(this);
            }
        } else {

        }

    }

    private void exchangeAnimation(final int rowFirstExchange, final int columnFirstExchange, final int casilleroObj1, final int actionRow, final int actionBubble, final int casilleroObj2, boolean isAutoTutorial) {

        casObj[casilleroObj1].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        casObj[casilleroObj2].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        casObj[casilleroObj2].setAlpha(1f);

        soundPlayer.playSoundExchange(this);

        int multiple = 2;

        if (isAutoTutorial){
            multiple = 4;
        }
        ObjectAnimator scaleOutYFirst = ObjectAnimator.ofFloat(casObj[casilleroObj1], "scaleY", 0);
        scaleOutYFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleOutYFirst.setInterpolator(new AnticipateInterpolator(3));

        ObjectAnimator scaleOutXFirst = ObjectAnimator.ofFloat(casObj[casilleroObj1], "scaleX", 0);
        scaleOutXFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleOutXFirst.setInterpolator(new AnticipateInterpolator(3));


        ObjectAnimator scaleOutYSecond = ObjectAnimator.ofFloat(casObj[casilleroObj2], "scaleY", 0);
        scaleOutYFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleOutYFirst.setInterpolator(new AnticipateInterpolator(3));

        ObjectAnimator scaleOutXSecond = ObjectAnimator.ofFloat(casObj[casilleroObj2], "scaleX", 0);
        scaleOutXFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleOutXFirst.setInterpolator(new AnticipateInterpolator(3));

        ObjectAnimator scaleInYFirst = ObjectAnimator.ofFloat(casObj[casilleroObj1], "scaleY", 1);
        scaleInYFirst.setStartDelay(ANIMATION_VELOCITY * multiple);
        scaleInYFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleInYFirst.setInterpolator(new OvershootInterpolator(3));

        ObjectAnimator scaleInXFirst = ObjectAnimator.ofFloat(casObj[casilleroObj1], "scaleX", 1);
        scaleInXFirst.setStartDelay(ANIMATION_VELOCITY * multiple);
        scaleInXFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleInXFirst.setInterpolator(new AnticipateOvershootInterpolator(3));

        ObjectAnimator scaleInYSecond = ObjectAnimator.ofFloat(casObj[casilleroObj2], "scaleY", 1);
        scaleInYSecond.setStartDelay(ANIMATION_VELOCITY * multiple);
        scaleInYFirst.setDuration(ANIMATION_VELOCITY * multiple);
        scaleInYFirst.setInterpolator(new AnticipateOvershootInterpolator(3));

        ObjectAnimator scaleInXSecond = ObjectAnimator.ofFloat(casObj[casilleroObj2], "scaleX", 1);
        scaleInXSecond.setStartDelay(ANIMATION_VELOCITY * multiple);
        scaleInXSecond.setDuration(ANIMATION_VELOCITY * multiple);
        scaleInXSecond.setInterpolator(new AnticipateOvershootInterpolator(3));

        scaleOutYFirst.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[casilleroObj1].setText(gameManager.row[rowFirstExchange].column[columnFirstExchange].getString());
                printBubble(rowFirstExchange, casilleroObj1, gameManager.row[rowFirstExchange].column[columnFirstExchange].getValue(), gameManager.row[rowFirstExchange].column[columnFirstExchange].getState());
            }
        });

        scaleOutYSecond.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[casilleroObj2].setText(gameManager.row[actionRow].column[actionBubble].getString());
                printBubble(actionRow, casilleroObj2, gameManager.row[actionRow].column[actionBubble].getValue(), gameManager.row[actionRow].column[actionBubble].getState());
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleOutXFirst).with(scaleOutXSecond).with(scaleOutYFirst).with(scaleOutYSecond).with(scaleInXFirst).with(scaleInXSecond).with(scaleInYSecond).with(scaleInYFirst);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[casilleroObj1].setLayerType(View.LAYER_TYPE_NONE, null);
                casObj[casilleroObj2].setLayerType(View.LAYER_TYPE_NONE, null);
                casObj[casilleroObj1].setAlpha(ALPHA_BASE);
                casObj[casilleroObj2].setAlpha(ALPHA_BASE);
            }
        });


    }

    private void exchangeBubble(int actionExchange, int actionBubble, boolean isAutoTutorial) {

        isSavedGame = false;
        isSavedMap = false;
        int actionRow = gameManager.getThirdRow();
        if (actionExchange == 3) {
            actionRow = gameManager.getFourthRow();
        }
        final int casilleroObj1 = (columnFirstExchange + 1) + (rowFirstExchange * NUM_COLUMNAS_BASE);
        final int casilleroObj2 = (actionBubble + 1) + (actionRow * NUM_COLUMNAS_BASE);
        if (gameManager.row[actionRow].column[actionBubble].getState() != 3) {
            if (actionExchange == rowSwipeFirst && actionBubble == columnFirstExchange) {
                firstBubble = false;
                casObj[casilleroObj1].setScaleY(1f);
                casObj[casilleroObj1].setScaleX(1f);
                casObj[casilleroObj1].setAlpha(ALPHA_BASE);
            } else {
                firstBubble = false;
                gameManager.exchangeBubble(rowFirstExchange, columnFirstExchange, actionRow, actionBubble);
                if (isAutoTutorial) {
                    gameManager.player.setBonusExchange(gameManager.player.getBonusExchange() + 1);
                }
                actualBonusExchange = gameManager.player.getBonusExchange();
                if (actualBonusExchange > 0) {
                    txtBonusExchange.setText(Integer.toString(actualBonusExchange));
                } else {
                    txtBonusExchange.setBackgroundResource(R.drawable.ic_bubble_exchange_0);
                    txtBonusExchange.setText("");
                }
                exchangeAnimation(rowFirstExchange, columnFirstExchange, casilleroObj1, actionRow, actionBubble, casilleroObj2, isAutoTutorial);
            }
        }


    }

    private void resetExchange() {

        final int casilleroObj1 = (columnFirstExchange + 1) + (rowFirstExchange * NUM_COLUMNAS_BASE);
        casObj[casilleroObj1].setScaleY(1f);
        casObj[casilleroObj1].setScaleX(1f);
        casObj[casilleroObj1].setAlpha(ALPHA_BASE);

    }

    private void printPlus(final int actionRow, final int casillero, int operation, int actionPlus) {

        final int casilleroObj = (casillero + 1) + (actionRow * NUM_COLUMNAS_BASE);
        final String newValue = gameManager.row[actionRow].column[casillero].getString();
        final int casilleroFinal = casillero;
        final int operationFinal = operation;

        imgAnimatePlus.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        if (operation == 1) {

            imgAnimatePlus.setImageResource(R.drawable.bubble_colors_new_sings_plus);

        } else if (operation == -1) {

            imgAnimatePlus.setImageResource(R.drawable.bubble_colors_new_sings_menos);

        }

        imgAnimatePlus.setX(columnaX[casillero]);
        imgAnimatePlus.setY(mapPosicion[actionPlus + 2]);
        imgAnimatePlus.bringToFront();
        imgAnimatePlus.setScaleY(0);
        imgAnimatePlus.setAlpha(0.5f);


        ObjectAnimator plusMove = ObjectAnimator.ofFloat(imgAnimatePlus, "scaleY", 1);
        plusMove.setDuration(ANIMATION_VELOCITY);
        plusMove.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator plusIn = ObjectAnimator.ofFloat(imgAnimatePlus, "alpha", 0.8f);
        plusIn.setDuration(ANIMATION_VELOCITY);
        plusIn.setInterpolator(new BounceInterpolator());

        ObjectAnimator plusOut = ObjectAnimator.ofFloat(imgAnimatePlus, "alpha", 0f);
        plusOut.setStartDelay(ANIMATION_VELOCITY);
        plusOut.setDuration(ANIMATION_VELOCITY * 2);
        plusOut.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator plusScaleOutY = ObjectAnimator.ofFloat(imgAnimatePlus, "scaleY", 1.05f);
        plusScaleOutY.setStartDelay(ANIMATION_VELOCITY * 2);
        plusScaleOutY.setDuration(ANIMATION_VELOCITY);
        plusScaleOutY.setInterpolator(new AccelerateInterpolator());


        ObjectAnimator plusScaleOutX = ObjectAnimator.ofFloat(imgAnimatePlus, "scaleX", 1.05f);
        plusScaleOutX.setStartDelay(ANIMATION_VELOCITY * 2);
        plusScaleOutX.setDuration(ANIMATION_VELOCITY);
        plusScaleOutX.setInterpolator(new AccelerateInterpolator());

        plusIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                playSingOut(operationFinal, casilleroFinal);
                if (operationFinal == 1) {
                    imgAnimatePlus.setImageResource(R.drawable.bubble_colors_new_sings_plus_2);
                } else if (operationFinal == -1) {
                    imgAnimatePlus.setImageResource(R.drawable.bubble_colors_new_sings_menos_2);
                }
                printBubble(actionRow, casilleroObj, gameManager.row[actionRow].column[casillero].getValue(), gameManager.row[actionRow].column[casillero].getState());
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(plusMove).with(plusOut).with(plusIn);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgAnimatePlus.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        soundPlayer.playSoundPlusPlay(this);

        casObj[casilleroObj].setText(newValue);


    }

    public void play(int columnPlay) {

        boolean isComodin = false;
        if (gameManager.row[gameManager.getFirstRow()].column[gameManager.player.getLastColumnPlay()].getState() == 6 || gameManager.row[gameManager.getNextRow()].column[columnPlay].getState() == 6) {
            isComodin = true;
        }
        int playIntent = gameManager.row[gameManager.getNextRow()].column[columnPlay].getValue();
        if (playIntent == gameManager.player.getPlayValue() - 1 || playIntent == gameManager.player.getPlayValue() + 1 || isComodin && gameManager.row[gameManager.getNextRow()].column[columnPlay].getState() != 3) {

            isSavedGame = false;
            isSavedMap = false;
            if (isTutorialRunning) {
                gameManager.tutorialPlay(columnPlay, tutorialPlay);
            } else {
                gameManager.play(columnPlay);
            }
            //Graphics
            casAnterior = casSeleccionado;
            casSeleccionado = (gameManager.getFirstRow() * NUM_COLUMNAS_BASE) + (1 + columnPlay);
            printRow(gameManager.getFourthRow());
            txtGameData(false);
            jugadaColumnaX = gameManager.player.getLastColumnPlay();

            //Animation
            stopArrows();
            if (isTutorialRunning) {
                moverMapaTutorial(gameManager.getFirstRow());
                marcarJugadaTutorial(casSeleccionado, casAnterior);
            } else {
                marcarJugada(casSeleccionado, casAnterior);
                moverMapa(gameManager.getFirstRow());
            }
            if (gameManager.player.getIsMaxPlay() && isTutorialRunning == false) {
                rotarShinningAlpha(true);
            } else {
                rotarShinningAlpha(false);
            }
            imgRotarShinning.setAlpha(comboFloat);
            if (gameManager.user.isNewCredits()) {
                animationCoins(jugadaColumnaX, 7);
                txtNewCredits.setText(Long.toString(gameManager.user.newCredits));
                if (gameManager.user.newCredits > 0) {
                    //txtNewCredits.setAlpha(ALPHA_BASE);
                }
            }

            //Playability
            swipeUp = gameManager.player.getLastColumnPlay();
            swipeLeft = gameManager.player.getLastColumnPlay() - 1;
            swipeRight = gameManager.player.getLastColumnPlay() + 1;

            if (gameManager.getIsMultiple100()) {

                if (comboActual == 3) {
                    comboActual = 0;
                } else {
                    comboActual++;
                }
                animationText(2);
                continueMap(false);
                isGameOver(false);
                soundPlayer.playSoundCasillero100(this);

            } else {

                isGameOver(false);

                showPossibilities();

            }

            soundPlayer.playSoundUp(this, columnPlay);

        } else {

            soundPlayer.playSoundDont(this);

            animationDont(columnPlay);

        }

    }

    private void isGameOver(final boolean isFirstCheck) {

        if (gameManager.isGameOVer() == false) {
            if (isFirstCheck == false) {
                getUpBonusAdmin(casSeleccionado);
            }
        } else {
            if (isFirstCheck) {
                syncUserData(true, true);
                isGameOver = true;
            } else {
                calculateLiveCost();
                consultarDbBest();
                animateGameOver(isFirstCheck);
            }
        }

    }

    private void calculateLiveCost() {

        boolean lateLogIn = false;

        if (gameManager.user.isLoggedIn == false) {
            SharedPreferences userSP = getSharedPreferences("userSP", Context.MODE_PRIVATE);
            lateLogIn = userSP.getBoolean("logged in", false);
            syncUserData(true, true);
        }
        if (gameManager.user.isLoggedIn || lateLogIn) {

            txtResetButtonGameOver.setBackgroundResource(R.drawable.ic_reset_blanco_with_candado);

            txtContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueButton();
                }
            });

            txtResetButtonGameOver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNewGame(true);
                }
            });


            if (gameManager.user.userLevel < utilities.LOW_COST_MAXLEVEL_LIMIT && gameManager.player.getMaxBubble() < utilities.LOW_COST_MAXB_LIMIT) {
                gameManager.user.liveCost = (gameManager.player.getMaxBubble() * 2) * (gameManager.user.livesUses + 1);
                //TODO event for Analytics
            } else {
                gameManager.user.liveCost = (gameManager.player.getMaxBubble() * utilities.MULT_MAXB_COST) * ((gameManager.user.livesUses + utilities.MULT_LIVESUSES_COST) * (gameManager.user.livesUses + utilities.MULT_LIVESUSES_COST));
                //TODO event for Analytics (Quizas juntar los eventos con la decision de si continuar o no)
            }
            txtContinue.setText(Long.toString(gameManager.user.liveCost));
            txtContinue.setBackgroundResource(R.drawable.ic_bubble_life_coins);

            long delay = 0;
            if (gameManager.user.getCredits() == 0) {
                delay = ENABLED_GAME_OVER_BUTTON;
            }
            if (gameManager.user.liveCost <= gameManager.user.getCredits()) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        txtContinue.setEnabled(true);

                    }
                }, ENABLED_GAME_OVER_BUTTON);

                txtContinue.setTextColor(BLACK);
            } else {
                txtContinue.setEnabled(false);
                txtContinue.setTextColor(LTGRAY);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (gameManager.user.liveCost <= gameManager.user.getCredits()) {


                            txtContinue.setEnabled(true);


                            txtContinue.setTextColor(BLACK);

                        }
                    }
                }, delay);
            }
            txtCredits.setText(Long.toString(gameManager.user.getCredits()));
            txtNewCredits.setText(Long.toString(gameManager.user.newCredits));

        } else {

            txtResetButtonGameOver.setBackgroundResource(R.drawable.ic_reset_blanco);
            txtContinue.setText("Sing in");
            txtContinue.setTextColor(BLUE);
            txtContinue.setBackgroundResource(R.drawable.ic_bubble_life_sing_in);
            txtContinue.setEnabled(true);

            txtContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    googleSingIn();

                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculateLiveCost();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            calculateLiveCost();
                        }
                    }, ENABLED_GAME_OVER_BUTTON);

                }
            }, ENABLED_GAME_OVER_BUTTON);
        }


    }

    private void showPossibilities() {

        if (gameManager.isPlayLeft()) {
            int casillero = gameManager.player.getLastColumnPlay() + (gameManager.getNextRow() * NUM_COLUMNAS_BASE);
            casObj[casillero].setAlpha(1f);
        }
        if (gameManager.isPlayUp()) {
            int casillero = gameManager.player.getLastColumnPlay() + 1 + (gameManager.getNextRow() * NUM_COLUMNAS_BASE);
            casObj[casillero].setAlpha(1f);
        }
        if (gameManager.isPlayRight()) {
            int casillero = gameManager.player.getLastColumnPlay() + 2 + (gameManager.getNextRow() * NUM_COLUMNAS_BASE);
            casObj[casillero].setAlpha(1f);
        }

    }

    public void moverMapa(int pisoMap) {

        int pisoDesaparece = (pisoMap + 10 - 3) % 10;
        int pisoAparece = (pisoDesaparece + 10 + 6) % 10;


        for (int i = 0; i <= 6; i++) {
            pisosAnimados[i] = (pisoDesaparece + i + 10) % 10;
        }

        moverMapGroup();

        for (int i = 0; i <= 9; i++) {
            int pisoMov = (pisoDesaparece + i + 10 + 1) % 10;
            piso[pisoMov].setY(mapPosicion[i]);
        }
        piso[pisoAparece].setAlpha(ALPHA_BASE);
        piso[pisoDesaparece].setAlpha(0f);

        if (gameManager.player.getProgress() < 3) {
            casObj[1].setAlpha(0f);
            casObj[2].setAlpha(0f);
            casObj[4].setAlpha(0f);
            casObj[5].setAlpha(0f);
            casObj[8].setAlpha(0f);
            casObj[12].setAlpha(0f);
        }


    } //Mueve el mapa mientras se juega.

    public void moverMapaTutorial(int pisoMap) {

        int pisoDesaparece = (pisoMap + 10 - 3) % 10;
        int pisoAparece = (pisoDesaparece + 10 + 6) % 10;


        for (int i = 0; i <= 6; i++) {
            pisosAnimados[i] = (pisoDesaparece + i + 10) % 10;
        }
        moverMapGroupTutorial();

        for (int i = 0; i <= 9; i++) {
            int pisoMov = (pisoDesaparece + i + 10 + 1) % 10;
            piso[pisoMov].setY(mapPosicion[i]);
        }
        piso[pisoAparece].setAlpha(ALPHA_BASE);
        piso[pisoDesaparece].setAlpha(0f);

        if (progreso < 3) {
            casObj[1].setAlpha(0f);
            casObj[2].setAlpha(0f);
            casObj[4].setAlpha(0f);
            casObj[5].setAlpha(0f);
            casObj[8].setAlpha(0f);
            casObj[12].setAlpha(0f);
        }


    } //Mueve el mapa mientras se juega.

    public void playSingOut(final int operationFinal, final int casilleroFinal) {

        final int randomInt = new Random().nextInt(400 - 250) + 250;
        final float alphaRandom = randomInt / 1000.00F;
        final int randomInt2 = new Random().nextInt(850 - 750) + 750;
        final float scale = randomInt2 / 1000.00F;
        boolean right = new Random().nextBoolean();
        int max = 125;
        int min = 100;
        final int xPos = new Random().nextInt((max - min) + 1) + min;
        long animateVelocity1 = 350;
        long animateVelocity2 = 600;
        long animateVelocity3 = 500;
        int xThreshold1 = new Random().nextInt(max);
        int xThreshold2 = new Random().nextInt(max) + 120;
        int xThreshold3 = new Random().nextInt(max) + 50;
        int animateVelocityY = 2000;
        int direction = 1;
        if (right == false) {
            direction = -1;
        }

        if (operationFinal == 1) {
            imgAnimatedSing.setImageResource(R.drawable.bubble_with_plus_sing);
        } else if (operationFinal == -1) {
            imgAnimatedSing.setImageResource(R.drawable.bubble_with_menos_sing);
        }


        imgAnimatedSing.setScaleY(scale);
        imgAnimatedSing.setScaleX(scale);
        imgAnimatedSing.setY(mapPosicion[2 + accionPiso] - utilities.MODULO_Y / 2);
        imgAnimatedSing.setX(columnaX[casilleroFinal] + moduloX / 2);
        imgAnimatedSing.setAlpha(alphaRandom);
        imgAnimatedSing.bringToFront();

        imgAnimatedSing.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator animadorY = ObjectAnimator.ofFloat(imgAnimatedSing, "y", mapPosicion[2 + accionPiso] - utilities.MODULO_Y * 4);
        animadorY.setDuration(animateVelocityY);
        animadorY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX1 = ObjectAnimator.ofFloat(imgAnimatedSing, "translationX", columnaX[casilleroFinal] + xThreshold1 * direction);
        animadorX1.setDuration(animateVelocity1);
        animadorX1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX2 = ObjectAnimator.ofFloat(imgAnimatedSing, "translationX", columnaX[casilleroFinal] - xThreshold2 * direction);
        animadorX2.setDuration(animateVelocity2);
        animadorX2.setStartDelay(animateVelocity1);
        animadorX2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX3 = ObjectAnimator.ofFloat(imgAnimatedSing, "translationX", columnaX[casilleroFinal] + xThreshold3 * direction);
        animadorX3.setDuration(animateVelocity3);
        animadorX3.setStartDelay(animateVelocity1 + animateVelocity2);
        animadorX3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imgAnimatedSing, "alpha", 0);
        fadeOut.setDuration((animateVelocityY * 2) / 3);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animadorY).with(animadorX1).with(animadorX2).with(animadorX3).with(fadeOut);
        animatorSet.start();


        animadorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgAnimatedSing.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

    }

    private void animationContinueBubbles(int jugadaColumnaX, final int comboBubble) {


        final int randomInt = new Random().nextInt(900 - 450) + 450;
        final float alphaRandom = randomInt / 1000.00F;
        final int randomInt2 = new Random().nextInt(1800 - 1000) + 1000;
        final float scale = randomInt2 / 1000.00F;
        boolean right = new Random().nextBoolean();
        int max = 150;
        int min = 0;
        final int xPos = new Random().nextInt((max - min) + 1) + min;
        long animateVelocity1 = 600;
        long animateVelocity2 = 500;
        long animateVelocity3 = 800;
        int xThreshold1 = new Random().nextInt(max);
        int xThreshold2 = new Random().nextInt(max) + 150;
        int xThreshold3 = new Random().nextInt(max) + 300;
        int animateVelocityY = new Random().nextInt((3300 - 2400) + 2400);
        int direction = 1;
        if (right == false) {
            direction = -1;
        }

        imgAnimatedCombo[comboBubble].setScaleY(scale);
        imgAnimatedCombo[comboBubble].setScaleX(scale);
        imgAnimatedCombo[comboBubble].setY(mapPosicion[2] - utilities.MODULO_Y / 2);
        imgAnimatedCombo[comboBubble].setX(columnaX[jugadaColumnaX] + xPos);
        imgAnimatedCombo[comboBubble].setAlpha(alphaRandom);
        imgAnimatedCombo[comboBubble].bringToFront();

        imgAnimatedCombo[comboBubble].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator animadorY = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "y", mapPosicion[9]);
        animadorY.setDuration(animateVelocityY);
        animadorY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX1 = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "translationX", columnaX[jugadaColumnaX] + xThreshold1 * direction);
        animadorX1.setDuration(animateVelocity1);
        animadorX1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX2 = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "translationX", columnaX[jugadaColumnaX] - xThreshold2 * direction);
        animadorX2.setDuration(animateVelocity2);
        animadorX2.setStartDelay(animateVelocity1);
        animadorX2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX3 = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "translationX", columnaX[jugadaColumnaX] + xThreshold3 * direction);
        animadorX3.setDuration(animateVelocity3);
        animadorX3.setStartDelay(animateVelocity1 + animateVelocity2);
        animadorX3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "alpha", 0);
        fadeOut.setDuration((animateVelocityY * 2) / 3);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animadorY).with(animadorX1).with(animadorX2).with(animadorX3).with(fadeOut);
        animatorSet.start();


        animadorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgAnimatedCombo[comboBubble].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
    }

    public void animationComboBubbles(int jugadaColumnaX, final int comboBubble) {


        final int randomInt = new Random().nextInt(900 - 450) + 450;
        final float alphaRandom = randomInt / 1000.00F;
        final int randomInt2 = new Random().nextInt(1200 - 800) + 800;
        final float scale = randomInt2 / 1000.00F;
        boolean right = new Random().nextBoolean();
        int max = 150;
        int min = 0;
        final int xPos = new Random().nextInt((max - min) + 1) + min;
        long animateVelocity1 = 300;
        long animateVelocity2 = 250;
        long animateVelocity3 = 400;
        int xThreshold1 = new Random().nextInt(max);
        int xThreshold2 = new Random().nextInt(max) + 75;
        int xThreshold3 = new Random().nextInt(max) + 150;
        int animateVelocityY = new Random().nextInt((1650 - 1200) + 1200);
        int direction = 1;
        if (right == false) {
            direction = -1;
        }

        imgAnimatedCombo[comboBubble].setScaleY(scale);
        imgAnimatedCombo[comboBubble].setScaleX(scale);
        imgAnimatedCombo[comboBubble].setY(mapPosicion[2] - utilities.MODULO_Y / 2);
        imgAnimatedCombo[comboBubble].setX(columnaX[jugadaColumnaX] + xPos);
        imgAnimatedCombo[comboBubble].setAlpha(alphaRandom);
        imgAnimatedCombo[comboBubble].bringToFront();

        imgAnimatedCombo[comboBubble].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator animadorY = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "y", mapPosicion[7]);
        animadorY.setDuration(animateVelocityY);
        animadorY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX1 = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "translationX", columnaX[jugadaColumnaX] + xThreshold1 * direction);
        animadorX1.setDuration(animateVelocity1);
        animadorX1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX2 = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "translationX", columnaX[jugadaColumnaX] - xThreshold2 * direction);
        animadorX2.setDuration(animateVelocity2);
        animadorX2.setStartDelay(animateVelocity1);
        animadorX2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX3 = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "translationX", columnaX[jugadaColumnaX] + xThreshold3 * direction);
        animadorX3.setDuration(animateVelocity3);
        animadorX3.setStartDelay(animateVelocity1 + animateVelocity2);
        animadorX3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imgAnimatedCombo[comboBubble], "alpha", 0);
        fadeOut.setDuration((animateVelocityY * 2) / 3);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animadorY).with(animadorX1).with(animadorX2).with(animadorX3).with(fadeOut);
        animatorSet.start();


        animadorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgAnimatedCombo[comboBubble].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
    }

    private void animationText(int typeText) {

        if (typeText >= 2) {
            comboActual = 1;
        }

        float destiny = mapPosicion[6] + utilities.MODULO_Y / 2;
        txtCombo[comboActual].bringToFront();
        txtCombo[comboActual].setAlpha(ALPHA_ANTERIOR);
        if (typeText == 1) {

            txtCombo[comboActual].setText("combo x" + Integer.toString((gameManager.player.getCombo() / utilities.COMBO_MULTIPLE) + 1));
            txtCombo[comboActual].setTextColor(BLUE);
            txtCombo[comboActual].setTextSize(20);
            txtCombo[comboActual].bringToFront();
            destiny = mapPosicion[6] + utilities.MODULO_Y / 2;
            txtComboFijo.setAlpha(0f);
            txtComboFijo.setText("combo x" + Integer.toString((gameManager.player.getCombo() / utilities.COMBO_MULTIPLE) + 1));
            txtComboFijo.setTextColor(WHITE);
            txtComboFijo.setTextSize(20);
            txtComboFijo.setTextSize(20);


        } else if (typeText >= 2) {

            if (typeText == 2) {
                txtCombo[comboActual].setText(Long.toString(gameManager.player.getMaxBubble()));
                txtCombo[comboActual].setTextSize(50);
            } else if (typeText == 3) {
                txtCombo[comboActual].setText(R.string.level_up);
                txtCombo[comboActual].setTextSize(30);
            } else if (typeText == 4) {
                txtCombo[comboActual].setText("+" + Integer.toString(RUSH_UP));
                txtCombo[comboActual].setTextSize(50);
            }
            txtCombo[comboActual].setTextColor(RED);
            destiny = mapPosicion[4];

        }

        txtCombo[comboActual].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        txtCombo[comboActual].setScaleX(1);
        txtCombo[comboActual].setScaleY(1);
        txtCombo[comboActual].setY(mapPosicion[7]);

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(txtCombo[comboActual], "textColor",
                LTGRAY, BLUE);
        colorAnim.setDuration(ANIMATION_VELOCITY * 2);
        colorAnim.setInterpolator(new BounceInterpolator());
        colorAnim.setEvaluator(new ArgbEvaluator());

        ObjectAnimator moveY = ObjectAnimator.ofFloat(txtCombo[comboActual], "Y", destiny);
        moveY.setDuration(ANIMATION_VELOCITY);

        ObjectAnimator comboScale = ObjectAnimator.ofFloat(txtCombo[comboActual], "scaleX", 50);
        comboScale.setDuration(ANIMATION_VELOCITY * 2);
        comboScale.setStartDelay(ANIMATION_VELOCITY * 3);
        comboScale.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator comboScaleY = ObjectAnimator.ofFloat(txtCombo[comboActual], "scaleY", 0.05f);
        comboScaleY.setDuration(ANIMATION_VELOCITY * 1);
        comboScaleY.setStartDelay(ANIMATION_VELOCITY * 4);
        comboScaleY.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator comboFadeOut = ObjectAnimator.ofFloat(txtCombo[comboActual], "alpha", 0f);
        comboFadeOut.setDuration(ANIMATION_VELOCITY * 3);
        comboFadeOut.setStartDelay(ANIMATION_VELOCITY);
        comboFadeOut.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(colorAnim).with(moveY).with(comboFadeOut).with(comboScale).with(comboScaleY);
        animatorSet.start();

        moveY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                txtComboFijo.setAlpha(0.3f);
            }
        });


        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                txtCombo[comboActual].setLayerType(View.LAYER_TYPE_NONE, null);
                txtCombo[comboActual].setScaleX(1);
                txtCombo[comboActual].setScaleY(1);
                txtCombo[comboActual].setAlpha(0);
                txtComboFijo.bringToFront();

            }
        });

    }

    public void animationCombo(final int jugadaColumnaX) {

        soundPlayer.playSoundCombo(this);
        soundPlayer.playSoundBubble(MainActivity.this, 0);

        final int comboBubble = 0 + (comboActual * 8);

        animationComboBubbles(jugadaColumnaX, comboBubble);


        if (gameManager.player.getCombo() >= utilities.COMBO_MULTIPLE && gameManager.player.getCombo() % (utilities.COMBO_MULTIPLE / 2) == 0) {

            animationText(1);

        }

        //if (gameManager.player.getCombo() > 4){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final int comboBubble1 = 1 + (comboActual * 8);
                final int columnaCombo1 = jugadaColumnaX;

                animationComboBubbles(columnaCombo1, comboBubble1);

                //if (gameManager.player.getCombo() > 5) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        final int comboBubble2 = 2 + (comboActual * 8);
                        final int columnaCombo2 = jugadaColumnaX;

                        animationComboBubbles(columnaCombo2, comboBubble2);


                        //if (gameManager.player.getCombo() > 4) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                final int comboBubble3 = 3 + (comboActual * 8);
                                final int columnaCombo3 = jugadaColumnaX;

                                animationComboBubbles(columnaCombo3, comboBubble3);

                                if (gameManager.player.getCombo() > 15) {

                                    final int comboBubble6 = 6 + (comboActual * 8);
                                    final int columnaCombo6 = jugadaColumnaX;
                                    animationComboBubbles(columnaCombo6, comboBubble6);

                                }

                                if (gameManager.player.getCombo() > 4) {

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            final int comboBubble4 = 4 + (comboActual * 8);
                                            final int columnaCombo4 = jugadaColumnaX;

                                            animationComboBubbles(columnaCombo4, comboBubble4);

                                            if (gameManager.player.getCombo() > 9) {

                                                final int comboBubble5 = 5 + (comboActual * 8);
                                                final int columnaCombo5 = jugadaColumnaX;
                                                animationComboBubbles(columnaCombo5, comboBubble5);
                                            }

                                            if (gameManager.player.getCombo() > 20) {

                                                final int comboBubble7 = 7 + (comboActual * 8);
                                                final int columnaCombo7 = jugadaColumnaX;
                                                animationComboBubbles(columnaCombo7, comboBubble7);
                                            }

                                        }
                                    }, ANIMATION_VELOCITY / 5);
                                }

                            }
                        }, ANIMATION_VELOCITY / 5);
                        //}

                    }
                }, ANIMATION_VELOCITY / 5);
                //}
            }
        }, ANIMATION_VELOCITY / 5);


        //}

    }

    public void animationCoinsBubbles(int jugadaColumnaX, final int comboBubble) {


        final int randomInt = new Random().nextInt(1000 - 600) + 600;
        final float alphaRandom = randomInt / 1000.00F;
        final int randomInt2 = new Random().nextInt(1200 - 800) + 800;
        final float scale = randomInt2 / 1000.00F;
        boolean right = new Random().nextBoolean();
        int max = 150;
        int min = 0;
        final int xPos = new Random().nextInt((max - min) + 1) + min;
        long animateVelocity1 = 600;
        long animateVelocity2 = 500;
        long animateVelocity3 = 800;
        int xThreshold1 = new Random().nextInt(max);
        int xThreshold2 = new Random().nextInt(max) + 75;
        int xThreshold3 = new Random().nextInt(max) + 150;
        int animateVelocityY = new Random().nextInt((3200 - 2400) + 2400);
        int direction = 1;
        if (right == false) {
            direction = -1;
        }

        if (jugadaColumnaX == 5) {
            imgAnimatedCoin[comboBubble].setY(mapPosicion[0] - utilities.MODULO_Y / 2);
            imgAnimatedCoin[comboBubble].setX(columnaX[1] + xPos);
            jugadaColumnaX = 1;
        } else {
            imgAnimatedCoin[comboBubble].setY(mapPosicion[2] - utilities.MODULO_Y / 2);
            imgAnimatedCoin[comboBubble].setX(columnaX[jugadaColumnaX] + xPos);
        }

        imgAnimatedCoin[comboBubble].setScaleY(scale);
        imgAnimatedCoin[comboBubble].setScaleX(scale);

        imgAnimatedCoin[comboBubble].setAlpha(alphaRandom);
        imgAnimatedCoin[comboBubble].bringToFront();
        imgAnimatedCoin[comboBubble].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator animadorY = ObjectAnimator.ofFloat(imgAnimatedCoin[comboBubble], "y", mapPosicion[7]);
        animadorY.setDuration(animateVelocityY);
        animadorY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX1 = ObjectAnimator.ofFloat(imgAnimatedCoin[comboBubble], "translationX", columnaX[jugadaColumnaX] + xThreshold1 * direction);
        animadorX1.setDuration(animateVelocity1);
        animadorX1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX2 = ObjectAnimator.ofFloat(imgAnimatedCoin[comboBubble], "translationX", columnaX[jugadaColumnaX] - xThreshold2 * direction);
        animadorX2.setDuration(animateVelocity2);
        animadorX2.setStartDelay(animateVelocity1);
        animadorX2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX3 = ObjectAnimator.ofFloat(imgAnimatedCoin[comboBubble], "translationX", columnaX[jugadaColumnaX] + xThreshold3 * direction);
        animadorX3.setDuration(animateVelocity3);
        animadorX3.setStartDelay(animateVelocity1 + animateVelocity2);
        animadorX3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imgAnimatedCoin[comboBubble], "alpha", 0);
        fadeOut.setDuration((animateVelocityY * 2) / 3);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animadorY).with(animadorX1).with(animadorX2).with(animadorX3).with(fadeOut);
        animatorSet.start();


        animadorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgAnimatedCoin[comboBubble].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
    }

    public void animationCoins(final int jugadaColumnaX, final int difCredits) {

        soundPlayer.playSoundCombo(this);


        animationCoinsBubbles(jugadaColumnaX, 0);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final int columnaCombo1 = jugadaColumnaX;

                animationCoinsBubbles(columnaCombo1, 1);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        final int columnaCombo2 = jugadaColumnaX;

                        animationCoinsBubbles(columnaCombo2, 2);


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                final int columnaCombo3 = jugadaColumnaX;

                                animationCoinsBubbles(columnaCombo3, 3);


                                final int columnaCombo6 = jugadaColumnaX;
                                animationCoinsBubbles(columnaCombo6, 6);


                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        final int columnaCombo4 = jugadaColumnaX;

                                        animationCoinsBubbles(columnaCombo4, 4);


                                        final int columnaCombo5 = jugadaColumnaX;
                                        animationCoinsBubbles(columnaCombo5, 5);

                                    }
                                }, ANIMATION_VELOCITY / 5);
                            }

                        }, ANIMATION_VELOCITY / 5);
                    }

                }, ANIMATION_VELOCITY / 5);
            }
        }, ANIMATION_VELOCITY / 5);

    }

    public void animationBonusGetUp(int jugadaColumnaXimportada, final int getUp, final int typeBonus) {

        float xEndAnimation = columnaX[0];

        soundPlayer.playSoundCombo(this);

        switch (typeBonus) {
            case 1:
                xEndAnimation = columnaX[0];
                bonusSwipeAnimacion[getUp].setImageResource(R.drawable.ic_bubble_swipe);
                break;
            case 2:
                xEndAnimation = columnaX[1];
                bonusSwipeAnimacion[getUp].setImageResource(R.drawable.ic_bubble_plus);
                break;
            case 7:
                xEndAnimation = columnaX[2];
                bonusSwipeAnimacion[getUp].setImageResource(R.drawable.ic_bubble_exchange);
                break;
        }

        bonusSwipeAnimacion[getUp].setX(columnaX[jugadaColumnaX]);
        bonusSwipeAnimacion[getUp].bringToFront();
        bonusSwipeAnimacion[getUp].setAlpha(ALPHA_BASE);
        bonusSwipeAnimacion[getUp].setY(mapPosicion[3]);

        bonusSwipeAnimacion[getUp].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator fadeInBonus = ObjectAnimator.ofFloat(bonusSwipeAnimacion[getUp], "alpha", ALPHA_BASE);
        fadeInBonus.setDuration(ANIMATION_VELOCITY * 3);
        fadeInBonus.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animadorY = ObjectAnimator.ofFloat(bonusSwipeAnimacion[getUp], "y", buttonPosY);
        animadorY.setDuration(ANIMATION_VELOCITY * 3);
        animadorY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorX = ObjectAnimator.ofFloat(bonusSwipeAnimacion[getUp], "X", xEndAnimation);
        animadorX.setDuration(ANIMATION_VELOCITY * 3);
        animadorX.setInterpolator(new AccelerateDecelerateInterpolator());


        animadorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                bonusSwipeAnimacion[getUp].setLayerType(View.LAYER_TYPE_NONE, null);
                bonusSwipeAnimacion[getUp].setAlpha(0f);
                //Todo Sonido recoleccion bonus
                if (typeBonus == 1) {
                    if (getUp == 4) {
                        actualBonusSwipe = gameManager.player.getBonusSwipe();
                    } else {
                        actualBonusSwipe++;
                    }
                    txtBonusSwipe.setBackgroundResource(R.drawable.ic_bubble_swipe);
                    txtBonusSwipe.setText(Integer.toString(actualBonusSwipe));
                    txtBonusSwipe.bringToFront();
                } else if (typeBonus == 2) {
                    if (getUp == 4) {
                        actualBonusPlus = gameManager.player.getBonusPlus();
                    } else {
                        actualBonusPlus++;
                    }
                    txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus);
                    txtBonusPlus.setText(Integer.toString(actualBonusPlus));
                    txtBonusPlus.bringToFront();
                } else if (typeBonus == 7) {
                    if (getUp == 4) {
                        actualBonusExchange = gameManager.player.getBonusExchange();
                    } else {
                        actualBonusExchange++;
                    }
                    txtBonusExchange.setBackgroundResource(R.drawable.ic_bubble_exchange);
                    txtBonusExchange.setText(Integer.toString(actualBonusExchange));
                    txtBonusExchange.bringToFront();
                }
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeInBonus).with(animadorX).with(animadorY);
        animatorSet.start();

    }

    public void moverMapGroup() {

        isAnimationMapRunning = true;
        checkAction();

        for (int i = 0; i <= 6; i++) {
            piso[pisosAnimados[i]].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        int rushUp = 1;

        ObjectAnimator animadorFadeOut = ObjectAnimator.ofFloat(piso[pisosAnimados[0]], "alpha", 0f);
        animadorFadeOut.setDuration(ANIMATION_VELOCITY * rushUp);
        animadorFadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador0 = ObjectAnimator.ofFloat(piso[pisosAnimados[0]], "y", mapPosicion[0] + utilities.MODULO_Y);
        animador0.setDuration(ANIMATION_VELOCITY * rushUp);
        animador0.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador1 = ObjectAnimator.ofFloat(piso[pisosAnimados[1]], "y", mapPosicion[0]);
        animador1.setDuration(ANIMATION_VELOCITY * rushUp);
        animador1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador2 = ObjectAnimator.ofFloat(piso[pisosAnimados[2]], "y", mapPosicion[1]);
        animador2.setDuration(ANIMATION_VELOCITY * rushUp);
        animador2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador3 = ObjectAnimator.ofFloat(piso[pisosAnimados[3]], "y", mapPosicion[2]);
        animador3.setDuration(ANIMATION_VELOCITY * rushUp);
        animador3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador4 = ObjectAnimator.ofFloat(piso[pisosAnimados[4]], "y", mapPosicion[3]);
        animador4.setDuration(ANIMATION_VELOCITY * rushUp);
        animador4.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador5 = ObjectAnimator.ofFloat(piso[pisosAnimados[5]], "y", mapPosicion[4]);
        animador5.setDuration(ANIMATION_VELOCITY * rushUp);
        animador5.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador6 = ObjectAnimator.ofFloat(piso[pisosAnimados[6]], "y", mapPosicion[5]);
        animador6.setDuration(ANIMATION_VELOCITY * rushUp);
        animador6.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorFadeIn = ObjectAnimator.ofFloat(piso[pisosAnimados[6]], "alpha", ALPHA_BASE);
        animadorFadeIn.setDuration((ANIMATION_VELOCITY * 2) * rushUp);
        animadorFadeIn.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animador0).with(animador1).with(animador2).with(animador3).with(animador4).with(animador5).with(animador6).with(animadorFadeOut).with(animadorFadeIn);


        animador0.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[0]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[1]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[2]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[3]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador4.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[4]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[5]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animadorFadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[6]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });


        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                isAnimationMapRunning = false;

            }
        });

    }

    private void animateInUserProfile() {

        ObjectAnimator animador1 = ObjectAnimator.ofFloat(txtUserNameProfile, "translationX", 0);
        animador1.setDuration(ANIMATION_VELOCITY * 3);
        animador1.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animador2 = ObjectAnimator.ofFloat(txtUserLevel, "translationX", 0);
        animador2.setDuration(ANIMATION_VELOCITY * 3);
        animador2.setStartDelay(ANIMATION_VELOCITY / 4);
        animador2.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animador3 = ObjectAnimator.ofFloat(txtCredits, "translationX", 0);
        animador3.setDuration(ANIMATION_VELOCITY * 3);
        animador3.setStartDelay(ANIMATION_VELOCITY / 2);
        animador3.setInterpolator(new OvershootInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animador1).with(animador2).with(animador3);
        animatorSet.start();

    }

    private void moverMapGroupInicial(int pisoInicial) {

        playability(false);

        final int pisoAnimacionInicial[] = new int[4];

        for (int i = 0; i < 4; i++) {
            pisoAnimacionInicial[i] = (pisoInicial + i + 10) % 10;
        }

        ObjectAnimator animador1 = ObjectAnimator.ofFloat(piso[pisoAnimacionInicial[0]], "y", mapPosicion[2]);
        animador1.setDuration(ANIMATION_VELOCITY * 2);
        animador1.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animador2 = ObjectAnimator.ofFloat(piso[pisoAnimacionInicial[1]], "y", mapPosicion[3]);
        animador2.setDuration(ANIMATION_VELOCITY * 2);
        animador2.setStartDelay(ANIMATION_VELOCITY / 8);
        animador2.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animador3 = ObjectAnimator.ofFloat(piso[pisoAnimacionInicial[2]], "y", mapPosicion[4]);
        animador3.setDuration(ANIMATION_VELOCITY * 2);
        animador3.setStartDelay(ANIMATION_VELOCITY / 6);
        animador3.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animador4 = ObjectAnimator.ofFloat(piso[pisoAnimacionInicial[3]], "y", mapPosicion[5]);
        animador4.setDuration(ANIMATION_VELOCITY * 2);
        animador4.setStartDelay(ANIMATION_VELOCITY / 4);
        animador4.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animador5 = ObjectAnimator.ofFloat(imgCasilleroSeleccionado, "y", mapPosicion[2]);
        animador5.setDuration(ANIMATION_VELOCITY * 2);
        animador5.setInterpolator(new OvershootInterpolator());


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animador1).with(animador2).with(animador3).with(animador4).with(animador5);
        animatorSet.setStartDelay(ANIMATION_VELOCITY);

        animador1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoAnimacionInicial[0]].setLayerType(View.LAYER_TYPE_NONE, null);
                imgRotarShinning.setY(mapPosicion[2] - 4);
            }
        });

        animador2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoAnimacionInicial[1]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoAnimacionInicial[2]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoAnimacionInicial[3]].setLayerType(View.LAYER_TYPE_NONE, null);
                if (isTutorialRunning == false) {
                    if (gameManager.isGameOVer() == false) {
                        playability(true);
                    }
                }
            }
        });

        animatorSet.start();

    }

    public void moverMapGroupTutorial() {

        for (int i = 0; i <= 6; i++) {
            piso[pisosAnimados[i]].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        ObjectAnimator animadorFadeOut = ObjectAnimator.ofFloat(piso[pisosAnimados[0]], "alpha", 0f);
        animadorFadeOut.setDuration(ANIMATION_VELOCITY * 3);
        animadorFadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador0 = ObjectAnimator.ofFloat(piso[pisosAnimados[0]], "y", mapPosicion[0] + utilities.MODULO_Y);
        animador0.setDuration(ANIMATION_VELOCITY * 3);
        animador0.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador1 = ObjectAnimator.ofFloat(piso[pisosAnimados[1]], "y", mapPosicion[0]);
        animador1.setDuration(ANIMATION_VELOCITY * 3);
        animador1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador2 = ObjectAnimator.ofFloat(piso[pisosAnimados[2]], "y", mapPosicion[1]);
        animador2.setDuration(ANIMATION_VELOCITY * 3);
        animador2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador3 = ObjectAnimator.ofFloat(piso[pisosAnimados[3]], "y", mapPosicion[2]);
        animador3.setDuration(ANIMATION_VELOCITY * 3);
        animador3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador4 = ObjectAnimator.ofFloat(piso[pisosAnimados[4]], "y", mapPosicion[3]);
        animador4.setDuration(ANIMATION_VELOCITY * 3);
        animador4.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador5 = ObjectAnimator.ofFloat(piso[pisosAnimados[5]], "y", mapPosicion[4]);
        animador5.setDuration(ANIMATION_VELOCITY * 3);
        animador5.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animador6 = ObjectAnimator.ofFloat(piso[pisosAnimados[6]], "y", mapPosicion[5]);
        animador6.setDuration(ANIMATION_VELOCITY * 3);
        animador6.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animadorFadeIn = ObjectAnimator.ofFloat(piso[pisosAnimados[6]], "alpha", ALPHA_BASE);
        animadorFadeIn.setDuration(ANIMATION_VELOCITY * 3);
        animadorFadeIn.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animador0).with(animador1).with(animador2).with(animador3).with(animador4).with(animador5).with(animador6).with(animadorFadeOut).with(animadorFadeIn);

        animador0.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[0]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[1]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[2]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[3]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador4.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[4]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animador5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[5]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animadorFadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisosAnimados[6]].setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animatorSet.start();

    }

    public void moveRowRight(final int pisoMap, float mov, int duracion) {

        piso[pisoMap].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        final int multPiso = ((pisoMap) + 10) % 10;

        ObjectAnimator animador1 = ObjectAnimator.ofFloat(casObj[1 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[1]);
        ObjectAnimator animador2 = ObjectAnimator.ofFloat(casObj[2 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[2]);
        ObjectAnimator animador3 = ObjectAnimator.ofFloat(casObj[3 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[3]);
        ObjectAnimator animador4 = ObjectAnimator.ofFloat(casObj[4 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[4]);
        ObjectAnimator animador5 = ObjectAnimator.ofFloat(casObj[5 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[4] + moduloX * 4);
        ObjectAnimator animador7 = ObjectAnimator.ofFloat(casObj[5 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[0]);

        animador1.setDuration(duracion);
        animador1.setInterpolator(new OvershootInterpolator());
        animador1.setStartDelay((duracion / 8) * 5);
        animador2.setDuration(duracion);
        animador2.setInterpolator(new OvershootInterpolator());
        animador2.setStartDelay((duracion / 8) * 4);
        animador3.setDuration(duracion);
        animador3.setInterpolator(new OvershootInterpolator());
        animador3.setStartDelay((duracion / 8) * 3);
        animador4.setDuration(duracion);
        animador4.setInterpolator(new OvershootInterpolator());
        animador4.setStartDelay((duracion / 8) * 2);
        animador5.setDuration(duracion / 2);
        animador5.setInterpolator(new OvershootInterpolator());
        animador7.setDuration(duracion);
        animador7.setInterpolator(new OvershootInterpolator());
        animador7.setStartDelay(duracion + 2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animador1).with(animador2).with(animador3).with(animador4).with(animador5).with(animador7);

        animatorSet.start();
        animador7.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoMap].setLayerType(View.LAYER_TYPE_NONE, null);
                //ubicarCasilleros();
            }
        });

        animador5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[5 + multPiso * NUM_COLUMNAS_BASE].setX(columnaX[0] - moduloX * 4);
            }
        });

    }

    public void moveRowLeft(final int pisoMap, float mov, int duracion) {

        piso[pisoMap].setLayerType(View.LAYER_TYPE_HARDWARE, null);

        final int multPiso = ((pisoMap) + 10) % 10;

        ObjectAnimator animador1 = ObjectAnimator.ofFloat(casObj[5 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[3]);
        ObjectAnimator animador2 = ObjectAnimator.ofFloat(casObj[4 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[2]);
        ObjectAnimator animador3 = ObjectAnimator.ofFloat(casObj[3 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[1]);
        ObjectAnimator animador4 = ObjectAnimator.ofFloat(casObj[2 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[0]);
        ObjectAnimator animador5 = ObjectAnimator.ofFloat(casObj[1 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[0] - moduloX * 4);
        ObjectAnimator animador6 = ObjectAnimator.ofFloat(casObj[1 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[4] + moduloX * 2);
        ObjectAnimator animador7 = ObjectAnimator.ofFloat(casObj[1 + multPiso * NUM_COLUMNAS_BASE], "x", columnaX[4]);

        animador1.setDuration(duracion);
        animador1.setInterpolator(new OvershootInterpolator());
        animador1.setStartDelay((duracion / 8) * 5);
        animador2.setDuration(duracion);
        animador2.setInterpolator(new OvershootInterpolator());
        animador2.setStartDelay((duracion / 8) * 4);
        animador3.setDuration(duracion);
        animador3.setInterpolator(new OvershootInterpolator());
        animador3.setStartDelay((duracion / 8) * 3);
        animador4.setDuration(duracion);
        animador4.setInterpolator(new OvershootInterpolator());
        animador4.setStartDelay((duracion / 8) * 2);
        animador5.setDuration(duracion);
        animador5.setInterpolator(new OvershootInterpolator());
        animador6.setDuration(1);
        animador6.setInterpolator(new OvershootInterpolator());
        animador6.setStartDelay(duracion);
        animador7.setDuration(duracion);
        animador7.setInterpolator(new OvershootInterpolator());
        animador7.setStartDelay(duracion + 2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animador1).with(animador2).with(animador3).with(animador4).with(animador5).with(animador7);

        animatorSet.start();

        animador5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                casObj[1 + multPiso * NUM_COLUMNAS_BASE].setX(columnaX[4] + moduloX * 4);
            }
        });

        animador7.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                piso[pisoMap].setLayerType(View.LAYER_TYPE_NONE, null);
                //ubicarCasilleros();
            }
        });
    }

    public void animateShine(int jugadaColumnaX, int mapPosition) {

        imgCasilleroResplandor.setAlpha(1f);
        imgCasilleroResplandor.setY(mapPosicion[mapPosition]);
        imgCasilleroResplandor.setX(columnaX[gameManager.player.getLastColumnPlay()]);
        imgCasilleroSeleccionado.setY(mapPosicion[mapPosition]);
        imgCasilleroSeleccionado.setX(columnaX[gameManager.player.getLastColumnPlay()]);
        imgRotarShinning.setY(mapPosicion[mapPosition] - 4);
        imgRotarShinning.setX(columnaX[gameManager.player.getLastColumnPlay()] - 4);


        ObjectAnimator fadeOutResplandor = ObjectAnimator.ofFloat(imgCasilleroResplandor, "alpha", 0f);
        fadeOutResplandor.setDuration(ANIMATION_VELOCITY);

        ObjectAnimator movY = ObjectAnimator.ofFloat(imgCasilleroResplandor, "y", mapPosicion[mapPosition - 1]);
        movY.setDuration(ANIMATION_VELOCITY);
        movY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator movYSelec = ObjectAnimator.ofFloat(imgCasilleroSeleccionado, "y", mapPosicion[mapPosition - 1]);
        movYSelec.setDuration(ANIMATION_VELOCITY);
        movYSelec.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator movYRotarShinning = ObjectAnimator.ofFloat(imgRotarShinning, "y", mapPosicion[2] - 4);
        movYRotarShinning.setDuration(ANIMATION_VELOCITY);
        movYRotarShinning.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(movY).with(fadeOutResplandor).with(movYSelec).with(movYRotarShinning);
        animatorSet.start();


    }

    private void animateExplode (int jugadaColumnaX, int mapPosition){

        imgExplode.setScaleY(1);
        imgExplode.setScaleX(1);
        imgExplode.setAlpha(0.3f);
        imgExplode.bringToFront();
        imgExplode.setY(mapPosicion[mapPosition] - 4);
        imgExplode.setX(columnaX[gameManager.player.getLastColumnPlay()] - 4);
        imgCasilleroResplandor.setAlpha(1f);
        imgCasilleroResplandor.setY(mapPosicion[mapPosition]);
        imgCasilleroResplandor.setX(columnaX[gameManager.player.getLastColumnPlay()]);
        imgCasilleroSeleccionado.setY(mapPosicion[mapPosition]);
        imgCasilleroSeleccionado.setX(columnaX[gameManager.player.getLastColumnPlay()]);
        imgRotarShinning.setY(mapPosicion[mapPosition] - 4);
        imgRotarShinning.setX(columnaX[gameManager.player.getLastColumnPlay()] - 4);

        imgGradientCombo.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgGradientCombo.setAlpha(0.7f);
            }
        }, ANIMATION_VELOCITY/2);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imgGradientCombo, "alpha", 0);
        fadeOut.setDuration(ANIMATION_VELOCITY);
        fadeOut.setStartDelay(ANIMATION_VELOCITY/2);
        fadeOut.setInterpolator(new FastOutSlowInInterpolator());

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgGradientCombo.setLayerType(View.LAYER_TYPE_NONE, null);
            }

        });

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgExplode, "scaleY", 2f);
        scaleY.setDuration(ANIMATION_VELOCITY/5);
        scaleY.setInterpolator(new OvershootInterpolator());

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgExplode, "scaleX", 2f);
        scaleX.setDuration(ANIMATION_VELOCITY/5);
        scaleX.setInterpolator(new OvershootInterpolator());

        ObjectAnimator fadeOutExplode = ObjectAnimator.ofFloat(imgExplode, "alpha", 0f);
        fadeOutExplode.setStartDelay(ANIMATION_VELOCITY/10);
        fadeOutExplode.setDuration((ANIMATION_VELOCITY/10));
        fadeOutExplode.setInterpolator(new AnticipateInterpolator());

        ObjectAnimator movYExplode = ObjectAnimator.ofFloat(imgCasilleroResplandor, "y", mapPosicion[mapPosition - 1]);
        movYExplode.setDuration(ANIMATION_VELOCITY);
        movYExplode.setInterpolator(new AccelerateDecelerateInterpolator());


        ObjectAnimator fadeOutResplandor = ObjectAnimator.ofFloat(imgCasilleroResplandor, "alpha", 0f);
        fadeOutResplandor.setDuration(ANIMATION_VELOCITY);

        ObjectAnimator movY = ObjectAnimator.ofFloat(imgCasilleroResplandor, "y", mapPosicion[mapPosition - 1]);
        movY.setDuration(ANIMATION_VELOCITY);
        movY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator movYSelec = ObjectAnimator.ofFloat(imgCasilleroSeleccionado, "y", mapPosicion[mapPosition - 1]);
        movYSelec.setDuration(ANIMATION_VELOCITY);
        movYSelec.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator movYRotarShinning = ObjectAnimator.ofFloat(imgRotarShinning, "y", mapPosicion[2] - 4);
        movYRotarShinning.setDuration(ANIMATION_VELOCITY);
        movYRotarShinning.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(movY).with(fadeOutResplandor).with(movYSelec).with(movYRotarShinning).with(scaleY).with(scaleX).with(fadeOutExplode).with(movYExplode).with(fadeOut);
        animatorSet.start();

    }

    public void animateShineTutorial(int jugadaColumnaX) {

        imgCasilleroResplandor.setAlpha(1f);
        imgCasilleroResplandor.setY(mapPosicion[3]);
        imgCasilleroResplandor.setX(columnaX[jugadaColumnaX]);
        imgCasilleroSeleccionado.setY(mapPosicion[3]);
        imgCasilleroSeleccionado.setX(columnaX[jugadaColumnaX]);


        ObjectAnimator fadeOutResplandor = ObjectAnimator.ofFloat(imgCasilleroResplandor, "alpha", 0f);
        fadeOutResplandor.setDuration(ANIMATION_VELOCITY * 3);

        ObjectAnimator movY = ObjectAnimator.ofFloat(imgCasilleroResplandor, "y", mapPosicion[2]);
        movY.setDuration(ANIMATION_VELOCITY * 3);
        movY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator movYSelec = ObjectAnimator.ofFloat(imgCasilleroSeleccionado, "y", mapPosicion[2]);
        movYSelec.setDuration(ANIMATION_VELOCITY * 3);
        movYSelec.setInterpolator(new AccelerateDecelerateInterpolator());


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(movY).with(fadeOutResplandor).with(movYSelec);
        animatorSet.start();


    }

    private void getUpBonusAdmin(int casSeleccionado) {

        if (gameManager.player.getSwipeGetUp() > 0 || gameManager.player.getPlusGetUp() > 0 || gameManager.player.getExchangeGetUp() > 0) {

            isGetUpAnimationFinish = false;

            int preTypeBonus = 1;

            if (gameManager.player.getPlusGetUp() > 0) {
                preTypeBonus = 2;
            } else if (gameManager.player.getExchangeGetUp() > 0) {
                preTypeBonus = 7;
            }

            final int typeBonus = preTypeBonus;

            casObj[casSeleccionado].setBackgroundResource(R.drawable.ic_fondo_casillero_base_vector);

            final Handler handler2 = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isGetUpAnimationFinish = true;
                }
            }, ANIMATION_VELOCITY * 5);


            if (gameManager.player.getBonusSwipe() >= 1 || gameManager.player.getBonusPlus() >= 1 || gameManager.player.getExchangeGetUp() >= 1) {

                if (isTutorialRunning == false) {
                    checkTutorialViews(typeBonus);
                }

                animationBonusGetUp(jugadaColumnaX, 0, typeBonus);

                if (gameManager.player.getSwipeGetUp() > 1 || gameManager.player.getPlusGetUp() > 1 || gameManager.player.getExchangeGetUp() > 1) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationBonusGetUp(jugadaColumnaX, 1, typeBonus);

                        }
                    }, ANIMATION_VELOCITY);
                }
                if (gameManager.player.getSwipeGetUp() > 2 || gameManager.player.getPlusGetUp() > 2 || gameManager.player.getExchangeGetUp() > 2) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationBonusGetUp(jugadaColumnaX, 2, typeBonus);
                        }
                    }, ANIMATION_VELOCITY * 2);
                }
                if (gameManager.player.getSwipeGetUp() > 3 || gameManager.player.getPlusGetUp() > 3 || gameManager.player.getExchangeGetUp() > 3) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationBonusGetUp(jugadaColumnaX, 3, typeBonus);
                        }
                    }, ANIMATION_VELOCITY * 3);
                }
                if (gameManager.player.getSwipeGetUp() > 4 || gameManager.player.getPlusGetUp() > 4 || gameManager.player.getExchangeGetUp() > 4) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationBonusGetUp(jugadaColumnaX, 4, typeBonus);
                        }
                    }, ANIMATION_VELOCITY * 4);
                }
            }


        } else {
            if (isGetUpAnimationFinish) {
                actualBonusSwipe = gameManager.player.getBonusSwipe();
                actualBonusPlus = gameManager.player.getBonusPlus();
                actualBonusExchange = gameManager.player.getBonusExchange();
                if (actualBonusSwipe == 0) {
                    txtBonusSwipe.setText("");
                } else {
                    txtBonusSwipe.setText(Integer.toString(actualBonusSwipe));
                }
                if (actualBonusPlus == 0) {
                    txtBonusPlus.setText("");
                } else {
                    txtBonusPlus.setText(Integer.toString(actualBonusPlus));
                }
                if (actualBonusExchange == 0) {
                    txtBonusExchange.setText("");
                } else {
                    txtBonusExchange.setText(Integer.toString(actualBonusExchange));
                }
            }
        }


    }

    public void marcarJugada(int casSeleccionado, int casAnterior) {

        if (gameManager.isAnEvent()){
            if(gameManager.getColumnBomb() >= 0){

                animateExplode(gameManager.player.getLastColumnPlay() + 1, 3);

            } else {

                animateShine(gameManager.player.getLastColumnPlay() + 1, 3);

            }
        } else {

            animateShine(gameManager.player.getLastColumnPlay() + 1, 3);

        }

        casObj[casSeleccionado].setAlpha(1f);
        casObj[casSeleccionado].setTextColor(RED);
        casillerosObsoletos(casSeleccionado);
        final int casSel = casSeleccionado;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                casillerosObsoletos(casSel);
            }
        }, ANIMATION_VELOCITY * 2);


        playArrows();


    } // Marca la jugada en la fila.

    public void casillerosObsoletos(int casSeleccionado) {

        if (casSeleccionado % NUM_COLUMNAS_BASE == 1) {
            casObj[casSeleccionado + 4].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 3].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 2].setAlpha(ALPHA_ATRAS);
            if (casSeleccionado < 64) {
                casObj[casSeleccionado + 10].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 9].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 11].setAlpha(ALPHA_ATRAS);
            } else {
                casObj[3].setAlpha(ALPHA_ATRAS);
                casObj[4].setAlpha(ALPHA_ATRAS);
                casObj[5].setAlpha(ALPHA_ATRAS);
            }
        }
        if (casSeleccionado % NUM_COLUMNAS_BASE == 2) {
            casObj[casSeleccionado - 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 3].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 2].setAlpha(ALPHA_ATRAS);
            if (casSeleccionado < 64) {
                casObj[casSeleccionado + 10].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 9].setAlpha(ALPHA_ATRAS);
            } else {
                casObj[4].setAlpha(ALPHA_ATRAS);
                casObj[5].setAlpha(ALPHA_ATRAS);
            }
        }
        if (casSeleccionado % NUM_COLUMNAS_BASE == 3) {
            casObj[casSeleccionado - 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado - 2].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 2].setAlpha(ALPHA_ATRAS);
            if (casSeleccionado < 64) {
                casObj[casSeleccionado + 9].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 5].setAlpha(ALPHA_ATRAS);
            } else {
                casObj[1].setAlpha(ALPHA_ATRAS);
                casObj[5].setAlpha(ALPHA_ATRAS);
            }
        }
        if (casSeleccionado % NUM_COLUMNAS_BASE == 4) {
            casObj[casSeleccionado - 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado - 2].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado - 3].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado + 1].setAlpha(ALPHA_ATRAS);
            if (casSeleccionado < 64) {
                casObj[casSeleccionado + 5].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 4].setAlpha(ALPHA_ATRAS);
            } else {
                casObj[1].setAlpha(ALPHA_ATRAS);
                casObj[2].setAlpha(ALPHA_ATRAS);
            }
        }
        if (casSeleccionado % NUM_COLUMNAS_BASE == 5) {
            casObj[casSeleccionado - 1].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado - 2].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado - 3].setAlpha(ALPHA_ATRAS);
            casObj[casSeleccionado - 4].setAlpha(ALPHA_ATRAS);
            if (casSeleccionado < 64) {
                casObj[casSeleccionado + 5].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 4].setAlpha(ALPHA_ATRAS);
                casObj[casSeleccionado + 3].setAlpha(ALPHA_ATRAS);
            } else {
                casObj[1].setAlpha(ALPHA_ATRAS);
                casObj[2].setAlpha(ALPHA_ATRAS);
                casObj[3].setAlpha(ALPHA_ATRAS);
            }
        }

        if (casAnterior != 0) {
            casObj[casAnterior].setAlpha(ALPHA_ANTERIOR);
        }
    }

    public void marcarJugadaTutorial(int casSeleccionado, int casAnterior) {

        animateShineTutorial(jugadaColumnaX);


        casObj[casSeleccionado].setAlpha(1f);
        casObj[casSeleccionado].setTextColor(RED);
        casillerosObsoletos(casSeleccionado);

    } // Marca la jugada en la fila.

    public void tutorialPosibilitiesShow(boolean isShow) {

        float alpha = 0;

        if (isShow == true) {
            alpha = 0.4f;
        } else {
            alpha = 0f;
        }

        if (gameManager.getFirstRow() == 0) {
            casObjAccion[9].setAlpha(alpha);
            casObjAccion[10].setAlpha(alpha);
            casObjAccion[11].setAlpha(alpha);
        } else {
            if (jugadaColumnaX == 0) {
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE + 1].setAlpha(alpha);
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE + 2].setAlpha(alpha);
            } else if (jugadaColumnaX == NUM_COLUMNAS - 1) {
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE].setAlpha(alpha);
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE + 1].setAlpha(alpha);
            } else {
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE].setAlpha(alpha);
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE + 1].setAlpha(alpha);
                casObjAccion[jugadaColumnaX + NUM_COLUMNAS_BASE + 2].setAlpha(alpha);
            }
        }
        for (int i = 15; i <= (3 * NUM_COLUMNAS_BASE - (NUM_COLUMNAS_BASE - NUM_COLUMNAS)); i++) {
            casObjAccionbtn[i].setAlpha(alpha);
        }
        for (int i = 22; i <= (4 * NUM_COLUMNAS_BASE - (NUM_COLUMNAS_BASE - NUM_COLUMNAS)); i++) {
            casObjAccionbtn[i].setAlpha(alpha);
        }


    }

    public void tutorialBonusSwipe() {

        if (firstTutorial != 7) {
            txtTutorial.setY(mapPosicion[7]);
            txtTutorial.setScaleX(1);
            txtTutorial.setScaleY(1);
            txtTutorial.setTextSize((utilities.TUTORIAL_TEXT_SIZE / 4) * 3);
            txtTutorial.setText(getString(R.string.bonus_swipe2));
            txtTutorial.setAlpha(1f);
            txtTutorial.bringToFront();
        }
        playability(false);

        long startDelay = 0;
        imgSwipeRightTutorial.bringToFront();
        imgSwipeLeftTutorial.bringToFront();

        if (tutorialBonusSide == 1 && firstTutorial != 7) {

            animacionShade1(0, 4);
            animacionShade2(6, 15);
            tutorialPosibilitiesShow(true);
            imgSwipeLeftTutorial.setY(mapPosicion[5]);
            imgSwipeRightTutorial.setY(mapPosicion[4]);
            tutorialBonusSide = 2;
            imgSwipeRightTutorial.setAlpha(1f);

        } else if (tutorialBonusSide == 2 && firstTutorial != 7) {

            animacionShade1(0, 4);
            animacionShade2(6, 15);
            tutorialPosibilitiesShow(true);
            imgSwipeRightTutorial.setY(mapPosicion[5]);
            imgSwipeLeftTutorial.setY(mapPosicion[4]);
            tutorialBonusSide = 1;
            imgSwipeRightTutorial.setAlpha(1f);

        } else {

            imgSwipeRightTutorial.setY(mapPosicion[4] + utilities.MODULO_Y / 3);
            imgSwipeLeftTutorial.setY(mapPosicion[4] + utilities.MODULO_Y / 3);
            startDelay = ANIMATION_VELOCITY * 12;

        }

        imgSwipeLeftTutorial.setX(centroX - moduloX * 3);
        imgSwipeRightTutorial.setX(centroX - moduloX);

        imgSwipeLeftTutorial.setAlpha(1f);


        ObjectAnimator fadeInLeft = ObjectAnimator.ofFloat(imgSwipeLeftTutorial, "alpha", 1f);
        fadeInLeft.setDuration(300);

        ObjectAnimator fadeInRight = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "alpha", 1f);
        fadeInRight.setDuration(1);
        fadeInRight.setStartDelay(startDelay);

        ObjectAnimator moverLeft = ObjectAnimator.ofFloat(imgSwipeLeftTutorial, "x", centroX - moduloX * 5);
        moverLeft.setDuration(2000);

        ObjectAnimator moverRight = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "x", centroX + moduloX * 2);
        moverRight.setDuration(2000);
        moverRight.setStartDelay(startDelay);

        ObjectAnimator fadeOutLeft = ObjectAnimator.ofFloat(imgSwipeLeftTutorial, "alpha", 0f);
        fadeOutLeft.setStartDelay(2000);
        fadeOutLeft.setDuration(1000);
        fadeOutLeft.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator fadeOutRight = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "alpha", 0f);
        fadeOutRight.setStartDelay(2000 + startDelay);
        fadeOutRight.setDuration(1000);
        fadeOutRight.setInterpolator(new AccelerateDecelerateInterpolator());

        tutorialSwipeShow = true;

        AnimatorSet animatorSet = new AnimatorSet();
        if (firstTutorial != 7) {
            playSwipeHands(2);
            animatorSet.play(fadeInLeft).with(fadeInRight).with(fadeOutLeft).with(fadeOutRight).with(moverLeft).with(moverRight);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playSwipeHands(1);
                }
            }, startDelay);
        } else {
            playSwipeHands(5);
            animatorSet.play(fadeInLeft).with(fadeInRight).with(moverLeft).with(moverRight).with(fadeOutLeft);
        }
        animatorSet.start();


        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (firstTutorial != 7) {
                    txtTutorial.setAlpha(0f);
                    tutorialPosibilitiesShow(false);
                    fadeOutShades();
                    playability(true);
                } else {
                    btnSwipeAccion1.setEnabled(true);
                }
            }
        });


    }

    public void tutorialBonusSwipeAuto(boolean isFirstTutorial) {

        txtTutorial.setY(mapPosicion[7]);
        txtTutorial.setTextSize((utilities.TUTORIAL_TEXT_SIZE / 4) * 3);
        txtTutorial.setText(getString(R.string.bonus_swipe2));
        txtTutorial.setAlpha(1f);
        txtTutorial.bringToFront();

        animacionShade1(0, 4);
        animacionShade2(6, 15);

        playability(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                imgSwipeRightTutorial.setY(mapPosicion[5]);
                imgSwipeRightTutorial.setX(columnaX[1]);
                ObjectAnimator fadeInRight = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "alpha", 1f);
                fadeInRight.setDuration(ANIMATION_VELOCITY_SWIPE);
                fadeInRight.setStartDelay(ANIMATION_VELOCITY * 2);

                ObjectAnimator moverRight = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "x", centroX + moduloX * 2);
                moverRight.setDuration(ANIMATION_VELOCITY_TUTORIAL);
                moverRight.setStartDelay(ANIMATION_VELOCITY * 2);

                ObjectAnimator fadeOutRight = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "alpha", 0f);
                fadeOutRight.setDuration(500);
                fadeOutRight.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(fadeInRight).with(moverRight).with(fadeOutRight);
                animatorSet.start();
                playSwipeHands(1);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animacionSwipe(3, 1);
                        moveRow(3, 1, true);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                ObjectAnimator fadeInBonus = ObjectAnimator.ofFloat(txtBonusSwipe, "alpha", 0.9f);
                                fadeInBonus.setDuration(ANIMATION_VELOCITY_SWIPE);
                                fadeInBonus.setInterpolator(new OvershootInterpolator());
                                fadeInBonus.start();
                                ObjectAnimator fadeOutBonus = ObjectAnimator.ofFloat(txtBonusSwipe, "alpha", ALPHA_BASE);
                                fadeOutBonus.setStartDelay(ANIMATION_VELOCITY_SWIPE);
                                fadeOutBonus.setDuration(ANIMATION_VELOCITY_SWIPE);
                                fadeOutBonus.start();

                                ObjectAnimator fadeInBonus2 = ObjectAnimator.ofFloat(txtBonusSwipe, "alpha", 0.9f);
                                fadeInBonus2.setStartDelay(ANIMATION_VELOCITY_SWIPE * 2);
                                fadeInBonus2.setDuration(ANIMATION_VELOCITY_SWIPE * 2);
                                fadeInBonus2.setInterpolator(new OvershootInterpolator());
                                fadeInBonus2.start();
                                ObjectAnimator fadeOutBonus2 = ObjectAnimator.ofFloat(txtBonusSwipe, "alpha", ALPHA_BASE);
                                fadeOutBonus2.setStartDelay(ANIMATION_VELOCITY_SWIPE * 4);
                                fadeOutBonus2.setDuration(ANIMATION_VELOCITY_SWIPE * 2);
                                fadeOutBonus2.start();

                                txtRulesButton.setAlpha(0f);
                                txtMaxBubbleThis.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                                txtMaxBubbleThis.setAlpha(0f);
                                fadeOutShades();
                                txtTutorial.setAlpha(0f);
                                playability(true);
                                imgFondoBN.setAlpha(0f);

                                txtBonusSwipe.setAlpha(ALPHA_BASE);
                                txtBonusSwipe.bringToFront();
                                txtBonusSwipe.setEnabled(true);
                                txtBonusSwipe.setTextColor(BLACK);

                            }
                        }, ANIMATION_VELOCITY_SWIPE);

                    }
                }, ANIMATION_VELOCITY_TUTORIAL - ANIMATION_VELOCITY_SWIPE * 3);

            }
        }, ANIMATION_VELOCITY);


        tutorialSwipeShow = true;
        if (isFirstTutorial) {

        } else {
            skipButton(1);
        }
    }

    private void skipButton(final int typeBonus) {

        txtMaxBubbleThis.setText(R.string.dont_show);
        txtMaxBubbleThis.setAlpha(1f);
        txtMaxBubbleThis.setTextSize(18);
        txtMaxBubbleThis.setEnabled(true);

        txtMaxBubbleThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dontShowAgain(typeBonus);
            }
        });

    }

    private void dontShowAgain(int typeBonus) {

        SharedPreferences tutorialCount = getSharedPreferences("tutorialCount", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = tutorialCount.edit();
        if (typeBonus == 1) {
            editor.putInt("bonusSwipe", AUTO_TUTORIAL_NUMBER);
        } else if (typeBonus == 2) {
            editor.putInt("bonusPlus", AUTO_TUTORIAL_NUMBER);
        } else if (typeBonus == 7) {
            editor.putInt("bonusExchange", AUTO_TUTORIAL_NUMBER);
        }
        editor.commit();
        txtMaxBubbleThis.setEnabled(false);
        txtMaxBubbleThis.setText("okeydokey");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtMaxBubbleThis.setAlpha(0f);
                txtMaxBubbleThis.setBackgroundResource(0);
            }
        }, ANIMATION_VELOCITY * 3);


        txtMaxBubbleThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    public void tutorialPlus() {

        txtTutorial.setY(mapPosicion[7]);
        txtTutorial.setTextSize((utilities.TUTORIAL_TEXT_SIZE / 4) * 3);
        txtTutorial.setText(getString(R.string.bonus_plus));
        txtTutorial.setAlpha(1f);
        txtTutorial.bringToFront();

        imgSwipeDownTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        imgSwipeUpTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        animacionShade1(0, 4);
        animacionShade2(6, 15);
        playability(false);
        tutorialPosibilitiesShow(true);

        imgSwipeUpTutorial.setY(mapPosicion[5]);
        imgSwipeUpTutorial.setX(columnaX[4]);
        imgSwipeDownTutorial.setY(mapPosicion[4] - utilities.MODULO_Y / 2);
        imgSwipeDownTutorial.setX(columnaX[0]);


        ObjectAnimator fadeInUp = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "alpha", 1f);
        fadeInUp.setDuration(ANIMATION_VELOCITY_SWIPE);
        fadeInUp.setStartDelay(ANIMATION_VELOCITY * 2);

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "y", mapPosicion[5] - utilities.MODULO_Y * 2);
        moveUp.setDuration(ANIMATION_VELOCITY_TUTORIAL);
        moveUp.setStartDelay(ANIMATION_VELOCITY * 2);

        ObjectAnimator fadeOutUp = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "alpha", 0f);
        fadeOutUp.setDuration(500);
        fadeOutUp.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

        ObjectAnimator fadeInDown = ObjectAnimator.ofFloat(imgSwipeDownTutorial, "alpha", 1f);
        fadeInDown.setDuration(ANIMATION_VELOCITY_SWIPE);
        fadeInDown.setStartDelay(ANIMATION_VELOCITY * 2);

        ObjectAnimator moveDown = ObjectAnimator.ofFloat(imgSwipeDownTutorial, "y", mapPosicion[4] + utilities.MODULO_Y * 2);
        moveDown.setDuration(ANIMATION_VELOCITY_TUTORIAL);
        moveDown.setStartDelay(ANIMATION_VELOCITY * 2);

        ObjectAnimator fadeOutDown = ObjectAnimator.ofFloat(imgSwipeDownTutorial, "alpha", 0f);
        fadeOutDown.setDuration(500);
        fadeOutDown.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeInDown).with(moveDown).with(fadeOutDown).with(fadeInUp).with(moveUp).with(fadeOutUp);
        animatorSet.start();

        playSwipeHands(4);
        playSwipeHands(3);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgSwipeDownTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
                imgSwipeUpTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
                imgFondoBN.setLayerType(View.LAYER_TYPE_NONE, null);
                txtTutorial.setAlpha(0f);
                fadeOutShades();
                playability(true);
                tutorialPosibilitiesShow(false);
            }
        });

    }

    public void tutorialPlusAuto(boolean isFirstTutorial) {

        txtTutorial.setY(mapPosicion[7]);
        txtTutorial.setTextSize((utilities.TUTORIAL_TEXT_SIZE / 4) * 3);
        txtTutorial.setText(getString(R.string.bonus_plus));
        txtTutorial.setAlpha(1f);
        txtTutorial.bringToFront();

        imgSwipeDownTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        imgSwipeUpTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        animacionShade1(0, 4);
        animacionShade2(6, 15);

        playability(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                imgSwipeUpTutorial.setY(mapPosicion[5]);
                imgSwipeUpTutorial.setX(columnaX[1]);
                accionPiso = 3;

                ObjectAnimator fadeInUp = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "alpha", 1f);
                fadeInUp.setDuration(ANIMATION_VELOCITY_SWIPE);
                fadeInUp.setStartDelay(ANIMATION_VELOCITY * 2);

                ObjectAnimator moveUp = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "y", mapPosicion[5] - utilities.MODULO_Y * 2);
                moveUp.setDuration(ANIMATION_VELOCITY_TUTORIAL);
                moveUp.setStartDelay(ANIMATION_VELOCITY * 2);

                ObjectAnimator fadeOutUp = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "alpha", 0f);
                fadeOutUp.setDuration(500);
                fadeOutUp.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(fadeInUp).with(moveUp).with(fadeOutUp);
                animatorSet.start();
                playSwipeHands(3);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        plusAction(3, 1, 1, true);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        imgSwipeUpTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
                                        imgSwipeDownTutorial.setY(mapPosicion[5] - utilities.MODULO_Y / 2);
                                        imgSwipeDownTutorial.setX(columnaX[1]);

                                        ObjectAnimator fadeOutBN = ObjectAnimator.ofFloat(imgFondoBN, "alpha", 0f);
                                        fadeOutBN.setDuration(500);
                                        fadeOutBN.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                                        ObjectAnimator fadeInDown = ObjectAnimator.ofFloat(imgSwipeDownTutorial, "alpha", 1f);
                                        fadeInDown.setDuration(ANIMATION_VELOCITY_SWIPE);
                                        fadeInDown.setStartDelay(ANIMATION_VELOCITY * 2);

                                        ObjectAnimator moveDown = ObjectAnimator.ofFloat(imgSwipeDownTutorial, "y", mapPosicion[5] + utilities.MODULO_Y * 2);
                                        moveDown.setDuration(ANIMATION_VELOCITY_TUTORIAL);
                                        moveDown.setStartDelay(ANIMATION_VELOCITY * 2);

                                        ObjectAnimator fadeOutDown = ObjectAnimator.ofFloat(imgSwipeDownTutorial, "alpha", 0f);
                                        fadeOutDown.setDuration(500);
                                        fadeOutDown.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                                        AnimatorSet animatorSet = new AnimatorSet();
                                        animatorSet.play(fadeInDown).with(moveDown).with(fadeOutDown).with(fadeOutBN);
                                        animatorSet.start();
                                        playSwipeHands(4);

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                plusAction(3, 1, -1, true);


                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        imgSwipeDownTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
                                                        imgFondoBN.setLayerType(View.LAYER_TYPE_NONE, null);
                                                        txtTutorial.setAlpha(0f);
                                                        fadeOutShades();
                                                        playability(true);
                                                        accionPiso = 0;
                                                        txtRulesButton.setAlpha(0f);
                                                        txtMaxBubbleThis.setAlpha(0f);
                                                        txtMaxBubbleThis.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                            }
                                                        });

                                                        txtBonusPlus.setAlpha(ALPHA_BASE);
                                                        txtBonusPlus.bringToFront();
                                                        txtBonusPlus.setEnabled(true);
                                                        txtBonusPlus.setTextColor(BLACK);

                                                    }
                                                }, ANIMATION_VELOCITY_SWIPE);

                                            }
                                        }, ANIMATION_VELOCITY_TUTORIAL - ANIMATION_VELOCITY_SWIPE * 3);

                                    }
                                }, ANIMATION_VELOCITY);

                            }
                        }, ANIMATION_VELOCITY_SWIPE);

                    }
                }, ANIMATION_VELOCITY_TUTORIAL - ANIMATION_VELOCITY_SWIPE * 3);

            }
        }, ANIMATION_VELOCITY);

        tutorialPlusShow = true;

        if (isFirstTutorial) {

        } else {
            skipButton(2);
        }

    }

    private void tutorialExchange(){

        txtTutorial.setY(mapPosicion[7]);
        txtTutorial.setTextSize((utilities.TUTORIAL_TEXT_SIZE / 4) * 3);
        txtTutorial.setText(getString(R.string.bonus_exchange_0));
        txtTutorial.setAlpha(1f);
        txtTutorial.bringToFront();

        imgTapTutorial1.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        animacionShade2(6, 15);
        tutorialPosibilitiesShow(false);

        playability(false);

        imgTapTutorial1.setY(mapPosicion[5] + MODULO_Y / 4);
        imgTapTutorial1.setX(columnaX[4] - moduloX);
        accionPiso = 2;

        ObjectAnimator fadeInUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 1f);
        fadeInUp.setDuration(ANIMATION_VELOCITY_SWIPE);
        fadeInUp.setStartDelay(ANIMATION_VELOCITY * 2);


        ObjectAnimator fadeOutUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 0f);
        fadeOutUp.setDuration(500);
        fadeOutUp.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeInUp).with(fadeOutUp);
        animatorSet.start();
        playSwipeHands(6);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                imgTapTutorial1.setY(mapPosicion[4] + MODULO_Y / 4);
                imgTapTutorial1.setX(columnaX[1] - moduloX);

                ObjectAnimator fadeInUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 1f);
                fadeInUp.setDuration(ANIMATION_VELOCITY_SWIPE);
                fadeInUp.setStartDelay(ANIMATION_VELOCITY * 2);


                ObjectAnimator fadeOutUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 0f);
                fadeOutUp.setDuration(500);
                fadeOutUp.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(fadeInUp).with(fadeOutUp);
                animatorSet.start();
                playSwipeHands(6);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imgTapTutorial1.setLayerType(View.LAYER_TYPE_NONE, null);
                        imgFondoBN.setLayerType(View.LAYER_TYPE_NONE, null);
                        txtTutorial.setAlpha(0f);
                        fadeOutShades();
                        playability(true);
                        tutorialPosibilitiesShow(false);
                    }
                });

            }
        }, ANIMATION_VELOCITY_TUTORIAL);

    }

    private void tutorialExchangeAuto(boolean isFirstTutorial) {

        txtTutorial.setY(mapPosicion[7]);
        txtTutorial.setTextSize((utilities.TUTORIAL_TEXT_SIZE / 4) * 3);
        txtTutorial.setText(getString(R.string.bonus_exchange_1));
        txtTutorial.setAlpha(1f);
        txtTutorial.bringToFront();

        imgTapTutorial1.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        animacionShade2(6, 15);

        playability(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int firstBubble = 3;

                if (gameManager.row[gameManager.getFourthRow()].column[firstBubble].getState() == 3){
                    firstBubble = 4;
                        if (gameManager.row[gameManager.getFourthRow()].column[firstBubble].getState() == 3){
                            firstBubble = 2;
                        }
                }

                final int firstBubbleFinal = firstBubble;

                imgTapTutorial1.setY(mapPosicion[5] + MODULO_Y / 4);
                imgTapTutorial1.setX(columnaX[firstBubble] - moduloX);
                accionPiso = 3;

                ObjectAnimator fadeInUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 1f);
                fadeInUp.setDuration(ANIMATION_VELOCITY_SWIPE);
                fadeInUp.setStartDelay(ANIMATION_VELOCITY * 2);


                ObjectAnimator fadeOutUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 0f);
                fadeOutUp.setDuration(500);
                fadeOutUp.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(fadeInUp).with(fadeOutUp);
                animatorSet.start();
                playSwipeHands(6);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        preExchangeBubble(3, firstBubbleFinal);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                int secondBubble = 0;

                                if (gameManager.row[gameManager.getFourthRow()].column[secondBubble].getState() == 3){
                                    secondBubble = 1;
                                    if (gameManager.row[gameManager.getFourthRow()].column[secondBubble].getState() == 3 ){
                                        if (firstBubbleFinal == 4) {
                                            secondBubble = 2;
                                        } else if (firstBubbleFinal == 2){
                                            secondBubble = 4;
                                        }
                                    }
                                }

                                txtTutorial.setText(getString(R.string.bonus_exchange_2));
                                imgTapTutorial1.setY(mapPosicion[5] + MODULO_Y / 4);
                                imgTapTutorial1.setX(columnaX[0] - moduloX);
                                accionPiso = 3;

                                ObjectAnimator fadeInUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 1f);
                                fadeInUp.setDuration(ANIMATION_VELOCITY_SWIPE);
                                fadeInUp.setStartDelay(ANIMATION_VELOCITY * 2);


                                ObjectAnimator fadeOutUp = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 0f);
                                fadeOutUp.setDuration(500);
                                fadeOutUp.setStartDelay(ANIMATION_VELOCITY_TUTORIAL - 1000 + ANIMATION_VELOCITY);

                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.play(fadeInUp).with(fadeOutUp);
                                animatorSet.start();
                                playSwipeHands(6);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                txtTutorial.setText(getString(R.string.bonus_exchange_3));
                                                exchangeBubble(3, 0, true);

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        imgTapTutorial1.setLayerType(View.LAYER_TYPE_NONE, null);
                                                        imgFondoBN.setLayerType(View.LAYER_TYPE_NONE, null);
                                                        txtTutorial.setAlpha(0f);
                                                        fadeOutShades();
                                                        playability(true);
                                                        accionPiso = 0;
                                                        txtRulesButton.setAlpha(0f);
                                                        txtMaxBubbleThis.setAlpha(0f);
                                                        txtMaxBubbleThis.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                            }
                                                        });
                                                        txtBonusExchange.setAlpha(ALPHA_BASE);
                                                        txtBonusExchange.bringToFront();
                                                        txtBonusExchange.setEnabled(true);
                                                        txtBonusExchange.setTextColor(BLACK);

                                                    }
                                                }, ANIMATION_VELOCITY_TUTORIAL);

                                            }

                                        }, ANIMATION_VELOCITY_TUTORIAL);

                                    }
                                }, ANIMATION_VELOCITY_SWIPE);

                            }
                        }, ANIMATION_VELOCITY_TUTORIAL);

                    }
                }, ANIMATION_VELOCITY_SWIPE * 2);
            }
        }, ANIMATION_VELOCITY);


        tutorialExchangeShow = true;

        if (isFirstTutorial) {

        } else {
            skipButton(7);
        }

    }

    private void swipeRowAnimation(int actionRow, int direction) {

        soundPlayer.playSoundBonusSwipe(this);

        if (direction == 1) {

            moveRowRight(actionRow, moduloX, ANIMATION_VELOCITY_SWIPE);

        } else if (direction == 0) {

            moveRowLeft(actionRow, moduloX, ANIMATION_VELOCITY_SWIPE);

        }

    }

    public void animacionSwipe(int accionPiso, int direccion) {

        soundPlayer.playSoundSwipePlay(this);

        int pisoSwipe = (gameManager.getFirstRow() + accionPiso) % 10;

        float leftSide = columnaX[0];
        float rightSide = columnaX[4];

        if (accionPiso == 2) {

            imgAnimacionSwipe.setY(mapPosicion[4]);
            imgResplandorSwipe.setY(mapPosicion[4]);

        } else if (accionPiso == 3) {

            imgAnimacionSwipe.setY(mapPosicion[5]);
            imgResplandorSwipe.setY(mapPosicion[5]);

        }

        float positionAnimation;

        if (direccion == 1) {
            imgAnimacionSwipe.setX(leftSide);
            imgAnimacionSwipe.setRotation(0);

            moveRowRight(pisoSwipe, moduloX, ANIMATION_VELOCITY_SWIPE);

        } else if (direccion == 0) {
            imgAnimacionSwipe.setX(rightSide);
            imgAnimacionSwipe.setRotation(180);

            moveRowLeft(pisoSwipe, moduloX, ANIMATION_VELOCITY_SWIPE);
        }

    }

    public void tutorialTextAnimate() {

        //txtTutorial.setY(mapPosicion[7]);
        txtTutorial.bringToFront();
        txtTutorial.setAlpha(1f);
        txtTutorial.setTextColor(BLUE);

        txtTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);


        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(txtTutorial, "alpha", 0f);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(ANIMATION_VELOCITY * 3);
        fadeOut.setStartDelay(ANIMATION_VELOCITY * 5);

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(txtTutorial, "Y", mapPosicion[1]);
        moveUp.setInterpolator(new AccelerateDecelerateInterpolator());
        moveUp.setDuration(ANIMATION_VELOCITY * 8);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                txtTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeOut).with(moveUp);
        animatorSet.start();

    }

    public void animacionTutorialJugada1() {

        imgSwipeUpTutorial.bringToFront();
        imgSwipeUpTutorial.setAlpha(1f);
        imgSwipeUpTutorial.setY(mapPosicion[0]);
        imgSwipeUpTutorial.setX(columnaX[2]);


        imgSwipeUpTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator swipeUpAnimation = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "y", mapPosicion[1]);
        swipeUpAnimation.setDuration(ANIMATION_VELOCITY * 6);
        swipeUpAnimation.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator swipeUpFadeOut = ObjectAnimator.ofFloat(imgSwipeUpTutorial, "alpha", 0f);
        swipeUpFadeOut.setStartDelay(ANIMATION_VELOCITY * 5);
        swipeUpFadeOut.setDuration(ANIMATION_VELOCITY);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(swipeUpAnimation).with(swipeUpFadeOut);
        animatorSet.start();
        playSwipeHands(3);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgSwipeUpTutorial.setLayerType(View.LAYER_TYPE_NONE, null);

                //playability(true);


                //isTutorialRunning = false;
            }
        });

    }

    public void animacionTutorialJugada2() {

        imgSwipeRightTutorial.bringToFront();
        imgSwipeRightTutorial.setAlpha(1f);
        imgSwipeRightTutorial.setY(mapPosicion[0]);
        imgSwipeRightTutorial.setX(columnaX[2]);

        imgSwipeRightTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator swipeRightAnimation = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "x", columnaX[4]);
        swipeRightAnimation.setDuration(ANIMATION_VELOCITY * 6);
        swipeRightAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator swipeFadeOut = ObjectAnimator.ofFloat(imgSwipeRightTutorial, "alpha", 0f);
        swipeFadeOut.setStartDelay(ANIMATION_VELOCITY * 5);
        swipeFadeOut.setDuration(ANIMATION_VELOCITY);

        playSwipeHands(1);

        swipeRightAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgSwipeRightTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(swipeRightAnimation);
        animatorSet.start();


    }

    public void animacionTutorialJugada3() {

        imgSwipeLeftTutorial.bringToFront();
        imgSwipeLeftTutorial.setAlpha(1f);
        imgSwipeLeftTutorial.setY(mapPosicion[0]);
        imgSwipeLeftTutorial.setX(columnaX[2]);

        imgSwipeLeftTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator swipeAnimation = ObjectAnimator.ofFloat(imgSwipeLeftTutorial, "x", columnaX[0]);
        swipeAnimation.setDuration(ANIMATION_VELOCITY * 6);
        swipeAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator swipeFadeOut = ObjectAnimator.ofFloat(imgSwipeLeftTutorial, "alpha", 0f);
        swipeFadeOut.setStartDelay(ANIMATION_VELOCITY * 5);
        swipeFadeOut.setDuration(ANIMATION_VELOCITY);

        playSwipeHands(2);

        swipeAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgSwipeLeftTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(swipeAnimation);
        animatorSet.start();


    }

    public void animacionTutorialJugada4() {

        imgTapTutorial1.bringToFront();
        imgTapTutorial1.setAlpha(1f);
        imgTapTutorial1.setY(mapPosicion[3] + utilities.MODULO_Y / 4);
        imgTapTutorial1.setX(columnaX[1] + moduloX);

        //imgTapTutorial1.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator swipeFadeOut = ObjectAnimator.ofFloat(imgTapTutorial1, "alpha", 0f);
        swipeFadeOut.setStartDelay(ANIMATION_VELOCITY * 3);
        swipeFadeOut.setDuration(ANIMATION_VELOCITY * 1);


        ObjectAnimator fadeOutBN = ObjectAnimator.ofFloat(imgFondoBN, "alpha", 0f);
        fadeOutBN.setStartDelay(ANIMATION_VELOCITY * 2);
        fadeOutBN.setDuration(ANIMATION_VELOCITY * 6);
        playSwipeHands(6);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(swipeFadeOut).with(fadeOutBN);
        //animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                imgTapTutorial1.setLayerType(View.LAYER_TYPE_NONE, null);

            }
        });


    }

    private void animacionShade1(int pos, int height) {

        imgShade1.setAlpha(0f);
        imgShade1.setY(utilities.MODULO_Y * pos);
        imgShade1.setMinimumWidth(utilities.WIDTHSCREEN);
        imgShade1.setMaxWidth(utilities.WIDTHSCREEN);
        imgShade1.setMinimumHeight((int) utilities.MODULO_Y * height);
        imgShade1.setMaxHeight((int) utilities.MODULO_Y * height);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imgShade1, "alpha", 0.4f);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(ANIMATION_VELOCITY * 2);
        fadeIn.start();

        imgShade1.bringToFront();
        txtRulesButton.bringToFront();
        txtTutorial.bringToFront();
        txtMaxBubbleThis.bringToFront();
        txtBonusSwipe.bringToFront();
        txtBonusSwipe.bringToFront();

    }

    private void animacionShade2(int pos, int height) {

        imgShade2.setAlpha(0f);
        imgShade2.setY(utilities.MODULO_Y * pos);
        imgShade2.setMinimumWidth(utilities.WIDTHSCREEN);
        imgShade2.setMaxWidth(utilities.WIDTHSCREEN);
        imgShade2.setMinimumHeight((int) utilities.MODULO_Y * height);
        imgShade2.setMaxHeight((int) utilities.MODULO_Y * height);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imgShade2, "alpha", 0.4f);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(ANIMATION_VELOCITY * 2);
        fadeIn.start();

        imgShade2.bringToFront();
        txtRulesButton.bringToFront();
        txtRulesButton.bringToFront();
        txtTutorial.bringToFront();
        txtMaxBubbleThis.bringToFront();
        txtBonusSwipe.bringToFront();
        txtBonusPlus.bringToFront();

    }

    private void fadeOutShades() {
        imgShade1.setAlpha(0f);
        imgShade2.setAlpha(0f);
    }

    private void animateTextTutorial(float mapPosicion, float textSize, String strTutorial) {

        if (mapPosicion != 0) {
            txtTutorial.setY(mapPosicion);
        }
        if (textSize != 0) {
            txtTutorial.setTextSize(textSize);
        }
        txtTutorial.setText(strTutorial);
        txtTutorial.bringToFront();
        txtTutorial.setAlpha(1f);

    }

    private void firstTutorial() {


        txtRulesButton.setAlpha(0f);
        txtBonusPlus.setAlpha(0f);
        txtBonusSwipe.setAlpha(0f);
        txtBonusExchange.setAlpha(0f);
        imgSound.setAlpha(0f);
        imgResetButton.setAlpha(0f);
        piso[9].setAlpha(0f);
        piso[8].setAlpha(0f);
        piso[7].setAlpha(0f);
        piso[6].setAlpha(0f);
        piso[5].setAlpha(0f);
        piso[4].setAlpha(0f);
        piso[3].setAlpha(0f);
        piso[2].setAlpha(0f);
        piso[1].setAlpha(0f);
        piso[0].setAlpha(0f);
        imgCasilleroSeleccionado.setAlpha(0f);
        rlButtonMenu.setEnabled(false);

        txtRulesButton.setEnabled(true);
        txtRulesButton.bringToFront();


        playability(false);
        txtPuntaje.setAlpha(0f);
        txtMax.setAlpha(0f);

        isTutorialRunning = true;

        txtRulesButton.setTextSize(utilities.NUM_SIZE);
        txtRulesButton.setTextColor(BLACK);
        txtRulesButton.setText("");
        txtRulesButton.setAlpha(1f);
        txtRulesButton.setBackgroundResource(R.drawable.ic_bubble_tick);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                animateTextTutorial(mapPosicion[4], utilities.TUTORIAL_TEXT_SIZE, getString(R.string.goal));
                txtRulesButton.setY(mapPosicion[3] + utilities.MODULO_Y / 2);
                txtRulesButton.bringToFront();

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(txtTutorial, "alpha", 1f);
                fadeIn.setInterpolator(new AccelerateInterpolator());
                fadeIn.setDuration(ANIMATION_VELOCITY * 3);

                ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(txtRulesButton, "alpha", 1f);
                fadeIn2.setInterpolator(new AccelerateInterpolator());
                fadeIn2.setDuration(ANIMATION_VELOCITY * 3);


                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(fadeIn).with(fadeIn2);
                animatorSet.start();

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        txtRulesButton.setEnabled(true);
                        txtTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
                        txtRulesButton.setLayerType(View.LAYER_TYPE_NONE, null);
                    }
                });

            }
        }, ANIMATION_VELOCITY);

        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indications1();
            }
        });

    }

    private void indications1() {

        firstTutorial = 1;
        startNewGame(false);
        //crearInicialTutorial();
        txtMaxBubbleThis.bringToFront();
        animateTextTutorial(mapPosicion[7], (utilities.TUTORIAL_TEXT_SIZE / 4) * 3, getString(R.string.can_next));
        txtRulesButton.setY(mapPosicion[7] + utilities.MODULO_Y / 3);
        txtMaxBubbleThis.setText(getString(R.string.tap));
        imgCasilleroSeleccionado.setAlpha(1f);
        txtMaxBubbleThis.setAlpha(1f);
        txtRulesButton.setEnabled(false);
        txtRulesButton.setAlpha(0f);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionTutorialJugada4();
                casObjAccion[10].setEnabled(true);
                animacionShade1(0, 6);
                animacionShade2(7, 12);
            }
        }, ANIMATION_VELOCITY * 4);

    }

    private void indications2() {

        fadeOutShades();
        firstTutorial++;
        tutorialPlay = 1;
        play(2);
        //jugarTutorial(2, 1);
        animateTextTutorial(0, (utilities.TUTORIAL_TEXT_SIZE / 4) * 3, getString(R.string.swipe));
        txtMaxBubbleThis.setText(getString(R.string.swipe_right));
        txtMaxBubbleThis.setAlpha(1f);
        imgTapTutorial1.setAlpha(0f);
        constantAnimation();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionTutorialJugada2();
                animacionShade1(4, 3);
            }
        }, ANIMATION_VELOCITY * 3);

        btnSwipeZone.setEnabled(true);
        casObjAccion[10].setEnabled(false);

    }

    private void indications3() {

        fadeOutShades();
        firstTutorial++;
        tutorialPlay = 2;
        play(3);
        //jugarTutorial(3, 2);
        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.never_can) + "\n" + getString(R.string.never_can2) + "\n" + getString(R.string.never_can1));
        txtMaxBubbleThis.setAlpha(0f);
        imgSwipeRightTutorial.setAlpha(0f);
        txtRulesButton.setEnabled(true);
        txtRulesButton.setAlpha(1f);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                casObjAccion[12].setAlpha(0.7f);
                casObjAccion[11].setAlpha(0.7f);
                imgSwipeRightTutorial.setAlpha(0f);
                animacionShade2(4, 14);
            }
        }, ANIMATION_VELOCITY * 3);


        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indications4();
            }
        });

    }

    private void indications4() {

        fadeOutShades();
        firstTutorial++;
        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.can_previos));
        txtMaxBubbleThis.setText(getString(R.string.swipe_left) + " " + getString(R.string.or) + " " + getString(R.string.tap));
        txtMaxBubbleThis.setAlpha(1f);
        casObjAccion[10].setAlpha(0.5f);
        casObjAccion[10].setEnabled(true);
        casObjAccion[10].setBackgroundResource(R.drawable.bubble_ok);
        txtRulesButton.setEnabled(false);
        txtRulesButton.setAlpha(0f);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionTutorialJugada3();
            }
        }, ANIMATION_VELOCITY * 3);

    }

    private void indications5() {

        firstTutorial++;
        tutorialPlay = 3;
        play(2);
        //jugarTutorial(2, 3);
        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.special_moves));
        rlButtonMenu.setAlpha(ALPHA_BASE);
        imgSwipeLeftTutorial.setAlpha(0f);
        txtMaxBubbleThis.setText(getString(R.string.swipe_up) + " " + getString(R.string.or) + " " + getString(R.string.tap));
        casObjAccion[12].setAlpha(0f);
        casObjAccion[11].setAlpha(0f);
        casObjAccion[10].setAlpha(0f);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionTutorialJugada1();
                imgSwipeLeftTutorial.setAlpha(0f);
                casObjAccion[10].setEnabled(true);
            }
        }, ANIMATION_VELOCITY * 3);


    }

    private void indications51() {

        txtBonusPlus.setAlpha(ALPHA_BASE);
        txtBonusSwipe.setAlpha(ALPHA_BASE);
        txtBonusExchange.setAlpha(ALPHA_BASE);
        txtBonusSwipe.setTextColor(BLACK);
        txtBonusSwipe.setEnabled(true);
        casObjAccion[10].setEnabled(false);
        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.bonus_menu));
        casObj[31].setBackgroundResource(R.drawable.ic_bubble_3);
        txtRulesButton.setAlpha(1f);
        txtRulesButton.setEnabled(true);
        tutorialPlay = 4;
        play(2);
        txtMaxBubbleThis.setAlpha(0f);
        imgSwipeUpTutorial.setAlpha(0f);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionShade1(4, 12);
                txtBonusSwipe.setText(Integer.toString(gameManager.player.getBonusSwipe()));
                txtBonusSwipe.setAlpha(ALPHA_BASE);
                txtBonusSwipe.setTextColor(BLACK);
                txtBonusSwipe.setEnabled(true);
            }
        }, ANIMATION_VELOCITY * 3);

        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indications52();
            }
        });

    }

    private void indications52() {

        fadeOutShades();
        playArrows();
        txtRulesButton.setEnabled(false);
        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.where_use));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionShade2(6, 15);
                txtRulesButton.setEnabled(true);
            }
        }, ANIMATION_VELOCITY * 3);

        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indications6();
            }
        });

    }

    private void indications6() {

        stopArrows();
        fadeOutShades();
        txtRulesButton.setEnabled(false);
        casObjAccion[10].setEnabled(false);
        firstTutorial++;
        casObjAccion[9].setAlpha(0.7f);
        casObjAccion[9].setBackgroundResource(R.drawable.bubble_ok);
        casObjAccion[10].setAlpha(0.7f);
        casObjAccion[10].setBackgroundResource(R.drawable.bubble_ok);
        casObjAccion[11].setAlpha(0.7f);
        casObjAccion[11].setBackgroundResource(R.drawable.ic_bubble_dont);
        casObjAccionbtn[15].setAlpha(0.7f);
        casObjAccionbtn[15].setBackgroundResource(R.drawable.ic_bubble_dont);
        casObjAccionbtn[16].setAlpha(0.7f);
        casObjAccionbtn[16].setBackgroundResource(R.drawable.ic_bubble_dont);
        casObjAccionbtn[17].setAlpha(0.7f);
        casObjAccionbtn[17].setBackgroundResource(R.drawable.ic_bubble_dont);
        casObjAccionbtn[18].setAlpha(0.7f);
        casObjAccionbtn[18].setBackgroundResource(R.drawable.ic_bubble_dont);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtRulesButton.setEnabled(true);
                txtRulesButton.setAlpha(1f);
                animacionShade2(4, 15);
            }
        }, ANIMATION_VELOCITY * 4);


        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.attention));

        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indications7();
            }
        });

    }

    private void indications7() {


        playArrowsTutorial();
        casObjAccionbtn[15].setAlpha(0f);
        casObjAccionbtn[15].setBackgroundResource(R.drawable.bubble_ok);
        casObjAccionbtn[16].setAlpha(0f);
        casObjAccionbtn[16].setBackgroundResource(R.drawable.bubble_ok);
        casObjAccionbtn[17].setAlpha(0f);
        casObjAccionbtn[17].setBackgroundResource(R.drawable.bubble_ok);
        casObjAccionbtn[18].setAlpha(0f);
        casObjAccionbtn[18].setBackgroundResource(R.drawable.bubble_ok);
        casObjAccion[9].setAlpha(0f);
        casObjAccion[10].setAlpha(0f);
        casObjAccion[11].setAlpha(0f);
        casObjAccion[10].setEnabled(false);
        firstTutorial++;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animacionShade1(0, 5);
                animacionShade2(6, 3);
                tutorialBonusSwipe();
            }
        }, ANIMATION_VELOCITY * 4);

        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.bonus_swipe));
        txtTutorial.setText(getString(R.string.bonus_swipe));
        txtRulesButton.setEnabled(false);
        txtRulesButton.setAlpha(0f);
        txtMaxBubbleThis.setAlpha(1f);
        txtMaxBubbleThis.setText(getString(R.string.swipe_left) + " " + getString(R.string.or) + " " + getString(R.string.swipe_right) + "\n" + getString(R.string.move_the_row));
        imgSwipeUpTutorial.setAlpha(0f);
        btnSwipeAccion1.setEnabled(true);

    }

    private void indications8(final int direction) {

        stopArrows();
        fadeOutShades();
        casObjAccion[9].setBackgroundResource(R.drawable.bubble_ok);
        firstTutorial++;

        imgSwipeLeftTutorial.setAlpha(0f);
        imgSwipeRightTutorial.setAlpha(0f);
        casObjAccion[10].setAlpha(0f);
        casObjAccion[11].setAlpha(0f);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                casObjAccion[9].setAlpha(0.4f);
                if (direction == 2) {
                    casObjAccionbtn[15].setAlpha(0.4f);
                    casObjAccionbtn[22].setAlpha(0.4f);
                    casObjAccionbtn[23].setAlpha(0.4f);
                } else if (direction == 1) {
                    casObjAccionbtn[17].setAlpha(0.4f);
                    casObjAccionbtn[23].setAlpha(0.4f);
                    casObjAccionbtn[24].setAlpha(0.4f);
                }
                imgSwipeLeftTutorial.setAlpha(0f);
                imgSwipeRightTutorial.setAlpha(0f);
            }
        }, ANIMATION_VELOCITY * 3);


        animateTextTutorial(0, ((utilities.TUTORIAL_TEXT_SIZE / 4) * 3), getString(R.string.great));
        txtMaxBubbleThis.setText(getString(R.string.tip0));
        txtRulesButton.setAlpha(1f);
        txtRulesButton.setEnabled(true);

        txtRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indications9();
            }
        });

    }

    private void indications9() {

        SharedPreferences tutorialCount = getSharedPreferences("tutorialCount", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = tutorialCount.edit();
        editor.putBoolean("firstTime", false);
        editor.putInt("adviceNumber", 0);
        editor.commit();

        imgSwipeLeftTutorial.setAlpha(0f);
        imgSwipeRightTutorial.setAlpha(0f);
        imgSound.setAlpha(ALPHA_BASE);
        imgResetButton.setAlpha(ALPHA_BASE);
        txtTutorial.setY(mapPosicion[4]);
        txtTutorial.setTextSize(utilities.TUTORIAL_TEXT_SIZE);
        txtTutorial.setText(R.string.go);
        txtMaxBubbleThis.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        txtTutorial.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        casObjAccion[9].setAlpha(0f);
        casObjAccion[9].setBackgroundResource(R.drawable.ic_bubble_dont);
        casObjAccion[10].setBackgroundResource(R.drawable.ic_bubble_dont);
        casObjAccionbtn[15].setAlpha(0f);
        casObjAccionbtn[17].setAlpha(0f);
        casObjAccionbtn[23].setAlpha(0f);
        casObjAccionbtn[24].setAlpha(0f);
        casObjAccionbtn[22].setAlpha(0f);
        txtRulesButton.setEnabled(false);
        txtRulesButton.setAlpha(0f);
        txtMax.setText(Long.toString(gameManager.player.getMaxBubble()));
        txtPuntaje.setText(Long.toString(gameManager.player.getScore()));
        startWithTutorial = false;
        isTutorialRunning = false;
        saveLeaderBoard();
        saveClassicLeaderBoard();

        animationContinueBubbles(0, 0);
        animationContinueBubbles(1, 1);
        animationContinueBubbles(2, 2);
        animationContinueBubbles(3, 3);
        animationContinueBubbles(4, 4);

        final Handler handler2 = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationContinueBubbles(0, 5);
                animationContinueBubbles(1, 6);
                animationContinueBubbles(2, 7);
                animationContinueBubbles(3, 8);
                animationContinueBubbles(4, 9);
            }
        }, ANIMATION_VELOCITY);

        ObjectAnimator movY = ObjectAnimator.ofFloat(txtTutorial, "y", mapPosicion[7]);
        movY.setDuration(ANIMATION_VELOCITY * 7);
        movY.setInterpolator(new AccelerateInterpolator());
        movY.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ObjectAnimator textScaleX = ObjectAnimator.ofFloat(txtTutorial, "scaleX", 50);
                textScaleX.setDuration(ANIMATION_VELOCITY);
                textScaleX.setInterpolator(new AccelerateInterpolator());

                ObjectAnimator textScaleY = ObjectAnimator.ofFloat(txtTutorial, "scaleY", 0.05f);
                textScaleY.setDuration(ANIMATION_VELOCITY / 2);
                textScaleY.setStartDelay(ANIMATION_VELOCITY / 2);
                textScaleY.setInterpolator(new AccelerateInterpolator());

                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(txtTutorial, "alpha", 0);
                fadeOut.setInterpolator(new AccelerateInterpolator());
                fadeOut.setDuration(ANIMATION_VELOCITY);

                ObjectAnimator fadeOut2 = ObjectAnimator.ofFloat(txtMaxBubbleThis, "alpha", 0);
                fadeOut2.setInterpolator(new AccelerateInterpolator());
                fadeOut2.setStartDelay(utilities.ADVICE_DURATION);
                fadeOut2.setDuration(utilities.ADVICE_VELOCITY);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(fadeOut2).with(fadeOut).with(textScaleX).with(textScaleY);
                animatorSet.start();

                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        txtMax.setAlpha(ALPHA_BASE);
                        txtPuntaje.setAlpha(ALPHA_BASE);
                        txtTutorial.setLayerType(View.LAYER_TYPE_NONE, null);
                        txtTutorial.setScaleX(1);
                        txtTutorial.setScaleY(1);
                    }
                });

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        txtTutorial.setScaleX(1);
                        txtTutorial.setScaleY(1);
                        txtMaxBubbleThis.setLayerType(View.LAYER_TYPE_NONE, null);
                    }
                });

            }
        }, ANIMATION_VELOCITY * 5);

        playability(true);
        continuePlaying(true);
        txtMax.setAlpha(0f);
        txtPuntaje.setAlpha(0f);
        txtMaxBubbleThis.setAlpha(1f);
    }

    private void adviceAnimation (){


        SharedPreferences adviceCount = getSharedPreferences("tutorialCount", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = adviceCount.edit();

        if (adviceCount.getInt("adviceNumber", 1) < NUMBER_OF_ADVICE * 3) {

            int tip = adviceCount.getInt("adviceNumber", 1);
            int tipNumber = (adviceCount.getInt("adviceNumber", 1)) % NUMBER_OF_ADVICE;
            String advice = "tip" + tipNumber;
            int stringResource = getResources().getIdentifier(advice, "string", getPackageName());
            txtMaxBubbleThis.setAlpha(1f);
            txtMaxBubbleThis.setTextColor(BLACK);
            txtMaxBubbleThis.setText(getString(stringResource));
            txtMaxBubbleThis.setTextSize(15);
            txtMaxBubbleThis.bringToFront();

            editor.putInt("adviceNumber", tip + 1);
            editor.commit();

            ObjectAnimator fadeOut2 = ObjectAnimator.ofFloat(txtMaxBubbleThis, "alpha", 0);
            fadeOut2.setInterpolator(new AccelerateInterpolator());
            fadeOut2.setStartDelay(utilities.ADVICE_DURATION);
            fadeOut2.setDuration(utilities.ADVICE_VELOCITY);
            fadeOut2.start();

        }


    }

    private void shakeAnimation (){

        soundPlayer.playSoundBomb(this);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

        for (int i = 0; i < 10; i++){

            piso[i].setLayerType(View.LAYER_TYPE_HARDWARE, null);

            ObjectAnimator shakeX = new ObjectAnimator().ofFloat(piso[i], "translationX", 5);
            shakeX.setDuration(ANIMATION_VELOCITY/2);
            shakeX.setInterpolator(new BounceInterpolator());

            ObjectAnimator shakex2 = new ObjectAnimator().ofFloat(piso[i], "translationX", 0);
            shakex2.setDuration(ANIMATION_VELOCITY);
            shakex2.setStartDelay(ANIMATION_VELOCITY);
            shakex2.setInterpolator(new BounceInterpolator());

            ObjectAnimator shakex3 = new ObjectAnimator().ofFloat(piso[i], "translationX", -5);
            shakex3.setDuration(ANIMATION_VELOCITY/2);
            shakex3.setStartDelay(ANIMATION_VELOCITY/2);
            shakex3.setInterpolator(new BounceInterpolator());

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(shakeX).with(shakex2).with(shakex3);
            animatorSet.start();

            final int j = i;

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd (Animator animation){
                    piso[j].setLayerType(View.LAYER_TYPE_NONE, null);
                    piso[j].setTranslationX(0);
                }
            });

        }

        txtMax.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        txtPuntaje.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator shakeX = new ObjectAnimator().ofFloat(txtMax, "translationX", 10);
        shakeX.setDuration(ANIMATION_VELOCITY);
        shakeX.setInterpolator(new BounceInterpolator());

        ObjectAnimator shakex2 = new ObjectAnimator().ofFloat(txtMax, "translationX", 0);
        shakex2.setDuration(ANIMATION_VELOCITY);
        shakex2.setStartDelay(ANIMATION_VELOCITY);
        shakex2.setInterpolator(new BounceInterpolator());

        ObjectAnimator shakeXScore = new ObjectAnimator().ofFloat(txtPuntaje, "translationX", 10);
        shakeXScore.setDuration(ANIMATION_VELOCITY);
        shakeXScore.setInterpolator(new BounceInterpolator());

        ObjectAnimator shakex2Score = new ObjectAnimator().ofFloat(txtPuntaje, "translationX", 0);
        shakex2Score.setDuration(ANIMATION_VELOCITY);
        shakex2Score.setStartDelay(ANIMATION_VELOCITY);
        shakex2Score.setInterpolator(new BounceInterpolator());



        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(shakeX).with(shakex2).with(shakeXScore).with(shakex2Score);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd (Animator animation){
                txtMax.setLayerType(View.LAYER_TYPE_NONE, null);
                txtPuntaje.setLayerType(View.LAYER_TYPE_NONE, null);
                txtMax.setTranslationX(0);
                txtPuntaje.setTranslationX(0);
            }
        });

    }

    public void animationDont(final int jugadaDont) {

        if (jugadaDont < 5) {
            casObjAccion[jugadaDont + NUM_COLUMNAS_BASE + 1].setAlpha(0.3f);
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(casObjAccion[jugadaDont + NUM_COLUMNAS_BASE + 1], "alpha", 0f);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setDuration(350);
            fadeOut.start();
        } else if (jugadaDont == 6) {
            txtBonusSwipe.setBackgroundResource(R.drawable.ic_bubble_dont);
            for (int i = 15; i <= (3 * NUM_COLUMNAS_BASE - (NUM_COLUMNAS_BASE - NUM_COLUMNAS)); i++) {
                casObjAccionbtn[i].setAlpha(0.3f);
                casObjAccionbtn[i].setBackgroundResource(R.drawable.ic_bubble_dont);
                final int j = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        casObjAccionbtn[j].setAlpha(0f);
                        casObjAccionbtn[j].setBackgroundResource(R.drawable.bubble_ok);

                    }
                }, ANIMATION_VELOCITY);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtBonusSwipe.setBackgroundResource(R.drawable.ic_fondo_casillero_bonus_vector_0);
                }
            }, ANIMATION_VELOCITY * 2);

        } else if (jugadaDont == 7) {
            txtBonusSwipe.setBackgroundResource(R.drawable.ic_bubble_dont);
            for (int i = 22; i <= (4 * NUM_COLUMNAS_BASE - (NUM_COLUMNAS_BASE - NUM_COLUMNAS)); i++) {
                casObjAccionbtn[i].setAlpha(0.3f);
                casObjAccionbtn[i].setBackgroundResource(R.drawable.ic_bubble_dont);
                final int j = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        casObjAccionbtn[j].setAlpha(0f);
                        casObjAccionbtn[j].setBackgroundResource(R.drawable.bubble_ok);
                    }
                }, ANIMATION_VELOCITY);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtBonusSwipe.setBackgroundResource(R.drawable.ic_fondo_casillero_bonus_vector_0);
                }
            }, ANIMATION_VELOCITY * 2);
        } else if (jugadaDont > 10) {
            txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_dont);

            final int casilleroDont = ((jugadaDont - 10) * NUM_COLUMNAS_BASE) + 1 + casilleroPlus;
            casObjAccionbtn[casilleroDont].setAlpha(0.3f);
            casObjAccionbtn[casilleroDont].setBackgroundResource(R.drawable.ic_bubble_dont);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    casObjAccionbtn[casilleroDont].setAlpha(0f);
                    casObjAccionbtn[casilleroDont].setBackgroundResource(R.drawable.bubble_ok);
                }
            }, ANIMATION_VELOCITY);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtBonusPlus.setBackgroundResource(R.drawable.ic_bubble_plus_0);
                }
            }, ANIMATION_VELOCITY * 2);
        }
    } //jugadaDont = 6 swipe en pisoAccion 2, 7 en pisoAccion 3


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        longPress = true;
        singleTap = true;
        singleTapBonusImage = false;
        longPressChangeLevel = false;
        longPressPlus = false;
        singleTapExchange = false;
        swipeZone = false;
        singleTapSwipeZone = true;
        accionPiso = 0;

        int action = event.getAction();


        switch (view.getId()) {
            case R.id.bonusSwipe:
                singleTap = false;
                longPress = false;
                singleTapBonusImage = true;
                typeBonusTutorial = 1;
                singleTapSwipeZone = false;
                swipeZone = false;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.bonusPlus:
                singleTap = false;
                longPress = false;
                singleTapBonusImage = true;
                typeBonusTutorial = 2;
                singleTapSwipeZone = false;
                swipeZone = false;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.bonusExchange:
                singleTap = false;
                longPress = false;
                singleTapBonusImage = true;
                typeBonusTutorial = 7;
                singleTapSwipeZone = false;
                swipeZone = false;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca8:
                jugadaColumnaPosible = 0;
                jugadaColumnaTap = 0;
                accionPiso = 0;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca9:
                jugadaColumnaPosible = 1;
                jugadaColumnaTap = 1;
                accionPiso = 0;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca10:
                jugadaColumnaPosible = 2;
                jugadaColumnaTap = 2;
                accionPiso = 0;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca11:
                jugadaColumnaPosible = 3;
                jugadaColumnaTap = 3;
                accionPiso = 0;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca12:
                jugadaColumnaPosible = 4;
                jugadaColumnaTap = 4;
                accionPiso = 0;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca15:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 0;
                accionPiso = 2;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca16:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 1;
                accionPiso = 2;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca17:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 2;
                accionPiso = 2;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca18:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 3;
                accionPiso = 2;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca19:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 4;
                accionPiso = 2;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca22:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 0;
                accionPiso = 3;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca23:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 1;
                accionPiso = 3;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca24:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 2;
                accionPiso = 3;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca25:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 3;
                accionPiso = 3;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.ca26:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                casilleroPlus = 4;
                accionPiso = 3;
                singleTapExchange = true;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.swipeAccion1:
                longPress = false;
                singleTap = false;
                longPressPlus = true;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                accionPiso = 2;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.swipeAccion2:
                longPress = false;
                singleTap = false;
                singleTapSwipeZone = false;
                longPressChangeLevel = false;
                accionPiso = 3;
                gestureDetector.onTouchEvent(event);
                break;
            case R.id.swipeZone:
                longPress = false;
                singleTap = false;
                longPressChangeLevel = false;
                swipeZone = true;
                gestureDetector.onTouchEvent(event);
                break;
        }


        return super.onTouchEvent(event);

    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {


        if (singleTap == true) {
            if (isTutorialRunning) {
                if (firstTutorial == 1) {
                    indications2();
                } else if (firstTutorial == 4) {
                    indications5();
                } else if (firstTutorial == 5) {
                    indications51();
                }
            } else {
                play(jugadaColumnaTap);
            }
        } else if (singleTapBonusImage == true) {
            if (isTutorialRunning == false) {
                if (typeBonusTutorial == 1) {
                    tutorialBonusSwipe();
                } else if (typeBonusTutorial == 2) {
                    tutorialPlus();
                } else if (typeBonusTutorial == 7) {
                    tutorialExchange();
                }
            }
        }

        if (singleTapExchange) {
            if (firstBubble == false) {
                preExchangeBubble(accionPiso, casilleroPlus);
            } else if (firstBubble) {
                exchangeBubble(accionPiso, casilleroPlus, false);
            }
        } else {
            if (firstBubble) {
                firstBubble = false;
                resetExchange();
            }
        }

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {


    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {

        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();
        singleTapExchange = false;

        if (firstBubble) {
            firstBubble = false;
            resetExchange();
        }

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                if (diffX > 0) {
                    onSwipeRifht();
                } else {
                    onSwipeLeft();
                }
                result = true;
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                result = true;
            }
        }
        return result;
    }

    private void onSwipeTop() {

        if (longPressPlus == true) {

            if (gameManager.player.getBonusPlus() > 0) {

                plusAction(accionPiso, casilleroPlus, 1, false);

            } else {

                soundPlayer.playSoundDont(this);
                animationDont(accionPiso + 10);

            }

        } else if (swipeZone == true) {

            if (isTutorialRunning) {
                if (firstTutorial == 5) {
                    indications51();
                }
            } else {
                play(swipeUp);
            }
            singleTapSwipeZone = false;
        }
    }

    private void onSwipeBottom() {

        if (longPressPlus == true) {

            if (gameManager.player.getBonusPlus() > 0) {

                plusAction(accionPiso, casilleroPlus, -1, false);

            } else {

                soundPlayer.playSoundDont(this);
                animationDont(accionPiso + 10);

            }

        }

    }

    private void onSwipeRifht() {
        switch (accionPiso) {
            case 0:
                if (swipeZone == true) {
                    if (isTutorialRunning) {
                        if (firstTutorial == 2) {
                            indications3();
                        }
                        break;
                    } else {
                        if (swipeRight < NUM_COLUMNAS) {
                            play(swipeRight);
                            singleTapSwipeZone = false;
                        } else {
                            soundPlayer.playSoundDont(this);
                        }
                    }
                }
                break;
            case 2:
                if (isTutorialRunning) {
                    indications8(2);
                }
                if (gameManager.player.getBonusSwipe() > 0) {
                    moveRow(2, 1, false);
                } else {
                    soundPlayer.playSoundDont(this);
                    animationDont(6);
                }
                break;
            case 3:
                if (gameManager.player.getBonusSwipe() > 0) {
                    moveRow(3, 1, false);
                } else {
                    soundPlayer.playSoundDont(this);
                    animationDont(7);
                }
                break;
        }
    }

    private void onSwipeLeft() {
        switch (accionPiso) {
            case 0:
                if (swipeZone == true) {
                    if (isTutorialRunning) {
                        if (firstTutorial == 4) {
                            indications5();
                        }
                        break;
                    } else {
                        if (swipeLeft >= 0) {
                            play(swipeLeft);
                            singleTapSwipeZone = false;
                        } else {
                            soundPlayer.playSoundDont(this);
                        }
                    }
                }
                break;
            case 2:
                if (isTutorialRunning) {
                    indications8(1);
                }
                if (gameManager.player.getBonusSwipe() > 0) {
                    moveRow(2, 0, false);
                } else {
                    soundPlayer.playSoundDont(this);
                    animationDont(6);
                }
                break;
            case 3:
                if (gameManager.player.getBonusSwipe() > 0) {
                    moveRow(3, 0, false);
                } else {
                    soundPlayer.playSoundDont(this);
                    animationDont(7);
                }
                break;
        }
    }

    @Override
    public void run() {

    }


}


