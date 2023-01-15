package com.capstone.gometry.utils

import android.content.Context
import com.capstone.gometry.BuildConfig
import com.capstone.gometry.R
import com.capstone.gometry.data.model.Achievement
import com.capstone.gometry.data.model.Exam
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.data.model.GeometryAR

object GenerateData {
    fun generateGeometries(context: Context, userGeometries: List<String>? = null): List<Geometry> {
        val resources = context.resources
        val geometryId = resources.getStringArray(R.array.geometry_id)
        val geometryName = resources.getStringArray(R.array.geometry_name)
        val geometryPreview = resources.obtainTypedArray(R.array.geometry_preview)
        val geometryImage = resources.obtainTypedArray(R.array.geometry_image)
        val geometryTheory = resources.getStringArray(R.array.geometry_theory)
        val geometrySurfaceArea = resources.obtainTypedArray(R.array.geometry_surface_area)
        val geometryVolume = resources.obtainTypedArray(R.array.geometry_volume)
        val geometryExampleQuestion = resources.getStringArray(R.array.geometry_example_question)
        val geometryExampleAnswer = resources.obtainTypedArray(R.array.geometry_example_answer)
        val geometryModel3d = resources.getStringArray(R.array.geometry_model_3d)

        val listOfGeometry: ArrayList<Geometry> = arrayListOf()
        for (i in geometryId.indices) {
            var locked = true

            if (i == 0) locked = false
            else if (listOfGeometry[i-1].passed) locked = false

            val geometry = Geometry(
                id = geometryId[i],
                name = geometryName[i],
                preview = geometryPreview.getResourceId(i, -1),
                image = geometryImage.getResourceId(i, -1),
                theory = geometryTheory[i],
                surfaceArea = geometrySurfaceArea.getResourceId(i, -1),
                volume = geometryVolume.getResourceId(i, -1),
                exampleQuestion = geometryExampleQuestion[i],
                exampleAnswer = geometryExampleAnswer.getResourceId(i, -1),
                model3dUrl = String.format(BuildConfig.BASE_URL_STORAGE, "models%2F${geometryModel3d[i]}"),
                passed = if (userGeometries == null) false else geometryId[i] in userGeometries,
                locked = locked
            )
            listOfGeometry.add(geometry)
        }
        geometryPreview.recycle()
        geometryImage.recycle()
        geometrySurfaceArea.recycle()
        geometryVolume.recycle()
        geometryExampleAnswer.recycle()

        return listOfGeometry
    }

    fun generateGeometryAR(context: Context): List<GeometryAR> {
        val resources = context.resources
        val geometryPreview = resources.obtainTypedArray(R.array.geometry_preview)
        val geometryModel3d = resources.getStringArray(R.array.geometry_model_3d)

        val listOfGeometryAR = ArrayList<GeometryAR>()
        for (i in geometryModel3d.indices) {
            val geometryAR = GeometryAR(
                geometryPreview.getResourceId(i, -1),
                String.format(BuildConfig.BASE_URL_STORAGE, "models%2F${geometryModel3d[i]}")
            )
            listOfGeometryAR.add(geometryAR)
        }
        geometryPreview.recycle()

        return listOfGeometryAR
    }

    fun generateExams(context: Context, userGeometries: List<String>): List<Exam> {
        val resources = context.resources
        val examId = resources.getStringArray(R.array.exam_id)
        val examIcon = resources.obtainTypedArray(R.array.exam_icon)
        val examLevel = resources.getStringArray(R.array.exam_level)
        val examPoint = resources.getStringArray(R.array.exam_point)
        val geometryId = resources.getStringArray(R.array.geometry_id)
        val locked = userGeometries.size == geometryId.size && userGeometries.toSet() == geometryId.toSet()

        val listOfExams: ArrayList<Exam> = arrayListOf()
        for (i in examId.indices) {
            val exam = Exam(
                id = examId[i],
                icon = examIcon.getResourceId(i, -1),
                level = examLevel[i],
                point = examPoint[i].toInt(),
                locked = !locked
            )
            listOfExams.add(exam)
        }
        examIcon.recycle()

        return listOfExams
    }

    fun generateAchievements(context: Context, userAchievements: List<String>): List<Achievement> {
        val resources = context.resources
        val achievementId = resources.getStringArray(R.array.achievement_id)
        val achievementName = resources.getStringArray(R.array.achievement_name)
        val achievementMedal = resources.obtainTypedArray(R.array.achievement_medal)

        val listAchievement = ArrayList<Achievement>()
        for (i in achievementId.indices) {
            val achievement = Achievement(
                achievementId[i],
                achievementName[i],
                achievementMedal.getResourceId(i, -1)
            )
            if (achievementId[i] in userAchievements)
                listAchievement.add(achievement)
        }
        achievementMedal.recycle()

        return listAchievement
    }
}