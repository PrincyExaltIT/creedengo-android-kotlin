/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.BluetoothLowEnergyRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class BluetoothLowEnergyRuleTest {

    @Test
    fun `is annotated with rule key GCI518`() {
        val annotation = BluetoothLowEnergyRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI518")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(BluetoothLowEnergyRule::class.java)
    }

    @Test
    fun `ERROR_MESSAGE mentions Bluetooth Low Energy`() {
        assertThat(BluetoothLowEnergyRule.ERROR_MESSAGE)
            .containsIgnoringCase("Bluetooth Low Energy")
    }

    @Test
    fun `GOOD_PRACTICE_MESSAGE mentions android bluetooth le`() {
        assertThat(BluetoothLowEnergyRule.GOOD_PRACTICE_MESSAGE)
            .contains("android.bluetooth.le")
    }
}
