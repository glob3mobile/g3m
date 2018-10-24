

package org.glob3.mobile.specific;

import java.text.DecimalFormat;

import org.glob3.mobile.generated.IStringBuilder;


public final class StringBuilder_JavaDesktop
         extends
            IStringBuilder {

   private final StringBuilder _builder = new StringBuilder();
   private final DecimalFormat _df      = new DecimalFormat("0");


   public StringBuilder_JavaDesktop(final int floatPrecision) {
      _df.setMaximumFractionDigits(floatPrecision);
   }


   @Override
   protected IStringBuilder clone(final int floatPrecision) {
      return new StringBuilder_JavaDesktop(floatPrecision);
   }


   @Override
   public IStringBuilder addDouble(final double d) {
      _builder.append(_df.format(d));
      return this;
   }


   @Override
   public IStringBuilder addFloat(final float f) {
      _builder.append(_df.format(f));
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
   public boolean contentEqualsTo(final String that) {
      return (_builder.length() == that.length()) && (_builder.indexOf(that) == 0);
   }


   @Override
   public IStringBuilder clear(final int floatPrecision) {
      _df.setMaximumFractionDigits(floatPrecision);
      _builder.setLength(0);
      return this;
   }


}
