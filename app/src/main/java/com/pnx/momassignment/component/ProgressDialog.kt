package com.pnx.momassignment.component

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.pnx.momassignment.R

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_progress)

        (findViewById<LottieAnimationView>(R.id.img_progress)).apply {
           setAnimation(R.raw.progress)
           playAnimation()
           repeatCount = LottieDrawable.INFINITE
        }
//        Glide.with(context).asGif().load(R.raw.progress_gif).into(imgProgress);
    }
}