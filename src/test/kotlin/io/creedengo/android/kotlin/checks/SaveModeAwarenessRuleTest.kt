/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.SaveModeAwarenessRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class SaveModeAwarenessRuleTest {

    @Test
    fun `is annotated with rule key GCI520`() {
        val annotation = SaveModeAwarenessRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI520")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(SaveModeAwarenessRule::class.java)
    }

    @Test
    fun `MESSAGE mentions power save mode`() {
        assertThat(SaveModeAwarenessRule.MESSAGE).containsIgnoringCase("power save mode")
    }

    @Test
    fun `BATTERY_ACTION_STRINGS contains BATTERY_CHANGED action`() {
        assertThat(SaveModeAwarenessRule.BATTERY_ACTION_STRINGS)
            .contains("android.intent.action.BATTERY_CHANGED")
    }

    @Test
    fun `BATTERY_ACTION_CONSTANTS contains ACTION_BATTERY_CHANGED`() {
        assertThat(SaveModeAwarenessRule.BATTERY_ACTION_CONSTANTS)
            .contains("ACTION_BATTERY_CHANGED")
    }
}
