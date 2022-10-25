package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.FeedFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


class MainActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null


    val fragmentManager: FragmentManager = supportFragmentManager

    // define your fragments here

    override fun onCreate(savedInstanceState: Bundle?) {
        super. onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        findViewById<Button>(R.id.logOut).setOnClickListener {
            logOutUser()
        }
        bottomNav.setOnItemSelectedListener {
            item ->
            var fragmentToShow: Fragment? = null

            when (item.itemId){
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
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow)
                    .commit()
            }
            true
        }

        bottomNav.selectedItemId = R.id.action_home
    }



    private fun logOutUser(){
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }






    companion object{
        const val TAG = "MainActivity"
    }
}


