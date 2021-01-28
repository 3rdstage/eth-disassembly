#! /bin/bash

readonly uname=`uname -s`  # OS type
readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/mongodb/data" && cd "${script_dir}/../run/mongodb" && pwd)
readonly port=27017

# Catch command-line options
# Check whether GNU getopt is available or not
if [ `getopt --test; echo $?` -ne 4 ]; then
  echo "The avaiable 'getopt' is not GNU getopt which supports long options."
  echo "For MacOS, install 'gnu-getopt' refering 'https://formulae.brew.sh/formula/gnu-getopt'."
  exit 410
fi

options=$(getopt -o av --long "auth,verbose" --name 'besu-start-options' -- "$@");

if [ $? -ne 0 ]; then
  command=${0##*/}
  echo "Unable to parse command line, which expect '$command [-r|--refresh] [-d|--dryrun] [-b|--background] [-v|verbose]'."
  echo ""
  exit 400
fi

eval set -- "$options"

declare auth=0   #false
declare verboses=0 
while true; do
  case "$1" in
    -a | --auth )
      echo "Authentication enabled."
      auth=1
      shift ;;
    -v | --verbose )
      verboses=1
      shift ;;
    -- ) shift; break ;;
  esac
done

# Check whether or not another process (maybe `mongod`) is already running.
cmd=`lsof -i TCP@127.0.0.1:${port} -F c | grep -E '^c' | cut -c 2-`
if [ ! -z $cmd ]; then
  echo ""
  echo "TCP port ${port} on localhost is already occupied by '${cmd}'."
  echo "Cancled the script."
  exit 100
fi

# https://docs.mongodb.com/manual/reference/program/mongod/#options
cmd="mongod --bind_ip localhost \
       --logpath '${run_dir}'/mongod.log \
       --pidfilepath '${run_dir}'/mongod.pid \
       --dbpath '${run_dir}'/data \
       --nojournal \
       --profile 0 \
       --enableFreeMonitoring off"

if [ $auth -eq 1 ]; then
  cmd="$cmd --auth"
fi

case $uname in
Darwin*) # macOS
  cmd="$cmd --fork"
  ;;
*)
  cmd="$cmd --snmp-disabled"
esac

echo ""
echo "Starting 'mongod'."
echo $cmd
eval $cmd

if [ $? -eq 0 ]; then
  sleep 3
  echo ""
  tail -n 50 "${run_dir}"/mongod.log
  echo ""
  echo "A MongoDB has started."
  echo "The log file and PID file are located under '${run_dir}'."
fi  
