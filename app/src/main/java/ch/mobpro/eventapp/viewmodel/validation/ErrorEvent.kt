package ch.mobpro.eventapp.viewmodel.validation

import android.content.Context
import android.support.annotation.StringRes

interface ErrorEvent {

    @StringRes
    fun getErrorResource(): Int
}

fun getError(from: Context, errorEvent: ErrorEvent?) = if (errorEvent == null || errorEvent.getErrorResource() == 0) {
    null
} else {
    from.getString(errorEvent.getErrorResource())
}
