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

## Running it locally

You'll need: **Java 21**, **Maven**, **MySQL**, and **Python 3** installed.

### 1. Database

Create an empty MySQL database:

```sql
CREATE DATABASE linknet_db;
```

No need to create tables manually — Hibernate creates them automatically on first run (see `ddl-auto=update` below).

### 2. Backend config (not included in this repo — contains credentials)

Create `backend/src/main/resources/application.properties` with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/linknet_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update

file.upload-dir=./uploads
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

### 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

Runs on `http://localhost:8080`. Confirm it's up by visiting `http://localhost:8080/api/users` in a browser — should return `[]` on a fresh database.

### 4. Run the frontend

From the repo root (not inside `backend/`):

```bash
pip install streamlit requests
streamlit run app.py
```

Opens automatically at `http://localhost:8501`. The `.streamlit/config.toml` in this repo sets the color theme — no setup needed for that, Streamlit picks it up automatically as long as `app.py` and `.streamlit/` are in the same folder (they are, by default, in this repo).

### 5. Try it

Sign up a user, log in, create a post with an image, add a comment, then create a second account and try adding a friendship between the two.

## Known gaps (not yet built)

- No real authentication — login is session-based only
- Friendships are instant, no request/accept flow
