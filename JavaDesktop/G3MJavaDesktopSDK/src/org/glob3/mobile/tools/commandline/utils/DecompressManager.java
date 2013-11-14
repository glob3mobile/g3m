

package org.glob3.mobile.tools.commandline.utils;

import java.io.File;
import java.io.IOException;

import org.glob3.mobile.tools.commandline.core.CommandLine;
import org.glob3.mobile.tools.commandline.core.CommandLine.StreamGobbler;
import org.glob3.mobile.tools.commandline.exception.CommandLineException;
import org.glob3.mobile.tools.gdal.exception.GDALException;
import org.glob3.mobile.tools.utils.FileUtils;


public class DecompressManager {

   private static final String TAR_COMMAND = "tar";
   public final static String  ZIP_EXT     = ".zip";


   public static boolean checkIfExistCommand(final String command) throws CommandLineException {

      final String[] cmd = new String[3];
      cmd[0] = "bash";
      cmd[1] = "type";
      cmd[2] = command;

      StreamGobbler sb = null;
      try {
         sb = CommandLine.getInstance().execute(cmd);
         System.out.println(sb.getResult());
         if (!sb.getResult().contains("not found")) {
            return true;
         }

      }
      catch (final IOException e) {
         throw new CommandLineException(e.getMessage(), e.getCause());
      }
      catch (final InterruptedException e) {
         throw new CommandLineException(e.getMessage(), e.getCause());
      }
      finally {
         if (sb != null) {
            sb.destroy();
         }
      }

      return false;
   }


   //tar -x -f /Users/vidalete/Downloads/fuentes2.zip -C /Users/vidalete/Downloads/fuentes2
   public static File unzipFile(final File zipFile) throws CommandLineException, GDALException {
      final File decompressDir = new File(zipFile.getParentFile(), FileUtils.getFileNameWithoutExtension(zipFile.getName()));
      if (!decompressDir.exists()) {
         decompressDir.mkdirs();
      }
      if (FileUtils.checkFile(zipFile) && checkIfExistCommand(TAR_COMMAND)) {
         final String[] cmd = new String[6];
         cmd[0] = "tar";
         cmd[1] = "-x";
         cmd[2] = "-f";
         cmd[3] = zipFile.getAbsolutePath();
         cmd[4] = "-C";
         cmd[5] = decompressDir.getAbsolutePath();

         System.out.println(decompressDir.getAbsolutePath());
         StreamGobbler sb = null;
         try {
            sb = CommandLine.getInstance().execute(cmd);
            return decompressDir;
         }
         catch (final IOException e) {
            if (decompressDir.exists()) {
               FileUtils.deleteDirectory(decompressDir);
            }
            throw new CommandLineException(e.getMessage(), e.getCause());
         }
         catch (final InterruptedException e) {
            if (decompressDir.exists()) {
               FileUtils.deleteDirectory(decompressDir);
            }
            throw new CommandLineException(e.getMessage(), e.getCause());
         }
         finally {
            if (sb != null) {
               sb.destroy();
            }
         }
      }

      return null;
   }
}
