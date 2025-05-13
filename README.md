# Task API

Task API — это RESTful сервис для управления проектами и задачами, реализованный на Spring Boot с использованием JPA и PostgreSQL.

## Возможности
- Управление проектами (создание, получение, удаление)
- Управление состояниями задач внутри проектов
- Валидация и обработка ошибок

## Технологии
- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- JUnit 5, MockMvc

## Быстрый старт

### 1. Клонирование репозитория
```bash
git clone <ваш-репозиторий>
cd task-api
```

### 2. Конфигурация БД
В файле `src/main/resources/application.yaml` укажите параметры подключения к вашей базе PostgreSQL:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost/task-tracker
    username: postgres
    password: core76
```

### 3. Сборка и запуск
```bash
./gradlew bootRun
```

Приложение будет доступно на http://localhost:8082

### 4. Запуск тестов
```bash
./gradlew test
```

## Примеры запросов

### Получить все проекты
```
GET /api/projects
```

### Создать проект
```
PUT /api/projects?project_name=MyProject
```

### Удалить проект
```
DELETE /api/projects/{project_id}
```

### Получить состояния задач проекта
```
GET /api/projects/{project_id}/task-states
```

### Создать состояние задачи
```
POST /api/projects/{project_id}/task-states?task_state_name=ToDo
```

## Тесты
В проекте реализованы интеграционные тесты для основных REST-контроллеров с использованием MockMvc и JUnit 5.

## Контакты
Автор: [Ваше имя] 