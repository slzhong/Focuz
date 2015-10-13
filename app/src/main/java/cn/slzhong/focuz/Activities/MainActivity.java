package cn.slzhong.focuz.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.slzhong.focuz.R;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {

    // views
    private RelativeLayout root;
    private RelativeLayout welcome;
    private TextView welcomeTitle;
    private TextView welcomeCopyright;
    private ImageView welcomeIcon;
    private View welcomeBackground;
    private View welcomeBackgroundPseudo;
    private RelativeLayout login;
    private EditText loginAccount;
    private EditText loginPassword;
    private EditText loginConfirm;
    private Button loginSignin;
    private Button loginSignup;

    // data
    private Handler animationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
//        showAnimations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterFullscreen();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void initViews() {
        // init components
        root = (RelativeLayout) findViewById(R.id.fl_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterFullscreen();
            }
        });
        welcome = (RelativeLayout) findViewById(R.id.rl_welcome);
        welcomeTitle = (TextView) findViewById(R.id.tv_welcome_title);
        welcomeCopyright = (TextView) findViewById(R.id.tv_welcome_copyright);
        welcomeIcon = (ImageView) findViewById(R.id.iv_welcome_icon);
        welcomeBackground = findViewById(R.id.ll_welcome_background);
        welcomeBackgroundPseudo = findViewById(R.id.ll_welcome_background_pseudo);

        login = (RelativeLayout) findViewById(R.id.rl_login);
        loginAccount = (EditText) findViewById(R.id.et_account);
        loginPassword = (EditText) findViewById(R.id.et_password);
        loginConfirm = (EditText) findViewById(R.id.et_confirm);
        loginSignin = (Button) findViewById(R.id.bt_signin);
        loginSignup = (Button) findViewById(R.id.bt_signup);
        loginSignin.setOnClickListener(new SigninListener());
        loginSignup.setOnClickListener(new SignupListener());

        // hide actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // hide status bar
        enterFullscreen();
    }

    private void initData() {
        animationHandler = new Handler();
    }

    private void enterFullscreen() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * functions of animations
     */
    private void showWelcome() {
        showWelcomeAnimation();

        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTitleAnimation();
            }
        }, 2100);
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCopyrightAnimation();
            }
        }, 2800);
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideWelcomeAnimation();
            }
        }, 5200);
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMain();
            }
        }, 5300);
    }

    private void showWelcomeAnimation() {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillEnabled(true);
        animationSet.setFillAfter(true);

        TranslateAnimation moveUp = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -4);
        moveUp.setDuration(600);
        moveUp.setStartOffset(100);
        moveUp.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(moveUp);

        TranslateAnimation dropDown = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 3.5f);
        dropDown.setDuration(525);
        dropDown.setStartOffset(800);
        dropDown.setInterpolator(new AccelerateInterpolator());
        animationSet.addAnimation(dropDown);

        TranslateAnimation expandTranslate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.1f);
        expandTranslate.setDuration(200);
        expandTranslate.setStartOffset(1325);
        expandTranslate.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(expandTranslate);

        ScaleAnimation expandScale = new ScaleAnimation(
                1, 20, 1, 20,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        expandScale.setDuration(300);
        expandScale.setStartOffset(1325);
        expandScale.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(expandScale);

        welcomeBackground.startAnimation(animationSet);
    }

    private void moveTitleAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1f);
        translateAnimation.setDuration(500);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        welcomeTitle.startAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        welcomeIcon.setVisibility(View.VISIBLE);
        welcomeIcon.startAnimation(alphaAnimation);
    }

    private void showCopyrightAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(200);
        welcomeCopyright.setVisibility(View.VISIBLE);
        welcomeCopyright.startAnimation(alphaAnimation);
    }

    private void hideWelcomeAnimation() {
        welcomeBackground.setVisibility(View.INVISIBLE);
        welcomeBackgroundPseudo.setVisibility(View.VISIBLE);

        ScaleAnimation scaleContent = new ScaleAnimation(
                1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleContent.setDuration(300);
        scaleContent.setFillAfter(true);
        welcome.startAnimation(scaleContent);

        ScaleAnimation scaleBackground = new ScaleAnimation(
                2, 0, 2, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleBackground.setDuration(300);
        scaleBackground.setFillAfter(true);
        welcomeBackgroundPseudo.startAnimation(scaleBackground);
    }

    private void showMain() {
        login.setVisibility(View.VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(200);
        animationSet.setFillEnabled(true);
        animationSet.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        animationSet.addAnimation(alphaAnimation);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.7f, 1, 0.7f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(scaleAnimation);

        login.startAnimation(animationSet);
    }

    /**
     * funcitions of notices like alert and toast
     */
    private void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("NOTICE")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * private classes
     */
    private class SigninListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (loginAccount.getText().length() * loginPassword.getText().length() == 0) {
                showAlert("PLEASE ENTER ACCOUNT AND PASSWORD");
            } else {

            }
        }
    }

    private class SignupListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (loginConfirm.getVisibility() == View.GONE) {
                loginConfirm.setVisibility(View.VISIBLE);
            } else if (!loginPassword.getText().equals(loginConfirm.getText())) {
                showAlert("PASSWORDS DO NOT MATCH");
            } else {

            }
        }
    }

}
