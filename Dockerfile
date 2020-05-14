FROM navikt/java:14
COPY target/*.jar "/app/app.jar"
