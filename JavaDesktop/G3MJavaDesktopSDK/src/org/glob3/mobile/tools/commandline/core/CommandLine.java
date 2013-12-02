

package org.glob3.mobile.tools.commandline.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.glob3.mobile.tools.commandline.core.CommandLine.ExecResult.ExecResultType;


public class CommandLine {

   private static CommandLine _commandLineUtils = null;


   public synchronized static CommandLine getInstance() {
      if (_commandLineUtils == null) {
         _commandLineUtils = new CommandLine();
      }

      return _commandLineUtils;
   }


   public void showResult(final String result) {
      StreamGobbler sg = null;
      try {
         final StringBuffer cmd = new StringBuffer("echo \"");
         cmd.append(result);
         cmd.append("\"");
         sg = execute(cmd.toString());
      }
      catch (final IOException e) {
         e.printStackTrace();
      }
      catch (final InterruptedException e) {
         e.printStackTrace();
      }
      finally {
         if (sg != null) {
            sg.destroy();
         }
      }
   }


   public ExecResult exec(final String... cmd) throws IOException, InterruptedException {
      final Process p = Runtime.getRuntime().exec(cmd);
      final int exitVal = p.waitFor();


      final ExecResult execResult;
      final InputStreamReader isr;
      if (exitVal == 0) {
         isr = new InputStreamReader(p.getInputStream());
         execResult = new ExecResult(ExecResultType.OUTPUT, new BufferedReader(isr));
      }
      else {
         isr = new InputStreamReader(p.getErrorStream());
         execResult = new ExecResult(ExecResultType.ERROR, new BufferedReader(isr));
      }


      isr.close();
      p.destroy();
      return execResult;
   }


   public StreamGobbler execute(final String... cmd) throws IOException, InterruptedException {
      final Process p = Runtime.getRuntime().exec(cmd);

      // any error message?
      final InputStreamReader isrError = new InputStreamReader(p.getErrorStream());
      final StreamGobbler errorGobbler = new StreamGobbler(new BufferedReader(isrError), StreamGobbler.streamGobblerType.ERROR);

      // any output?
      final InputStreamReader isrOutput = new InputStreamReader(p.getInputStream());
      final StreamGobbler outputGobbler = new StreamGobbler(new BufferedReader(isrOutput), StreamGobbler.streamGobblerType.OUTPUT);

      // kick them off
      errorGobbler.start();
      outputGobbler.start();


      final int exitVal = p.waitFor();

      System.out.println("Destroying Process");
      p.destroy();


      if (exitVal == 0) {
         isrError.close();
         errorGobbler.destroy();

         return outputGobbler;
      }
      isrOutput.close();
      outputGobbler.destroy();

      return errorGobbler;
   }


   public StreamGobbler execute(final String[] cmd,
                                final String[] envp,
                                final File dir) throws IOException, InterruptedException {
      final Process p = Runtime.getRuntime().exec(cmd, envp, dir);


      // any error message?
      final InputStreamReader isrError = new InputStreamReader(p.getErrorStream());
      final StreamGobbler errorGobbler = new StreamGobbler(new BufferedReader(isrError), StreamGobbler.streamGobblerType.ERROR);

      // any output?
      final InputStreamReader isrOutput = new InputStreamReader(p.getInputStream());
      final StreamGobbler outputGobbler = new StreamGobbler(new BufferedReader(isrOutput), StreamGobbler.streamGobblerType.OUTPUT);

      // kick them off
      errorGobbler.start();
      outputGobbler.start();

      final int exitVal = p.waitFor();
      p.destroy();


      if (exitVal == 0) {
         isrError.close();
         errorGobbler.destroy();
         return outputGobbler;
      }
      isrOutput.close();
      outputGobbler.destroy();
      return errorGobbler;
   }


   public static class ExecResult {
      public enum ExecResultType {
         ERROR,
         OUTPUT;
      }

      final ExecResultType _type;
      final StringBuffer   _result = new StringBuffer();


      private ExecResult(final ExecResultType type,
                         final BufferedReader br) throws IOException {
         super();
         _type = type;

         String line = null;
         while (((line = br.readLine()) != null)) {
            _result.append(line);
         }
         br.close();
      }


      /**
       * @return the type
       */
      public ExecResultType getType() {
         return _type;
      }


      /**
       * @return the result
       */
      public String getResult() {
         return _result.toString();
      }


   }

   public static class StreamGobbler
            extends
               Thread {

      public enum streamGobblerType {
         ERROR,
         OUTPUT;
      }

      final BufferedReader    _br;
      final streamGobblerType _type;
      final StringBuilder     _sb = new StringBuilder();


      public StreamGobbler(final BufferedReader br,
                           final streamGobblerType type) {
         _br = br;
         _type = type;
      }


      @Override
      public void run() {
         try {
            String line = null;
            while (((line = _br.readLine()) != null)) {
               _sb.append(line);
               //               System.out.println(_type + ">" + line);
            }
            _br.close();
         }
         catch (final IOException e) {
            System.out.println("Error: " + e.getMessage() + "; " + e.getCause());
         }
      }


      @Override
      final public void destroy() {
         try {
            _br.close();
         }
         catch (final IOException e) {
            System.out.println("Error: " + e.getMessage() + "; " + e.getCause());
         }
         if (!interrupted()) {
            interrupt();
         }
      }


      final public String getResult() {
         return _sb.toString();
      }


      final public streamGobblerType getType() {
         return _type;
      }


      final public String getTypeName() {
         return _type.name();
      }
   }
}
