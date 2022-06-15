package com.dicoding.githubuserappv2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserappv2.data.Status
import com.dicoding.githubuserappv2.data.local.entity.UserEntity
import com.dicoding.githubuserappv2.data.remote.response.FollowingResponseItem
import com.dicoding.githubuserappv2.data.remote.response.ItemsItem
import com.dicoding.githubuserappv2.databinding.FragmentFollowingBinding


class FollowingFragment() : Fragment() {
    companion object {
        var USER = "sidiqpermana"
    }

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(USER)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        if (username != null) {
            viewModel.getFollowing(username).observe(this,{result ->
                if (result != null) {
                    when (result) {
                        is Status.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Status.Success -> {
                            binding.tvNotFound.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            val listFollowing = result.data
                            showRecycleView(listFollowing)
                        }
                        is Status.Error -> {
                            binding.tvNotFound.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                        }
                        is Status.NotFound -> {
                            binding.tvNotFound.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            })
        }


    }


    private fun showRecycleView(users: List<FollowingResponseItem>) {
        val listUser = users.map {
            ItemsItem(it.login, it.avatarUrl)
        }
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        val listFollowing = UserAdapter(listUser)
        binding.rvFollowing.adapter = listFollowing
        listFollowing.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showDetailUser(data)
            }
        })
    }

    private fun showDetailUser(user: ItemsItem) {
        val moveWithObjectIntent = Intent(activity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_PERSON, user)
        startActivity(moveWithObjectIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


