package com.rohitthebest.quizzed_aquizapp.util

import android.content.res.ColorStateList
import android.view.View
import android.widget.ProgressBar

class ExtensionFunctions {

    companion object {

        fun View.show() {

            try {
                this.visibility = View.VISIBLE

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun View.hide() {

            try {
                this.visibility = View.GONE

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun View.enable() {

            try {

                this.isEnabled = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun View.disable() {

            try {

                this.isEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun ProgressBar.changeColor(color: Int) {

            this.progressTintList = ColorStateList.valueOf(
                context?.getColor(
                    color
                )!!
            )

        }
    }
}