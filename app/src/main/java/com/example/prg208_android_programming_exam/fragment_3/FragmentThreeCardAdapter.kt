package com.example.prg208_android_programming_exam.fragment_3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prg208_android_programming_exam.ImageClickListener
import com.example.prg208_android_programming_exam.ImageInfo
import com.example.prg208_android_programming_exam.databinding.FragmentThreeCardCellBinding
import com.example.prg208_android_programming_exam.ifImageStartWithFile


class FragmentThreeCardAdapter(
    //takes inn this parameters
    private val imageArrayList: ArrayList<ImageInfo>,
    private val clickListener: ImageClickListener
    ): RecyclerView.Adapter<FragmentThreeCardViewHolder>() {

    //inflates the card view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentThreeCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentThreeCardCellBinding.inflate(from, parent, false)
        return FragmentThreeCardViewHolder(binding, clickListener)
    }

    //Gets the object from that position/ the index of getItemCount
    override fun onBindViewHolder(holder: FragmentThreeCardViewHolder, position: Int) {
        //Gets a filtered arraylist with only the original pictures.
        val filteredArray: ArrayList<ImageInfo> = ifImageStartWithFile(imageArrayList)
        holder.bindImage(filteredArray[position])
    }

    //Loops through the Array, and the size decides how many items there is in the recycler view.
    override fun getItemCount(): Int {
        val filteredArray: ArrayList<ImageInfo> = ifImageStartWithFile(imageArrayList)
        return filteredArray.size
    }
}