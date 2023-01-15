package com.capstone.gometry.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.capstone.gometry.R
import com.capstone.gometry.databinding.ActivityDetailBinding
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.ui.quiz.QuizActivity
import com.capstone.gometry.utils.Constants
import com.capstone.gometry.utils.Constants.EXTRA_DETAIL
import com.capstone.gometry.utils.ViewExtensions.setImageFromResource
import com.capstone.gometry.utils.viewBinding
import kotlinx.coroutines.*
import java.io.Serializable

class DetailActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDetailBinding::inflate)

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.btnQuiz.apply {
                isEnabled = false
                text = context.getString(R.string.quiz_solved)
            }
            setResult(Activity.RESULT_OK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        initialization()
    }

    private fun initialization() {
        val geometry = intent.serializable<Geometry>(EXTRA_DETAIL)!!
        binding.apply {
            tvName.text = geometry.name
            ivImage.setImageFromResource(this@DetailActivity, geometry.image)
            tvTheory.text = geometry.theory
            ivSurfaceAreaFormula.setBackgroundResource(geometry.surfaceArea)
            ivVolumeFormula.setBackgroundResource(geometry.volume)
            tvExampleQuestion.text = geometry.exampleQuestion
            ivExampleAnswer.setBackgroundResource(geometry.exampleAnswer)
            btnClose.setOnClickListener { finish() }
        }

        if (geometry.passed) binding.btnQuiz.apply {
            isEnabled = false
            text = context.getString(R.string.quiz_solved)
        } else binding.btnQuiz.setOnClickListener { handleActivityQuiz(geometry.id) }
    }

    private fun handleActivityQuiz(geometryId: String) {
        launcher.launch(
            Intent(this@DetailActivity, QuizActivity::class.java)
                .putExtra(Constants.EXTRA_GEOMETRY_ID, geometryId)
        )
    }
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}