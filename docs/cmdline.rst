Arguments of the application
.............................

The application can validate one image or compare two images.

 =========================   ===================================================================================
 Argument                    What   
 =========================   ===================================================================================
 --save-report               Save report into file with the same name and with extension *.drep
 --load-report               Load report from a given file and print it in a readable form at stdout
 --include-image-in-report   
 --report-format             html/txt/pdf/xml
 --transform-report          transform xml report into other format - html/txt/pdf
 --save-histogram            save histogram as csv
 --save-raw-outputs          save raw outputs from validators into files
 --load-properties           program will print just properties that has 1 in the file defined by this argument
 --show-properties           program will print out all known properties in a usable format and will finish
 =========================   ===================================================================================

Compare report contains of all informations from validation reports and even compare informations.


Example of property list definition
...................................

::

   Image width;1
   Image height;0
   

So property =Image width= will be printed and =Image height= will not be printed.

Examples
.........

Program usage
~~~~~~~~~~~~~
  
::

   validates image, prints report readable way at stdout and saves it into file image.drep
   c:\> differ.bat --save-report image.jpg

   loads report from file image.drep and prints it readable way at stdout
   c:\> differ.bat --load-report image.drep

   validates images and compares it and prints report readable way at stdout 
   and save it into file image1.drep and image2.drep
   c:\> differ.bat --save-report image1.jpg image2.jpg

   validates images and compares it and prints report readable way at stdout 
   The program will print properties regarding to a file properties.lst
   c:\> differ.bat --load-properties properties.lst image1.jpg image2.jpg

   The program will print properties in a format that can be used to choose properties to print in future
   c:\> differ.bat --show-properties




Program output when validating image
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. literalinclude:: outputs/cmdline.txt

Program output when comparing images
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. literalinclude:: outputs/cmdline-compare.txt


Program output examples
~~~~~~~~~~~~~~~~~~~~~~~

.. literalinclude:: examples.rst

