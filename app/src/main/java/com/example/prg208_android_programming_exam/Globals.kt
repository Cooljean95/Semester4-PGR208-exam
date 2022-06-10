package com.example.prg208_android_programming_exam

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL

//used as a default tag for android lifeCycle logs.
object Globals {
    const val TAG = "AndroidLifeCycle"
}

// Arraylist that's assessable in all classes.
var imageInfoFromApiArray = ArrayList<ImageInfo>()
var imageArrayList = ArrayList<ImageInfo>()

// uses to send intent
val IMAGE_ID_EXTRA = "imageExtra"

// the object that includes the information for a image, and its serializable
data class ImageInfo(var imageId: Int?,  var imageUri: String?, var imageLink: String?, var image: ByteArray): Serializable{
}

//Retrives the images saved in the database witch is used for fragment three
fun writeFromDatabase(dbHelper: FeedReaderDbHelper){

    imageArrayList.clear()

    val cursor = dbHelper.writableDatabase?.query(
        "Images",
        arrayOf("imageId", "imageUri", "imageLink", "image"),
        null,
        null,
        null,
        null,
        null
    )

    while (cursor!!.moveToNext()) {
        val imageID = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"))
        val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("imageUri"))
        val imageLink = cursor.getString(cursor.getColumnIndexOrThrow("imageLink"))
        val imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("image"))

        imageArrayList.add(
            ImageInfo(
                imageID,
                imageUri,
                imageLink,
                imageBlob
            )
        )
    }
    cursor.close()
}

// Filters the array list from server images, and connects it to its original picture from the emulator.
fun ifImageStartWithHttp(imageArrayList: ArrayList<ImageInfo>, imageUri: String?): ArrayList<ImageInfo> {
    val filteredArray = ArrayList<ImageInfo>()

    for ( index in 0 until imageArrayList.size ) {

        if (imageArrayList[index].imageLink!!.startsWith("http", false).and(imageUri == imageArrayList[index].imageUri)) {

            val image = imageArrayList[index].image
            val imageId = imageArrayList[index].imageId
            val imageUri = imageArrayList[index].imageUri
            val imageLink = imageArrayList[index].imageLink
            filteredArray.add(ImageInfo(
                imageId,
                imageUri,
                imageLink,
                image
            ))
        }
    }
    return filteredArray
}

//Filters out the original pictures from the emulators into a arraylist
fun ifImageStartWithFile(imageArrayList: ArrayList<ImageInfo>): ArrayList<ImageInfo> {
    val filteredArray = ArrayList<ImageInfo>()

    for ( index in 0 until imageArrayList.size ) {

        if (imageArrayList[index].imageLink!!.startsWith("file", false)) {

            val image = imageArrayList[index].image
            val imageId = imageArrayList[index].imageId
            val imageUri = imageArrayList[index].imageUri
            val imageLink = imageArrayList[index].imageLink
                filteredArray.add(ImageInfo(
                    imageId,
                    imageUri,
                    imageLink,
                    image
                ))
        }
    }
    return filteredArray
}

// Takes inn a byte array and converts it to a drawable.
fun byteArrayToDrawable(byte: ByteArray, resources: Resources): Drawable {
    val bm: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
    return BitmapDrawable(resources, bm)
}

// Uses uri to get a bitmap.
fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
    val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
    return image
}

fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
    return decoder(context, id, uri)
}

//Takes inn a bitmap, compresses it, and returns a outputstream.
fun bitmapToByteArray(image: Bitmap): ByteArrayOutputStream {
    val outputStream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream
}

//Suspend function that downloads the image with http connection, and turns the inputstream to a bitmap.
suspend fun downloadImageUsingCoroutines(httpImageLink: String): Bitmap? {
    var bm: Bitmap? = null

    withContext(Dispatchers.IO) {

        with(URL(httpImageLink).openConnection() as HttpURLConnection) {
            requestMethod = "GET"

            setRequestProperty(
                "User-Agent",
                "Mozilla/5.0"
            )
            bm = BitmapFactory.decodeStream(inputStream)
        }
    }
    return bm
}