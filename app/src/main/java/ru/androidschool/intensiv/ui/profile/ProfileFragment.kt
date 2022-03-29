package ru.androidschool.intensiv.ui.profile

import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.database.MovieDao
import ru.androidschool.intensiv.database.MovieDatabase
import ru.androidschool.intensiv.database.TvShowDao
import ru.androidschool.intensiv.databinding.FragmentProfileBinding
import ru.androidschool.intensiv.utils.setSchedulersFromIoToMainThread
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var profileTabLayoutTitles: Array<String>

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val movieDao: MovieDao by lazy {
        MovieDatabase.get(requireContext()).movieDao()
    }
    private val tvShowDao: TvShowDao by lazy {
        MovieDatabase.get(requireContext()).tvShowDao()
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    private var profilePageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Toast.makeText(
                requireContext(),
                "Selected position: $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get()
            .load(R.drawable.ic_avatar)
            .transform(CropCircleTransformation())
            .placeholder(R.drawable.ic_avatar)
            .into(binding.avatar)

        profileTabLayoutTitles = resources.getStringArray(R.array.tab_titles)

        val profileAdapter = ProfileAdapter(
            this,
            profileTabLayoutTitles.size
        )
        binding.profileViewPager.adapter = profileAdapter

        binding.profileViewPager.registerOnPageChangeCallback(
            profilePageChangeCallback
        )

        TabLayoutMediator(binding.tabLayout, binding.profileViewPager) { tab, position ->
            disposables += Single.zip(
                movieDao.getMoviesCount(),
                tvShowDao.getTvShowCount()
            ) { moviesCount, tvShowsCount ->
                moviesCount + tvShowsCount
            }
                .setSchedulersFromIoToMainThread()
                .subscribe({
                    val spannableStringTitle = if (position == 0) {
                        SpannableString("${it}\n Понравилось").apply {
                            setSpan(
                                RelativeSizeSpan(2f),
                                0,
                                it.toString().count(),
                                0
                            )
                        }
                    } else {
                        SpannableString(getString(R.string.later)).apply {
                            setSpan(
                                RelativeSizeSpan(2f),
                                0, 2, 0
                            )
                        }
                    }
                    tab.text = spannableStringTitle
                }, {
                    Timber.tag(TAG).e(it)
                })
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}
