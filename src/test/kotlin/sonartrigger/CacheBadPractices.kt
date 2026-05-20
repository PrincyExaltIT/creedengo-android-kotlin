package sonartrigger

import android.content.Context

class CacheBadPractices(private val context: Context) {

    fun triggerByCacheDir() {
        context.cacheDir.deleteRecursively() // attendu: GCI600
    }
}
