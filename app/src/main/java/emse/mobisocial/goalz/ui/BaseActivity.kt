package emse.mobisocial.goalz.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import emse.mobisocial.goalz.R
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_base_basic.*


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Temporary for deciding which navigation menu and header to be shown
    // If the user use the app without login
    // it will use without_login_base_drawer menu
    private var loggedIn =  true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)
    }

    //Initialize the Navigation view and menu + floating button for Tabbed activity
    fun setFab(){
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // when the user using the app without login
        if(!loggedIn){
            nav_view.menu.clear()
            nav_view.inflateMenu(R.menu.without_login_base_drawer)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //Initialize profile menu from navigation view in Tabbed activity
        var header = nav_view.getHeaderView(0)
        var sidebarNickname : TextView = header.findViewById(R.id.sidebar_nickname)
        var profileImage : ImageView = header.findViewById(R.id.profile_image)

        // When the user logged in the profile picture and nickname will redirect to user's profile
        // if user using the app without login, it will only show Goalz there instead of nickname (as for now)
        if(loggedIn) {
            sidebarNickname.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            profileImage.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
        }else{
            sidebarNickname.text = getString(R.string.app_name)
        }
    }

    //Initialize the Navigation view and menu + floating button for Normal activity
    fun setFabBasic(){
        fab_basic.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // when the user using the app without login
        if(!loggedIn){
            nav_view_basic.menu.clear()
            nav_view_basic.inflateMenu(R.menu.without_login_base_drawer)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout_basic, toolbar_basic, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout_basic.addDrawerListener(toggle)
        toggle.syncState()

        nav_view_basic.setNavigationItemSelectedListener(this)

        //Initialize profile menu from navigation view in Tabbed activity
        var header_basic = nav_view_basic.getHeaderView(0)
        var sidebarNickname : TextView = header_basic.findViewById(R.id.sidebar_nickname)
        var profileImage : ImageView = header_basic.findViewById(R.id.profile_image)

        // When the user logged in the profile picture and nickname will redirect to user's profile
        // if user using the app without login, it will only show Goalz there instead of nickname (as for now)
        if(loggedIn){
            sidebarNickname.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            profileImage.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
        }else{
            sidebarNickname.text = getString(R.string.app_name)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (drawer_layout_basic != null && drawer_layout_basic.isDrawerOpen(GravityCompat.START)){
            drawer_layout_basic.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.base, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            // Available for user who use the app without login
            R.id.nav_explore -> {
                val intent = Intent(this, ExploreActivity::class.java)
                startActivity(intent)
            }

            // Available only for user who login
            R.id.nav_goals -> {
                val intent = Intent(this, GoalsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_library -> {
                val intent = Intent(this, UsersLibraryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_timeline -> {

            }
            R.id.nav_setting -> {

            }
            R.id.nav_logout -> {

            }

            // Without login menu
            R.id.nav_login -> {

            }
            R.id.nav_signup -> {

            }
        }
        if (drawer_layout != null) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout_basic.closeDrawer(GravityCompat.START)
        }
        return true
    }
}