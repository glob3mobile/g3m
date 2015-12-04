

package com.glob3mobile.server.tools;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class TM {

   @Option(
            name = "-src-folder", //
            usage = "folder where the xxx.tiles directories are", //
            metaVar = "INPUT-FOLDER", //
            required = true)
   private final String _inputDirectoryName = null;

   @Option(
            name = "-dest-folder", //
            usage = "output to this folder", //
            metaVar = "OUTPUT-FOLDER", //
            required = true)
   private final String _ouputDirectoryName = null;


   public static void main(final String[] args) throws IOException {
      //final String[] args2 = new String[] { "-dest-folder", "__dest__", "-src-folder", "source" };
      new TM().doMain(args);
   }


   private TM() {

   }


   private void doMain(final String[] args) throws IOException {
      System.out.println("TM 0.1");
      System.out.println("======\n");

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

      TilesMixer.processSubdirectories(_inputDirectoryName, _ouputDirectoryName);
   }


}
