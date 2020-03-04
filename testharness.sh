#!/bin/bash
echo "========"
echo "Running test cases"
echo "========"

for n in $(ls inputs)
do
    echo "running test $n"
    java -jar CPS707--FE-LLC-Ticket-System.jar resources/accounts.txt resources/tickets.txt < inputs/$n > outputs/$n
done

echo "========"
echo "Test cases execution completed. Validating results"
echo "========"

for n in $(ls outputs)
do
    echo "validating test $n"
    diff -y outputs/$n expected/$n

    if [[ $? -eq 0 ]]; then
      echo "$n PASSED"
    else
      echo "$n FAILED!"
    fi
done

echo "========"
echo "Results validation completed."
echo "========"
