# Novely
A robust RESTful API for managing an online novel platform. It provides user registration/login, full CRUD operations for novels, chapters, comments, and ratings, as well as “favorite” and “super-favorite” features. The architecture follows a layered structure, consisting of Domain, Repository, Service, and Resource (Controller) layers. The API also follows best practices, using DTOs for data transfer, centralized exception handling, Docker, Swagger documentation, and much more.

## Technologies
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Features

👤 **User Management:** Register, login, profile updates and soft delete.

📖 **Novel Management:** Create, read, update and delete novels; track views, favorites, and super-favorites.

📑 **Chapter Management:** List, add, edit and delete.

💬 **Comments and Ratings:** Users can comment on and rate novels.

🔐 **Security:** Spring Security with OAuth2, JWT, and password hashing.

✔️ **Validation:** DTO-level validation to prevent mismatched inputs.

🐳 **Docker Support:**  Containerization enabled through the provided Dockerfile.

## Testing

🔗 **https://novely.onrender.com/swagger-ui/index.html#/**

Since the API is documented using Swagger, I highly recommend testing it through the link above. Alternatively, you can use a platform such as Postman with the following base URL: https://novely.onrender.com

## Key Endpoints
### Register User
**Method:** `POST`

**Endpoint:** `api/users/auth/signup`

**Body:** 
  ```json
    {
      "name": "Test",
      "email": "test@gmail.com",
      "password": "123456"
    }
  ```
---
### Login
**Method:** `POST`

**Endpoint:** `api/users/auth/login`

**Body:** 
   ```json
      {
        "email": "test@gmail.com",
        "password": "123456"
      }
   ```
---
### Create Novel (Authenticated)
**Method:** `POST`

**Endpoint:** `api/novels`

**Body:** 
  ```json
    {
      "title": "The testing tales",
      "description": "Once, a one-of-a-kind API was committed to GitHub...",
      "genre": "FANTASY"
    }
  ```
---
## Error Handling
Exception handling is centralized in the exception layer using a `ResourceExceptionHandler` to ensure global error management with clear responses and appropriate HTTP status codes.

## License
This project is licensed under the MIT License. *Suggestions are welcome!*

***

*Sometimes it is the people no one imagines anything of who do the things that no one can imagine -* **Alan Turing**


