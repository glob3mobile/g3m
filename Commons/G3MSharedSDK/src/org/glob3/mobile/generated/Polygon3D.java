package org.glob3.mobile.generated;import java.util.*;

//
//  Polygon3D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

//
//  Polygon3D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//




public class Polygon3D
{

	private java.util.ArrayList<Vector3D> _coor3D = new java.util.ArrayList<Vector3D>();
	private Vector3D _normal = new Vector3D();
	private Polygon2D _polygon2D;

	private Vector3D getNormalOfFirstVertex(java.util.ArrayList<Vector3D*> coor3D)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		final Vector3D e1 = coor3D.get(0).sub(*coor3D.get(1));
		final Vector3D e2 = coor3D.get(2).sub(*coor3D.get(1));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		final Vector3D e1 = coor3D.get(0).sub(coor3D.get(1));
		final Vector3D e2 = coor3D.get(2).sub(coor3D.get(1));
//#endif

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return e1.cross(e2);
		return e1.cross(new Vector3D(e2));
	}



	public static int numberOfP3D = 0;
	public static int numberOfP3D_4 = 0;


	public Polygon3D(java.util.ArrayList<Vector3D> coor3D)
	{
		_coor3D = coor3D;
		_normal = new Vector3D(getNormalOfFirstVertex(coor3D));
		java.util.ArrayList<Vector2D> _coor2D = createCoordinates2D();
		_polygon2D = new Polygon2D(_coor2D);
	}

	public void dispose()
	{
		if (_polygon2D != null)
			_polygon2D.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		for (int j = 0; j < (int)_coor3D.size(); j++)
		{
			if (_coor3D.get(j) != null)
				_coor3D.get(j).dispose();
		}
//#endif
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCCWNormal() const
	public final Vector3D getCCWNormal()
	{
		if (_polygon2D.areVerticesCounterClockWise())
		{
			return _normal.times(-1);
		}
		return _normal;
	}


	public final java.util.ArrayList<Vector2D> createCoordinates2D()
	{
    
	  if (_coor3D.size() == 5)
	  {
		numberOfP3D_4++;
	  }
    
	  final Vector3D z = Vector3D.upZ();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D rotationAxis = z.cross(_normal);
	  final Vector3D rotationAxis = z.cross(new Vector3D(_normal));
	  java.util.ArrayList<Vector2D> coor2D = new java.util.ArrayList<Vector2D>();
    
	  if (rotationAxis.isZero())
	  {
    
		if (_normal._z > 0)
		{
		  for (int i = 0; i < _coor3D.size(); i++)
		  {
			final Vector3D v3D = _coor3D.get(i);
			coor2D.add(new Vector2D(v3D._x, v3D._y));
		  }
		}
		else
		{
		  for (int i = 0; i < _coor3D.size(); i++)
		  {
			final Vector3D v3D = _coor3D.get(i);
			coor2D.add(new Vector2D(v3D._x, -v3D._y));
		  }
		}
	  }
	  else
	  {
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle a = _normal.signedAngleBetween(rotationAxis, z);
		final Angle a = _normal.signedAngleBetween(new Vector3D(rotationAxis), new Vector3D(z));
    
		for (int i = 0; i < _coor3D.size(); i++)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D v3D = _coor3D[i]->rotateAroundAxis(rotationAxis, a);
		  final Vector3D v3D = _coor3D.get(i).rotateAroundAxis(new Vector3D(rotationAxis), new Angle(a));
		  coor2D.add(new Vector2D(v3D._x, v3D._y));
		}
	  }
    
	  return coor2D;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: short addTrianglesByEarClipping(FloatBufferBuilderFromCartesian3D& fbb, FloatBufferBuilderFromCartesian3D& normals, ShortBufferBuilder& indexes, const short firstIndex) const
	public final short addTrianglesByEarClipping(tangible.RefObject<FloatBufferBuilderFromCartesian3D> fbb, tangible.RefObject<FloatBufferBuilderFromCartesian3D> normals, tangible.RefObject<ShortBufferBuilder> indexes, short firstIndex)
	{
    
    
	  numberOfP3D++;
    
	  java.util.ArrayList<Short> indexes2D = _polygon2D.calculateTrianglesIndexesByEarClipping();
	  if (indexes2D.size() > 3)
	  {
    
		Vector3D normal = getCCWNormal();
		for (int i = 0; i < _coor3D.size(); i++)
		{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		  fbb.argvalue.add(*_coor3D.get(i));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		  fbb.argvalue.add(_coor3D.get(i));
//#endif
		  normals.argvalue.add(normal);
		}
    
		for (int i = 0; i < indexes2D.size(); i++)
		{
		  indexes.argvalue.add((short)(indexes2D.get(i) + firstIndex));
		}
		return (short)(_coor3D.size() + firstIndex);
	  }
    
	  return firstIndex;
	}


}
