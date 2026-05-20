package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.BrightnessOverrideRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sonar.check.Rule

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BrightnessOverrideRuleTest {

    @Test
    fun `is annotated with rule key GCI522`() {
        val annotation = BrightnessOverrideRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI522")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(BrightnessOverrideRule::class.java).isAssignableFrom(BrightnessOverrideRule::class.java)
    }

    @Test
    fun `MESSAGE mentions the cache anti-pattern`() {
        assertThat(BrightnessOverrideRule.MESSAGE)
            .contains("Forcing brightness to max value may cause useless energy consumption.")
    }
}