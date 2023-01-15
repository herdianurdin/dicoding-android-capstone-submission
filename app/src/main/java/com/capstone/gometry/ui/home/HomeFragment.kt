package com.capstone.gometry.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.gometry.R
import com.capstone.gometry.adapter.GeometryAdapter
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.databinding.FragmentHomeBinding
import com.capstone.gometry.ui.detail.DetailActivity
import com.capstone.gometry.utils.Constants.EXTRA_DETAIL
import com.capstone.gometry.utils.Response
import com.capstone.gometry.utils.ViewExtensions.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var geometryAdapter: GeometryAdapter

    private val launchPostActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) getGeometriesWithUserInformation {
            geometryAdapter.submitList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getGeometriesWithUserInformation { initialization(it) }
    }

    private fun initialization(listOfGeometries: List<Geometry>) {
        geometryAdapter = GeometryAdapter()
        geometryAdapter.submitList(listOfGeometries)

        val recyclerView = binding?.rvGeometry
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = geometryAdapter
        }

        geometryAdapter.setOnStartActivityCallback(object: GeometryAdapter.OnStartActivityCallback {
            override fun onStartActivityCallback(geometry: Geometry) {
                if (geometry.locked) showAlertLockedGeometry()
                else Intent(requireContext(), DetailActivity::class.java).also { intent ->
                    intent.putExtra(EXTRA_DETAIL, geometry)
                    launchPostActivity.launch(intent)
                }
            }
        })
    }

    private fun showAlertLockedGeometry() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.locked))
            setMessage(getString(R.string.message_locked_geometry))
            setNegativeButton(getString(R.string.ok)) { _, _ -> }
            create()
            show()
        }
    }

    private fun getGeometriesWithUserInformation(handleAction: (List<Geometry>) -> Unit) {
        showLoading(true)

        lifecycleScope.launchWhenResumed {
            viewModel.getGeometries(requireContext())
            viewModel.geometries.observe(viewLifecycleOwner) { response ->
                when(response) {
                    is Response.Loading -> showLoading(true)
                    is Response.Failure -> showLoading(true)
                    is Response.Success -> {
                        showLoading(false)
                        handleAction(response.data)
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (!state) binding?.apply {
            rvGeometry.setVisible(true)
            shimmerLayout.stopShimmer()
            fakeRvGeometry.setVisible(false)
        } else binding?.apply {
            rvGeometry.setVisible(false)
            shimmerLayout.startShimmer()
            fakeRvGeometry.setVisible(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}