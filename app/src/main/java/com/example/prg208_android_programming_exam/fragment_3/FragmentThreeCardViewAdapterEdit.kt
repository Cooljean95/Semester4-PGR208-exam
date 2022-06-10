package com.example.prg208_android_programming_exam.fragment_3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prg208_android_programming_exam.ImageClickListener
import com.example.prg208_android_programming_exam.ImageInfo
import com.example.prg208_android_programming_exam.databinding.FragmentThreeCardCellBinding
import com.example.prg208_android_programming_exam.ifImageStartWithHttp

class FragmentThreeCardViewAdapterEdit(
    //takes inn this parameters
    private val imageArrayList: ArrayList<ImageInfo>,
    private val imageUri: String,
    private val clickListenerDelete: ImageClickListener
    ): RecyclerView.Adapter<FragmentThreeCardViewHolderEdit>() {

    //inflates the card view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): FragmentThreeCardViewHolderEdit {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentThreeCardCellBinding.inflate(from, parent, false)
        return FragmentThreeCardViewHolderEdit(binding)
    }

    //Gets the object from that position/ the index of getItemCount
    override fun onBindViewHolder(holder: FragmentThreeCardViewHolderEdit, position: Int) {
        //Gets a filtered arraylist with only the server images.
        var filteredArray: ArrayList<ImageInfo> = ifImageStartWithHttp(imageArrayList, imageUri)
        holder.bindImage(filteredArray[position], clickListenerDelete)
    }

    //Loops through the Array, and the size decides how many items there is in the recycler view.
    override fun getItemCount(): Int {
        var filteredArray: ArrayList<ImageInfo> = ifImageStartWithHttp(imageArrayList, imageUri)
        return filteredArray.size
    }
}