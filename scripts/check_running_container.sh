#!/bin/bash

check_running_container() {
  local CONTAINER_NAME=$(sudo docker ps --filter "name=auth-" --format "{{.Names}}" | head -n 1)

  if [ -z "$CONTAINER_NAME" ]; then
    echo "⚠️  No running container detected. Defaulting to auth-blue."
    RUNNING_CONTAINER=auth-green
    NEW_CONTAINER=auth-blue
    NEW_PORT=$AUTH_BLUE_PORT
  else
    local HOST_PORT=$(sudo docker inspect -f '{{(index (index .NetworkSettings.Ports "8080/tcp") 0).HostPort}}' "$CONTAINER_NAME")

    if [[ "$HOST_PORT" == "$AUTH_BLUE_PORT" ]]; then
      RUNNING_CONTAINER=auth-blue
      NEW_CONTAINER=auth-green
      NEW_PORT=$AUTH_GREEN_PORT
    elif [[ "$HOST_PORT" == "$AUTH_GREEN_PORT" ]]; then
      RUNNING_CONTAINER=auth-green
      NEW_CONTAINER=auth-blue
      NEW_PORT=$AUTH_BLUE_PORT
    else
      echo "⚠️  Unexpected port detected: $HOST_PORT. Defaulting to auth-blue."
      RUNNING_CONTAINER=auth-green
      NEW_CONTAINER=auth-blue
      NEW_PORT=$AUTH_BLUE_PORT
    fi
  fi

  export RUNNING_CONTAINER
  export NEW_CONTAINER
  export NEW_PORT
}