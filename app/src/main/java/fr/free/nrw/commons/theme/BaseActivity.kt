package fr.free.nrw.commons.theme

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import javax.inject.Inject
import javax.inject.Named
import fr.free.nrw.commons.R
import fr.free.nrw.commons.di.CommonsDaggerAppCompatActivity
import fr.free.nrw.commons.kvstore.JsonKvStore
import fr.free.nrw.commons.settings.Prefs
import fr.free.nrw.commons.CommonsApplication
import fr.free.nrw.commons.utils.SystemThemeUtils
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : CommonsDaggerAppCompatActivity() {

    @Inject
    @field:Named("default_preferences")
    lateinit var defaultKvStore: JsonKvStore

    @Inject
    lateinit var systemThemeUtils: SystemThemeUtils

    protected val compositeDisposable = CompositeDisposable()
    protected var wasPreviouslyDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ────── Apply saved UI language instantly ──────
        val savedLang = defaultKvStore.getString(Prefs.APP_UI_LANGUAGE, "") ?: ""
        if (savedLang.isNotEmpty()) {
            val newContext = CommonsApplication.setLocale(this, savedLang)
            resources.updateConfiguration(
                newContext.resources.configuration,
                newContext.resources.displayMetrics
            )
        }
        // ───────────────────────────────────────────────

        wasPreviouslyDarkTheme = systemThemeUtils.isDeviceInNightMode()
        setTheme(if (wasPreviouslyDarkTheme) R.style.DarkAppTheme else R.style.LightAppTheme)

        val fontScale = android.provider.Settings.System.getFloat(
            contentResolver,
            android.provider.Settings.System.FONT_SCALE,
            1f
        )
        adjustFontScale(resources.configuration, fontScale)
        enableEdgeToEdge()
    }

    override fun onResume() {
        if (wasPreviouslyDarkTheme != systemThemeUtils.isDeviceInNightMode()) {
            recreate()
        }
        super.onResume()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }
}