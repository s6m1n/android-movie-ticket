package woowacourse.movie.screen.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import woowacourse.movie.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `영화리스트가_화면에_표시된다`() {
        onView(withId(R.id.list_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun `포스터가_화면에_표시된다`() {
        onView(withId(R.id.list_img_poster))
            .check(matches(isDisplayed()))
    }

    @Test
    fun `영화_제목이_화면에_표시된다`() {
        onView(withId(R.id.list_img_poster))
            .check(matches(isDisplayed()))
    }
}