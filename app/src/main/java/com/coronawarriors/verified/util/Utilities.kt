package com.coronawarriors.verified.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.coronawarriors.verified.BuildConfig
import com.coronawarriors.verified.R
import java.lang.String
import java.util.*

object Utilities {

    /**
     * launch intent to share normal text message
     */
    fun sendShareIntent(context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        val message = String.format(
            Locale.ENGLISH, "%s %s %s",
            context.resources.getString(R.string.share_app_text),
            context.resources.getString(R.string.tap_on_link),
            BuildConfig.STORE_LINK
        )
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.app_name)))
    }

    /**
     * launch intent to send mail
     */
    fun sendContactUsIntent(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.DEVELOPER_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, Constants.EMPTY_STRING)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}