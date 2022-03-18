package com.example.android.catsapp.uilayer.catslistfeature.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.catsapp.datalayer.catsbreeedsfeature.CatsRepository
import com.example.android.catsapp.datalayer.catsbreeedsfeature.datamodels.getbreeds.BreedsItem
import com.example.android.catsapp.domainlayer.Either
import com.example.android.catsapp.domainlayer.NoInternetConnectionException
import com.example.android.catsapp.domainlayer.SearchReturnedZeroItemsException
import com.example.android.catsapp.uilayer.catslistfeature.datamodels.Breed
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.BreedItem
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.ErrorItem
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.HeaderItem
import com.example.android.catsapp.uilayer.catslistfeature.fragments.breedslist.recycler.models.LoadingItem
import com.example.android.catsapp.uilayer.catslistfeature.uistate.BreedDetailsUiState
import com.example.android.catsapp.uilayer.catslistfeature.uistate.BreedsListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BreedsViewModel(
    private val repository: CatsRepository
) : ViewModel() {
    private val _breedsListState =
        MutableLiveData(BreedsListUiState(breedsList = listOf(LoadingItem())))
    val breedsListUiState: LiveData<BreedsListUiState>
        get() = _breedsListState

    private val _breedDetailsState = MutableLiveData(BreedDetailsUiState())
    val breedDetailsState: LiveData<BreedDetailsUiState>
        get() = _breedDetailsState

    private var breedsList: List<BreedsItem>? = null
    private var fetchBreedsJob: Job? = null
    fun fetchBreeds() {
        fetchBreedsJob?.cancel()

        _breedsListState.value = breedsListUiState.value!!.copy(
            breedsList = listOf(LoadingItem())
        )

        fetchBreedsJob = viewModelScope.launch {
            val result = repository.getBreeds()

            if (result is Either.Right) {
                breedsList = result.right
                _breedsListState.value = BreedsListUiState(
                    breedsList = listOf(HeaderItem()) + result.right.map { BreedItem(Breed.from(it)) }
                )
            } else {
                _breedsListState.value = BreedsListUiState(
                    breedsList = listOf(ErrorItem(NoInternetConnectionException()))
                )
            }
        }
    }

    private var fetchBreedImagesByCountJob: Job? = null
    fun fetchBreedImagesByCount(count: Int, breedsId: String) {
        fetchBreedImagesByCountJob?.cancel()

        fetchBreedImagesByCountJob = viewModelScope.launch {
            val result = repository.getBreedsImageById(count, breedsId)

            if (result is Either.Right) {
                _breedDetailsState.value = BreedDetailsUiState(
                    list = result.right
                )
            }
        }
    }

    private var searchJob: Job? = null
    fun search(breed: String) {
        searchJob?.cancel()
        Log.e("search", breed)
        searchJob = viewModelScope.launch {
            val resultList = breedsList?.filter {
                it.name.contains(breed.dropWhitespaces(), true)
            } ?: emptyList()

            if (resultList.isEmpty()) {
                _breedsListState.value = breedsListUiState.value!!.copy(
                    breedsList = listOf(HeaderItem(), ErrorItem(SearchReturnedZeroItemsException()))
                )
                return@launch
            }
            _breedsListState.value = breedsListUiState.value!!.copy(
                breedsList = listOf(HeaderItem()) + resultList.map { BreedItem(Breed.from(it)) }
            )
        }

    }

    private fun String.dropWhitespaces(): String =
        this.dropWhile { it.isWhitespace() }.dropLastWhile { it.isWhitespace() }

    override fun onCleared() {
        super.onCleared()
        fetchBreedsJob?.cancel()
        fetchBreedImagesByCountJob?.cancel()
        searchJob?.cancel()
    }
}