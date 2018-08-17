package org.glob3.mobile.generated;//
//  Cylinder.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/09/2017.
//
//

//
//  Cylinder.hpp
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


public class Cylinder
{
	private static int DISTANCE_METHOD = 2;
	private static boolean DEPTH_ENABLED = false;


	public static class CylinderMeshInfo
	{
		public java.util.ArrayList<Double> _latlng = new java.util.ArrayList<Double>();
		public int _cylId;
		public String internalMat;
		public String externalMat;
		public String internalWidth;
		public String externalWidth;
		public String cylinderClass;
		public String cylinderType;
		public boolean isTransport;
		public boolean isCommunication;
		public int officialId;

		public CylinderMeshInfo()
		{
			_cylId = 0;
			isTransport = false;
			isCommunication = false;
			officialId = -1;
		}
		public CylinderMeshInfo(int id)
		{
			_cylId = id;
			isTransport = false;
			isCommunication = false;
			officialId = -1;
		}
		public CylinderMeshInfo(CylinderMeshInfo cylInfo)
		{
			_latlng.ensureCapacity(cylInfo._latlng.size());
			copy(cylInfo._latlng.iterator(),cylInfo._latlng.end(),back_inserter(_latlng));
			_cylId = cylInfo._cylId;
			internalMat = cylInfo.internalMat;
			externalMat = cylInfo.externalMat;
			internalWidth = cylInfo.internalWidth;
			externalWidth = cylInfo.externalWidth;
			cylinderClass = cylInfo.cylinderClass;
			cylinderType = cylInfo.cylinderType;
			isTransport = cylInfo.isTransport;
			isCommunication = cylInfo.isCommunication;
			officialId = cylInfo.officialId;
		}
		public final void addLatLng(double lat, double lng, double hgt)
		{
			_latlng.add(lat);
			_latlng.add(lng);
			_latlng.add(hgt);
		}

		public final void setID (int theId)
		{
			officialId = theId;
		}

		public final void setMaterials(String extMat, String intMat)
		{
			//Ad hoc
			externalMat = extMat;
			internalMat = intMat;
		}

		public final void setWidths(double intWidth, double extWidth)
		{
			//Ad hoc
			std.stringstream sstm = new std.stringstream();
			std.stringstream sstm2 = new std.stringstream();
			sstm << extWidth << " cm.";
			externalWidth = sstm.str();
			sstm2 << intWidth << " cm.";
			internalWidth = sstm2.str();
		}

		public final void setClassAndType (String cClass, String cType)
		{
			//Ad hoc
			cylinderClass = cClass;
			cylinderType = cType;
		}

		public final void setTransportComm (boolean transport, boolean communication)
		{
			//Ad hoc;
			isTransport = transport;
			isCommunication = communication;
		}

		public final String getMessage()
		{
			std.stringstream sstm = new std.stringstream();
			sstm << "ID: " << officialId << "\n";
			sstm << "Class: " << cylinderClass.compareTo() < 0< < "\n";
			sstm << "Type: " << cylinderType + "\n";
			if (cylinderClass.equals("Cable"))
			{
				sstm << "Internal Material: " << internalMat.compareTo() < 0< < "\n";
				sstm << "External Material: " << externalMat.compareTo() < 0< < "\n";
				sstm << "Cross section: " << externalWidth.compareTo() < 0< < "\n";
				sstm << "Is Transmission: " << isTransport << "\n";
				sstm << "Is Communication: " << isCommunication;
			}
			else
			{
				sstm << "Material: " << externalMat.compareTo() < 0< < "\n";
				sstm << "Internal Width: " << internalWidth.compareTo() < 0< < "\n";
				sstm << "External Width: " << externalWidth;
			}
			return sstm.str();
		}

	}
	public CylinderMeshInfo _info = new CylinderMeshInfo();

	public Sphere s;

	public Cylinder(Vector3D start, Vector3D end, double radius, double startAngle)
	{
		this(start, end, radius, startAngle, 360.0);
	}
	public Cylinder(Vector3D start, Vector3D end, double radius)
	{
		this(start, end, radius, 0.0, 360.0);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Cylinder(const Vector3D& start, const Vector3D& end, const double radius, const double startAngle = 0.0, const double endAngle = 360.0): _start(start), _end(end), _radius(radius), s(null), _startAngle(startAngle), _endAngle(endAngle)
	public Cylinder(Vector3D start, Vector3D end, double radius, double startAngle, double endAngle)
	{
		_start = new Vector3D(start);
		_end = new Vector3D(end);
		_radius = radius;
		s = null;
		_startAngle = startAngle;
		_endAngle = endAngle;
	}

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	Mesh createMesh(Color color, int nSegments, Planet planet);

	public static String adaptMeshes(MeshRenderer mr, java.util.ArrayList<CylinderMeshInfo> cylInfo, Camera camera, Planet planet)
	{
    
		java.util.ArrayList<CylinderMeshInfo> visibleInfo = new java.util.ArrayList<CylinderMeshInfo>();
		tangible.RefObject<java.util.ArrayList<CylinderMeshInfo>> tempRef_visibleInfo = new tangible.RefObject<java.util.ArrayList<CylinderMeshInfo>>(visibleInfo);
		java.util.ArrayList<Mesh> theMeshes = visibleMeshes(mr, camera, planet, cylInfo, tempRef_visibleInfo);
		visibleInfo = tempRef_visibleInfo.argvalue;
    
		double maxDt = 100;
		String text = "";
    
		for (int i = 0;i<theMeshes.size();i++)
		{
			java.util.ArrayList<Double> dt = distances(visibleInfo.get(i), camera, planet);
			//bool textWritten = false;
			for (int j = 0;j< dt.size(); j++)
			{
				//maxDt = fmax(maxDt,dt[j]);
				if ((dt.get(j) < DefineConstants.PROXIMITY_VALUE)) // && (!textWritten)){
				{
					String buffer = new String(new char[75]);
					String.format(buffer, "You are close to a visible pipe with id %d \n",visibleInfo.get(i)._cylId);
					text.append(buffer);
					// textWritten = true;
					break;
				}
			}
		}
    
		for (int i = 0;i<theMeshes.size();i++)
		{
			Mesh im = (Mesh) theMeshes.get(i);
			java.util.ArrayList<Double> dt = distances(visibleInfo.get(i), camera, planet);
			for (int i = 0; i < dt.size(); ++i)
			{
				dt.set(i, 1.0); //getAlpha(dt[i], maxDt, true);
			}
			im.setColorTransparency(dt);
		}
		return text;
	}

	public static void setDistanceMethod(int method)
	{
		Cylinder.DISTANCE_METHOD = method;
	}
	public static int getDistanceMethod()
	{
		return Cylinder.DISTANCE_METHOD;
	}
	public static void setDepthEnabled(boolean enabled)
	{
		Cylinder.DEPTH_ENABLED = enabled;
	}
	public static boolean getDepthEnabled()
	{
		return Cylinder.DEPTH_ENABLED;
	}


	private final Vector3D _start = new Vector3D();
	private final Vector3D _end = new Vector3D();
	private final double _radius;
	private final double _startAngle;
	private final double _endAngle;

	private static java.util.ArrayList<Mesh > visibleMeshes(MeshRenderer mr, Camera camera, Planet planet, java.util.ArrayList<CylinderMeshInfo> cylInfo, tangible.RefObject<java.util.ArrayList<CylinderMeshInfo>> visibleInfo)
	{
		java.util.ArrayList<Mesh > theMeshes = mr.getMeshes();
		java.util.ArrayList<Mesh > theVisibleMeshes = new java.util.ArrayList<Mesh >();
		for (int i = 0;i<theMeshes.size();i++)
		{
			//        CompositeMesh *cm = (CompositeMesh *) theMeshes[i];
			IndexedMesh im = (IndexedMesh) theMeshes.get(i); // cm->getChildAtIndex(0);
			// Pregunta: ¿el cash devuelve un puntero diferente a la misma dirección de memoria o otra dirección de memoria?
			cylInfo.get(i)._cylId = (int) i;
			CylinderMeshInfo info = cylInfo.get(i);
			// IFloatBuffer *vertices = im->getVerticesFloatBuffer();
			//const size_t numberVertices = vertices->size();
			boolean visible = false;
			int vpWidth = camera.getViewPortWidth();
			int vpHeight = camera.getViewPortHeight();
			for (int j = 0; j<info._latlng.size(); j+=3)
			{
				//¿Cómo se definen los vértices? //
				Vector3D vPos = planet.toCartesian(Geodetic3D.fromDegrees(info._latlng.get(j), info._latlng.get(j+1), info._latlng.get(j+2)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector2F pixel = camera->point2Pixel(vPos);
				Vector2F pixel = camera.point2Pixel(new Vector3D(vPos));
				if (pixel._x >= 0 && pixel._x < vpWidth && pixel._y >=0 && pixel._y <= vpHeight)
				{
					//            if (pixel._x >= 0 and pixel._x < vpHeight and pixel._y >=0 and pixel._y <= vpWidth){
					visible = true;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D ray = camera->pixel2Ray(pixel).normalized();
					Vector3D ray = camera.pixel2Ray(new Vector2F(pixel)).normalized();
					break;
				}
			}
			if (visible)
			{
				theVisibleMeshes.add(im);
				visibleInfo.argvalue.add(new CylinderMeshInfo(cylInfo.get(i)));
			}
		}
		return theVisibleMeshes;
	}
	private static java.util.ArrayList<Double> distances(CylinderMeshInfo info, Camera camera, Planet planet)
	{
		java.util.ArrayList<Double> dt = new java.util.ArrayList<Double>();
    
		for (int i = 0; i<info._latlng.size(); i = i+3)
		{
			Geodetic2D userPosition = camera.getGeodeticPosition().asGeodetic2D();
			Geodetic2D vertexPosition = Geodetic2D.fromDegrees(info._latlng.get(i), info._latlng.get(i+1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: dt.push_back(planet->computeFastLatLonDistance(userPosition, vertexPosition));
			dt.add(planet.computeFastLatLonDistance(new Geodetic2D(userPosition), new Geodetic2D(vertexPosition)));
		}
		return dt;
	}

	private void createSphere(tangible.RefObject<java.util.ArrayList<Vector3D>> vs)
	{
		/*std::vector<Vector3D*> vs;
		 for (int i=0; i < fbb->size(); i++) {
		 vs.push_back(new Vector3D(fbb->getVector3D(i)));
		 }*/
    
		Sphere sphere = Sphere.createSphereContainingPoints(vs.argvalue);
		s = new Sphere(sphere);
		for (int i = 0; i < vs.argvalue.size(); i++)
		{
			if (vs.argvalue.get(i) != null)
				vs.argvalue.get(i).dispose();
		}
		vs.argvalue.clear();
    
    
    
	}

	private static double getAlpha(double distance, double proximityThreshold, boolean divide)
	{
		if (getDepthEnabled() == true)
		{
			return rawAlpha(distance, 10000, false); //This should cover a whole city without problems.
		}
		else
			switch (getDistanceMethod())
			{
			case 1:
				return rawAlpha(distance, proximityThreshold, divide);
			case 2:
				return linearAlpha(distance, proximityThreshold, divide);
			case 3:
				return smoothstepAlpha(distance, proximityThreshold, divide);
			case 4:
				return perlinSmootherstepAlpha(distance, proximityThreshold, divide);
			case 5:
				return mcDonaldSmootheststepAlpha(distance, proximityThreshold, divide);
			case 6:
				return sigmoidAlpha(distance, proximityThreshold, divide);
			case 7:
				return tanhAlpha(distance, proximityThreshold, divide);
			case 8:
				return arctanAlpha(distance, proximityThreshold, divide);
			case 9:
				return softsignAlpha(distance, proximityThreshold, divide);
		}
    
		return NAND; // NAN
	}
	private static double rawAlpha(double distance, double proximityThreshold, boolean divide)
	{
		double ndt = (distance > proximityThreshold)? 0: 1;
		if (divide)
			ndt = ndt / 2;
		return ndt;
	}
	private static double linearAlpha(double distance, double proximityThreshold, boolean divide)
	{
		double ndt = (distance > proximityThreshold)? 0: 1 - ((distance / proximityThreshold));
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
	}
	private static double smoothstepAlpha(double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		double ndt = (distance > proximityThreshold)? 0: 1 - (3 *Math.pow(softDistance,2) - 2 *Math.pow(softDistance,3));
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
	}
	private static double perlinSmootherstepAlpha (double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		double ndt = (distance > proximityThreshold)? 0: 1 - (6 *Math.pow(softDistance,5) - 15 *Math.pow(softDistance,4) + 10 *Math.pow(softDistance,3));
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
    
	}
	private static double mcDonaldSmootheststepAlpha (double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		double ndt = (distance > proximityThreshold)? 0: 1 - (-20 *Math.pow(softDistance,7) + 70 *Math.pow(softDistance,6) - 84 *Math.pow(softDistance,5) + 35 *Math.pow(softDistance,4));
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
	}
	private static double sigmoidAlpha (double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		softDistance = (softDistance * 10) - 5;
    
		double ndt = (distance > proximityThreshold)? 0: 1 - (1 /(1 + Math.exp(-softDistance)));
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
    
	}
	private static double tanhAlpha (double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		softDistance = (softDistance * 10) - 5;
    
		double factor = 2 /(1 + Math.exp(-2 *softDistance));
		factor = factor / 2; // Para convertir de 0-2 a 0-1
    
		double ndt = (distance > proximityThreshold)? 0: 1 - factor;
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
	}
	private static double arctanAlpha (double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		softDistance = (softDistance * 20) - 10;
    
		double factor = Math.atan(softDistance);
		factor = factor + 1.5;
		factor = factor / 3; // Para convertir de -1.5 / 1.5 a 0 / 1
    
		double ndt = (distance > proximityThreshold)? 0: 1 - factor;
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
	}
	private static double softsignAlpha (double distance, double proximityThreshold, boolean divide)
	{
		double softDistance = distance / proximityThreshold;
		softDistance = (softDistance * 100) - 50;
    
		double factor = softDistance /(1 + Math.abs(softDistance));
		factor = factor + 1;
		factor = factor / 2; // Para convertir de -1 / 1 a 0 / 1
    
		double ndt = (distance > proximityThreshold)? 0: 1 - factor;
		//    ¿Dividir entre 2 para alpha inicial 0.5 ? //
		if (divide)
			ndt = ndt / 2;
    
		return ndt;
	}

}
