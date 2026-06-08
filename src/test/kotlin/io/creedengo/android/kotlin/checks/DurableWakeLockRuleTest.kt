package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.DurableWakeLockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class DurableWakeLockRuleTest {

    @Test
    fun `is annotated with rule key GCI508`() {
        val annotation = DurableWakeLockRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI508")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(DurableWakeLockRule::class.java)
    }

    @Test
    fun `message mentions timeout`() {
        assertThat(DurableWakeLockRule.MESSAGE)
            .contains("timeout")
    }
}
