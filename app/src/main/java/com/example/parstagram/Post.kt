package com.example.parstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post : ParseObject() {
    fun getDescription(): String? {
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(description: String) {
        return put(KEY_DESCRIPTION, description)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }
    fun setUser(user: ParseUser) {
        return put(KEY_USER, user)
    }

    fun setImage(parsefile: ParseFile) {
        return put(KEY_IMAGE, parsefile)
    }
    fun getImage(): ParseFile? {
        return getParseFile(KEY_IMAGE)
    }
    fun getProfilePic(): ParseFile? {
        return getParseFile(KEY_PROFILE_PICTURE)
    }
    fun setProfilePicture(parsefile: ParseFile) {
        return put(KEY_PROFILE_PICTURE, parsefile)
    }

    companion object{
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_PROFILE_PICTURE = "profilepicture"
        const val KEY_USER = "user"
    }
}