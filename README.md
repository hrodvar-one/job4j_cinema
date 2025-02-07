# Сервис - Кинотеатр

## Описание проекта

job4j_cinema - сервис по покупке билетов в кинотеатр.
Сервис позволяет:
1. Зарегистрировать нового пользователя или войти в систему при наличии аккаунта.
2. Посмотреть информацию о киносеансах и фильмах
3. Купить билет.

Данные сохраняются в БД.

### Технологии используемые в проекте:
- Java 17
- Spring boot 2.7.6
- Tomcat
- Thymeleaf 3.0.15
- Bootstrap
- Liquibase 4.15.0
- PostgreSQL 42.5.1
- H2 2.2.220
- Sql2o 1.6.0
- Junit5
- Jacoco 0.8.7
- AssertJ
- Mockito

### Требования к окружению:
- Java 17
- Maven 3.9.6
- PostgreSQL 16.3

# Запуск

1. Создайте Postgres базу данных "cinema" 
```
CREATE DATABASE cinema
```

2. Клонируйте репозиторий
```
git clone https://github.com/hrodvar-one/job4j_cinema
```

3. Переходим в папку куда склонировали проект
```
cd job4j_cinema
```

4. Соберите проект с помощью Maven под профилем "production":
```
mvn clean install -Pproduction
```

5. Запустите приложение:
```
mvn spring-boot:run
```
После запуска, проект будет доступен по адресу: [http://localhost:8080](http://localhost:8080)

Взаимодействие с приложением:

1. Главная

![Главная](screenshots/main_page.png)

2. Расписание

![Расписание](screenshots/shedule_page.png)

3. Кинотека

![Кинотека](screenshots/cinematheque_page.png)

4. Страница входа

![Вход](screenshots/login_page.png)

5. Страница регистрации

![Регистрация](screenshots/registration_page.png)

6. Страница покупки билета

![Покупка билета](screenshots/buy_page.png)

7. Страница успешной покупки билета

![Успешная покупка билета](screenshots/successful_buy_page.png)

8. Страница неуспешной покупки билета

![Неуспешная покупка билета](screenshots/error_buy_page.png)


## Контакты

Telegram: @hrodvar