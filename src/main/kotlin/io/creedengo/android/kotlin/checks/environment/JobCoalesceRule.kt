/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI501")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC501")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EBAT003")
class JobCoalesceRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Avoid using AlarmManager or a SyncAdapter for an alarm. Instead use the JobScheduler because the alarm triggers are mutualized."

        private const val ALARM_MANAGER = "android.app.AlarmManager"
        private const val SYNC_ADAPTER = "android.content.AbstractThreadedSyncAdapter"
    }

    override val functionsToVisit = listOf(
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "set"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setAlarmClock"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setAndAllowWhileIdle"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setExact"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setExactAndAllowWhileIdle"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setInexactRepeating"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setRepeating"),
        FunMatcher(definingSupertype = ALARM_MANAGER, name = "setWindow"),
        FunMatcher(definingSupertype = SYNC_ADAPTER, name = "onPerformSync"),
        FunMatcher(definingSupertype = SYNC_ADAPTER, name = "getSyncAdapterBinder"),
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
    }
}
