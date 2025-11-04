package fr.free.nrw.commons.settings

import android.os.Bundle
import android.view.MenuItem
import fr.free.nrw.commons.CommonsApplication
import fr.free.nrw.commons.databinding.ActivitySettingsBinding
import fr.free.nrw.commons.kvstore.JsonKvStore
import fr.free.nrw.commons.theme.BaseActivity
import fr.free.nrw.commons.utils.applyEdgeToEdgeAllInsets
import javax.inject.Inject
import javax.inject.Named

class SettingsActivity : BaseActivity() {

    @Inject
    @field:Named("default_preferences")
    lateinit var defaultKvStore: JsonKvStore

    private lateinit var binding: ActivitySettingsBinding

    // Listener that instantly restarts the app when the UI language changes
    private val languageChangeListener = object : JsonKvStore.OnChangeListener {
        override fun onChange(key: String, value: Any?) {
            if (key == Prefs.APP_UI_LANGUAGE) {
                defaultKvStore.unregisterChangeListener(this)
                CommonsApplication.reloadActivity(this@SettingsActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        applyEdgeToEdgeAllInsets(view)
        setContentView(view)

        setSupportActionBar(binding.toolbarBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Watch for language preference changes
        defaultKvStore.registerChangeListener(languageChangeListener)
    }

    override fun onDestroy() {
        defaultKvStore.unregisterChangeListener(languageChangeListener)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}