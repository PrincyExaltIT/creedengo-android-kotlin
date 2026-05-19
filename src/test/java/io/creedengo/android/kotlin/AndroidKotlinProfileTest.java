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

import io.creedengo.android.kotlin.checks.ClearCacheCheck;
import org.junit.jupiter.api.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInActiveRule;
import org.sonar.check.Rule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the built-in "creedengo way" quality profile only references rules
 * declared in the {@link AndroidKotlinCheckList} — guards against drift between
 * the profile JSON and the actual check classes.
 */
class AndroidKotlinProfileTest {

    @Test
    void profile_references_only_known_rule_keys() {
        BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();
        new AndroidKotlinProfile().define(context);

        BuiltInQualityProfilesDefinition.BuiltInQualityProfile profile =
                context.profile(AndroidKotlinRulesDefinition.LANGUAGE, AndroidKotlinProfile.PROFILE_NAME);
        assertThat(profile).isNotNull();

        Set<String> profileKeys = profile.rules().stream()
                .map(BuiltInActiveRule::ruleKey)
                .collect(Collectors.toSet());

        Set<String> checkListKeys = AndroidKotlinCheckList.checks().stream()
                .map(c -> c.getAnnotation(Rule.class).key())
                .collect(Collectors.toSet());

        assertThat(profileKeys).isEqualTo(checkListKeys);
        assertThat(profileKeys).contains("GCI600");
    }

    @Test
    void clear_cache_check_is_annotated_with_GCI600() {
        Rule annotation = ClearCacheCheck.class.getAnnotation(Rule.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.key()).isEqualTo("GCI600");
    }
}
