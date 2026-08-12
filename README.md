# LinkNet

A social media app built as a learning project — Spring Boot + MySQL backend, Streamlit frontend.

## Tech stack

- **Backend:** Java, Spring Boot, Spring Data JPA / Hibernate
- **Database:** MySQL
- **Frontend:** Python, Streamlit

## Features

- User signup and session-based login
- Posts with image/video upload (stored locally, served as static files)
- Comments on posts
- Friendships (add/remove) with duplicate and self-friend checks
- Friend-of-friend recommendations via a BFS graph traversal
- Profile page (view/edit bio, full name, password; view/edit/delete own posts)

## Running locally

**Backend:**
1. Create a MySQL database and update `src/main/resources/application.properties` with your own credentials (not committed — see below).
2. From the `backend` folder: `mvn spring-boot:run`
3. Runs on `http://localhost:8080`.

**Frontend:**
1. `pip install streamlit requests`
2. `streamlit run app.py`

## Notes on setup

`application.properties` isn't committed since it holds database credentials. Add your own locally with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/linknet_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
file.upload-dir=./uploads
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

## Known gaps (not yet built)

- No real authentication — login is session-based only, no password verification
- Friendships are instant, no request/accept flow
