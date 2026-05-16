# Pashu-Aahar

Native Android cattle nutrition calculator for small dairy farmers.

## Submission Form Details

### Project Title

Pashu-Aahar: Cattle Nutrition Calculator

### Short Description

Pashu-Aahar is an offline Android app that helps dairy farmers generate balanced cattle feed recipes using local grains, compare home-made feed cost with market feed, and follow simple veterinary hygiene and fodder-storage tips.

### Live URL

Use this GitHub Pages URL after enabling Pages for the `docs` folder:

https://sampathrmg21-debug.github.io/pashu-aaharpashu-aahar/

### GitHub URL

https://github.com/sampathrmg21-debug/pashu-aaharpashu-aahar

### Problem Statement

Small dairy farmers often buy costly branded cattle feed without knowing how to prepare scientifically balanced feed at home using locally available ingredients. This increases daily feeding cost and reduces profit. Pashu-Aahar solves this by calculating a feed recipe from cow breed, age, weight, current milk yield, and target milk yield, then showing the expected cost saving compared with market feed.

### Technologies Used

```text
Kotlin, Android, Jetpack Compose, Material 3, MPAndroidChart, JUnit, Offline Nutrition Formula Engine
```

### Research Paper Published

No.

### PRD Document

Upload the provided project report PDF:

```text
C:\Users\aravi\Downloads\Pashu_Aahar_Project_Report.pdf
```

## What It Does

Pashu-Aahar helps a farmer enter a cow profile, choose a milk yield target, and generate a low-cost home feed recipe using local ingredients such as maize, cottonseed cake, wheat bran, and mineral mix.

The app works offline after installation because the nutrition formulas and veterinary tips are bundled in the app code.

## Features

- Cow profile: breed, age, and weight
- Yield target planner with dynamic feed recommendations
- Home-made feed versus market feed cost comparison
- MPAndroidChart-powered cost savings chart
- Clear fodder icons and farmer-friendly cards
- Offline veterinary hygiene and fodder-storage tips
- Unit-tested nutrition calculation engine

## Open In Android Studio

1. Open this folder in Android Studio.
2. Let Gradle sync download Android and Kotlin dependencies.
3. Run the `app` configuration on an emulator or Android phone.

## Project Structure

- `app/src/main/java/com/pashuaahar/nutrition` - feed recommendation logic
- `app/src/main/java/com/pashuaahar/model` - domain models
- `app/src/main/java/com/pashuaahar/data` - bundled veterinary tips
- `app/src/main/java/com/pashuaahar/ui` - Jetpack Compose screens
- `app/src/test` - unit tests for formula behavior
