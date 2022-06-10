package com.example.prg208_android_programming_exam.fragment_2

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.example.prg208_android_programming_exam.ImageClickListener
import com.example.prg208_android_programming_exam.ImageInfo
import com.example.prg208_android_programming_exam.byteArrayToDrawable
import com.example.prg208_android_programming_exam.databinding.FragmentTwoCardCellBinding

class FragmentTwoCardViewHolder(
    //takes inn this parameters
    private val cardCellBinding: FragmentTwoCardCellBinding,
    private val clickListener: ImageClickListener
): RecyclerView.ViewHolder(cardCellBinding.root) {

    //uses the object to deploy what to be shown in the bind image.
    fun bindImage(imageInfo: ImageInfo) {

        val drawable: Drawable = byteArrayToDrawable(imageInfo.image, itemView.resources)

        //Lambda easy on click listener, activates the function inside.
            cardCellBinding.cell.setImageDrawable(drawable)
            cardCellBinding.cell.setOnClickListener {
                clickListener.onClick(imageInfo)
            }
    }
}