function cityGMLGenerator(filename,bbox,ground,walls,roofs,utmZone)
    %% Definiendo constantes 
    
    xmlStr = '<?xml version="1.0" encoding="utf-8"?> \n';
    cityModelOpen = '<CityModel xmlns:veg="http://www.opengis.net/citygml/vegetation/2.0" xmlns:tran="http://www.opengis.net/citygml/transportation/2.0" xmlns:xAL="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dem="http://www.opengis.net/citygml/relief/2.0" xmlns:fme="http://www.safe.com/xml/xmltables" xmlns:gml="http://www.opengis.net/gml" xmlns:bldg="http://www.opengis.net/citygml/building/2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.opengis.net/citygml/2.0"> \n';
    cityModelClose = '</CityModel>\n';
    prototypeStr = '<gml:name>Prototype</gml:name> \n';
    gmlBoundedByOpen = '<gml:boundedBy>\n';
    gmlBoundedByClose = '</gml:boundedBy>\n';
    envelopeOpen = '<gml:Envelope srsDimension="3" srsName="EPSG:4326"> \n';
    envelopeClose = '</gml:Envelope>\n';
    lowerCornerOpen = '<gml:lowerCorner>';
    lowerCornerClose = '</gml:lowerCorner>\n';
    upperCornerOpen = '<gml:upperCorner>';
    upperCornerClose = '</gml:upperCorner>\n';
    memberOpen = '<cityObjectMember>\n';
    memberClose = '</cityObjectMember>\n';
    buildingOpen1 = '<bldg:Building gml:id=';
    buildingOpen2 = '> \n'; %% Ojo ID?
    buildingClose = '</bldg:Building> \n';
    bldgBoundedByOpen = '<bldg:boundedBy>\n';
    bldgBoundedByClose = '</bldg:boundedBy>\n';
    groundSurfaceOpen = '<bldg:GroundSurface> \n';
    groundSurfaceClose = '</bldg:GroundSurface> \n'; %% Ojo ID?
    wallSurfaceOpen = '<bldg:WallSurface> \n';
    wallSurfaceClose = '</bldg:WallSurface> \n'; %% Ojo ID?
    roofSurfaceOpen = '<bldg:RoofSurface> \n';
    roofSurfaceClose = '</bldg:RoofSurface> \n'; %% Ojo ID?
    lod2MultiSurfaceOpen = '<bldg:lod2MultiSurface> \n';
    lod2MultiSurfaceClose = '</bldg:lod2MultiSurface> \n';
    gmlMultiSurfaceOpen = '<gml:MultiSurface gml:srsDimension="3">\n'; %%Ojo id?
    gmlMultiSurfaceClose = '</gml:MultiSurface>\n';
    surfaceMemberOpen = '<gml:surfaceMember>\n';
    surfaceMemberClose = '</gml:surfaceMember>\n';
    polygonOpen = '<gml:Polygon>\n'; %% Ojo id?
    polygonClose = '</gml:Polygon>\n';
    exteriorOpen = '<gml:exterior>\n';
    exteriorClose = '</gml:exterior>\n';
    linearRingOpen = '<gml:LinearRing>\n'; %%Ojo id?
    linearRingClose = '</gml:LinearRing>\n';
    posListOpen = '<gml:posList>';
    posListClose = '</gml:posList>\n';     
    
    %% Let's start composing the file!
    cityGMLString = sprintf('%s%s%s',xmlStr,cityModelOpen,prototypeStr);
    %% Generate bbox
    cityGMLString = strcat(cityGMLString,gmlBoundedByOpen,envelopeOpen);
    [lowerText,upperText] = bboxAsText(bbox,utmZone);
    cityGMLString = strcat(cityGMLString,lowerCornerOpen,lowerText,lowerCornerClose);
    cityGMLString = strcat(cityGMLString,upperCornerOpen,upperText,upperCornerClose);
    cityGMLString = strcat(cityGMLString,envelopeClose,gmlBoundedByClose);
    
    %% Para cada building, la estructura será:
    for i=1:size(ground,2)
        singleGround = ground{1,i};
        bWalls = walls{1,i};
        bRoofs = roofs{1,i};
    
        cityGMLString = strcat(cityGMLString,memberOpen,buildingOpen1,...
            sprintf('"%s-%d"',filename,i),buildingOpen2);
        %% Generar ground. Hay un único ground por edificio.
        cityGMLString = strcat(cityGMLString,bldgBoundedByOpen,groundSurfaceOpen);
        cityGMLString = strcat(cityGMLString,lod2MultiSurfaceOpen,gmlMultiSurfaceOpen);
        cityGMLString = strcat(cityGMLString,surfaceMemberOpen,polygonOpen);
        cityGMLString = strcat(cityGMLString,exteriorOpen,linearRingOpen);
        [groundText] = polygonAsText(singleGround,utmZone);
        cityGMLString = strcat(cityGMLString,posListOpen,groundText,posListClose);
        cityGMLString = strcat(cityGMLString,linearRingClose,exteriorClose);
        cityGMLString = strcat(cityGMLString,polygonClose,surfaceMemberClose);
        cityGMLString = strcat(cityGMLString,gmlMultiSurfaceClose,lod2MultiSurfaceClose);
        cityGMLString = strcat(cityGMLString,groundSurfaceClose,bldgBoundedByClose);
        
        %% Generar walls. Hay varios walls por edificio
        %% Para cada wall individual
        
        for j= 1:max(unique(bWalls(:,1)))
            singleWall = bWalls(bWalls(:,1) == j,2:4);
        
            cityGMLString = strcat(cityGMLString,bldgBoundedByOpen,wallSurfaceOpen);
            cityGMLString = strcat(cityGMLString,lod2MultiSurfaceOpen,gmlMultiSurfaceOpen);
            cityGMLString = strcat(cityGMLString,surfaceMemberOpen,polygonOpen);
            cityGMLString = strcat(cityGMLString,exteriorOpen,linearRingOpen);
            [wallText] = polygonAsText(singleWall,utmZone);
            cityGMLString = strcat(cityGMLString,posListOpen,wallText,posListClose);
            cityGMLString = strcat(cityGMLString,linearRingClose,exteriorClose);
            cityGMLString = strcat(cityGMLString,polygonClose,surfaceMemberClose);
            cityGMLString = strcat(cityGMLString,gmlMultiSurfaceClose,lod2MultiSurfaceClose);
            cityGMLString = strcat(cityGMLString,wallSurfaceClose,bldgBoundedByClose);

        end
        
        %% Generar Roofs. Pueden haber uno o varios roofs por edificio.
        %% Posiblemente serán varios.
        %% Para cada roof individual:
        
        for j= 1:max(unique(bRoofs(:,1)))
            singleRoof = bRoofs(bRoofs(:,1) == j,2:4);
    
            cityGMLString = strcat(cityGMLString,bldgBoundedByOpen,roofSurfaceOpen);
            cityGMLString = strcat(cityGMLString,lod2MultiSurfaceOpen,gmlMultiSurfaceOpen);
            cityGMLString = strcat(cityGMLString,surfaceMemberOpen,polygonOpen);
            cityGMLString = strcat(cityGMLString,exteriorOpen,linearRingOpen);
            [roofText] = polygonAsText(singleRoof,utmZone);
            cityGMLString = strcat(cityGMLString,posListOpen,roofText,posListClose);
            cityGMLString = strcat(cityGMLString,linearRingClose,exteriorClose);
            cityGMLString = strcat(cityGMLString,polygonClose,surfaceMemberClose);
            cityGMLString = strcat(cityGMLString,gmlMultiSurfaceClose,lod2MultiSurfaceClose);
            cityGMLString = strcat(cityGMLString,roofSurfaceClose,bldgBoundedByClose);
        
        end
        %% Cerrando edificio.
        cityGMLString = strcat(cityGMLString,buildingClose,memberClose);
    end

    %% Close the file.
    cityGMLString = strcat(cityGMLString,cityModelClose);
    %% Save
    %save(filename,'cityGMLString','-ascii');
    fileID = fopen(filename,'w');
    fprintf(fileID,cityGMLString);
    fclose(fileID);
               
end

function [polyText] = polygonAsText(polygon,utmZone)
%     [lat,lon] = utm2deg(polygon(1,1),polygon(1,2),utmZone);
    [lat,lon] = utm2ll(polygon(1,1),polygon(1,2),28);
    polyText = sprintf('%0.8f %0.8f %0.2f',lon,lat,polygon(1,3));
     
    for i=2:size(polygon,1)
%         [lat,lon] = utm2deg(polygon(i,1),polygon(i,2),utmZone);
        [lat,lon] = utm2ll(polygon(i,1),polygon(i,2),28);
        polyText = strcat(polyText,sprintf(' %0.8f %0.8f %0.2f',lon,lat,polygon(i,3)));
    end
end

function [lowerText,upperText] = bboxAsText(bbox,utmZone)
%     [llat,llon] = utm2deg(bbox(1,1),bbox(1,2),utmZone);
%     [ulat,ulon] = utm2deg(bbox(2,1),bbox(2,2),utmZone);
    [llat,llon] = utm2ll(bbox(1,1),bbox(1,2),28);
    [ulat,ulon] = utm2ll(bbox(2,1),bbox(2,2),28);
    lowerText = sprintf('%0.8f %0.8f %0.2f',llon,llat,bbox(1,3));
    upperText = sprintf('%0.8f %0.8f %0.2f',ulon,ulat,bbox(2,3));
end