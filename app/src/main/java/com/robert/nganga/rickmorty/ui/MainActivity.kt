package com.robert.nganga.rickmorty.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: RickMortyViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Get the NavHostFragment
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFrag.navController

        navController.addOnDestinationChangedListener{ _, destination, _ ->

            when(destination.id){
                R.id.searchFragment -> {binding.toolBar.visibility = View.GONE}
                else -> {binding.toolBar.visibility = View.VISIBLE}
            }

        }

        // Define AppBar Configuration
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        // Connect ToolBar with NavController
        binding.toolBar.setupWithNavController(navController, appBarConfiguration)

        //Connect NavigationView with NavController
        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isOpen){
            binding.drawerLayout.close()
        }else{
            super.onBackPressed()
        }
    }
}