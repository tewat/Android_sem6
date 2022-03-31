package com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.room

import android.content.Context
import androidx.room.Room
import com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.CatsLocalDataSource
import com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.datamodels.FullBreedInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RoomLocalDataSource(
    context: Context,
    private val dispatcher: CoroutineDispatcher
) : CatsLocalDataSource {
    private val database = Room.databaseBuilder(
        context, CatsDatabase::class.java, "cats_database"
    ).build()

    override suspend fun addBreedToDb(breedDetails: FullBreedInfo) {
        withContext(dispatcher) {
            database.breedDao().insertBreed(FullBreedInfo.mapToBreedEntity(breedDetails))
        }

    }

    override suspend fun removeBreedFromDb(breedDetails: FullBreedInfo) {
        withContext(dispatcher) {
            database.breedDao().delete(FullBreedInfo.mapToBreedEntity(breedDetails))
        }
    }

    override suspend fun getAllBreedsFromDb(): List<FullBreedInfo> = withContext(dispatcher) {
        database.breedDao().getAllBreeds().map { FullBreedInfo.fromBreedEntity(it) }
    }

}