PID=$(< wow-daemon.pid)

echo kill scim process : $PID

kill -15 $PID

rm wow-daemon.pid nohup.out