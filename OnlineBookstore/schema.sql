CREATE TABLE Users (
    UserID SERIAL PRIMARY KEY,
    Name VARCHAR(255),
    Username VARCHAR(255) UNIQUE,
    Password VARCHAR(255),
    Role VARCHAR(50)
);

CREATE TABLE Books (
    BookID SERIAL PRIMARY KEY,
    Title VARCHAR(255),
    Author VARCHAR(255),
    Genre VARCHAR(100),
    Price NUMERIC(10, 2),
    Quantity INT
);

CREATE TABLE Client_Books (
    ClientID INT REFERENCES Users(UserID),
    BookID INT REFERENCES Books(BookID),
    PRIMARY KEY (ClientID, BookID)
);

CREATE TABLE Requests (
    RequestID SERIAL PRIMARY KEY,
    BorrowerID INT REFERENCES Users(UserID),
    LenderID INT REFERENCES Users(UserID),
    BookID INT REFERENCES Books(BookID),
    Status VARCHAR(50)
);

CREATE TABLE Reviews (
    ReviewID SERIAL PRIMARY KEY,
    BookID INT REFERENCES Books(BookID),
    UserID INT REFERENCES Users(UserID),
    Rating INT,
    ReviewText TEXT,
    Timestamp TIMESTAMP
);

CREATE TABLE BookRecommendations (
    RecommendationID SERIAL PRIMARY KEY,
    UserID INT REFERENCES Users(UserID),
    BookID INT REFERENCES Books(BookID),
    RecommendationScore INT
);

CREATE TABLE RequestHistory (
    HistoryID SERIAL PRIMARY KEY,
    RequestID INT REFERENCES Requests(RequestID),
    UserID INT REFERENCES Users(UserID),
    OldStatus VARCHAR(50),
    NewStatus VARCHAR(50),
    Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
