package io.creedengo.android.kotlin.checks.environment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.AbstractCheck

class BrightnessOverrideRuleParameterizedTest {

    @Test
    fun `is annotated with rule key GCI522`() {
        val annotation = BrightnessOverrideRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI522")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(BrightnessOverrideRule::class.java)
    }

    @Test
    fun `MESSAGE describes the brightness energy concern`() {
        assertThat(BrightnessOverrideRule.MESSAGE)
            .containsIgnoringCase("brightness")
            .containsIgnoringCase("energy")
    }

    @ParameterizedTest
    @CsvSource(
        "creedengo-android-java, EC522",
        "ecocode-android-java,   ESOB002",
    )
    fun `declares legacy rule key for backward compatibility`(repositoryKey: String, ruleKey: String) {
        val deprecatedKeys = BrightnessOverrideRule::class.java
            .getAnnotationsByType(DeprecatedRuleKey::class.java)

        assertThat(deprecatedKeys)
            .anySatisfy { key ->
                assertThat(key.repositoryKey).isEqualTo(repositoryKey)
                assertThat(key.ruleKey).isEqualTo(ruleKey)
            }
    }

    @ParameterizedTest
    @ValueSource(strings = ["1", "1f", "1F", "1.0", "1.0f", "1.0F"])
    fun `recognises every literal form that equals full brightness`(literal: String) {
        val normalised = literal.trimEnd('f', 'F').trimEnd('.')
        val isFullValue = normalised.toFloatOrNull() == 1.0f || normalised.toIntOrNull() == 1
        assertThat(isFullValue)
            .`as`("literal `%s` should be detected as full-brightness value", literal)
            .isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["0", "0.5f", "0.99", "2", "-1"])
    fun `does not flag literals that are not full brightness`(literal: String) {
        val normalised = literal.trimEnd('f', 'F').trimEnd('.')
        val isFullValue = normalised.toFloatOrNull() == 1.0f || normalised.toIntOrNull() == 1
        assertThat(isFullValue)
            .`as`("literal `%s` must not be treated as full brightness", literal)
            .isFalse
    }
}
