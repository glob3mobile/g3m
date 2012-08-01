package org.glob3.mobile.generated; 
public class SGCubeNode extends SGGLeafNode
{
  private boolean _initializedGL;

  private int _numIndices;

  private int[] _index;
  private float[] _vertices;
  private float _halfSize;


  public SGCubeNode()
  {
	  _initializedGL = false;
	  _halfSize = 0.5F;

  }

  public final void initialize(RenderContext rc)
  {
	int res = 12;
	_vertices = new float[res * res * 3];
	_numIndices = 2 * (res - 1) * (res + 1);
	_index = new int[_numIndices];
  
	// create vertices
  
  //  if (ic != NULL && ic->getPlanet() != NULL)
  //    _halfSize = ic->getPlanet()->getRadii().x() / 2.0;
  //  else
  //  _halfSize = 7e6;
  
	int n = 0;
	for (int j = 0; j < res; j++)
	{
	  for (int i = 0; i < res; i++)
	  {
		_vertices[n++] = (float) 0;
		_vertices[n++] = (float)(-_halfSize + i / (float)(res - 1) * 2 *_halfSize);
		_vertices[n++] = (float)(_halfSize - j / (float)(res - 1) * 2 *_halfSize);
	  }
	}
  
	n = 0;
	for (int j = 0; j < res - 1; j++)
	{
	  if (j > 0)
		  _index[n++] = (byte)(j * res);
	  for (int i = 0; i < res; i++)
	  {
		_index[n++] = (byte)(j * res + i);
		_index[n++] = (byte)(j * res + i + res);
	  }
	  _index[n++] = (byte)(j * res + 2 * res - 1);
	}
  }

  public int rawRender(RenderContext rc)
  {
	IGL gl = rc.getGL();
  
	if (!_initializedGL)
	{
	  initialize(rc);
	  _initializedGL = true;
	}
  
  
  
  //  gl->depthTest(true);
  //
  //  gl->enableVertices();
  //
  //  // insert pointers
  //  gl->disableTextures();
  //  gl->vertexPointer(3, 0, _vertices);
  //
  //  {
  //    // draw a red square
  //    gl->color((float) 1, (float) 0, (float) 0, 1);
  ////    gl->pushMatrix();
  ////    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(_halfSize,0,0));
  ////    gl->multMatrixf(T);
  //    gl->drawTriangleStrip(_numIndices, _index);
  ////    gl->popMatrix();
  //  }
  //
  //
  //  gl->depthTest(false);
  //
  //  int __complete_cube;
  
  
  
	gl.enableVerticesPosition();
  
	// insert pointers
	gl.disableTextures();
	gl.vertexPointer(3, 0, _vertices);
  
	{
	  // draw a red square
	  gl.color((float) 1, (float) 0, (float) 0, 1);
	  gl.pushMatrix();
	  MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(new Vector3D(_halfSize,0,0));
	  gl.multMatrixf(T);
	  gl.drawTriangleStrip(_numIndices, _index);
	  gl.popMatrix();
	}
  
	{
	  // draw a green square
	  gl.color((float) 0, (float) 1, (float) 0, 1);
	  gl.pushMatrix();
	  MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(new Vector3D(0,_halfSize,0));
	  MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(90), new Vector3D(0,0,1));
	  gl.multMatrixf(T.multiply(R));
	  gl.drawTriangleStrip(_numIndices, _index);
	  gl.popMatrix();
	}
  
	{
	  // draw a blue square
	  gl.color((float) 0, (float) 0, (float) 1, 1);
	  gl.pushMatrix();
	  MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(new Vector3D(0,-_halfSize,0));
	  MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(-90), new Vector3D(0,0,1));
	  gl.multMatrixf(T.multiply(R));
	  gl.drawTriangleStrip(_numIndices, _index);
	  gl.popMatrix();
	}
  
	{
	  // draw a purple square
	  gl.color((float) 1, (float) 0, (float) 1, 1);
	  gl.pushMatrix();
	  MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(new Vector3D(0,0,-_halfSize));
	  MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(90), new Vector3D(0,1,0));
	  gl.multMatrixf(T.multiply(R));
	  gl.drawTriangleStrip(_numIndices, _index);
	  gl.popMatrix();
	}
  
	{
	  // draw a cian square
	  gl.color((float) 0, (float) 1, (float) 1, 1);
	  gl.pushMatrix();
	  MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(new Vector3D(0,0,_halfSize));
	  MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(-90), new Vector3D(0,1,0));
	  gl.multMatrixf(T.multiply(R));
	  gl.drawTriangleStrip(_numIndices, _index);
	  gl.popMatrix();
	}
  
	{
	  // draw a grey square
	  gl.color((float) 0.5, (float) 0.5, (float) 0.5, 1);
	  gl.pushMatrix();
	  MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(new Vector3D(-_halfSize,0,0));
	  MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(180), new Vector3D(0,0,1));
	  gl.multMatrixf(T.multiply(R));
	  gl.drawTriangleStrip(_numIndices, _index);
	  gl.popMatrix();
	}
  
	gl.enableTextures();
  
  
  
	return Renderer.maxTimeToRender;
  }
}