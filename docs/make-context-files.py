#!/usr/bin/python
import os
import csv
from collections import namedtuple
from itertools import imap

DOCS_DIR = os.path.dirname(os.path.abspath(__file__))
BASE_DIR = os.path.dirname(DOCS_DIR)

Row = namedtuple("Row",['property_name', 'system_name', 'expression'])

# jpylyzer
print """<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:jhove="http://hul.harvard.edu/ois/xml/ns/jhove"
  xmlns:mix="http://www.loc.gov/mix/v20"
  exclude-result-prefixes="jhove mix"
  >

  <xsl:output method="xml" indent="yes" encoding="iso-8859-1"/>

  <xsl:template match="text()"/>

  <xsl:template match="/jpylyzer">
      <properties>"""

for row in imap(Row._make, csv.reader(open(os.path.join(DOCS_DIR,"jpylyzer.csv"),"rb"),delimiter=";", quotechar='"')):
    print """      <property name = "%s"><xsl:value-of select="%s"/></property>""" % ( row.property_name, row.expression )

print """   </properties>  
   </xsl:template>
</xsl:stylesheet>"""
