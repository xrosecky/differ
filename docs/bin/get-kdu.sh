#!/bin/bash
export LD_LIBRARY_PATH=/home/stavel/KDU71_Demo_Apps_for_Linux-x86-64_120605/
for FILE in $(ls images)
do
    OUTPUT=outputs/kdu/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    /home/stavel/KDU71_Demo_Apps_for_Linux-x86-64_120605/kdu_expand -i "images/$FILE" -record /dev/stdout -no_decode > $OUTPUT
done
