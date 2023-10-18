FROM ghcr.io/navikt/fp-baseimages/java:17
COPY domene/target/*.jar app.jar
ENV JAVA_OPTS -XX:MaxRAMPercentage=75 --add-opens java.base/java.time=ALL-UNNAMED
