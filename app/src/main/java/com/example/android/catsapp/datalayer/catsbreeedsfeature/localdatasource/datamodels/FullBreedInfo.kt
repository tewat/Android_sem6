package com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.datamodels

import com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.room.entities.BreedEntity
import com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.room.entities.CharacteristicsEntity
import com.example.android.catsapp.datalayer.catsbreeedsfeature.localdatasource.room.entities.ImageEntity
import com.example.android.catsapp.datalayer.catsbreeedsfeature.remotedatasource.datamodels.getimages.Images
import com.example.android.catsapp.datalayer.catsbreeedsfeature.remotedatasource.datamodels.getimages.ImagesItem

data class FullBreedInfo(
    val id: String,
    val isSelected: Boolean,
    val name: String,
    val temperament: String,
    val description: String,
    val wikiUrl: String,
    val affectionLevel: Int,
    val adaptability: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val grooming: Int,
    val healthIssues: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val socialNeeds: Int,
    val strangerFriendly: Int,
    val vocalisation: Int,
    val mainImageUrl: String,
    val imagesUrl: List<String>
) {
    companion object {
        fun mapToBreedEntity(info: FullBreedInfo): BreedEntity = with(info) {
            BreedEntity(
                id,
                name,
                temperament,
                description,
                wikiUrl,
                mainImageUrl,
                mapToCharacteristicsEntity(this),
                imagesUrls = info.imagesUrl
            )
        }


        fun mapToCharacteristicsEntity(info: FullBreedInfo): CharacteristicsEntity = with(info) {
            CharacteristicsEntity(
                affectionLevel = affectionLevel,
                adaptability = adaptability,
                childFriendly = childFriendly,
                dogFriendly = dogFriendly,
                energyLevel = energyLevel,
                grooming = grooming,
                healthIssues = healthIssues,
                intelligence = intelligence,
                sheddingLevel = sheddingLevel,
                socialNeeds = socialNeeds,
                strangerFriendly = strangerFriendly,
                vocalisation = vocalisation
            )
        }

        fun fromBreedEntity(entity: BreedEntity): FullBreedInfo = with(entity) {
            FullBreedInfo(
                id = breedId,
                isSelected = true,
                name,
                temperament,
                description,
                wikiUrl,
                affectionLevel = characteristics.affectionLevel,
                adaptability = characteristics.adaptability,
                childFriendly = characteristics.childFriendly,
                dogFriendly = characteristics.dogFriendly,
                energyLevel = characteristics.energyLevel,
                grooming = characteristics.grooming,
                healthIssues = characteristics.healthIssues,
                intelligence = characteristics.intelligence,
                sheddingLevel = characteristics.sheddingLevel,
                socialNeeds = characteristics.socialNeeds,
                strangerFriendly = characteristics.socialNeeds,
                vocalisation = characteristics.vocalisation,
                mainImageUrl,
                imagesUrl = imagesUrls
            )
        }

        fun fromImageItem(item: Images, selected: Boolean): FullBreedInfo = with(item) {
            val breed = first().breeds.first()
            val imagesUrl = map { it.url }

            with(breed) {
                FullBreedInfo(
                    id = id,
                    isSelected = selected,
                    name,
                    temperament,
                    description,
                    wikipedia_url,
                    affection_level,
                    adaptability,
                    child_friendly,
                    dog_friendly,
                    energy_level,
                    grooming,
                    health_issues,
                    intelligence,
                    shedding_level,
                    social_needs,
                    stranger_friendly,
                    vocalisation,
                    imagesUrl.first(),
                    imagesUrl
                )
            }
        }
    }
}
