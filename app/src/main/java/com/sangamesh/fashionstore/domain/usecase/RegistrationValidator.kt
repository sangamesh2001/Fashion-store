package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.RegisterInfo

data class FieldErrors(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
) {
    val isValid: Boolean get() = name == null && email == null && phone == null && password == null && confirmPassword == null
}

object RegistrationValidator {
    const val MIN_PASSWORD_LENGTH = 6

    // Framework-independent so this validator is plain-JUnit testable without Robolectric/instrumentation.
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val phoneRegex = Regex("^[+]?[0-9 ()-]{7,15}$")

    fun validateLogin(email: String, password: String): FieldErrors = FieldErrors(
        email = emailError(email),
        password = if (password.isBlank()) "Password cannot be empty" else null,
    )

    fun validateRegistration(info: RegisterInfo, confirmPassword: String): FieldErrors = FieldErrors(
        name = if (info.fullName.isBlank()) "Name is required" else null,
        email = emailError(info.email),
        phone = if (!phoneRegex.matches(info.phoneNumber)) "Enter a valid phone number" else null,
        password = if (info.password.length < MIN_PASSWORD_LENGTH) "Password must be at least $MIN_PASSWORD_LENGTH characters" else null,
        confirmPassword = if (info.password != confirmPassword) "Passwords do not match" else null,
    )

    private fun emailError(email: String): String? =
        if (!emailRegex.matches(email)) "Enter a valid email address" else null
}
