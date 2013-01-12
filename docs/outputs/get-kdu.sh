#!/bin/bash
export LD_LIBRARY_PATH=/home/stavel/KDU71_Demo_Apps_for_Linux-x86-64_120605/
FILE="/opt/testovaci-data/ONLINE DIFFER/lena_std lossy.jpf"
/home/stavel/KDU71_Demo_Apps_for_Linux-x86-64_120605/kdu_expand -i "$FILE" -record /dev/stdout -no_decode
