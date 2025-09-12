FROM ghcr.io/navikt/fp-baseimages/distroless:21

# Dependencies and config is bundled in jar file
COPY domene/target/app.jar .
