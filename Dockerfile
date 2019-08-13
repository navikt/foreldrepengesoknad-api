FROM navikt/java:11

ARG version
ARG app_name

COPY target/*.jar "/app/app.jar"
