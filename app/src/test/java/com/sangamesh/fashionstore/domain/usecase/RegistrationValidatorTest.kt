package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.RegisterInfo
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistrationValidatorTest {

    @Test fun `valid login has no errors`() {
        val errors = RegistrationValidator.validateLogin("user@example.com", "password123")
        assertTrue(errors.isValid)
    }

    @Test fun `invalid email is rejected`() {
        val errors = RegistrationValidator.validateLogin("not-an-email", "password123")
        assertFalse(errors.isValid)
    }

    @Test fun `blank password is rejected`() {
        val errors = RegistrationValidator.validateLogin("user@example.com", "")
        assertFalse(errors.isValid)
    }

    @Test fun `mismatched confirm password is rejected`() {
        val info = RegisterInfo("Jane Doe", "jane@example.com", "9876543210", "password123")
        val errors = RegistrationValidator.validateRegistration(info, confirmPassword = "different")
        assertFalse(errors.isValid)
    }

    @Test fun `short password is rejected`() {
        val info = RegisterInfo("Jane Doe", "jane@example.com", "9876543210", "123")
        val errors = RegistrationValidator.validateRegistration(info, confirmPassword = "123")
        assertFalse(errors.isValid)
    }
}
