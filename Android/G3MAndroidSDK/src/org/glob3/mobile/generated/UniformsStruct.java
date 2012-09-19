package org.glob3.mobile.generated; 
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
  public IGLUniformID ViewPortRatio;

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
	ViewPortRatio = null;

	//FOR COLOR MIXING
	FlatColorIntensity = null;
	EnableColorPerVertex = null;
	EnableFlatColor = null;
	ColorPerVertexIntensity = null;
  }

  public final void deleteUniformsIDs()
  {
  }

  public void dispose()
  {
	deleteUniformsIDs();
  }
}