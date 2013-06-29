#!/bin/bash
for FILE in $(ls images)
do
    OUTPUT=outputs/exiv2/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    exiftool "images/$FILE" > $OUTPUT
done
