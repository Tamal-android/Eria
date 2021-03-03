package com.magicmind.eria.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File

object DImageUtils {

    fun loadImageFromUrl(
        mContext: Context?,
        imageUrl: String?,
        placeHolderImg: Int?,
        view: ImageView?
    ) {
        val options = placeHolderImg?.let {
            RequestOptions()
                .centerCrop()
                .placeholder(it)
                .error(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        }

        mContext?.let { context ->
            options?.let { reqOptions ->
                view?.let { imgView ->
                    Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .apply(reqOptions)
                        .into(imgView)
                }
            }
        }
    }

    fun loadImageFromUri(
        mContext: Context?,
        imageUri: Uri?,
        placeHolderImg: Int?,
        view: ImageView?
    ) {
        val options = placeHolderImg?.let {
            RequestOptions()
                .centerCrop()
                .placeholder(it)
                .error(it)
        }

        mContext?.let { context ->
            options?.let { reqOptions ->
                view?.let { imageView ->
                    Glide.with(context)
                        .asBitmap()
                        .load(File(imageUri?.path))
                        .apply(reqOptions)
                        .into(imageView)
                }
            }
        }
    }

    fun loadImageFromDrawable(
        mContext: Context?,
        imageResId: Int,
        view: ImageView?
    ) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(imageResId)
        mContext?.let { context ->
            view?.let { imgView ->
                Glide.with(context)
                    .asBitmap()
                    .load("")
                    .apply(options)
                    .into(imgView)
            }
        }
    }
}