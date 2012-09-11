package org.glob3.mobile.generated; 
public abstract class Effect
{

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double pace(const double f) const
  protected final double pace(double f)
  {
	if (f < 0)
		return 0;
	if (f > 1)
		return 1;

//    return sigmoid(f);
//    return gently(f, 0.6, 0.85);
	return gently(f, 0.25, 0.75);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double sigmoid(double x) const
  protected final double sigmoid(double x)
  {
	x = 12.0 *x - 6.0;
	return (1.0 / (1.0 + IMathUtils.instance().exp(-1.0 * x)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double gently(const double x, const double lower, const double upper) const
  protected final double gently(double x, double lower, double upper)
  {
	final double uperSquared = upper * upper;
	final double lowerPerUper = lower * upper;
	final double tmp = uperSquared - lowerPerUper + lower - 1;

	if (x < lower)
	{
	  return ((upper - 1) / (lower * tmp)) * x * x;
	}

	if (x > upper)
	{
	  final double a3 = 1 / tmp;
	  final double b3 = -2 * a3;
	  final double c3 = 1 + a3;
	  return (a3 * x * x) + (b3 * x) + c3;
	}

	final double m = 2 * (upper - 1) / tmp;
	final double b2 = (0 - m) * lower / 2;
	return m * x + b2;
  }

  public abstract void start(RenderContext rc, TimeInterval now);

  public abstract void doStep(RenderContext rc, TimeInterval now);

  public abstract boolean isDone(RenderContext rc, TimeInterval now);

  public abstract void stop(RenderContext rc, TimeInterval now);

  public abstract void cancel(TimeInterval now);

  public void dispose()
  {
  }
}