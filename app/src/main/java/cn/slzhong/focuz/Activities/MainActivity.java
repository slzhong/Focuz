package cn.slzhong.focuz.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.neurosky.thinkgear.TGDevice;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.slzhong.focuz.Constants.CODES;
import cn.slzhong.focuz.Constants.URLS;
import cn.slzhong.focuz.Models.Recorder;
import cn.slzhong.focuz.Models.User;
import cn.slzhong.focuz.R;
import cn.slzhong.focuz.Utils.StorageUtil;

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
    private RelativeLayout main;
    private Button mainTimer;
    private Button mainStopwatch;
    private Button mainHistory;
    private Button mainStop;
    private TextView mainStatus;
    private RelativeLayout rate;
    private Button rate1;
    private Button rate2;
    private Button rate3;
    private Button rate4;
    private Button rate5;
    private Button rateSave;
    private LinearLayout result;
    private LinearLayout resultChart;
    private TextView resultStart;
    private TextView resultEnd;
    private TextView resultTime;
    private TextView resultAttention;
    private TextView resultMeditation;
    private TextView resultRate;
    private Button resultBack;
    private ScrollView resultScroll;
    private LinearLayout history;
    private LinearLayout historyList;
    private Button historyBack;
    private ScrollView historyScroll;

    // data
    private Handler animationHandler;
    private Handler tgHandler;
    private BluetoothAdapter bluetoothAdapter;
    private TGDevice tgDevice;
    private Recorder tgRecorder;
    private Runnable tgRunnable;
    private User user;

    // flags and temps
    private boolean tgConnected = false;
    private int tgAttention = 0;
    private int tgMeditation = 0;
    private int tgTime = -1;
    private int tgCount = 0;
    private boolean isHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        showWelcome();
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

        main = (RelativeLayout) findViewById(R.id.rl_main);
        mainTimer = (Button) findViewById(R.id.bt_timer);
        mainStopwatch = (Button) findViewById(R.id.bt_stopwatch);
        mainHistory = (Button) findViewById(R.id.bt_history);
        mainStop = (Button) findViewById(R.id.bt_stop);
        mainStatus = (TextView) findViewById(R.id.tv_status);
        mainTimer.setOnClickListener(new TimerListener());
        mainStopwatch.setOnClickListener(new StopwatchListener());
        mainStop.setOnClickListener(new StopListener());
        mainHistory.setOnClickListener(new HistoryListener());

        rate = (RelativeLayout) findViewById(R.id.rl_rate);
        rate1 = (Button) findViewById(R.id.bt_rate_1);
        rate2 = (Button) findViewById(R.id.bt_rate_2);
        rate3 = (Button) findViewById(R.id.bt_rate_3);
        rate4 = (Button) findViewById(R.id.bt_rate_4);
        rate5 = (Button) findViewById(R.id.bt_rate_5);
        rateSave = (Button) findViewById(R.id.bt_save);
        rate1.setOnClickListener(new RateListener());
        rate2.setOnClickListener(new RateListener());
        rate3.setOnClickListener(new RateListener());
        rate4.setOnClickListener(new RateListener());
        rate5.setOnClickListener(new RateListener());
        rateSave.setOnClickListener(new SaveListener());

        result = (LinearLayout) findViewById(R.id.ll_result);
        resultChart = (LinearLayout) findViewById(R.id.ll_chart);
        resultStart = (TextView) findViewById(R.id.tv_start);
        resultEnd = (TextView) findViewById(R.id.tv_end);
        resultTime = (TextView) findViewById(R.id.tv_time);
        resultAttention = (TextView) findViewById(R.id.tv_attention);
        resultMeditation = (TextView) findViewById(R.id.tv_meditation);
        resultRate = (TextView) findViewById(R.id.tv_rate);
        resultBack = (Button) findViewById(R.id.bt_back);
        resultScroll = (ScrollView) findViewById(R.id.sv_result);
        resultBack.setOnClickListener(new BackListener());

        history = (LinearLayout) findViewById(R.id.ll_history);
        historyList = (LinearLayout) findViewById(R.id.ll_list);
        historyBack = (Button) findViewById(R.id.bt_main);
        historyScroll = (ScrollView) findViewById(R.id.sv_history);
        historyBack.setOnClickListener(new BackListener());
        loadHistoryList();

        // hide status bar
        enterFullscreen();
    }

    private void initData() {
        user = User.getInstance(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        animationHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == CODES.SIGN_SUCCESS) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                    hideScene(login);
                    showScene(main);
                    connectTG();
                }
            }
        };

        tgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TGDevice.MSG_STATE_CHANGE:
                        switch (msg.arg1) {
                            case TGDevice.STATE_IDLE:
                                break;
                            case TGDevice.STATE_CONNECTING:
                                break;
                            case TGDevice.STATE_CONNECTED:
                                tgDevice.start();
                                tgConnected = true;
                                mainStatus.setText("");
                                break;
                            case TGDevice.STATE_DISCONNECTED:
                                mainStatus.setText("THINK GEAR DEVICE NOT DISCONNECTED");
                                break;
                            case TGDevice.STATE_NOT_FOUND:
                            case TGDevice.STATE_NOT_PAIRED:
                                mainStatus.setText("THINK GEAR DEVICE NOT FOUND");
                                break;
                            default:
                                break;
                        }
                        break;
                    case TGDevice.MSG_MEDITATION:
                        System.out.println("***** m" + msg.arg1);
                        tgMeditation = msg.arg1;
                        break;
                    case TGDevice.MSG_ATTENTION:
                        System.out.println("***** a" + msg.arg1);
                        tgAttention = msg.arg1;
                        break;
                    case TGDevice.MSG_RAW_DATA:
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void enterFullscreen() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void loadHistoryList() {
        historyList.removeAllViews();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File[] files = StorageUtil.listFiles();
        if (files != null && files.length > 0) {
            for (int i = files.length - 1; i >= 0; i--) {
                final File file = files[i];
                TextView textView = new TextView(this);
                textView.setText(sdf.format(new Date(Long.parseLong(file.getName()))));
                textView.setTextColor(getResources().getColor(R.color.main_bright));
                textView.setTextSize(20);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 40, 0, 40);
                textView.setLayoutParams(layoutParams);
                historyList.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isHistory = true;
                        String data = StorageUtil.readStringFromFile(file.toString());
                        try {
                            JSONTokener jsonTokener = new JSONTokener(data);
                            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                            showResult(new Recorder(jsonObject));
                            hideScene(history);
                            showScene(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
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
                if (user.hasSignedIn) {
                    showScene(main);
                    connectTG();
                } else {
                    showScene(login);
                }
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
        welcomeBackground.setVisibility(View.GONE);
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

    private void showScene(View scene) {
        if (scene.getId() == R.id.ll_history) {
            historyScroll.setVisibility(View.VISIBLE);
        } else if (scene.getId() == R.id.ll_result) {
            resultScroll.setVisibility(View.VISIBLE);
        }

        scene.setVisibility(View.VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300);
        animationSet.setFillEnabled(true);
        animationSet.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        animationSet.addAnimation(alphaAnimation);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.7f, 1, 0.7f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(scaleAnimation);

        scene.startAnimation(animationSet);
    }

    private void hideScene(final View scene) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillEnabled(true);
        alphaAnimation.setFillAfter(true);
        scene.startAnimation(alphaAnimation);
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scene.setVisibility(View.GONE);
                if (scene.getId() == R.id.ll_history) {
                    historyScroll.setVisibility(View.GONE);
                } else if (scene.getId() == R.id.ll_result) {
                    resultScroll.setVisibility(View.GONE);
                }
            }
        }, 310);
    }

    /**
     * funcitions of notices like alert and toast
     */
    private void showAlert(String msg) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Looper.prepare();
        }
        new AlertDialog.Builder(this)
                .setTitle("NOTICE")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Looper.loop();
        }
    }

    /**
     * functions of recording data
     */
    private void connectTG() {
        if (bluetoothAdapter != null) {
            if (tgDevice != null) {
                tgDevice.close();
                tgDevice = null;
            }
            tgDevice = new TGDevice(bluetoothAdapter, tgHandler);
            tgDevice.connect(true);
            mainStatus.setText("CONNECTING TO THINK GEAR DEVICE...");
        }
    }

    private void startLoop() {
        if (tgRecorder != null) {
            tgRunnable = new Runnable() {
                @Override
                public void run() {
                    tgCount++;
                    tgRecorder.pushAttention(tgAttention);
                    tgRecorder.pushMeditation(tgMeditation);
                    tgHandler.postDelayed(this, 15000);
                }
            };
            tgHandler.postDelayed(tgRunnable, 0);
        }
    }

    private void showResult(Recorder recorder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resultStart.setText("" + sdf.format(new Date(recorder.startAt)));
        resultEnd.setText("" + sdf.format(new Date(recorder.endAt)));

        long span = Math.round((recorder.endAt - recorder.startAt) / 1000.0);
        String spanString = (span % 60) + "S";
        span = span / 60;
        spanString = span > 0 ? (span % 60) + "M " + spanString : spanString;
        span = span / 60;
        spanString = span > 0 ? (span % 60) + "H " + spanString : spanString;
        resultTime.setText(spanString);

        resultAttention.setText("" + recorder.attention);
        resultMeditation.setText("" + recorder.meditation);
        resultRate.setText("" + recorder.rate);

        showChart(recorder.attentions);
    }

    private void showChart(List<Integer> data) {
        resultChart.removeAllViews();

        XYMultipleSeriesRenderer multipleSeriesRenderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        seriesRenderer.setColor(getResources().getColor(R.color.theme));
        seriesRenderer.setPointStyle(PointStyle.CIRCLE);
        seriesRenderer.setFillPoints(true);
        seriesRenderer.setLineWidth(5);
        multipleSeriesRenderer.addSeriesRenderer(seriesRenderer);
        multipleSeriesRenderer.setXAxisMin(0);
        multipleSeriesRenderer.setXAxisMax(data.size() - 1);
        multipleSeriesRenderer.setYAxisMin(0);
        multipleSeriesRenderer.setYAxisMax(100);
        multipleSeriesRenderer.setAxesColor(getResources().getColor(R.color.main_bright));
        multipleSeriesRenderer.setLabelsColor(getResources().getColor(R.color.main_bright));
        multipleSeriesRenderer.setShowGrid(true);
        multipleSeriesRenderer.setGridColor(getResources().getColor(R.color.main_bright));
        multipleSeriesRenderer.setXLabels(data.size());
        multipleSeriesRenderer.setYLabels(10);
        multipleSeriesRenderer.setLabelsTextSize(22);
        multipleSeriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multipleSeriesRenderer.setPointSize((float) 10);
        multipleSeriesRenderer.setShowLegend(false);

        XYSeries series = new XYSeries("ATTENTION LEVEL");
        for (int i = 0; i < data.size(); i++) {
            series.add(i, data.get(i));
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        GraphicalView graphicalView = ChartFactory.getLineChartView(this, dataset, multipleSeriesRenderer);
        resultChart.addView(graphicalView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject result = user.signInAndUp(loginAccount.getText().toString(), loginPassword.getText().toString(), URLS.SIGNIN);
                            if (result == null) {
                                showAlert("UNKNOWN ERROR");
                            } else if (result.getInt("code") == 500) {
                                showAlert(result.getString("msg"));
                            } else {
                                Message message = new Message();
                                message.what = CODES.SIGN_SUCCESS;
                                animationHandler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert("UNKNOWN ERROR");
                        }
                    }
                }).start();
            }
        }
    }

    private class SignupListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (loginConfirm.getVisibility() == View.GONE) {
                loginConfirm.setVisibility(View.VISIBLE);
            } else if (!loginPassword.getText().toString().equals(loginConfirm.getText().toString())) {
                showAlert("PASSWORDS DO NOT MATCH");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject result = user.signInAndUp(loginAccount.getText().toString(), loginPassword.getText().toString(), URLS.SIGNUP);
                            if (result == null) {
                                showAlert("UNKNOWN ERROR");
                            } else if (result.getInt("code") == 500) {
                                showAlert(result.getString("msg"));
                            } else {
                                Message message = new Message();
                                message.what = CODES.SIGN_SUCCESS;
                                animationHandler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert("UNKNOWN ERROR");
                        }
                    }
                }).start();
            }
        }
    }

    private class TimerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (tgConnected) {
                tgRecorder = new Recorder();
                mainTimer.setVisibility(View.GONE);
                mainStopwatch.setVisibility(View.GONE);
                mainHistory.setVisibility(View.GONE);
                mainStop.setVisibility(View.VISIBLE);
                startLoop();
            } else {
                showAlert("CONNECT TO A THINK GEAR DEVICE BEFORE STARTING A TASK");
            }
        }
    }

    private class StopwatchListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (tgConnected) {

            } else {
                showAlert("CONNECT TO A THINK GEAR DEVICE BEFORE STARTING A TASK");
            }
        }
    }

    private class HistoryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            hideScene(main);
            showScene(history);
        }
    }

    private class StopListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tgRecorder.stop();
            tgCount = 0;
            tgHandler.removeCallbacks(tgRunnable);
            mainTimer.setVisibility(View.VISIBLE);
            mainStopwatch.setVisibility(View.VISIBLE);
            mainHistory.setVisibility(View.VISIBLE);
            mainStop.setVisibility(View.GONE);
            hideScene(main);
            showScene(rate);
        }
    }

    private class RateListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (tgRecorder != null) {
                if (v.getId() == R.id.bt_rate_1) {
                    tgRecorder.setRate(1);
                    rate1.setBackgroundResource(R.drawable.rate_selected);
                    rate2.setBackgroundResource(R.drawable.rate_normal);
                    rate3.setBackgroundResource(R.drawable.rate_normal);
                    rate4.setBackgroundResource(R.drawable.rate_normal);
                    rate5.setBackgroundResource(R.drawable.rate_normal);
                } else if (v.getId() == R.id.bt_rate_2) {
                    tgRecorder.setRate(2);
                    rate1.setBackgroundResource(R.drawable.rate_selected);
                    rate2.setBackgroundResource(R.drawable.rate_selected);
                    rate3.setBackgroundResource(R.drawable.rate_normal);
                    rate4.setBackgroundResource(R.drawable.rate_normal);
                    rate5.setBackgroundResource(R.drawable.rate_normal);
                } else if (v.getId() == R.id.bt_rate_3) {
                    tgRecorder.setRate(3);
                    rate1.setBackgroundResource(R.drawable.rate_selected);
                    rate2.setBackgroundResource(R.drawable.rate_selected);
                    rate3.setBackgroundResource(R.drawable.rate_selected);
                    rate4.setBackgroundResource(R.drawable.rate_normal);
                    rate5.setBackgroundResource(R.drawable.rate_normal);
                } else if (v.getId() == R.id.bt_rate_4) {
                    tgRecorder.setRate(4);
                    rate1.setBackgroundResource(R.drawable.rate_selected);
                    rate2.setBackgroundResource(R.drawable.rate_selected);
                    rate3.setBackgroundResource(R.drawable.rate_selected);
                    rate4.setBackgroundResource(R.drawable.rate_selected);
                    rate5.setBackgroundResource(R.drawable.rate_normal);
                } else if (v.getId() == R.id.bt_rate_5) {
                    tgRecorder.setRate(5);
                    rate1.setBackgroundResource(R.drawable.rate_selected);
                    rate2.setBackgroundResource(R.drawable.rate_selected);
                    rate3.setBackgroundResource(R.drawable.rate_selected);
                    rate4.setBackgroundResource(R.drawable.rate_selected);
                    rate5.setBackgroundResource(R.drawable.rate_selected);
                }
            }
        }
    }

    private class SaveListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (tgRecorder != null) {
                tgRecorder.save();
                showResult(tgRecorder);
                hideScene(rate);
                showScene(result);
                loadHistoryList();
            }
        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_back) {
                hideScene(result);
                if (isHistory) {
                    showScene(history);
                } else {
                    showScene(main);
                }
            } else if (v.getId() == R.id.bt_main) {
                hideScene(history);
                showScene(main);
            }
            isHistory = false;
        }
    }

}
