package com.dicoding.githubuserappv2.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserappv2.R
import com.dicoding.githubuserappv2.data.Status
import com.dicoding.githubuserappv2.data.remote.response.ItemsItem
import com.dicoding.githubuserappv2.databinding.ActivityMainBinding
import com.dicoding.githubuserappv2.data.local.datastore.SettingPreferences
import com.dicoding.githubuserappv2.data.local.datastore.ThemeViewModel
import com.dicoding.githubuserappv2.data.local.datastore.ViewModelFactoryTheme

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User App"

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactoryTheme(pref)).get(
            ThemeViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getFindUser(query).observe(this@MainActivity, { result ->
                    if (result != null) {
                        when (result) {
                            is Status.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Status.Success -> {
                                binding.fragmentContainer.visibility = View.GONE
                                binding.tvNotFound.visibility = View.GONE
                                binding.rvUser.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                val userData= result.data
                                showRecycleView(userData)
                            }
                            is Status.Error -> {
                                binding.fragmentContainer.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@MainActivity,
                                    "Gagal mendapatkan data",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is Status.NotFound -> {
                                binding.fragmentContainer.visibility = View.GONE
                                binding.tvNotFound.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                binding.rvUser.visibility = View.GONE
                            }
                        }
                    }
                })
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.tvGuide.visibility = View.GONE
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val i = Intent(this, ListFavoriteActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.theme -> {
                val i = Intent(this, ThemeActivity::class.java)
                startActivity(i)
                return true
            }

            else -> return true
        }
    }

    private fun showRecycleView(listUser: List<ItemsItem>) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.tvGuide.visibility = View.GONE
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.tvGuide.visibility = View.GONE
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
        Log.d("CLICKED", "CHECKPOINT")

        val listAdapter = UserAdapter(listUser)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showDetailUser(data)
            }
        })

    }

    private fun showDetailUser(user: ItemsItem) {
        val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_PERSON, user)
        startActivity(moveWithObjectIntent)
    }

}