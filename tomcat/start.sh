#!/bin/bash
CONTEXT_PATH=`yml.pl /conf/config.yml registry.context_path`
CATALINA_OPTS="$CATALINA_OPTS -Dcontext.path=${CONTEXT_PATH}"
echo CATALINA_OPTS: $CATALINA_OPTS
exec catalina.sh run