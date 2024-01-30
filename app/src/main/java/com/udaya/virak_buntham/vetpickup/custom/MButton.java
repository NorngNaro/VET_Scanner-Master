package com.udaya.virak_buntham.vetpickup.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.widget.AppCompatButton;
import com.udaya.virak_buntham.vetpickup.R;

public class MButton extends AppCompatButton{
    public MButton(Context context) {
        super(context);
    }

    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showAnimation(Context mContext){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_apha);
        this.startAnimation(animation);
    }
}
