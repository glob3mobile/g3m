package org.glob3.mobile.generated;//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3MiOSSDK


//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//




public class FloatBufferBuilderFromGeodetic extends FloatBufferBuilder
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FloatBufferBuilderFromGeodetic(FloatBufferBuilderFromGeodetic that);

  private final CenterStrategy _centerStrategy;
  private float _cx;
  private float _cy;
  private float _cz;

  private void setCenter(Vector3D center)
  {
    _cx = (float) center._x;
    _cy = (float) center._y;
    _cz = (float) center._z;
  }

  private final Planet _planet;

  private FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy, Planet planet, Vector3D center)
  {
      _planet = planet;
      _centerStrategy = centerStrategy;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: setCenter(center);
    setCenter(new Vector3D(center));
  }

  private FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy, Planet planet, Geodetic2D center)
  {
      _planet = planet;
      _centerStrategy = centerStrategy;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: setCenter(_planet->toCartesian(center));
    setCenter(_planet.toCartesian(new Geodetic2D(center)));
  }

  private FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy, Planet planet, Geodetic3D center)
  {
      _planet = planet;
      _centerStrategy = centerStrategy;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: setCenter(_planet->toCartesian(center));
    setCenter(_planet.toCartesian(new Geodetic3D(center)));
  }


  public static FloatBufferBuilderFromGeodetic builderWithoutCenter(Planet planet)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(NO_CENTER, planet, Vector3D::zero);
    return new FloatBufferBuilderFromGeodetic(NO_CENTER, planet, new Vector3D(Vector3D.zero));
//#else
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(CenterStrategy.NO_CENTER, planet, Vector3D::zero);
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.NO_CENTER, planet, new Vector3D(Vector3D.zero));
//#endif
  }

  public static FloatBufferBuilderFromGeodetic builderWithFirstVertexAsCenter(Planet planet)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(FIRST_VERTEX, planet, Vector3D::zero);
    return new FloatBufferBuilderFromGeodetic(FIRST_VERTEX, planet, new Vector3D(Vector3D.zero));
//#else
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(CenterStrategy.FIRST_VERTEX, planet, Vector3D::zero);
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.FIRST_VERTEX, planet, new Vector3D(Vector3D.zero));
//#endif
  }

  public static FloatBufferBuilderFromGeodetic builderWithGivenCenter(Planet planet, Vector3D center)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, center);
    return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, new Vector3D(center));
//#else
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, center);
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, new Vector3D(center));
//#endif
  }

  public static FloatBufferBuilderFromGeodetic builderWithGivenCenter(Planet planet, Geodetic2D center)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, center);
    return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, new Geodetic2D(center));
//#else
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, center);
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, new Geodetic2D(center));
//#endif
  }

  public static FloatBufferBuilderFromGeodetic builderWithGivenCenter(Planet planet, Geodetic3D center)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, center);
    return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, new Geodetic3D(center));
//#else
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, center);
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, new Geodetic3D(center));
//#endif
  }

//C++ TO JAVA CONVERTER TODO TASK: The following line could not be converted:
  void add(const Angle& latitude, const Angle& longitude, const double height);
  {
    public static final Vector3D vector = _planet.toCartesian(latitude, longitude, height);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
    if (_centerStrategy ==FIRST_VERTEX)
    {
//#else
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
    if (_centerStrategy ==CenterStrategy.FIRST_VERTEX)
    {
//#endif
      if (_values.size() == 0)
      {
        setCenter(vector);
      }
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
    if (_centerStrategy ==NO_CENTER)
    {
//#else
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
    if (_centerStrategy ==CenterStrategy.NO_CENTER)
    {
//#endif
      _values.add((float) vector._x);
      _values.add((float) vector._y);
      _values.add((float) vector._z);
    }
    else
    {
      _values.add((float)(vector._x - _cx));
      _values.add((float)(vector._y - _cy));
      _values.add((float)(vector._z - _cz));
    }
  }
  

  void add(const Geodetic3D& position)
  {
    add(position._latitude, position._longitude, position._height);
  }

  void add(const Geodetic2D& position)
  {
    add(position._latitude, position._longitude, 0.0);
  }

  void add(const Geodetic2D& position, const double height)
  {
    add(position._latitude, position._longitude, height);
  }

  Vector3D getCenter()
  {
    return new Vector3D(_cx, _cy, _cz);
  }
}
