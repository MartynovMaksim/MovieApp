package ru.androidschool.intensiv.network.logger

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class CustomHttpLogging : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "OkHttp"
        if (!message.startsWith("{")) {
            Timber.tag(logName).d(message)
            return
        }
        try {
            val prettyPrintJson = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(JsonParser().parse(message))
            Timber.tag(logName).d(prettyPrintJson)
        } catch (m: JsonSyntaxException) {
            Timber.tag(logName).d(message)
        }
    }
}