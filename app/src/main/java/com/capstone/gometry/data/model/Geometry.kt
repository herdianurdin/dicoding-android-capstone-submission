package com.capstone.gometry.data.model

import java.io.Serializable

data class Geometry(
    val id: String,
    val name: String,
    val preview: Int,
    val image: Int,
    val theory: String,
    val surfaceArea: Int,
    val volume: Int,
    val exampleQuestion: String,
    val exampleAnswer: Int,
    val model3dUrl: String,
    val passed: Boolean,
    val locked: Boolean,
): Serializable
