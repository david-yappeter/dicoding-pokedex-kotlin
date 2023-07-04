package playground.example.dicoding_pokedex.component

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import playground.example.dicoding_pokedex.R


open class DrawerActivity : AppCompatActivity() {
    private lateinit var fullLayout: DrawerLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var mToggle : ActionBarDrawerToggle

    override fun setContentView(view: View) {
        fullLayout = layoutInflater.inflate(R.layout.drawer_n_activity, null) as DrawerLayout
        frameLayout = fullLayout.findViewById<View>(R.id.drawer_frame) as FrameLayout

        frameLayout.addView(view)

        super.setContentView(fullLayout)

        // Drawer Content
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Add Hamburger Icon
        mToggle = ActionBarDrawerToggle(this, fullLayout, R.string.open, R.string.close)
        // Add mToggle as listener
        fullLayout.addDrawerListener(mToggle)
        mToggle.syncState()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return mToggle.onOptionsItemSelected(item)
    }
}
