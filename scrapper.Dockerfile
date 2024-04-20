FROM openjdk:21
WORKDIR /app
COPY . /app/scrapper.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/scrapper.jar"]
