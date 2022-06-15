package com.dicoding.githubuserappv2.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.githubuserappv2.R
import com.dicoding.githubuserappv2.data.Status
import com.dicoding.githubuserappv2.data.local.entity.UserEntity
import com.dicoding.githubuserappv2.data.remote.response.DetailResponse
import com.dicoding.githubuserappv2.data.remote.response.ItemsItem
import com.dicoding.githubuserappv2.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PERSON = "sidiqpermana"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }

    private var isFav = false
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        val user = intent.getParcelableExtra<ItemsItem>(EXTRA_PERSON) as ItemsItem
        binding.tabs.visibility = View.GONE
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }

        viewModel.isFavorite(user.login).observe(this, {
            when (it) {
                is Status.Success -> {
                    if (it.data) {
                        setFavoriteIcon(true)
                        isFav = true
                    }
                }
            }
        })

        viewModel.getDetailUser(user.login).observe(this, { result ->
            if (result != null) {
                when (result) {
                    is Status.Loading -> {
                        binding.progressBarDetail.visibility = View.VISIBLE
                    }
                    is Status.Success -> {
                        binding.progressBarDetail.visibility = View.GONE
                        val userData = result.data
                        setDetailUser(userData)
                    }
                    is Status.Error -> {
                        binding.progressBarDetail.visibility = View.GONE
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal mendapatkan data",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.user = user.login
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabFav.setOnClickListener {
            if (isFav) {
                val data = UserEntity(user.login, user.avatarUrl, false)
                viewModel.removeFavUser(data)
                setFavoriteIcon(false)
                isFav = false
                Toast.makeText(
                    this@DetailActivity,
                    "Di hapus dari favorite",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val data = UserEntity(user.login, user.avatarUrl, true)
                viewModel.setFavUser(data)
                setFavoriteIcon(true)
                isFav = true
                Toast.makeText(
                    this@DetailActivity,
                    "Di tambah ke favorite",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun setFavoriteIcon(fav: Boolean) {
        if (fav) {
            binding.fabFav.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_filled
                )
            )
        } else {
            binding.fabFav.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_border
                )
            )
        }
    }


    private fun setDetailUser(user: DetailResponse) {
        binding.tabs.visibility = View.VISIBLE
        binding.tvDetailName.text = user.name
        binding.tvDetailUserName.text = "@${user.login}"
        binding.tvDetailLocation.text = user.location
        binding.tvDetailCompany.text = user.company
        binding.tvDetailNumOfRepository.text = user.publicRepos.toString()
        binding.tvDetailNumOfFollowers.text = user.followers.toString()
        binding.tvDetailNumOfFollowing.text = user.following.toString()
        Glide.with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.tvDetailImg)
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
            else -> return true
        }
    }

}