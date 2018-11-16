

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IStringBuilder;

import com.google.gwt.i18n.client.NumberFormat;


public final class StringBuilder_WebGL
         extends
            IStringBuilder {

   private final StringBuilder _builder = new StringBuilder();
   private NumberFormat        _nf;


   public StringBuilder_WebGL(final int floatPrecision) {
      createFormatter(floatPrecision);
   }


   private void createFormatter(final int floatPrecision) {
      _nf = NumberFormat.getFormat(createPattern(floatPrecision));
   }


   private static String createPattern(final int floatPrecision) {
      final StringBuilder numberPattern = new StringBuilder((floatPrecision <= 0) ? "" : ".");
      for (int i = 0; i < floatPrecision; i++) {
         numberPattern.append('0');
      }
      final String pattern = numberPattern.toString();
      return pattern;
   }


   @Override
   protected IStringBuilder clone(final int floatPrecision) {
      return new StringBuilder_WebGL(floatPrecision);
   }


   @Override
   public IStringBuilder addDouble(final double d) {
      _builder.append(_nf.format(d));
      return this;
   }


   @Override
   public IStringBuilder addFloat(final float f) {
      _builder.append(_nf.format(f));
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
      return getString().equals(that);
   }


   @Override
   public IStringBuilder clear(final int floatPrecision) {
      createFormatter(floatPrecision);
      _builder.setLength(0);
      return this;
   }


}
