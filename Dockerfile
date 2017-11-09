FROM navikt/java:8

ARG version
ARG app_name

COPY target/$app_name-$version.jar "/app/app.jar"
