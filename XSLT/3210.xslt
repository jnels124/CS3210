<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
    <xsl:for-each select="rental_listings/property">
      <h2 align="center" margin-top="0" margin-bottom="0">
        <xsl:value-of select="address/@number"/>
          <xsl:text> </xsl:text>
          <xsl:value-of select="address/@street"/>
          <xsl:text>, </xsl:text>
          <xsl:value-of select="address/@unit"/>
          <xsl:text>&#10;</xsl:text>
          <xsl:value-of select="address/@city"/>
          <xsl:text>, </xsl:text>
          <xsl:value-of select="address/@state"/>
          <xsl:text> </xsl:text>
          <xsl:value-of select="address/@zip"/>
      </h2>
      <h2 margin-top="0" margin-bottom="0" align="center">
        <xsl:value-of select="address/@number"/>
          <xsl:text> </xsl:text>
          <xsl:value-of select="address/@street"/>
          <xsl:text>, </xsl:text>
          <xsl:value-of select="address/@unit"/>
          <xsl:text>&#10;</xsl:text>
          <xsl:value-of select="address/@city"/>
          <xsl:text>, </xsl:text>
          <xsl:value-of select="address/@state"/>
          <xsl:text> </xsl:text>
          <xsl:value-of select="address/@zip"/>
      </h2>
      <h3>Property Features:</h3>
      <table border="1">
        <tr>
          <th>Bedrooms</th>
          <th>Bathrooms</th>
          <th>Sqft</th>
          <th># Parking Spots</th>
          <th>Pets Allowed</th>
          <th>Washer/Dryer</th>
        </tr>
        <tr>
          <td><xsl:value-of select="description/@nbeds"/></td>
          <td><xsl:value-of select="description/@nbaths"/></td>
          <td><xsl:value-of select="description/@sqft"/></td>
          <td><xsl:value-of select="description/@nparking_spots"/></td>
          <td><xsl:value-of select="description/@pet"/></td>
          <td><xsl:value-of select="description/@washer_drier"/></td>
        </tr>
      </table>
      <label><b>Montly Rent: </b><xsl:value-of select="format-number(rent,'$#,##0.00')"/></label>
      <h3>Steps to Apply:</h3>
      <ol>
        <xsl:for-each select="application_process/step">
          <li><xsl:value-of select="."/></li>
      </xsl:for-each>
      </ol>
      <h3>Comments</h3>
      <xsl:value-of select="comments"/>
    </xsl:for-each>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>