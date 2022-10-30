#!/bin/bash

set -eu

mvn clean verify
docker push lkwg82/webradio-api