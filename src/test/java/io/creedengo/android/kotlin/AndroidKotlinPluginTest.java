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
package io.creedengo.android.kotlin;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test verifying the plugin entry point registers exactly the expected SonarQube extensions.
 * This guards against broken wire-up before the rule itself runs against real Kotlin code.
 */
class AndroidKotlinPluginTest {

    private static final SonarRuntime RUNTIME = SonarRuntimeImpl.forSonarQube(
            Version.create(9, 9), SonarQubeSide.SERVER, SonarEdition.COMMUNITY);

    @Test
    void plugin_registers_three_expected_extensions() {
        Plugin.Context context = new Plugin.Context(RUNTIME);
        new AndroidKotlinPlugin().define(context);

        assertThat(context.getExtensions())
                .containsExactlyInAnyOrder(
                        AndroidKotlinRulesDefinition.class,
                        AndroidKotlinProfile.class,
                        AndroidKotlinExtensionsProvider.class);
    }
}
