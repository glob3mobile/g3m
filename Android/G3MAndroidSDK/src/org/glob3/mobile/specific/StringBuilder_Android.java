

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IStringBuilder;


public class StringBuilder_Android
         extends
            IStringBuilder {

   private final StringBuilder _builder = new StringBuilder();


   @Override
   protected IStringBuilder getNewInstance() {
      return new StringBuilder_Android();
   }


   @Override
   public IStringBuilder add(final double d) {
      _builder.append(d);
      return this;
   }


   @Override
   public IStringBuilder add(final String s) {
      _builder.append(s);
      return this;
   }


   @Override
   public IStringBuilder addBool(final boolean b) {
      _builder.append(b);
      return this;
   }


   @Override
   public String getString() {
      return _builder.toString();
   }

}
