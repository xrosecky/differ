#!/bin/bash
java -Dapplication.properties=file:./application.properties -jar target/differ-cmdline-0.0.1-SNAPSHOT.jar --save-raw-outputs --save-report --save-properties ../docs/examples/images_01/05.png ../docs/examples/images_02/05.jpg
