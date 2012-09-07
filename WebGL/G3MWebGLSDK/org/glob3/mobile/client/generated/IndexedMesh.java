package org.glob3.mobile.generated; 
public class IndexedMesh extends Mesh
{
  private IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes, Color flatColor, java.util.ArrayList<Color> colors)
  {
	  this(vertices, primitive, strategy, center, indexes, flatColor, colors, (float)0.0);
  }
  private IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes, Color flatColor)
  {
	  this(vertices, primitive, strategy, center, indexes, flatColor, null, (float)0.0);
  }
  private IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes)
  {
	  this(vertices, primitive, strategy, center, indexes, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(java.util.ArrayList<MutableVector3D>& vertices, const GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<int>& indexes, const Color* flatColor = null, java.util.ArrayList<Color>* colors = null, const float colorsIntensity = (float)0.0): _owner(true), _primitive(primitive), _numVertices(vertices.size()), _flatColor(flatColor), _numIndex(indexes.size()), _colorsIntensity(colorsIntensity), _extent(null), _centerStrategy(strategy), _center(center)
  private IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes, Color flatColor, java.util.ArrayList<Color> colors, float colorsIntensity)
  {
	  _owner = true;
	  _primitive = primitive;
	  _numVertices = vertices.size();
	  _flatColor = flatColor;
	  _numIndex = indexes.size();
	  _colorsIntensity = colorsIntensity;
	  _extent = null;
	  _centerStrategy = strategy;
	  _center = new Vector3D(center);
	float[] vert = new float[3 * vertices.size()];
	int p = 0;
  
	switch (strategy)
	{
	  case NoCenter:
		for (int i = 0; i < vertices.size(); i++)
		{
		  vert[p++] = (float) vertices.get(i).x();
		  vert[p++] = (float) vertices.get(i).y();
		  vert[p++] = (float) vertices.get(i).z();
		}
		break;
  
	  case GivenCenter:
		for (int i = 0; i < vertices.size(); i++)
		{
		  vert[p++] = (float)(vertices.get(i).x() - center.x());
		  vert[p++] = (float)(vertices.get(i).y() - center.y());
		  vert[p++] = (float)(vertices.get(i).z() - center.z());
		}
		break;
  
	  default:
		System.out.print("IndexedMesh vector constructor: this center Strategy is not yet implemented\n");
	}
  
	_vertices = vert;
  
	int[] ind = new int[indexes.size()];
	for (int i = 0; i < indexes.size(); i++)
	{
	  ind[i] = indexes.get(i);
	}
	_indexes = ind;
  
	if (colors != null)
	{
	  float[] vertexColor = new float[4 * colors.size()];
	  for (int i = 0; i < colors.size(); i+=4)
	  {
		vertexColor[i] = colors.get(i).getRed();
		vertexColor[i+1] = colors.get(i).getGreen();
		vertexColor[i+2] = colors.get(i).getBlue();
		vertexColor[i+3] = colors.get(i).getAlpha();
	  }
	  _colors = vertexColor;
	}
	else
	{
	  _colors = null;
	}
  }

  private IndexedMesh(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor, float[] colors)
  {
	  this(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, (float)0.0);
  }
  private IndexedMesh(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor)
  {
	  this(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, null, (float)0.0);
  }
  private IndexedMesh(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex)
  {
	  this(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(boolean owner, const GLPrimitive primitive, CenterStrategy strategy, Vector3D center, const int numVertices, const float vertices[], const int indexes[], const int numIndex, const Color* flatColor = null, const float colors[] = null, const float colorsIntensity = (float)0.0): _owner(owner), _primitive(primitive), _numVertices(numVertices), _vertices(vertices), _indexes(indexes), _numIndex(numIndex), _flatColor(flatColor), _colors(colors), _colorsIntensity(colorsIntensity), _extent(null), _centerStrategy(strategy), _center(center)
  private IndexedMesh(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor, float[] colors, float colorsIntensity)
  {
	  _owner = owner;
	  _primitive = primitive;
	  _numVertices = numVertices;
	  _vertices = vertices;
	  _indexes = indexes;
	  _numIndex = numIndex;
	  _flatColor = flatColor;
	  _colors = colors;
	  _colorsIntensity = colorsIntensity;
	  _extent = null;
	  _centerStrategy = strategy;
	  _center = new Vector3D(center);
	if (strategy!=CenterStrategy.NoCenter)
	  System.out.print("IndexedMesh array constructor: this center Strategy is not yet implemented\n");
  }


  private final float[]         _vertices;
  private final int[]           _indexes;
  private final float[]         _colors;
  private final GLPrimitive     _primitive; 

  private final boolean _owner;
  private final int _numVertices;
  private final int _numIndex;
  private final Color _flatColor;
  private final float _colorsIntensity;
  private Extent _extent;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* computeExtent() const
  private Extent computeExtent()
  {
	if (_numVertices <= 0)
	{
	  return null;
	}
  
	double minx = 1e10;
	double miny = 1e10;
	double minz = 1e10;
	double maxx = -1e10;
	double maxy = -1e10;
	double maxz = -1e10;
  
	for (int i = 0; i < _numVertices; i++)
	{
	  final int p = i * 3;
  
	  final double x = _vertices[p] + _center.x();
	  final double y = _vertices[p+1] + _center.y();
	  final double z = _vertices[p+2] + _center.z();
  
	  if (x < minx)
		  minx = x;
	  if (x > maxx)
		  maxx = x;
  
	  if (y < miny)
		  miny = y;
	  if (y > maxy)
		  maxy = y;
  
	  if (z < minz)
		  minz = z;
	  if (z > maxz)
		  maxz = z;
	}
  
	return new Box(new Vector3D(minx, miny, minz), new Vector3D(maxx, maxy, maxz));
  }

  private CenterStrategy _centerStrategy;
  private Vector3D _center ;


  public void dispose()
  {
  }


  public static IndexedMesh createFromVector3D(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor, float[] colors)
  {
	  return createFromVector3D(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, (float)0.0);
  }
  public static IndexedMesh createFromVector3D(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor)
  {
	  return createFromVector3D(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, null, (float)0.0);
  }
  public static IndexedMesh createFromVector3D(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex)
  {
	  return createFromVector3D(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static IndexedMesh* createFromVector3D(boolean owner, const GLPrimitive primitive, CenterStrategy strategy, Vector3D center, const int numVertices, const float vertices[], const int indexes[], const int numIndex, const Color* flatColor = null, const float colors[] = null, const float colorsIntensity = (float)0.0)
  public static IndexedMesh createFromVector3D(boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor, float[] colors, float colorsIntensity)
  {
	return new IndexedMesh(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, colorsIntensity);
  }


  public static IndexedMesh createFromVector3D(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes, Color flatColor, java.util.ArrayList<Color> colors)
  {
	  return createFromVector3D(vertices, primitive, strategy, center, indexes, flatColor, colors, (float)0.0);
  }
  public static IndexedMesh createFromVector3D(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes, Color flatColor)
  {
	  return createFromVector3D(vertices, primitive, strategy, center, indexes, flatColor, null, (float)0.0);
  }
  public static IndexedMesh createFromVector3D(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes)
  {
	  return createFromVector3D(vertices, primitive, strategy, center, indexes, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static IndexedMesh* createFromVector3D(java.util.ArrayList<MutableVector3D>& vertices, const GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<int>& indexes, const Color* flatColor = null, java.util.ArrayList<Color>* colors = null, const float colorsIntensity = (float)0.0)
  public static IndexedMesh createFromVector3D(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, java.util.ArrayList<Integer> indexes, Color flatColor, java.util.ArrayList<Color> colors, float colorsIntensity)
  {
	return new IndexedMesh(vertices, primitive, strategy, center, indexes, flatColor, colors, colorsIntensity);
  }


  public static IndexedMesh createFromGeodetic3D(Planet planet, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor, float[] colors)
  {
	  return createFromGeodetic3D(planet, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, (float)0.0);
  }
  public static IndexedMesh createFromGeodetic3D(Planet planet, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor)
  {
	  return createFromGeodetic3D(planet, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, null, (float)0.0);
  }
  public static IndexedMesh createFromGeodetic3D(Planet planet, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex)
  {
	  return createFromGeodetic3D(planet, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static IndexedMesh* createFromGeodetic3D(const Planet *planet, boolean owner, const GLPrimitive primitive, CenterStrategy strategy, Vector3D center, const int numVertices, float vertices[], const int indexes[], const int numIndex, const Color* flatColor = null, const float colors[] = null, const float colorsIntensity = (float)0.0)
  public static IndexedMesh createFromGeodetic3D(Planet planet, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int[] indexes, int numIndex, Color flatColor, float[] colors, float colorsIntensity)
  {
	// convert vertices to latlon coordinates
	for (int n = 0; n<numVertices *3; n+=3)
	{
	  final Geodetic3D g = new Geodetic3D(Angle.fromDegrees(vertices[n]), Angle.fromDegrees(vertices[n+1]), vertices[n+2]);
	  final Vector3D v = planet.toCartesian(g);
	  vertices[n] = (float) v.x();
	  vertices[n+1] = (float) v.y();
	  vertices[n+2] = (float) v.z();
	}

	// create indexed mesh
	return new IndexedMesh(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, colorsIntensity);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void render(const RenderContext* rc) const
  public void render(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.enableVerticesPosition();
  
	if (_colors == null)
	{
	  gl.disableVertexColor();
	}
	else
	{
	  gl.enableVertexColor(_colors, _colorsIntensity);
	}
  
	if (_flatColor == null)
	{
	  gl.disableVertexFlatColor();
	}
	else
	{
	  gl.enableVertexFlatColor(_flatColor, _colorsIntensity);
	}
  
	gl.vertexPointer(3, 0, _vertices);
  
	if (_centerStrategy != CenterStrategy.NoCenter)
	{
	  gl.pushMatrix();
	  gl.multMatrixf(MutableMatrix44D.createTranslationMatrix(_center));
	}
  
	switch (_primitive)
	{
	  case TriangleStrip:
		gl.drawTriangleStrip(_numIndex, _indexes);
		break;
	  case Lines:
		gl.drawLines(_numIndex, _indexes);
		break;
	  case LineLoop:
		gl.drawLineLoop(_numIndex, _indexes);
		break;
	  default:
		break;
	}
  
	if (_centerStrategy != CenterStrategy.NoCenter)
	{
	  gl.popMatrix();
	}
  
	gl.disableVerticesPosition();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* getExtent() const
  public final Extent getExtent()
  {
	if (_extent == null)
	{
	  _extent = computeExtent();
	}
	return _extent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
  public final int getVertexCount()
  {
	return _numVertices;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
  public final Vector3D getVertex(int i)
  {
	final int p = i * 3;
	return new Vector3D(_vertices[p] + _center.x(), _vertices[p+1] + _center.y(), _vertices[p+2] + _center.z());
  }


}