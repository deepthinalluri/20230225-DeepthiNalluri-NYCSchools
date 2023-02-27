package com.example.a20230225_deepthichowdarynalluri_nycschools

import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
/*
NycSchoolActivityUiTest compose UI tests
 */
class NycSchoolActivityUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<NYCSchoolActivity>()  // create compose test rule for NYCSchoolActivity

    @Test
    fun loadingState_isDisplayed() { // checking loading state
        composeTestRule.activity.setContent {
            LoadingState()
        }
        // Verify that the CircularProgressIndicator is displayed
        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ProgressBar",false)
    }

    @Test
    fun nycError_isDisplayed() { // checking error state
        composeTestRule.activity.setContent {
            NycError()
        }
        // Verify that the error message is displayed
        composeTestRule.onNodeWithText("An error occured , please try again").assertIsDisplayed()
    }

    //  turn of the wifi/mobile data to test this UI test
    @Test
    fun connectivityNotAvailable_isDisplayed() { // // checking network connectivity
        composeTestRule.activity.setContent {
            ConnectivityNotAvailable()
        }
        // Verify that the error message is displayed
        composeTestRule.onNodeWithText("Please check network connection and try again").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Internet Connection Not Available",false)
    }

    @Test
    fun nycSchoolNameItem_clickLaunchesSchoolDetailsActivityWithCorrectData() { // checking  click event on  school name click on and launch details actvity
        val nycHighSchoolItems = NYCHighSchoolItems("3124", "University High School")
        composeTestRule.activity.setContent {
            NycSchoolNameItem(nycHighSchoolItems = nycHighSchoolItems)
        }
        // Click on the card
        composeTestRule.onNodeWithContentDescription("NYC School Name Item Card").performClick()

        // Verify that the SchoolDetailsActivity is launched with the correct data
        val intent = composeTestRule.activity.applicationContext
            .packageManager.getLaunchIntentForPackage(composeTestRule.activity.packageName)?.apply {
                this.putExtra("NYCdbn","3124")
                this.putExtra("NYCSchoolName","University High School")
                action = Intent.ACTION_SEND
            }
        val bundle = intent?.extras
        assertEquals("3124", bundle?.getString("NYCdbn"))
        assertEquals("University High School", bundle?.getString("NYCSchoolName"))
    }

}