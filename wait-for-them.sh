#!/bin/bash

# TODO: to improve (very simple version at the moment)

WAITFORTHEM_cmdname=${0##*/}

echoerr() { if [[ $WAITFORIT_QUIET -ne 1 ]]; then echo "$@" 1>&2; fi }

usage()
{
    cat << USAGE >&2
Usage:
    $WAITFORTHEM_cmdname host:port [-- command args]
    -- COMMAND ARGS             Execute command with args after the test finishes
USAGE
    exit 1
}

while [[ $# -gt 0 ]]
do
    case "$1" in
        *:* )
        ./wait-for-it.sh $1
        shift 1
        ;;
        --)
        shift
        exec "$@"
        break
        ;;
        --help)
        usage
        ;;
        *)
        echoerr "Unknown argument: $1"
        usage
        ;;
    esac
done
