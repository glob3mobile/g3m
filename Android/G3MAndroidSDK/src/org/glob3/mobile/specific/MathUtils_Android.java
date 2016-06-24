

package org.glob3.mobile.specific;

import java.util.Random;

import org.glob3.mobile.generated.IMathUtils;


public final class MathUtils_Android
extends
IMathUtils {

   private final Random _random = new Random();


   @Override
   public double sin(final double v) {
      return Math.sin(v);
   }


   @Override
   public float sin(final float v) {
      //      return FloatMath.sin(v);
      return (float) Math.sin(v);
   }


   @Override
   public double asin(final double v) {
      return Math.asin(v);
   }


   @Override
   public float asin(final float v) {
      return (float) Math.asin(v);
   }


   @Override
   public double cos(final double v) {
      return Math.cos(v);
   }


   @Override
   public float cos(final float v) {
      //      return FloatMath.cos(v);
      return (float) Math.cos(v);
   }


   @Override
   public double acos(final double v) {
      return Math.acos(v);
   }


   @Override
   public float acos(final float v) {
      return (float) Math.acos(v);
   }


   @Override
   public double tan(final double v) {
      return Math.tan(v);
   }


   @Override
   public float tan(final float v) {
      return (float) Math.tan(v);
   }


   @Override
   public double atan(final double v) {
      return Math.atan(v);
   }


   @Override
   public float atan(final float v) {
      return (float) Math.atan(v);
   }


   @Override
   public double atan2(final double u,
                       final double v) {
      return Math.atan2(u, v);
   }


   @Override
   public float atan2(final float u,
                      final float v) {
      return (float) Math.atan2(u, v);
   }


   @Override
   public long round(final double v) {
      return Math.round(v);
   }


   @Override
   public int round(final float v) {
      return Math.round(v);
   }


   @Override
   public int abs(final int v) {
      return Math.abs(v);
   }


   @Override
   public double abs(final double v) {
      return Math.abs(v);
   }


   @Override
   public float abs(final float v) {
      return Math.abs(v);
   }


   @Override
   public double sqrt(final double v) {
      return Math.sqrt(v);
   }


   @Override
   public float sqrt(final float v) {
      //      return FloatMath.sqrt(v);
      return (float) Math.sqrt(v);
   }


   @Override
   public double pow(final double v,
                     final double u) {
      return Math.pow(v, u);
   }


   @Override
   public float pow(final float v,
                    final float u) {
      return (float) Math.pow(v, u);
   }


   @Override
   public double exp(final double v) {
      return Math.exp(v);
   }


   @Override
   public float exp(final float v) {
      return (float) Math.exp(v);
   }


   @Override
   public double log10(final double v) {
      return Math.log10(v);
   }


   @Override
   public float log10(final float v) {
      return (float) Math.log10(v);
   }


   @Override
   public int maxInt32() {
      return Integer.MAX_VALUE;
   }


   @Override
   public int minInt32() {
      return Integer.MIN_VALUE;
   }


   @Override
   public double maxDouble() {
      return Double.MAX_VALUE;
   }


   @Override
   public double minDouble() {
      return -Double.MAX_VALUE;
   }


   @Override
   public float maxFloat() {
      return Float.MAX_VALUE;
   }


   @Override
   public float minFloat() {
      return -Float.MAX_VALUE;
   }


   @Override
   public double log(final double v) {
      return Math.log(v);
   }


   @Override
   public float log(final float v) {
      return (float) Math.log(v);
   }


   @Override
   public int toInt(final double value) {
      return (int) value;
   }


   @Override
   public int toInt(final float value) {
      return (int) value;
   }


   @Override
   public double min(final double d1,
                     final double d2) {
      return (d1 < d2) ? d1 : d2;
   }


   @Override
   public double max(final double d1,
                     final double d2) {
      return (d1 > d2) ? d1 : d2;
   }


   @Override
   public long maxInt64() {
      return Long.MAX_VALUE;
   }


   @Override
   public long minInt64() {
      return Long.MIN_VALUE;
   }


   @Override
   public long doubleToRawLongBits(final double value) {
      return Double.doubleToRawLongBits(value);
   }


   @Override
   public double rawLongBitsToDouble(final long value) {
      return Double.longBitsToDouble(value);
   }


   @Override
   public float rawIntBitsToFloat(final int value) {
      return Float.intBitsToFloat(value);
   }


   @Override
   public double sinh(final double v) {
      return Math.sinh(v);
   }


   @Override
   public float sinh(final float v) {
      return (float) Math.sinh(v);
   }


   @Override
   public double floor(final double d) {
      return Math.floor(d);
   }


   @Override
   public float floor(final float f) {
      return (float) Math.floor(f);
   }


   @Override
   public float min(final float f1,
                    final float f2) {
      return Math.min(f1, f2);
   }


   @Override
   public float max(final float f1,
                    final float f2) {
      return Math.max(f1, f2);
   }


   @Override
   public int max(final int i1,
                  final int i2) {
      return Math.max(i1, i2);
   }


   @Override
   public long max(final long l1,
                   final long l2) {
      return Math.max(l1, l2);
   }


   @Override
   public short maxInt16() {
      return Short.MAX_VALUE;
   }


   @Override
   public short minInt16() {
      return Short.MIN_VALUE;
   }


   @Override
   public double ceil(final double d) {
      return Math.ceil(d);
   }


   @Override
   public float ceil(final float f) {
      return (float) Math.ceil(f);
   }


   @Override
   public double fmod(final double d1,
                      final double d2) {
      return d1 % d2;
   }


   @Override
   public float fmod(final float f1,
                     final float f2) {
      return f1 % f2;
   }


   @Override
   public double nextRandomDouble() {
      return _random.nextDouble();
   }


}
