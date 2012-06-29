//
//  IndexedTriangleStripMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <stdlib.h>

#include "IndexedTriangleStripMesh.hpp"

IndexedTriangleStripMesh::~IndexedTriangleStripMesh()
{
  if (_owner){
    delete[] _vertices;
    delete[] _indexes;
    delete[] _texCoords;
    if (_normals != NULL) delete[] _normals;
  }
}

IndexedTriangleStripMesh::IndexedTriangleStripMesh(bool owner, const float* vertices, 
                                                   const unsigned char* indexes, const int numIndex, 
                                                   const Color& color,
                                                   const float* normals):
_owner(owner),
_vertices(vertices),
_indexes(indexes),
_numIndex(numIndex),
_texCoords(NULL),
_color(color),
_textureId(-1),
_normals(normals)
{
}

IndexedTriangleStripMesh::IndexedTriangleStripMesh(std::vector<MutableVector3D>& vertices, 
                                                   std::vector<unsigned char>& indexes,
                                                   const Color& color,
                                                   std::vector<MutableVector3D>* normals):
_owner(true),
_color(color),
_texCoords(NULL),
_textureId(-1),
_numIndex(indexes.size())
{
  
  float * vert = new float[3* vertices.size()];
  int p = 0;
  for (int i = 0; i < vertices.size(); i++) {
    vert[p++] = vertices[i].x();
    vert[p++] = vertices[i].y();
    vert[p++] = vertices[i].z();
  }
  _vertices = vert;
  
  unsigned char * ind = new unsigned char[indexes.size()];
  for (int i = 0; i < indexes.size(); i++) {
    ind[i] = indexes[i];
  }
  _indexes = ind;
  
  if (normals != NULL){
    float * norm = new float[3* vertices.size()];
    p = 0;
    for (int i = 0; i < vertices.size(); i++) {
      norm[p++] = (*normals)[i].x();
      norm[p++] = (*normals)[i].y();
      norm[p++] = (*normals)[i].z();
    }
    _normals = norm;
  } else {
    _normals = NULL;
  }
  
  
}

IndexedTriangleStripMesh::IndexedTriangleStripMesh(bool owner, const float* vertices, 
                                                   const unsigned char* indexes, 
                                                   const int numIndex,
                                                   const int texID, 
                                                   const float* texCoords,
                                                   const float* normals):
_owner(owner),
_vertices(vertices),
_indexes(indexes),
_numIndex(numIndex),
_texCoords(texCoords),
_color( Color::fromRGB((float)0,(float)0,(float)0,(float)0) ),
_textureId(texID),
_normals(normals)
{
}

IndexedTriangleStripMesh::IndexedTriangleStripMesh(std::vector<MutableVector3D>& vertices, 
                                                   std::vector<unsigned char>& indexes,
                                                   const int texID,
                                                   std::vector<MutableVector2D>& texCoords, 
                                                   std::vector<MutableVector3D>* normals):
_owner(true),
_numIndex(indexes.size()),
_color( Color::fromRGB((float)0,(float)0,(float)0,(float)0) ),
_textureId(texID)
{
  float* vert = new float[3 * vertices.size()];
  int p = 0;
  for (int i = 0; i < vertices.size(); i++) {
    vert[p++] = vertices[i].x();
    vert[p++] = vertices[i].y();
    vert[p++] = vertices[i].z();
  }
  _vertices = vert;
  
  unsigned char* ind = new unsigned char[indexes.size()];
  for (int i = 0; i < indexes.size(); i++) {
    ind[i] = indexes[i];
  }
  _indexes = ind;
  
  float* tc = new float[2 * texCoords.size()];
  p = 0;
  for (int i = 0; i < vertices.size(); i++) {
    tc[p++] = texCoords[i].x();
    tc[p++] = texCoords[i].y();
  }
  _texCoords = tc;
  
  if (normals == NULL) {
    _normals = NULL;
  }
  else {
    float* norm = new float[3 * vertices.size()];
    p = 0;
    for (int i = 0; i < vertices.size(); i++) {
      norm[p++] = (*normals)[i].x();
      norm[p++] = (*normals)[i].y();
      norm[p++] = (*normals)[i].z();
    }
    
    _normals = norm;
  }
}

void IndexedTriangleStripMesh::render(const RenderContext* rc) const
{
  IGL *gl = rc->getGL();
  
  gl->enableVertices();
  
  const bool isTextured = (_textureId > 0) && (_texCoords != NULL);
  if (isTextured) {
    gl->enableTextures();
    gl->enableTexture2D();
    
    gl->bindTexture(_textureId);
    gl->setTextureCoordinates(2, 0, _texCoords); 
  }
  else {
    gl->color(_color);
  }
  
  gl->vertexPointer(3, 0, _vertices);
  gl->drawTriangleStrip(_numIndex, _indexes);
  
  if (isTextured) {
    gl->disableTexture2D();
    gl->disableTextures();
  }
  
  gl->disableVertices();
}
