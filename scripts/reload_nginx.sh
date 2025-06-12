#!/bin/bash

reload_nginx() {
  local PORT=$1
  echo "🔄 Reloading Nginx to use port ${PORT}"
  echo "set \$service_url http://127.0.0.1:${PORT};" | sudo tee /etc/nginx/conf.d/platform-auth-url.inc
  sudo nginx -s reload
  echo "✅ Nginx now routing to: $(sudo cat /etc/nginx/conf.d/platform-auth-url.inc)"
}
