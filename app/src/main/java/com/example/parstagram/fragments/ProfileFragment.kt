package com.example.parstagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parstagram.User
import com.example.parstagram.*
import com.example.parstagram.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


class ProfileFragment : FeedFragment() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var profilePreview : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = User()
        val parseUser = ParseUser.getCurrentUser()
        profilePreview = view.findViewById(R.id.profilePreview)
        Glide.with(requireContext()).load(user.getProfilePic()?.url).into(profilePreview)


        view.findViewById<Button>(R.id.takePhoto).setOnClickListener {
            onLaunchCamera()
        }
        view.findViewById<Button>(R.id.updatePhoto).setOnClickListener {
            val user = User()
            val post = Post()
            val parseUser = ParseUser.getCurrentUser()

            if (photoFile != null){
                user.setProfilePicture(ParseFile(photoFile))
                user.setUser(parseUser)
                user.saveInBackground{ exception ->
                    if (exception != null){
                        Log.e(MainActivity.TAG, "Error while saving post")
                        exception.printStackTrace()
                        Toast.makeText(requireContext(), "Could not upload post", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.i(MainActivity.TAG, "Successfully saved profile")
                        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please take a photo", Toast.LENGTH_SHORT).show()
            }
        }


        super.onViewCreated(view, savedInstanceState)

        swipeContainer = view.findViewById(R.id.swipeContainer)

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener {

            queryPosts()

        }



        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

            android.R.color.holo_green_light,

            android.R.color.holo_orange_light,

            android.R.color.holo_red_light);
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "In the feed.")

        view.findViewById<Button>(R.id.logOut).setOnClickListener {
            logOutUser()
        }

        postsRecyclerView = view.findViewById<RecyclerView>(R.id.postsRecycler)

        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter

        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        queryPosts()
    }

    override fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.include(Post.KEY_USER)
        //current user post
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        query.addDescendingOrder("createdAt")
        query.findInBackground(object: FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null){
                    Log.e(TAG, "Error Fetching Post")
                } else {
                    if (posts != null){
                        allPosts.addAll(posts)

                        adapter.notifyDataSetChanged()
                    }
                    swipeContainer.setRefreshing(false)
                }
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if (resultCode == AppCompatActivity.RESULT_OK){

                val takenImage = BitmapFactory.decodeFile((photoFile!!.absolutePath))

                profilePreview.setImageBitmap(takenImage)
            } else {
                Toast.makeText(requireContext(), "Could not upload post", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
}