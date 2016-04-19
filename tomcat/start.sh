#!/bin/bash
CATALINA_OPTS="$CATALINA_OPTS -Dcontext.path=${CONTEXT_PATH}"
echo CATALINA_OPTS: $CATALINA_OPTS
catalina.sh run