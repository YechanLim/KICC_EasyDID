package com.example.easydid.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easydid.EasyDidApplication;
import com.example.easydid.R;

import java.util.ArrayList;
import java.util.List;

public class EasyAdapter {

    public static class CardArrayAdapter extends RecyclerView.Adapter<CardArrayAdapter.ViewHolder> {
        private List<String> cardList;
        long animDuration = 400;
        private boolean on_attach = true;

        public CardArrayAdapter(List<String> dataSet){
            cardList = dataSet;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView textViewCallNum;
            private final FrameLayout textViewCallNumFrame;
            private SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();
            private Boolean useBasicCardYn = true;

            public ViewHolder(View view){
                super(view);

                textViewCallNum = view.findViewById(R.id.text_view_call_num);
                textViewCallNumFrame = view.findViewById(R.id.text_view_call_num_frame);

                if(prefs.contains("pref_key_easydid_use_basic_card_yn"))
                {
                    useBasicCardYn = prefs.getBoolean("pref_key_easydid_use_basic_card_yn", true);
                }

                if(!useBasicCardYn) {

                    if (prefs.contains("pref_key_easydid_call_num_background")) {
                        int intColor = prefs.getInt("pref_key_easydid_call_num_background", -1);
                        textViewCallNumFrame.setBackgroundTintList(ColorStateList.valueOf(intColor));
                    }

                    if (prefs.contains("pref_key_easydid_call_num_text_color")) {
                        int intColor = prefs.getInt("pref_key_easydid_call_num_text_color", -16777216);
                        textViewCallNum.setTextColor(intColor);
                    }
                }
            }

            public TextView getTextView(){
                return textViewCallNum;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EasyAdapter.CardArrayAdapter.ViewHolder viewHolder, int position) {
            viewHolder.textViewCallNum.setText(cardList.get(position));

            if(position == 0) {
                setAnimation(viewHolder.itemView, position);
            }
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    on_attach = false;
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });

            super.onAttachedToRecyclerView(recyclerView);
        }

        private void setAnimation(View itemView, int i){
            if(!on_attach){
                i = -1;
            }
            boolean not_first_item = i == -1;
            i = i + 1;
            itemView.setTranslationX(-400f);
            itemView.setAlpha(0.f);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(itemView, "translationX", -400f, 0);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
            ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
            animatorTranslateY.setStartDelay(not_first_item ? animDuration : (i * animDuration));
            animatorTranslateY.setDuration((not_first_item ? 2 : 1) * animDuration);
            animatorSet.playTogether(animatorTranslateY, animatorAlpha);
            animatorSet.start();
        }

    }

    public static class CardArrayAdapter2 extends RecyclerView.Adapter<CardArrayAdapter2.ViewHolder> {

        private List<String> cardList;
        public CardArrayAdapter2(List<String> dataSet){
            cardList = dataSet;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView textViewCallNum;
            private final FrameLayout textViewCallNumFrame;
            private SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();
            private Boolean useBasicCardYn = true;

            public ViewHolder(View view){
                super(view);
                textViewCallNum = view.findViewById(R.id.text_view_call_num);
                textViewCallNumFrame = view.findViewById(R.id.text_view_call_num_frame);

                if(prefs.contains("pref_key_easydid_use_basic_card_yn"))
                {
                    useBasicCardYn = prefs.getBoolean("pref_key_easydid_use_basic_card_yn", true);
                }

                if(!useBasicCardYn) {

                    if (prefs.contains("pref_key_easydid_call_num_background")) {
                        int intColor = prefs.getInt("pref_key_easydid_call_num_background", -1);
                        textViewCallNumFrame.setBackgroundTintList(ColorStateList.valueOf(intColor));
                    }

                    if (prefs.contains("pref_key_easydid_call_num_text_color")) {
                        int intColor = prefs.getInt("pref_key_easydid_call_num_text_color", -16777216);
                        textViewCallNum.setTextColor(intColor);
                    }
                }
            }

            public TextView getTextView(){
                return textViewCallNum;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EasyAdapter.CardArrayAdapter2.ViewHolder viewHolder, int position) {
            viewHolder.textViewCallNum.setText(cardList.get(position));
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

    }
}
