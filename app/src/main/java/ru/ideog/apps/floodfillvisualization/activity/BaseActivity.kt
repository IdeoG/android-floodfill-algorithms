package ru.ideog.apps.floodfillvisualization.activity

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import ru.ideog.apps.floodfillvisualization.view.MainView


open class BaseActivity : AppCompatActivity(), MainView {

    override fun showProgress() {
        loading_view.visibility = View.VISIBLE

        loading_view.playAnimation()
        loading_view.loop(true)
    }

    override fun hideProgress() {
        loading_view.cancelAnimation()
        loading_view.visibility = View.GONE
    }

    override fun showResult(bitmap: Bitmap, view: ImageView) {
        val emptyBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        if (bitmap.sameAs(emptyBitmap)) return

        view.setImageBitmap(bitmap)
    }

    override fun showResults(bitmaps: Array<Bitmap>) {
        first_algorithm_image.setImageBitmap(bitmaps[0])
        second_algorithm_image.setImageBitmap(bitmaps[1])
    }

    companion object {
        private const val TAG = "BaseActivity"
    }

}