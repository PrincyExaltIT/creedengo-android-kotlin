/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.EverlastingServiceRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class EverlastingServiceRuleTest {

    @Test
    fun `is annotated with rule key GCI551`() {
        val annotation = EverlastingServiceRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI551")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(EverlastingServiceRule::class.java)
    }

    @Test
    fun `MESSAGE mentions stopService and battery`() {
        assertThat(EverlastingServiceRule.MESSAGE)
            .contains("stopService")
            .contains("battery")
    }
}
