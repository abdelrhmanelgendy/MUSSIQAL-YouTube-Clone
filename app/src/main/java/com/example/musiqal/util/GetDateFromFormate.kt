package com.example.musiqal.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class GetDateFromFormate(private val pattern: String) {
    private val myCal: Calendar = GregorianCalendar()
    private fun getSimpleDateFormate(pattern: String) =
        SimpleDateFormat(pattern, Locale.US)

    private var simpleDateFormate: SimpleDateFormat

    init {
        simpleDateFormate = getSimpleDateFormate(pattern)
    }

    fun parseDate(date: String): String {
        myCal.time = simpleDateFormate.parse(date)!!
        return "${myCal.get(Calendar.DAY_OF_MONTH)}/${myCal.get(Calendar.MONTH)}/${
            myCal.get(Calendar.YEAR)
        }"
    }
}