package how.about.it.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import how.about.it.database.Prefs
import how.about.it.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG ="MainActivity"
    private lateinit var mainViewBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)
        var mainIntent : Intent = intent

        if(mainIntent.hasExtra("idToken")){
            val idToken = mainIntent?.getStringExtra("idToken")
            Log.d(TAG, "$idToken" )
            mainViewBinding.mainText.text = idToken
        }


    }
}