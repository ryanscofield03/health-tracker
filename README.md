# Health Tracker App

## Overview

An android health tracking application designed to help users monitor their workouts, eating, and sleeping. 
This project was developed for my own personal use (hence a lot of it is catered to myself rather than following standards),
but also to increase my experience with long-running projects and develop my mobile development + UI/UX skills.

## Features

### Track Workouts: 
Build workouts, run them, and log your data. Users are given a timer between sets to avoid underresting and wasting too much time resting. Data from the previous workout is provided to the user when working out so that they can ensure progress.

### Track Eating: 
Users can set nutrition goals (currently limited to macronutrients (protein, carbs, fats) + calories). They can then create meals each day and see how they are progressing throughout the day towards their daily goals.

### Track Sleeping: 
Users can track their sleep schedule and feeling when they wake up on the sleep screen. This provides the ability to input a sleep start and end time, and a rating of quality from 1-5.

### Progress Analytics: 
Users can visualise their data through graphs to see if they are sticking to their goals, and see how they are progressing. 

### Dark Mode Support: 
UI supports both light and dark mode.

## Technologies Used

Backend: Kotlin + some Java

UI: Jetpack Compose

Database: Uses Room and some sharedpreferences (all local to the device)

APIs: CalorieNinjas for fetching macronutrients of food - would possibly be a good idea to have a backup for this in the case that CalorieNinjas is not available 

## Installation

### Prerequisites

Android Studio

### Steps

Clone the repository:

```
git clone https://github.com/ryanscofield03/fitness-tracker.git
```

Navigate to the project directory:

```
cd fitness-tracker-app
```

Run the app:

```
./gradlew build
```

## Screenshots



## Next Steps: 
* The application is primarily used in portrait mode and landscape has been ignored for this reason. Extending the application to allow for landscape usage would be a nice addition.
* Settings page has been setup, however the changing of settings has no impact on the application. Allowing for language & measurement changes (metric vs. imperial) in the future would be useful.
* Extend tracking of food to include micronutrients, such as vitamen D or iron, which would be useful for those who are deficient or lack certain nutrients in their diet.

## License

This project is licensed under the Apache 2.0 License - see the LICENSE file for details.
