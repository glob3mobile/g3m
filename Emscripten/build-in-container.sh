#!/bin/bash
#
# Runs the Makefile inside the official Emscripten image, so every machine builds with the same toolchain.
#
# usage: CONTAINER_RUNTIME=docker bash build-in-container.sh <make arguments>
#   e.g. CONTAINER_RUNTIME=docker bash build-in-container.sh MODE=release -j8
#
# CONTAINER_RUNTIME: any OCI runtime with docker-compatible flags (docker, podman).

set -e

EMSDK_IMAGE="emscripten/emsdk:6.0.12"

if [[ -z "$CONTAINER_RUNTIME" ]]; then
  echo "- Missing mandatory variable CONTAINER_RUNTIME (docker or podman)"
  exit 1
fi

# the g3m repository, mounted at /src
G3M_DIRECTORY="$(cd "$(dirname "$0")/.." && pwd)"

# the toolchain builds system libraries on demand; a volume per image keeps them between runs
CACHE_VOLUME="emsdk-cache-${EMSDK_IMAGE##*:}"

"$CONTAINER_RUNTIME" run --rm \
  -v "$G3M_DIRECTORY":/src \
  -v "$CACHE_VOLUME":/emsdk/upstream/emscripten/cache \
  -u "$(id -u):$(id -g)" \
  -w /src/Emscripten \
  "$EMSDK_IMAGE" \
  make FILE_PREFIX_MAP="/src=$G3M_DIRECTORY" "$@"
