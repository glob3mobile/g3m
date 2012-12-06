package org.glob3.mobile.generated; 
public class TextureUploader implements IImageListener
{
  private TileTextureBuilder _builder;

  private final java.util.ArrayList<const RectangleI> _rectangles = new java.util.ArrayList<const RectangleI>();
  private final String _textureId;

  public TextureUploader(TileTextureBuilder builder, java.util.ArrayList<RectangleI> rectangles, String textureId)
  {
	  _builder = builder;
	  _rectangles = rectangles;
	  _textureId = textureId;

  }

  public final void imageCreated(IImage image)
  {
	_builder.imageCreated(image, _rectangles, _textureId);
  }
}