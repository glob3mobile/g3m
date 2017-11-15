package org.glob3.mobile.generated;

import java.util.ArrayList;

public class Cylinder {
	
	public static class CylinderMeshInfo {
		public ArrayList<Double> _latlng;
		public int _cylId;
		
		public CylinderMeshInfo() {_cylId = 0; _latlng = new ArrayList<Double>();}
		public CylinderMeshInfo(int id) {_cylId = id; _latlng = new ArrayList<Double>();}
		public CylinderMeshInfo(CylinderMeshInfo cylInfo){
			_cylId = cylInfo._cylId;
			_latlng = new ArrayList<Double>();
			_latlng.addAll(cylInfo._latlng);
		}
		public void addLatLng(double lat, double lng, double hgt){
			_latlng.add(lat);
			_latlng.add(lng);
			_latlng.add(hgt);
		}
	}
	
	public CylinderMeshInfo _info;
	private Vector3D _start;
	private Vector3D _end;
	private double _radius;
	
	private static double PROXIMITY_VALUE = 25.0;
	private static int DISTANCE_METHOD = 2; 
	
	public Cylinder(final Vector3D start, final Vector3D end, final double radius){
		_start = start;
		_end = end;
		_radius = radius;
		_info = new CylinderMeshInfo();
	}
	
	public static void setProximity(double proximity){
		PROXIMITY_VALUE = proximity;
	}
	
	public static void setDistanceMethod(int method){
		DISTANCE_METHOD = method;
	}
	
	public Mesh createMesh(final Color color, final int nSegments,final Planet planet){
		Vector3D d = _end.sub(_start);
		Vector3D r = d._z == 0? new Vector3D(0.0,0.0,1.0) : new Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
		  
		Vector3D p = _start.add(r.times(_radius / r.length()));
		  
		MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(360.0 / nSegments),
		                                                                     d,
		                                                                     _start); 
		FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
		FloatBufferBuilderFromCartesian3D fbbC1 = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
		fbbC1.add(_start);
		FloatBufferBuilderFromCartesian3D fbbC2 = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
		fbbC2.add(_end);
		  
		FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
		FloatBufferBuilderFromCartesian3D normalsC1 = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
		normalsC1.add(d.times(-1.0));
		FloatBufferBuilderFromCartesian3D normalsC2 = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
		normalsC2.add(d);
		
		FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
		
		MutableVector3D x = new MutableVector3D(p); 
		for (int i = 0; i < nSegments; ++i){
			//Tube
			Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
			Vector3D newEndPoint = newStartPoint.add(d);
		
			x.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);

			fbb.add(newStartPoint);
			fbb.add(newEndPoint);
			Geodetic3D stPoint = planet.toGeodetic3D(newStartPoint);
			Geodetic3D endPoint = planet.toGeodetic3D(newEndPoint);
			_info.addLatLng(stPoint._latitude._degrees, stPoint._longitude._degrees, stPoint._height);
			_info.addLatLng(endPoint._latitude._degrees, endPoint._longitude._degrees, endPoint._height);
			normals.add(newStartPoint.sub(_start));
			normals.add(newEndPoint.sub(_end));
		    
		 	//Cover1
		 	fbbC1.add(newStartPoint);
		 	normalsC1.add(d.times(-1.0));
		 	//Cover2
		 	fbbC2.add(newEndPoint);
		 	normalsC2.add(d);
		      
		 	colors.add(color);
		 	colors.add(color);
		 } 
		  //Still covers
		  Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
		  fbbC1.add(newStartPoint);
		  normalsC1.add(d.times(-1.0));
		  Vector3D newEndPoint = newStartPoint.add(d);
		  fbbC2.add(newEndPoint);
		  normalsC2.add(d);
		  
		  ShortBufferBuilder ind = new ShortBufferBuilder();
		  for (int i = 0; i < nSegments*2; ++i){
		    ind.add((short)i);
		  }
		  ind.add((short)0);
		  ind.add((short)1);
		  
		  IFloatBuffer vertices = fbb.create();
		//#warning Tercer parámetro booleano == Depth Test. True oculta todo lo subterráneo.
		  IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(),
		                                    fbb.getCenter(),
		                                    vertices,
		                                    true,
		                                    ind.create(),
		                                    true,
		                                    1.0f,
		                                    1.0f,
		                                    null,//new Color(color),
		                                    colors.create(),//NULL,
		                                    1.0f,
		                                    true,
		                                    normals.create(),
		                                    false,
		                                    0.0f,
		                                    0.0f);
		  
		  
		  CompositeMesh cm = new CompositeMesh();
		  cm.addMesh(im);
		  return cm;
	}
		    
	public static String adaptMeshes(MeshRenderer meshRenderer,
			ArrayList<CylinderMeshInfo> cylInfo,
			final Camera camera,
			final Planet planet){
		
		ArrayList<CylinderMeshInfo> visibleInfo = new ArrayList<CylinderMeshInfo>();
	    ArrayList<Mesh> theMeshes = visibleMeshes(meshRenderer,camera,planet,cylInfo,visibleInfo);
	    
	    double maxDt = 100;
	    String text = "";
	    
	    for (int i=0;i<theMeshes.size();i++){
	        ArrayList<Double> dt = distances(visibleInfo.get(i),camera,planet);
	        for (int j=0;j< dt.size(); j++) {
	            if ((dt.get(j) < PROXIMITY_VALUE)){
	                text = text + "You are close to a visible pipe with id "+visibleInfo.get(i)._cylId+" \n";
	                break;
	            }
	        }
	    }
	    
	    for (int i=0;i<theMeshes.size();i++){
	        IndexedMesh im = (IndexedMesh) theMeshes.get(i);
	        ArrayList<Double> dt = distances(visibleInfo.get(i),camera,planet);
	        IFloatBuffer colors = im.getColorsFloatBuffer();
	        for (int j=3, c=0; j<colors.size(); j=j+4, c++){
	            //double ndt = (dt.get(c) > 100)? 0: 1 - ((dt.get(c) / maxDt));
	        	double ndt = getAlpha(dt.get(c),maxDt,true);
	            colors.put(j,(float)ndt); //Suponiendo que sea un valor de alpha
	        }
	    }
	    return text;

	}
	
	private static ArrayList<Mesh> visibleMeshes(MeshRenderer mr, 
			final Camera camera,
            final Planet planet,
            ArrayList<CylinderMeshInfo> cylInfo,
            ArrayList<CylinderMeshInfo> visibleInfo){
		
		ArrayList<Mesh> theMeshes = mr.getMeshes();
	    ArrayList<Mesh> theVisibleMeshes = new ArrayList<Mesh>();
	    for (int i=0;i<theMeshes.size();i++){
	        CompositeMesh cm = (CompositeMesh) theMeshes.get(i);
	        IndexedMesh im = (IndexedMesh) cm.getChildAtIndex(0);
	        // Pregunta: ¿el cash devuelve un puntero diferente a la misma dirección de memoria o otra dirección de memoria?
	        cylInfo.get(i)._cylId = (int) i;
	        CylinderMeshInfo info = cylInfo.get(i);
	       // IFloatBuffer *vertices = im->getVerticesFloatBuffer();
	        //const size_t numberVertices = vertices->size();
	        boolean visible = false;
	        int vpWidth = camera.getViewPortWidth();
	        int vpHeight = camera.getViewPortHeight();
	        for (int j=0; j<info._latlng.size(); j+=3){
	            //¿Cómo se definen los vértices? //
	            Vector3D vPos = planet.toCartesian(Geodetic3D.fromDegrees(info._latlng.get(j), info._latlng.get(j+1), info._latlng.get(j+2)));
	            Vector2F pixel = camera.point2Pixel(vPos);
	            if (pixel._x >= 0 && pixel._x < vpWidth && pixel._y >=0 && pixel._y <= vpHeight){
//	            if (pixel._x >= 0 and pixel._x < vpHeight and pixel._y >=0 and pixel._y <= vpWidth){
	                visible = true;
	                break;
	            }
	        }
	        if (visible){
	            theVisibleMeshes.add(im);
	            visibleInfo.add(new CylinderMeshInfo(cylInfo.get(i)));
	        }
	    }
	    return theVisibleMeshes;
	}
	
	private static ArrayList<Double> distances(CylinderMeshInfo info,
        final Camera camera,
        final Planet planet){
		ArrayList<Double> dt = new ArrayList<Double>();
	    
	    for (int i=0; i<info._latlng.size(); i=i+3){
	        Geodetic2D userPosition = camera.getGeodeticPosition().asGeodetic2D();
	        Geodetic2D vertexPosition = Geodetic2D.fromDegrees(info._latlng.get(i),info._latlng.get(i+1));
	        dt.add(planet.computeFastLatLonDistance(userPosition, vertexPosition));
	    }
	    return dt;
	}
	
	private static double getAlpha(double distance, double proximityThreshold, boolean divide){
		switch (DISTANCE_METHOD) {
			case 1:
				return rawAlpha(distance,proximityThreshold,divide);
			case 2:
				return linearAlpha(distance,proximityThreshold,divide);
			case 3:
				return smoothstepAlpha(distance,proximityThreshold,divide);
			case 4:
				return perlinSmootherstepAlpha(distance,proximityThreshold,divide);
			case 5:
				return mcDonaldSmootheststepAlpha(distance,proximityThreshold,divide);
			case 6:
				return sigmoidAlpha(distance,proximityThreshold,divide);
			case 7:
				return tanhAlpha(distance,proximityThreshold,divide);
			case 8:
				return arctanAlpha(distance,proximityThreshold,divide);
			case 9:
				return softsignAlpha(distance,proximityThreshold,divide);
		}
		
		return 1/0; // NAN
	}
	
	private static double rawAlpha(double distance, double proximityThreshold, boolean divide){
	    double ndt = (distance > proximityThreshold)? 0: 1;
	    if (divide) ndt = ndt / 2;
	    return ndt;
	}

	private static double linearAlpha(double distance, double proximityThreshold, boolean divide){
	    double ndt = (distance > proximityThreshold)? 0: 1 - ((distance / proximityThreshold));
//	    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	}

	private static double smoothstepAlpha(double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    double ndt = (distance > proximityThreshold)? 0: 1 - ( 3*Math.pow(softDistance,2) - 2*Math.pow(softDistance,3));
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	}
	private static double perlinSmootherstepAlpha (double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    double ndt = (distance > proximityThreshold)? 0: 1 - ( 6*Math.pow(softDistance,5) - 15*Math.pow(softDistance,4) + 10*Math.pow(softDistance,3));
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	    
	}
	private static double mcDonaldSmootheststepAlpha (double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    double ndt = (distance > proximityThreshold)? 0: 1 - ( -20*Math.pow(softDistance,7) + 70*Math.pow(softDistance,6) - 84*Math.pow(softDistance,5) + 35*Math.pow(softDistance,4));
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	}
	private static double sigmoidAlpha (double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    softDistance = (softDistance * 10) - 5;
	    
	    double ndt = (distance > proximityThreshold)? 0: 1 - ( 1 /(1 + Math.exp(-softDistance)));
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	    
	}
	private static double tanhAlpha (double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    softDistance = (softDistance * 10) - 5;
	    
	    double factor = 2 /(1 + Math.exp(-2*softDistance));
	    factor = factor / 2; // Para convertir de 0-2 a 0-1
	    
	    double ndt = (distance > proximityThreshold)? 0: 1 - factor;
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	}

	private static double arctanAlpha (double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    softDistance = (softDistance * 20) - 10;
	    
	    double factor = Math.atan(softDistance);
	    factor = factor + 1.5;
	    factor = factor / 3; // Para convertir de -1.5 / 1.5 a 0 / 1
	    
	    double ndt = (distance > proximityThreshold)? 0: 1 - factor;
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	    
	    return ndt;
	}

	private static double softsignAlpha (double distance, double proximityThreshold, boolean divide){
	    double softDistance = distance / proximityThreshold;
	    softDistance = (softDistance * 100) - 50;
	    
	    double factor = softDistance /(1 + Math.abs(softDistance));
	    factor = factor + 1;
	    factor = factor / 2; // Para convertir de -1 / 1 a 0 / 1
	    
	    double ndt = (distance > proximityThreshold)? 0: 1 - factor;
	    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
	    if (divide) ndt = ndt / 2;
	        
	    return ndt;
	}

}
