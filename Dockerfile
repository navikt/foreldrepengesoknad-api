FROM gcr.io/distroless/java21:nonroot

WORKDIR /app

COPY --from=busybox:stable-musl /bin/wget /usr/bin/wget
COPY domene/target/*.jar ./

ENV JAVA_OPTS -XX:MaxRAMPercentage=75 --add-opens java.base/java.time=ALL-UNNAMED

CMD ["/app/app.jar"]
