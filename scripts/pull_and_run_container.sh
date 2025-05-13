#!/bin/bash

pull_and_run_container() {
  local CONTAINER_NAME=$1
  echo "▶️ Pull & Run: ${CONTAINER_NAME}"
  docker-compose pull $CONTAINER_NAME
  docker-compose up -d $CONTAINER_NAME
}