package ch.mobpro.eventapp.viewmodel.validation

import android.support.annotation.StringRes
import ch.mobpro.eventapp.R

enum class PasswordErrorEvent(@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    EMPTY(R.string.error_field_required),
    INVALID(R.string.error_invalid_password);

    override fun getErrorResource() = resourceId
}