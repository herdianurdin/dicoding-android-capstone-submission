package com.capstone.gometry.ui.exam_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.gometry.R
import com.capstone.gometry.adapter.ExamAdapter
import com.capstone.gometry.data.model.Exam
import com.capstone.gometry.databinding.FragmentExamListBinding
import com.capstone.gometry.ui.exam.ExamActivity
import com.capstone.gometry.utils.Constants.EXTRA_EXAM_ID
import com.capstone.gometry.utils.Constants.EXTRA_EXAM_POINT
import com.capstone.gometry.utils.Response
import com.capstone.gometry.utils.ViewExtensions.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamListFragment : Fragment() {
    private var _binding: FragmentExamListBinding? = null
    private val binding get() = _binding
    private val viewModel: ExamListViewModel by viewModels()

    private lateinit var examAdapter: ExamAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamListBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
    }

    private fun getUserData() {
        lifecycleScope.launchWhenResumed {
            viewModel.getExams(requireContext())
            viewModel.exams.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> showLoading(true)
                    is Response.Failure -> showLoading(true)
                    is Response.Success -> {
                        bindViews(response.data)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun bindViews(listOfExam: List<Exam>) {
        examAdapter = ExamAdapter()
        examAdapter.submitList(listOfExam)

        binding?.rvExam?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = examAdapter
        }

        examAdapter.setOnStartActivityCallback(object: ExamAdapter.OnStartActivityCallback {
            override fun onStartActivityCallback(exam: Exam) {
                if (exam.locked) showAlertLockedGeometry()
                else showExam(exam.id, exam.point)
            }
        })
    }

    private fun showExam(examId: String, examPoint: Int) {
        Intent(requireContext(), ExamActivity::class.java).also {
            it.putExtra(EXTRA_EXAM_ID, examId)
            it.putExtra(EXTRA_EXAM_POINT, examPoint)
            startActivity(it)
        }
    }

    private fun showAlertLockedGeometry() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.locked))
            setMessage(getString(R.string.message_locked_exam))
            setNegativeButton(getString(R.string.ok)) { _, _ -> }
            create()
            show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding?.rvExam?.setVisible(!state)

        if (!state) binding?.apply {
            shimmerLayout.stopShimmer()
            fakeRvExam.removeAllViews()
        }
    }
}