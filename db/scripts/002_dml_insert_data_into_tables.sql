INSERT INTO files (name, path) VALUES
('Poster for Film 1', 'files/poster_harry_potter_and_the_philosophers_stone.jpg'),
('Poster for Film 2', 'files/poster_lord_of_the_rings_the_fellowship_of_the_ring.jpg'),
('Poster for Film 3', 'files/poster_sweet_november.jpg'),
('Poster for Film 4', 'files/poster_the_rock.jpg');

INSERT INTO genres (name) VALUES
('Fantasy'),
('Adventures'),
('Drama'),
('Thriller');

INSERT INTO films (name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id) VALUES
('Гарри Поттер и философский камень', 'Первая часть романа Джоан Роулинг повествует о приключениях 11-летнего мальчика Гарри Поттера', 2001, 1, 11, 159, 1),
('Властелин колец: Братство Кольца', 'Первая часть трилогии Питера Джексона', 2001, 2, 15, 228, 2),
('Сладкий ноябрь', 'Фильм о романтической истории Сары и Нельсона, которые влюблены и счастливы вместе, лишь рак встал между ними', 2001, 3, 18, 115, 3),
('Скала', 'Американский остросюжетный триллер', 1996, 4, 18, 136, 4);

INSERT INTO halls (name, row_count, place_count, description) VALUES
('Зал 1', 15, 20, 'Самый большой зал в кинотеатре.'),
('Зал 2', 10, 10, 'Средний зал.'),
('Зал 3', 6, 7, 'Малый зал.'),
('Зал 4', 5, 5, 'VIP зал.');

INSERT INTO film_sessions (film_id, halls_id, start_time, end_time, price) VALUES
(1, 1, '2025-01-25 15:00:00', '2025-01-25 18:00:00', 300),
(2, 2, '2025-01-25 16:00:00', '2025-01-25 20:00:00', 500),
(3, 3, '2025-01-25 20:00:00', '2025-01-25 22:00:00', 800),
(4, 1, '2025-02-25 16:00:00', '2025-02-25 19:00:00', 700),
(3, 4, '2025-02-25 13:00:00', '2025-02-25 15:00:00', 900);