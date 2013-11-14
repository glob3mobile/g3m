

package org.glob3.mobile.tools.commandline.exception;

public class CommandLineException
         extends
            Exception {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;


   public CommandLineException(final String message,
                        final Throwable cause) {
      super(message, cause);
   }


   @Override
   public String getMessage() {
      return super.getMessage() + ". Cause By: " + ((super.getCause() != null) ? super.getCause() : "");
   }
}
