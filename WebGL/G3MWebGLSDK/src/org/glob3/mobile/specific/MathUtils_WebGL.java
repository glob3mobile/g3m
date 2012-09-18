

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IMathUtils;


public class MathUtils_WebGL
         extends
            IMathUtils {

   @Override
   public double pi() {
      return Math.PI;
   }


   @Override
   public boolean isNan(final double v) {
      return Double.isNaN(v);
   }


   @Override
   public boolean isNan(final float v) {
      return Float.isNaN(v);
   }


   @Override
   public double NanD() {
      return Double.NaN;
   }


   @Override
   public float NanF() {
      return (float) Double.NaN;
   }


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
   public double round(final double v) {
      return Math.round(v);
   }


   @Override
   public float round(final float v) {
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
   public int maxInt() {
      return Integer.MAX_VALUE;
   }


   @Override
   public int minInt() {
      return Integer.MIN_VALUE;
   }


   @Override
   public double maxDouble() {
      return Double.MAX_VALUE;
   }


   @Override
   public double minDouble() {
      return Double.MIN_VALUE;
   }


   @Override
   public float maxFloat() {
      return Float.MAX_VALUE;
   }


   @Override
   public float minFloat() {
      return Float.MIN_VALUE;
   }


   @Override
   public double log(final double v) {
      return Math.log(v);
   }


   @Override
   public float log(final float v) {
      return (float) Math.log(v);
   }

}
