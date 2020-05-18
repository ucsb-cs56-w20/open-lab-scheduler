release: ./mvnw flyway:baseline; ./mvnw flyway:migrate
web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar 