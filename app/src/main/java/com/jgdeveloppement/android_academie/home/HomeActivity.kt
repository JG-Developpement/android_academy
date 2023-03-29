package com.jgdeveloppement.android_academie.home

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.ask.AskFragmentDirections
import com.jgdeveloppement.android_academie.bookmarks.BookmarksFragmentDirections
import com.jgdeveloppement.android_academie.databinding.ActivityHomeBinding
import com.jgdeveloppement.android_academie.injection.Injection
import com.jgdeveloppement.android_academie.utils.Categories
import com.jgdeveloppement.android_academie.utils.Utils
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var academyViewModel: AcademyViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setupViewModel()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        title = ""
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        setBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        setSupportActionBar(binding.toolbar)
    }

    private fun setupViewModel() {
        academyViewModel = ViewModelProvider(viewModelStore, Injection.provideAcademyViewModelFactory(this))[AcademyViewModel::class.java]
    }

    private fun setBottomNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_view).setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment2, R.id.askFragment, R.id.bookmarksFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomView.visibility = View.VISIBLE
        if (this::searchView.isInitialized)
            searchView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomView.visibility = View.GONE
        if (this::searchView.isInitialized)
            searchView.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when (navController.currentDestination?.id) {
            R.id.homeFragment2, R.id.askFragment, R.id.bookmarksFragment -> {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
                searchView = menu.findItem(R.id.search)?.actionView as SearchView

                val icon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_button)
                icon.setImageResource(R.drawable.loupe_svgrepo_com)

                val closeIcon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
                closeIcon.setImageResource(R.drawable.close)

                val face = ResourcesCompat.getFont(this, R.font.shadows_light)
                val searchText = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
                searchText.threshold = 1
                searchText.typeface = face

                //autocomplete
                val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
                val to = intArrayOf(R.id.article_title)
                val cursorAdapter = SimpleCursorAdapter(this, R.layout.search_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
                searchView.suggestionsAdapter = cursorAdapter

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        hideKeyboard(binding.activityHomeLayout)
                        navigateToArticle(null, null, true, query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2))
                        newText?.let {
                            Utils.articleListSearch.forEach {
                                if (it.title.contains(newText, true))
                                    cursor.addRow(arrayOf(it.id, it.title, it.categories.name))
                                cursorAdapter.changeCursor(cursor)
                            }
                        }

                        return true
                    }
                })

                searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener{
                    override fun onSuggestionSelect(position: Int): Boolean {
                        return false
                    }

                    @SuppressLint("Range")
                    override fun onSuggestionClick(position: Int): Boolean {
                        val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                        val selectionId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                        val categories = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2))

                        hideKeyboard(binding.activityHomeLayout)
                        navigateToArticle(selectionId, Categories.valueOf(categories), false, null)

                        return true
                    }

                })
            }
            else -> {}
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun navigateToArticle(articleId: Int?, categories: Categories?, isSearch: Boolean, searchQuery: String?){

        if (isSearch){
            when (navController.currentDestination?.id) {
                R.id.askFragment -> {
                    val action = AskFragmentDirections.actionAskFragmentToSearchFragment()
                    action.searchQuery = searchQuery!!
                    navController.navigate(action)
                }
                R.id.bookmarksFragment -> {
                    val action = BookmarksFragmentDirections.actionBookmarksFragmentToSearchFragment()
                    action.searchQuery = searchQuery!!
                    navController.navigate(action)
                }
                else -> {
                    val action = HomeFragmentDirections.actionHomeFragment2ToSearchFragment()
                    action.searchQuery = searchQuery!!
                    navController.navigate(action)
                }
            }
        }else{
            if (categories == Categories.QCM_ANDROID || categories == Categories.QCM_KOTLIN){
                when (navController.currentDestination?.id) {
                    R.id.askFragment -> {
                        val action = AskFragmentDirections.actionAskFragmentToQcmFragment()
                        action.articleId = articleId!!
                        navController.navigate(action)
                    }
                    R.id.bookmarksFragment -> {
                        val action = BookmarksFragmentDirections.actionBookmarksFragmentToQcmFragment()
                        action.articleId = articleId!!
                        navController.navigate(action)
                    }
                    else -> {
                        val action = HomeFragmentDirections.actionHomeFragment2ToQcmFragment()
                        action.articleId = articleId!!
                        navController.navigate(action)
                    }
                }
            }else{
                when (navController.currentDestination?.id) {
                    R.id.askFragment -> {
                        val action = AskFragmentDirections.actionAskFragmentToInterviewFragment()
                        action.articleId = articleId!!
                        action.articleCategories = Categories.getValues(categories!!)
                        navController.navigate(action)
                    }
                    R.id.bookmarksFragment -> {
                        val action = BookmarksFragmentDirections.actionBookmarksFragmentToInterviewFragment()
                        action.articleId = articleId!!
                        action.articleCategories = Categories.getValues(categories!!)
                        navController.navigate(action)
                    }
                    else -> {
                        val action = HomeFragmentDirections.actionHomeFragment2ToInterviewFragment()
                        action.articleId = articleId!!
                        action.articleCategories = Categories.getValues(categories!!)
                        navController.navigate(action)
                    }
                }
            }
        }


    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        /** Used to navigate to this activity  */
        fun navigate(activity: FragmentActivity?) {
            val intent = Intent(activity, HomeActivity::class.java)
            ActivityCompat.startActivity(activity!!, intent, null)
            //activity.finish()
        }
    }
}