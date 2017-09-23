package adrygraph.simplelauncher

import android.app.Application

/**
 * Created by Audrey on 10/09/2017.
 */

class SimpleLauncherApp : Application() {

    companion object {
        lateinit var instance: SimpleLauncherApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
