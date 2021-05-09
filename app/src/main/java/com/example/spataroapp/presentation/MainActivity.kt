package com.example.spataroapp.presentation

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.spataroapp.R
import com.example.spataroapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.drawer_header.view.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var navigation_view: NavigationView
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun hideDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun setInfoDrawer(name: String, email: String, foto: String){
        drawerLayout.user_name.text = name
        drawerLayout.email.text = email
        val imageBytes = Base64.decode(foto, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        drawerLayout.profile_photo.setImageBitmap(decodedImage)
    }

    fun createToolbar(){
        //setting navigation drawer
        binding.appBar.isVisible = true
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        navigation_view = findViewById(R.id.nav_view)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
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
                R.id.clients_menu -> {
                    Toast.makeText(this, "CLIENTES", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
                R.id.files -> {
                    Toast.makeText(this, "REFERENCIAS", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
                R.id.orders -> {
                    Toast.makeText(this, "PEDIDOS", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
            }
            true
        }
    }
}