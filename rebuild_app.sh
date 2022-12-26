#!/bin/bash

# detecting docker compose installation
if ! command -v docker compose --version &> /dev/null
then
	compose="docker-compose"
else
	compose="docker compose"
fi

$compose build app

$compose up app -d
