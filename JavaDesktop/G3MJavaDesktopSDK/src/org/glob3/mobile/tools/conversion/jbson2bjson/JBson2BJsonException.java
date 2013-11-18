

package org.glob3.mobile.tools.conversion.jbson2bjson;

public class JBson2BJsonException
         extends
            Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;


   public JBson2BJsonException(final String message) {
      super(message);
   }


   public JBson2BJsonException(final String message,
                               final Throwable cause) {
      super(message, cause);
   }


   @Override
   public String getMessage() {
      return super.getMessage() + ". Cause By: " + ((super.getCause() != null) ? super.getCause() : "");
   }
}
