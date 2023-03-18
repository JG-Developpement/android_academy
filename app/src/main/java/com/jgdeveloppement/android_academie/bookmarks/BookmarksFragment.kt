package com.jgdeveloppement.android_academie.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.databinding.FragmentBookmarksBinding
import com.jgdeveloppement.android_academie.home.ArticleAdapter
import com.jgdeveloppement.android_academie.injection.Injection
import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.retrofit.Status
import com.jgdeveloppement.android_academie.utils.Categories
import com.jgdeveloppement.android_academie.utils.Utils
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel

class BookmarksFragment : Fragment(), ArticleAdapter.OnClickItem {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var academyViewModel: AcademyViewModel
    private lateinit var navController: NavController
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupViewModel()
        initArticles()
    }

    private fun setupViewModel() {
        academyViewModel = ViewModelProvider(viewModelStore, Injection.provideAcademyViewModelFactory(requireContext()))[AcademyViewModel::class.java]
    }

    private fun initArticles(){
        academyViewModel.getArticleBookmarked().observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> resource.data?.let { initRecyclerView(it) }
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.bookmarkLayout, resource.message.toString())
            }
        }
    }

    private fun initRecyclerView(articles: List<Article>){
        articleAdapter = ArticleAdapter(articles.toMutableList(), this, true)
        binding.recyclerBookmark.adapter = articleAdapter
        binding.loginProgressLayout.visibility = View.GONE
    }

    private fun deleteBookmark(articleId: Int, position: Int){
        academyViewModel.deleteBookmark(articleId).observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    articleAdapter.notifyArticleRemoved(position)
                    binding.loginProgressLayout.visibility = View.GONE
                }
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.bookmarkLayout, resource.message.toString())
            }
        }
    }

    private fun navigateToArticle(articleId: Int, categories: Categories){
        if (categories == Categories.QCM_ANDROID || categories == Categories.QCM_KOTLIN){
            val action = BookmarksFragmentDirections.actionBookmarksFragmentToQcmFragment()
            action.articleId = articleId
            navController.navigate(action)
        }else{
            val action = BookmarksFragmentDirections.actionBookmarksFragmentToInterviewFragment()
            action.articleId = articleId
            action.articleCategories = Categories.getValues(categories)
            navController.navigate(action)
        }
    }

    override fun onClickArticleCard(articleId: Int, categories: Categories) {
        navigateToArticle(articleId, categories)
    }

    override fun onClickBookmarkBtn(articleId: Int, position: Int) {
        deleteBookmark(articleId, position)
    }
}