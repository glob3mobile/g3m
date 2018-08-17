package org.glob3.mobile.generated;//
//  DebugTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

//
//  DebugTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;

public class DebugTileImageProvider extends CanvasTileImageProvider
{
  private static class ImageListener implements IImageListener
  {
	private final String _tileId;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final TileImageContribution _contribution;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final TileImageContribution _contribution = new internal();
//#endif

	private TileImageListener _listener;
	private boolean _deleteListener;

	private static String getImageId(String tileId)
	{
	  IStringBuilder isb = IStringBuilder.newStringBuilder();
	  isb.addString("DebugTileImageProvider/");
	  isb.addString(tileId);
	  final String s = isb.getString();
	  if (isb != null)
		  isb.dispose();
	  return s;
	}

	public ImageListener(String tileId, TileImageContribution contribution, TileImageListener listener, boolean deleteListener)
	{
		_tileId = tileId;
		_contribution = contribution;
		_listener = listener;
		_deleteListener = deleteListener;
	  TileImageContribution.retainContribution(_contribution);
	}

	public void dispose()
	{
	  TileImageContribution.releaseContribution(_contribution);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
	}

	public final void imageCreated(IImage image)
	{
	  final String imageId = getImageId(_tileId);
	  _listener.imageCreated(_tileId, image, imageId, _contribution);
	  if (_deleteListener)
	  {
		if (_listener != null)
			_listener.dispose();
	  }
	}

  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GFont _font = new GFont();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final GFont _font = new internal();
//#endif
  private final Color _color = new Color();

  private final boolean _showIDLabel;
  private final boolean _showSectorLabels;
  private final boolean _showTileBounds;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getIDLabel(const Tile* tile) const
  private String getIDLabel(Tile tile)
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("L:");
	isb.addInt(tile._level);
  
	isb.addString(", C:");
	isb.addInt(tile._column);
  
	isb.addString(", R:");
	isb.addInt(tile._row);
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getSectorLabel1(const Sector& sector) const
  private String getSectorLabel1(Sector sector)
  {
	return "Lower lat: " + sector._lower._latitude.description();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getSectorLabel2(const Sector& sector) const
  private String getSectorLabel2(Sector sector)
  {
	return "Lower lon: " + sector._lower._longitude.description();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getSectorLabel3(const Sector& sector) const
  private String getSectorLabel3(Sector sector)
  {
	return "Upper lat: " + sector._upper._latitude.description();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getSectorLabel4(const Sector& sector) const
  private String getSectorLabel4(Sector sector)
  {
	return "Upper lon: " + sector._upper._longitude.description();
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public DebugTileImageProvider()
  {
	  _font = new GFont(GFont.monospaced(15));
	  _color = new Color(Color.yellow());
	  _showIDLabel = true;
	  _showSectorLabels = true;
	  _showTileBounds = true;
  }

  public DebugTileImageProvider(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds)
  {
	  _font = new GFont(font);
	  _color = new Color(color);
	  _showIDLabel = showIDLabel;
	  _showSectorLabels = showSectorLabels;
	  _showTileBounds = showTileBounds;
  
  }


  public final TileImageContribution contribution(Tile tile)
  {
	return TileImageContribution.fullCoverageTransparent(1);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
	final int width = resolution._x;
	final int height = resolution._y;
  
	ICanvas canvas = getCanvas(width, height);
  
	//canvas->removeShadow();
  
	//canvas->clearRect(0, 0, width, height);
  
  
	if (_showTileBounds)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setLineColor(_color);
	  canvas.setLineColor(new Color(_color));
	  canvas.setLineWidth(1);
	  canvas.strokeRectangle(0, 0, width, height);
	}
  
	if (_showIDLabel || _showSectorLabels)
	{
	  canvas.setShadow(Color.black(), 2, 1, -1);
	  ColumnCanvasElement col = new ColumnCanvasElement();
	  if (_showIDLabel)
	  {
		col.add(new TextCanvasElement(getIDLabel(tile), _font, _color));
	  }
  
	  if (_showSectorLabels)
	  {
		final Sector sectorTile = tile._sector;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: col.add(new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color));
		col.add(new TextCanvasElement(getSectorLabel1(new Sector(sectorTile)), _font, _color));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: col.add(new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color));
		col.add(new TextCanvasElement(getSectorLabel2(new Sector(sectorTile)), _font, _color));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: col.add(new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color));
		col.add(new TextCanvasElement(getSectorLabel3(new Sector(sectorTile)), _font, _color));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: col.add(new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color));
		col.add(new TextCanvasElement(getSectorLabel4(new Sector(sectorTile)), _font, _color));
	  }
  
	  final Vector2F colExtent = col.getExtent(canvas);
	  col.drawAt((width - colExtent._x) / 2, (height - colExtent._y) / 2, canvas);
	}
  
	//ILogger::instance()->logInfo(getIDLabel(tile));
  
	canvas.createImage(new DebugTileImageProvider.ImageListener(tile._id, contribution, listener, deleteListener), true);
  }

  public final void cancel(String tileId)
  {
	// do nothing, can't cancel
  }

}
