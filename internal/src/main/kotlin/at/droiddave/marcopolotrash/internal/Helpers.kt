package at.droiddave.marcopolotrash.internal

import android.content.Context
import android.support.v4.content.ContextCompat

fun Context.getColorCompat(resId: Int) = ContextCompat.getColor(this, resId)