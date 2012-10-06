

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IStringBuilder;


public final class StringBuilder_WebGL
         extends
            IStringBuilder {

   String res = "";


   @Override
   protected IStringBuilder getNewInstance() {
      return new StringBuilder_WebGL();
   }


   @Override
   public IStringBuilder add(final double d) {
      res += d;
      return this;
   }


   @Override
   public IStringBuilder add(final String s) {
      res += s;
      return this;
   }


   @Override
   public IStringBuilder addBool(final boolean b) {
      res += b;
      return this;
   }


   @Override
   public String getString() {
      return res;
   }

}
