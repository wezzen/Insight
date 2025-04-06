[![Build status](https://github.com/wezzen/Insight/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/wezzen/Insight/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/wezzen/Insight/branch/master/graph/badge.svg?token=vXlQBfhkJY)](https://codecov.io/gh/wezzen/Insight)
[![Test Passed](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/wezzen/ce7d1abd067064169575beb1c1781f98/raw/3a77a079128fa0aec389a8bf4c22c174da79b1a0/Insight-junit-tests.json)](https://github.com/wezzen/Insight)

## Requirements:

To ensure proper operation of the application, you need to set up a PostgreSQL database and pass secrets and parameters through environment variables. The application uses the following environment variables for database connection:

**DB_HOST** — the database host address
**DB_NAME** — the name of the database
**DB_USER** — the database username
**DB_PASSWORD** — the database password

## Running the Container:

1. Set up and configure your PostgreSQL database.
2. Prepare the secrets for the database connection.
3. Run the container with the secrets passed as environment variables:
```
docker run \
  --name <container-name> \
  -p 8080:8080 \
  -e DB_HOST=<database host> \
  -e DB_NAME=<database name> \
  -e DB_USER=<database user> \
  -e DB_PASSWORD=<database password> \
  wezzen/insight-app:latest
```
