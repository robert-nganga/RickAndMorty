# RickAndMorty
A mobile application built with Kotlin and MVVM architecture pattern that displays character details from the popular animated television show, Rick and Morty. This app uses the Rick and Morty API to retrieve character data and follows the Android recommended best practices.

## Features
   - Display a list of all Rick and Morty characters
   - View detailed information about each character including their name, species, status, and origin
   - Offline support with remote mediator and caching

## Techstacks
The app is built with popular Jetpack libraries including:
   * [Paging 3](https://developer.android.com/topic/libraries/architecture/paging) Paging is a part of Android Jetpack and a data library that provides a simple and efficient way to load data, especially when dealing with large datasets. It helps to load data gradually and gracefully within our app's UI.
   * [Room](https://developer.android.com/topic/libraries/architecture/room) Room is a part of the Android Jetpack and a persistence library that provides an abstract layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
   * [Navigation Component](https://developer.android.com/guide/navigation) Navigation Component is a part of Android Jetpack and a UI toolkit for navigating between screens within an app. It provides a simple, consistent and flexible way to manage navigation in the app.
   * [Retrofit](https://square.github.io/retrofit/) Retrofit is a popular networking library for Android that makes it easy to consume RESTful APIs.
   * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)  Coroutines are a Kotlin feature that makes it easier to write asynchronous, non-blocking code.
   * [Hilt](https://dagger.dev/hilt/) Hilt is a dependency injection library for Android that provides a standard way to manage dependencies in an Android app.
   * [Glide](https://bumptech.github.io/glide/) Glide is a popular image loading library for Android that makes it easy to load and display images in the app.
   * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) The ViewModel library is part of the Android Jetpack and a UI component that is responsible for preparing and managing the data for an Activity or a Fragment.
   * [Remote Mediator](https://developer.android.com/topic/libraries/architecture/paging/v3-remote-mediator)  The Remote Mediator library is used for offline caching of data in the app. It provides a simple way to cache data from the API to the device, making the app more efficient and user-friendly, even when there's no internet connection.
   * [Gson](https://github.com/google/gson) A Java library that can be used to convert Java Objects into their JSON representation.
   * [Data binding](https://developer.android.com/topic/libraries/data-binding) The Data Binding Library is a support library that allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically.

## Usage
To use this app, simply clone or download the repository, open the project in Android Studio and run the app on an emulator or physical device. The app will use the Rick and Morty API to retrieve character data and display it to the user, or you can download the apk file attached.

## Screenshots
 ![](screenshots/screenshot0.png)
 ![](screenshots/screenshot5.png)
