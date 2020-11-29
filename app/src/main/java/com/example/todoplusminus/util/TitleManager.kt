package com.example.todoplusminus.util

import android.content.Context
import com.example.todoplusminus.R

object TitleManager {

    private var mContext: Context? = null

    fun setUp(context: Context) {
        mContext = context
    }

    val titleList: List<String>
        get() = if (mContext != null) listOf(
            mContext!!.getString(R.string.travel),
            mContext!!.getString(R.string.beer),
            mContext!!.getString(R.string.date),
            mContext!!.getString(R.string.scalp_massage),
            mContext!!.getString(R.string.an_hour_of_game),
            mContext!!.getString(R.string.newspaper_editorial),
            mContext!!.getString(R.string.walk),
            mContext!!.getString(R.string.haircut),
            mContext!!.getString(R.string.nail_art),
            mContext!!.getString(R.string.sanitary_day),
            mContext!!.getString(R.string.cheating_day),
            mContext!!.getString(R.string.writing_a_diary),
            mContext!!.getString(R.string.pot_water),
            mContext!!.getString(R.string.stretching),
            mContext!!.getString(R.string.riding_a_1km_bike),
            mContext!!.getString(R.string.a_general_house_cleaning),
            mContext!!.getString(R.string.breakfast),
            mContext!!.getString(R.string.yoga),
            mContext!!.getString(R.string.an_hour_of_study),
            mContext!!.getString(R.string.a_3km_run),
            mContext!!.getString(R.string.have_a_smoke),
            mContext!!.getString(R.string.instant_intake),
            mContext!!.getString(R.string.multivitamin),
            mContext!!.getString(R.string.a_cup_of_water),
            mContext!!.getString(R.string.say_hello_to_my_parents),
            mContext!!.getString(R.string.blog_posting),
            mContext!!.getString(R.string.car_wash),
            mContext!!.getString(R.string.a_bowl_of_salad),
            mContext!!.getString(R.string.protein_intake),
            mContext!!.getString(R.string.refueling),
            mContext!!.getString(R.string.quilt_burning),
            mContext!!.getString(R.string.gas_bullet_checking),
            mContext!!.getString(R.string.washroom),
            mContext!!.getString(R.string.bible_reading),
            mContext!!.getString(R.string.window_closing),
            mContext!!.getString(R.string._20_squats),
            mContext!!.getString(R.string.chapter_reading)
        )else listOf()
}