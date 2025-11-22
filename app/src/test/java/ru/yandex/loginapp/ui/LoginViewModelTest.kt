package ru.yandex.loginapp.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.yandex.loginapp.LoginScreenState
import ru.yandex.loginapp.LoginViewModel

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty email sets EmptyFieldsError`() = runTest {
        viewModel.login("", "qwerty")
        assertEquals(LoginScreenState.EmptyFieldsError, viewModel.state.value)
    }

    @Test
    fun `login with empty password sets EmptyFieldsError`() = runTest {
        viewModel.login("myemail@yandex.ru", "")
        assertEquals(LoginScreenState.EmptyFieldsError, viewModel.state.value)
    }

    @Test
    fun `login with not valid email sets EmailValidationError`() = runTest {
        viewModel.login("myemail", "mypassword")
        assertEquals(LoginScreenState.EmailValidationError, viewModel.state.value)
    }

    @Test
    fun `login with valid data sets LoginScreenState`() = runTest {
        viewModel.login("myemail@yandex.ru", "mypassword")
        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginScreenState.Loading, viewModel.state.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        viewModel.login("myemail@yandex.ru", "mypassword")
        testDispatcher.scheduler.runCurrent()
        testDispatcher.scheduler.advanceTimeBy(3100)
        assertEquals(LoginScreenState.Success, viewModel.state.value)
    }
}