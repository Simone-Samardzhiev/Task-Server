# Task-Server

## Description
The project is a simple http server made in Ktor,
which is a Kotlin framework for creating https servers.

## Installation 
To run the code you can clone the repository
and used gradle to build the project. The server will start 
listening at `http://localhost:8080`

## Api overview
### End points
- **POST /register**: Register a new user.
- **GET /tasks**: Retrieve all tasks.
- **POST /tasks/addTask**: Add a new task.
- **POST /tasks/updateTask** Update an existing task.
- **DELETE /tasks/removeTask** Remove an existing task.
