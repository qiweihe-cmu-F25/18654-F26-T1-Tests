#!/bin/bash

for dir in */ ; do
  if [ -f "$dir/pom.xml" ]; then
    echo "Building project in $dir"
    cd "$dir"
    mvn clean install -DskipTests
    cd ..
    echo -e "\n\n\n"
  else
    echo "Skipping $dir (no pom.xml found)"
    echo -e "\n\n\n"
  fi
done




