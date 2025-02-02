CREATE TABLE IF NOT EXISTS `Films`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(250) NOT NULL,
    `releaseDate` timestamp NOT NULL,
    `duration` integer NOT NULL,
    `mpa_id` integer NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Ratings`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Friends`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `user_id` integer NOT NULL,
    `friend_id` integer NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Genres`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Genres_save`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `film_id` integer NOT NULL,
    `genre_id` integer NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Likes`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `user_id` integer NOT NULL,
    `film_id` integer NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Users`
(
      `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
      `email` VARCHAR(100) NOT NULL,
      `login` VARCHAR(100) NOT NULL,
      `name` VARCHAR(100) NOT NULL,
      `birthday` TIMESTAMP NOT NULL,
      `friends_id` INTEGER,
      `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS `Directors`
(
      `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
      `name` VARCHAR(250) NOT NULL,
      `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS `Directors_save`
(
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `film_id` integer NOT NULL,
    `director_id` integer NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE IF EXISTS `Films`
    ADD CONSTRAINT IF NOT EXISTS `FilmMpa` FOREIGN KEY (mpa_id)
    REFERENCES `Ratings` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_g`
    ON `Films`(mpa_id);
ALTER TABLE IF EXISTS `Friends`
    ADD CONSTRAINT IF NOT EXISTS `FriendUser` FOREIGN KEY (friend_id)
    REFERENCES `Users` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_FriendUser`
    ON `Friends`(friend_id);
ALTER TABLE IF EXISTS `Friends`
    ADD CONSTRAINT IF NOT EXISTS `UserUser` FOREIGN KEY (user_id)
    REFERENCES `Users` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_UserUser`
    ON `Friends`(user_id);
ALTER TABLE IF EXISTS `Likes`
    ADD CONSTRAINT IF NOT EXISTS `LikeFilm` FOREIGN KEY (film_id)
    REFERENCES `Films` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_LikeFilm`
    ON `Likes`(film_id);
ALTER TABLE IF EXISTS `Likes`
    ADD CONSTRAINT IF NOT EXISTS `LikeUser` FOREIGN KEY (user_id)
    REFERENCES `Users` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_LikeUser`
    ON `Likes`(user_id);
ALTER TABLE IF EXISTS `Genres_save`
    ADD CONSTRAINT IF NOT EXISTS `GenreSaveFilm` FOREIGN KEY (film_id)
    REFERENCES `Films` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `GenreSaveFilm`
    ON `Genres_save`(film_id);
ALTER TABLE IF EXISTS `Genres_save`
    ADD CONSTRAINT IF NOT EXISTS `GenreSaveGenre` FOREIGN KEY (genre_id)
    REFERENCES `Genres` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_GenreSaveGenre`
    ON `Genres_save`(genre_id);



ALTER TABLE IF EXISTS `Directors_save`
    ADD CONSTRAINT IF NOT EXISTS `DirectorSaveFilm` FOREIGN KEY (film_id)
    REFERENCES `Films` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `DirectorSaveFilm`
    ON `Directors_save`(film_id);

ALTER TABLE IF EXISTS `Directors_save`
    ADD CONSTRAINT IF NOT EXISTS `DirectorSaveDirector` FOREIGN KEY (director_id)
    REFERENCES `Directors` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS `fki_DirectorSaveDirector`
    ON `Directors_save`(director_id);