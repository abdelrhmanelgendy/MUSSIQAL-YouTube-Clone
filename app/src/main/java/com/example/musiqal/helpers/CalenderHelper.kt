package com.example.musiqal.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class CalenderHelper {
    var calender:Calendar
    init {
        calender= Calendar.getInstance()
    }
    fun setCalender(timeInMillis:Long)
    {
        calender.timeInMillis=timeInMillis
    }
    fun getDay(): Int {
        return calender.get(Calendar.DAY_OF_MONTH)
    }
    fun getMonth(): Int {
        return calender.get(Calendar.MONTH)
    }
    fun getYear(): Int {
        return calender.get(Calendar.YEAR)
    }
    @SuppressLint("SimpleDateFormat")
    fun getMonthName(): String {
        val month_date = SimpleDateFormat("MMMM")
        val month_name = month_date.format(calender.getTime())
        return month_name
    }

    fun getHour(): Int {
        return calender.get(Calendar.HOUR)
    }
    fun getMinutes(): Int {
        return calender.get(Calendar.MINUTE)
    }
    fun getSeconds(): Int {
        return calender.get(Calendar.SECOND)
    }

    override fun toString(): String {
        return "${calender.get(Calendar.DAY_OF_MONTH)}-${calender.get(Calendar.MONTH)+1}-${calender.get(Calendar.YEAR)}"
    }


}