#!/bin/bash
for FILE in $(ls images)
do
    OUTPUT=outputs/djvudump/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    djvudump "images/$FILE" > "$OUTPUT"
done
