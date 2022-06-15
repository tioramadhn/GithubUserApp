package com.dicoding.githubuserappv2.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserappv2.R
import com.dicoding.githubuserappv2.data.Status
import com.dicoding.githubuserappv2.data.local.entity.UserEntity
import com.dicoding.githubuserappv2.data.remote.response.ItemsItem
import com.dicoding.githubuserappv2.databinding.ActivityDetailBinding
import com.dicoding.githubuserappv2.databinding.ActivityListFavoriteBinding

class ListFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Favorite User's"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }

        viewModel.getFavoriteUsers().observe(this, {
            binding.progressBar.visibility = View.GONE
            if (it.isNotEmpty()) {
                binding.tvFavorite.visibility = View.GONE
                showRecycleView(it)
            } else {
                binding.rvFavorite.visibility = View.GONE
                binding.tvFavorite.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecycleView(users: List<UserEntity>) {
        val listUser = users.map {
            ItemsItem(it.username, it.avatar)
        }
        val listFavorite = UserAdapter(listUser)
        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listFavorite
        }
        listFavorite.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showDetailUser(data)
            }
        })
    }

    private fun showDetailUser(user: ItemsItem) {
        val moveWithObjectIntent = Intent(this@ListFavoriteActivity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_PERSON, user)
        startActivity(moveWithObjectIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        menu.findItem(R.id.search).isVisible = false
        menu.findItem(R.id.favorite).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme -> {
                val i = Intent(this, ThemeActivity::class.java)
                startActivity(i)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return true
        }
    }

}