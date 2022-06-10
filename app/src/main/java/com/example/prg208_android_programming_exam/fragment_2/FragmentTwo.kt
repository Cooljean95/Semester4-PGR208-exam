package com.example.prg208_android_programming_exam.fragment_2

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
import com.example.prg208_android_programming_exam.databinding.FragmentTwoBinding


class FragmentTwo() : Fragment(R.layout.fragment_two), ImageClickListener {

    //This are all the local variable that reuses trough out the code.
    private val fragmentTwo = this
    // This is a view binding, allows you to more easily write code that interacts with views
    private lateinit var _binding: FragmentTwoBinding
    private val binding get() = _binding!!

    //Android lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(Globals.TAG, "Activity Fragment Two onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i(Globals.TAG, "Activity Fragment Two onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(Globals.TAG, "Activity Fragment Two onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(Globals.TAG, "Activity Fragment Two onDestroy")
    }

    //Inflates fragment 2, and deploys the adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTwoBinding.inflate(inflater, container, false)

            binding.recyclerViewTwo.apply {
                layoutManager = GridLayoutManager(activity!!.applicationContext, 2)
                adapter = FragmentTwoCardAdapter(imageInfoFromApiArray, fragmentTwo)
            }
        return binding.root
    }

    // Activates when image is click on and sends a intent
    override fun onClick(imageInfo: ImageInfo) {
        val intent = Intent(activity?.applicationContext, FragmentTwoEditActivity::class.java)
        intent.putExtra(IMAGE_ID_EXTRA, imageInfo)
        startActivity(intent)
    }

    //This onclick method is implemented form ann interface, but not in use.
    override fun onClickDelete(imageInfo: ImageInfo) {
        TODO("Not in use")
    }

}