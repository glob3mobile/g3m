package org.glob3.mobile.generated;//
//  ChessboardLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

//
//  ChessboardLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//



public class ChessboardLayer extends ProceduralLayer
{
  private final Sector _dataSector = new Sector();

  private final Color _backgroundColor = new Color();
  private final Color _boxColor = new Color();
  private final int _splits;



  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition)
  {
	  this(parametersVector, backgroundColor, boxColor, splits, dataSector, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency)
  {
	  this(parametersVector, backgroundColor, boxColor, splits, dataSector, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector)
  {
	  this(parametersVector, backgroundColor, boxColor, splits, dataSector, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits)
  {
	  this(parametersVector, backgroundColor, boxColor, splits, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor)
  {
	  this(parametersVector, backgroundColor, boxColor, 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor)
  {
	  this(parametersVector, backgroundColor, Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector)
  {
	  this(parametersVector, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: ChessboardLayer(const java.util.ArrayList<const LayerTilesRenderParameters*> parametersVector, const Color& backgroundColor = Color::white(), const Color& boxColor = Color::fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), const int splits = 8, const Sector& dataSector = Sector::fullSphere(), const float transparency = 1.0f, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : ProceduralLayer(parametersVector, transparency, condition, layerInfo), _dataSector(dataSector), _backgroundColor(backgroundColor), _boxColor(boxColor), _splits(splits)
  public ChessboardLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(parametersVector, transparency, condition, layerInfo);
	  _dataSector = new Sector(dataSector);
	  _backgroundColor = new Color(backgroundColor);
	  _boxColor = new Color(boxColor);
	  _splits = splits;
  }

  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, dataSector, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, dataSector, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, dataSector, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, 0, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer(int mercatorFirstLevel)
  {
	  this(mercatorFirstLevel, 18, 0, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public ChessboardLayer()
  {
	  this(2, 18, 0, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: ChessboardLayer(const int mercatorFirstLevel = 2, const int mercatorMaxLevel = 18, const int wgs84firstLevel = 0, const int wgs84maxLevel = 18, const Color& backgroundColor = Color::white(), const Color& boxColor = Color::fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), const int splits = 8, const Sector& dataSector = Sector::fullSphere(), const float transparency = 1.0f, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : ProceduralLayer(LayerTilesRenderParameters::createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel), transparency, condition, layerInfo), _dataSector(dataSector), _backgroundColor(backgroundColor), _boxColor(boxColor), _splits(splits)
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(LayerTilesRenderParameters.createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel), transparency, condition, layerInfo);
	  _dataSector = new Sector(dataSector);
	  _backgroundColor = new Color(backgroundColor);
	  _boxColor = new Color(boxColor);
	  _splits = splits;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  public final String getLayerType()
  {
	return "ChessboardLayer";
  }

  public final RenderState getRenderState()
  {
	return RenderState.ready();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getDataSector() const
  public final Sector getDataSector()
  {
	return _dataSector;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "[ChessboardLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& position, const Sector& sector) const
  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
	return new URL();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ChessboardLayer* copy() const
  public final ChessboardLayer copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new ChessboardLayer(createParametersVectorCopy(), _backgroundColor, _boxColor, _splits, _dataSector, _transparency, (_condition == null) ? null : _condition->copy(), _layerInfo);
	return new ChessboardLayer(createParametersVectorCopy(), new Color(_backgroundColor), new Color(_boxColor), _splits, new Sector(_dataSector), _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileImageProvider* createTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const
  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
	return new ChessboardTileImageProvider(_backgroundColor, _boxColor, _splits);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
  public final boolean rawIsEquals(Layer that)
  {
	final ChessboardLayer t = (ChessboardLayer) that;
  
	if (!_backgroundColor.isEquals(t._backgroundColor))
	{
	  return false;
	}
  
	if (!_boxColor.isEquals(t._boxColor))
	{
	  return false;
	}
  
	if (_splits != t._splits)
	{
	  return false;
	}
  
	return _dataSector.isEquals(t._dataSector);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<URL*> getDownloadURLs(const Tile* tile) const
  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
	java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
	return result;
  }

}
