# Kitchen Diary (Cooked Diary Recipe Organizer)

> **A personal recipe collection and cooking companion mobile application built for Android using Jetpack Compose, Kotlin, and Material 3.**

---

## 📋 Project Part 3 - Initial Release Submission Summary

- **Student Name:** Juliamay Nina Estrada
- **Course & Year:** BSIT – 4
- **Application Title:** Kitchen Diary (Cooked Diary Recipe Organizer)
- **GitHub Repository Link:** [https://github.com/iamjuliamaye/KitchenDiary](https://github.com/iamjuliamaye/KitchenDiary)

---

### 1. Implemented Features (Over 100% of Proposal Scope)
- **Add, Edit, and Delete Recipe**: Full recipe lifecycle with deletion confirmation.
- **Scrollable Recipe List & Full Detail View**: Material 3 cards with emoji badges and step-by-step layout.
- **Real-time Title Search & Category Filter Chips**: Instant search bar and quick category chips (`Breakfast`, `Main Course`, `Dessert`, `Snack`).
- **Favorites Toggle & Dedicated Favorites Screen**: Heart icon toggle saving to a dedicated screen.
- **Local Storage Persistence**: Saved recipes and photos persist permanently on device storage across app restarts and device reboots.
- **Recipe Photo Picker**: Select gallery photos automatically copied to  internal storage.
- **Serving Size Adjuster**: Interactive `1x`, `2x`, `3x`, `4x` ingredient quantity scaling.
- **Cooking Timer**: Embedded timer with `+1m`, `+5m`, `+10m` presets, play/pause, reset, and completion alert.
- **Sorting Options**: Sort recipes by `A–Z`, `Z–A`, `Newest`, `Oldest`, or `Category`.
- **Dark / Light Theme Toggle**: Sun/Moon header icon switching between light pastel and dark cozy themes.
- **Instant Snackbar Pop-up Feedback**: Immediate pop-up feedback for user actions.

---

### 2. AI Usage Documentation
## AI Usage Disclaimer & Academic Integrity Documentation

> **Statement of AI Tool Usage**

### 1. Extent of AI Tool Usage
In compliance with academic integrity guidelines, Generative AI tools (such as Android Studio AI / Gemini / Built in AI Windsurf) were used as an **interactive technical assistant and coding companion** during the development of Kitchen Diary.

### 2. Specific Scope of AI Assistance
- **UI & Layout Styling**: Assistance with declarative Jetpack Compose layout structures.
- **Debugging & Edge-Case Resolution**: Troubleshooting dark mode text contrast ratios, layout weight constraints in header rows, and Uri permission lifetimes across activity lifecycles.

### 3. Student Ownership & Verification Statement
All AI-suggested code snippets, architectural patterns, and logic were thoroughly reviewed, edited, tested, and integrated by the student. 

---

## Background & Problem Statement

Many people collect recipes from multiple disconnected sources, a photo of a handwritten family recipe, a screenshot from social media, a link sent on Messenger, or a page folded in a printed cookbook. Over time, these saved items become scattered and impossible to find quickly when cooking.

**Kitchen Diary** solves this problem by providing a central, private, and offline-friendly digital organizer. Users can input, categorize, search, scale, and cook their favorite dishes with zero friction. Because all recipe data and attached photos are stored locally on the device, the app remains fully functional without an active internet connection—making it an essential tool inside the kitchen.

---

## Target Audience

1. **Home Cooks**: Individuals who regularly collect and cook recipes from multiple sources.
2. **Independent Students & Young Adults**: Beginners building their personal recipe collection from scratch.
3. **Family Cooks**: Users preserving traditional heirloom recipes passed down through generations.
4. **Offline Kitchen Users**: Cooks who need instant access to step-by-step instructions without relying on Wi-Fi or cellular data.

---

## Detailed Features Breakdown

### Core Features
1. **Add Recipe**:
   - Intuitive creation form for title, category selection, multiline ingredient entry, and step-by-step cooking instructions.
   - Form validation ensures a title is entered before saving.
2. **Edit Recipe**:
   - Allows modifying any existing recipe details with pre-filled fields.
3. **Delete Recipe with Confirmation**:
   - Permanently removes a recipe after user confirmation via a cute `AlertDialog` to prevent accidental deletion.
4. **Recipe List View (Home Screen)**:
   - Displays all saved recipes in a clean, scrollable list (`LazyColumn`) using customized `RecipeCard` components showing the title, category pill, category emoji, and favorite status at a glance.
5. **Recipe Detail View**:
   - Clean, readable view featuring a full-width header card/photo banner, bulleted ingredients, and numbered step badges (`1`, `2`, `3`...).
6. **Real-Time Search**:
   - Search bar that filters recipes dynamically as the user types.
7. **Favorites System**:
   - Toggle heart icon on any recipe to save it to a dedicated **Favorites Screen** for quick access to frequently cooked dishes.

---

### Advanced & Optional Features
8. **Recipe Photo Picker & Local Image Storage**:
   - Users can attach photos from their device gallery.
   - **Internal Storage Copying**: Automatically copies selected images to `context.filesDir` to guarantee that photos persist permanently across app restarts.
   - **Fallback Badge**: Automatically falls back to cute category emoji badges if no photo is attached.
9. **Serving Size Adjuster**:
   - Interactive multiplier chips (`1x`, `2x`, `3x`, `4x`) on the detail view.
   - Uses regex logic to dynamically scale ingredient quantities (integers, decimals, and fractions like `1/2 cup` → `1 cup`, `200g` → `400g`) in real time.
10. **Interactive Cooking Timer**:
    - Embedded countdown timer with `+1m`, `+5m`, and `+10m` quick add buttons, Play/Pause toggle, Reset button, live `MM:SS` display, and completion alert.
11. **Multi-Option Sorting System**:
    - Dropdown menu supporting sorting by `A–Z` (Title Alphabetical), `Z–A` (Reverse Alphabetical), `Newest` (Date Added), `Oldest`, or `Category`.
12. **Dark & Light Theme System**:
    - Custom pastel light theme (`CuteColors`) and cozy dark slate/rose theme (`DarkCuteColors`).
    - Dedicated Sun/Moon header button for instant theme switching with optimized text contrast across all cards and input fields.
13. **Instant Snackbar Feedback**:
    - Pop-up confirmation snackbars notify users immediately whenever a recipe is added, updated, deleted, or favorited.

---

