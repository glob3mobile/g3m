#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

cd deploy
#emrun --no_browser --port 8080 .
#python -m SimpleHTTPServer 8080
python3 -m http.server 8080


