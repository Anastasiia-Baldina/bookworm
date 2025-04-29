### Проект Bookworm

Настройка БД
1) Скачать по ссылке https://www.postgresql.org/download/ и установить СУБД PostgreSql
2) Скачать по ссылке https://www.pgadmin.org/download/ и установить средство управления PgAdmin
3) В PgAdmin создать подключение к установленной БД PostgresSql
4) В PgAdmin создать схему book_worm и задать пароль пользователя
5) Выполнить скрипт в PgAdmin скрипт bookworm-internal-shard-boot/src/main/resources/db_scripts/v1.sql

Настрока Kafka
1) Скачать https://kafka.apache.org/downloads и установить сервис Kafka v3.9.0

Настройка Zookeeper
2) Скачать https://zookeeper.apache.org/releases.html#download и установить сервис Zookeeper

Backend
1) Установить Java JDK v21
2) Установить Maven v3.8.3 и выше
3) В настройках сервисов (application.yaml) приписать адреса Kafka, Zookeeper, и параметры подключения к БД PostgreSql
4) Выполнить команду mvn clean package
5) Запустить сервисы командами 
java -jar bookworm-internal-shard-boot\target\bookworm-internal-shard-boot-1.0-SNAPSHOT.jar
java -jar bookworm-internal-facade-boot\target\bookworm-internal-facade-boot-1.0-SNAPSHOT.jar
java -jar bookworm-telegram-msg-proc-boot\target\bookworm-telegram-msg-proc-boot-1.0-SNAPSHOT.jar
java -jar bookworm-telegram-file-proc-boot\target\bookworm-telegram-file-proc-boot-1.0-SNAPSHOT.jar
java -jar bookworm-telegram-responder-boot\target\bookworm-telegram-responder-boot-1.0-SNAPSHOT.jar
java -jar bookworm-telegram-listener-boot\target\bookworm-telegram-listener-boot-1.0-SNAPSHOT.jar

Приложение "Книгочей"
1) Установить актуальную IDE Android Studio
2) Открыть проект android/bookworm
3) Запустить проект в виртуальной машине AVD (andriod virtual device) или собрать apk и установить на телефон.

