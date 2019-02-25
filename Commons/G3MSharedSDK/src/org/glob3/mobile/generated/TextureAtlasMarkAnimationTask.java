package org.glob3.mobile.generated;
public class TextureAtlasMarkAnimationTask extends PeriodicalTask
{

  private static class TextureAtlasMarkAnimationGTask extends GTask
  {
    private Mark _mark;

    private final int _cols;
    private final int _rows;
    private final int _nFrames;
    private final float _scaleX;
    private final float _scaleY;

    private int _currentFrame;


    public void dispose()
    {
    }

    public TextureAtlasMarkAnimationGTask(Mark mark, int cols, int rows, int nFrames)
    {
       _mark = mark;
       _cols = cols;
       _rows = rows;
       _nFrames = nFrames;
       _scaleX = 1.0f / cols;
       _scaleY = 1.0f / rows;
       _currentFrame = 0;
      //    _mark->setOnScreenSize(Vector2F(100,100));
    }


    public void run(G3MContext context)
    {
      final int row = _currentFrame / _cols;
      final int col = _currentFrame % _cols;

      final float translationX = col * (1.0f / _cols);
      final float translationY = row * (1.0f / _rows);

      _mark.setTextureCoordinatesTransformation(translationX, translationY, _scaleX, _scaleY);
      _currentFrame = (_currentFrame+1) % _nFrames;
    }

  }


  public TextureAtlasMarkAnimationTask(Mark mark, int nColumn, int nRows, int nFrames, TimeInterval frameTime)
  {
     super(frameTime, new TextureAtlasMarkAnimationGTask(mark, nColumn, nRows, nFrames));
  }
}
