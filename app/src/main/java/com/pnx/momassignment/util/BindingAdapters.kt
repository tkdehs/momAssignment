package com.pnx.momassignment.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pnx.momassignment.R

/**
 * layout.xml의 ImageView에서 URL 입력하여 사용
 * @sample imageFromUrl="@{modelView.imageUrl}"
 */
@BindingAdapter("imageFromUrl")
fun imageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
//                .override(200,200)
            .error(ContextCompat.getDrawable(view.context, R.drawable.ic_dummy))
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    Log.e("bindImageFromUrl", "onLoadFailed ${imageUrl}  ${e?.message}")
                    view.setImageURI(Uri.parse(imageUrl))
                    view.scaleType = ImageView.ScaleType.CENTER
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = ImageView.ScaleType.FIT_XY
                    return false
                }
            })
            .into(view)
    } else {
        view.setImageResource(R.drawable.ic_dummy)
        view.scaleType = ImageView.ScaleType.CENTER
    }
}
/**
 * RecyclerView Adapter의 마지막 스크롤 Listener
 * @sample recyclerViewLastScroll="@{() -> viewModel.onScrollPaging()}"
 */
@BindingAdapter("recyclerViewLastScroll")
fun recyclerViewLastScroll(recyclerView: RecyclerView, listener: InverseBindingListener) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1)) {
                listener.onChange()
            }
        }
    })
}
