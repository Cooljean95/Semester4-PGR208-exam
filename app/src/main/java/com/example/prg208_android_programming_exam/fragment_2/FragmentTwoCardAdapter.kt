package com.example.prg208_android_programming_exam.fragment_2
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prg208_android_programming_exam.*
import com.example.prg208_android_programming_exam.databinding.FragmentTwoCardCellBinding

class FragmentTwoCardAdapter(
    //takes inn this parameters
    private val imageArrayApiList: ArrayList<ImageInfo>,
    private val clickListener: ImageClickListener,
    ):RecyclerView.Adapter<FragmentTwoCardViewHolder>(){

    //inflates the card view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentTwoCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentTwoCardCellBinding.inflate(from, parent, false)
        return FragmentTwoCardViewHolder(binding, clickListener)
    }

    //Gets the object from that position/ the index of getItemCount
    override fun onBindViewHolder(holder: FragmentTwoCardViewHolder, position: Int) {
        holder.bindImage(imageArrayApiList[position])
    }

    //Loops through the Array, and the size decides how many items there is in the recycler view.
    override fun getItemCount(): Int {
        return imageArrayApiList.size
    }

}

