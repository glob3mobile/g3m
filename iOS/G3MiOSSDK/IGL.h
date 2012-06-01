//
//  IGL.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IGL_h
#define G3MiOSSDK_IGL_h

#include <vector>

#include "IImage.h"


class IGL {
public:
  
  virtual void EnableVertices() = 0;
  
  virtual void EnableTextures() = 0;
  
  virtual void EnableTexture2D() = 0;
  
  virtual void DisableTexture2D() = 0;
  
  virtual void DisableVertices() = 0;
  
  virtual void DisableTextures() = 0;
  
  virtual void ClearScreen(float r, float g, float b) = 0;
  
  virtual void Color(float r, float g, float b) = 0;
  
  virtual void PushMatrix() = 0;
  
  virtual void PopMatrix() = 0;
  
  virtual bool IsTexture(unsigned int texture) = 0;

  virtual void DeleteTexture(unsigned int n) = 0;
  
  virtual void LoadMatrixf(const float m[]) = 0;
  
  virtual void Translate(float tx, float ty, float tz) = 0;
  
  virtual void BindTexture(unsigned int texture) = 0;
  
  virtual void VertexPointer(int size, int stride, const float vertex[]) = 0;
  
  virtual void TexCoordPointer(int size, int stride, const float texcoord[]) = 0;
  
  virtual void DrawTriangleStrip(int n, unsigned char *i) = 0;
  
  virtual void DrawLines(int n, unsigned char *i) = 0;
  
  virtual void DrawLineLoop(int n, unsigned char *i) = 0;
  
  virtual unsigned int UploadTextures(std::vector<IImage *> images, bool transparent) = 0;
  
  virtual unsigned int UploadTexture(int size, const void *pixels) = 0;
  
  virtual unsigned int UploadTexture(IImage *image) = 0;
  
  virtual void UpdateTexture(unsigned int id, int size, const void *pixels) = 0;
  
  virtual void UpdateTexture(unsigned int id, IImage *image) = 0;
  
  virtual void SetProjection(float projection[]) = 0;
  
  virtual void AllocateTextureMemory(unsigned int num) = 0;
  
  virtual unsigned int GetNumFreeIdTextures() = 0;
  
  virtual void UseProgram(unsigned int program) = 0;
  
  virtual void DrawBillBoard(const unsigned int tex,
                             const float x, 
                             const float y, 
                             const float z) = 0;
  
  virtual void DepthTestEnabled(bool t) = 0;
  
  virtual void BlendingEnabled(bool t) = 0;
  
  virtual void EnablePolygonOffset(float factor, float units) = 0;
  
  virtual void DisablePolygonOffset() = 0;
  
  virtual void LineWidth(float width) = 0;
  
  
  virtual ~IGL() {};
};


#endif
