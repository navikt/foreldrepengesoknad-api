FROM ghcr.io/navikt/fp-baseimages/java:17
COPY target/*.jar app.jar
ENV TZ=Europe/Oslo
ENV JAVA_OPTS -XX:MaxRAMPercentage=75 --add-opens java.base/java.time=ALL-UNNAMED
