package com.pashuaahar.nutrition

import com.pashuaahar.model.CowBreed
import com.pashuaahar.model.CowProfile
import com.pashuaahar.model.FeedPrices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NutritionEngineTest {
    @Test
    fun recipeChangesWhenMilkTargetChanges() {
        val lowTarget = NutritionEngine.buildRecipe(
            profile = CowProfile(
                breed = CowBreed.Jersey,
                weightKg = 380.0,
                currentMilkYieldLiters = 8.0,
                targetMilkYieldLiters = 9.0
            ),
            prices = FeedPrices()
        )

        val highTarget = NutritionEngine.buildRecipe(
            profile = CowProfile(
                breed = CowBreed.Jersey,
                weightKg = 380.0,
                currentMilkYieldLiters = 8.0,
                targetMilkYieldLiters = 15.0
            ),
            prices = FeedPrices()
        )

        assertTrue(highTarget.totalConcentrateKg > lowTarget.totalConcentrateKg)
        assertNotEquals(lowTarget.ingredients, highTarget.ingredients)
    }

    @Test
    fun homeFeedCostIsComparedAgainstMarketFeed() {
        val recipe = NutritionEngine.buildRecipe(
            profile = CowProfile(targetMilkYieldLiters = 10.0),
            prices = FeedPrices(marketFeedPerKg = 48.0)
        )

        assertTrue(recipe.dailyMarketCost > 0.0)
        assertTrue(recipe.dailyHomeCost > 0.0)
        assertEquals(recipe.dailySavings * 30.0, recipe.monthlySavings, 0.01)
    }

    @Test
    fun desiBreedUsesLowerConcentrateThanJerseyForSameYield() {
        val prices = FeedPrices()
        val jersey = NutritionEngine.buildRecipe(
            CowProfile(breed = CowBreed.Jersey, targetMilkYieldLiters = 8.0),
            prices
        )
        val desi = NutritionEngine.buildRecipe(
            CowProfile(breed = CowBreed.Desi, targetMilkYieldLiters = 8.0),
            prices
        )

        assertTrue(jersey.totalConcentrateKg > desi.totalConcentrateKg)
    }
}
