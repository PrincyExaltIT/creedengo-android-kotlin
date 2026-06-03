package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyNotificationRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyNotificationRuleTest {

    @Test
    fun `is annotated with rule key GCI529`() {
        val annotation = ThriftyNotificationRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI529")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ThriftyNotificationRule::class.java)
    }

    @Test
    fun `message mentions vibration and sound`() {
        assertThat(ThriftyNotificationRule.MESSAGE)
            .contains("vibration")
            .contains("sound")
    }

    @Test
    fun `visits four method matchers`() {
        assertThat(ThriftyNotificationRule().functionsToVisit).hasSize(4)
    }
}
