
package org.glob3.mobile.specific;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.glob3.mobile.generated.IStringBuilder;

public final class StringBuilder_JavaDesktop extends IStringBuilder {

   // as iOS's ostringstream writes NaN
   private static final String NAN = "nan";
   // NumberFormat's infinity
   private static final String INFINITY = "∞";

   private static final MathContext TO_PRECISION_20 = new MathContext(20, RoundingMode.HALF_UP);

   // As StringBuilder_WebGL writes numbers: GWT's NumberFormat with pattern "0" and
   // floatPrecision maximum fraction digits (com.google.gwt.i18n.client.NumberFormat 2.13.1).
   private static String format(final double d, final int maxDecimals) {
      if (Double.isNaN(d)) {
         return NAN;
      }
      final boolean negative = (d < 0) || ((d == 0) && ((1 / d) < 0));
      final String sign = negative ? "-" : "";
      final double absolute = Math.abs(d);
      if (Double.isInfinite(absolute)) {
         return sign + INFINITY;
      }

      // NumberFormat.toScaledString: JavaScript's toPrecision(20), value = digits * 10^scale
      final StringBuilder digits = new StringBuilder();
      int scale;
      if (absolute == 0) {
         for (int i = 0; i < 20; i++) {
            digits.append('0');
         }
         scale = -19;
      }
      else {
         final BigDecimal rounded = new BigDecimal(absolute).round(TO_PRECISION_20);
         digits.append(rounded.unscaledValue().toString());
         scale = -rounded.scale();
         // an exact value with fewer than 20 significant digits keeps fewer
         while (digits.length() < 20) {
            digits.append('0');
            scale--;
         }
      }

      // NumberFormat.format(double): pre-rounds .1499999... at three digits past the decimals
      final int preRound = digits.length() + scale + maxDecimals + 3;
      if ((preRound > 0) && (preRound < digits.length()) && (digits.charAt(preRound) == '9')) {
         propagateCarry(digits, preRound - 1);
         scale += digits.length() - preRound;
         digits.setLength(preRound);
      }

      // NumberFormat.processLeadingZeros, with one minimum integer digit
      int decimalPosition = digits.length() + scale;
      while (digits.length() < decimalPosition) {
         digits.append('0');
      }
      if (decimalPosition < 1) {
         while (decimalPosition < 1) {
            digits.insert(0, '0');
            decimalPosition++;
         }
      }
      else if (decimalPosition > 1) {
         int strip = 0;
         while ((strip < (decimalPosition - 1)) && (digits.charAt(strip) == '0')) {
            strip++;
         }
         digits.delete(0, strip);
         decimalPosition -= strip;
      }

      // NumberFormat.roundValue: half up
      final int roundPosition = decimalPosition + maxDecimals;
      if ((digits.length() > roundPosition) && (digits.charAt(roundPosition) >= '5')) {
         if (propagateCarry(digits, roundPosition - 1)) {
            decimalPosition++;
         }
      }

      // NumberFormat.adjustFractionDigits, with no minimum fraction digits
      int keep = Math.min(decimalPosition + maxDecimals, digits.length());
      while ((keep > decimalPosition) && (digits.charAt(keep - 1) == '0')) {
         keep--;
      }
      digits.setLength(keep);

      if (decimalPosition < digits.length()) {
         digits.insert(decimalPosition, '.');
      }
      return sign + digits;
   }

   // NumberFormat.propagateCarry: adds one at index i; returns true when it adds a leading 1
   private static boolean propagateCarry(final StringBuilder digits, int i) {
      while (i >= 0) {
         final char digit = digits.charAt(i);
         if (digit != '9') {
            digits.setCharAt(i, (char) (digit + 1));
            return false;
         }
         digits.setCharAt(i, '0');
         i--;
      }
      digits.insert(0, '1');
      return true;
   }

   private final StringBuilder _builder = new StringBuilder();
   private int                 _floatPrecision;

   public StringBuilder_JavaDesktop(final int floatPrecision) {
      _floatPrecision = floatPrecision;
   }

   @Override
   protected IStringBuilder clone(final int floatPrecision) {
      return new StringBuilder_JavaDesktop(floatPrecision);
   }

   @Override
   public IStringBuilder addDouble(final double d) {
      _builder.append(format(d, _floatPrecision));
      return this;
   }

   @Override
   public IStringBuilder addFloat(final float f) {
      _builder.append(format(f, _floatPrecision));
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
      _floatPrecision = floatPrecision;
      _builder.setLength(0);
      return this;
   }

}
