# Practical 4 – Chinook Database Application

A JavaFX desktop application that connects to the Chinook music store database (MySQL) via JDBC. Provides a GUI with tabs for viewing employees, managing tracks, customer CRUD, reporting, and customer recommendations.

## Prerequisites

- **JDK 21+** — [download](https://jdk.java.net/)
- **Maven** — [download](https://maven.apache.org/download.cgi) (or use IntelliJ's built-in Maven)
- **MySQL / MariaDB Server** running on `localhost:3306`
- **MySQL Workbench** (optional, for importing the database)

## Step 1: Import the Database

1. Download `Chinook_MySql.sql` from [chinook-database releases](https://github.com/lerocha/chinook-database/releases).
2. Open MySQL Workbench, connect to your local server.
3. Run the SQL script to create the database.
4. Rename or create the database to match your student number:
   ```sql
   CREATE DATABASE u25056809_chinook;
   ```
5. Verify:
   ```sql
   USE u25056809_chinook;
   SHOW TABLES;
   ```
   You should see 11 tables (Album, Artist, Customer, Employee, Genre, Invoice, InvoiceLine, MediaType, Playlist, PlaylistTrack, Track).

## Step 2: Configure Database Connection

The application reads connection details from environment variables. Two options:

### Option A: `.env` file (recommended)

Create a `.env` file in the `practical-4/` directory:

```
CHINOOK_DB_PROTO=jdbc:mysql
CHINOOK_DB_HOST=127.0.0.1
CHINOOK_DB_PORT=3306
CHINOOK_DB_NAME=u25056809_chinook
CHINOOK_DB_USERNAME=root
CHINOOK_DB_PASSWORD=yourpassword
```

This file is gitignored and won't be committed.

### Option B: System environment variables

```bash
export CHINOOK_DB_PROTO=jdbc:mysql
export CHINOOK_DB_HOST=127.0.0.1
export CHINOOK_DB_PORT=3306
export CHINOOK_DB_NAME=u25056809_chinook
export CHINOOK_DB_USERNAME=root
export CHINOOK_DB_PASSWORD=yourpassword
```

In IntelliJ: **Run → Edit Configurations → Environment Variables**.

> If neither is set, defaults to `jdbc:mysql://127.0.0.1:3306/chinook` with user `root` and no password.

## Step 3: Build

**Command line** (from `practical-4/`):
```bash
mvn clean compile
```

**IntelliJ**: Open the folder as a Maven project — dependencies are downloaded automatically.

## Step 4: Run

**IntelliJ**: Run `Runner.java` (`org.example.Runner`).

**Command line**:
```bash
mvn compile exec:java -Dexec.mainClass="org.example.Runner"
```

The GUI opens at 1000×700 with tabs for Employees, Tracks, Reports, Customer CRUD, Inactive Customers, and Customer Recommendations.

> `Runner.main()` delegates to `Main.main()` to avoid JavaFX module issues with Maven.

## Project Structure

```
practical-4/
├── .env                              # Local DB credentials (not committed)
├── pom.xml                           # Maven config and dependencies
└── src/main/java/org/example/
    ├── Runner.java                   # Entry point
    ├── Main.java                     # JavaFX Application launcher
    ├── controller/
    │   ├── MainController.java       # Main window logic (all tabs)
    │   └── AddTrackController.java   # Add-track popup logic
    ├── dao/                          # Data Access Objects
    │   ├── AlbumDAO.java
    │   ├── ArtistDAO.java
    │   ├── CustomerDAO.java          # + inactive customer query
    │   ├── EmployeeDAO.java          # + self-join for supervisor
    │   ├── GenreDAO.java             # + revenue report
    │   ├── InvoiceDAO.java           # + customer insights
    │   ├── InvoiceLineDAO.java
    │   ├── MediaTypeDAO.java
    │   ├── PlaylistDAO.java
    │   ├── PlaylistTrackDAO.java
    │   └── TrackDAO.java             # + recommendations
    ├── model/                        # Data models
    │   ├── Person.java, Employee.java, Customer.java,
    │   ├── Album.java, Artist.java, Genre.java, GenreRevenue.java,
    │   ├── Invoice.java, InvoiceLine.java, MediaType.java,
    │   └── Playlist.java, PlaylistTrack.java, Track.java
    └── util/
        └── DatabaseManager.java      # Reads .env / env vars, returns Connection
```

## Dependencies (Maven)

| Dependency | Version |
|---|---|
| `com.mysql:mysql-connector-j` | 9.7.0 |
| `org.openjfx:javafx-controls` | 21.0.5 |
| `org.openjfx:javafx-fxml` | 21.0.5 |

## Troubleshooting

- **"Access denied for user"** — Check username/password in `.env`.
- **Tables empty in GUI** — Confirm MySQL is running and the database name in `.env` matches exactly.
- **JavaFX warnings on startup** — Cosmetic warnings from newer JDK versions; doesn't affect functionality.
- **"Communications link failure"** — MySQL server isn't running. Start it with `brew services start mysql` (macOS) or `sudo systemctl start mysql` (Linux).
