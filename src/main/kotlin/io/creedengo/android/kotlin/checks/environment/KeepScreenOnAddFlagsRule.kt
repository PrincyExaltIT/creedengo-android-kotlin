package io.creedengo.android.kotlin.checks.environment

import io.creedengo.android.kotlin.checks.helpers.FlagOnMethodCheck
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey

@Rule(key = "GCI505")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC505")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EIDL001")
class KeepScreenOnAddFlagsRule : FlagOnMethodCheck(
    methodName = "addFlags",
    fullyQualifiedClassName = "android.view.Window",
    flagValue = 0x00000080,   // FLAG_KEEP_SCREEN_ON
    parameterIndices = listOf(0)
) {
    override fun getMessage(): String =
        "Keeping the screen on should be avoided to avoid draining the battery."
}