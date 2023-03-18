package com.jgdeveloppement.android_academie.interview

import android.content.res.Configuration
import android.os.Bundle
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.databinding.FragmentInterviewBinding
import com.jgdeveloppement.android_academie.injection.Injection
import com.jgdeveloppement.android_academie.models.Bookmark
import com.jgdeveloppement.android_academie.models.InterviewAsk
import com.jgdeveloppement.android_academie.retrofit.Status
import com.jgdeveloppement.android_academie.utils.PicassoImageGetter
import com.jgdeveloppement.android_academie.utils.QuoteSpanClass
import com.jgdeveloppement.android_academie.utils.Utils
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel


class InterviewFragment : Fragment() {

    private var _binding: FragmentInterviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var academyViewModel: AcademyViewModel
    private lateinit var navController: NavController
    private lateinit var interviewList: MutableList<InterviewAsk>
    private val args: InterviewFragmentArgs by navArgs()
    private var listSize = 0
    private var index = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupViewModel()
        getBookmarked(args.articleId)
        getInterviewAsks()

        binding.interviewNextBtn.setOnClickListener {
            index++
            initView(interviewList, index)
            binding.interviewBackBtn.visibility = View.VISIBLE

            if (index == listSize-1)
                binding.interviewNextBtn.visibility = View.GONE
        }

        binding.interviewBackBtn.setOnClickListener {
            index--
            initView(interviewList, index)
            binding.interviewNextBtn.visibility = View.VISIBLE
            if (index == 0)
                binding.interviewBackBtn.visibility = View.GONE
        }

        binding.bookmarkFloatBtn.setOnClickListener {
            val bookmark = Bookmark(null, args.articleId, index)
            insertBookmark(bookmark)
        }
    }

    override fun onResume() {
        super.onResume()
        setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        academyViewModel = ViewModelProvider(viewModelStore, Injection.provideAcademyViewModelFactory(requireContext()))[AcademyViewModel::class.java]
    }

    private fun getInterviewAsks(){
        academyViewModel.getInterviewAsks().observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    this.interviewList = resource.data!!.filter { it.articleId == args.articleId }.sortedBy { it.page }.toMutableList()
                    initView(this.interviewList, index)
                }
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.interviewLayout, resource.message.toString())
            }
        }
    }

    //Room
    private fun insertBookmark(bookmark: Bookmark){
        academyViewModel.insertBookmark(bookmark).observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    binding.loginProgressLayout.visibility = View.GONE
                    Utils.showSnackBar(binding.interviewLayout, getString(R.string.add_to_bookmark))
                }
                Status.ERROR -> Utils.reactToError(binding.loginProgressLayout, getString(R.string.error_occured), binding.interviewLayout, resource.message.toString())
            }
        }
    }

    private fun getBookmarked(articleId: Int){
        academyViewModel.getBookmarkByArticleId(articleId).observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    index = resource.data!!.page
                    binding.loginProgressLayout.visibility = View.GONE
                }
                Status.ERROR -> {
                    index = 0
                    binding.loginProgressLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun initView(interviewAskList: MutableList<InterviewAsk>, index: Int){
        listSize = interviewAskList.size
        this.index = index
        if (listSize > 1)
            binding.interviewNextBtn.visibility = View.VISIBLE
        if (this.index > 0)
            binding.interviewBackBtn.visibility = View.VISIBLE
        val interviewAsk = interviewAskList[index]
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        binding.interviewLayout.fullScroll(ScrollView.FOCUS_UP)
        binding.interviewAskTitle.text = interviewAsk.ask
        binding.interviewAskCategories.text = args.articleCategories
        displayHtml(interviewAsk.answer, isLandscape)
    }

    private fun displayHtml(html: String, isLandscape: Boolean) {
        val imageGetter = PicassoImageGetter(binding.interviewAskAnswer, isLandscape, binding.loginProgressLayout);
        val styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)
        replaceQuoteSpans(styledText as Spannable)
        imageClick(styledText)

        binding.interviewAskAnswer.text = styledText
        binding.interviewAskAnswer.movementMethod = LinkMovementMethod.getInstance()
        if (imageGetter.haveImage() == null)
            binding.loginProgressLayout.visibility = View.GONE
    }

    private fun replaceQuoteSpans(spannable: Spannable) {
        val quoteSpans: Array<QuoteSpan> =
            spannable.getSpans(0, spannable.length - 1, QuoteSpan::class.java)

        for (quoteSpan in quoteSpans) {
            val start: Int = spannable.getSpanStart(quoteSpan)
            val end: Int = spannable.getSpanEnd(quoteSpan)
            val flags: Int = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(QuoteSpanClass(
                    ContextCompat.getColor(requireContext(), R.color.bleu),
                    ContextCompat.getColor(requireContext(), R.color.dark_green),
                    10F, 50F), start, end, flags)
        }
    }

    private fun imageClick(html: Spannable) {
        for (span in html.getSpans(0, html.length, ImageSpan::class.java)) {
            val flags = html.getSpanFlags(span)
            val start = html.getSpanStart(span)
            val end = html.getSpanEnd(span)
            html.setSpan(object : URLSpan(span.source) {
                override fun onClick(v: View) {
                    val action = InterviewFragmentDirections.actionInterviewFragmentToCodeViewFragment()
                    action.imageUrl = span.source.toString()
                    navController.navigate(action)
                }
            }, start, end, flags)
        }
    }
}