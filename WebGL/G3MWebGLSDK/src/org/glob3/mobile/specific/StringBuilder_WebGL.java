
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.i18n.client.*;

public final class StringBuilder_WebGL extends IStringBuilder {

   // as iOS's ostringstream writes NaN
   private static final String NAN = "nan";

   private final StringBuilder _builder = new StringBuilder();
   private NumberFormat        _nf;

   public StringBuilder_WebGL(final int floatPrecision) {
      _nf = createNumberFormat(floatPrecision);
   }

   private static NumberFormat createNumberFormat(final int floatPrecision) {
      return NumberFormat.getFormat(createPattern(floatPrecision));
   }

   private static String createPattern(final int floatPrecision) {
      // "0" keeps the integer zero (0.5, not .5), as iOS does
      final StringBuilder numberPattern = new StringBuilder((floatPrecision <= 0) ? "0" : "0.");
      for (int i = 0; i < floatPrecision; i++) {
         numberPattern.append('#');
      }
      return numberPattern.toString();
   }

   @Override
   protected IStringBuilder clone(final int floatPrecision) {
      return new StringBuilder_WebGL(floatPrecision);
   }

   @Override
   public IStringBuilder addDouble(final double d) {
      _builder.append(Double.isNaN(d) ? NAN : _nf.format(d));
      return this;
   }

   @Override
   public IStringBuilder addFloat(final float f) {
      _builder.append(Float.isNaN(f) ? NAN : _nf.format(f));
      return this;
   }

   @Override
   public IStringBuilder addString(final String s) {
      _builder.append((s == null) ? "NULL" : s);
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
      _nf = createNumberFormat(floatPrecision);
      _builder.setLength(0);
      return this;
   }

}
