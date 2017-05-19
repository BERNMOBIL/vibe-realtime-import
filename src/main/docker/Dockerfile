FROM openjdk:8
VOLUME /tmp
ADD realtime-import-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
CMD ["java", "-jar", "-Xmx1G", "app.jar"]
