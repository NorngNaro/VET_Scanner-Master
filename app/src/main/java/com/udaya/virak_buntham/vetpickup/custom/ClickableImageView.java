package com.udaya.virak_buntham.vetpickup.custom;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.udaya.virak_buntham.vetpickup.R;

public class ClickableImageView extends AppCompatImageView{
    public ClickableImageView(Context context) {
        super(context);
    }

    public ClickableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showAnimation(Context mContext){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_apha);
        this.startAnimation(animation);
    }
}
