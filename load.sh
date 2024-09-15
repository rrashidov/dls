#/bin/bash

x=1
while [ $x -gt 0 ]
do
  echo "Execution number: $x"
  curl -X POST localhost:8081/api/v1/lock/test-lock-id-${x}
  curl -X DELETE localhost:8081/api/v1/lock/test-lock-id-${x}
  x=$(( $x + 1 ))
done
