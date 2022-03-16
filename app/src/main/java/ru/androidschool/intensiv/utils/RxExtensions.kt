package ru.androidschool.intensiv.utils

import android.view.View
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun <T> Single<T>.setSchedulersForShowcaseRequest(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.showAndHideView(view: View): Single<T> =
    this.doOnSubscribe {
        view.visibility = View.VISIBLE
    }
        .doFinally {
            view.visibility = View.GONE
        }