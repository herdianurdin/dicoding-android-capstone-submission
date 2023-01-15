package com.capstone.gometry.ui.leaderboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.gometry.adapter.LeaderboardAdapter
import com.capstone.gometry.data.model.User
import com.capstone.gometry.databinding.FragmentLeaderboardBinding
import com.capstone.gometry.ui.other_profile.OtherProfileActivity
import com.capstone.gometry.utils.Constants.EXTRA_USER
import com.capstone.gometry.utils.Response
import com.capstone.gometry.utils.ViewExtensions.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderboardFragment : Fragment() {
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding
    private val viewModel: LeaderboardViewModel by viewModels()

    private lateinit var leaderboardAdapter: LeaderboardAdapter

    private val launchPostActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { getDataUsers { leaderboardAdapter.submitList(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeaderboardBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataUsers { initialization(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initialization(listUser: List<User>) {
        leaderboardAdapter = LeaderboardAdapter()
        leaderboardAdapter.submitList(listUser)

        binding?.rvLeaderboard?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leaderboardAdapter
        }

        leaderboardAdapter.setOnStartActivityCallback(object: LeaderboardAdapter.OnStartActivityCallback {
            override fun onStartActivityCallback(user: User) {
                Intent(requireContext(), OtherProfileActivity::class.java).also { intent ->
                    intent.putExtra(EXTRA_USER, user)
                    launchPostActivity.launch(intent)
                }
            }
        })
    }

    private fun getDataUsers(handleAction: (List<User> ) -> Unit) {
        lifecycleScope.launchWhenResumed {
            viewModel.getUsers()
            viewModel.users.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> showLoading(true)
                    is Response.Failure -> showLoading(true)
                    is Response.Success -> {
                        handleAction(response.data)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding?.apply {
            rvLeaderboard.setVisible(!state)
            fakeRvLeaderboard.setVisible(state)
            if (state) shimmerLayout.startShimmer() else shimmerLayout.stopShimmer()
        }
    }
}