for FILE in $(ls images)
do
    OUTPUT=outputs/daitss/$FILE.raw
    echo -e "images/$FILE \t -> $OUTPUT"
    #curl -F "document=@images/$FILE" -F "extension=jpg" http://description.fcla.edu/description > daitss.raw
    curl -F "document=@images/$FILE" http://description.fcla.edu/description > $OUTPUT
done


