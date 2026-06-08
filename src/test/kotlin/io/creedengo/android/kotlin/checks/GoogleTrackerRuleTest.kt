/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.social.GoogleTrackerRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class GoogleTrackerRuleTest {

    @Test
    fun `is annotated with rule key GCI532`() {
        val annotation = GoogleTrackerRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI532")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(GoogleTrackerRule::class.java)
    }

    @Test
    fun `GOOGLE_ANALYTICS_MESSAGE mentions gms analytics`() {
        assertThat(GoogleTrackerRule.GOOGLE_ANALYTICS_MESSAGE)
            .contains("com.google.android.gms.analytics")
    }

    @Test
    fun `FIREBASE_ANALYTICS_MESSAGE mentions firebase analytics`() {
        assertThat(GoogleTrackerRule.FIREBASE_ANALYTICS_MESSAGE)
            .contains("com.google.firebase.analytics")
    }
}
