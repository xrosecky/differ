Introduction
------------------

Developing preservation processes for a trusted digital repository requires the utilization of new methods and technologies, which have helped to accelerate the whole process of control.

The current approach at the Digital Preservation Standards Department at The National Library of the Czech Republic along with the GSOC 2012/2013 program contribution is to develop a quality control application for still image file formats capable of performing identification, characterization, validation and visual/mathematical comparison integrated into an operational digital preservation framework.

The online application DIFFER is utilizing existing tools (JHOVE, FITS, ExifTool, KDU_expand, DJVUDUMP, Jpylyzer, etc.), which are mainly used separately across a whole spectrum of existing projects.

This open source application comes with a well-structured and uniform GUI, which helps the user to understand the relationships between various file format properties, detect visual and non-visual errors and simplifies decision-making. An additional feature called compliance-check is designed to help us check the required specifications of the JPEG2000 file format.

.. raw:: html

	 <iframe width="640" height="360" src="http://www.youtube.com/embed/2u0MxhOZ5h8?feature=player_detailpage" frameborder="0" allowfullscreen></iframe>

New project was born
....................

The project ``The Image Validator`` is a successor of the project ``Differ``.
``Differ`` is short for ``Determinator of Image File Format properties``.

The project ``Differ`` was written in ``C++`` and ``PHP`` and it allowed to get
main properties for images in various image formats.

The main purpose of creating a succesor was the need to have a program running
on a various platforms. That is why new project ``The Image Validator`` is written in ``Java``.


About
..............

The Image Validator helps an user with transformation of an image 
from one format to another one.

Sometimes it is necessary to convert an image from one format into another format. 
Some distorsions, glitches or side effects can occur when transforming an image.

The Image Validator gives the user information whether the graphic information that 
an image carries was destroyed or not after the transformation.

The Image Validator also collects significant properties from various image data extractors,
processes them and shows them to a user so that he or she can easily compare two images and control 
the tranformation process.

