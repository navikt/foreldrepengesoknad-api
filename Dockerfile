FROM gcr.io/distroless/java17-debian12:nonroot

COPY --from=busybox:stable-musl /bin/wget /usr/bin/wget
COPY domene/target/*.jar app.jar

ENV JAVA_OPTS -XX:MaxRAMPercentage=75 --add-opens java.base/java.time=ALL-UNNAMED

CMD ["app.jar"]
