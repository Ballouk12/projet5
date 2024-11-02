package ma.ensa.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)
        val bottomNavBar: BottomNavigationView = findViewById(R.id.bottomNavBar)

        if (savedInstanceState == null) {
            loadFragment(HomeScreenFragment())
        }

    bottomNavBar.setOnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_graph_home -> {
                loadFragment(HomeScreenFragment())
                true
            }
            R.id.nav_graph_cart -> {
                loadFragment(CreateFragment())
                true
            }
            else -> false
        }
    }
}

private fun loadFragment(fragment: Fragment) {
    val fragmentManager: FragmentManager = supportFragmentManager
    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.nav_host_main, fragment)
    fragmentTransaction.commit()
}
}