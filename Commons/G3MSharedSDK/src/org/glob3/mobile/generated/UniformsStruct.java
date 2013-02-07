package org.glob3.mobile.generated; 
///#include "IIntBuffer.hpp"

public class UniformsStruct
{

  public IGLUniformID Projection;
  public IGLUniformID Modelview;
  public IGLUniformID Sampler;
  public IGLUniformID EnableTexture;
  public IGLUniformID FlatColor;
  public IGLUniformID TranslationTexCoord;
  public IGLUniformID ScaleTexCoord;
  public IGLUniformID PointSize;

  //FOR BILLBOARDING
  public IGLUniformID BillBoard;
  public IGLUniformID ViewPortExtent;
  public IGLUniformID TextureExtent;


  //FOR COLOR MIXING
  public IGLUniformID FlatColorIntensity;
  public IGLUniformID EnableColorPerVertex;
  public IGLUniformID EnableFlatColor;
  public IGLUniformID ColorPerVertexIntensity;

  public UniformsStruct()
  {
    Projection = null;
    Modelview = null;
    Sampler = null;
    EnableTexture = null;
    FlatColor = null;
    TranslationTexCoord = null;
    ScaleTexCoord = null;
    PointSize = null;

    //FOR BILLBOARDING
    BillBoard = null;
    ViewPortExtent = null;
    TextureExtent = null;

    //FOR COLOR MIXING
    FlatColorIntensity = null;
    EnableColorPerVertex = null;
    EnableFlatColor = null;
    ColorPerVertexIntensity = null;
  }

  public final void deleteUniformsIDs()
  {
    if (Projection != null)
       Projection.dispose();
    if (Modelview != null)
       Modelview.dispose();
    if (Sampler != null)
       Sampler.dispose();
    if (EnableTexture != null)
       EnableTexture.dispose();
    if (FlatColor != null)
       FlatColor.dispose();
    if (TranslationTexCoord != null)
       TranslationTexCoord.dispose();
    if (ScaleTexCoord != null)
       ScaleTexCoord.dispose();
    if (PointSize != null)
       PointSize.dispose();

    //FOR BILLBOARDING
    if (BillBoard != null)
       BillBoard.dispose();
    if (ViewPortExtent != null)
       ViewPortExtent.dispose();
    if (TextureExtent != null)
       TextureExtent.dispose();

    //FOR COLOR MIXING
    if (FlatColorIntensity != null)
       FlatColorIntensity.dispose();
    if (EnableColorPerVertex != null)
       EnableColorPerVertex.dispose();
    if (EnableFlatColor != null)
       EnableFlatColor.dispose();
    if (ColorPerVertexIntensity != null)
       ColorPerVertexIntensity.dispose();
  }

  public void dispose()
  {
    deleteUniformsIDs();
  }
}