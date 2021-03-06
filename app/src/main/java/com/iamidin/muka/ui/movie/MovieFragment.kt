package com.iamidin.muka.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamidin.muka.R
import com.iamidin.muka.adapter.movie.MovieAdapter
import com.iamidin.muka.adapter.movie.MovieDiscoverAdapter
import com.iamidin.muka.model.Movie
import com.iamidin.muka.viewmodel.movie.MovieDiscoverViewModel
import com.iamidin.muka.viewmodel.movie.MovieNowPlayingViewModel
import com.iamidin.muka.viewmodel.movie.MoviePopularViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import java.util.*

class MovieFragment : Fragment() {

    private lateinit var discoverViewModel: MovieDiscoverViewModel
    private lateinit var nowPlayingViewModel: MovieNowPlayingViewModel
    private lateinit var popularViewModel: MoviePopularViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeToolbarTitle(resources.getString(R.string.app_name).toUpperCase(Locale.getDefault()) + " " + resources.getString(R.string.title_movie).toUpperCase(Locale.getDefault()))

        discoverViewModel = ViewModelProvider(this).get(MovieDiscoverViewModel::class.java)
        nowPlayingViewModel = ViewModelProvider(this).get(MovieNowPlayingViewModel::class.java)
        popularViewModel = ViewModelProvider(this).get(MoviePopularViewModel::class.java)

        initRecyclerView()

        subscribeUI()
    }

    private fun changeToolbarTitle(title: String?) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    private fun initRecyclerView() {
        rv_movies_discover.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_movies_discover.setHasFixedSize(true)

        rv_movies_now_playing.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_movies_now_playing.setHasFixedSize(true)

        rv_movies_popular.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_movies_popular.setHasFixedSize(true)
    }

    private fun subscribeUI() {
        discoverViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            showLoading(it, "discover")
        })

        discoverViewModel.movieLiveData.observe(viewLifecycleOwner, Observer {
            showMovieInRecyclerView(it, "discover")
        })

        nowPlayingViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            showLoading(it, "now_playing")
        })

        nowPlayingViewModel.movieLiveData.observe(viewLifecycleOwner, Observer {
            showMovieInRecyclerView(it, "now_playing")
        })

        popularViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            showLoading(it, "popular")
        })

        popularViewModel.movieLiveData.observe(viewLifecycleOwner, Observer {
            showMovieInRecyclerView(it, "popular")
        })
    }

    private fun showLoading(state: Boolean, shimmerId: String) {
        if (state) {
            when (shimmerId) {
                "discover" -> {
                    shimmer_layout_discover.visibility = View.VISIBLE
                    shimmer_layout_discover.startShimmer()
                    tv_title_discover.visibility = View.INVISIBLE
                }

                "now_playing" -> {
                    shimmer_layout_nowplaying.visibility = View.VISIBLE
                    shimmer_layout_nowplaying.startShimmer()
                    tv_title_now_playing.visibility = View.INVISIBLE
                }

                "popular" -> {
                    shimmer_layout_popular.visibility = View.VISIBLE
                    shimmer_layout_popular.startShimmer()
                    tv_title_popular.visibility = View.INVISIBLE
                }
            }
        } else {
            when (shimmerId) {
                "discover" -> {
                    shimmer_layout_discover.stopShimmer()
                    shimmer_layout_discover.visibility = View.INVISIBLE
                    tv_title_discover.visibility = View.VISIBLE
                }

                "now_playing" -> {
                    shimmer_layout_nowplaying.stopShimmer()
                    shimmer_layout_nowplaying.visibility = View.INVISIBLE
                    tv_title_now_playing.visibility = View.VISIBLE
                }

                "popular" -> {
                    shimmer_layout_popular.stopShimmer()
                    shimmer_layout_popular.visibility = View.INVISIBLE
                    tv_title_popular.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun showMovieInRecyclerView(
        movieList: List<Movie>,
        to: String
    ) {
        when(to) {
            "discover" -> {
                val adapter = MovieDiscoverAdapter(movieList)
                rv_movies_discover.adapter = adapter

            }

            "now_playing" -> {
                val adapter = MovieAdapter(movieList)
                rv_movies_now_playing.adapter = adapter

            }

            "popular" -> {
                val adapter = MovieAdapter(movieList)
                rv_movies_popular.adapter = adapter

            }
        }
    }

}