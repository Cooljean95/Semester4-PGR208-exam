package com.example.prg208_android_programming_exam

import android.graphics.drawable.Drawable

interface ImageClickListener {

    // Classes that implements this interface needs to also implement this members
    fun onClick (
        imageInfo: ImageInfo
    )

    fun onClickDelete (
        imageInfo: ImageInfo
    )
}