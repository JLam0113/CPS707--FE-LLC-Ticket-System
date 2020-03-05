#!/bin/bash
echo "========"
echo "Running test cases"
echo "========"

if [[ -d "input" ]]; then
  rm -rf input
fi
cp -r "inputs_mastercopy" "inputs"

lastDTF=$(ls resources/DTF*)

if [[ -d $lastDTF ]]; then
  mv $lastDTF resources/$(date +%s).dtf
fi

for n in $(ls inputs)
do
    echo "running unit test for $n"

    for m in $(ls inputs/$n/$n*) # inputs/$n/$n*
    do
      testfile=$(basename $m)
      testfile=${testfile%.*} # extract file name no extension
      echo "running test $testfile"
      java -jar CPS707--FE-LLC-Ticket-System.jar inputs/$n/accounts.txt inputs/$n/tickets.txt < inputs/$n/$testfile.inp > outputs/$testfile.out # inputs/$n/accounts.txt inputs/$n/tickets.txt < inputs/$n/$m > outputs/$m
    done
done

echo "========"
echo "Test cases execution completed. Validating results"
echo "========"

for n in $(ls inputs)
do
  for m in $(ls outputs/$n*)
  do
    testfile=$(basename $m)
    #testfile=${testfile%.*} # extract file name no extension

    echo "validating test $testfile"
    diff -y outputs/$testfile expected/$testfile

    if [[ $? -eq 0 ]]; then
      echo "$n PASSED"
    else
      echo "$n FAILED!"
    fi
  done

  echo "validating accounts.txt $n"
  diff -y "inputs/$n/accounts.txt" "expected/${n}_accounts.txt"

  if [[ $? -eq 0 ]]; then
      echo "$n PASSED"
    else
      echo "$n FAILED!"
    fi

  echo "validating tickets.txt $n"
  diff -y "inputs/$n/tickets.txt" "expected/${n}_tickets.txt"

  if [[ $? -eq 0 ]]; then
      echo "$n PASSED"
    else
      echo "$n FAILED!"
  fi
done

echo "validating DTF $n"
currentDTF=$(ls resources/DTF*)
diff -y $currentDTF "expected/DTF.dtf"

if [[ $? -eq 0 ]]; then
    echo "$n PASSED"
  else
    echo "$n FAILED!"
fi
