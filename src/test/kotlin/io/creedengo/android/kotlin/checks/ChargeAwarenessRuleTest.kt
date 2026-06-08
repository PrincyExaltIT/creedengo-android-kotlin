/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ChargeAwarenessRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ChargeAwarenessRuleTest {

    @Test
    fun `is annotated with rule key GCI519`() {
        val annotation = ChargeAwarenessRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI519")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ChargeAwarenessRule::class.java)
    }

    @Test
    fun `MESSAGE mentions battery and power`() {
        assertThat(ChargeAwarenessRule.MESSAGE)
            .containsIgnoringCase("battery")
    }

    @Test
    fun `POWER_ACTION_STRINGS contains four entries`() {
        assertThat(ChargeAwarenessRule.POWER_ACTION_STRINGS).hasSize(4)
    }

    @Test
    fun `POWER_ACTION_CONSTANTS contains four entries`() {
        assertThat(ChargeAwarenessRule.POWER_ACTION_CONSTANTS).hasSize(4)
    }
}
