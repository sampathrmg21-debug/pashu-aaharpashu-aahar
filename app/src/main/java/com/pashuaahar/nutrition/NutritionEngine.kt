package com.pashuaahar.nutrition

import com.pashuaahar.model.CowBreed
import com.pashuaahar.model.CowProfile
import com.pashuaahar.model.FeedIngredient
import com.pashuaahar.model.FeedPrices
import com.pashuaahar.model.FeedRecipe
import kotlin.math.max
import kotlin.math.round

object NutritionEngine {
    fun buildRecipe(profile: CowProfile, prices: FeedPrices): FeedRecipe {
        val targetYield = max(profile.currentMilkYieldLiters, profile.targetMilkYieldLiters)
        val maintenanceConcentrate = profile.weightKg * 0.002
        val totalConcentrate = roundOne(
            max(1.5, maintenanceConcentrate + targetYield * profile.breed.concentratePerLiter)
        )

        val cakeShare = when (profile.breed) {
            CowBreed.Jersey -> 0.30 + targetYield * 0.006
            CowBreed.Desi -> 0.26 + targetYield * 0.005
        }.coerceIn(0.28, 0.42)
        val mineralShare = 0.03
        val maizeShare = (0.55 - targetYield * 0.004).coerceIn(0.40, 0.52)
        val branShare = (1.0 - cakeShare - mineralShare - maizeShare).coerceIn(0.12, 0.24)
        val normalizedTotal = cakeShare + mineralShare + maizeShare + branShare

        val maizeKg = roundOne(totalConcentrate * maizeShare / normalizedTotal)
        val cakeKg = roundOne(totalConcentrate * cakeShare / normalizedTotal)
        val branKg = roundOne(totalConcentrate * branShare / normalizedTotal)
        val mineralKg = roundOne(max(0.1, totalConcentrate - maizeKg - cakeKg - branKg))

        val ingredients = listOf(
            FeedIngredient(
                name = "Maize grain",
                iconName = "grain",
                kg = maizeKg,
                pricePerKg = prices.maizePerKg,
                proteinPercent = 9.0,
                energyMcalPerKg = 3.4
            ),
            FeedIngredient(
                name = "Cottonseed cake",
                iconName = "cake",
                kg = cakeKg,
                pricePerKg = prices.cottonseedCakePerKg,
                proteinPercent = 24.0,
                energyMcalPerKg = 2.7
            ),
            FeedIngredient(
                name = "Wheat bran",
                iconName = "bran",
                kg = branKg,
                pricePerKg = prices.wheatBranPerKg,
                proteinPercent = 15.0,
                energyMcalPerKg = 2.5
            ),
            FeedIngredient(
                name = "Mineral mix",
                iconName = "mineral",
                kg = mineralKg,
                pricePerKg = prices.mineralMixPerKg,
                proteinPercent = 0.0,
                energyMcalPerKg = 0.0
            )
        )

        val roughageKg = roundOne(profile.weightKg * profile.breed.dryMatterRate - totalConcentrate)
            .coerceAtLeast(3.0)
        val homeCost = ingredients.sumOf { it.dailyCost }
        val marketCost = totalConcentrate * prices.marketFeedPerKg
        val protein = ingredients.sumOf { it.kg * it.proteinPercent * 10.0 }
        val energy = ingredients.sumOf { it.kg * it.energyMcalPerKg }

        return FeedRecipe(
            ingredients = ingredients,
            roughageKg = roughageKg,
            totalConcentrateKg = totalConcentrate,
            dailyHomeCost = roundOne(homeCost),
            dailyMarketCost = roundOne(marketCost),
            dailySavings = roundOne(marketCost - homeCost),
            monthlySavings = roundOne((marketCost - homeCost) * 30.0),
            proteinGrams = roundOne(protein),
            energyMcal = roundOne(energy),
            proteinTargetGrams = roundOne(520.0 + targetYield * 95.0),
            energyTargetMcal = roundOne(6.2 + targetYield * 0.72)
        )
    }

    private fun roundOne(value: Double): Double = round(value * 10.0) / 10.0
}
