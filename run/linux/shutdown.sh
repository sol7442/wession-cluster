PID=$(< scim.pid)

echo kill scim process : $PID

kill -15 $PID

rm scim.pid