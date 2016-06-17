#!/bin/bash
openssl req \
    -new \
    -newkey rsa:4096 \
    -days 365 \
    -subj "/CN=localhost" \
    -nodes \
    -x509 \
    -keyout auth.key \
    -out conf/registry/auth.cert

openssl rsa -in auth.key -out conf/registry-web/auth.key
rm auth.key