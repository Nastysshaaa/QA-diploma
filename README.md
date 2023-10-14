## Как запустить автотесты:
1. Запустить Docker Desktop
2. Выполнить [git clone](git@github.com:Nastysshaaa/QA-diploma.git)
3. Открыть проект в IntelliJ IDEA
4. Для запуска приложения с БД MySQL выполняем следующую команду:
   `java -jar artifacts/aqa-shop.jar "- Dspring.datasource.url=jdbc:mysql://localhost:3306/app" "- Dspring.datasource.username=app" "-Dspring.datasource.password=pass"`
5. Для запуска приложения с БД PostgreSQL выполняем следующую команду:
   `java -jar artifacts/aqa-shop.jar "- Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" "- Dspring.datasource.username=app" "-Dspring.datasource.password=pass"`
6. Открыть терминал и выполнить команду запуска контейнеров `docker-compose up`
7. Запустить тесты командой:
Для SQL:
`./gradlew clean test -DdUrl=jdbc:mysql://localhost:3306/mysql` в новой вкладке терминала
Для PostgreSQL:
`./gradlew clean test -DdUrl=jdbc:postgresql://localhost:5432/postgresql` в новой вкладке терминала
8. После завершения тестов выполнить команду ```.\gradlew allureServe```.
9. После выгрузки данных отчета Allure, завершить его работу комбинацией клавиш ```CTRL + C```.
10. Подтвердить завершение выполнения пакетного файла командой ```y```, затем ```ENTER```.
11. Завершить работу Docker командой ```docker-compose down```
