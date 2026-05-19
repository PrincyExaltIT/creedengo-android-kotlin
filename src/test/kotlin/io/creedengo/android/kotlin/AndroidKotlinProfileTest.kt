/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.creedengo.android.kotlin

import io.creedengo.android.kotlin.checks.ClearCacheCheck
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition
import org.sonar.check.Rule

class AndroidKotlinProfileTest {

    @Test
    fun `profile references only known rule keys`() {
        val context = BuiltInQualityProfilesDefinition.Context()
        AndroidKotlinProfile().define(context)

        val profile = context.profile(AndroidKotlinRulesDefinition.LANGUAGE, AndroidKotlinProfile.PROFILE_NAME)
        assertThat(profile).isNotNull

        val profileKeys = profile!!.rules().map { it.ruleKey() }.toSet()

        assertThat(profileKeys).isEqualTo(AndroidKotlinRulesDefinition.RULE_KEYS.toSet())
        assertThat(profileKeys).contains("GCI600")
    }

    @Test
    fun `ClearCacheCheck is annotated with GCI600`() {
        val annotation = ClearCacheCheck::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI600")
    }
}
