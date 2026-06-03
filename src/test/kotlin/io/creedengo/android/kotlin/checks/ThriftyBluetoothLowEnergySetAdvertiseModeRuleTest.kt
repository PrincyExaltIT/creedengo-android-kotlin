package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyBluetoothLowEnergySetAdvertiseModeRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyBluetoothLowEnergySetAdvertiseModeRuleTest {

    @Test
    fun `is annotated with rule key GCI525`() {
        val annotation = ThriftyBluetoothLowEnergySetAdvertiseModeRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI525")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java)
            .isAssignableFrom(ThriftyBluetoothLowEnergySetAdvertiseModeRule::class.java)
    }

    @Test
    fun `message mentions ADVERTISE_MODE_LOW_POWER`() {
        assertThat(ThriftyBluetoothLowEnergySetAdvertiseModeRule.MESSAGE)
            .contains("ADVERTISE_MODE_LOW_POWER")
    }
}
