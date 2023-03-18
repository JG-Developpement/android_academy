package com.jgdeveloppement.android_academie.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.databinding.FragmentSearchBinding
import com.jgdeveloppement.android_academie.injection.Injection
import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.Bookmark
import com.jgdeveloppement.android_academie.retrofit.Status
import com.jgdeveloppement.android_academie.utils.Categories
import com.jgdeveloppement.android_academie.utils.Utils
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel

class SearchFragment : Fragment(), ArticleAdapter.OnClickItem {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var academyViewModel: AcademyViewModel
    private lateinit var navController: NavController
    private lateinit var articleAdapter: ArticleAdapter
    private val args: SearchFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
        setupViewModel()
        initArticles()
    }

    private fun setupViewModel() {
        academyViewModel = ViewModelProvider(viewModelStore, Injection.provideAcademyViewModelFactory(requireContext()))[AcademyViewModel::class.java]
    }

    private fun initArticles(){
        initRecyclerView(Utils.articleListSearch.filter { it.title.contains(args.searchQuery, true) })
    }

    private fun initRecyclerView(articles: List<Article>){
        articleAdapter = ArticleAdapter(articles.toMutableList(), this, false)
        binding.recyclerSearch.adapter = articleAdapter
        binding.loginProgressLayout.visibility = View.GONE
    }

    private fun navigateToArticle(articleId: Int, categories: Categories){
        if (categories == Categories.QCM_ANDROID || categories == Categories.QCM_KOTLIN){
            val action = SearchFragmentDirections.actionSearchFragmentToQcmFragment()
            action.articleId = articleId
            navController.navigate(action)
        }else{
            val action = SearchFragmentDirections.actionSearchFragmentToInterviewFragment()
            action.articleId = articleId
            action.articleCategories = Categories.getValues(categories)
            navController.navigate(action)
        }
    }

    //Room
    private fun insertBookmark(bookmark: Bookmark){
        academyViewModel.insertBookmark(bookmark).observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    binding.loginProgressLayout.visibility = View.GONE
                    Utils.showSnackBar(binding.searchLayout, getString(R.string.add_to_bookmark))
                }
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.searchLayout, resource.message.toString())
            }
        }
    }

    override fun onClickArticleCard(articleId: Int, categories: Categories) {
        navigateToArticle(articleId, categories)
    }

    override fun onClickBookmarkBtn(articleId: Int, position: Int) {
        val bookmark = Bookmark(null, articleId, 1)
        insertBookmark(bookmark)
    }
}