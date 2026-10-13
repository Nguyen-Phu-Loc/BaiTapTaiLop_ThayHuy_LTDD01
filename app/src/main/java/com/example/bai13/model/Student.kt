package com.example.bai13.model

import java.io.Serializable

// ── Data Class quản lý hồ sơ sinh viên ─────────────────────
data class Student(
    val id: String,         // Mã số sinh viên (VD: 22505120005)
    val name: String,       // Họ và tên
    val className: String,  // Lớp sinh hoạt (VD: 22CT111)
    val email: String,      // Địa chỉ email sinh viên
    val gpa: Double         // Điểm trung bình tích lũy (0.0 - 4.0)
) : Serializable {

    // Thuộc tính tính toán (Computed Property)
    val isHonorStudent: Boolean
        get() = gpa >= 3.6

    // Companion object — tạo student mẫu
    companion object {
        fun createSample() = Student(
            id = "22505120005",
            name = "Nguyễn Phú Lộc",
            className = "126TLTDT03",
            email = "2415141122110@sv.ute.udn.vn",
            gpa = 3.75
        )
    }
}