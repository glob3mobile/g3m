//
//  GL2.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include <OpenGLES/ES2/gl.h>
#include <list>

#include "GL2.hpp"

#include "IImage.hpp"

/*
IGL* CreateGL()
{
    return new GL2();
}*/

struct UniformsStruct {
    GLuint Projection;
    GLuint Modelview;
    GLint Sampler;
    GLint EnableTexture;
    GLint FlatColor;

    //FOR BILLBOARDING
    GLint BillBoard;
    GLint ViewPortRatio;
} Uniforms;

struct AttributesStruct {
    GLint Position;
    GLint TextureCoord;
} Attributes;


void mat4::MultMatrix(const float m[16]) {
    float R[16];
    for (int j = 0; j < 4; j++)
        for (int i = 0; i < 4; i++)
            R[j * 4 + i] = m[j * 4] * _M[i] + 
                            m[j * 4 + 1] * _M[4 + i] + 
                            m[j * 4 + 2] * _M[8 + i] + 
                            m[j * 4 + 3] * _M[12 + i];
    assign(R);
}


void GL2::UseProgram(unsigned int program) {
    // set shaders
    glUseProgram(program);

    // Extract the handles to attributes
    Attributes.Position = glGetAttribLocation(program, "Position");
    Attributes.TextureCoord = glGetAttribLocation(program, "TextureCoord");

    // Extract the handles to uniforms
    Uniforms.Projection = glGetUniformLocation(program, "Projection");
    Uniforms.Modelview = glGetUniformLocation(program, "Modelview");
    Uniforms.Sampler = glGetUniformLocation(program, "Sampler");
    Uniforms.EnableTexture = glGetUniformLocation(program, "EnableTexture");
    Uniforms.FlatColor = glGetUniformLocation(program, "FlatColor");

    //BILLBOARDS
    Uniforms.BillBoard = glGetUniformLocation(program, "BillBoard");
    glUniform1i(Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
    Uniforms.ViewPortRatio = glGetUniformLocation(program, "ViewPortRatio");
}


void GL2::SetProjection(float projection[]) {
    glUniformMatrix4fv(Uniforms.Projection, 1, 0, projection);
}


unsigned int GL2::UploadTexture(int size, const void *pixels) {
    // search for free idTexture
    unsigned int n = 0;
    while (_idAssigned[n]) n++;
    _idAssigned[n] = true;
    _numFreeIdTextures--;

    // upload to GPU
    glBindTexture(GL_TEXTURE_2D, _idTextures[n]);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, size, size, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, pixels);

    return n;
}


unsigned int GL2::UploadTexture(IImage *image)
{
  /*
    Image_iOS *image_ios = (Image_iOS *) image;
    int textureSize = SceneController::GetInstance()->getGlobe()->GetTextureSize();
    unsigned char *pixels = image_ios->copyPixelData16(textureSize);
    if (pixels) {
        unsigned int id = UploadTexture(textureSize, (void *) pixels);
        delete [] pixels;
        return id;
    } else {
        // return a dummy id
        return Tile::idBlankTexture;
    }
   */
}


void GL2::UpdateTexture(unsigned int id, int size, const void *pixels)
{
    glBindTexture(GL_TEXTURE_2D, _idTextures[id]);
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, size, size, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, pixels);
}


void GL2::UpdateTexture(unsigned int id, IImage *image)
{   
  /*
    Image_iOS *image_ios = (Image_iOS *) image;
    int textureSize = SceneController::GetInstance()->getGlobe()->GetTextureSize();
    unsigned char *pixels = image_ios->copyPixelData16(textureSize);
    if (pixels) {
        UpdateTexture(id, textureSize, (void *) pixels);
        delete [] pixels;
    }
   */
}
    


unsigned int GL2::UploadTextures(std::vector<IImage *> images, bool transparent) {
  
  /*
  
    if (!transparent) {

        switch (images.size()) {

            case 1: {
                // case one texture: upload texture in GPU and return texture id
                Image_iOS *image = (Image_iOS *) images[0];
                int textureSize = SceneController::GetInstance()->getGlobe()->GetTextureSize();
                unsigned char *pixels = image->copyPixelData16(textureSize);
                if (pixels) {
                    unsigned int id = UploadTexture(textureSize, (void *) pixels);
                    delete [] pixels;
                    return id;
                } else {
                    // return a dummy id
                    return Tile::idBlankTexture;
                }
            }

            case 2: {
                // case with two textures (texture 0 has alpha channel)
                Image_iOS *image0 = (Image_iOS *) images[0];
                Image_iOS *image1 = (Image_iOS *) images[1];
                int textureSize = SceneController::GetInstance()->getGlobe()->GetTextureSize();
                unsigned char *pixels = image1->copyCombinedPixelData16(textureSize, image0);
                if (pixels) {
                    unsigned int id = UploadTexture(textureSize, (void *) pixels);
                    delete [] pixels;
                    return id;
                } else {
                    // return a dummy id
                    return Tile::idBlankTexture;
                }
            }

            default: // ONLY TWO TEXTURES MAXIMUM TO COMBINE!!
                printf("-----More than two textures to combine are not allowed now!!\n");
                return 0;
        }
    } else {
        unsigned char *rgb32 = ((Image_iOS *) images[0])->copyPixelData32();
        if (rgb32 == NULL) return Tile::idBlankTexture;

        int w = ((Image_iOS *) images[0])->GetUIImage().size.width;
        //int h = ((Image_iOS *)images[0])->GetUIImage().size.height;

        return UploadTransparentTexture(w, rgb32);


    }
   
   */
}


unsigned int GL2::UploadTransparentTexture(int size, const void *pixels) {
    // search for free idTexture
    unsigned int n = 0;
    while (_idAssigned[n]) n++;
    _idAssigned[n] = true;
    _numFreeIdTextures--;

    //glEnable (GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glBindTexture(GL_TEXTURE_2D, _idTextures[n]);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

    return n;

}

void GL2::DepthTestEnabled(bool t) {
    if (t) {
        glEnable(GL_DEPTH_TEST);
    } else {
        glDisable(GL_DEPTH_TEST);
    }
}

void GL2::BlendingEnabled(bool t) {
    if (t) {
        glEnable(GL_BLEND);
    } else {
        glDisable(GL_BLEND);
    }
}


void GL2::DrawBillBoard(const unsigned int tex, const float x, const float y, const float z) {
  
  /*
  
    glUniform1i(Uniforms.BillBoard, true); //DRAWING BILLBOARD

    float vertex[] = {
      x, y, z,
      x, y, z,
      x, y, z,
      x, y, z
    };

    Camera *c = View::GetInstance()->GetCamera();
    float ratio = c->GetViewPortRatio();

    glUniform1f(Uniforms.ViewPortRatio, ratio);
  
    static float texcoord[] = {
      1, 1,
      1, 0,
      0, 1,
      0, 0
    };

    //glDisable(GL_DEPTH_TEST);

    glUniform1i(Uniforms.EnableTexture, true);
    glUniform4f(Uniforms.FlatColor, 1.0, 0.0, 0.0, 1);

    glBindTexture(GL_TEXTURE_2D, idTextures[tex]);
    glVertexAttribPointer(Attributes.Position, 3, GL_FLOAT, 0, 0, (const void *) vertex);
    glVertexAttribPointer(Attributes.TextureCoord, 2, GL_FLOAT, 0, 0, (const void *) texcoord);

    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    //glEnable(GL_DEPTH_TEST);

    glUniform1i(Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
   
   */
}

void GL2::DrawTriangleStrip(int n, unsigned char *i) {
    glDrawElements(GL_TRIANGLE_STRIP, n, GL_UNSIGNED_BYTE, i);
}


void GL2::DrawLines(int n, unsigned char *i) {
    glDrawElements(GL_LINES, n, GL_UNSIGNED_BYTE, i);
}


void GL2::DrawLineLoop(int n, unsigned char *i) {
    glDrawElements(GL_LINE_LOOP, n, GL_UNSIGNED_BYTE, i);
}

void GL2::VertexPointer(int size, int stride, const float vertex[]) {
    glVertexAttribPointer(Attributes.Position, size, GL_FLOAT, 0, stride, (const void *) vertex);
}


void GL2::TexCoordPointer(int size, int stride, const float texcoord[]) {
    glVertexAttribPointer(Attributes.TextureCoord, size, GL_FLOAT, 0, stride, (const void *) texcoord);
}


void GL2::BindTexture(unsigned int n) {
    glBindTexture(GL_TEXTURE_2D, _idTextures[n]);
}


void GL2::LoadMatrixf(const float m[]) {
    glUniformMatrix4fv(Uniforms.Modelview, 1, 0, m);
    _modelView.assign(m);
}


void GL2::Translate(float tx, float ty, float tz) {
    float T[16] = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      tx, ty, tz, 1
    };

    _modelView.MultMatrix(T);
    glUniformMatrix4fv(Uniforms.Modelview, 1, 0, _modelView.GetMatrix());
}


void GL2::PopMatrix() {
    _modelView = _matrixStack.back();
    _matrixStack.pop_back();
    glUniformMatrix4fv(Uniforms.Modelview, 1, 0, _modelView.GetMatrix());
}

void GL2::DeleteTexture(unsigned int n) {
    glDeleteTextures(1, &_idTextures[n]);
    _idAssigned[n] = false;
    _numFreeIdTextures++;
    //printf ("borrando texture(%d)=%d\n", n, idTextures[n]);
}


bool GL2::IsTexture(unsigned int texture) {
    return (bool) glIsTexture(texture);
}


void GL2::PushMatrix() {
    _matrixStack.push_back(_modelView);
}


void GL2::EnableVertices() {
    glEnableVertexAttribArray(Attributes.Position);
}


void GL2::EnableTextures() {
    glEnableVertexAttribArray(Attributes.TextureCoord);
}


void GL2::EnableTexture2D() {
    glUniform1i(Uniforms.EnableTexture, true);
}


void GL2::DisableTexture2D() {
    glUniform1i(Uniforms.EnableTexture, false);
}


void GL2::DisableVertices() {
    glDisableVertexAttribArray(Attributes.Position);
}


void GL2::DisableTextures() {
    glDisableVertexAttribArray(Attributes.TextureCoord);
}


void GL2::ClearScreen(float r, float g, float b) {
    glClearColor(r, g, b, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}


void GL2::Color(float r, float g, float b) {
    glUniform4f(Uniforms.FlatColor, r, g, b, 1);
}


void GL2::AllocateTextureMemory(unsigned int num) {
    // allocate memory
    _idTextures = (unsigned int *) malloc((num + 1) * sizeof(unsigned int));
    _idAssigned = (bool *) malloc((num + 1) * sizeof(bool));
    _maxIdTextures = _numFreeIdTextures = num;

    // the position 0 is not used
    _idAssigned[0] = true;
    for (int n = 1; n <= num; n++) _idAssigned[n] = false;
    glGenTextures(num, _idTextures + 1);
}


void GL2::EnablePolygonOffset(float factor, float units) {
    glEnable(GL_POLYGON_OFFSET_FILL);
    glPolygonOffset(factor, units);
}


void GL2::DisablePolygonOffset() {
    glDisable(GL_POLYGON_OFFSET_FILL);
}


void GL2::LineWidth(float width) {
    glLineWidth(width);
}



