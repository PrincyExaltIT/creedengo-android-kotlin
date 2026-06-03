package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyBluetoothLowEnergyRequestConnectionPriorityRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyBluetoothLowEnergyRequestConnectionPriorityRuleTest {

    @Test
    fun `is annotated with rule key GCI526`() {
        val annotation = ThriftyBluetoothLowEnergyRequestConnectionPriorityRule::class.java
            .getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI526")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java)
            .isAssignableFrom(ThriftyBluetoothLowEnergyRequestConnectionPriorityRule::class.java)
    }

    @Test
    fun `message mentions CONNECTION_PRIORITY_LOW_POWER`() {
        assertThat(ThriftyBluetoothLowEnergyRequestConnectionPriorityRule.MESSAGE)
            .contains("CONNECTION_PRIORITY_LOW_POWER")
    }
}
