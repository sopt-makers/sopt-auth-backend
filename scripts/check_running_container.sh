#!/bin/bash

check_running_container() {
  local RUNNING_PORT=$(sudo docker ps | grep 'auth-' | awk -F '->' '{print $1}' | awk '{print $NF}' | cut -d ':' -f 2)

  if [[ "$RUNNING_PORT" == "$AUTH_BLUE_PORT" ]]; then
    RUNNING_CONTAINER=auth-blue
    NEW_CONTAINER=auth-green
    NEW_PORT=$AUTH_GREEN_PORT
  elif [[ "$RUNNING_PORT" == "$AUTH_GREEN_PORT" ]]; then
    RUNNING_CONTAINER=auth-green
    NEW_CONTAINER=auth-blue
    NEW_PORT=$AUTH_BLUE_PORT
  else
    echo "⚠️  No running container detected. Defaulting to auth-blue."
    RUNNING_CONTAINER=auth-green
    NEW_CONTAINER=auth-blue
    NEW_PORT=$AUTH_BLUE_PORT
  fi

  export RUNNING_CONTAINER
  export NEW_CONTAINER
  export NEW_PORT
}