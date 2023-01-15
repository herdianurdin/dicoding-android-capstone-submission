package com.capstone.gometry.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.gometry.R
import com.capstone.gometry.adapter.AchievementAdapter
import com.capstone.gometry.data.model.User
import com.capstone.gometry.databinding.FragmentProfileBinding
import com.capstone.gometry.ui.auth.AuthActivity
import com.capstone.gometry.utils.GenerateData.generateAchievements
import com.capstone.gometry.utils.Response
import com.capstone.gometry.utils.ViewExtensions.setImageFromUrl
import com.capstone.gometry.utils.ViewExtensions.setVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private var _firebaseAuth: FirebaseAuth? = null

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _firebaseAuth = null
    }

    private fun initialization() {
        _firebaseAuth = Firebase.auth
        getUserData()
    }

    private fun getUserData() {
        lifecycleScope.launchWhenResumed {
            viewModel.getCurrentUser()
            viewModel.user.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> showLoading(true)
                    is Response.Failure -> showLoading(true)
                    is Response.Success -> {
                        bindViews(response.data!!)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun bindViews(user: User) {
        binding?.apply {
            ivPhoto.setImageFromUrl(requireContext(), user.photoUrl!!)
            tvName.text = user.displayName
            tvEmail.text = user.email
            tvTotalPoint.text = if (user.point == null) "0" else user.point.toString()
            tvTotalMedal.text = if (user.achievements == null) "0" else "${user.achievements.size}"
            btnSignOut.setOnClickListener { handleBtnSignOut() }
        }

        if (user.achievements != null) {
            binding?.clEmptyAchievement?.setVisible(false)

            val achievementAdapter = AchievementAdapter()
            achievementAdapter.submitList(generateAchievements(requireContext(), user.achievements))

            binding?.rvAchievement?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = achievementAdapter
            }
        } else binding?.apply {
            rvAchievement.setVisible(false)
            clEmptyAchievement.setVisible(true)
        }
    }

    private fun handleBtnSignOut() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.sign_out)
            setMessage(getString(R.string.message_confirmation))
            setPositiveButton(getString(R.string.yes)) { _, _ -> handleSignOut() }
            setNegativeButton(getString(R.string.no)) { _, _ -> }
            create()
            show()
        }
    }

    private fun handleSignOut() {
        lifecycleScope.launchWhenResumed {
            viewModel.logout(requireContext()) {
                Intent(requireContext(), AuthActivity::class.java).also { intent ->
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding?.clProfile?.setVisible(!state)
        if (!state) binding?.apply {
                shimmerLayout.stopShimmer()
                clShimmer.removeAllViews()
            }
    }
}