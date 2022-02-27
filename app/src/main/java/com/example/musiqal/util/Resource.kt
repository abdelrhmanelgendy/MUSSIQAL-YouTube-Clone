package com.example.musiqal.util

sealed class Resource<T>(val data: T?, val message: String?) {

    class Success<T>(data: T) : Resource<T>(data, null)

    class Failed<T>(message: String) : Resource<T>(null, message)

}
