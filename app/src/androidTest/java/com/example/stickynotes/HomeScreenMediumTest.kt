package com.example.stickynotes.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.compose.rememberNavController
import com.example.stickynotes.ui.screens.HomeScreen
import com.example.stickynotes.viewmodel.AuthViewModel
import com.example.stickynotes.viewmodel.FirestoreNotesViewModel
import org.junit.Rule
import org.junit.Test

class HomeScreenMediumTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_addEditDeleteNote_behavesCorrectly() {
        // Create fake ViewModels
        val fakeAuthVM = AuthViewModel().apply {
            // Directly emit a dummy user
            // In real tests you'd substitute the StateFlow
        }
        val fakeNotesVM = FirestoreNotesViewModel().apply {
            // Initialize with empty list
        }

        composeTestRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                notesVM = fakeNotesVM,
                authVM = fakeAuthVM
            )
        }

        // Initially placeholder is shown
        composeTestRule.onNodeWithText("Nema bilješki.").assertIsDisplayed()

        // Add a note
        composeTestRule.onNodeWithText("Naslov bilješke").performTextInput("MediumTest")
        composeTestRule.onNodeWithText("Dodaj").performClick()
        // Verify it appears
        composeTestRule.onNodeWithText("MediumTest").assertIsDisplayed()

        // Edit the note
        composeTestRule.onNodeWithContentDescription("Uredi").performClick()
        composeTestRule.onNodeWithText("Novi naslov").performTextInput("EditedTest")
        composeTestRule.onNodeWithText("Spremi").performClick()
        // Verify updated
        composeTestRule.onNodeWithText("EditedTest").assertIsDisplayed()

        // Delete the note
        composeTestRule.onNodeWithContentDescription("Obriši").performClick()
        // Placeholder returns
        composeTestRule.onNodeWithText("Nema bilješki.").assertIsDisplayed()
    }
}
