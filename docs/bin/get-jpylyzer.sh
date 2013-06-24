#!/bin/bash
for FILE in $(ls images)
do
    OUTPUT=outputs/jpylyzer/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    jpylyzer --verbose "images/$FILE"  | xmlstarlet fo > $OUTPUT
done
