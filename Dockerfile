FROM gcr.io/distroless/java21-debian12:nonroot
# Healtcheck lokalt/test
COPY --from=busybox:stable-musl /bin/wget /usr/bin/wget

# Working dir for RUN, CMD, ENTRYPOINT, COPY and ADD (required because of nonroot user cannot run commands in root)
WORKDIR /app

# Dependencies and config is bundled in jar file
COPY domene/target/app.jar .

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 \
    -XX:+PrintCommandLineFlags \
    -Duser.timezone=Europe/Oslo \
    --add-opens java.base/java.time=ALL-UNNAMED"

CMD ["app.jar"]
