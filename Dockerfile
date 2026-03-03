# jdk image pull
FROM eclipse-temurin:17-jdk-alpine

# jar 파일 위치 지정
#   - ARG: 매개변수
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ./backend.jar
ENTRYPOINT ["java", "-jar", "./backend.jar"]