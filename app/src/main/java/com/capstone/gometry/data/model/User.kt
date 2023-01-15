package com.capstone.gometry.data.model

import java.io.Serializable

data class User(
    var id: String? = null,
    var displayName: String? = null,
    var email: String? = null,
    val photoUrl: String? = null,
    val point: Int? = null,
    val achievements: List<String>? = null,
    val geometries: List<String>? = null,
): Serializable
