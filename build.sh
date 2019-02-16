#! /bin/bash

POM_FILE="$(dirname "$(realpath "${BASH_SOURCE[0]:-$0}")")/pom.xml"

mvn -f "$POM_FILE" -Dstyle.color=always -T 1.5C -B clean install
