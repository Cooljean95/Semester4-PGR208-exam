package com.example.prg208_android_programming_exam.fragment_1
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.prg208_android_programming_exam.*
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import kotlinx.coroutines.*
import java.io.File

class FragmentOne : Fragment() {

    //This are all the local variable that reuses trough out the code.
    lateinit var image: ImageView
    lateinit var imageUri: Uri
    lateinit var imageUrl: String
    lateinit var filePath: String
    //livedata is a observer holder class, that respects the lifecycle
    private val livedata : MutableLiveData<String> = MutableLiveData<String>()

    //Android lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(Globals.TAG, "Activity Fragment One onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i(Globals.TAG, "Activity Fragment One onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(Globals.TAG, "Activity Fragment One onPause")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(Globals.TAG, "Activity Fragment One onDestroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Retrieves the views by finding it with its id.
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        var gallery = view!!.findViewById<Button>(R.id.Gallery)
        var camera = view!!.findViewById<Button>(R.id.Camera)

        val startForReverseImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->

                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    imageUri = data?.data!!
                    filePath = FileUriUtils.getRealPath(view.context, imageUri)!!

                    //Checks if the file is a jpg, and converts it to png.
                    if (filePath.endsWith(".jpg")){
                        val imageFile = File(filePath)
                        val file: File = File(filePath.replace("jpg", "png"))
                        imageFile.renameTo(file)
                        imageUri = Uri.parse(imageUri.toString().replace("jpg", "png"))
                        livedata.postValue(file.toString())
                    }else{
                        livedata.postValue(filePath)
                    }
                    //loads image into imageView with glide
                    Glide.with(view!!.context!!)
                        .load(imageUri) // Uri of the picture
                        .into(image);
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(view!!.context, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(view!!.context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        gallery.setOnClickListener {
            //Uses lambda to find and crop pictures with imagePicker and intent
            ImagePicker.with(this).galleryMimeTypes(arrayOf("image/*")).crop().compress(100).galleryOnly()
                //User can only select image from Gallery
                .createIntent { intent ->
                    startForReverseImageResult.launch(intent)
                }
        }

        camera.setOnClickListener {
            //Uses lambda to take a picture using camera with imagePicker and intent
            ImagePicker.with(this).crop().compress(100)
                .cameraOnly()   //User can only select image from Gallery
                .createIntent { intent ->
                    startForReverseImageResult.launch(intent)
                }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {

                image = view!!.findViewById<ImageView>(R.id.image)

                //Finds file by observing for livedata.
                livedata.observe(viewLifecycleOwner){file ->
                    val url = "http://api-edu.gtl.ai/api/v1/imagesearch/upload"

                    // Send a post request by using fast android networking
                    AndroidNetworking.upload(url)
                        .addMultipartFile("image", File(file))
                        .addMultipartParameter("text", "image/png")
                        .setTag("uploadTest")
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener { bytesUploaded, totalBytes ->
                            println("bytesUploaded: $bytesUploaded")
                        }
                        .getAsString(object : StringRequestListener {
                            override fun onResponse(response: String) {
                                //catches respons from upload api
                                imageUrl = response
                                println("From response: ${response}")
                            }

                            override fun onError(error: ANError) {
                                //Catches error
                                println("From error: ${error.errorBody}")
                            }

                        })
                }
            }
        }
    }
}


