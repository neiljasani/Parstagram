package com.example.parstagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.FeedFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


/**
 * Let user create a post by taking a photo with their camera
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null

            when (item.itemId) {
                R.id.action_home -> {
                    fragmentToShow = FeedFragment()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            // Return true to say that we've handled this user interaction on the item
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.action_home

        // queryPosts()
    }

    // Source: https://guides.codepath.com/android/Defining-The-ActionBar#adding-action-items
    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Source: https://guides.codepath.com/android/Defining-The-ActionBar#handling-actionbar-clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.miSignout) {
            ParseUser.logOutInBackground { e ->
                if (e == null) {
                    goToLoginActivity()
                    Log.i(TAG, "Sign out successful")
                    Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT)
                } else {
                    Log.e(TAG, "Sign out failed")
                    Toast.makeText(this, "Sign out failed", Toast.LENGTH_SHORT)
                }
            }
            val currentUser = ParseUser.getCurrentUser() // this will now be null
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

//
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.FileProvider
//import com.parse.*
//import java.io.ByteArrayOutputStream
//import java.io.File
//import java.io.FileOutputStream
//
//
///**
// * Let user create a post by taking a photo with their camera
// */
//class MainActivity : AppCompatActivity() {
//
//    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
//    val photoFileName = "photo"
//    val photoFileExtension = ".jpg"
//    var photoFile: File? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // 1. Setting the description of the post
//        // 2. A button to launch the camera to take a picture
//        // 3. An ImageView to show the picture the user has taken
//        // 4. A button to save and send the post to our Parse server
//
//        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
//            // Send post to server without an image
//            // Get the description the user has inputted
//            val description = findViewById<EditText>(R.id.etDescription).text.toString()
//            val user = ParseUser.getCurrentUser()
//            if (photoFile != null) {
//                submitPost(description, user, photoFile!!)
//            } else {
//                Log.e(TAG, "photoFile is null")
//                Toast.makeText(this, "Please take a picture", Toast.LENGTH_SHORT)
//            }
//        }
//
//        findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
//            // Launch camera to let user take picture
//            onLaunchCamera()
//
//            Log.i(TAG, "photoFile: ${photoFile.toString()}")
//        }
//
//        // queryPosts()
//    }
//
//    // Source: https://guides.codepath.com/android/Defining-The-ActionBar#adding-action-items
//    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    // Source: https://guides.codepath.com/android/Defining-The-ActionBar#handling-actionbar-clicks
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle presses on the action bar items
//        if (item.getItemId() == R.id.miSignout) {
//            ParseUser.logOutInBackground { e ->
//                if (e == null) {
//                    goToLoginActivity()
//                    Log.i(TAG, "Sign out successful")
//                    Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT)
//                } else {
//                    Log.e(TAG, "Sign out failed")
//                    Toast.makeText(this, "Sign out failed", Toast.LENGTH_SHORT)
//                }
//            }
//            val currentUser = ParseUser.getCurrentUser() // this will now be null
//            return true
//        } else {
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun goToLoginActivity() {
//        val intent = Intent(this@MainActivity, LoginActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    fun submitPost(description: String, user: ParseUser, file: File) {
//        // Create the Post object
//        val post = Post()
//        post.setDescription(description)
//        post.setUser(user)
//        post.setImage(ParseFile(file))
//        post.saveInBackground { exception ->
//            if (exception != null) {
//                // Something went wrong
//                Log.e(TAG, "Error while saving post")
//                exception.printStackTrace()
//                Toast.makeText(this, "Error while saving post", Toast.LENGTH_SHORT).show()
//            } else {
//                Log.i(TAG, "Successfully saved post")
//
//                // Clear the description field
//                var etDescription = findViewById<EditText>(R.id.etDescription)
//                etDescription.text.clear()
//
//                // Reset the ImageView
//                // Source: https://stackoverflow.com/a/8243184
//                var ivPicture = findViewById<ImageView>(R.id.ivPicture)
//                ivPicture.setImageResource(android.R.color.transparent)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // by this point we have the camera photo on disk
//                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
//                // Resize the bitmap
//                val resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 480)
//                // Load the taken image into a preview
//                val ivPreview: ImageView = findViewById(R.id.ivPicture)
//                ivPreview.setImageBitmap(resizedBitmap)
//
//                // Configure byte output stream
//                val bytes = ByteArrayOutputStream()
//                // Compress the image further
//                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
//                // Create a new file for the resized bitmap
//                photoFile = getPhotoFileUri(photoFileName + "_resized" + photoFileExtension)
//                photoFile!!.createNewFile()
//                val fos = FileOutputStream(photoFile)
//                // Write the bytes of the bitmap to file
//                fos.write(bytes.toByteArray())
//                fos.close()
//
//            } else { // Result was a failure
//                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    fun onLaunchCamera() {
//        // create Intent to take a picture and return control to the calling application
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        // Create a File reference for future access
//        photoFile = getPhotoFileUri(photoFileName + photoFileExtension)
//
//        // wrap File object into a content provider
//        // required for API >= 24
//        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//        if (photoFile != null) {
//            val fileProvider: Uri =
//                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
//
//            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
//            // So as long as the result is not null, it's safe to use the intent.
//            if (intent.resolveActivity(packageManager) != null) {
//                // Start the image capture intent to take photo
//                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
//            }
//        }
//    }
//
//    // Returns the File for a photo stored on disk given the fileName
//    fun getPhotoFileUri(fileName: String): File {
//        // Get safe storage directory for photos
//        // Use `getExternalFilesDir` on Context to access package-specific directories.
//        // This way, we don't need to request external read/write runtime permissions.
//        val mediaStorageDir =
//            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
//            Log.d(TAG, "failed to create directory")
//        }
//
//        // Return the file target for the photo based on filename
//        return File(mediaStorageDir.path + File.separator + fileName)
//    }
//
//    // Query for all posts in our server
//    fun queryPosts() {
//
//        // Specify which class to query
//        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
//
//        // Find all Post objects
//        query.include(Post.KEY_USER)
//        query.findInBackground(object : FindCallback<Post> {
//            override fun done(posts: MutableList<Post>?, e: ParseException?) {
//                if (e != null) {
//                    // Something went wrong
//                    Log.e(TAG, "Error fetching posts")
//                } else {
//                    if (posts != null) {
//                        for (post in posts) {
//                            Log.i(TAG, "Post: " + post.getDescription() + " , username: " +
//                                    post.getUser()?.username)
//                        }
//                    }
//                }
//            }
//
//        })
//    }
//
//    companion object {
//        const val TAG = "MainActivity"
//    }
//}