package com.example.prg208_android_programming_exam.fragment_2

import android.content.ContentValues
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prg208_android_programming_exam.*

import com.example.prg208_android_programming_exam.databinding.FragmentTwoActivityDetailBinding


class FragmentTwoEditActivity : AppCompatActivity() {
    //This are all the local variable that reuses trough out the code.
    private var dbHelper = FeedReaderDbHelper(this)
    private lateinit var binding:FragmentTwoActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTwoActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //receives the intent, send by a function, activated with a on click listener
        val selectedImage = (intent.getSerializableExtra(IMAGE_ID_EXTRA) as ImageInfo)
        val imageId = selectedImage.imageId
        val imageUri = selectedImage.imageUri
        val imageLink = selectedImage.imageLink
        val images = selectedImage.image

        //Start a function. Saves the image in to the database.
        writeToDatabase(imageId!!, imageUri!!, imageLink!!, images)

        val image = imageFromID(imageId!!)
        if ( image != null ) {
            val drawable: Drawable = byteArrayToDrawable(image.image, resources )
            binding.FullCardView.setImageDrawable(drawable)
        }
    }

    //Checks if the id matches and returns the image that match with the id.
    private fun imageFromID(imageId: Int): ImageInfo? {

        for(image in imageInfoFromApiArray) {
            if (image.imageId == imageId) {
                return image
            }
        }
        return null
    }

    //Writes the images inn to the database.
    private fun writeToDatabase(imageId: Int, imageUri: String, imageLink: String, image: ByteArray){

        dbHelper?.writableDatabase?.insert("Images", null, ContentValues().apply {

            put("imageUri", imageUri.toString())
            put("imageLink", imageLink )
            put("image", image)
        })

        //Restart write from database so fragment 3 is updated.
        writeFromDatabase(dbHelper)
        dbHelper.close()
        Toast.makeText(this, "Saved Image To Results", Toast.LENGTH_SHORT).show()
    }
}