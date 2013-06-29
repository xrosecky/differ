#!/bin/bash
for FILE in $(ls images)
do
    OUTPUT=outputs/jhove/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    jhove -h xml "images/$FILE" > $OUTPUT
done
