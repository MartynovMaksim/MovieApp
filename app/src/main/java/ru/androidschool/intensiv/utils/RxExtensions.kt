package ru.androidschool.intensiv.utils

import android.view.View
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun <T> Single<T>.setSchedulersFromIoToMainThread(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.setSchedulersFromIoToMainThread(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.showAndHideView(view: View): Single<T> =
    this.doOnSubscribe {
        view.visibility = View.VISIBLE
    }
        .doFinally {
            view.visibility = View.GONE
        }

fun Completable.setCompletableToDbCall(tag: String): Disposable =
    this.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnError {
            Timber.tag(tag).e(it)
        }
        .subscribe()
