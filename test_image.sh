#!/bin/bash

set -ex

err_report() {
    echo "Error on line $1"
}

cleanup(){
  echo "cleanup running container $cid"
  docker kill "$cid"
}

trap 'err_report $LINENO' ERR
trap 'cleanup' EXIT

cid=$(docker run --rm -d lkwg82/webradio-api)
ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' "$cid")

curl \
  --retry 10 \
  --retry-delay 1 \
  --retry-connrefused \
  --fail \
  "http://$ip:8080/stationInfo?stationId=2459"