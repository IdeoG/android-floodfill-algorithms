package ru.ideog.apps.floodfillvisualization

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


//class MainActivity : AppCompatActivity() {
//    private val TAG = "MainActivity"
//    private lateinit var filler: QueueLinearFloodFill
//
//    private lateinit var bitmapFloodFiller: Observable<MotionEvent>
//    private lateinit var bitmapGenerator: Observable<Point>
//    private lateinit var dialogView: View
//    private lateinit var dialog: AlertDialog
//    private lateinit var progress: ProgressDialog
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        progress = ProgressDialog(this)
//
//
//        bitmapFloodFiller = createBitmapMotionEventObservable()
//        bitmapGenerator = createBitmapGenerateObservable()
//        generate_noise_button.setOnClickListener { openSizeDialog() }
//
//        bitmapGenerator
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext { showProgress() }
//                .observeOn(Schedulers.computation())
//                .map { generateRandomBitmap(it.x, it.y) }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    hideProgress()
//                    filler = QueueLinearFloodFill(it)
//                    showResult(it)
//                }
//
//        bitmapFloodFiller
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter { it.action == MotionEvent.ACTION_DOWN }
//                .observeOn(Schedulers.computation())
//                .map { executeFloodFilling(it) }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { showResult(it) }
//    }
//
//    private fun createSizeClickObservable(): Observable<Void> = Observable.create { emitter ->
//        val dialogView = layoutInflater.inflate(R.layout.dialog_size, null)
//
//        val dialogBuilder = AlertDialog.Builder(this)
//        val dialog = dialogBuilder.create()
//        dialog.setView(dialogView)
//
//        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
//        val okButton = dialogView.findViewById<Button>(R.id.ok_button)
//
//        val width = dialogView.findViewById<EditText>(R.id.edit_width)
//        val height = dialogView.findViewById<EditText>(R.id.edit_height)
//
//        okButton.setOnClickListener {
//
//            val x = width.text.toString().toInt()
//            val y = height.text.toString().toInt()
//
//            dialog.dismiss()
//        }
//
//        cancelButton.setOnClickListener { dialog.dismiss() }
//    }
//
//    private fun createBitmapGeneratorClickObservable(): Observable<Point> = Observable.create { emitter ->
//
//    }
//
//    private fun executeFloodFilling( event: MotionEvent): Bitmap {
//        val viewCoords = IntArray(2)
//        drawing_surface.getLocationOnScreen(viewCoords)
//
//        val absX = event.rawX
//        val absY = event.rawY
//
//        val imgX = absX - viewCoords[0]
//        val imgY = absY - viewCoords[1]
//
//        val maxImgX = drawing_surface.width
//        val maxImgY = drawing_surface.height
//
//        val bitmap = (drawing_surface.drawable as BitmapDrawable).bitmap
//
//        val maxX = bitmap.width
//        val maxY = bitmap.height
//
//        val x = (maxX * imgX / maxImgX).toInt()
//        val y = (maxY * imgY / maxImgY).toInt()
//
//        val color = bitmap.getPixel(x, y)
//        val isBlack = color == Color.BLACK
//
//        val replacementColor = if (isBlack) Color.WHITE else Color.BLACK
//
//        filler.floodFill(x, y, color, replacementColor)
//
//        return bitmap
//    }
//
//    private fun generateRandomBitmap(width: Int, height: Int): Bitmap {
//        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//
//        for (i in 0 until width) {
//            for (j in 0 until height) {
//                val color = when ((0..1).random()) {
//                    0 -> Color.BLACK
//                    1 -> Color.WHITE
//                    else -> Color.BLACK
//                }
//                bmp.setPixel(i, j, color)
//            }
//        }
//        return bmp
//    }
//
//    private fun createBitmapMotionEventObservable(): Observable<MotionEvent> = Observable.create { emitter ->
//                drawing_surface.setOnTouchListener { _, event ->
//                    emitter.onNext(event)
//                    true
//                }
//
//                emitter.setCancellable { drawing_surface.setOnTouchListener(null) }
//            }
//
//    private fun openSizeDialog(): Point {
//        val sizes = Point(64, 64)
//        // TODO: open Dialog and pick x,y values
//        dialog.show()
//
//        return sizes
//    }
//
//    private fun showProgress() {
//        // TODO: Show progress bar
//        progress.show()
//    }
//
//    private fun hideProgress() {
//        // TODO: Hide progress bar
//        progress.hide()
//    }
//
//    private fun showResult(bitmap: Bitmap) {
//        drawing_surface.setImageBitmap(bitmap)
//    }
//
//    private fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start
//}


