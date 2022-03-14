FROM navikt/java:17
COPY target/*.jar "/app/app.jar"
ENV JAVA_OPTS -XX:MaxRAMPercentage=75 --enable-preview --add-opens java.base/java.time=ALL-UNNAMED
