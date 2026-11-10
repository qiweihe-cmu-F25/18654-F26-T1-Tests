#!/bin/bash

# Check if .env.example exists in the current directory
if [ -f ".env.example" ]; then
    cp ".env.example" ".env"
    echo "Copied .env.example to .env in the current directory"
else
    echo "No .env.example found in the current directory"
fi

# Loop through all directories in the current directory
for dir in */ ; do
    # Check if .env.example exists in the directory
    if [ -f "${dir}.env.example" ]; then
        # If .env.example exists, copy it to .env
        cp "${dir}.env.example" "${dir}.env"
        echo "Copied .env.example to .env in ${dir}"
    else
        echo "No .env.example found in ${dir}"
    fi
done
