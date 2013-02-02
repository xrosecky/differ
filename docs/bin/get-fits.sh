#!/bin/bash
for FILE in $(ls images)
do
    OUTPUT=outputs/fits/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    /opt/fits-0.6.1/fits.sh -i "images/$FILE" > $OUTPUT
done
