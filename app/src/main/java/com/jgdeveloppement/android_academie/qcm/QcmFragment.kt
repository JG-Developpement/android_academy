package com.jgdeveloppement.android_academie.qcm

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.databinding.FragmentQcmBinding
import com.jgdeveloppement.android_academie.injection.Injection
import com.jgdeveloppement.android_academie.interview.InterviewFragmentArgs
import com.jgdeveloppement.android_academie.models.Quiz
import com.jgdeveloppement.android_academie.retrofit.Status
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel

class QcmFragment : Fragment() {

    private var _binding: FragmentQcmBinding? = null
    private val binding get() = _binding!!
    private lateinit var academyViewModel: AcademyViewModel
    private lateinit var navController: NavController
    private val args: InterviewFragmentArgs by navArgs()
    private lateinit var quizList: MutableList<Quiz>
    private var listSize = 0
    private var index = 0
    private var score = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentQcmBinding.inflate(inflater, container, false)
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
        getQuiz()

        binding.quizBtn.setOnClickListener {
            binding.quizScore.visibility = View.GONE

            if (index == listSize-2){
                binding.quizBtn.isEnabled = false
                binding.quizScore.text = getString(R.string.score, score.toString())
                binding.quizScore.visibility = View.VISIBLE
            }else{
                index++
                initView(quizList, index)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
    }


    private fun setupViewModel() {
        academyViewModel = ViewModelProvider(viewModelStore, Injection.provideAcademyViewModelFactory(requireContext()))[AcademyViewModel::class.java]
    }

    private fun getQuiz(){
        academyViewModel.getQuiz().observe(viewLifecycleOwner){ resource ->
            when(resource.status){
                Status.LOADING -> binding.loginProgressLayout.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    this.quizList = resource.data!!.filter { it.articleId == args.articleId }.toMutableList()
                    initView(quizList, 0)
                }
                Status.ERROR   -> {}
            }
        }
    }

    private fun initView(quizList: MutableList<Quiz>, index: Int){
        listSize = quizList.size+1
        this.index = index
        val quiz = quizList[index]
        binding.quizAsk.text = quiz.quizAsk
        generateRadioBtn(quiz)
        binding.loginProgressLayout.visibility = View.GONE
    }

    private fun checkResult(quiz: Quiz, answer: String){
        if (quiz.quizAnswer == answer){
            binding.quizScore.setTextColor(Color.parseColor("#959774"))
            binding.quizScore.text = getString(R.string.good_answer)
            score++
        }
        else{
            binding.quizScore.setTextColor(Color.parseColor("#D14242"))
            binding.quizScore.text = getString(R.string.wrong_answer, quiz.quizAnswer)
        }
        binding.quizScore.visibility = View.VISIBLE
        for (i1 in 0 until binding.quizRadioGroup.childCount) {
            binding.quizRadioGroup.getChildAt(i1).isEnabled = false
        }
    }


    @SuppressLint("ResourceType")
    private fun generateRadioBtn(quiz: Quiz){
        binding.quizRadioGroup.removeAllViews()
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.julee)
        if (!quiz.qcmA.contentEquals(getString(R.string.qcm_null))){
            val radioButton = RadioButton(requireContext())
            radioButton.text = quiz.qcmA
            radioButton.id = 1
            radioButton.typeface= typeface
            radioButton.textSize = 20f
            radioButton.setPadding(10,15,0,15)
            binding.quizRadioGroup.addView(radioButton)
        }
        if (!quiz.qcmB.contentEquals(getString(R.string.qcm_null))){
            val radioButton = RadioButton(requireContext())
            radioButton.text = quiz.qcmB
            radioButton.id = 2
            radioButton.typeface= typeface
            radioButton.textSize = 20f
            radioButton.setPadding(10,15,0,15)
            binding.quizRadioGroup.addView(radioButton)
        }
        if (!quiz.qcmC.contentEquals(getString(R.string.qcm_null))){
            val radioButton = RadioButton(requireContext())
            radioButton.text = quiz.qcmC
            radioButton.id = 3
            radioButton.typeface= typeface
            radioButton.textSize = 20f
            radioButton.setPadding(10,15,0,15)
            binding.quizRadioGroup.addView(radioButton)
        }
        if (!quiz.qcmD.contentEquals(getString(R.string.qcm_null))){
            val radioButton = RadioButton(requireContext())
            radioButton.text = quiz.qcmD
            radioButton.id = 4
            radioButton.typeface= typeface
            radioButton.textSize = 20f
            radioButton.setPadding(10,15,0,15)
            binding.quizRadioGroup.addView(radioButton)
        }

        binding.quizRadioGroup.setOnCheckedChangeListener { _, i ->
            when(i){
                1 -> checkResult(quiz, quiz.qcmA)
                2 -> checkResult(quiz, quiz.qcmB)
                3 -> checkResult(quiz, quiz.qcmC)
                4 -> checkResult(quiz, quiz.qcmD)
            }
        }
    }

}