# Movie List App

Movie Explorer is an Android application that allows users to explore trending, latest, and popular
movies. Additionally, users can search for movies, filter them based on various criteria, and sort
them according to their preferences. This app utilizes a custom networking library developed
specifically for handling API requests to [The Movie Database (TMDb)](https://www.themoviedb.org/)
API.

## Features

- View trending, latest, and popular movies.
- Search for movies by title.
- Filter movies based on criteria such as genre, release year, etc.
- Sort movies by popularity, rating, release date, etc.

## Setup

1. Clone the repository to your local machine:

   git clone https://github.com/yourusername/movie-explorer.git
2. Open the project in Android Studio.
3. Build and run the project on your Android device or emulator.


## Getting Started

To use the app, you need to obtain an API key from TMDb. Follow these steps:

1. Sign up for an account on [TMDb](https://www.themoviedb.org/signup).
2. Once logged in, go to your account settings and navigate to the API section.
3. Generate an API key.
4. Update the `API_KEY` variable in `Constants.kt` with your API key.
5. Optionally, if authentication is required, update the `AUTHORIZATION_CODE` variable
   in `Constants.kt` with the appropriate value.


Custom Networking Library

The app utilizes a custom networking library developed by Aman Jain specifically for handling API
requests using HTTPS. This library simplifies the process of making HTTP requests, handling
responses, and managing network operations.

    NetworkSDK - A custom networking library for making network requests using HTTPS.


Support

For any issues or support related to the custom networking library, please contact Aman Jain at amanjn38@gmail.com.

If you encounter any issues or have any questions or suggestions regarding the Movie Explorer app, feel free to open an issue. Pull requests are also welcome.