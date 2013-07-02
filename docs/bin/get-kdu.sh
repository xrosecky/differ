#!/bin/bash
PATH=$PATH:~/lib/kakadu
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:~/lib/kakadu
export PATH
export LD_LIBRARY_PATH

for FILE in $(ls images)
do
    OUTPUT=outputs/kdu/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    home/klas/lib/kakadu/kdu_expand -i "images/$FILE" -record /dev/stdout -no_decode > $OUTPUT
done
