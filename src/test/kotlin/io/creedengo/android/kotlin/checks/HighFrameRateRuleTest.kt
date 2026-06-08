/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.HighFrameRateRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class HighFrameRateRuleTest {

    @Test
    fun `is annotated with rule key GCI531`() {
        val annotation = HighFrameRateRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI531")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(HighFrameRateRule::class.java)
    }

    @Test
    fun `message mentions 60Hz`() {
        assertThat(HighFrameRateRule.MESSAGE).contains("60Hz")
    }

    @Test
    fun `visits one function matcher for setFrameRate`() {
        assertThat(HighFrameRateRule().functionsToVisit).hasSize(1)
    }
}
