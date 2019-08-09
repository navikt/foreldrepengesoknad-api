FROM navikt/java:8

ARG version
ARG app_name

COPY target/*.jar "/app/app.jar"
