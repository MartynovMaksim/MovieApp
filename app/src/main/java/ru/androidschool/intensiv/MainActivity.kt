package ru.androidschool.intensiv

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.androidschool.intensiv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        const val KEY_MOVIE = "movie"
        const val KEY_TV = "tv_show"
        const val KEY_SEARCH = "search"
    }

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        binding.bottomNavView.setupWithNavController(navController)
    }
}
