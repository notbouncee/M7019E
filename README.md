# M7019E Mobile Applications

Taken at Luleå University of Technology 🇸🇪 and mapped to IM3180 Design and Innovation Project 🇸🇬

# Topics

 • Kotlin
 • Android Studio

## Lab 1

- Learning Goals that you need to demonstrate:

    - Ability to create new screens.
    - Ability to add navigation.
    - Ability to open other apps from your app.

- Things added to app

    - A hardcoded database for the Movie details of the five movies we are currently using. 
    - A composable that shows movie genre(s).
    - A composable that shows a link to the movie homepage, which opens the URL in a web browser.
    - Created an IMDB URL that can open the IMDB page for the movie that opens on the IMDB app.

## Lab 2

- Learning Goals that you need to demonstrate:

    - Ability to use scrollable lists in your app, and show horizontal and vertical scrolling (vertical scrolling is already implemented as part of the lecture)
    - Ability to use different layouts and different display options
    - Ability to show you can get data from the internet via API calls.
    - Ability to open third-party apps.

- Things added to app

    - Converted the Movie List Screen to use Grid Layout with vertical scrolling
    - A list composable that has a horizontal scrollable layout to show reviews through API call for Movie Reviews.
    - A list composable that has a horizontal scrollable layout that shows trailers/videos through API call for Movie Videos. Opens the Youtube App if it is a Youtube link.

## Lab 3

- Learning Goals that you need to demonstrate:

    - Show the ability to use the room database in your app, the ability to add and delete entries from the database
    - Show how to cache data into the room database so that the list and MovieDetail can still work when there is no internet connection
    - Show the ability to load data automatically when the internet connection is back on. 
    - Show the ability to have background tasks

## Mini-Project

# QuickBite

- An App for recipes accessing the https://www.themealdb.com/ API. 

- Overview:

- QuickBite is a simple app where users can browse easy-to-make recipes, view the details including images, and save their favorites.

- Screens (Minimum 3):

1. Home Screen – Recipe List
 • Shows a list of recipes with:
 • Recipe name
 • Thumbnail image
 • Short description (e.g., “15-min pasta”)
 • Tap on any recipe to navigate to the Recipe Details screen.

UI Elements:
 • Scrollable list (LazyColumn/LazyGrid if using Jetpack Compose)
 • CardView or equivalent for each recipe item
 • Navigation on tap

2. Recipe Details Screen
 • Displays:
 • Full image of the dish
 • Ingredients list
 • Step-by-step cooking instructions
 • “Save to Favorites” button

Media Modality:
 • Image of the dish

UI Elements:
 • ImageView (or equivalent)
 • TextViews for ingredients and steps
 • Button for adding to favorites

3. Favorites Screen
 • Shows a list of recipes the user has saved
 • Tapping a recipe opens it in the Details screen again

UI Elements:
 • Similar list layout as Home
 • Possibly a trash icon or “Remove from Favorites” button
