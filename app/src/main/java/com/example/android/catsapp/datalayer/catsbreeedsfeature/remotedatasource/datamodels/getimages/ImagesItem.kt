package com.example.android.catsapp.datalayer.catsbreeedsfeature.remotedatasource.datamodels.getimages

data class ImagesItem(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)