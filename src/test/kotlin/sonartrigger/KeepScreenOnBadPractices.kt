package sonartrigger

import android.app.Activity
import android.os.Bundle

class KeepScreenOnBadPractices : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(0x00000080) // GCI505 attendu
    }
}

