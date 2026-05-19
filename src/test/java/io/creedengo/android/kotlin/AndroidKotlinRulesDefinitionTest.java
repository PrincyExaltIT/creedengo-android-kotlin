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
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the rule metadata (.json + .html) shipped in {@code src/main/resources/...} loads cleanly
 * and produces a Sonar repository containing GCI600.
 */
class AndroidKotlinRulesDefinitionTest {

    private static final SonarRuntime RUNTIME = SonarRuntimeImpl.forSonarQube(
            Version.create(9, 9), SonarQubeSide.SERVER, SonarEdition.COMMUNITY);

    @Test
    void repository_exposes_GCI600_with_loaded_metadata() {
        RulesDefinition.Context context = new RulesDefinition.Context();
        new AndroidKotlinRulesDefinition(RUNTIME).define(context);

        RulesDefinition.Repository repo = context.repository(AndroidKotlinRulesDefinition.REPOSITORY_KEY);
        assertThat(repo).isNotNull();
        assertThat(repo.language()).isEqualTo("kotlin");
        assertThat(repo.name()).isEqualTo(AndroidKotlinRulesDefinition.REPOSITORY_NAME);

        RulesDefinition.Rule gci600 = repo.rule("GCI600");
        assertThat(gci600).isNotNull();
        assertThat(gci600.name()).isEqualTo("Avoid programmatically clearing the application cache");
        assertThat(gci600.htmlDescription()).contains("clearApplicationUserData");
        assertThat(gci600.tags()).contains("android", "kotlin");
    }
}
