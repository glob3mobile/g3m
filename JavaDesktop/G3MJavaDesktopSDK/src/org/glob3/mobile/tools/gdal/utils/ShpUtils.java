

package org.glob3.mobile.tools.gdal.utils;

import java.io.File;

import org.glob3.mobile.tools.gdal.core.GDAL;
import org.glob3.mobile.tools.gdal.exception.GDALException;


public class ShpUtils {
   /**
    * Mandatory files :
    * 
    * .shp — shape format; the feature geometry itself
    * 
    * .shx — shape index format; a positional index of the feature geometry to allow seeking forwards and backwards quickly
    * 
    * .dbf — attribute format; columnar attributes for each shape, in dBase IV format
    * 
    * Optional files :
    * 
    * .prj — projection format; the coordinate system and projection information, a plain text file describing the projection
    * using well-known text format
    * 
    * .sbn and .sbx — a spatial index of the features
    * 
    * .fbn and .fbx — a spatial index of the features for shapefiles that are read-only
    * 
    * .ain and .aih — an attribute index of the active fields in a table
    * 
    * .ixs — a geocoding index for read-write shapefiles
    * 
    * .mxs — a geocoding index for read-write shapefiles (ODB format)
    * 
    * .atx — an attribute index for the .dbf file in the form of shapefile.columnname.atx (ArcGIS 8 and later)
    * 
    * .shp.xml — geospatial metadata in XML format, such as ISO 19115 or other XML schema
    * 
    * .cpg — used to specify the code page (only for .dbf) for identifying the character encoding to be used
    * 
    * @param parentFile
    * @param fileNameWithoutExtension
    * @return
    * @throws GDALException
    */
   public static boolean checkShpDir(final File parentFile,
                                     final String fileNameWithoutExtension) throws GDALException {
      final File shpFile = new File(parentFile.getAbsoluteFile() + "/" + fileNameWithoutExtension + ".shp");
      if (shpFile.exists()) {
         if (new File(parentFile.getAbsoluteFile() + "/" + fileNameWithoutExtension + ".shx").exists()) {
            if (new File(parentFile.getAbsoluteFile() + "/" + fileNameWithoutExtension + ".dbf").exists()) {
               if (GDAL.instance().validateSRS(shpFile)) {
                  return true;
               }
               throw new GDALException("Not valid SRS", null);
            }
            throw new GDALException("File .dbf is mandatory", null);
         }
         throw new GDALException("File .shx is mandatory", null);
      }
      throw new GDALException("File .shp is mandatory", null);
   }
}
