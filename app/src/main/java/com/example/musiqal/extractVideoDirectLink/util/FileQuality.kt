package com.example.musiqal.extractVideoDirectLink.util

sealed class FileQuality(val msg:String){
    object QualityHigh:FileQuality("high")
    object QualityLow:FileQuality("low")
    object QualityMeduim:FileQuality("high")
}
