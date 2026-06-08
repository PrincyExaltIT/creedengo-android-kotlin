package io.creedengo.android.kotlin.checks.environment

import io.creedengo.android.kotlin.checks.helpers.FlagOnMethodCheck
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey

@Rule(key = "GCI506")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC506")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EIDL002")
class KeepScreenOnSetFlagsRule : FlagOnMethodCheck(
    methodName = "setFlags",
    fullyQualifiedClassName = "android.view.Window",
    flagValue = 0x00000080,   // FLAG_KEEP_SCREEN_ON
    parameterIndices = listOf(0, 1)  // setFlags(flags, mask) — check both parameters
) {
    override fun getMessage(): String =
        "Keeping the screen on should be avoided to avoid draining the battery."
}
