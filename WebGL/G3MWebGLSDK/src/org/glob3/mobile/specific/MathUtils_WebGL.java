

package org.glob3.mobile.specific;

import java.util.Random;

import org.glob3.mobile.generated.IMathUtils;


public final class MathUtils_WebGL
extends
IMathUtils {

   private final Random _random = new Random();


   @Override
   public double sin(final double v) {
      return Math.sin(v);
   }


   @Override
   public float sin(final float v) {
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
      return (float) Math.cos(v);
   }


   @Override
   public double acos(final double v) {
      return Math.acos(v);
   }


   @Override
   public float acos(final float v) {
      return (float) Math.cos(v);
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
      final byte[] byteArray = doubleToByteArray(value);
      // LittleEndian

      final int b1 = byteArray[0] & 0xFF;
      final int b2 = byteArray[1] & 0xFF;
      final int b3 = byteArray[2] & 0xFF;
      final int b4 = byteArray[3] & 0xFF;
      final int b5 = byteArray[4] & 0xFF;
      final int b6 = byteArray[5] & 0xFF;
      final int b7 = byteArray[6] & 0xFF;
      final int b8 = byteArray[7] & 0xFF;

      return (b1) | ((long) b2 << 8) | ((long) b3 << 16) | ((long) b4 << 24) | ((long) b5 << 32) | ((long) b6 << 40)
               | ((long) b7 << 48) | ((long) b8 << 56);
   }


   private native byte[] doubleToByteArray(final double value) /*-{
        var buffer = new ArrayBuffer(8);
        var doubleView = new Float64Array(buffer);
        doubleView[0] = value;

        return (new Uint8Array(buffer));
   }-*/;


   @Override
   public double rawLongBitsToDouble(final long value) {
      final int[] byteArray = new int[8];

      byteArray[0] = (int) (value & 0xFF);
      byteArray[1] = (int) ((value >> 8) & 0xFF);
      byteArray[2] = (int) ((value >> 16) & 0xFF);
      byteArray[3] = (int) ((value >> 24) & 0xFF);
      byteArray[4] = (int) ((value >> 32) & 0xFF);
      byteArray[5] = (int) ((value >> 40) & 0xFF);
      byteArray[6] = (int) ((value >> 48) & 0xFF);
      byteArray[7] = (int) ((value >> 56) & 0xFF);

      return byteArrayToDouble(byteArray);
   }


   private native double byteArrayToDouble(final int[] byteArray) /*-{
        var uint8Array = new Uint8Array(byteArray);
        var doubleView = new Float64Array(uint8Array.buffer);

        return doubleView[0];
   }-*/;


   @Override
   public float rawIntBitsToFloat(final int value) {
      final int[] byteArray = new int[4];

      byteArray[0] = value & 0xFF;
      byteArray[1] = (value >> 8) & 0xFF;
      byteArray[2] = (value >> 16) & 0xFF;
      byteArray[3] = (value >> 24) & 0xFF;

      return byteArrayToFloat(byteArray);
   }


   private native float byteArrayToFloat(final int[] byteArray) /*-{
        var uint8Array = new Uint8Array(byteArray);
        var floatView = new Float32Array(uint8Array.buffer);

        return floatView[0];
   }-*/;


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
