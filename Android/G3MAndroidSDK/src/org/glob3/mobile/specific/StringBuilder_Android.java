

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IStringBuilder;


public final class StringBuilder_Android
         extends
            IStringBuilder {

   private final StringBuilder _builder = new StringBuilder();


   @Override
   protected IStringBuilder getNewInstance() {
      return new StringBuilder_Android();
   }


   @Override
   public IStringBuilder addDouble(final double d) {
      _builder.append(d);
      return this;
   }


   @Override
   public IStringBuilder addString(final String s) {
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


   @Override
   public IStringBuilder addFloat(final float f) {
      _builder.append(f);
      return this;
   }


   @Override
   public IStringBuilder addInt(final int i) {
      _builder.append(i);
      return this;
   }


   @Override
   public IStringBuilder addLong(final long l) {
      _builder.append(l);
      return this;
   }


   @Override
   public IStringBuilder clear() {
      _builder.setLength(0);
      return this;
   }


   @Override
   public boolean contentEqualsTo(final String that) {
      return (_builder.length() == that.length()) && (_builder.indexOf(that) == 0);
   }

}
