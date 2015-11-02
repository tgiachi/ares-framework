#!/bin/bash

ARES_DEMO_DIR="$HOME/ares/"

function create_dirs
{
  echo "-- Copia dei file per la demo --"
  echo "==> Demo directory:  $ARES_DEMO_DIR"
  if [ ! -d "$ARES_DEMO_DIR" ]; then
    mkdir -p $ARES_DEMO_DIR
  fi
}

function copy_files
{
  cp ./default/* -Rf $ARES_DEMO_DIR
}

function gradle_wrap_installer
{
  sudo curl -o /usr/local/bin/gradle-wrapper-here -L https://github.com/rholder/gradle-wrapper-here/releases/download/v0.1.3/gradle-wrapper-here && \
  sudo chmod +x /usr/local/bin/gradle-wrapper-here
  gradle-wrapper-here
}

function main
{
echo "-- Partenza demo per Ares framework --"

create_dirs
copy_files
gradle_wrap_installer
./gradlew appRun
}

main
