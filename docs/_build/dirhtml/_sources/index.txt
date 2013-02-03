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
			 $(this).parent().find('.highlight-python').fadeOut();
			 $(this).hide();
			 $(this).parent().find('a.show').show();
			 return false;
		 });
		 $('.output .highlight-python').hide();
		 $('.output a.hide').hide();
	 });
	 </script>

Welcome to Differ Documentation!
================================

Contents:

.. contents::

.. toctree::
   :maxdepth: 2


Installation
------------

.. include:: installation.rst  

Recognized Validator Outputs
----------------------------

.. include:: djvudump.rst

.. include:: jpylyzer.rst

.. include:: exiftool.rst

.. include:: fits.rst

.. include:: kdu.rst

.. include:: daitss.rst

.. include:: imagemagick.rst

.. include:: jhove.rst

Command Line Application
------------------------

.. include:: cmdline.rst
