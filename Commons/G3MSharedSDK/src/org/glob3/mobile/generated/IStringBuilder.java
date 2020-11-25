package org.glob3.mobile.generated;
//
//  IStringBuilder.cpp
//  G3M
//
//  Created by José Miguel S N on 22/08/12.
//

//
//  IStringBuilder.hpp
//  G3M
//
//  Created by José Miguel S N on 22/08/12.
//



public abstract class IStringBuilder
{

  private static IStringBuilder _exemplar = null;


  protected abstract IStringBuilder clone(int floatPrecision);

  public static final int DEFAULT_FLOAT_PRECISION = 20;

  public static void setInstance(IStringBuilder exemplar)
  {
    if (_exemplar != null)
    {
      ILogger.instance().logWarning("IStringBuilder set two times");
    }
    _exemplar = exemplar;
  }

  public static IStringBuilder newStringBuilder()
  {
     return newStringBuilder(DEFAULT_FLOAT_PRECISION);
  }
  public static IStringBuilder newStringBuilder(int floatPrecision)
  {
    return _exemplar.clone(floatPrecision);
  }

  public abstract IStringBuilder addDouble(double d);
  public abstract IStringBuilder addFloat(float f);

  public abstract IStringBuilder addInt(int i);
  public abstract IStringBuilder addLong(long l);

  public abstract IStringBuilder addString(String s);
  public abstract IStringBuilder addBool(boolean b);

  public IStringBuilder clear()
  {
    return clear(DEFAULT_FLOAT_PRECISION);
  }

  public abstract IStringBuilder clear(int floatPrecision);

  public abstract String getString();

  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }

  public abstract boolean contentEqualsTo(String that);

}