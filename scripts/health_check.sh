#!/bin/bash

health_check() {
  local PORT=$1

  echo "▶️ Health Check : http://localhost:${PORT}${AUTH_ACTUATOR_PATH}/health"
  for retry_count in {1..10}; do
    response=$(curl -s \
      -H "X-Api-Key: ${PLATFORM_API_KEY}" \
      -H "X-Service-Name: ${PLATFORM_SERVICE_NAME}" \
      http://localhost:${PORT}${AUTH_ACTUATOR_PATH}/health)

    if echo "$response" | grep -q 'UP'; then
      echo "✅ Health check successful"
      return 0
    else
      echo "❌ Health check failed (try $retry_count)"
    fi
    sleep 5
  done

  return 1
}