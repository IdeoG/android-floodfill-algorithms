package ru.ideog.apps.floodfillvisualization.model

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import java.util.*

class FloodFillModel {

    private var width = 0
    private var height = 0
    private var fillColor: Int = -1
    private var startColor = intArrayOf(0, 0, 0)

    private lateinit var image: Bitmap
    private lateinit var pixels: IntArray
    private lateinit var pixelsChecked: BooleanArray
    private lateinit var ranges: Queue<FloodFillRange>

    fun floodFill(method: Int, x: Int, y: Int, targetColor: Int, replacementColor: Int) {
        when (method) {
            0 -> basicNonRecursiveFloodFill(x, y, targetColor, replacementColor)
            1 -> queueLinearFloodFill(x, y, targetColor, replacementColor)
            2 -> recursiveFloodFill(x, y, targetColor, replacementColor)
        }
    }

    fun useImage(img: Bitmap) {
        image = img
        width = img.width
        height = img.height
        pixels = IntArray(width * height)

        image!!.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1)
    }

    private fun basicNonRecursiveFloodFill(x: Int, y: Int, targetColor: Int, replacementColor: Int) {
        setTargetColor(targetColor)
        fillColor = replacementColor

        if (targetColor == fillColor) return

        val width = image.width
        val height = image.height
        val queue = LinkedList<Point>()
        var nnode: Point? = Point(x, y)

        do {
            var x = nnode!!.x
            val y = nnode.y
            while (x > 0 && image.getPixel(x - 1, y) == targetColor) x--
            var spanUp = false
            var spanDown = false

            while (x < width && image.getPixel(x, y) == targetColor) {
                image.setPixel(x, y, fillColor)

                if (!spanUp && y > 0 && image.getPixel(x, y - 1) == targetColor) {
                    queue.add(Point(x, y - 1))
                    spanUp = true
                } else if (spanUp && y > 0 && image.getPixel(x, y - 1) != targetColor) {
                    spanUp = false
                }

                if (!spanDown && y < height - 1 && image.getPixel(x, y + 1) == targetColor) {
                    queue.add(Point(x, y + 1))
                    spanDown = true
                } else if (spanDown && y < height - 1 && image.getPixel(x, y + 1) != targetColor) {
                    spanDown = false
                }
                x++
            }
            nnode = queue.pollFirst()
        } while (nnode != null)
    }

    private fun queueLinearFloodFill(x: Int, y: Int, targetColor: Int, replacementColor: Int) {
        setTargetColor(targetColor)
        fillColor = replacementColor

        prepare()

        if (startColor[0] == 0) {
            val startPixel = pixels[width * y + x]
            startColor[0] = startPixel shr 16 and 0xff
            startColor[1] = startPixel shr 8 and 0xff
            startColor[2] = startPixel and 0xff
        }

        linearFill(x, y)

        var range: FloodFillRange

        while (ranges.size > 0) {
            range = ranges.remove()

            var downPxIdx = width * (range.Y + 1) + range.startX
            var upPxIdx = width * (range.Y - 1) + range.startX
            val upY = range.Y - 1
            val downY = range.Y + 1

            for (i in range.startX..range.endX) {
                if (range.Y > 0 && !pixelsChecked[upPxIdx]
                        && checkPixel(upPxIdx))
                    linearFill(i, upY)

                if (range.Y < height - 1 && !pixelsChecked[downPxIdx]
                        && checkPixel(downPxIdx))
                    linearFill(i, downY)

                downPxIdx++
                upPxIdx++
            }
        }

        image!!.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1)
    }

    private fun recursiveFloodFill(x: Int, y: Int, targetColor: Int, replacementColor: Int) {
        val color = image.getPixel(x, y)
        if (color != targetColor) return

        image.setPixel(x, y, replacementColor)

        val up = clamp(y + 1, 0, height - 1)
        val down = clamp(y - 1, 0, height - 1)
        val left = clamp(x - 1, 0, width - 1)
        val right = clamp(x + 1, 0, width - 1)

        recursiveFloodFill(x, up, targetColor, replacementColor)
        recursiveFloodFill(x, down, targetColor, replacementColor)
        recursiveFloodFill(left, y, targetColor, replacementColor)
        recursiveFloodFill(right, y, targetColor, replacementColor)

    }

    private fun setTargetColor(targetColor: Int) {
        startColor[0] = Color.red(targetColor)
        startColor[1] = Color.green(targetColor)
        startColor[2] = Color.blue(targetColor)
    }

    private fun prepare() {
        pixelsChecked = BooleanArray(pixels.size)
        ranges = LinkedList<FloodFillRange>()
    }

    private fun linearFill(x: Int, y: Int) {
        var lFillLoc = x
        var pxIdx = width * y + x

        while (true) {
            pixels[pxIdx] = fillColor
            pixelsChecked[pxIdx] = true

            lFillLoc--
            pxIdx--

            if (lFillLoc < 0 || pixelsChecked[pxIdx] || !checkPixel(pxIdx)) {
                break
            }
        }

        lFillLoc++

        var rFillLoc = x

        pxIdx = width * y + x

        while (true) {
            pixels[pxIdx] = fillColor
            pixelsChecked[pxIdx] = true

            rFillLoc++
            pxIdx++

            if (rFillLoc >= width || pixelsChecked[pxIdx] || !checkPixel(pxIdx)) {
                break
            }
        }

        rFillLoc--

        val r = FloodFillRange(lFillLoc, rFillLoc, y)

        ranges.offer(r)
    }

    private fun checkPixel(px: Int): Boolean {
        val red = pixels[px].ushr(16) and 0xff
        val green = pixels[px].ushr(8) and 0xff
        val blue = pixels[px] and 0xff

        return (red >= startColor[0] && red <= startColor[0] &&
                green >= startColor[1] && green <= startColor[1] &&
                blue >= startColor[2] && blue <= startColor[2])
    }

    private inner class FloodFillRange(var startX: Int, var endX: Int, var Y: Int)

    private fun clamp(value: Int, min: Int, max: Int): Int = Math.max(min, Math.min(value, max))

}