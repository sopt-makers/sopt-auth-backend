#!/bin/bash

stop_and_clean_container() {
  echo "🛑 Stopping and removing container: $1"
  docker-compose stop $1
  docker-compose rm -f $1
  docker image prune -a -f
}