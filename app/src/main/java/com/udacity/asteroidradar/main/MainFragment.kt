package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.util.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: AsteroidAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val application: Application = requireNotNull(this.activity).application
        viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(
                    AsteroidDatabase.getInstance(application),
                    application
                )
            )[MainViewModel::class.java]



        binding.viewModel = viewModel
        adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })
        binding.asteroidRecycler.adapter = adapter


        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (viewModel.asteroidList.value == null || viewModel.asteroidList.value?.isEmpty() == true)
                binding.statusLoadingWheel.visibility = View.VISIBLE
            else
                binding.statusLoadingWheel.visibility = View.GONE

        })

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroid_menu -> viewModel.onItemMenuWeekAsteroidsClicked()
            R.id.show_today_Asteroid_menu -> viewModel.onItemMenuTodayAsteroidsClicked()
            R.id.show_saved_asteroid_menu -> viewModel.onItemMenuSavedAsteroidsClicked()
        }
        return true
    }
}
