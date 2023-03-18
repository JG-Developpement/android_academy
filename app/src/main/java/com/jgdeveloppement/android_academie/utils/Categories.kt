package com.jgdeveloppement.android_academie.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.jgdeveloppement.android_academie.R

enum class Categories {
    INTERVIEW_ANDROID,
    INTERVIEW_KOTLIN,
    QCM_ANDROID,
    QCM_KOTLIN,
    SECURITY,
    ANDROID,
    KOTLIN,
    GITHUB,
    OTHER,
    NEWS;


    companion object{
        fun getValues(categories: Categories): String =
            when(categories){
                INTERVIEW_ANDROID -> Utils.myApp.getString(R.string.interview)
                INTERVIEW_KOTLIN -> Utils.myApp.getString(R.string.interview_kotlin)
                QCM_ANDROID -> Utils.myApp.getString(R.string.qcm_android)
                QCM_KOTLIN -> Utils.myApp.getString(R.string.qcm_k)
                SECURITY -> Utils.myApp.getString(R.string.security_android)
                ANDROID, KOTLIN -> Utils.myApp.getString(R.string.theory_course)
                NEWS -> Utils.myApp.getString(R.string.actuality)
                GITHUB -> Utils.myApp.getString(R.string.git_trick)
                OTHER -> Utils.myApp.getString(R.string.other)
            }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun categoriesImage(categories: Categories): Drawable? =
            when(categories){
                INTERVIEW_ANDROID, INTERVIEW_KOTLIN -> Utils.myApp.getDrawable(R.drawable.interview)
                QCM_ANDROID, QCM_KOTLIN -> Utils.myApp.getDrawable(R.drawable.quiz)
                ANDROID -> Utils.myApp.getDrawable(R.drawable.logo7)
                KOTLIN -> Utils.myApp.getDrawable(R.drawable.kotlin)
                NEWS -> Utils.myApp.getDrawable(R.drawable.boy)
                SECURITY -> Utils.myApp.getDrawable(R.drawable.security6)
                GITHUB -> Utils.myApp.getDrawable(R.drawable.github)
                OTHER -> Utils.myApp.getDrawable(R.drawable.computer1)
            }
    }

}