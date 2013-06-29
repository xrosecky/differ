for FILE in $(ls images)
do
    OUTPUT=outputs/daitss/$FILE.raw
    if echo "$FILE" | grep -v -i '.fits'
	then
	EXTENSION=${FILE#*.}
	echo $EXTENSION
	echo -e "images/$FILE \t -> $OUTPUT"
	#curl -F "document=@images/$FILE" -F "extension=jpg" http://description.fcla.edu/description > daitss.raw
	curl -F "document=@images/$FILE" -F "extension=$EXTENSION" http://description.fcla.edu/description > $OUTPUT
	sleep 2
    fi
done


