# movie-catalog


Пет проект : Каталог фильмов с функциями CREATE, GET, UPDATE, DELETE 


GET ALL Movies - / 
GET Movies By Id - /movie/{id}

POST Save Movie - /save-movie
UPDATE movie - /update-movie/{id}
DELETE movie- /delete/{id}

FILTER - filter - /filter/{name}/{genre}/{lastname}

name - Название фильма
genre - жанр фильма
lastname - фамилия актера


Стэк технологий: Spring MVC, JDBC, Liquibase, thymeleaf, Html, css


Чтобы запустить проект вам надо 

1. Java 11 и выше https://www.oracle.com/cis/java/technologies/javase/jdk11-archive-downloads.html
2. Postgresql 12 https://www.postgresql.org/download/
3. Maven https://maven.apache.org/download.cgi 
Шаги для запуска приложения:
установить в src/main/java/org/movie/config в классе SpringConfig в методе getDataSource() свои параметры подключения к бд(url, username, password),
собрать проект с помощью команды: mvn clean install
запустить Tomkat server
