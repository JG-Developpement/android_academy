package com.jgdeveloppement.android_academie.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.databinding.FragmentKotlinBinding
import com.jgdeveloppement.android_academie.injection.Injection
import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.Bookmark
import com.jgdeveloppement.android_academie.retrofit.Status
import com.jgdeveloppement.android_academie.utils.Categories
import com.jgdeveloppement.android_academie.utils.Utils
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel

class KotlinFragment : Fragment(), ArticleAdapter.OnClickItem {

    private var _binding: FragmentKotlinBinding? = null
    private val binding get() = _binding!!
    private lateinit var academyViewModel: AcademyViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentKotlinBinding.inflate(inflater, container, false)
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
        academyViewModel.getArticles().observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> initRecyclerView(resource.data!!)
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.kotlinLayout, resource.message.toString())
            }
        }
    }

    private fun initRecyclerView(articles: List<Article>){
        binding.recyclerKotlin.adapter = ArticleAdapter(articles.filter { it.categories == Categories.INTERVIEW_KOTLIN || it.categories == Categories.KOTLIN || it.categories == Categories.QCM_KOTLIN }.toMutableList(),
            this, false)
        articles.lastOrNull { it.categories == Categories.INTERVIEW_KOTLIN || it.categories == Categories.KOTLIN || it.categories == Categories.QCM_KOTLIN }
            ?.let { article ->
                binding.kotlinTopArticleTitle.text = article.title
                binding.kotlinTopArticleCard.setOnClickListener { navigateToArticle(article.id, article.categories) }
                binding.kotlinBookmarkBtn.setOnClickListener {
                    val bookmark = Bookmark(null, article.id, 1)
                    insertBookmark(bookmark)
                }
            }
        binding.loginProgressLayout.visibility = View.GONE
    }

    private fun navigateToArticle(articleId: Int, categories: Categories){

        if (categories == Categories.QCM_KOTLIN){
            val action = HomeFragmentDirections.actionHomeFragment2ToQcmFragment()
            action.articleId = articleId
            navController.navigate(action)
        }else{
            val action = HomeFragmentDirections.actionHomeFragment2ToInterviewFragment()
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
                    Utils.showSnackBar(binding.kotlinLayout, getString(R.string.add_to_bookmark))
                }
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.kotlinLayout, resource.message.toString())
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