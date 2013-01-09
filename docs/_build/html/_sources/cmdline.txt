Arguments of the application
.............................

The application can validate one image or compare two images.

 =========================   =========================================================================
 Argument                    What   
 =========================   =========================================================================
 --save-report               Save report into file with the same name and with extension *.drep
 --load-report               Load report from a given file and print it in a readable form at stdout
 --include-image-in-report   
 --report-format             html/txt/pdf/xml
 --transform-report          transform xml report into other format - html/txt/pdf
 --save-histogram            save histogram as csv
 --name-style                a way how to name and store result files
 --save-raw-outputs          save raw outputs from validators into files
 =========================   =========================================================================

Compare report contains of all informations from validation reports and even compare informations.

Naming styles
..............

  thesamename ::

    generated files has the same name as image file

  thesamenamedir ::

    all generated files are in a directory with the same name as an image file
  
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



Program output when validating image
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. literalinclude:: outputs/cmdline.txt

Program output when comparing images
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. literalinclude:: outputs/cmdline-compare.txt
