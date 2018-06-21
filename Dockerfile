FROM navikt/java:8

RUN apk add --no-cache curl

ARG version
ARG app_name

COPY target/$app_name-$version.jar "/app/app.jar"
