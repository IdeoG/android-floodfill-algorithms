package ru.ideog.apps.floodfillvisualization.view

import android.graphics.Bitmap
import android.widget.ImageView

interface MainView {
    fun showProgress()

    fun hideProgress()

    fun showResult(bitmap: Bitmap, view: ImageView)

    fun showResults(bitmaps: Array<Bitmap>)

    fun showSizeDialog()

    fun hideSizeDialog()

}