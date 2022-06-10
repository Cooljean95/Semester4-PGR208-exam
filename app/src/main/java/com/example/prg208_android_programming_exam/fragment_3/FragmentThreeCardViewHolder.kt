package com.example.prg208_android_programming_exam.fragment_3

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.example.prg208_android_programming_exam.ImageClickListener
import com.example.prg208_android_programming_exam.ImageInfo
import com.example.prg208_android_programming_exam.byteArrayToDrawable
import com.example.prg208_android_programming_exam.databinding.FragmentThreeCardCellBinding


class FragmentThreeCardViewHolder(
    private val cardCellBinding: FragmentThreeCardCellBinding,
    private val clickListener: ImageClickListener,
): RecyclerView.ViewHolder(cardCellBinding.root) {

    //uses the object to deploy what to be shown in the bind image.
    fun bindImage(imageInfo: ImageInfo) {
        //in this case only a image.
        val drawable: Drawable = byteArrayToDrawable(imageInfo.image, itemView.resources )
            cardCellBinding.image.setImageDrawable(drawable)

        //A lambda on click listener, that activates a function, and show a new recyclerview
            cardCellBinding.cardView.setOnClickListener {
                clickListener.onClick(imageInfo)
            }

        //A lambda on long click listener, that activates a function, and deletes the original image.
            cardCellBinding.cardView.setOnLongClickListener {
                clickListener.onClickDelete(imageInfo)
                true
            }
    }
}

