# Social Media Blog API

This project implements a backend API for a hypothetical social media application using the Spring Boot framework. It manages user accounts and messages, providing endpoints for registration, login, message creation, retrieval, and deletion.

## Background

When building a full-stack application, we're typically concerned with both a front end (user interface) and a backend (data management). This project focuses on the backend, handling data persistence and business logic for a micro-blogging or messaging app. It leverages the Spring framework for dependency injection, data persistence, and API endpoint management.

## Database Tables

The following tables are initialized in the project's embedded database upon startup, using the provided SQL script:

### Account Table

```sql
Copy
CREATE TABLE Account (
    account_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);
Message Table
sql
Copy
CREATE TABLE Message (
    message_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    posted_by INTEGER,
    message_text VARCHAR(255),
    time_posted_epoch LONG,
    FOREIGN KEY (posted_by) REFERENCES Account(account_id)
);
```

1. User Registration
   
Endpoint: POST /register
Request Body: JSON representation of an Account (without account_id).

Success (200 OK):
Username is not blank.
Password is at least 4 characters long.
Username is unique.
Response body contains the created Account including the generated account_id.

Failure:
409 Conflict: Username already exists.
400 Bad Request: Missing username or short password.

2. User Login
   
Endpoint: POST /login
Request Body: JSON representation of an Account (username and password).

Success (200 OK):
Username and password match an existing account.
Response body contains the Account including account_id.

Failure:
401 Unauthorized: Invalid username or password.

3. Create Message
   
Endpoint: POST /messages
Request Body: JSON representation of a Message (without message_id).

Success (200 OK):
message_text is not blank and not over 255 characters.
posted_by refers to an existing user.
Response body contains the created Message including the generated message_id.

Failure:
400 Bad Request: Invalid message_text or posted_by.

4. Retrieve All Messages
   
Endpoint: GET /messages

Success (200 OK):
Returns a JSON list of all messages.
An empty list is returned if no messages exist.

5. Retrieve Message by ID
   
Endpoint: GET /messages/{message_id}

Success (200 OK):
Returns a JSON representation of the message with the given message_id.
An empty response is returned if no such message exists.

6. Delete Message
   
Endpoint: DELETE /messages/{message_id}

Success (200 OK):
Deletes the message with the given message_id.
Returns the number of rows updated (1 if the message existed).
If the message did not exist, the response body is empty (idempotent).

7. Update Message
   
Endpoint: PATCH /messages/{message_id}
Request Body: JSON with a new message_text.

Success (200 OK):
message_id exists.
message_text is not blank and not over 255 characters.
Response body contains the updated Message.

Failure:
400 Bad Request: Invalid message_text or message_id.

8. Retrieve Messages by User
   
Endpoint: GET /accounts/{account_id}/messages

Success (200 OK):
Returns a JSON list of all messages posted by the user with the given account_id.
An empty list is returned if no messages exist for that user.

Running the Application

bash
./mvnw spring-boot:run

Testing the Application
bash
./mvnw test

Do Not Modify:
Integration tests, model classes (Account and Message), and ConnectionUtil class.
The SQL script in src/main/resources.

Summary
This project implements a backend API for a social media app, allowing users to register, log in, and manage messages. It uses Spring Boot for dependency injection, data persistence, and API management. Follow the requirements and design patterns to ensure functionality and testability.
