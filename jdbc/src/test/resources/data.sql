-- Use the test database

USE VideoGames_Test;

-- Insert developers (DEVELOPER)

INSERT INTO DEVELOPER (NAME, COUNTRY, FOUNDATION_YEAR) VALUES
                                                           ('Naughty Dog', 'USA', 1984),
                                                           ('CD Projekt Red', 'Poland', 1994),
                                                           ('Nintendo EPD', 'Japan', 1889),
                                                           ('FromSoftware', 'Japan', 1986),
                                                           ('Rockstar North', 'UK', 1988),
                                                           ('Santa Monica Studio', 'USA', 1999),
                                                           ('Insomniac Games', 'USA', 1994),
                                                           ('Ubisoft Montreal', 'Canada', 1997),
                                                           ('Square Enix', 'Japan', 2003),
                                                           ('Capcom', 'Japan', 1979);

-- Insert publishers (PUBLISHER)

INSERT INTO PUBLISHER (NAME, COUNTRY, DEVELOPER_ID) VALUES
                                                        ('Sony Interactive Entertainment', 'USA', 1),
                                                        ('CD Projekt', 'Poland', 2),
                                                        ('Nintendo', 'Japan', 3),
                                                        ('Bandai Namco Entertainment', 'Japan', 4),
                                                        ('Rockstar Games', 'USA', 5),
                                                        ('Sony Interactive Entertainment', 'USA', 6),
                                                        ('Sony Interactive Entertainment', 'USA', 7),
                                                        ('Ubisoft', 'France', 8),
                                                        ('Square Enix', 'Japan', 9),
                                                        ('Capcom', 'Japan', 10);

-- Insert genres (GENRE)

INSERT INTO GENRE (NAME, DESCRIPTION) VALUES
                                          ('Action-Adventure', 'Combines action and adventure elements'),
                                          ('RPG', 'Role-playing game'),
                                          ('Horror', 'Horror games'),
                                          ('Open World', 'Open world exploration'),
                                          ('Platformer', 'Platform games'),
                                          ('FPS', 'First-person shooter'),
                                          ('Fighting', 'Fighting games'),
                                          ('Sports', 'Sports games'),
                                          ('Strategy', 'Strategy games'),
                                          ('Puzzle', 'Puzzle games');

-- Insert consoles (CONSOLE)

INSERT INTO CONSOLE (NAME, MANUFACTURER, RELEASE_YEAR) VALUES
                                                           ('PlayStation 5', 'Sony', 2020),
                                                           ('Xbox Series X', 'Microsoft', 2020),
                                                           ('Nintendo Switch', 'Nintendo', 2017),
                                                           ('PC', 'Various', NULL),
                                                           ('PlayStation 4', 'Sony', 2013),
                                                           ('Xbox One', 'Microsoft', 2013),
                                                           ('PlayStation 3', 'Sony', 2006),
                                                           ('Xbox 360', 'Microsoft', 2005),
                                                           ('Wii U', 'Nintendo', 2012),
                                                           ('PlayStation 2', 'Sony', 2000);

-- Insert games (GAME)

INSERT INTO GAME (TITLE, RELEASE_DATE, DEVELOPER_ID, PUBLISHER_ID, PEGI_RATING, IS_MULTIPLAYER) VALUES
                                                                                                    ('The Last of Us Part II', '2020-06-19', 1, 1, '18', 0),
                                                                                                    ('Cyberpunk 2077', '2020-12-10', 2, 2, '18', 0),
                                                                                                    ('The Legend of Zelda: Breath of the Wild', '2017-03-03', 3, 3, '12', 0),
                                                                                                    ('Elden Ring', '2022-02-25', 4, 4, '16', 1),
                                                                                                    ('Grand Theft Auto V', '2013-09-17', 5, 5, '18', 1),
                                                                                                    ('God of War: Ragnarök', '2022-11-09', 6, 6, '18', 0),
                                                                                                    ('Marvel''s Spider-Man: Miles Morales', '2020-11-12', 7, 7, '16', 0),
                                                                                                    ('Assassin''s Creed Valhalla', '2020-11-10', 8, 8, '18', 1),
                                                                                                    ('Final Fantasy VII Remake', '2020-04-10', 9, 9, '16', 0),
                                                                                                    ('Resident Evil Village', '2021-05-07', 10, 10, '18', 0);

-- Insert special editions (GAME_EDITION)

INSERT INTO GAME_EDITION (GAME_ID, EDITION_NAME, SPECIAL_CONTENT, PRICE) VALUES
                                                                             (1, 'Collector''s Edition', 'Ellie statue, concept art, pins', 199.99),
                                                                             (2, 'Day One Edition', 'Night City map, stickers, postcards', 59.99),
                                                                             (3, 'Master Edition', 'Link figurine, map, soundtrack', 129.99),
                                                                             (4, 'Premium Edition', 'Artbook, soundtrack, world map', 89.99),
                                                                             (5, 'Premium Online Edition', 'In-game money, exclusive garage', 79.99),
                                                                             (6, 'Jötnar Edition', 'Kratos and Atreus statues, watch', 249.99),
                                                                             (7, 'Ultimate Edition', 'Exclusive suits, concept art', 69.99),
                                                                             (8, 'Ultimate Edition', 'Season Pass, additional mission', 119.99),
                                                                             (9, 'Deluxe Edition', 'Soundtrack, concept art', 79.99),
                                                                             (10, 'Collector''s Edition', 'Lady Dimitrescu statue, artbook', 219.99);

-- Insert game-console relationships (GAME_CONSOLE)

INSERT INTO GAME_CONSOLE (GAME_ID, CONSOLE_ID, RELEASE_DATE, IS_EXCLUSIVE, RESOLUTION) VALUES
                                                                                           (1, 1, '2020-06-19', 1, '4K/30fps'),   -- The Last of Us PS5 (exclusivo)
                                                                                           (2, 1, '2020-12-10', 0, '4K/30fps'),   -- Cyberpunk PS5
                                                                                           (2, 2, '2020-12-10', 0, '4K/30fps'),   -- Cyberpunk XSX
                                                                                           (2, 4, '2020-12-10', 0, '4K/60fps'),   -- Cyberpunk PC
                                                                                           (3, 3, '2017-03-03', 1, '1080p/30fps'),-- Zelda Switch (exclusivo)
                                                                                           (4, 1, '2022-02-25', 0, '4K/60fps'),   -- Elden Ring PS5
                                                                                           (4, 2, '2022-02-25', 0, '4K/60fps'),   -- Elden Ring XSX
                                                                                           (5, 1, '2022-03-15', 0, '4K/60fps'),   -- GTA V PS5
                                                                                           (6, 1, '2022-11-09', 1, '4K/60fps'),   -- God of War PS5 (exclusivo)
                                                                                           (7, 1, '2020-11-12', 1, '4K/60fps');   -- Spider-Man PS5 (exclusivo)