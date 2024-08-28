#!/bin/bash

echo "Running healthcheck"
if [[ "$1" == "thearg" ]]
then
    echo "Healthcheck complete"
    exit 0
else
    echo "The argument wasn't set properly, healthcheck failed"
    exit 1
fi