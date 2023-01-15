package com.capstone.gometry.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.gometry.data.GeometryRepository
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GeometryRepository
): ViewModel() {
    private val _geometries = MutableLiveData<Response<List<Geometry>>>()
    val geometries: LiveData<Response<List<Geometry>>> = _geometries

    fun getGeometries(context: Context) {
        _geometries.value = Response.Loading
        repository.getUserGeometries(context) { _geometries.value = it }
    }
}