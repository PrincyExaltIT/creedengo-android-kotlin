package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.KeepScreenOnSetFlagsRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class KeepScreenOnSetFlagsRuleTest {

    private val rule = KeepScreenOnSetFlagsRule()

    @Test
    fun `is annotated with rule key GCI506`() {
        val annotation = KeepScreenOnSetFlagsRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI506")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(KeepScreenOnSetFlagsRule::class.java)
    }

    @Test
    fun `message mentions keeping screen on`() {
        assertThat(rule.getMessage())
            .isEqualTo("Keeping the screen on should be avoided to avoid draining the battery.")
    }
}
