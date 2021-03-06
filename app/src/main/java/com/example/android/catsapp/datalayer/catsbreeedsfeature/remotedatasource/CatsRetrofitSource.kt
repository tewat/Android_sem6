package com.example.android.catsapp.datalayer.catsbreeedsfeature.remotedatasource

import com.example.android.catsapp.datalayer.catsbreeedsfeature.remotedatasource.datamodels.getbreeds.Breeds
import com.example.android.catsapp.datalayer.catsbreeedsfeature.remotedatasource.datamodels.getimages.Images
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class CatsRetrofitSource(
    private val catsApi: CatsApi,
    private val ioDispatcher: CoroutineDispatcher
) : CatsRemoteDataSource {

    override suspend fun getBreeds(): Response<Breeds> = withContext(ioDispatcher) {
        catsApi.getBreeds()
    }

    override suspend fun getBreedsImageById(
        count: Int,
        breedId: String
    ): Response<Images> = withContext(ioDispatcher) {
        catsApi.getCountImagesOfBreed(limit = count, breedId = breedId)
    }
}