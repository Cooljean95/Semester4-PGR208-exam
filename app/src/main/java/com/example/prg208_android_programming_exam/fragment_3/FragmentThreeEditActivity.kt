package com.example.prg208_android_programming_exam.fragment_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.prg208_android_programming_exam.*
import com.example.prg208_android_programming_exam.databinding.FragmentThreeActivetyDetailBinding

class FragmentThreeEditActivity: AppCompatActivity(), ImageClickListener {
    //This are all the local variable that reuses trough out the code.
    private lateinit var binding: FragmentThreeActivetyDetailBinding
    var itemAdapter: FragmentThreeCardViewAdapterEdit? = null
    val FragmentThreeDetailActivity = this
    private var dbHelper = FeedReaderDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentThreeActivetyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //receives the intent, send by a function, activated with a on click listener
        val imageInfo = (intent.getSerializableExtra(IMAGE_ID_EXTRA) as ImageInfo)
        val imageId = imageInfo.imageId
        val imageUri = imageInfo.imageLink
        val image = imageFromID(imageId!!)

        //deploys a new adapter to the recyclerview within a recyclerview
        if ( image != null){
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 2)
                itemAdapter = FragmentThreeCardViewAdapterEdit(imageArrayList, imageUri!!, FragmentThreeDetailActivity)
                adapter = itemAdapter
            }
        }
    }

    //Checks if the id matches and returns the image that match with the id.
    private fun imageFromID(imageId: Int): ImageInfo? {

        for (image in imageArrayList){

            if (image.imageId == imageId){
                return image
            }
        }
        return null
    }

    //implements from a interface, and its not in use.
    override fun onClick(imageInfo: ImageInfo) {
        TODO("Not in use")
    }

    //Deletes the server image that in a recycler view, that's within a recyclerview
    override fun onClickDelete(imageInfo: ImageInfo) {
        imageArrayList.remove(imageInfo)
        itemAdapter?.notifyDataSetChanged()

        //Deletes from the database
        dbHelper.writableDatabase.delete("Images", "imageLink =?",
            arrayOf(imageInfo.imageLink))
    }
}