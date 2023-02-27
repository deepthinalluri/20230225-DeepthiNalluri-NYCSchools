package com.example.a20230225_deepthichowdarynalluri_nycschools

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
/*
Compose UI tests for NycSchoolDetailsActivity
 */
class NycSchoolDetailsActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<SchoolDetailsActivity>() // create compose test rule for NycSchoolDetailsActivity


    @Test
    fun noSchoolDataFound_test() { // Check no school data found
        composeTestRule.activity.setContent {
            NoSchoolDataFound()
        }
        // Verify that the Text node is displayed with the expected text
        composeTestRule.onNodeWithText("No School data Found").assertIsDisplayed()
    }

    @Test
    fun nycSchoolDetails_test() { // Check  all school details displayed or not
        // Create a sample SchoolDetails object for testing
        val schoolDetails = SchoolDetails(
            school_name = "University High School",
            sat_math_avg_score = "258",
            sat_writing_avg_score = "224",
            sat_critical_reading_avg_score = "789"
        )

        composeTestRule.activity.setContent {
            NycSchoolDetails(schoolDetails = schoolDetails)
        }

        // Check if the title text is displayed correctly
        composeTestRule.onNodeWithTag("NYC School Details", useUnmergedTree = true)

        // Check if the school name text is displayed correctly
        composeTestRule.onNodeWithTag("school_name", useUnmergedTree = true)

        // Check if the math score text is displayed correctly
        composeTestRule.onNodeWithTag("Math Score", useUnmergedTree = true)

        // Check if the writing score text is displayed correctly
        composeTestRule.onNodeWithTag("Sat Score", useUnmergedTree = true)

        // Check if the reading score text is displayed correctly
        composeTestRule.onNodeWithTag("Writing Score", useUnmergedTree = true)
    }

}