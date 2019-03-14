package ch.mobpro.eventapp.viewmodel.validation

import android.support.annotation.StringRes
import ch.mobpro.eventapp.R

enum class EmailErrorEvent(@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    EMPTY(R.string.error_field_required),
    INVALID_FORMAT(R.string.error_too_short_username);

    override fun getErrorResource() = resourceId
}