#!/bin/sh
folder="/Users/yourname/Desktop/gc"


if [ -d $folder ]; then
	echo $folder
    sudo -s java -jar shredder.jar $folder
else
    echo "File does not exists."
fi
