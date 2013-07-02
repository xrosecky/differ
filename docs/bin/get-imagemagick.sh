#!/bin/bash
for FILE in $(ls images)
do
    OUTPUT=outputs/imagemagick/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    identify -verbose "images/$FILE" > "$OUTPUT"
done

