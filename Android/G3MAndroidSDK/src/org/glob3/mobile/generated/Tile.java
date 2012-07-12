package org.glob3.mobile.generated; 
//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  Tile.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class Tile
{

  public Tile(Sector bbox)
  {
	  BBox = bbox;
	  vertices = null;
  }
  public void dispose()
  {
	if (vertices!=null)
		vertices = null;
  }

  public final void createVertices(Planet planet)
  {
	Angle maxLat = BBox.max().latitude();
	Angle minLat = BBox.min().latitude();
	Angle minLon = BBox.min().longitude();
	Angle maxLon = BBox.max().longitude();
	//Globe *globe = SceneController::GetInstance()->getGlobe();
	//Ellipsoid *ellipsoid = globe->GetEllipsoid();
	//bool skirts = globe->SkirtedTiles();
  
	int resol = _resolution;
	int resol2 = resol * resol;
	int n1 = _resolution - 1;
	//double exag = globe->GetExagElevFactor();
	double maxH = 0;
	double H;
	Angle latSize = maxLat.sub(minLat);
	Angle lonSize = maxLon.sub(minLon);
	final double sizeSkirt = 0.95;
  
	// compute number of vertices in the mesh (there are less vertices if the tiles touches one of the poles)
	int numVertices = resol2;
	if (_skirts)
		numVertices += 4 * resol - 4;
  
	// if first time for tile, alloc memory
	if (vertices == null)
	{
	  vertices = new float[numVertices * 3];
	  //textureCoor = new float[numVertices * 2];
	}
  
	// alloc temp memory to create a matrix of coordinates
	double []x = new double[resol2];
	double []y = new double [resol2];
	double []z = new double [resol2];
	float[] u = new float[resol2];
	float[] v = new float [resol2];
  
	// create mesh coordinates
	for (int j = 0; j < resol; j++)
	  for (int i = 0; i < resol; i++)
	  {
		int pos = j * resol + i;
		//H = (elev != NULL) ? elev[pos] * exag : 0;
		H = 0.0;
		if (H > maxH)
			maxH = H;
		//lat = (maxLat.value - latSize.value*j/n1);
		//lon = (minLon.value + lonSize.value*i/n1);
		Angle lat = Angle.fromDegrees((maxLat.degrees() - latSize.degrees() * j / n1));
		Angle lon = Angle.fromDegrees((minLon.degrees() + lonSize.degrees() * i / n1));
		Geodetic3D g3 = new Geodetic3D(lat, lon, H);
		Vector3D P = planet.toVector3D(g3);
		x[pos] = P.x();
		y[pos] = P.y();
		z[pos] = P.z();
		u[pos] = (float) i / n1;
		v[pos] = (float) j / n1;
	  }
  
	// compute center of tile
	Angle lat = Angle.fromDegrees((minLat.degrees() + maxLat.degrees()) / 2);
	Angle lon = Angle.fromDegrees((minLon.degrees() + maxLon.degrees()) / 2);
	Geodetic3D g3 = new Geodetic3D(lat, lon, maxH);
	//Vector3D center = planet->toVector3D(g3);
	center = planet.toVector3D(g3).asMutableVector3D();
  
	// create a nxn mesh
	int posV = 0;
	//unsigned int posT = 0;
	for (int j = 0; j < resol; j++)
	  for (int i = 0; i < resol; i++)
	  {
		int pos = j * resol + i;
		vertices[posV++] = (float)(x[pos] - center.x());
		vertices[posV++] = (float)(y[pos] - center.y());
		vertices[posV++] = (float)(z[pos] - center.z());
		//textureCoor[posT++] = u[pos];
		//textureCoor[posT++] = v[pos];
	  }
  
	// create skirts
	if (_skirts)
	{
  
	  // west side
	  for (int j = 0; j < resol - 1; j++)
	  {
		int pos = j * resol;
		vertices[posV++] = (float)(x[pos] * sizeSkirt - center.x());
		vertices[posV++] = (float)(y[pos] * sizeSkirt - center.y());
		vertices[posV++] = (float)(z[pos] * sizeSkirt - center.z());
		//textureCoor[posT++] = u[pos];
		//textureCoor[posT++] = v[pos];
	  }
  
	  // south side
	  for (int i = 0; i < resol - 1; i++)
	  {
		int pos = (resol - 1) * resol + i;
		vertices[posV++] = (float)(x[pos] * sizeSkirt - center.x());
		vertices[posV++] = (float)(y[pos] * sizeSkirt - center.y());
		vertices[posV++] = (float)(z[pos] * sizeSkirt - center.z());
		//textureCoor[posT++] = u[pos];
		//textureCoor[posT++] = v[pos];
	  }
  
	  // east side
	  for (int j = resol - 1; j > 0; j--)
	  {
		int pos = j * resol + resol - 1;
		vertices[posV++] = (float)(x[pos] * sizeSkirt - center.x());
		vertices[posV++] = (float)(y[pos] * sizeSkirt - center.y());
		vertices[posV++] = (float)(z[pos] * sizeSkirt - center.z());
		//textureCoor[posT++] = u[pos];
		//textureCoor[posT++] = v[pos];
	  }
  
	  // north side
	  for (int i = resol - 1; i > 0; i--)
	  {
		int pos = i;
		vertices[posV++] = (float)(x[pos] * sizeSkirt - center.x());
		vertices[posV++] = (float)(y[pos] * sizeSkirt - center.y());
		vertices[posV++] = (float)(z[pos] * sizeSkirt - center.z());
		//textureCoor[posT++] = u[pos];
		//textureCoor[posT++] = v[pos];
	  }
	}
  
	// free temp memory
	x = null;
	y = null;
	z = null;
	u = null;
	v = null;
  }
  public final void render(RenderContext rc)
  {
	// obtain the gl object
	IGL gl = rc.getGL();
  
	// translate model reference system to tile center
	gl.pushMatrix();
	MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(center.asVector3D());
	gl.multMatrixf(T);
  
	// set opengl texture and pointers
	//gl->BindTexture(idTexture);
	gl.vertexPointer(3, 0, vertices);
	//gl->TexCoordPointer(2, 0, textureCoor);
	gl.color(0.5f,0.5f,0.8f,1.0f);
  
	// draw tile geometry
	if (true) //g->GetWireframe()
	{
  
	  // draw solid mesh
	  gl.enablePolygonOffset(5, 5);
	  gl.drawTriangleStrip(numIndices, indices);
	  gl.disablePolygonOffset();
  
	  // draw wireframe
	  //gl->disableTexture2D();
	  //gl->disableTextures();
	  gl.lineWidth(1);
	  gl.color(0.0f, 0.0f, 0.0f, 1.0f);
	  gl.drawLines(numInnerIndices, innerIndices);
	  gl.lineWidth(2);
	  gl.color(1.0f, 0.0f, 0.0f, 1.0f);
	  gl.drawLineLoop(numBorderIndices, borderIndices);
	  //gl->EnableTextures();
	  //gl->EnableTexture2D();
  
	}
	else
	{
  
	  // draw the mesh
	  gl.drawTriangleStrip(numIndices, indices);
	}
  
	// recover original model matrix
	gl.popMatrix();
  }

  public static void createIndices(int resol, boolean skirts)
  {
	_resolution = resol;
	_skirts = skirts;
  
	// alloc memory
	numIndices = (resol - 1) * (2 * resol + 2) - 1; //remove the first degenerated vertex
	if (skirts)
		numIndices += 8 * resol - 4;
	indices = new byte[numIndices];
  
	// create indices vector for the mesh
	int posI = 0;
	for (int j = 0; j < resol - 1; j++)
	{
	  if (j > 0)
		  indices[posI++] = (byte)(j * resol);
	  for (int i = 0; i < resol; i++)
	  {
		indices[posI++] = (byte)(j * resol + i);
		indices[posI++] = (byte)(j * resol + i + resol);
	  }
	  indices[posI++] = (byte)(j * resol + 2 * resol - 1);
	}
  
	// create skirts
	if (skirts)
	{
	  indices[posI++] = 0;
	  int posS = resol * resol;
  
	  // west side
	  for (int j = 0; j < resol - 1; j++)
	  {
		int pos = j * resol;
		indices[posI++] = (byte)(pos);
		indices[posI++] = (byte)(posS++);
	  }
  
	  // south side
	  for (int i = 0; i < resol - 1; i++)
	  {
		int pos = (resol - 1) * resol + i;
		indices[posI++] = (byte) pos;
		indices[posI++] = (byte)(posS++);
	  }
  
	  // east side
	  for (int j = resol - 1; j > 0; j--)
	  {
		int pos = j * resol + resol - 1;
		indices[posI++] = (byte)(pos);
		indices[posI++] = (byte)(posS++);
	  }
  
	  // north side
	  for (int i = resol - 1; i > 0; i--)
	  {
		int pos = i;
		indices[posI++] = (byte) pos;
		indices[posI++] = (byte)(posS++);
	  }
  
	  // last triangles
	  indices[posI++] = (byte) 0;
	  indices[posI++] = (byte)(resol * resol);
	  indices[posI++] = (byte)(resol * resol);
	}
  
	// create border indices (wireframe mode)
	numBorderIndices = 4 * (resol - 1);
	borderIndices = new byte[numBorderIndices];
	posI = 0;
	for (int j = 0; j < resol - 1; j++)
		borderIndices[posI++] = (byte)(j * resol);
	for (int i = 0; i < resol - 1; i++)
		borderIndices[posI++] = (byte)((resol - 1) * resol + i);
	for (int j = resol - 1; j > 0; j--)
		borderIndices[posI++] = (byte)(j * resol + resol - 1);
	for (int i = resol - 1; i > 0; i--)
		borderIndices[posI++] = (byte)(i);
  
	// create inner indices (wireframe mode)
	numInnerIndices = numBorderIndices * (resol - 2);
	innerIndices = new byte[numInnerIndices];
	posI = 0;
	for (int j = 1; j < resol - 1; j++)
	  for (int i = 0; i < resol - 1; i++)
	  {
		int pos = j * resol + i;
		innerIndices[posI++] = (byte) pos;
		innerIndices[posI++] = (byte)(pos + 1);
	  }
	for (int i = 1; i < resol - 1; i++)
	  for (int j = 0; j < resol - 1; j++)
	  {
		int pos = j * resol + i;
		innerIndices[posI++] = (byte) pos;
		innerIndices[posI++] = (byte)(pos + resol);
	  }
  }
  public static void deleteIndices()
  {
	if (numIndices != 0)
	{
	  indices = null;
	  numIndices = 0;
	}
	if (numInnerIndices != 0)
	{
	  innerIndices = null;
	  numInnerIndices = 0;
	}
	if (numBorderIndices != 0)
	{
	  borderIndices = null;
	  numBorderIndices = 0;
	}
  }

  private final Sector BBox ;
  private float[]vertices;

  private static int _resolution;
  private static boolean _skirts;
  private static int numIndices;
  private static int numBorderIndices = 0;
  private static int numInnerIndices = 0;
  private static byte[]indices;
  private static byte[]borderIndices;
  private static byte[]innerIndices;

  private MutableVector3D center = new MutableVector3D();



}