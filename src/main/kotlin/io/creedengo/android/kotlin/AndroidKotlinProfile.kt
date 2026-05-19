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

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader

class AndroidKotlinProfile : BuiltInQualityProfilesDefinition {

    override fun define(context: BuiltInQualityProfilesDefinition.Context) {
        val profile = context.createBuiltInQualityProfile(PROFILE_NAME, AndroidKotlinRulesDefinition.LANGUAGE)
        BuiltInQualityProfileJsonLoader.load(profile, AndroidKotlinRulesDefinition.REPOSITORY_KEY, PROFILE_PATH)
        profile.done()
    }

    companion object {
        const val PROFILE_PATH = "io/creedengo/android/kotlin/creedengo_way_profile.json"
        const val PROFILE_NAME = "creedengo way"
    }
}
