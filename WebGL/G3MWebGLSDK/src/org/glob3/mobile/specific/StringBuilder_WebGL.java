

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IStringBuilder;


public final class StringBuilder_WebGL
         extends
            IStringBuilder {

   private String _string = "";


   @Override
   protected IStringBuilder getNewInstance() {
      return new StringBuilder_WebGL();
   }


   @Override
   public IStringBuilder addDouble(final double d) {
      _string += d;
      return this;
   }


   @Override
   public IStringBuilder addString(final String s) {
      _string += s;
      return this;
   }


   @Override
   public IStringBuilder addBool(final boolean b) {
      _string += b;
      return this;
   }


   @Override
   public String getString() {
      return _string;
   }


   @Override
   public IStringBuilder addFloat(final float f) {
      _string += f;
      return this;
   }


   @Override
   public IStringBuilder addInt(final int i) {
      _string += i;
      return this;
   }


   @Override
   public IStringBuilder addLong(final long l) {
      _string += l;
      return this;
   }


   @Override
   public IStringBuilder clear() {
      _string = "";
      return this;
   }


   @Override
   public boolean contentEqualsTo(final String that) {
      return _string.equals(that);
   }

}
