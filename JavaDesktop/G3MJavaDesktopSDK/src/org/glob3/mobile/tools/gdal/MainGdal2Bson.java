

package org.glob3.mobile.tools.gdal;

import java.io.File;

import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.tools.commandline.core.CommandLineException;
import org.glob3.mobile.tools.conversion.jbson2bjson.JBson2BJsonException;


public class MainGdal2Bson {
   /**
    * @param args
    */
   public static void main(final String[] args) {
      System.out.println("GDAL Converter to Bson 0.1");
      System.out.println("----------------------------- " + args.length + "\n");

      if (args.length != 3) {
         System.out.println("Arguments must be properly specified.");
         System.exit(1);
      }

      // Inicializando
      final String gdalHome = args[0];
      final File inputFile = new File(args[1]);
      final String outputFileName = new String(args[2]);

      try {
         GDAL.initialize(gdalHome);
         GeoBSONConverter.instance().convert(inputFile, inputFile.getParentFile(), outputFileName, true);
      }
      catch (final GDALException e) {
         ILogger.instance().logError(e.getMessage(), e.getCause());
      }
      catch (final JBson2BJsonException e) {
         ILogger.instance().logError(e.getMessage(), e.getCause());
      }
      catch (final CommandLineException e) {
         ILogger.instance().logError(e.getMessage(), e.getCause());
      }
   }
}
