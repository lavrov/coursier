#!/bin/bash
set -e

if [ -z ${VERSION+x} ]; then
  VERSION="$(git describe --tags --abbrev=0 --match "v*" | sed 's/^v//')"
fi

OUTPUT="${OUTPUT:-"coursier"}"

SBTPACK_LAUNCHER="$(dirname "$0")/../modules/cli/target/pack/bin/coursier"

if [ ! -f "$SBTPACK_LAUNCHER" ]; then
  sbt scala212 cli/pack
fi

"$SBTPACK_LAUNCHER" bootstrap \
  "io.get-coursier:coursier-cli_2.12:$VERSION" \
  -J "-noverify" \
  --no-default \
  -r central \
  -r typesafe:ivy-releases \
  -f -o "$OUTPUT" \
  "$@"
