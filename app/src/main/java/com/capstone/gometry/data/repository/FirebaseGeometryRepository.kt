package com.capstone.gometry.data.repository

import android.content.Context
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.utils.Response

interface FirebaseGeometryRepository {
    fun getUserGeometries(
        context: Context,
        response: (Response<List<Geometry>>) -> Unit
    )
}