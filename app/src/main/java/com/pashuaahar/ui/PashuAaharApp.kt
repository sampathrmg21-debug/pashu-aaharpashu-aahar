package com.pashuaahar.ui

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pashuaahar.data.VeterinaryTipsRepository
import com.pashuaahar.model.CowBreed
import com.pashuaahar.model.CowProfile
import com.pashuaahar.model.FeedIngredient
import com.pashuaahar.model.FeedPrices
import com.pashuaahar.model.FeedRecipe
import com.pashuaahar.model.VeterinaryTip
import com.pashuaahar.nutrition.NutritionEngine
import com.pashuaahar.ui.theme.Clay
import com.pashuaahar.ui.theme.Leaf
import com.pashuaahar.ui.theme.LeafDark
import com.pashuaahar.ui.theme.Sky
import com.pashuaahar.ui.theme.Wheat

private val steps = listOf("Profile", "Yield", "Costs", "Recipe")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PashuAaharApp() {
    var step by remember { mutableIntStateOf(0) }
    var breed by remember { mutableStateOf(CowBreed.Jersey) }
    var ageMonths by remember { mutableIntStateOf(48) }
    var weightKg by remember { mutableStateOf(380.0) }
    var currentYield by remember { mutableStateOf(8.0) }
    var targetYield by remember { mutableStateOf(10.0) }
    var maizePrice by remember { mutableStateOf(24.0) }
    var cakePrice by remember { mutableStateOf(36.0) }
    var branPrice by remember { mutableStateOf(22.0) }
    var mineralPrice by remember { mutableStateOf(80.0) }
    var marketPrice by remember { mutableStateOf(42.0) }

    val profile = CowProfile(
        breed = breed,
        ageMonths = ageMonths,
        weightKg = weightKg,
        currentMilkYieldLiters = currentYield,
        targetMilkYieldLiters = targetYield
    )
    val prices = FeedPrices(
        maizePerKg = maizePrice,
        cottonseedCakePerKg = cakePrice,
        wheatBranPerKg = branPrice,
        mineralMixPerKg = mineralPrice,
        marketFeedPerKg = marketPrice
    )
    val recipe = NutritionEngine.buildRecipe(profile, prices)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pashu-Aahar",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            StepActions(
                step = step,
                onPrevious = { step = (step - 1).coerceAtLeast(0) },
                onNext = { step = (step + 1).coerceAtMost(steps.lastIndex) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            StepperHeader(step = step)
            Spacer(Modifier.height(16.dp))
            when (step) {
                0 -> ProfileStep(
                    breed = breed,
                    ageMonths = ageMonths,
                    weightKg = weightKg,
                    onBreedChange = { breed = it },
                    onAgeChange = { ageMonths = it },
                    onWeightChange = { weightKg = it }
                )

                1 -> YieldStep(
                    currentYield = currentYield,
                    targetYield = targetYield,
                    recipe = recipe,
                    onCurrentYieldChange = { currentYield = it },
                    onTargetYieldChange = { targetYield = it }
                )

                2 -> CostStep(
                    prices = prices,
                    onMaizeChange = { maizePrice = it },
                    onCakeChange = { cakePrice = it },
                    onBranChange = { branPrice = it },
                    onMineralChange = { mineralPrice = it },
                    onMarketChange = { marketPrice = it },
                    recipe = recipe
                )

                else -> RecipeStep(recipe = recipe)
            }
            Spacer(Modifier.height(92.dp))
        }
    }
}

@Composable
private fun StepperHeader(step: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, label ->
                StepBadge(label = label, selected = index == step, complete = index < step)
            }
        }
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { (step + 1) / steps.size.toFloat() },
            modifier = Modifier.fillMaxWidth(),
            color = Leaf,
            trackColor = Color(0xFFE1E7E1)
        )
    }
}

@Composable
private fun StepBadge(label: String, selected: Boolean, complete: Boolean) {
    val color = when {
        selected -> Leaf
        complete -> Sky
        else -> Color(0xFFD9DED7)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier.size(34.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (complete) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                } else {
                    Text(label.take(1), color = if (selected) Color.White else LeafDark)
                }
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(72.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProfileStep(
    breed: CowBreed,
    ageMonths: Int,
    weightKg: Double,
    onBreedChange: (CowBreed) -> Unit,
    onAgeChange: (Int) -> Unit,
    onWeightChange: (Double) -> Unit
) {
    SectionTitle("Cow profile", "Breed, age, and weight tune the feed base.")
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CowBreed.entries.forEach { option ->
            FilterChip(
                selected = breed == option,
                onClick = { onBreedChange(option) },
                label = { Text(option.displayName) },
                leadingIcon = {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            )
        }
    }
    Spacer(Modifier.height(18.dp))
    NumericStepper(
        label = "Age",
        value = ageMonths.toDouble(),
        suffix = "months",
        step = 6.0,
        range = 12.0..180.0,
        onChange = { onAgeChange(it.toInt()) }
    )
    NumericStepper(
        label = "Weight",
        value = weightKg,
        suffix = "kg",
        step = 10.0,
        range = 150.0..750.0,
        onChange = onWeightChange
    )
}

@Composable
private fun YieldStep(
    currentYield: Double,
    targetYield: Double,
    recipe: FeedRecipe,
    onCurrentYieldChange: (Double) -> Unit,
    onTargetYieldChange: (Double) -> Unit
) {
    SectionTitle("Milk yield target", "Changing the target updates the recipe instantly.")
    SliderMetric(
        label = "Current yield",
        value = currentYield,
        range = 1.0f..25.0f,
        suffix = "L/day",
        onChange = onCurrentYieldChange
    )
    SliderMetric(
        label = "Target yield",
        value = targetYield,
        range = 1.0f..30.0f,
        suffix = "L/day",
        onChange = onTargetYieldChange
    )
    SummaryStrip(recipe = recipe)
}

@Composable
private fun CostStep(
    prices: FeedPrices,
    onMaizeChange: (Double) -> Unit,
    onCakeChange: (Double) -> Unit,
    onBranChange: (Double) -> Unit,
    onMineralChange: (Double) -> Unit,
    onMarketChange: (Double) -> Unit,
    recipe: FeedRecipe
) {
    SectionTitle("Feed costs", "Local prices decide the saving estimate.")
    NumericStepper("Maize", prices.maizePerKg, "Rs/kg", 1.0, 10.0..80.0, onMaizeChange)
    NumericStepper("Cottonseed cake", prices.cottonseedCakePerKg, "Rs/kg", 1.0, 15.0..100.0, onCakeChange)
    NumericStepper("Wheat bran", prices.wheatBranPerKg, "Rs/kg", 1.0, 10.0..80.0, onBranChange)
    NumericStepper("Mineral mix", prices.mineralMixPerKg, "Rs/kg", 5.0, 30.0..160.0, onMineralChange)
    NumericStepper("Market feed", prices.marketFeedPerKg, "Rs/kg", 1.0, 20.0..120.0, onMarketChange)
    SavingsChart(recipe = recipe)
}

@Composable
private fun RecipeStep(recipe: FeedRecipe) {
    SectionTitle("Balanced recipe", "Daily concentrate mix with roughage target.")
    MetricGrid(recipe = recipe)
    Spacer(Modifier.height(14.dp))
    recipe.ingredients.forEach { ingredient ->
        IngredientCard(ingredient = ingredient)
    }
    RoughageCard(roughageKg = recipe.roughageKg)
    NutritionBalance(recipe = recipe)
    Text(
        text = "Veterinary tips",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)
    )
    VeterinaryTipsRepository.tips.forEach { TipCard(it) }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Text(
        subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF4E5B53),
        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
    )
}

@Composable
private fun NumericStepper(
    label: String,
    value: Double,
    suffix: String,
    step: Double,
    range: ClosedFloatingPointRange<Double>,
    onChange: (Double) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.SemiBold)
                Text("${format(value)} $suffix", color = LeafDark)
            }
            IconButton(onClick = { onChange((value - step).coerceAtLeast(range.start)) }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease $label")
            }
            IconButton(onClick = { onChange((value + step).coerceAtMost(range.endInclusive)) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase $label")
            }
        }
    }
}

@Composable
private fun SliderMetric(
    label: String,
    value: Double,
    range: ClosedFloatingPointRange<Float>,
    suffix: String,
    onChange: (Double) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, fontWeight = FontWeight.SemiBold)
                Text("${format(value)} $suffix", color = LeafDark, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = value.toFloat(),
                onValueChange = { onChange(kotlin.math.round(it.toDouble() * 10.0) / 10.0) },
                valueRange = range
            )
        }
    }
}

@Composable
private fun SummaryStrip(recipe: FeedRecipe) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SmallMetric("Mix", "${format(recipe.totalConcentrateKg)} kg", Modifier.weight(1f))
        SmallMetric("Save", "Rs ${format(recipe.dailySavings)}", Modifier.weight(1f))
    }
}

@Composable
private fun MetricGrid(recipe: FeedRecipe) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SmallMetric("Home cost", "Rs ${format(recipe.dailyHomeCost)}", Modifier.weight(1f))
        SmallMetric("Market", "Rs ${format(recipe.dailyMarketCost)}", Modifier.weight(1f))
    }
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SmallMetric("Daily save", "Rs ${format(recipe.dailySavings)}", Modifier.weight(1f))
        SmallMetric("Monthly", "Rs ${format(recipe.monthlySavings)}", Modifier.weight(1f))
    }
}

@Composable
private fun SmallMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(88.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color(0xFF5D665F))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun IngredientCard(ingredient: FeedIngredient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FodderGlyph(iconName = ingredient.iconName, modifier = Modifier.size(48.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(ingredient.name, fontWeight = FontWeight.Bold)
                Text(
                    "${format(ingredient.kg)} kg at Rs ${format(ingredient.pricePerKg)}/kg",
                    color = Color(0xFF4E5B53)
                )
            }
            Text(
                "Rs ${format(ingredient.dailyCost)}",
                fontWeight = FontWeight.Bold,
                color = LeafDark
            )
        }
    }
}

@Composable
private fun RoughageCard(roughageKg: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3EC)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FodderGlyph(iconName = "roughage", modifier = Modifier.size(48.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Green fodder and dry roughage", fontWeight = FontWeight.Bold)
                Text("${format(roughageKg)} kg dry matter equivalent")
            }
        }
    }
}

@Composable
private fun NutritionBalance(recipe: FeedRecipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Nutrition balance", fontWeight = FontWeight.Bold)
            BalanceRow("Protein", recipe.proteinGrams, recipe.proteinTargetGrams, "g")
            BalanceRow("Energy", recipe.energyMcal, recipe.energyTargetMcal, "Mcal")
        }
    }
}

@Composable
private fun BalanceRow(label: String, value: Double, target: Double, unit: String) {
    val progress = (value / target).toFloat().coerceIn(0f, 1f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.width(76.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            color = if (progress > 0.85f) Leaf else Wheat,
            trackColor = Color(0xFFE1E7E1)
        )
        Text(
            "${format(value)} / ${format(target)} $unit",
            modifier = Modifier.width(122.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun SavingsChart(recipe: FeedRecipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Leaf)
                Spacer(Modifier.width(8.dp))
                Text("Cost savings", fontWeight = FontWeight.Bold)
            }
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(top = 10.dp),
                factory = { context ->
                    BarChart(context).apply {
                        description.isEnabled = false
                        axisRight.isEnabled = false
                        legend.isEnabled = true
                        setFitBars(true)
                    }
                },
                update = { chart ->
                    val labels = listOf("Home", "Market", "Saving")
                    val entries = listOf(
                        BarEntry(0f, recipe.dailyHomeCost.toFloat()),
                        BarEntry(1f, recipe.dailyMarketCost.toFloat()),
                        BarEntry(2f, recipe.dailySavings.coerceAtLeast(0.0).toFloat())
                    )
                    val dataSet = BarDataSet(entries, "Rs/day").apply {
                        setColors(Leaf.toArgb(), Clay.toArgb(), Sky.toArgb())
                        valueTextColor = AndroidColor.rgb(24, 32, 29)
                        valueTextSize = 12f
                    }
                    chart.data = BarData(dataSet).apply {
                        barWidth = 0.55f
                    }
                    chart.xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(labels)
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        setDrawGridLines(false)
                    }
                    chart.axisLeft.axisMinimum = 0f
                    chart.invalidate()
                }
            )
        }
    }
}

@Composable
private fun TipCard(tip: VeterinaryTip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = Color(0xFFEAF3EC), modifier = Modifier.size(46.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Leaf)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(tip.title, fontWeight = FontWeight.Bold)
                Text(tip.description, color = Color(0xFF4E5B53))
            }
            AssistChip(
                onClick = {},
                label = { Text(tip.duration) },
                leadingIcon = {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun FodderGlyph(iconName: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val bg = when (iconName) {
            "grain" -> Wheat
            "cake" -> Clay
            "bran" -> Sky
            "mineral" -> Color(0xFF8E7CC3)
            else -> Leaf
        }
        drawCircle(color = bg.copy(alpha = 0.18f), radius = size.minDimension / 2f)
        when (iconName) {
            "grain" -> {
                repeat(4) { index ->
                    drawOval(
                        color = Wheat,
                        topLeft = Offset(size.width * (0.24f + index * 0.12f), size.height * 0.26f),
                        size = Size(size.width * 0.16f, size.height * 0.34f)
                    )
                }
                drawLine(
                    color = LeafDark,
                    start = Offset(size.width * 0.5f, size.height * 0.24f),
                    end = Offset(size.width * 0.5f, size.height * 0.78f),
                    strokeWidth = 4f
                )
            }

            "cake" -> {
                drawRoundRect(
                    color = Clay,
                    topLeft = Offset(size.width * 0.22f, size.height * 0.38f),
                    size = Size(size.width * 0.56f, size.height * 0.34f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
                drawCircle(color = Wheat, radius = 4f, center = Offset(size.width * 0.38f, size.height * 0.48f))
                drawCircle(color = Wheat, radius = 4f, center = Offset(size.width * 0.60f, size.height * 0.60f))
            }

            "bran" -> {
                drawArc(
                    color = Sky,
                    startAngle = 15f,
                    sweepAngle = 150f,
                    useCenter = false,
                    topLeft = Offset(size.width * 0.18f, size.height * 0.30f),
                    size = Size(size.width * 0.64f, size.height * 0.50f),
                    style = Stroke(width = 6f)
                )
                drawLine(Sky, Offset(size.width * 0.28f, size.height * 0.56f), Offset(size.width * 0.72f, size.height * 0.56f), 6f)
            }

            "mineral" -> {
                drawCircle(color = Color(0xFF8E7CC3), radius = size.minDimension * 0.18f, center = center)
                drawCircle(color = Color.White, radius = size.minDimension * 0.08f, center = center)
            }

            else -> {
                drawLine(
                    color = Leaf,
                    start = Offset(size.width * 0.50f, size.height * 0.75f),
                    end = Offset(size.width * 0.50f, size.height * 0.28f),
                    strokeWidth = 5f
                )
                drawOval(
                    color = Leaf,
                    topLeft = Offset(size.width * 0.22f, size.height * 0.32f),
                    size = Size(size.width * 0.34f, size.height * 0.20f)
                )
                drawOval(
                    color = Leaf,
                    topLeft = Offset(size.width * 0.48f, size.height * 0.42f),
                    size = Size(size.width * 0.34f, size.height * 0.20f)
                )
            }
        }
    }
}

@Composable
private fun StepActions(step: Int, onPrevious: () -> Unit, onNext: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPrevious,
                enabled = step > 0,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }
            Button(
                onClick = onNext,
                enabled = step < steps.lastIndex,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (step == steps.lastIndex) "Ready" else "Next")
            }
        }
    }
}

private fun format(value: Double): String {
    val rounded = kotlin.math.round(value * 10.0) / 10.0
    return if (rounded % 1.0 == 0.0) rounded.toInt().toString() else rounded.toString()
}
