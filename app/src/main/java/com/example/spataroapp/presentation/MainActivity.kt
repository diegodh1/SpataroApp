package com.example.spataroapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.spataroapp.R
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var navigation_view: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting navigation drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout)
        navigation_view = findViewById(R.id.nav_view)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()
        //add listener to menu
        navigation_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.users_menu -> {
                    Toast.makeText(this, "USER", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
                R.id.clients_menu->{
                    Toast.makeText(this, "CLIENTES", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
                R.id.files->{
                    Toast.makeText(this, "REFERENCIAS", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
                R.id.orders->{
                    Toast.makeText(this, "PEDIDOS", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
            }
            true
        }
    }
}