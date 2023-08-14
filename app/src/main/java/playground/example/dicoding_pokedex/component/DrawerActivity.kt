package playground.example.dicoding_pokedex.component

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import playground.example.dicoding_pokedex.AboutActivity
import playground.example.dicoding_pokedex.R


open class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fullLayout: DrawerLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var mToggle : ActionBarDrawerToggle

    override fun setContentView(view: View) {
        fullLayout = View.inflate(view.context, R.layout.drawer_n_activity, null) as DrawerLayout
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

        // Add Event Listener
        fullLayout.findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return mToggle.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pokemon -> {
                fullLayout.closeDrawer(GravityCompat.START)
                println("Friend Clicked")
                true
            }
            R.id.about -> {
                fullLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
        return true
    }
}
