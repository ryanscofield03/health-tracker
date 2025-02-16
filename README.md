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

### Reminders:
Workouts can be scheduled for specific days, and the user will be notified on those days.

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
git clone https://github.com/ryanscofield03/health-tracker.git
```

Navigate to the project directory:

```
cd fitness-tracker-app
```

Run the app:

```
./gradlew build
```

## Screenshots of Application
This is a set of screenshots displaying the core features and visuals of the app (currently graphs are empty of data).

<img src="https://github.com/user-attachments/assets/27bac894-9cf4-433c-afca-f3fc7969718a" alt="image" width="300">
<img src="https://github.com/user-attachments/assets/ecdef30e-870e-4728-8aaa-25756970ee6c" alt="image" width="300">

<img src="https://github.com/user-attachments/assets/aa9759e2-1442-4c9f-b494-22d62c636341" alt="image" width="300">
<img src="https://github.com/user-attachments/assets/ff7b4cf2-29a2-4273-b1d4-68505823abce" alt="image" width="300">

<img src="https://github.com/user-attachments/assets/3e544685-23b5-4021-a1f9-a796bb4ab9bc" alt="image" width="300">
<img src="https://github.com/user-attachments/assets/425c829d-a9a1-4f56-b213-14b9a1330973" alt="image" width="300">

<img src="https://github.com/user-attachments/assets/96b6850c-91be-4d67-a6ec-89c0676c4b50" alt="image" width="300">
<img src="https://github.com/user-attachments/assets/d712bae3-485a-4181-93aa-b34cb1415656" alt="image" width="300">

<img src="https://github.com/user-attachments/assets/7161902f-3311-462c-96ed-80af80dbdd1a" alt="image" width="300">


## Next Steps: 
* The application is primarily used in portrait mode and landscape has been ignored for this reason. Extending the application to allow for landscape usage would be a nice addition.
* Settings page has been setup, however the changing of settings has no impact on the application. Allowing for language & measurement changes (metric vs. imperial) in the future would be useful.
* Extend tracking of food to include micronutrients, such as vitamen D or iron, which would be useful for those who are deficient or lack certain nutrients in their diet.

## License

This project is licensed under the Apache 2.0 License - details in the LISENCE file.
