//
//  GL2.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include "IGL.hpp"

// class to keep a 4x4 matrix
class mat4 {
public:
    void assign(const float m[16]) {
        for (int k = 0; k < 16; k++) _M[k] = m[k];
    }

    void MultMatrix(const float m[16]);

    const float *GetMatrix() {
        return _M;
    }
private:
    float _M[16];
};


class GL2: public IGL {

public:

    void EnableVertices();

    void EnableTextures();

    void EnableTexture2D();

    void DisableTexture2D();

    void DisableVertices();

    void DisableTextures();

    void ClearScreen(float r, float g, float b);

    void Color(float r, float g, float b);

    void PushMatrix();

    void PopMatrix();

    bool IsTexture(unsigned int texture);

    //void DeleteTextures (int n, const unsigned int textures[]);
    void DeleteTexture(unsigned int n);

    void LoadMatrixf(const float m[]);

    void Translate(float tx, float ty, float tz);

    void BindTexture(unsigned int texture);

    void VertexPointer(int size, int stride, const float vertex[]);

    void TexCoordPointer(int size, int stride, const float texcoord[]);

    //void DrawTriangleFan (int first, int count);
    //void DrawTriangleStrip (int first, int count);
    void DrawTriangleStrip(int n, unsigned char *i);

    void DrawLines(int n, unsigned char *i);

    void DrawLineLoop(int n, unsigned char *i);

    unsigned int UploadTextures(std::vector<IImage *> t, bool transparent);

    void SetProjection(float projection[]);

    void UseProgram(unsigned int program);

    void AllocateTextureMemory(unsigned int num);

    unsigned int GetNumFreeIdTextures() {
        return _numFreeIdTextures;
    };

    void DrawBillBoard(const unsigned int tex, const float x, const float y, const float z);

    void DepthTestEnabled(bool t);

    void BlendingEnabled(bool t);

    void EnablePolygonOffset(float factor, float units);

    void DisablePolygonOffset();

    void LineWidth(float width);

    unsigned int UploadTexture(int size, const void *pixels);
    
    void UpdateTexture(unsigned int id, int size, const void *pixels);
    void UpdateTexture(unsigned int id, IImage *image);
    unsigned int UploadTexture(IImage *image);


    
private:
    // stack of ModelView matrices
    mat4 _modelView;
    std::list<mat4> _matrixStack;

    // handling id textures (because behavior of glDeleteTextures is not as expected)
    unsigned int _maxIdTextures;
    unsigned int _numFreeIdTextures;
    unsigned int * _idTextures;
    bool * _idAssigned;

    // for drawing elements
    unsigned int _numIndices;
    unsigned char * _indices;



    unsigned int UploadTransparentTexture(int size, const void *pixels);

};

