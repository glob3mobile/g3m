package org.glob3.mobile.generated; 
public abstract class IGL
{

  public void dispose()
  {
  }

  public abstract void enableVertices();

  public abstract void enableTextures();

  public abstract void enableVertexColor(float[] colors, float intensity);

  public abstract void enableVertexFlatColor(Color c, float intensity);

  public abstract void disableVertexColor();

  public abstract void disableVertexFlatColor();

  public abstract void enableVertexNormal(float[] normals);

  public abstract void disableVertexNormal();

  public abstract void enableTexture2D();

  public abstract void disableTexture2D();

  public abstract void disableVertices();

  public abstract void disableTextures();

  public abstract void clearScreen(float r, float g, float b, float a);

  public final void clearScreen(Color col)
  {
	clearScreen(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public abstract void color(float r, float g, float b, float a);

  public void color(Color col)
  {
	color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public abstract void pushMatrix();

  public abstract void popMatrix();

  public abstract void loadMatrixf(MutableMatrix44D m);

  public abstract void multMatrixf(MutableMatrix44D m);

  public abstract void vertexPointer(int size, int stride, float[] vertex);

  public abstract void drawTriangleStrip(int n, int[] i);

  public abstract void drawLines(int n, int[] i);

  public abstract void drawLineLoop(int n, int[] i);

  public abstract void setProjection(MutableMatrix44D projection);

  public abstract void useProgram(int program);

  public abstract void enablePolygonOffset(float factor, float units);

  public abstract void disablePolygonOffset();

  public abstract void lineWidth(float width);

  public abstract int getError();

  public abstract int uploadTexture(IImage image, int textureWidth, int textureHeight);

  public abstract void setTextureCoordinates(int size, int stride, float[] texcoord);

  public abstract void bindTexture(int n);

  public abstract void depth(boolean b);

  public abstract void blend(boolean b);

  public abstract void drawBillBoard(int textureId, Vector3D pos, float viewPortRatio);

  public abstract void deleteTexture(int glTextureId);

  public abstract void cullFace(boolean b, CullFace face);
}