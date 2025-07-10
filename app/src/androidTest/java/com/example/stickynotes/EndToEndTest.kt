package com.example.stickynotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class EndToEndTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullFlow_loginAddNote_logoutNavigatesBack() {
        // Wait for login screen
        composeTestRule.onNodeWithText("Prijava / Registracija").assertIsDisplayed()

        // Locate all text fields by setTextAction
        val fields = composeTestRule.onAllNodes(hasSetTextAction())
        // Enter email and password
        fields[0].performTextInput("test@example.com")
        fields[1].performTextInput("password123")
        // Perform signup
        composeTestRule.onNodeWithText("Registracija").performClick()

        // Wait until home screen shows
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("Moje bilješke")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Moje bilješke").assertIsDisplayed()

        // Add a note: the next text field is for note title
        composeTestRule.onNodeWithText("Naslov bilješke").assertExists()
        // Alternatively use fields[2]
        val homeField = composeTestRule.onAllNodes(hasSetTextAction())[2]
        homeField.performTextInput("E2ENote")
        composeTestRule.onNodeWithText("Dodaj").performClick()
        // Verify note appears
        composeTestRule.onNodeWithText("E2ENote").assertIsDisplayed()

        // Logout
        composeTestRule.onNodeWithText("Odjava").performClick()
        // Verify back to login
        composeTestRule.onNodeWithText("Prijava / Registracija").assertIsDisplayed()
    }
}
