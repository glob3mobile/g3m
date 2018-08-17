package org.glob3.mobile.generated;import java.util.*;

public class MutableLayerTilesRenderParameters
{
  private Sector _topSector;
  private int _topSectorSplitsByLatitude;
  private int _topSectorSplitsByLongitude;
  private int _firstLevel;
  private int _maxLevel;
  private int _tileTextureWidth;
  private int _tileTextureHeight;
  private short _tileMeshWidth;
  private short _tileMeshHeight;
  private boolean _mercator;

  public MutableLayerTilesRenderParameters()
  {
	  _topSector = null;
	  _topSectorSplitsByLatitude = 0;
	  _topSectorSplitsByLongitude = 0;
	  _firstLevel = 0;
	  _maxLevel = 0;
	  _tileTextureWidth = 0;
	  _tileTextureHeight = 0;
	  _tileMeshWidth = 0;
	  _tileMeshHeight = 0;
	  _mercator = false;
  }

  public void dispose()
  {
	if (_topSector != null)
		_topSector.dispose();
  }

  public final boolean update(LayerTilesRenderParameters parameters, tangible.RefObject<java.util.ArrayList<String>> errors)
  {
	if (_topSector == null)
	{
	  _topSector = new Sector(parameters._topSector);
	  _topSectorSplitsByLatitude = parameters._topSectorSplitsByLatitude;
	  _topSectorSplitsByLongitude = parameters._topSectorSplitsByLongitude;
	  _firstLevel = parameters._firstLevel;
	  _maxLevel = parameters._maxLevel;
	  _tileTextureWidth = parameters._tileTextureResolution._x;
	  _tileTextureHeight = parameters._tileTextureResolution._y;
	  _tileMeshWidth = parameters._tileMeshResolution._x;
	  _tileMeshHeight = parameters._tileMeshResolution._y;
	  _mercator = parameters._mercator;
	  return true;
	}

	if (_mercator != parameters._mercator)
	{
	  errors.argvalue.add("Inconsistency in Layer's Parameters: mercator");
	  return false;
	}

	if (!_topSector.isEquals(parameters._topSector))
	{
	  errors.argvalue.add("Inconsistency in Layer's Parameters: topSector");
	  return false;
	}

	if (_topSectorSplitsByLatitude != parameters._topSectorSplitsByLatitude)
	{
	  errors.argvalue.add("Inconsistency in Layer's Parameters: topSectorSplitsByLatitude");
	  return false;
	}

	if (_topSectorSplitsByLongitude != parameters._topSectorSplitsByLongitude)
	{
	  errors.argvalue.add("Inconsistency in Layer's Parameters: topSectorSplitsByLongitude");
	  return false;
	}

	if ((_tileTextureWidth != parameters._tileTextureResolution._x) || (_tileTextureHeight != parameters._tileTextureResolution._y))
	{
	  errors.argvalue.add("Inconsistency in Layer's Parameters: tileTextureResolution");
	  return false;
	}

	if ((_tileMeshWidth != parameters._tileMeshResolution._x) || (_tileMeshHeight != parameters._tileMeshResolution._y))
	{
	  errors.argvalue.add("Inconsistency in Layer's Parameters: tileMeshResolution");
	  return false;
	}

	if (_maxLevel < parameters._maxLevel)
	{
	  ILogger.instance().logWarning("Inconsistency in Layer's Parameters: maxLevel (upgrading from %d to %d)", _maxLevel, parameters._maxLevel);
	  _maxLevel = parameters._maxLevel;
	}

	if (_firstLevel < parameters._firstLevel)
	{
	  ILogger.instance().logWarning("Inconsistency in Layer's Parameters: firstLevel (upgrading from %d to %d)", _firstLevel, parameters._firstLevel);
	  _firstLevel = parameters._firstLevel;
	}

	return true;
  }

  public final boolean update(Layer layer, tangible.RefObject<java.util.ArrayList<String>> errors)
  {
	final java.util.ArrayList<const LayerTilesRenderParameters> layerParametersVector = layer.getLayerTilesRenderParametersVector();

	if (_topSector == null)
	{
	  tangible.RefObject<java.util.ArrayList<String>> tempRef_errors = new tangible.RefObject<java.util.ArrayList<String>>(errors);
	  return update(layerParametersVector.get(0), tempRef_errors);
	  errors.argvalue = tempRef_errors.argvalue;
	}

	int foundI = -1;
	for (int i = 0; i < layerParametersVector.size(); i++)
	{
	  final LayerTilesRenderParameters parameters = layerParametersVector.get(i);
	  if (parameters._mercator == _mercator)
	  {
		foundI = i;
		break;
	  }
	}

	if (foundI < 0)
	{
	  errors.argvalue.add("Can't find a compatible LayerTilesRenderParameters in layer " + layer.description());
	  return false;
	}

	layer.selectLayerTilesRenderParameters(foundI);

	tangible.RefObject<java.util.ArrayList<String>> tempRef_errors2 = new tangible.RefObject<java.util.ArrayList<String>>(errors);
	return update(layerParametersVector.get(foundI), tempRef_errors2);
	errors.argvalue = tempRef_errors2.argvalue;
  }

  public final LayerTilesRenderParameters create(tangible.RefObject<java.util.ArrayList<String>> errors)
  {
	if (_topSector == null)
	{
	  errors.argvalue.add("Can't find any enabled Layer");
	  return null;
	}

	return new LayerTilesRenderParameters(_topSector, _topSectorSplitsByLatitude, _topSectorSplitsByLongitude, _firstLevel, _maxLevel, new Vector2I(_tileTextureWidth, _tileTextureHeight), new Vector2S(_tileMeshWidth, _tileMeshHeight), _mercator);
  }
}
