package org.glob3.mobile.generated;//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//




public class FloatBufferBuilderFromCartesian3D extends FloatBufferBuilder
{
  private final CenterStrategy _centerStrategy;
  private float _cx;
  private float _cy;
  private float _cz;

  private void setCenter(double x, double y, double z)
  {
    _cx = (float) x;
    _cy = (float) y;
    _cz = (float) z;
  }

  private FloatBufferBuilderFromCartesian3D(CenterStrategy centerStrategy, Vector3D center)
  {
      _centerStrategy = centerStrategy;
    setCenter(center._x, center._y, center._z);
  }



  public static FloatBufferBuilderFromCartesian3D builderWithoutCenter()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromCartesian3D(NO_CENTER,Vector3D::zero);
    return new FloatBufferBuilderFromCartesian3D(NO_CENTER, new Vector3D(Vector3D.zero));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromCartesian3D(CenterStrategy.NO_CENTER, Vector3D.zero);
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.NO_CENTER, new Vector3D(Vector3D.zero));
//#endif
  }

  public static FloatBufferBuilderFromCartesian3D builderWithFirstVertexAsCenter()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromCartesian3D(FIRST_VERTEX,Vector3D::zero);
    return new FloatBufferBuilderFromCartesian3D(FIRST_VERTEX, new Vector3D(Vector3D.zero));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromCartesian3D(CenterStrategy.FIRST_VERTEX, Vector3D.zero);
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.FIRST_VERTEX, new Vector3D(Vector3D.zero));
//#endif
  }

  public static FloatBufferBuilderFromCartesian3D builderWithGivenCenter(Vector3D center)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromCartesian3D(GIVEN_CENTER, center);
    return new FloatBufferBuilderFromCartesian3D(GIVEN_CENTER, new Vector3D(center));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromCartesian3D(CenterStrategy.GIVEN_CENTER, center);
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.GIVEN_CENTER, new Vector3D(center));
//#endif
  }

  public final void add(Vector3D vector)
  {
    add(vector._x, vector._y, vector._z);
  }

  public final void add(double x, double y, double z)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
    if (_centerStrategy == FIRST_VERTEX)
    {
//#else
    if (_centerStrategy == CenterStrategy.FIRST_VERTEX)
    {
//#endif
      if (_values.size() == 0)
      {
        setCenter(x, y, z);
      }
    }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
    if (_centerStrategy == NO_CENTER)
    {
//#else
    if (_centerStrategy == CenterStrategy.NO_CENTER)
    {
//#endif
      _values.add((float) x);
      _values.add((float) y);
      _values.add((float) z);
    }
    else
    {
      _values.add((float)(x - _cx));
      _values.add((float)(y - _cy));
      _values.add((float)(z - _cz));
    }
  }

  public final void add(float x, float y, float z)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
    if (_centerStrategy == FIRST_VERTEX)
    {
//#else
    if (_centerStrategy == CenterStrategy.FIRST_VERTEX)
    {
//#endif
      if (_values.size() == 0)
      {
        setCenter(x, y, z);
      }
    }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
    if (_centerStrategy == NO_CENTER)
    {
//#else
    if (_centerStrategy == CenterStrategy.NO_CENTER)
    {
//#endif
      _values.add(x);
      _values.add(y);
      _values.add(z);
    }
    else
    {
      _values.add(x - _cx);
      _values.add(y - _cy);
      _values.add(z - _cz);
    }
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCenter() const
  public final Vector3D getCenter()
  {
    return new Vector3D(_cx, _cy, _cz);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getAbsoluteVector3D(int i) const
  public final Vector3D getAbsoluteVector3D(int i)
  {
    int pos = i * 3;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
    return new Vector3D(_values.get(pos) + _cx, _values.get(pos + 1) + _cy, _values.get(pos+2) + _cz);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
    return new Vector3D(_values.get(pos) + _cx, _values.get(pos + 1) + _cy, _values.get(pos + 2) + _cz);
//#endif
  }

}

