#!/bin/bash

total=5
echo "Running healthcheck which takes $total seconds"

for i in `seq $total`
do
    echo "I'm running iteration $i of $total"
    sleep 1
done

echo "Healthcheck complete"