package org.glob3.mobile.generated;//
//  Ditch.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/09/2017.
//
//

//
//  Ditch.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/09/2017.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MeshRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IndexedMesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FloatBufferBuilderFromCartesian3D;


public class Ditch
{

	public Ditch(Geodetic3D start, Geodetic3D end, double width)
	{
		_start = new Geodetic3D(start);
		_end = new Geodetic3D(end);
		_width = width;
	}

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	Mesh createMesh(Color color, int nSegments, Planet planet, ElevationData ed);


	private final Geodetic3D _start = new Geodetic3D();
	private final Geodetic3D _end = new Geodetic3D();
	private final double _width;
}
