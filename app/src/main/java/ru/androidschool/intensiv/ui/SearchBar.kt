package ru.androidschool.intensiv.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.SearchToolbarBinding
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    companion object {
        private const val MIN_LENGTH = 3
    }

    lateinit var binding: SearchToolbarBinding

    private var hint: String = ""
    private var isCancelVisible: Boolean = true

    init {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.SearchBar).apply {
                hint = getString(R.styleable.SearchBar_hint).orEmpty()
                isCancelVisible = getBoolean(R.styleable.SearchBar_cancel_visible, true)
                recycle()
            }
        }
    }

    fun setText(text: String?) {
        binding.searchEditText.setText(text)
    }

    fun clear() {
        binding.searchEditText.setText("")
    }

    fun doSearch(): Observable<String> = Observable.create<String> { emitter ->
        binding.searchEditText.afterTextChanged {
            Timber.tag("SearchBar").d(it.toString())
            emitter.onNext(it.toString())
        }
    }
        .filter { it.length > MIN_LENGTH }
        .map { it.trim() }
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = SearchToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        binding.searchEditText.hint = hint
        binding.deleteTextButton.setOnClickListener {
            binding.searchEditText.text.clear()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.searchEditText.afterTextChanged { text ->
            if (!text.isNullOrEmpty() && !binding.deleteTextButton.isVisible) {
                binding.deleteTextButton.visibility = View.VISIBLE
            }
            if (text.isNullOrEmpty() && binding.deleteTextButton.isVisible) {
                binding.deleteTextButton.visibility = View.GONE
            }
        }
    }

}
