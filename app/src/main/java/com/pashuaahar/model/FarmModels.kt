package com.pashuaahar.model

enum class CowBreed(
    val displayName: String,
    val dryMatterRate: Double,
    val concentratePerLiter: Double
) {
    Jersey("Jersey", dryMatterRate = 0.030, concentratePerLiter = 0.45),
    Desi("Desi", dryMatterRate = 0.025, concentratePerLiter = 0.38)
}

data class CowProfile(
    val breed: CowBreed = CowBreed.Jersey,
    val ageMonths: Int = 48,
    val weightKg: Double = 380.0,
    val currentMilkYieldLiters: Double = 8.0,
    val targetMilkYieldLiters: Double = 10.0
)

data class FeedPrices(
    val maizePerKg: Double = 24.0,
    val cottonseedCakePerKg: Double = 36.0,
    val wheatBranPerKg: Double = 22.0,
    val mineralMixPerKg: Double = 80.0,
    val marketFeedPerKg: Double = 42.0
)

data class FeedIngredient(
    val name: String,
    val iconName: String,
    val kg: Double,
    val pricePerKg: Double,
    val proteinPercent: Double,
    val energyMcalPerKg: Double
) {
    val dailyCost: Double = kg * pricePerKg
}

data class FeedRecipe(
    val ingredients: List<FeedIngredient>,
    val roughageKg: Double,
    val totalConcentrateKg: Double,
    val dailyHomeCost: Double,
    val dailyMarketCost: Double,
    val dailySavings: Double,
    val monthlySavings: Double,
    val proteinGrams: Double,
    val energyMcal: Double,
    val proteinTargetGrams: Double,
    val energyTargetMcal: Double
)

data class VeterinaryTip(
    val title: String,
    val description: String,
    val duration: String,
    val iconName: String
)
