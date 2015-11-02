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


function main
{
echo "-- Partenza demo per Ares framework --"

create_dirs
copy_files
./gradlew appRun
}

main
