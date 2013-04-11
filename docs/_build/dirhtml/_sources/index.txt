.. Differ Documentation documentation master file, created by
   sphinx-quickstart on Mon Dec 10 10:43:50 2012.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.


.. raw:: html
	 
	 <script type="text/javascript">
	 $(function(){
		 $('a.show').click(function(){
			 $(this).parent().find('.highlight-python').fadeIn();
			 $(this).hide();
			 $(this).parent().find('a.hide').show();
			 return false;
		 });
		 $('a.hide').click(function(){
			 $(this).parent().find('.highlight-python').hide();
			 $(this).hide();
			 $(this).parent().find('a.show').show();
			 return false;
		 });
		 $('.output .highlight-python').hide();
		 $('.output a.hide').hide();
		 $('td div').width($('div.contents').width()/2.0);
	 });
	 </script>

Ideas List - Google Summer of Code 2013
=======================================

.. contents::
   :local:

.. toctree::
   :maxdepth: 2

.. include:: improvements.rst

Links related to the project
----------------------------

project webpage
...............

     - http://differ.nkp.cz:8080/differ/ (BETA)
     - http://differ.nkp.czdiffer/

project source code
...................

     - http://github.com/jstavel/differ/differ
presentation
............

     - http://www.youtube.com/watch?v=2u0MxhOZ5h8

presentation Lighting talk at GSOC2012
......................................

     - https://docs.google.com/folder/d/0B-c5jwGzSQyPOC1JWjhnXzZKSXM/edit?docId=0Bw7eDY6ebeEdcGdyc2luSXNsTkE

project poster
..............

     - https://docs.google.com/file/d/0B9Ah7Og9gY_ORi1kandLZVJ2NEU/edit?usp=sharing

last year project’s topics GSOC 2012
....................................

     - https://github.com/moravianlibrary/differ/wiki/GSoC


The Image Validator - DIFFER
============================

.. contents::
   :local:

.. toctree::
   :maxdepth: 2

Introduction
------------

The Image Validator helps an user with transformation of an image 
from one format to another one.

Sometimes it is necessary to convert an image from one format into another format. 
Some distorsions or error effects can occur when transforming of an image.

The Image Validator gives user the information whether a graphic information that 
an image carries was destroyed or not after the transformation.

The Image Validator collects significant properties from various image data extractors,
process them and shows them to a user so that he or she can easily compare two images and controls 
a tranformation process.

.. include:: introduction.rst

Links related to the project
............................

.. list-table::  
   :header-rows: 0

   * - project webpage
     - http://differ.nkp.cz:8080/differ/ (BETA)
     - http://differ.nkp.czdiffer/
   * - project source code
     - http://github.com/jstavel/differ/differ
   * - presentation
     - http://www.youtube.com/watch?v=2u0MxhOZ5h8
   * - presentation Lighting talk at GSOC2012
     - https://docs.google.com/folder/d/0B-c5jwGzSQyPOC1JWjhnXzZKSXM/edit?docId=0Bw7eDY6ebeEdcGdyc2luSXNsTkE
   * - project poster
     - https://docs.google.com/file/d/0B9Ah7Og9gY_ORi1kandLZVJ2NEU/edit?usp=sharing
   * - last year project’s topics GSOC 2012
     - https://github.com/moravianlibrary/differ/wiki/GSoC

Recognized Extractor Outputs
----------------------------

.. include:: exiftool.rst

.. include:: exiv2.rst

.. include:: jhove.rst

.. include:: imagemagick.rst

.. include:: fits.rst

.. include:: kdu.rst

.. include:: jpylyzer.rst

.. include:: daitss.rst

.. include:: djvudump.rst

Installation
------------

.. include:: installation.rst

Command Line Application
------------------------

.. include:: cmdline.rst

Examples of validation results
------------------------------

.. include:: examples.rst

