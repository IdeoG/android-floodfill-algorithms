package ru.ideog.apps.floodfillvisualization.activity

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import ru.ideog.apps.floodfillvisualization.view.MainView

open class BaseActivity : AppCompatActivity(), MainView {

    override fun showProgress() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showResult(bitmap: Bitmap, view: ImageView) {
        view.setImageBitmap(bitmap)
    }

    override fun showResults(bitmaps: Array<Bitmap>) {
        first_algorithm_image.setImageBitmap(bitmaps[0])
        second_algorithm_image.setImageBitmap(bitmaps[1])
    }

    override fun showSizeDialog() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideSizeDialog() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}