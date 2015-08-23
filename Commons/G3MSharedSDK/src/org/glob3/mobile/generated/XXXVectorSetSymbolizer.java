package org.glob3.mobile.generated; 
public class XXXVectorSetSymbolizer extends VectorStreamingRenderer.VectorSetSymbolizer
{
  public final Mark createMark(GEO2DPointGeometry geometry)
  {
    final GEOFeature feature = geometry.getFeature();

    final JSONObject properties = feature.getProperties();

    final String label = properties.getAsString("name").value();
    final Geodetic3D position = new Geodetic3D(geometry.getPosition(), 0);

    //    double maxPopulation = 22315474;
    //    double population = properties->getAsNumber("population")->value();
    //    float labelFontSize = (float) (14.0 * (population / maxPopulation) + 16.0) ;

    float labelFontSize = 18F;

    Mark mark = new Mark(label, position, AltitudeMode.ABSOLUTE, 0, labelFontSize); // minDistanceToCamera
                          // Color::newFromRGBA(1, 1, 0, 1)
    mark.setZoomInAppears(true);
    return mark;
  }
}