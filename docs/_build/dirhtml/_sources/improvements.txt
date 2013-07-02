**Web interface** (GUI) improvement
-----------------------------------

to design of the existing GUI, preserving all the existing functionality

to add and review new features
..............................

::

    Widgets, Button, Functionalities, Traffic, Results, Compare, etc.

to add content into project webpage
...................................

::

    About, Documentation, FAQ, TOS, Help, etc. 


Any thoughts and improvement are welcome :)

required skills:

- Java
- Java Image IO and Image processing
- JUnit testing
- Excellent English (writing and speaking)

desirable skills:

- Spring framework
- experience with continuous integration
- Vaadin / GWT (google web toolkit)

**Mapping** of tool’s **XML output** into **web API**
-----------------------------------------------------

to **improve** current functionalities
.......................................

**more properties** and **statistics** extracted from an image
...............................................................

::
    
    `Histogram` or `Color map` extracted from an image can help with comparing of two images.

to create a **thumbnail** of an image and to add it into report
...............................................................

    User could view image in a report.

to **suggest** and implement useful tools for **Identification**, *Validation* and **Characterization**
........................................................................................................

::

  Great focus is on file format validations. We do not have many tools for JPEG 2000 and DjVu file format, 
  see http://djvu.sourceforge.net/doc/index.html.

**result of comparion** can be in **other** than **text format**
................................................................

::

    It could be great to have a comparison report in `html`.
    Or in `pdf`. Report can be done using JasperReport or Docbook or something similar.

to implement tests and to apply **Continuous Integration** practice
....................................................................
    
::

    Tests for significant properties processing. 
    Tests for xml export 
        - whether it contains all extracted significant properties that 
          are shown at text output.
    Tests for export XML reports to REST API.
    
    Apply continuous integration practice using site such as www.travis-ci.org.


required skills:

- Java
- XSLT
- JUnit testing

desirable skills:

- Spring framework
- experience with continuous integration

Improvements of **existing functionalities** and add new ones
-------------------------------------------------------------

significant properties **processing** is **slow**
.................................................

::
    
    There are a lot of properties that program must collects and normalizes.
    They are stored in ArrayList structure. It is often necessary to select given properties 
    by its own name.

    Implementing some kind of index by property name must speed up an aplication.
    At least ArrayHash by property name could help.
    
calling of **external java extractors** is **slow**
....................................................

::

    Metadata extractors that are written in java can be called in running JVM 
    calling some class methods. It must speed up an application a lot.

make a program distributed using **RabbitMQ** or **Apache ActiveMQ**
.....................................................................

::
    
    The intention is to run the external processes in a separate 
    JVM to be more robust and to offer standard integration pattern.

add a way to **exclude properties** that user does not need
...........................................................

::

    A user will choose properties that will be shown.

to improve an **iteration process** of **recognizing** new properties
.....................................................................
    
::

    It is difficult to observe all properties that metadata extractors offer.
    So it is possible that some of properties remain unrecognized and they are not never used.

    It would be great to offer some debug mode when an application shows all unused properties.
    At the end of an iteration (ie. configuration) an application will show no unused property.

Or something else. Each improvement is welcome.


requirements:

- Java
- XSLT
- JUnit testing

desirable skills:

- Spring framework
- experience with continuous integration
