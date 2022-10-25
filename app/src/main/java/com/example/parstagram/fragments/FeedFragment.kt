package com.example.parstagram.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.*
import com.example.parstagram.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


open class FeedFragment : Fragment() {

    val TAG = "FeedFragment"
    lateinit var postsRecyclerView: RecyclerView

    lateinit var adapter: PostAdapter

    lateinit var swipeContainer: SwipeRefreshLayout

    var allPosts: ArrayList<Post> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    open fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.include(Post.KEY_USER)
        query.addDescendingOrder("createdAt")
        query.setLimit(20)
        query.findInBackground(object: FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null){
                    Log.e(TAG, "Error Fetching Post")
                } else {
                    adapter.clear()
                    if (posts != null){
                            allPosts.addAll(posts)
                            adapter.notifyDataSetChanged()
                    }
                    swipeContainer.setRefreshing(false)
                }
            }
        })
    }


    fun logOutUser(){
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}
