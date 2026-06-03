package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.KeepCpuOnRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class KeepCpuOnRuleTest {

    private val rule = KeepCpuOnRule()

    @Test
    fun `is annotated with rule key GCI507`() {
        val annotation = KeepCpuOnRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI507")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(KeepCpuOnRule::class.java)
    }

    @Test
    fun `message mentions keeping CPU on`() {
        assertThat(KeepCpuOnRule.MESSAGE)
            .isEqualTo("Keeping the CPU on should be avoided to avoid draining the battery.")
    }
}
