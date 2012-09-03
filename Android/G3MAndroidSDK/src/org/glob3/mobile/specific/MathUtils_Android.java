package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IMathUtils;

import android.util.FloatMath;

public class MathUtils_Android extends IMathUtils {

	@Override
	public double pi() {
		return Math.PI;
	}

	@Override
	public boolean isNan(double v) {
		return Double.isNaN(v);
	}

	@Override
	public boolean isNan(float v) {
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
	public double sin(double v) {
		return Math.sin(v);
	}

	@Override
	public float sin(float v) {
		return FloatMath.sin(v);
	}

	@Override
	public double asin(double v) {
		return Math.asin(v);
	}

	@Override
	public float asin(float v) {
		return (float) Math.asin(v);
	}

	@Override
	public double cos(double v) {
		return Math.cos(v);
	}

	@Override
	public float cos(float v) {
		return FloatMath.cos(v);
	}

	@Override
	public double acos(double v) {
		return Math.acos(v);
	}

	@Override
	public float acos(float v) {
		return (float) Math.cos(v);
	}

	@Override
	public double tan(double v) {
		return Math.tan(v);
	}

	@Override
	public float tan(float v) {
		return (float) Math.tan(v);
	}

	@Override
	public double atan(double v) {
		return Math.atan(v);
	}

	@Override
	public float atan(float v) {
		return (float) Math.atan(v);
	}

	@Override
	public double atan2(double u, double v) {
		return Math.atan2(u,v);
	}

	@Override
	public float atan2(float u, float v) {
		return (float) Math.atan2(u,v);
	}

	@Override
	public double round(double v) {
		return Math.round(v);
	}

	@Override
	public float round(float v) {
		return Math.round(v);
	}

	@Override
	public int abs(int v) {
		return Math.abs(v);
	}

	@Override
	public double abs(double v) {
		return Math.abs(v);
	}

	@Override
	public float abs(float v) {
		return Math.abs(v);
	}

	@Override
	public double sqrt(double v) {
		return Math.sqrt(v);
	}

	@Override
	public float sqrt(float v) {
		return FloatMath.sqrt(v);
	}

	@Override
	public double pow(double v, double u) {
		return Math.pow(v,u);
	}

	@Override
	public float pow(float v, float u) {
		return (float) Math.pow(v,u);
	}

	@Override
	public double exp(double v) {
		return Math.exp(v);
	}

	@Override
	public float exp(float v) {
		return (float) Math.exp(v);
	}

	@Override
	public double log10(double v) {
		return Math.log10(v);
	}

	@Override
	public float log10(float v) {
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
	
	
	

}
