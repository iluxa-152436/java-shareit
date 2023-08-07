FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar server.jar
ENV TZ="Europe/Moscow"
RUN date
ENTRYPOINT ["java","-jar","/server.jar"]