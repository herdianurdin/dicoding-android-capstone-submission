package com.capstone.gometry.ui.other_profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.gometry.adapter.AchievementAdapter
import com.capstone.gometry.data.model.User
import com.capstone.gometry.databinding.ActivityOtherProfileBinding
import com.capstone.gometry.utils.Constants.EXTRA_USER
import com.capstone.gometry.utils.GenerateData.generateAchievements
import com.capstone.gometry.utils.ViewExtensions.setImageFromUrl
import com.capstone.gometry.utils.ViewExtensions.setVisible
import com.capstone.gometry.utils.viewBinding
import java.io.Serializable

class OtherProfileActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityOtherProfileBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        initialization()
    }

    private fun initialization() {
        val user = intent.serializable<User>(EXTRA_USER)!!
        binding.apply {
            ivPhoto.setImageFromUrl(this@OtherProfileActivity, user.photoUrl!!)
            tvName.text = user.displayName
            tvEmail.text = user.email
            tvTotalPoint.text = if (user.point == null) "0" else user.point.toString()
            tvTotalMedal.text = if (user.achievements == null) "0" else "${user.achievements.size}"
            btnSignOut.setOnClickListener { finish() }
        }

        if (user.achievements != null) {
            binding.clEmptyAchievement.setVisible(false)

            val achievementAdapter = AchievementAdapter()
            achievementAdapter.submitList(generateAchievements(this@OtherProfileActivity, user.achievements))

            binding.rvAchievement.apply {
                layoutManager = LinearLayoutManager(this@OtherProfileActivity)
                adapter = achievementAdapter
            }
        } else binding.apply {
            rvAchievement.setVisible(false)
            clEmptyAchievement.setVisible(true)
        }
    }
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}