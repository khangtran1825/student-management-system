package com.studentmanagement.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TeacherRequest {

    @NotBlank(message = "Mã giảng viên không được để trống")
    @Size(max = 20, message = "Mã giảng viên không vượt quá 20 ký tự")
    public String teacherCode;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không vượt quá 100 ký tự")
    public String fullName;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Giới tính không hợp lệ")
    public String gender;

    @NotNull(message = "Ngày sinh không được để trống")
    public LocalDate dateOfBirth;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không vượt quá 100 ký tự")
    public String email;

    @Size(max = 15, message = "Số điện thoại không vượt quá 15 ký tự")
    public String phone;

    @Size(max = 150, message = "Khoa không vượt quá 150 ký tự")
    public String department;
}
