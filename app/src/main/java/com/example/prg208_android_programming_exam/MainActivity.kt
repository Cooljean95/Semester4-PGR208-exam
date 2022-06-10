package com.example.prg208_android_programming_exam
import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL
import android.widget.Toast
import com.example.prg208_android_programming_exam.fragment_1.FragmentOne
import com.example.prg208_android_programming_exam.fragment_2.FragmentTwo
import com.example.prg208_android_programming_exam.fragment_3.FragmentThree
import java.net.URLConnection

class MainActivity : AppCompatActivity() {

    //This are all the local variable that reuses trough out the code.
    private var fragmentManager = supportFragmentManager
    private var dbHelper = FeedReaderDbHelper(this)
    private var imageUri: String? = null
    private var imageURL: String? = null
    private lateinit var json: JSONArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Activating onCreate with fragment 1, so the first page user see is fragment one.
        fragmentManager.beginTransaction().replace(R.id.main_fragment, FragmentOne(), "Fragment1").commit()

        //Sets up fragment 3 before, and deploys already existing images in database.
        writeFromDatabase(dbHelper)
        Log.i(Globals.TAG, "Activity Main onCreate")
        Toast.makeText(this, "Main Activity onCreate", Toast.LENGTH_SHORT).show()
    }

    //Changes the fragments between 1, 2 and 3
    fun changeFragment(view: View) {
        Log.i(Globals.TAG, "Activity Main switchFragment. Tag: " + view.tag.toString())
        Toast.makeText(
            this,
            "Main Activity switchFragment. Fragment" + view.getTag().toString(),
            Toast.LENGTH_SHORT
        ).show()

        if (Integer.parseInt(view.tag.toString()) == 1) {
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_fragment,
                    FragmentOne(),
                    "Fragment1"
                )
                .addToBackStack("home")
                .commit()
        } else if (Integer.parseInt(view.tag.toString()) == 2) {
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_fragment,
                    FragmentTwo(),
                    "Fragment2"
                )
                .addToBackStack("home")
                .commit()
        } else {
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_fragment,
                    FragmentThree(),
                    "Fragment3"
                )
                .addToBackStack("home")
                .commit()
        }
    }

    //Android lifecycle
    override fun onStart() {
        super.onStart()
        Log.i(Globals.TAG, "Activity Main onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(Globals.TAG, "Activity Main onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(Globals.TAG, "Activity Main onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(Globals.TAG, "Activity Main onDestroy")
    }

    //Start the http connection when upload button is pressed.
    fun submit(view: View) {

        GlobalScope.launch(Dispatchers.Default) {
            delay(1000)

            //Error handling if the imageUri or imageUrl are empty, and if the Api doesn't work.
            try{
                //Gets the imageUri and Url form fragment one
                imageUri = (fragmentManager.findFragmentByTag("Fragment1") as FragmentOne).imageUri.toString()
                imageURL = (fragmentManager.findFragmentByTag("Fragment1") as FragmentOne).imageUrl

                try {
                    httpConnection("bing")
                    imageServerMessage(view)
                }catch (e: JSONException) {
                    Log.i("Bing Error:", e.toString())
                    try {
                        httpConnection("tineye")
                        imageServerMessage(view)
                    }catch (e: JSONException) {
                        Log.i("Tineye Error:", e.toString())
                        try{
                            httpConnection("google")
                            imageServerMessage(view)
                        } catch (e: JSONException) {
                            Log.i("Google Error:", e.toString())
                        }
                        }
                }
            }catch (e: RuntimeException){
                Handler(Looper.getMainLooper()).post{
                    Toast.makeText(view.context, "Select a picture, or try again", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    // Makes a toast when the images are in fragment 2.
    private fun imageServerMessage(view: View){
        Handler(Looper.getMainLooper()).post{
            Toast.makeText(view.context, "The pictures are ready in Fragment 2", Toast.LENGTH_SHORT).show()
        }
    }

    //Connects to the Api with a get server, and receives a response.
    private suspend fun httpConnection(api: String ) {

        val result = URL("http://api-edu.gtl.ai/api/v1/imagesearch/$api?url=$imageURL")

        imageInfoFromApiArray.clear()

        //saves the image from emulator to the database.
        dbHelper?.writableDatabase?.insert("Images",null, ContentValues().apply {

            put("imageUri", imageUri)
            put("imageLink", imageUri)
            put("image", bitmapToByteArray(getBitmap(applicationContext, null, imageUri, ::UriToBitmap)).toByteArray())
        })
        dbHelper.close()

        withContext(Dispatchers.IO){
            with(result.openConnection() as HttpURLConnection){

                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")

                val stream = inputStream
                val text = stream.bufferedReader().use { it.readText() }
                json = JSONArray(text)
            }
            //loops through all the responses and adds to a arraylist
            for (index in 0 until json.length()) {
                val httpImageUrl: String = (json.get(index) as JSONObject).get("image_link").toString()

                //Callback functions. Functions called within a function, and retrieves information to the original function.
                val bitmapImage = downloadImageUsingCoroutines(httpImageUrl)!!
                val byte = bitmapToByteArray(bitmapImage!!).toByteArray()

                val newImage = ImageInfo(
                    index,
                    imageUri, httpImageUrl, byte)
                    imageInfoFromApiArray.add(newImage)
            }
        }
        writeFromDatabase(dbHelper)
    }
}