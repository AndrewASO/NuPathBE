# NuPath Backend

## Author
Andrew Skevington-Olivera

## Introduction

NuPath is a comprehensive backend system designed to facilitate seamless interaction between the frontend application and the backend server. It provides a robust platform for managing user profiles, forum interactions, class selections, and more, ensuring a dynamic and responsive user experience. This document outlines the architecture, setup, and key functionalities of the NuPath backend.

## Features

- User authentication and registration
- User profile management, including preferences and personal information
- Dynamic leaderboard updates based on user activities
- Forum for user interactions, including message posting and retrieval
- Management of class and dormitory selections
- Handling of user preferences for faculty and facilities


## Technologies and Tools

This project is built using several key technologies and tools, each chosen for their reliability, performance, and suitability for the project's requirements:

- **Java**: The primary programming language for backend development, selected for its object-oriented features, platform independence, and extensive support for web applications.

- **MongoDB**: A NoSQL database used for storing, retrieving, and managing application data. MongoDB's flexible document data model allows for the efficient handling of BSON documents, facilitating dynamic queries and data aggregation.

- **BSON documents**: The binary representation format used in MongoDB for storing documents. BSON enables the fast serialization and deserialization of data, supporting rich data types and embedded documents.

- **Maven**: A project management and build automation tool used for managing the project's lifecycle, dependencies, and builds. Maven simplifies the inclusion of libraries and frameworks, ensuring a consistent build environment.

- **com.sun.net.httpserver**: A lightweight HTTP server API provided with the Java Development Kit (JDK) for creating HTTP server applications. This API is utilized for handling all web server functionalities, including processing HTTP requests and generating responses.

These technologies combine to form the backbone of the project, ensuring a robust and scalable backend infrastructure capable of handling the application's requirements.

## Project Structure

Brief descriptions of key components:

- **Webserver.java**: Initializes and manages the HTTP server, serving as the interaction point between frontend and backend.
- **UserList.java**: Manages user data, including authentication, registration, and profile updates.
- **Classes.java**, **Dorm.java**, **Faculty.java**, **Facilities.java**: Handle user selections and preferences for classes, dormitories, faculty, and facilities.
- **Forums.java**: Manages forum interactions, allowing users to post and retrieve messages.
- **LeaderBoard.java**: Updates and manages the leaderboard based on user activities.
- **GetDbCollection.java**: Provides utility functions for accessing and updating MongoDB collections.

## Setup and Running

### Prerequisites

- Java JDK 16.0.2
- MongoDB 3.12.12
- Maven 

### Installation

1. Clone the repository: `git clone (https://github.com/AndrewASO/NuPathBE)`.
2. Navigate to the project directory: `cd src/main/java/com/example`.
3. Build the project with Maven: `mvn clean install`.

### Running the Project

```bash
java -jar NuPathFixed.jar
