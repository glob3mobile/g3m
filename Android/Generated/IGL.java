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

  public abstract void EnableVertices();

  public abstract void EnableTextures();

  public abstract void EnableTexture2D();

  public abstract void DisableTexture2D();

  public abstract void DisableVertices();

  public abstract void DisableTextures();

  public abstract void ClearScreen(float r, float g, float b);

  public abstract void Color(float r, float g, float b);

  public abstract void PushMatrix();

  public abstract void PopMatrix();

  public abstract boolean IsTexture(int texture);

  public abstract void DeleteTexture(int n);

  public abstract void LoadMatrixf(float[] m);

  public abstract void Translate(float tx, float ty, float tz);

  public abstract void BindTexture(int texture);

  public abstract void VertexPointer(int size, int stride, float[] vertex);

  public abstract void TexCoordPointer(int size, int stride, float[] texcoord);

  public abstract void DrawTriangleStrip(int n, byte []i);

  public abstract void DrawLines(int n, byte []i);

  public abstract void DrawLineLoop(int n, byte []i);

  public abstract int UploadTextures(java.util.ArrayList<IImage > images, boolean transparent);

  public abstract int UploadTexture(int size, Object pixels);

  public abstract int UploadTexture(IImage image);

  public abstract void UpdateTexture(int id, int size, Object pixels);

  public abstract void UpdateTexture(int id, IImage image);

  public abstract void SetProjection(float[] projection);

  public abstract void AllocateTextureMemory(int num);

  public abstract int GetNumFreeIdTextures();

  public abstract void UseProgram(int program);

  public abstract void DrawBillBoard(int tex, float x, float y, float z);

  public abstract void DepthTestEnabled(boolean t);

  public abstract void BlendingEnabled(boolean t);

  public abstract void EnablePolygonOffset(float factor, float units);

  public abstract void DisablePolygonOffset();

  public abstract void LineWidth(float width);


  public void dispose()
  {
  }
}