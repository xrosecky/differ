Things to improve
-----------------

- significant properties processing is slow ::
    
    There are a lot of properties that program must collects and normalizes.
    They are stored in ArrayList structure. It is necessary to select given properties 
    by its own name.

    Implementing some kind of index by property name must speed up an aplication.
    At least ArrayHash by property name could help.
    
- calling of external java extractors is slow ::

    Metadata extractors that are written in java can be called in running JVM 
    calling some class methods. It must speed up an application a lot.

- result of comparion can be in other than text format ::

    It could be great to have comparison report in `html`.
    Or in `pdf`.
    
- more properties and statistics extracted from an image ::
    
    `Histogram` or `Color map` of an image can help with comparing of two images.

- create a thumbnail of an image and add it into report ::

    User could view image in a report.

- make a program distributed using ``RabbitMQ``, ``Apache ActiveMQ`` ::
    
    Mainly due to slow external metadata extractors.

- add more metadata extractors ::

    Each program than identifies some properties of an image is welcome.

- add a way to exclude properties than user does not need ::

    A user will choose properties that will be shown.

- improve an iteration process of recognizing new properties ::
    
    It is difficult to observe all properties that metadata extractors offer.
    So it is posible that some of properties remain unrecognized and they are not never used.

    It would be great to offer some debug mode when an application shows all unused properties.
    At the end of an iteration an application will show no unused property.

    Or something else. Each improvement is welcome.

- add more metadata extractors for djvudump ::

    There are more binaries than understand djvu format. See http://djvu.sourceforge.net/doc/index.html.
    Is there some program that recognizes interesting properties of this image format?
  
