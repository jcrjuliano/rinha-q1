FROM amazoncorretto:21.0.2

WORKDIR /app

COPY build/libs/rinha-q1-0.0.1-SNAPSHOT.war /app/app.war

EXPOSE 8080

CMD ["java", "-jar", "app.war"]