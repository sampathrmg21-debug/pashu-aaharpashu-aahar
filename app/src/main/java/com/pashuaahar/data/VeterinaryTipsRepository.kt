package com.pashuaahar.data

import com.pashuaahar.model.VeterinaryTip

object VeterinaryTipsRepository {
    val tips = listOf(
        VeterinaryTip(
            title = "Clean trough before every feed",
            description = "Remove wet leftovers and rinse the manger to reduce mould and sour feed.",
            duration = "2 min",
            iconName = "clean"
        ),
        VeterinaryTip(
            title = "Store fodder above floor level",
            description = "Keep grain sacks dry, shaded, and away from wall moisture.",
            duration = "3 min",
            iconName = "storage"
        ),
        VeterinaryTip(
            title = "Introduce new mix slowly",
            description = "Blend the new recipe over three to five days so the rumen adapts.",
            duration = "4 min",
            iconName = "schedule"
        ),
        VeterinaryTip(
            title = "Keep mineral mix daily",
            description = "A small daily mineral dose supports fertility, bones, and steady milk output.",
            duration = "2 min",
            iconName = "mineral"
        )
    )
}
