#!/bin/bash

k=0
for file in $@
do

	sed -i 's/false_/num'$k'false_/g' $file
	sed -i 's/end_/num'$k'end_/g' $file
	sed -i 's/true_/num'$k'true_/g' $file
	sed -i 's/while_/num'$k'while_/g' $file
	sed -i 's/else_/num'$k'else_/g' $file

	sed -i 's/div_/num'$k'div_/g' $file
	sed -i 's/mod_/num'$k'mod_/g' $file
	sed -i 's/io_/num'$k'io_/g' $file
	sed -i 's/overflow_/num'$k'overflow_/g' $file
	sed -i 's/stack_/num'$k'stack_/g' $file
	sed -i 's/no_/num'$k'no_/g' $file
	let k+=1
done
