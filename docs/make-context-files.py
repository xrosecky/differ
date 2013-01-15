#!/usr/bin/python
import os
import csv
from collections import namedtuple
from itertools import imap,ifilter

DOCS_DIR = os.path.dirname(os.path.abspath(__file__))
BASE_DIR = os.path.dirname(DOCS_DIR)

Row = namedtuple("Row",['property_name', 'system_name', 'expression'])

with open(os.path.join(BASE_DIR,"differ-cmdline","jpylyzer.xsl.new"),"w") as out:
    # jpylyzer
    out.write("""<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:jhove="http://hul.harvard.edu/ois/xml/ns/jhove"
  xmlns:mix="http://www.loc.gov/mix/v20"
  exclude-result-prefixes="jhove mix"
  >

  <xsl:output method="xml" indent="yes" encoding="iso-8859-1"/>

  <xsl:template match="text()"/>

  <xsl:template match="/jpylyzer">
      <properties>\n""")

    # lambda is used to filter out rows without expression
    for row in ifilter(lambda row: row.expression, imap(Row._make, csv.reader(open(os.path.join(DOCS_DIR,"jpylyzer.csv"),"rb"),delimiter=";", quotechar='"'))):
        out.write("""      <property name = "%s"><xsl:value-of select="%s"/></property>\n""" % ( row.property_name, row.expression ))
        
    out.write("""   </properties>  
   </xsl:template>
</xsl:stylesheet>""")


# with open(os.path.join(BASE_DIR, "differ-cmdline","jhove.xsl.new"),"w") as out:
#     # jhove
#     out.write("""<xsl:stylesheet
#   version="1.0"
#   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
#   xmlns:jhove="http://hul.harvard.edu/ois/xml/ns/jhove"
#   xmlns:mix="http://www.loc.gov/mix/v20"
#   exclude-result-prefixes="jhove mix"
#   >

#   <xsl:output method="xml" indent="yes" encoding="iso-8859-1"/>

#   <xsl:template match="text()"/>

#   <xsl:template match="/jhove">
#       <properties>\n""")

#     for row in ifilter(lambda row: row.expression, imap(Row._make, csv.reader(open(os.path.join(DOCS_DIR,"jhove.csv"),"rb"),delimiter=";", quotechar='"'))):
#         out.write("""      <property name = "%s"><xsl:value-of select="%s"/></property>\n""" % ( row.property_name, row.expression ))

#     out.write("""   </properties>  
#    </xsl:template>
# </xsl:stylesheet>"""
#               )
