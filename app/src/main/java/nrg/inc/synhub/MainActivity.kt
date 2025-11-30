package nrg.inc.synhub

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.synhub.shared.nav.Navigator
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            nrg.inc.synhub.ui.theme.SynhubTheme {
                Navigator()
            }
        }

    }

    fun setAppAppearance(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun setAppLocale(localeTag: String?) {
        val localeList = if (localeTag != null) {
            LocaleListCompat.forLanguageTags(localeTag)
        } else {
            LocaleListCompat.getEmptyLocaleList()
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }

}