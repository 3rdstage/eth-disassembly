#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/mongodb/data" && cd "${script_dir}/../run/mongodb" && pwd)

# https://docs.mongodb.com/manual/reference/program/mongod/#options

mongod --bind_ip localhost \
       --logpath "${run_dir}"/mongod.log \
       --pidfilepath "${run_dir}"/mongod.pid \
       --dbpath "${run_dir}"/data \
       --nojournal \
       --profile 0 \
       --snmp-disabled \
       --enableFreeMonitoring off
