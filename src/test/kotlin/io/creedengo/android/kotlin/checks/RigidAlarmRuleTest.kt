package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.RigidAlarmRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class RigidAlarmRuleTest {

    @Test
    fun `is annotated with rule key GCI509`() {
        val annotation = RigidAlarmRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI509")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(RigidAlarmRule::class.java)
    }

    @Test
    fun `message mentions Doze Mode`() {
        assertThat(RigidAlarmRule.MESSAGE).contains("Doze Mode")
    }

    @Test
    fun `visits three AlarmManager method matchers`() {
        assertThat(RigidAlarmRule().functionsToVisit).hasSize(3)
    }
}
