#!/bin/bash

# Move to the root directory (auth directory)
cd "$(dirname "$0")/.."

# Load modular deployment functions
source ./scripts/check_running_container.sh
source ./scripts/pull_and_run_container.sh
source ./scripts/health_check.sh
source ./scripts/stop_and_clean_container.sh
source ./scripts/reload_nginx.sh

# 1. Determine which container is currently running (blue or green)
check_running_container

# 2. Pull and run the new container
pull_and_run_container $NEW_CONTAINER

# 3. Run health check on the new container
if ! health_check $NEW_PORT; then
  echo "❌ Health check failed. Deployment aborted."
  exit 1
fi

# 4. Stop and remove the previous container
stop_and_clean_container $RUNNING_CONTAINER

# 5. Reload Nginx to apply port switching
reload_nginx $NEW_PORT

echo "✅ Finish Deploy"