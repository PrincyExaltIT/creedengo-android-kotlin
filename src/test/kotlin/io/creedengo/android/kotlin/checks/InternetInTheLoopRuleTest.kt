/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.InternetInTheLoopRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class InternetInTheLoopRuleTest {

    @Test
    fun `is annotated with rule key GCI502`() {
        val annotation = InternetInTheLoopRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI502")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(InternetInTheLoopRule::class.java)
    }

    @Test
    fun `MESSAGE mentions loop and battery`() {
        assertThat(InternetInTheLoopRule.MESSAGE).contains("loop").contains("battery")
    }

    @Test
    fun `visits one function matcher for openConnection`() {
        assertThat(InternetInTheLoopRule().functionsToVisit).hasSize(1)
    }
}
