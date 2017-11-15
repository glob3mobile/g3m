package org.glob3.mobile.client;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Vector3D;

public class HoleCoverHelper {
	
	private static Color groundColor, wallColor, coverColor;
	private static float lineWidth,pointWidth;
	private static int HOLE_DEPTH = 20;
	
	public static void generateHoleCover(Sector sector, Vector3D center, Planet p, 
			ElevationData elevData, MeshRenderer holeRenderer){
		
		Sector holeSector = generateSector(center,p,9);
		Sector outerSector = generateSector(center,p,15);
		lineWidth = 8;
		pointWidth = 8;
		//Color.fromRGBA255(128,0,0,255);
		wallColor = Color.fromRGBA255(153,76,0,255); //Color.yellow().muchLighter();
		coverColor = wallColor.lighter();
		groundColor = wallColor.darker();
		holeRenderer.clearMeshes();
		   
		generateHole(holeSector,p,elevData,holeRenderer);
		generateOuterCover(holeSector,outerSector,p,elevData,holeRenderer);
	
	}
	
	private static Sector generateSector(Vector3D v1, Planet p, double offset){
		Vector3D v2 = new Vector3D(v1._x - offset, v1._y - offset, v1._z);
		Vector3D v3 = new Vector3D(v1._x + offset, v1._y + offset, v1._z);
		Geodetic3D l = p.toGeodetic3D(v2);
		Geodetic3D u = p.toGeodetic3D(v3);
		Geodetic2D lower = Geodetic2D.fromDegrees(
				Math.min(l._latitude._degrees, u._latitude._degrees), 
				Math.min(l._longitude._degrees, u._longitude._degrees));
		Geodetic2D upper = Geodetic2D.fromDegrees(
				Math.max(l._latitude._degrees, u._latitude._degrees), 
				Math.max(l._longitude._degrees, u._longitude._degrees));
		
		return new Sector(lower,upper);
	}
	
	private static void generateHole(Sector holeSector, Planet p,
			ElevationData elevData, MeshRenderer holeRenderer){
		
		Geodetic2D nw = holeSector.getNW(); 
		Geodetic2D ne = holeSector.getNE();
		Geodetic2D sw = holeSector.getSW();
		Geodetic2D se = holeSector.getSE();
		
		double hnw = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(nw) + 0.5 : 1;
		double hne = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(ne) + 0.5 : 1;
		double hsw = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(sw) + 0.5 : 1;
		double hse = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(se) + 0.5 : 1;
				
		double md = ((hnw + hne + hsw + hse) / 4) - HOLE_DEPTH;

		generateHoleGround(holeSector,md,p,holeRenderer);
		generateHoleWall(nw,ne,hnw,hne,md,p,holeRenderer);
		generateHoleWall(ne,se,hne,hse,md,p,holeRenderer);
		generateHoleWall(se,sw,hse,hsw,md,p,holeRenderer);
		generateHoleWall(sw,nw,hsw,hnw,md,p,holeRenderer);

	}

	private static void generateHoleGround(Sector holeSector,double depth, Planet p, MeshRenderer holeRenderer){
		Geodetic2D nw = holeSector.getNW(); 
		Geodetic2D ne = holeSector.getNE();
		Geodetic2D sw = holeSector.getSW();
		Geodetic2D se = holeSector.getSE();
		// Ground
		FloatBufferBuilderFromGeodetic fbb = 
				FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(p);

		double md = Math.max(depth,1);
		fbb.add(new Geodetic3D(nw,md));
		fbb.add(new Geodetic3D(ne,md));
		fbb.add(new Geodetic3D(se,md));
		fbb.add(new Geodetic3D(sw,md));
		fbb.add(new Geodetic3D(nw,md));

		Mesh mesh = new DirectMesh(GLPrimitive.triangleStrip(),
		   							  true,
		   							  fbb.getCenter(),
		   							  fbb.create(),
		   							  lineWidth,
		   							  pointWidth,
		   							  groundColor,
		   							  null, 0.0f, true);	
		holeRenderer.addMesh(mesh);
	}
	
	private static void generateHoleWall(Geodetic2D start, Geodetic2D end, 
			double hStart, double hEnd, double depth, Planet p, MeshRenderer holeRenderer){
		
		FloatBufferBuilderFromGeodetic fbb = 
				FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(p);

		double md = Math.max(depth,1);
		
		fbb.add(new Geodetic3D(start,hStart));
		fbb.add(new Geodetic3D(end,hEnd));
		fbb.add(new Geodetic3D(end,md));
		fbb.add(new Geodetic3D(start,md));
		fbb.add(new Geodetic3D(start,hStart));

		Mesh mesh = new DirectMesh(GLPrimitive.triangleStrip(),
		   							  true,
		   							  fbb.getCenter(),
		   							  fbb.create(),
		   							  lineWidth,
		   							  pointWidth,
		   							  wallColor,//color,
		   							  null, 0.0f, true);	
		holeRenderer.addMesh(mesh);
	}
	
	private static void generateOuterCover(Sector holeSector,Sector outerSector, 
			Planet p, ElevationData elevData, MeshRenderer holeRenderer){
		Geodetic2D nw = holeSector.getNW(); 
		Geodetic2D ne = holeSector.getNE();
		Geodetic2D sw = holeSector.getSW();
		Geodetic2D se = holeSector.getSE();
		
		double hnw = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(nw) + 0.5 : 1;
		double hne = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(ne) + 0.5 : 1;
		double hsw = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(sw) + 0.5 : 1;
		double hse = (elevData != null && 
				elevData.getSector().fullContains(holeSector)) ? 
				elevData.getElevationAt(se) + 0.5 : 1;
		
		Geodetic2D onw = outerSector.getNW(); 
		Geodetic2D one = outerSector.getNE();
		Geodetic2D osw = outerSector.getSW();
		Geodetic2D ose = outerSector.getSE();
				
		double honw = (elevData != null && 
				elevData.getSector().fullContains(outerSector)) ? 
				elevData.getElevationAt(onw) : 1;
		double hone = (elevData != null && 
				elevData.getSector().fullContains(outerSector)) ? 
				elevData.getElevationAt(one) : 1;
		double hosw = (elevData != null && 
				elevData.getSector().fullContains(outerSector)) ? 
				elevData.getElevationAt(osw) : 1;
		double hose = (elevData != null && 
				elevData.getSector().fullContains(outerSector)) ? 
				elevData.getElevationAt(ose) : 1;
				
		Geodetic3D outerNW = new Geodetic3D(onw,honw);
		Geodetic3D outerNE = new Geodetic3D(one,hone);
		Geodetic3D outerSW = new Geodetic3D(osw,hosw);
		Geodetic3D outerSE = new Geodetic3D(ose,hose);
		Geodetic3D innerNW = new Geodetic3D(nw,hnw);
		Geodetic3D innerNE = new Geodetic3D(ne,hne);
		Geodetic3D innerSW = new Geodetic3D(sw,hsw);
		Geodetic3D innerSE = new Geodetic3D(se,hse);
		
		generateOuterGround(outerNW,outerNE,innerNW,innerNE,p,holeRenderer);
		generateOuterGround(outerNE,outerSE,innerNE,innerSE,p,holeRenderer);
		generateOuterGround(outerSE,outerSW,innerSE,innerSW,p,holeRenderer);
		generateOuterGround(outerSW,outerNW,innerSW,innerNW,p,holeRenderer);
				
	}
	
	private static void generateOuterGround(Geodetic3D outerStart, Geodetic3D outerEnd, 
			Geodetic3D innerStart, Geodetic3D innerEnd, Planet p, MeshRenderer holeRenderer ){
		FloatBufferBuilderFromGeodetic fbb = 
				FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(p);
		
		fbb.add(innerStart);
		fbb.add(innerEnd);
		fbb.add(outerEnd);
		fbb.add(outerStart);
		fbb.add(innerStart);

		Mesh mesh = new DirectMesh(GLPrimitive.triangleStrip(),
		   							  true,
		   							  fbb.getCenter(),
		   							  fbb.create(),
		   							  lineWidth,
		   							  pointWidth,
		   							  coverColor,//color,
		   							  null, 0.0f, true);	
		holeRenderer.addMesh(mesh);
	}
}
