package org.glob3.mobile.generated; 
public class MapBoo_HUDRenderer extends HUDRenderer
{
  public final void updateInfo(java.util.ArrayList<String> info)
  {
    removeAllWidgets();
    final int size = info.size();
    for(int i = 0; i < size; i++)
    {
      String inf = info.get(i);
      LabelImageBuilder labelBuilder = new LabelImageBuilder(inf, GFont.monospaced(14), 3, Color.white(), Color.black(), 2, 1, -1, Color.transparent(), 4, true); // mutable -  cornerRadius -  backgroundColor -  shadowOffsetY -  shadowOffsetX -  shadowBlur -  shadowColor -  color -  margin -  font -  text
  
      HUDQuadWidget label = new HUDQuadWidget(labelBuilder, new HUDAbsolutePosition(5), new HUDAbsolutePosition(10 *i), new HUDRelativeSize(1, HUDRelativeSize.Reference.BITMAP_WIDTH), new HUDRelativeSize(1, HUDRelativeSize.Reference.BITMAP_HEIGTH));
  
      addWidget(label);
    }
  }
}