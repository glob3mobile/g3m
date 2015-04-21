package org.glob3.mobile.generated; 
public abstract class Effect
{

  protected static double pace(double f)
  {
    if (f < 0)
       return 0;
    if (f > 1)
       return 1;

    //const double result = gently(f, 0.6, 0.85);
    final double result = gently(f, 0.25, 0.75);
    if (result < 0)
       return 0;
    if (result > 1)
       return 1;
    return result;
  }

  protected static double sigmoid(double x)
  {
    x = 12.0 *x - 6.0;
    return (1.0 / (1.0 + IMathUtils.instance().exp(-1.0 * x)));
  }

  protected static double gently(double x, double lower, double upper)
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

  public abstract void start(G3MRenderContext rc, TimeInterval when);

  public abstract void doStep(G3MRenderContext rc, TimeInterval when);

  public abstract boolean isDone(G3MRenderContext rc, TimeInterval when);

  public abstract void stop(G3MRenderContext rc, TimeInterval when);

  public abstract void cancel(TimeInterval when);

  public void dispose()
  {

  }
}