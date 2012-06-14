package org.glob3.mobile.generated; 
//
//  IGL.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public abstract class IGL
{

  public void dispose()
  {
  }

  public abstract void enableVertices();

  public abstract void enableTextures();

  public abstract void enableTexture2D();

  public abstract void disableTexture2D();

  public abstract void disableVertices();

  public abstract void disableTextures();

  public abstract void clearScreen(float r, float g, float b, float a);

  public void clearScreen(Color col)
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

  public abstract void drawTriangleStrip(int n, byte[] i);

  public abstract void drawLines(int n, byte[] i);

  public abstract void drawLineLoop(int n, byte[] i);

  public abstract void setProjection(MutableMatrix44D projection);

  public abstract void useProgram(int program);

  public abstract void enablePolygonOffset(float factor, float units);

  public abstract void disablePolygonOffset();

  public abstract void lineWidth(float width);

  public abstract void getError();

  public abstract int uploadTexture(IImage image, int widthTexture, int heightTexture);

  public abstract void setTextureCoordinates(int size, int stride, float[] texcoord);

  public abstract void bindTexture(int n);

}