-- Schema VideoGames

DROP DATABASE IF EXISTS VideoGames_Test;
CREATE DATABASE VideoGames_Test;
USE VideoGames_Test;

-- Table DEVELOPER

CREATE TABLE IF NOT EXISTS DEVELOPER (
                                         DEVELOPER_ID INT NOT NULL AUTO_INCREMENT,
                                         NAME VARCHAR(100) NOT NULL,
    COUNTRY VARCHAR(50),
    FOUNDATION_YEAR INT,
    PRIMARY KEY (DEVELOPER_ID)
    );

-- Table PUBLISHER

CREATE TABLE IF NOT EXISTS PUBLISHER (
                                         PUBLISHER_ID INT NOT NULL AUTO_INCREMENT,
                                         NAME VARCHAR(100) NOT NULL,
    COUNTRY VARCHAR(50),
    DEVELOPER_ID INT UNIQUE,
    PRIMARY KEY (PUBLISHER_ID),
    CONSTRAINT FK_PUBLISHER_DEVELOPER
    FOREIGN KEY (DEVELOPER_ID)
    REFERENCES DEVELOPER (DEVELOPER_ID)
    );

-- Table GENRE

CREATE TABLE IF NOT EXISTS GENRE (
                                     GENRE_ID INT NOT NULL AUTO_INCREMENT,
                                     NAME VARCHAR(50) NOT NULL,
    DESCRIPTION VARCHAR(100),
    PRIMARY KEY (GENRE_ID)
    );

-- Table CONSOLE

CREATE TABLE IF NOT EXISTS CONSOLE (
                                       CONSOLE_ID INT NOT NULL AUTO_INCREMENT,
                                       NAME VARCHAR(100) NOT NULL,
    MANUFACTURER VARCHAR(100),
    RELEASE_YEAR INT,
    PRIMARY KEY (CONSOLE_ID)
    );

-- Table GAME

CREATE TABLE IF NOT EXISTS GAME (
                                    GAME_ID INT NOT NULL AUTO_INCREMENT,
                                    TITLE VARCHAR(100) NOT NULL,
    RELEASE_DATE DATE,
    DEVELOPER_ID INT NOT NULL,
    PUBLISHER_ID INT NOT NULL,
    PEGI_RATING VARCHAR(10),
    IS_MULTIPLAYER TINYINT,
    PRIMARY KEY (GAME_ID),
    CONSTRAINT FK_GAME_DEVELOPER
    FOREIGN KEY (DEVELOPER_ID)
    REFERENCES DEVELOPER (DEVELOPER_ID),
    CONSTRAINT FK_GAME_PUBLISHER
    FOREIGN KEY (PUBLISHER_ID)
    REFERENCES PUBLISHER (PUBLISHER_ID)
    );

-- Table GAME_EDITION (con PRICE modificado)

CREATE TABLE IF NOT EXISTS GAME_EDITION (
                                            GAME_ID INT NOT NULL,
                                            EDITION_NAME VARCHAR(100) NOT NULL,
    SPECIAL_CONTENT VARCHAR(100),
    PRICE DECIMAL(6,2),  -- Cambiado a 6 dígitos máximo
    PRIMARY KEY (GAME_ID),
    CONSTRAINT FK_GAME_EDITION_GAME
    FOREIGN KEY (GAME_ID)
    REFERENCES GAME (GAME_ID)
    );


-- Table GAME_GENRE

CREATE TABLE IF NOT EXISTS GAME_GENRE (
                                          GAME_ID INT NOT NULL,
                                          GENRE_ID INT NOT NULL,
                                          PRIMARY KEY (GAME_ID, GENRE_ID),
    CONSTRAINT FK_GAME_GENRE_GAME
    FOREIGN KEY (GAME_ID)
    REFERENCES GAME (GAME_ID),
    CONSTRAINT FK_GAME_GENRE_GENRE
    FOREIGN KEY (GENRE_ID)
    REFERENCES GENRE (GENRE_ID)
    );

-- Table GAME_CONSOLE

CREATE TABLE IF NOT EXISTS GAME_CONSOLE (
                                            GAME_ID INT NOT NULL,
                                            CONSOLE_ID INT NOT NULL,
                                            RELEASE_DATE DATE,
                                            IS_EXCLUSIVE TINYINT DEFAULT 0,
                                            RESOLUTION VARCHAR(20),
    PRIMARY KEY (GAME_ID, CONSOLE_ID),
    CONSTRAINT FK_GAME_CONSOLE_GAME
    FOREIGN KEY (GAME_ID)
    REFERENCES GAME (GAME_ID),
    CONSTRAINT FK_GAME_CONSOLE_CONSOLE
    FOREIGN KEY (CONSOLE_ID)
    REFERENCES CONSOLE (CONSOLE_ID)
    );