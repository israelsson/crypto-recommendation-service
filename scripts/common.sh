#!/usr/bin/env bash

echo "Loading common resources and functions..."
# Docker alias
shopt -s expand_aliases
if command -v podman &>/dev/null; then
  alias docker='podman'
  alias docker-compose='podman-compose'
fi
