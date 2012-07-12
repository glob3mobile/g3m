package org.glob3.mobile.generated; 
public class IndexedMesh extends Mesh
{
  private final boolean _owner;

  private final GLPrimitive _primitive;

  private final float _vertices;

  private final int _indexes;
  private final int _numIndex;

  private final float _normals;

  private final Color _flatColor;
  private final float _colors;

  private final float _texCoords;
  private final int _textureId;


  public void dispose()
  {
  }

  //NOT TEXTURED
  public IndexedMesh(boolean owner, GLPrimitive primitive, float vertices, int indexes, int numIndex, Color flatColor, float colors)
  {
	  this(owner, primitive, vertices, indexes, numIndex, flatColor, colors, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(boolean owner, const GLPrimitive primitive, const float* vertices, const int* indexes, const int numIndex, const Color* flatColor, const float * colors, const float* normals = null): _owner(owner), _primitive(primitive), _vertices(vertices), _indexes(indexes), _numIndex(numIndex), _texCoords(null), _flatColor(flatColor), _textureId(-1), _normals(normals)
  public IndexedMesh(boolean owner, GLPrimitive primitive, float vertices, int indexes, int numIndex, Color flatColor, float colors, float normals)
  {
	  _owner = owner;
	  _primitive = primitive;
	  _vertices = vertices;
	  _indexes = indexes;
	  _numIndex = numIndex;
	  _texCoords = null;
	  _flatColor = flatColor;
	  _textureId = -1;
	  _normals = normals;
  }

  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, Color flatColor, java.util.ArrayList<Color> colors)
  {
	  this(vertices, primitive, indexes, flatColor, colors, null);
  }
  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, Color flatColor)
  {
	  this(vertices, primitive, indexes, flatColor, null, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(java.util.ArrayList<MutableVector3D>& vertices, const GLPrimitive primitive, java.util.ArrayList<int>& indexes, const Color* flatColor, java.util.ArrayList<Color>* colors = null, java.util.ArrayList<MutableVector3D>* normals = null): _owner(true), _primitive(primitive), _flatColor(flatColor), _texCoords(null), _textureId(-1), _numIndex(indexes.size())
  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, Color flatColor, java.util.ArrayList<Color> colors, java.util.ArrayList<MutableVector3D> normals)
  {
	  _owner = true;
	  _primitive = primitive;
	  _flatColor = flatColor;
	  _texCoords = null;
	  _textureId = -1;
	  _numIndex = indexes.size();
  
	float[] vert = new float[3* vertices.size()];
	int p = 0;
	for (int i = 0; i < vertices.size(); i++)
	{
	  vert[p++] = vertices.get(i).x();
	  vert[p++] = vertices.get(i).y();
	  vert[p++] = vertices.get(i).z();
	}
	_vertices = vert;
  
	int[] ind = new int[indexes.size()];
	for (int i = 0; i < indexes.size(); i++)
	{
	  ind[i] = indexes.get(i);
	}
	_indexes = ind;
  
	if (normals != null)
	{
	  float[] norm = new float[3* vertices.size()];
	  p = 0;
	  for (int i = 0; i < vertices.size(); i++)
	  {
		norm[p++] = (normals)[i].x();
		norm[p++] = (normals)[i].y();
		norm[p++] = (normals)[i].z();
	  }
	  _normals = norm;
	}
	else
	{
	  _normals = null;
	}
  
	if (colors != null)
	{
	  float[] vertexColor = new float[4* colors.size()];
	  for (int i = 0; i < colors.size(); i+=4)
	  {
		vertexColor[i] = (colors)[i].getRed();
		vertexColor[i+1] = (colors)[i].getGreen();
		vertexColor[i+2] = (colors)[i].getBlue();
		vertexColor[i+3] = (colors)[i].getAlpha();
	  }
	  _colors = vertexColor;
	}
	else
		_colors = null;
  
  
  }

  //TEXTURED
  public IndexedMesh(boolean owner, GLPrimitive primitive, float vertices, int indexes, int numIndex, Color flatColor, float colors, int texID, float texCoords)
  {
	  this(owner, primitive, vertices, indexes, numIndex, flatColor, colors, texID, texCoords, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(boolean owner, const GLPrimitive primitive, const float* vertices, const int* indexes, const int numIndex, const Color* flatColor, const float * colors, const int texID, const float* texCoords, const float* normals = null): _owner(owner), _primitive(primitive), _vertices(vertices), _indexes(indexes), _numIndex(numIndex), _texCoords(texCoords), _flatColor(flatColor), _textureId(texID), _normals(normals), _colors(colors)
  public IndexedMesh(boolean owner, GLPrimitive primitive, float vertices, int indexes, int numIndex, Color flatColor, float colors, int texID, float texCoords, float normals)
  {
	  _owner = owner;
	  _primitive = primitive;
	  _vertices = vertices;
	  _indexes = indexes;
	  _numIndex = numIndex;
	  _texCoords = texCoords;
	  _flatColor = flatColor;
	  _textureId = texID;
	  _normals = normals;
	  _colors = colors;
  }

  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, int texID, java.util.ArrayList<MutableVector2D> texCoords, Color flatColor, java.util.ArrayList<Color> colors)
  {
	  this(vertices, primitive, indexes, texID, texCoords, flatColor, colors, null);
  }
  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, int texID, java.util.ArrayList<MutableVector2D> texCoords, Color flatColor)
  {
	  this(vertices, primitive, indexes, texID, texCoords, flatColor, null, null);
  }
  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, int texID, java.util.ArrayList<MutableVector2D> texCoords)
  {
	  this(vertices, primitive, indexes, texID, texCoords, null, null, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(java.util.ArrayList<MutableVector3D>& vertices, const GLPrimitive primitive, java.util.ArrayList<int>& indexes, const int texID, java.util.ArrayList<MutableVector2D>& texCoords, const Color* flatColor = null, java.util.ArrayList<Color>* colors = null, java.util.ArrayList<MutableVector3D>* normals = null): _owner(true), _primitive(primitive), _numIndex(indexes.size()), _flatColor(flatColor), _textureId(texID)
  public IndexedMesh(java.util.ArrayList<MutableVector3D> vertices, GLPrimitive primitive, java.util.ArrayList<Integer> indexes, int texID, java.util.ArrayList<MutableVector2D> texCoords, Color flatColor, java.util.ArrayList<Color> colors, java.util.ArrayList<MutableVector3D> normals)
  {
	  _owner = true;
	  _primitive = primitive;
	  _numIndex = indexes.size();
	  _flatColor = flatColor;
	  _textureId = texID;
	float[] vert = new float[3 * vertices.size()];
	int p = 0;
	for (int i = 0; i < vertices.size(); i++)
	{
	  vert[p++] = vertices.get(i).x();
	  vert[p++] = vertices.get(i).y();
	  vert[p++] = vertices.get(i).z();
	}
	_vertices = vert;
  
	int[] ind = new int[indexes.size()];
	for (int i = 0; i < indexes.size(); i++)
	{
	  ind[i] = indexes.get(i);
	}
	_indexes = ind;
  
	float[] tc = new float[2 * texCoords.size()];
	p = 0;
	for (int i = 0; i < vertices.size(); i++)
	{
	  tc[p++] = texCoords.get(i).x();
	  tc[p++] = texCoords.get(i).y();
	}
	_texCoords = tc;
  
	if (normals == null)
	{
	  _normals = null;
	}
	else
	{
	  float[] norm = new float[3 * vertices.size()];
	  p = 0;
	  for (int i = 0; i < vertices.size(); i++)
	  {
		norm[p++] = (normals)[i].x();
		norm[p++] = (normals)[i].y();
		norm[p++] = (normals)[i].z();
	  }
  
	  _normals = norm;
	}
  
	if (colors != null)
	{
	  float[] vertexColor = new float[4* colors.size()];
	  for (int i = 0; i < colors.size(); i+=4)
	  {
		vertexColor[i] = (colors)[i].getRed();
		vertexColor[i+1] = (colors)[i].getGreen();
		vertexColor[i+2] = (colors)[i].getBlue();
		vertexColor[i+3] = (colors)[i].getAlpha();
	  }
	  _colors = vertexColor;
	}
	else
		_colors = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void render(const RenderContext* rc) const
  public void render(RenderContext rc)
  {
	IGL gl = rc.getGL();
  
	gl.enableVertices();
  
	final boolean isTextured = (_textureId > 0) && (_texCoords != null);
	if (isTextured)
	{
	  gl.enableTextures();
	  gl.enableTexture2D();
  
	  gl.bindTexture(_textureId);
	  gl.setTextureCoordinates(2, 0, _texCoords);
	}
  
	if (_colors != null)
		gl.enableVertexColor(_colors, 0.5);
	else
		gl.disableVertexColor();
  
	if (_flatColor != null)
		gl.enableVertexFlatColor(_flatColor, 0.5);
	else
		gl.disableVertexFlatColor();
  
	if (_normals != null)
		gl.enableVertexNormal(_normals);
	else
		gl.disableVertexNormal();
  
	gl.vertexPointer(3, 0, _vertices);
  
	switch (_primitive)
	{
	  case TriangleStrip:
		gl.drawTriangleStrip(_numIndex, _indexes);
		break;
	  case Lines:
		gl.drawLines(_numIndex, _indexes);
	  case LineLoop:
		gl.drawLineLoop(_numIndex, _indexes);
	  default:
		break;
	}
  
	if (isTextured)
	{
	  gl.disableTexture2D();
	  gl.disableTextures();
	}
  
	gl.disableVertices();
  }

}