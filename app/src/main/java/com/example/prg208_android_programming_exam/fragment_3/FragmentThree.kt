package com.example.prg208_android_programming_exam.fragment_3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.prg208_android_programming_exam.*
import com.example.prg208_android_programming_exam.databinding.FragmentThreeBinding

class FragmentThree() : Fragment(R.layout.fragment_three), ImageClickListener {

    //This are all the local variable that reuses trough out the code.
    var itemAdapter: FragmentThreeCardAdapter? = null
    private val fragmentThree = this
    private lateinit var _binding: FragmentThreeBinding
    private val binding get() = _binding!!
    private lateinit var dbHelper: FeedReaderDbHelper

    //Android lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         Log.i(Globals.TAG, "Activity Fragment Three onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i(Globals.TAG, "Activity Fragment Three onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(Globals.TAG, "Activity Fragment Three onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(Globals.TAG, "Activity Fragment Three onDestroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThreeBinding.inflate(inflater, container, false)
        dbHelper = FeedReaderDbHelper(requireContext())

        //apply to adapter, with the needed parameters
            binding.recyclerViewThree.apply {
                layoutManager = GridLayoutManager(activity!!.applicationContext, 3)
                itemAdapter = FragmentThreeCardAdapter(imageArrayList, fragmentThree)
                adapter = itemAdapter
            }

        return binding.root
    }

    //onclick function that send a intent with info, to open a new recyclerview.
    override fun onClick(imageInfo: ImageInfo) {
        val intent = Intent(activity?.applicationContext, FragmentThreeEditActivity::class.java)
        intent.putExtra(IMAGE_ID_EXTRA, imageInfo)
        startActivity(intent)
    }

    // On click delete deletes the original picture with its corresponding server images.
    override fun onClickDelete(imageInfo: ImageInfo) {
        imageArrayList.remove(imageInfo)
        itemAdapter?.notifyDataSetChanged()

        //Deletes it from the database.
        dbHelper.writableDatabase.delete("Images", "imageUri =?", arrayOf(imageInfo.imageUri))
    }



}