package com.redwasp.cubix.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64

class Utils {
    companion object {
        fun lightenColor(color: Int, factor: Float): Int {
            val r = Color.red(color) * factor
            val g = Color.green(color) * factor
            val b = Color.blue(color) * factor
            val ir = Math.min(255, r.toInt())
            val ig = Math.min(255, g.toInt())
            val ib = Math.min(255, b.toInt())
            val ia = Color.alpha(color)
            return Color.argb(ia, ir, ig, ib)
        }

//            @Size(3)
//            private fun colorToHSL(@ColorInt color: Int,
//                                   @Size(3) hsl: FloatArray = FloatArray(3)): FloatArray {
//                val r = Color.red(color) / 255f
//                val g = Color.green(color) / 255f
//                val b = Color.blue(color) / 255f
//
//                val max = Math.max(r, Math.max(g, b))
//                val min = Math.min(r, Math.min(g, b))
//                hsl[2] = (max + min) / 2
//
//                if (max == min) {
//                    hsl[1] = 0f
//                    hsl[0] = hsl[1]
//
//                } else {
//                    val d = max - min
//
//                    hsl[1] = if (hsl[2] > 0.5f) d / (2f - max - min) else d / (max + min)
//                    when (max) {
//                        r -> hsl[0] = (g - b) / d + (if (g < b) 6 else 0)
//                        g -> hsl[0] = (b - r) / d + 2
//                        b -> hsl[0] = (r - g) / d + 4
//                    }
//                    hsl[0] /= 6f
//                }
//                return hsl
//            }

    }
    class Base64Coverter {
        companion object {
            fun convert(stringCode: String): Bitmap {
                val decodedByteArray : ByteArray = Base64.decode(stringCode, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
            }
        }
    }
}