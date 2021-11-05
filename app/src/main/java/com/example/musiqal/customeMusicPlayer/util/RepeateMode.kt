package com.example.musiqal.customeMusicPlayer.util

sealed class RepeateMode(val repeatMode:String){
    object NoRepeating: RepeateMode("no_repeat")
    object RepeateOnc: RepeateMode("repeate_once")
    object RepeateAll: RepeateMode("repeate_all")
}