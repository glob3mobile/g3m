//
//  UtilityJSONParser.m
//  EIFER App
//
//  Created by Chano on 21/6/18.
//
//

#import "UtilityJSONParser.h"
#import "PipesModel.hpp"

#include <G3MiOSSDK/JSONArray.hpp>


@implementation UtilityJSONParser

- (void) loadJSONWithPath:(NSString *)path elevData:(const ElevationData *)elevData planet:(const Planet *)planet mr:(MeshRenderer *)mr
{
    
    NSString *filePath = [[NSBundle mainBundle] pathForResource:path ofType:@"json"];
    NSData *data = [NSData dataWithContentsOfFile:filePath];
    NSMutableArray *json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
    
    
    NSLog(@"json: %@", json);
    [self correctHeightIfNecessaryWithJSON:json elevData:elevData];
    NSLog(@"json: %@", json);
    
    [self generateComplexCylindersWithJSON:json planet:planet mr:mr];
    
}

- (void) generateComplexCylindersWithJSON:(NSMutableArray *)json planet:(const Planet *)planet mr:(MeshRenderer *)mr
{
    for (unsigned int i=0;i<[json count];i++){
        NSMutableDictionary *pipeModel = [json objectAtIndex:i];
        Geodetic3D g = Geodetic3D::fromDegrees([[[pipeModel objectForKey:@"startPoint"] objectAtIndex:1] doubleValue],
                                               [[[pipeModel objectForKey:@"startPoint"] objectAtIndex:0] doubleValue],
                                               [[[pipeModel objectForKey:@"startPoint"] objectAtIndex:2] doubleValue]);
        
        Geodetic3D g2 = Geodetic3D::fromDegrees([[[pipeModel objectForKey:@"endPoint"] objectAtIndex:1] doubleValue],
                                               [[[pipeModel objectForKey:@"endPoint"] objectAtIndex:0] doubleValue],
                                               [[[pipeModel objectForKey:@"endPoint"] objectAtIndex:2] doubleValue]);
        
        double eDiam = 0, iDiam = 0;
        @try {
            eDiam = [[pipeModel objectForKey:@"eDiam"] doubleValue];
            iDiam = [[pipeModel objectForKey:@"iDiam"] doubleValue];
        }
        @catch (NSException *e){
            eDiam = [[pipeModel objectForKey:@"crossSection"] doubleValue];
            iDiam = [[pipeModel objectForKey:@"crossSection"] doubleValue];
        }
        bool isT = false, isC = false;
        @try {
            isT = [[pipeModel objectForKey:@"isTransportation"] boolValue];
            isC = [[pipeModel objectForKey:@"isCommunication"] boolValue];
        }
        @catch (NSException *e){}
        
        double covSegments = [[pipeModel objectForKey:@"covSegments"] doubleValue];
        
        //Y ahora las strings, que hay que generar ...
        
        std::string theClass([[pipeModel objectForKey:@"class"] UTF8String]);
        std::string theType([[pipeModel objectForKey:@"type"] UTF8String]);
        std::string eMat([[pipeModel objectForKey:@"eMat"] UTF8String]);
        std::string iMat([[pipeModel objectForKey:@"iMat"] UTF8String]);
        int theId = i;
        
        //Hay que hacer un "generar JSONArrays" y luego, tras llamar a lo que tiene que llamar, tiene que hacer un "destruir JSONArrays"
        JSONArray *covers = [self generateSuperArray:[pipeModel objectForKey:@"covers"]];
        JSONArray *ditches = [self generateSuperArray:[pipeModel objectForKey:@"ditches"]];
        JSONArray *line = [self generateLineArray:[pipeModel objectForKey:@"line"]];
        JSONArray *covNormals = [self generateSuperArray:[pipeModel objectForKey:@"ditCovers"]];
        
        PipesModel::generateComplexPipe(g,g2,eDiam,iDiam,isT,isC,theClass,theType,eMat,iMat,theId,covers,ditches,line,covSegments,covNormals,planet,mr);
        
        delete covers;
        delete ditches;
        delete line;
        delete covNormals;
    }
    

    /*
     
     for (int i=0;i<array.size();i++){
     JSONObject pipeModel = array.getAsObject(i);
     Geodetic3D g = Geodetic3D.fromDegrees(pipeModel.getAsArray("endPoint").getAsNumber(1).value(),
     pipeModel.getAsArray("startPoint").getAsNumber(0).value(),
     pipeModel.getAsArray("startPoint").getAsNumber(2).value());
     Geodetic3D g2 = Geodetic3D.fromDegrees(pipeModel.getAsArray("endPoint").getAsNumber(1).value(),
     pipeModel.getAsArray("endPoint").getAsNumber(0).value(),
     pipeModel.getAsArray("endPoint").getAsNumber(2).value());
     
     double eDiam = 0, iDiam = 0;
     try {
     eDiam = (double) pipeModel.getAsNumber("eDiam").value();
     iDiam = (double) pipeModel.getAsNumber("iDiam").value();
     }
     catch (Exception e)
     {
     eDiam = (double) pipeModel.getAsNumber("crossSection").value();
     iDiam = (double) pipeModel.getAsNumber("crossSection").value();
     }
     
     boolean isT = false, isC = false;
     try{
     isT = pipeModel.getAsBoolean("isTransportation").value();
     isC = pipeModel.getAsBoolean("isCommunication").value();
     }
     catch (Exception e){}
     
     double covSegments = (double) pipeModel.getAsNumber("covSegments").value();
     double ditSegments = (double) pipeModel.getAsNumber("ditSegments").value();
     
     Cylinder c = new Cylinder(p.toCartesian(g), p.toCartesian(g2), eDiam / 10. );
     c._info.setClassAndType(pipeModel.getAsString("class").toString(),pipeModel.getAsString("type").toString());
     c._info.setMaterials(pipeModel.getAsString("eMat").toString(),pipeModel.getAsString("iMat").toString());
     c._info.setWidths(iDiam,eDiam);
     c._info.setTransportComm(isT,isC);
     c._info.setID(i);
     
     int red; int green;
     if (c._info.cylinderType.contentEquals("naturalGas")){
     red = 255; green = 0;
     }
     else if (c._info.cylinderType.contentEquals("High power")){
     red = 255; green = 255;
     }
     else {
     red = 0; green = 255;
     }
     
     if (Cylinder.isDitchEnabled()){
     Ditch ditch = new Ditch(new Geodetic3D(g.asGeodetic2D(), g._height - 1.0),
     new Geodetic3D(g2.asGeodetic2D(), g2._height - 1.0),
     eDiam / 5);
     
     Mesh pipeMesh = c.createComplexCylinderMesh(Color.fromRGBA255(red,green,0,32), (int) covSegments,
     pipeModel.getAsArray("covers"),pipeModel.getAsArray("covNormals"),pipeModel.getAsArray("line"),p);
     
     // OJO : Tenemos que crear un ditch complejo a partir de lo que tenemos
     Mesh ditchMesh = ditch.createComplexDitchMesh(pipeModel.getAsArray("ditches"), p);
     //delete ditch;
     
     CompositeMesh cm = new CompositeMesh();
     
     cm.addMesh(ditchMesh);
     cm.addMesh(pipeMesh);
     
     mr.addMesh(cm);
     }
     else {
     mr.addMesh(c.createComplexCylinderMesh(Color.fromRGBA255(red,green,0,32), (int) covSegments,
     pipeModel.getAsArray("covers"),pipeModel.getAsArray("covNormals"),pipeModel.getAsArray("line"),p));
     }
     cylinderInfo.add(new Cylinder.CylinderMeshInfo(c._info));
     cylinders.add(c);
     
     }
     Log.e("Log","log");
     
     */
}

- (JSONArray *) generateLineArray:(NSMutableArray *) line{
    JSONArray * theArray = new JSONArray();
    for (unsigned long j=0; j < [line count];j++){
        double x = [[[line objectAtIndex:j] objectAtIndex:0] doubleValue];
        double y = [[[line objectAtIndex:j] objectAtIndex:1] doubleValue];
        double z = [[[line objectAtIndex:j] objectAtIndex:2] doubleValue];
        JSONArray * thePoint = new JSONArray();
        thePoint->add(x); thePoint->add(y); thePoint->add(z);
        theArray->add(thePoint);
    }
    return theArray;
}

- (JSONArray *) generateSuperArray:(NSMutableArray *) covers{
    // [
    //    [  [] [] [] ]
    //    [  [] [] [] ]
    // ]
    
    JSONArray * theArray = new JSONArray();
    for (unsigned long i=0; i< [covers count];i++){
        JSONArray * theCover = new JSONArray();
        for (unsigned long j=0; j < [[covers objectAtIndex:i] count];j++){
            
            double x = [[[[covers objectAtIndex:i] objectAtIndex:j] objectAtIndex:0] doubleValue];
            double y = [[[[covers objectAtIndex:i] objectAtIndex:j] objectAtIndex:1] doubleValue];
            double z = [[[[covers objectAtIndex:i] objectAtIndex:j] objectAtIndex:2] doubleValue];
            JSONArray * thePoint = new JSONArray();
            thePoint->add(x); thePoint->add(y); thePoint->add(z);
            theCover->add(thePoint);
        }
        theArray->add(theCover);
    }
    return theArray;
    
}

- (NSMutableArray *) correctHeightIfNecessaryWithJSON:(NSMutableArray *)json elevData:(const ElevationData *) elevData
{
    if (elevData == NULL)
        return json;
    
    Sector s = elevData->getSector();
    
    for (unsigned long i=0;i<json.count;i++)
    {
        for (unsigned long j=0;j < [[[json objectAtIndex:i] objectForKey:@"covers"] count];j++)
        {
            for (unsigned long k=0;k < [[[[json objectAtIndex:i] objectForKey:@"covers"] objectAtIndex:j] count];k++){
                double lat = [[[[[[json objectAtIndex:i] objectForKey:@"covers"] objectAtIndex:j] objectAtIndex:k] objectAtIndex:1] doubleValue];
                double lon = [[[[[[json objectAtIndex:i] objectForKey:@"covers"] objectAtIndex:j] objectAtIndex:k] objectAtIndex:0] doubleValue];
                double hgt = [[[[[[json objectAtIndex:i] objectForKey:@"covers"] objectAtIndex:j] objectAtIndex:k] objectAtIndex:2] doubleValue];
                if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
                    hgt = hgt + elevData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
                    [[[[[json objectAtIndex:i] objectForKey:@"covers"] objectAtIndex:j] objectAtIndex:k] replaceObjectAtIndex:2 withObject:[NSNumber numberWithDouble:hgt]];
                }
            }
        }

        for (unsigned long j=0;j < [[[json objectAtIndex:i] objectForKey:@"ditches"] count];j++)
        {
            for (unsigned long k=0;k < [[[[json objectAtIndex:i] objectForKey:@"ditches"] objectAtIndex:j] count];k++){
                double lat = [[[[[[json objectAtIndex:i] objectForKey:@"ditches"] objectAtIndex:j] objectAtIndex:k] objectAtIndex:1] doubleValue];
                double lon = [[[[[[json objectAtIndex:i] objectForKey:@"ditches"] objectAtIndex:j] objectAtIndex:k] objectAtIndex:0] doubleValue];
                double hgt = [[[[[[json objectAtIndex:i] objectForKey:@"ditches"] objectAtIndex:j] objectAtIndex:k] objectAtIndex:2] doubleValue];
                if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
                    hgt = hgt + elevData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
                    [[[[[json objectAtIndex:i] objectForKey:@"ditches"] objectAtIndex:j] objectAtIndex:k] replaceObjectAtIndex:2 withObject:[NSNumber numberWithDouble:hgt]];
                }
            }
        }
        
        for (unsigned long j=0;j < [[[json objectAtIndex:i] objectForKey:@"line"] count];j++)
        {
            double lat = [[[[[json objectAtIndex:i] objectForKey:@"line"] objectAtIndex:j] objectAtIndex:1] doubleValue];
            double lon = [[[[[json objectAtIndex:i] objectForKey:@"line"] objectAtIndex:j] objectAtIndex:0] doubleValue];
            double hgt = [[[[[json objectAtIndex:i] objectForKey:@"line"] objectAtIndex:j] objectAtIndex:2] doubleValue];
            if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
                hgt = hgt + elevData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
                [[[[json objectAtIndex:i] objectForKey:@"line"] objectAtIndex:j] replaceObjectAtIndex:2 withObject:[NSNumber numberWithDouble:hgt]];
            }
        }
        
        double lat = [[[[json objectAtIndex:i] objectForKey:@"startPoint"] objectAtIndex:1] doubleValue];
        double lon = [[[[json objectAtIndex:i] objectForKey:@"startPoint"] objectAtIndex:0] doubleValue];
        double hgt = [[[[json objectAtIndex:i] objectForKey:@"startPoint"] objectAtIndex:2] doubleValue];
        if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
            hgt = hgt + elevData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
            [[[json objectAtIndex:i] objectForKey:@"startPoint"] replaceObjectAtIndex:2 withObject:[NSNumber numberWithDouble:hgt]];
        }
        lat = [[[[json objectAtIndex:i] objectForKey:@"endPoint"] objectAtIndex:1] doubleValue];
        lon = [[[[json objectAtIndex:i] objectForKey:@"endPoint"] objectAtIndex:0] doubleValue];
        hgt = [[[[json objectAtIndex:i] objectForKey:@"endPoint"] objectAtIndex:2] doubleValue];
        if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
            hgt = hgt + elevData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
            [[[json objectAtIndex:i] objectForKey:@"endPoint"] replaceObjectAtIndex:2 withObject:[NSNumber numberWithDouble:hgt]];
        }
    }
    return json;
}

@end
