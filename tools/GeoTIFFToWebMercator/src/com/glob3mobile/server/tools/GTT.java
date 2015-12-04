

package com.glob3mobile.server.tools;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.glob3mobile.utils.Logger;


public class GTT {


   @Option(
            name = "-dest-folder", //
            usage = "output to this folder", //
            metaVar = "OUTPUT-FOLDER", //
            required = true)
   private final String  _ouputDirectoryName = null;

   @Option(
            name = "-src-folder", //
            usage = "folder where are the tiffs", //
            metaVar = "INPUT-FOLDER")
   private final String  _inputDirectoryName = null;


   @Option(
            name = "-src-file", //
            usage = "make pyramid with a single file", //
            metaVar = "INPUT-FILE")
   private final String  _inputSingleFile    = null;

   @Option(
            name = "-r", //
            usage = "run recursively into the origin folder")
   private final boolean _recursive          = false;


   //   @Argument
   //   private final List<String> _arguments          = new ArrayList<String>();


   public static void main(final String[] args) throws IOException {
      //final String[] args2 = new String[] { "-src-folder", "pepe.png", "-dest-folder", "__dest__", "-r", "true" };
      //final String[] args2 = new String[] { "-src-file", "pepe.png", "-dest-folder", "__dest__" };
      new GTT().doMain(args);
   }


   private GTT() {

   }


   private void doMain(final String[] args) throws IOException {
      System.out.println("GTT 0.1");
      System.out.println("=======\n");

      final CmdLineParser parser = new CmdLineParser(this);

      try {
         parser.parseArgument(args);
      }
      catch (final CmdLineException e) {
         System.err.println(e.getMessage());
         System.err.println();
         System.err.println("java " + getClass().getName() + " [options...]");
         parser.printUsage(System.err);
         System.err.println();
         return;
      }

      if (_ouputDirectoryName == null) {
         throw new RuntimeException("You need to set an output folder to save the pyramids: Use the parameter -dest-folder");
      }

      if ((_inputDirectoryName == null) && (_inputSingleFile == null)) {
         throw new RuntimeException(
                  "You need to set the input file or input directory: Use the parameter -src-folder or -src-file ");
      }

      if (_inputSingleFile != null) {
         Logger.log("-src-file: " + _inputSingleFile + //
                    ", -dest-folder: \"" + _ouputDirectoryName + "\"");
         GeoTIFFTiler.convertFile(_inputSingleFile, _ouputDirectoryName);
      }
      else if (_inputDirectoryName != null) {
         Logger.log("-src-folder: \"" + _inputDirectoryName + "\"" + //
                    (_recursive ? " <recursive>" : " <non-recursive>") + //
                    ", -dest-folder: \"" + _ouputDirectoryName + "\"");
         GeoTIFFTiler.convertDirectory(_inputDirectoryName, _ouputDirectoryName, _recursive);
      }


   }


}
