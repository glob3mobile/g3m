//
//  NativeGL_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "NativeGL2_iOS.hpp"

#include "FloatBuffer_iOS.hpp"

//#include <GLKit/GLKTextureLoader.h>
//
//void NativeGL2_iOS::texImage2D(const IImage* image,
//                               int format) const {
//  Image_iOS* image_iOS = ((Image_iOS*) image);
//
//  UIImage* uiImage = image_iOS->getUIImage();
//  
//  GLKTextureInfo* textureInfo = [GLKTextureLoader textureWithCGImage: [uiImage CGImage]
//                                                             options: nil
//                                                               error: nil];
//  if (textureInfo) {
//    NSLog(@"Texture loaded successfully. name = %d size = (%d x %d)",
//          textureInfo.name, textureInfo.width, textureInfo.height);
//  }
//
////  const unsigned char* data = ((Image_iOS*) image)->createByteArrayRGBA8888();
////
////  glTexImage2D(GL_TEXTURE_2D,
////               0,
////               format,
////               image->getWidth(),
////               image->getHeight(),
////               0,
////               format,
////               GL_UNSIGNED_BYTE,
////               data);
////
////  delete [] data;
//}

void NativeGL2_iOS::vertexAttribPointer(int index,
                         int size,
                         bool normalized,
                         int stride,
                         const IByteBuffer* buffer) const {
  const ByteBuffer_iOS* buffer_iOS = (ByteBuffer_iOS*) buffer;
  buffer_iOS->bindAsVBOToGPU(this);
  glVertexAttribPointer(index, size, GL_UNSIGNED_BYTE, normalized, stride, 0);
  
  //glBindBuffer(GL_ARRAY_BUFFER, 0);
  //const unsigned char* pointer = buffer_iOS->getPointer();
  //glVertexAttribPointer(index, size, GL_UNSIGNED_BYTE, normalized, stride, pointer);
}

void NativeGL2_iOS::vertexAttribPointer(int index,
                         int size,
                         bool normalized,
                         int stride,
                         const IFloatBuffer* buffer) const {
  const FloatBuffer_iOS* buffer_iOS = (FloatBuffer_iOS*) buffer;
  
  buffer_iOS->bindAsVBOToGPU(this);
  glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, 0);
  //glBindBuffer(GL_ARRAY_BUFFER, 0);
  
  //#warning uncoment for no VBO
  //    const float* pointer = buffer_iOS->getPointer();
  //    glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, pointer);
}

void NativeGL2_iOS::deleteVBO(const int x) const{
  
  if (!glIsBuffer(x)){
    printf("problem");
  }
  
  const GLuint vbo = x;
  glDeleteBuffers(1, &vbo);
  if (GL_NO_ERROR != glGetError()) {
    THROW_EXCEPTION("Problem deleting VBO");
  }
  
  if (x == _currentBoundVBO) {
    _currentBoundVBO = -1;
  }
  
}
