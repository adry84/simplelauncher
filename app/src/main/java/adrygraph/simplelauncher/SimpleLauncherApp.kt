package adrygraph.simplelauncher

import android.app.Application

/**
 * Created by Audrey on 10/09/2017.
 * App class
 */

class SimpleLauncherApp : Application() {

    companion object {
        lateinit var instance: SimpleLauncherApp
            private set
        val BROADCAST_BG_COLOR_CHANGED  = "BG_COLOR_CHANGED"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
