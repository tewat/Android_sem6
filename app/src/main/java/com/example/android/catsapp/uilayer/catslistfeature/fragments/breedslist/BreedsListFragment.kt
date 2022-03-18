package com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.catsapp.databinding.FragmentBreedsListBinding
import com.example.android.catsapp.uilayer.catslistfeature.datamodels.Breed
import com.example.android.catsapp.uilayer.catslistfeature.delegateadapter.CompositeAdapter
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.adapters.BreedAdapter
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.adapters.ErrorAdapter
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.adapters.HeaderAdapter
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.adapters.LoadingAdapter
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.BreedItem
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.ErrorItem
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.HeaderItem
import com.example.android.catsapp.uilayer.catslistfeature.viewmodel.BreedsViewModel

class BreedsListFragment : Fragment(),
    HasDefaultViewModelProviderFactory {
    private var _binding: FragmentBreedsListBinding? = null
    private val binding: FragmentBreedsListBinding
        get() = _binding!!

    private val viewModel: BreedsViewModel by activityViewModels()

    private val compositeAdapter = CompositeAdapter.build {
        add(BreedAdapter())

        add(HeaderAdapter(textWatcher = { s, start, before, count ->
            viewModel.search(s.toString())
        }))

        add(ErrorAdapter(reloadButtonListener = {
            viewModel.fetchBreeds()
        }))

        add(LoadingAdapter())
    }

    private val spanCount = 2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsListBinding.inflate(inflater)

        if (savedInstanceState == null) {
            viewModel.fetchBreeds()
        }

        binding.breedRecycler.apply {
            layoutManager = GridLayoutManager(this.context, spanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val item = compositeAdapter.currentList.getOrNull(position)
                            ?: return spanCount / spanCount
                        return when (item) {
                            is BreedItem -> spanCount / spanCount
                            else -> spanCount
                        }
                    }
                }
            }
            adapter = compositeAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.breedsListUiState.observe(this) { state ->
            if (!state.isErrorOccurred) {
                (binding.breedRecycler.adapter as CompositeAdapter).submitList(state.breedsList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}