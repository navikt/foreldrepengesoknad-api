FROM ghcr.io/navikt/fp-baseimages/chainguard:jre-21

# Dependencies and config is bundled in jar file
COPY domene/target/app.jar .
