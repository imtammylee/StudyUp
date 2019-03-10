#!/usr/bin/env bash
set -e

sed -i'' "s/%{REDIS_HOST}/$1/" /etc/nginx/nginx.conf
exec /usr/sbin/nginx -s reload

exec "$@"
