#!/bin/bash
CATALINA_OPTS="$CATALINA_OPTS -Dcontext.path=${CONTEXT_PATH}"
echo CATALINA_OPTS: $CATALINA_OPTS
exec catalina.sh run