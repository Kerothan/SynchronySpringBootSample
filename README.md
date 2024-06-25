Prerequisites
Java 17 or higher
Gradle

API Endpoints

POST /users: Register a User with basic information. Name, username, email, and password.

POST /upload: Upload an image for the logged in user.

GET /profile: Retrieve logged in user data including all basic info and the url for an uploaded profile picture.

DELETE /users: Deletes the logged in user.

Put /users: Edit the logged in user's profile info

Setup Instructions
1. Clone the repository
2. Navigate to the project root directory in your terminal or command prompt.
3. Build and run using ./gradlew bootRun to start the application on http://localhost:8080.
