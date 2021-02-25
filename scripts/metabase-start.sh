#! /bin/bash

# References
#   - https://www.metabase.com/docs/latest/operations-guide/environment-variables.html
#   - https://www.metabase.com/docs/latest/operations-guide/configuring-application-database.html


readonly uname=`uname -s`  # OS type
readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/metabase/data" && cd "${script_dir}/../run/metabase" && pwd)
readonly port=3000  # default port for Metabase

mkdir -p "${run_dir}/logs"

export MB_DB_TYPE="h2"
export MB_DB_FILE="${run_dir}/data/metabase.db"
export MB_JETTY_HOST="localhost"
export MB_JETTY_PORT=${port}
export MB_SITE_LOCALE="en"
export MB_PLUGINS_DIR="${run_dir}/plugins"

# Check whether or not another process (maybe another previous `Metabase` or other process) is already running.
cmd=`lsof -i TCP@127.0.0.1:${port} -F c | grep -E '^c' | cut -c 2-`
if [ ! -z $cmd ]; then
  echo ""
  echo "TCP port ${port} on localhost is already occupied by '${cmd}'."
  echo "Cancled the script."
  exit 100
fi

cd "${script_dir}/.."

cmd="java -jar '${run_dir}/metabase.jar'"

echo ""
echo "Starting 'metabase'."
echo $cmd
eval $cmd


