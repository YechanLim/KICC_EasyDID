package com.example.easydid.ui.activity;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easydid.EasyDidApplication;
import com.example.easydid.R;
import com.example.easydid.ui.fragment.EasyPreferenceFragment;
import com.example.easydid.ui.fragment.EasyVideoFragment;
import com.example.easydid.util.EasyAdapter;
import com.example.easydid.databinding.ActivityEasydidBinding;
import com.example.easydid.util.EasySocketServerAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class EasyDidActivity extends AppCompatActivity {

    SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();
    private ActivityEasydidBinding binding;
    private EasyDidViewModel viewModel;
    private EasyAdapter.CardArrayAdapter cardArrayAdapter;
    private EasyAdapter.CardArrayAdapter2 cardArrayAdapter2;
    private int prevCardCnt = 0;
    private List<String> prevCardList = new ArrayList<>();
    private EasySocketServerAsync serverAsync = new EasySocketServerAsync();
    private int animBlinkDuration = 6;
    private Boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_easydid);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(EasyDidViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        InitView();
        InitPrefs();

        Animation animLeftToRight = AnimationUtils.loadAnimation(this, R.anim.slide_to_right);
        AnimationSet animBlink = (AnimationSet)AnimationUtils.loadAnimation(this, R.anim.blink);
        animBlink.getAnimations().get(0).setRepeatCount(animBlinkDuration);

        viewModel.mCardList.observe(binding.getLifecycleOwner(), cardList -> {
            if (cardList.size() > 0) {
                binding.textViewRcntCallNum.setVisibility(View.VISIBLE);
                if(isTimerRunning.equals(false)) {
                    viewModel.orderRmvTimer = new Timer();
                    viewModel.orderRmvTimer.schedule(new OrderRmvTask(), viewModel.rmvTimeCycle, viewModel.rmvTimeCycle);
                    isTimerRunning = true;
                }
                //카드리스트 개수가 이전과 같거나 클 경우
                if (cardList.size() >= prevCardCnt) {
                    //1개 초과일 시, 리스트 추가
                    if(cardList.size() > 1) {
                        binding.textViewRcntCallNumFrame.startAnimation(animLeftToRight);
                    }
                    //1개일 시, 호출 번호만 반짝
                    else {
                        binding.textViewRcntCallNum.setText(prevCardList.get(0));
                        binding.textViewRcntCallNumFrame.setAnimation(animBlink);
                    }

                    cardArrayAdapter = new EasyAdapter.CardArrayAdapter(cardList.subList(1, cardList.size()));
                    binding.recyclerviewCard.setAdapter(cardArrayAdapter);
                }
                //줄어들었을 때
                else {
                    binding.textViewRcntCallNum.setText(cardList.get(0));
                    cardArrayAdapter2 = new EasyAdapter.CardArrayAdapter2(cardList.subList(1, cardList.size()));
                    binding.recyclerviewCard.setAdapter(cardArrayAdapter2);
                }
            } else {
                if(isTimerRunning.equals(true)){
                    viewModel.orderRmvTimer.cancel();
                    isTimerRunning = false;
                }
                binding.textViewRcntCallNum.setVisibility(View.GONE);
            }

            prevCardCnt = cardList.size();
            prevCardList = cardList;
        });

        animLeftToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(prevCardList.size() > 0) {
                    binding.textViewRcntCallNum.setText(prevCardList.get(0));
                    binding.textViewRcntCallNumFrame.setAnimation(animBlink);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void InitView()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.video_view_frame, new EasyVideoFragment(), "video").commit();
        binding.textViewRcntCallNumFrame.bringToFront();

        binding.recyclerviewCard.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewCard.addItemDecoration(new RecyclerViewDecoration(10));

        binding.btnEasydidSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, EasyPreferenceActivity.class);
            startActivity(intent);
        });
    }

    private void InitPrefs()
    {
        Boolean useBasicCardYn = true;

        if(prefs.contains("pref_key_easydid_theme"))
        {
            String strTheme = prefs.getString("pref_key_easydid_theme", "0");
            Resources res = getResources();
            Drawable drwBkgrnd = ResourcesCompat.getDrawable(res, R.drawable.border_white_bkgrnd_black, null);
            Drawable drwBkgrnd2 = ResourcesCompat.getDrawable(res, R.drawable.border_white_bkgrnd_black, null);
            Drawable drwLogoWhite = ResourcesCompat.getDrawable(res, R.drawable.icon_easydid_logo_white, null);
            Drawable drwSettingImg = ResourcesCompat.getDrawable(res, R.drawable.icon_settings_white, null);

            if(strTheme.equals("1")) {
//                this.setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
                binding.topBar.setBackground(drwBkgrnd);
                binding.layoutCardList.setBackground(drwBkgrnd2);
                binding.logoEasydid.setImageDrawable(drwLogoWhite);
                binding.btnEasydidSettings.setImageDrawable(drwSettingImg);
            }
        }

        if(prefs.contains("pref_key_easydid_use_top_bar_yn"))
        {
            Boolean useTopBar = true;
            useTopBar = prefs.getBoolean("pref_key_easydid_use_top_bar_yn", true);
            if(useTopBar)
                binding.topBar.setVisibility(View.VISIBLE);
            else
                binding.topBar.setVisibility(View.GONE);
        }

        if(prefs.contains("pref_key_easydid_use_basic_card_yn"))
        {
            useBasicCardYn = prefs.getBoolean("pref_key_easydid_use_basic_card_yn", true);
        }

        if(!useBasicCardYn) {

            if (prefs.contains("pref_key_easydid_call_num_background")) {
                int intColor = prefs.getInt("pref_key_easydid_call_num_background", -1);
                binding.textViewRcntCallNumFrame.setBackgroundTintList(ColorStateList.valueOf(intColor));
            }

            if(prefs.contains("pref_key_easydid_call_num_text_color")) {
                int intColor = prefs.getInt("pref_key_easydid_call_num_text_color", -16777216);
                binding.textViewRcntCallNum.setTextColor(intColor);
            }

        }

        if(prefs.contains("pref_key_easydid_call_num_flicker_time")) {
            animBlinkDuration = Integer.parseInt(prefs.getString("pref_key_easydid_call_num_flicker_time", "3"))*2;
        }
    }

    public class OrderRmvTask extends TimerTask {

        private Boolean isRunning = false;

        @Override
        public void run() {
            if (viewModel.cardList.size() > 0){
                viewModel.cardList.remove(viewModel.cardList.size()-1);
                viewModel.mCardList.postValue(viewModel.cardList);
            }
        }
    }

    public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
        private final int divHeight;

        public RecyclerViewDecoration(int divHeight) {
            this.divHeight = divHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = divHeight;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.textViewRcntCallNumFrame.clearAnimation();
        if(isTimerRunning.equals(true)) {
            viewModel.orderRmvTimer.cancel();
        }
        if(prevCardList.size() > 0)
        {
            EasyDidApplication.getInstance().setCardList(prevCardList);
        }
    }


}
