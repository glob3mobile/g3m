package org.glob3.mobile.generated; 
public class CityGMLBuildingTessellator
{

  private static short addTrianglesCuttingEarsForAllWalls(CityGMLBuilding building, FloatBufferBuilderFromCartesian3D fbb, FloatBufferBuilderFromCartesian3D normals, ShortBufferBuilder indexes, FloatBufferBuilderFromColor colors, double baseHeight, Planet planet, short firstIndex, Color color, ElevationData elevationData)
  {
    final java.util.ArrayList<CityGMLBuildingSurface> surfaces = building.getSurfaces();
  
    short buildingFirstIndex = firstIndex;
    for (int w = 0; w < surfaces.size(); w++)
    {
      CityGMLBuildingSurface s = surfaces.get(w);
  
      if (s.isVisible())
      {
        buildingFirstIndex = s.addTrianglesByEarClipping(fbb, normals, indexes, colors, baseHeight, planet, buildingFirstIndex, color, elevationData);
      }
    }
    return buildingFirstIndex;
  }


  private static Mesh createIndexedMeshWithColorPerVertex(CityGMLBuilding building, Planet planet, boolean fixOnGround, Color color, boolean includeGround, ElevationData elevationData)
  {
  
  
    final double baseHeight = fixOnGround ? building.getBaseHeight() : 0;
  
    FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    ShortBufferBuilder indexes = new ShortBufferBuilder();
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    final short firstIndex = 0;
    addTrianglesCuttingEarsForAllWalls(building, fbb, normals, indexes, colors, baseHeight, planet, firstIndex, color, elevationData);
  
    IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true, indexes.create(),true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());
  
    if (fbb != null)
       fbb.dispose();
    if (normals != null)
       normals.dispose();
  
    return im;
  }

  private static CityGMLBuildingTessellatorData createData(short firstV, short lastV, Mesh mesh, FloatBufferBuilderFromCartesian3D vertices)
  {
  
    //Creating sphere
    java.util.ArrayList<Vector3D> vs = new java.util.ArrayList<Vector3D>();
    for (short i = firstV; i < lastV; i++)
    {
      vs.add(new Vector3D(vertices.getAbsoluteVector3D(i)));
    }
  
    Sphere bSphere = Sphere.createSphereContainingPoints(vs);
  
    for (int i = 0; i < vs.size(); i++)
    {
      if (vs.get(i) != null)
         vs.get(i).dispose();
    }
  
    return new DefaultCityGMLBuildingTessellatorData(mesh, firstV, lastV, new Sphere(bSphere));
  
  }



  public static Mesh createMesh(java.util.ArrayList<CityGMLBuilding> buildings, Planet planet, boolean fixOnGround, boolean checkSurfacesVisibility, CityGMLBuildingColorProvider colorProvider, ElevationData elevationData)
  {
  
    CompositeMesh cm = null;
    int buildingCounter = 0;
    int meshesCounter = 0;
  
    if (checkSurfacesVisibility)
    {
      int n = CityGMLBuilding.checkWallsVisibility(buildings);
      ILogger.instance().logInfo("Removed %d invisible walls from the model.", n);
    }
  
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    ShortBufferBuilder indexes = new ShortBufferBuilder();
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    java.util.ArrayList<Short> buildingVertexIndex = new java.util.ArrayList<Short>();
    java.util.ArrayList<CityGMLBuilding> processedBuildings = new java.util.ArrayList<CityGMLBuilding>();
  
    final Color colorWheel = Color.red();
  
    short firstIndex = 0;
    for (int i = 0; i < buildings.size(); i++)
    {
      CityGMLBuilding b = buildings.get(i);
  
      int firstV = ((int)vertices.size()) / 3;
      buildingVertexIndex.add((short)firstV);
  
      processedBuildings.add(b);
  
      final double baseHeight = fixOnGround ? b.getBaseHeight() : 0;
  
      if (colorProvider == null)
      {
        firstIndex = addTrianglesCuttingEarsForAllWalls(b, vertices, normals, indexes, colors, baseHeight, planet, firstIndex, colorWheel.wheelStep((int)buildings.size(), buildingCounter), elevationData);
  
      }
      else
      {
        firstIndex = addTrianglesCuttingEarsForAllWalls(b, vertices, normals, indexes, colors, baseHeight, planet, firstIndex, colorProvider.getColor(b), elevationData);
      }
  
      int lastV = ((int)vertices.size()) / 3;
      buildingVertexIndex.add((short)lastV);
      buildingCounter++;
  
      if (firstIndex > 30000) //Max number of vertex per mesh (CHECK SHORT RANGE)
      {
        if (cm == null)
        {
          cm = new CompositeMesh();
        }
  
        //Adding new mesh
        IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), vertices.getCenter(), vertices.create(), true, indexes.create(), true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());
  
        cm.addMesh(im);
        meshesCounter++;
  
        //Linking buildings with its mesh
        for (int j = 0; j < processedBuildings.size(); j++)
        {
  
          processedBuildings.get(j).setTessellatorData(createData(buildingVertexIndex.get(j *2), buildingVertexIndex.get(j *2+1), im, vertices));
        }
  
        //Reset
        buildingVertexIndex.clear();
        processedBuildings.clear();
  
        if (vertices != null)
           vertices.dispose();
        if (normals != null)
           normals.dispose();
        colors = null;
        vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
        normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
        indexes = new ShortBufferBuilder();
        colors = new FloatBufferBuilderFromColor();
        firstIndex = 0;
      }
    }
  
    //Adding last mesh
    IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), vertices.getCenter(), vertices.create(), true, indexes.create(), true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());
  
    //Linking buildings with its mesh
    for (int j = 0; j < processedBuildings.size(); j++)
    {
  
      processedBuildings.get(j).setTessellatorData(createData(buildingVertexIndex.get(j *2), buildingVertexIndex.get(j *2+1), im, vertices));
    }
  
    if (vertices != null)
       vertices.dispose();
    if (normals != null)
       normals.dispose();
    colors = null;
  
    if (cm == null)
    {
      ILogger.instance().logInfo("One single mesh created for %d buildings", buildingCounter);
      return im;
    }
  
    cm.addMesh(im);
    meshesCounter++;
  
    ILogger.instance().logInfo("%d meshes created for %d buildings", meshesCounter, buildingCounter);
    return cm;
  }

  public static void changeColorOfBuildingInBoundedMesh(CityGMLBuilding building, Color color)
  {
  
    DefaultCityGMLBuildingTessellatorData data = (DefaultCityGMLBuildingTessellatorData) building.getTessllatorData();
  
    if (data == null)
    {
      throw new RuntimeException("NO TESSELLATOR DATA FOR BUILDING");
    }
  
    AbstractMesh mesh = (AbstractMesh)data._containerMesh;
  
    if (mesh != null)
    {
      //TODO
      IFloatBuffer colors = mesh.getColorsFloatBuffer();
  
      final int initPos = data._firstVertexIndexWithinContainerMesh * 4;
      final int finalPos = data._lastVertexIndexWithinContainerMesh * 4;
  
      for (int i = initPos; i < finalPos;)
      {
        colors.put(i++, color._red);
        colors.put(i++, color._green);
        colors.put(i++, color._blue);
        colors.put(i++, color._alpha);
      }
    }
  }

  public static Mark createMark(CityGMLBuilding building, boolean fixOnGround)
  {
    final double deltaH = fixOnGround ? building.getBaseHeight() : 0;
  
    final Geodetic3D center = building.getCenter();
    final Geodetic3D pos = Geodetic3D.fromDegrees(center._latitude._degrees, center._longitude._degrees, center._height - deltaH);
  
    Mark m = new Mark(building._name, pos, AltitudeMode.RELATIVE_TO_GROUND, 100.0);
    return m;
  }

  public static Sphere getSphereOfBuilding(CityGMLBuilding b)
  {
    DefaultCityGMLBuildingTessellatorData data = (DefaultCityGMLBuildingTessellatorData) b.getTessllatorData();
    if (data == null)
    {
      return null;
    }
    return data._bSphere;
  }

  public static boolean areClose(CityGMLBuilding b1, CityGMLBuilding b2)
  {
  
    final double threshold = 0.005;

    if (b1._surfaces.size() == 0){
      ILogger.instance().logError("SURFACES IS 0");
    }
    else if (b1._surfaces.get(0)._geodeticCoordinates.size() == 0){
      ILogger.instance().logError("GEOCOORDS IS 0");
    }
    else {
      //ILogger.instance().logError("ALL SEEMS GOOD");
    }
  
    double dLat = b1._surfaces.get(0)._geodeticCoordinates.get(0)._latitude._degrees - b2._surfaces.get(0)._geodeticCoordinates.get(0)._latitude._degrees;
    dLat = IMathUtils.instance().abs(dLat);
  
    if (dLat > threshold)
    {
      return false;
    }
  
    double dLon = b1._surfaces.get(0)._geodeticCoordinates.get(0)._longitude._degrees - b2._surfaces.get(0)._geodeticCoordinates.get(0)._longitude._degrees;
    dLon = IMathUtils.instance().abs(dLon);
  
    if (dLon > threshold)
    {
      return false;
    }
  
    return true;
  
    /*
    
     //This implementation does not work prior to the mesh generation
    const Sphere* s1 = getSphereOfBuilding(b1);
    const Sphere* s2 = getSphereOfBuilding(b2);
    
    if (s1 == NULL || s2 == NULL){
      return true;
    }
    
    return s1->touchesSphere(s2);
     */
  }
}