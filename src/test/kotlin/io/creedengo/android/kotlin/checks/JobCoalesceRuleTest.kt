/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.JobCoalesceRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class JobCoalesceRuleTest {

    @Test
    fun `is annotated with rule key GCI501`() {
        val annotation = JobCoalesceRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI501")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(JobCoalesceRule::class.java)
    }

    @Test
    fun `message mentions JobScheduler`() {
        assertThat(JobCoalesceRule.MESSAGE).contains("JobScheduler")
    }

    @Test
    fun `visits ten function matchers`() {
        assertThat(JobCoalesceRule().functionsToVisit).hasSize(10)
    }
}
