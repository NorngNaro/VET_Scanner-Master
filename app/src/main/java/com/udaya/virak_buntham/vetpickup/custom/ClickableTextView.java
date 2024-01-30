package com.udaya.virak_buntham.vetpickup.custom;

import android.content.Context;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.udaya.virak_buntham.vetpickup.R;

public class ClickableTextView extends AppCompatTextView{
    public ClickableTextView(Context context) {
        super(context);
    }

    public ClickableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showAnimation(Context mContext){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_apha);
        this.startAnimation(animation);
    }
}
