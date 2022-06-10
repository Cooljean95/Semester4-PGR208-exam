package com.example.prg208_android_programming_exam.fragment_3

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.prg208_android_programming_exam.ImageClickListener
import com.example.prg208_android_programming_exam.ImageInfo
import com.example.prg208_android_programming_exam.byteArrayToDrawable
import com.example.prg208_android_programming_exam.databinding.FragmentThreeCardCellBinding
import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty0

class FragmentThreeCardViewHolderEdit(
    private val cardCellBinding: FragmentThreeCardCellBinding,
): RecyclerView.ViewHolder(cardCellBinding.root){

    //uses the object to deploy what to be shown in the bind image.
    fun bindImage(imageInfo: ImageInfo, clickListenerDelete: ImageClickListener) {
        //in this case only a image.
        val drawable: Drawable = byteArrayToDrawable(imageInfo.image, itemView.resources )
            cardCellBinding.image.setImageDrawable(drawable)

        //A lambda on click listener, that activates a function, and deletes the image
            cardCellBinding.cardView.setOnClickListener {
                clickListenerDelete.onClickDelete(imageInfo)
            }
    }
}