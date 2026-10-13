package com.example.bai13

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.bai13.databinding.ActivityMainBinding
import com.example.bai13.model.Student
import com.example.bai13.utils.gone
import com.example.bai13.utils.show
import com.example.bai13.utils.toast
import com.example.bai13.utils.toAcademicRanking
import com.example.bai13.utils.trimmedText

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // ⭐ Sinh viên mặc định — luôn giữ nguyên để Reset về
    private val defaultStudent = Student(
        id = "2415141122110",
        name = "Nguyễn Phú Lộc",
        className = "126TLTDT03",
        email = "2415141122110@sv.ute.udn.vn",
        gpa = 3.75
    )

    // ⭐ Sinh viên hiện tại — có thể thay đổi
    private var currentStudent = defaultStudent

    companion object {
        private const val KEY_STUDENT_DATA = "KEY_STUDENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ⭐ Khôi phục nếu vừa xoay màn hình
        savedInstanceState?.getSerializable(KEY_STUDENT_DATA)?.let {
            currentStudent = it as Student
        }

        // Gán dữ liệu ban đầu
        bindStudentData(currentStudent)

        // Setup sự kiện
        setupRealTimePreview()
        setupUpdateButton()
        setupResetButton()
    }

    // ═══════════════════════════════════════════════════════════
    // ── Lưu trạng thái trước khi xoay màn hình ────────────────
    // ═══════════════════════════════════════════════════════════
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_STUDENT_DATA, currentStudent)
    }

    // ═══════════════════════════════════════════════════════════
    // ── Binding dữ liệu từ model lên Views ────────────────────
    // ═══════════════════════════════════════════════════════════
    private fun bindStudentData(student: Student) {
        with(binding) {
            tvStudentName.text = student.name
            tvStudentDetails.text = "MSSV: ${student.id} • Lớp: ${student.className}"
            tvStudentEmail.text = "Email: ${student.email}"
            tvGpaBadge.text = "${student.gpa} GPA • ${student.gpa.toAcademicRanking()}"

            // Honor badge
            if (student.isHonorStudent) {
                tvHonorBadge.show()
            } else {
                tvHonorBadge.gone()
            }

            edtGpaInput.setText(student.gpa.toString())
            edtGpaInput.error = null
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ── Preview xếp loại realtime ─────────────────────────────
    // ═══════════════════════════════════════════════════════════
    private fun setupRealTimePreview() {
        binding.edtGpaInput.doOnTextChanged { text, _, _, _ ->
            val input = text?.toString()?.trim() ?: ""

            if (input.isEmpty()) {
                binding.tvPreviewRanking.gone()
                return@doOnTextChanged
            }

            binding.edtGpaInput.error = null

            val tempScore = input.toDoubleOrNull()
            if (tempScore != null && tempScore in 0.0..4.0) {
                binding.tvPreviewRanking.text = "Dự kiến: ${tempScore.toAcademicRanking()}"
                binding.tvPreviewRanking.show()
            } else {
                binding.tvPreviewRanking.gone()
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ── Nút "Cập nhật GPA" — Validate + copy() ────────────────
    // ═══════════════════════════════════════════════════════════
    private fun setupUpdateButton() {
        binding.btnUpdateGpa.setOnClickListener {
            val gpa = binding.edtGpaInput.trimmedText().toDoubleOrNull()

            if (gpa == null || gpa !in 0.0..4.0) {
                binding.edtGpaInput.error = "GPA phải từ 0.0 đến 4.0"
                binding.edtGpaInput.requestFocus()
                toast("Điểm số không hợp lệ!")
                return@setOnClickListener
            }

            // ⭐ Cập nhật bất biến bằng copy()
            currentStudent = currentStudent.copy(gpa = gpa)

            bindStudentData(currentStudent)
            toast("Đã cập nhật GPA thành công!")
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ── Nút "Reset" — Quay về dữ liệu mặc định ────────────────
    // ═══════════════════════════════════════════════════════════
    private fun setupResetButton() {
        binding.btnReset.setOnClickListener {
            currentStudent = defaultStudent
            bindStudentData(currentStudent)
            toast("Đã khôi phục về dữ liệu mặc định")
        }
    }
}