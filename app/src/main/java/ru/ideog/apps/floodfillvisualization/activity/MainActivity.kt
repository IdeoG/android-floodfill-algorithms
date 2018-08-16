package ru.ideog.apps.floodfillvisualization.activity

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ru.ideog.apps.floodfillvisualization.R
import ru.ideog.apps.floodfillvisualization.presenter.FloodFillPresenter


class MainActivity : BaseActivity() {
    private val presenter = FloodFillPresenter()
    private val sizes = Point(64, 64)

    private var firstFloodFillMethod = 0
    private var secondFloodFillMethod = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstBitmapFloodFiller = createFirstBitmapMotionEventObservable()
        val secondBitmapFloodFiller = createSecondBitmapMotionEventObservable()

        val bitmapGenerator = createBitmapGeneratorClickObservable()
        val bitmapSizePicker = createBitmapSizeClickObservable()

        bitmapGenerator
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { presenter.generateRandomBitmaps(it.x, it.y) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    hideProgress()
                    showResults(it)
                }

        firstBitmapFloodFiller
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.action == MotionEvent.ACTION_DOWN }
                .observeOn(Schedulers.computation())
                .map { presenter.executeFloodFilling(firstFloodFillMethod, first_algorithm_image,it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { showResult(it, first_algorithm_image) }

        secondBitmapFloodFiller
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.action == MotionEvent.ACTION_DOWN }
                .observeOn(Schedulers.computation())
                .map { presenter.executeFloodFilling(secondFloodFillMethod, second_algorithm_image,it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { showResult(it, second_algorithm_image) }

        bitmapSizePicker
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.algorithms_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        first_algorithm_spinner.adapter = adapter
        second_algorithm_spinner.adapter = adapter

        first_algorithm_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { firstFloodFillMethod = p2 }
        }
        second_algorithm_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { secondFloodFillMethod = p2 }
        }
    }

    private fun createFirstBitmapMotionEventObservable(): Observable<MotionEvent> = Observable.create { emitter ->
        first_algorithm_image.setOnTouchListener { view, event ->
            if ((view as ImageView).drawable != null) emitter.onNext(event)
            true
        }

        emitter.setCancellable { first_algorithm_image.setOnTouchListener(null) }
    }

    private fun createSecondBitmapMotionEventObservable(): Observable<MotionEvent> = Observable.create { emitter ->
        second_algorithm_image.setOnTouchListener { view, event ->
            if ((view as ImageView).drawable != null) emitter.onNext(event)
            true
        }

        emitter.setCancellable { second_algorithm_image.setOnTouchListener(null) }
    }

    private fun createBitmapGeneratorClickObservable(): Observable<Point> = Observable.create { emitter ->
        generate_noise_button.setOnClickListener {
            first_algorithm_image.setImageDrawable(null)
            second_algorithm_image.setImageDrawable(null)

            emitter.onNext(sizes)
        }

        emitter.setCancellable { generate_noise_button.setOnClickListener(null) }
    }

    private fun createBitmapSizeClickObservable(): Completable = Completable.create { emitter ->
        val dialogView = layoutInflater.inflate(R.layout.dialog_size, null)

        val dialogBuilder = AlertDialog.Builder(this)
        val dialog = dialogBuilder.create()
        dialog.setView(dialogView)

        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
        val okButton = dialogView.findViewById<Button>(R.id.ok_button)

        val width = dialogView.findViewById<EditText>(R.id.edit_width)
        val height = dialogView.findViewById<EditText>(R.id.edit_height)

        size_button.setOnClickListener { dialog.show() }

        okButton.setOnClickListener {

            sizes.x = width.text.toString().toInt()
            sizes.y = height.text.toString().toInt()

            dialog.dismiss()
            emitter.onComplete()
        }

        cancelButton.setOnClickListener { dialog.dismiss() }
    }

}