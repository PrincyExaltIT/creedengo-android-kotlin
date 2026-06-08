package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.TorchFreeRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class TorchFreeRuleTest {

    @Test
    fun `is annotated with rule key GCI530`() {
        val annotation = TorchFreeRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI530")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(TorchFreeRule::class.java)
    }

    @Test
    fun `message mentions flashlight`() {
        assertThat(TorchFreeRule.MESSAGE).contains("Flashlight")
    }
}
