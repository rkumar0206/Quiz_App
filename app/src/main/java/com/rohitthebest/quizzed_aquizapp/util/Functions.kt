package com.rohitthebest.quizzed_aquizapp.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.others.Constants.NO_INTERNET_MESSAGE
import com.rohitthebest.quizzed_aquizapp.ui.fragments.Category

class Functions {

    companion object {

        private const val TAG = "Functions"

        fun showToast(
            context: Context,
            message: String,
            duration: Int = Toast.LENGTH_SHORT
        ) {
            try {
                Log.d(TAG, message)
                Toast.makeText(context, message, duration).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun showNoInternetMessage(context: Context) {

            showToast(context, NO_INTERNET_MESSAGE)
        }

        fun shareAsText(message: String?, subject: String?, context: Context) {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(Intent.createChooser(intent, "Share Via"))

        }

        fun copyToClipBoard(activity: Activity, text: String) {

            val clipboardManager =
                activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clipData = ClipData.newPlainText("url", text)

            clipboardManager.setPrimaryClip(clipData)

            showToast(activity, "Copied")

        }

        fun populateCategoryList(): ArrayList<Category> {

            val categoryList = ArrayList<Category>()

            categoryList.add(Category(R.drawable.general_knowledge, "General Knowledge", 9))
            categoryList.add(Category(R.drawable.animals, "Animals", 27))
            categoryList.add(Category(R.drawable.art, "Art", 25))
            categoryList.add(Category(R.drawable.books, "Books", 10))
            categoryList.add(Category(R.drawable.computer, "Computer", 18))
            categoryList.add(Category(R.drawable.film, "Film", 11))
            categoryList.add(Category(R.drawable.politics, "Politics", 24))
            categoryList.add(Category(R.drawable.gadget2, "Gadget", 30))
            categoryList.add(Category(R.drawable.geography, "Geography", 22))
            categoryList.add(Category(R.drawable.mathematics, "Mathematics", 19))
            categoryList.add(Category(R.drawable.music, "Music", 12))
            categoryList.add(Category(R.drawable.mythology, "Mythology", 20))
            categoryList.add(Category(R.drawable.nature, "Nature", 17))
            categoryList.add(Category(R.drawable.sports, "Sports", 21))
            categoryList.add(Category(R.drawable.vehicles, "Vehicles", 28))
            categoryList.add(Category(R.drawable.video_games, "Video-games", 15))

            return categoryList
        }


    }
}