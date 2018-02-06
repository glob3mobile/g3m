function footprintBasedWallClassification(lidarfile,footprintfile)
    load(lidarfile);
    load(footprintfile);
    
    %%Given a Lidar file and a Footprint MAT file defining polygons, this
    %%extracts points inside the footprint and send them to be analyzed and
    %%potentially a CityGML file to be generated.
    
    baseStr = 'buildings';
%     i = 11;
    for i=1:size(polygons,1)
       cell = polygons(i,:);
       mm = cell{1};
       
       ind = points(:,1) >= mm(1) & points(:,1) < mm(2) & points(:,2) >= mm(3) & points(:,2) < mm(4);
       buildingArea = points(ind,:);
       buildFile = sprintf('%s/building-%d.gml',baseStr,i);
       analyzeArea(buildingArea,cell,buildFile);
    end
end

function analyzeArea(bArea,cell,bfile)
   if (size(bArea,1) == 0)
        return;
   end
   vx = cell{2};
   vy = cell{3};
   [in,on] = inpolygon(bArea(:,1),bArea(:,2),vx,vy);
   in = in | on;
   
   selected = bArea(in,1:4);
   minZ = min(bArea(in,3));
   %This particular line invokes the magic! (See attached script).
   %generateFinalBuilding2(vx,vy,minZ,selected,bfile);
   generateBuildingsJM(selected);
end

function generateBuildingsJM(selected)


ptCloud = pointCloud(selected(:,1:3))


figure
hold off
pcshow(ptCloud)
xlabel('X(m)')
ylabel('Y(m)')
zlabel('Z(m)')
title('Original Point Cloud')

maxDistance = 0.2; %Distance plane-point

referenceVector = [0,0,1]; %Normal of the plane to extract

maxAngularDistance = 5; %Angular distance from final plane

figure
hold on

xlabel('X(m)')
ylabel('Y(m)')
zlabel('Z(m)')
title('Roof Selection Point Cloud')

remainPtCloud = ptCloud;
while(1)
   %Extracting points in plane
    [model,inlierIndices,outlierIndices] = pcfitplane(remainPtCloud,...
                maxDistance,referenceVector,maxAngularDistance);
    
    if (length(inlierIndices) < 100)
        break;
    end
            
    fprintf('Horizontal plane with %d points found.\nNormal: %f, %f, %f\n\n', length(inlierIndices), model.Normal(1), model.Normal(2), model.Normal(3));
    planePoints = select(remainPtCloud,inlierIndices); %Remaining points
    planePoints.Color = repmat(uint8([0, 0, 255]), length(planePoints.Location), 1);
    k = convhull(planePoints.Location(:,1),planePoints.Location(:,2));
    pcshow(planePoints, 'MarkerSize', 30);
    set = planePoints.Location(k,:);
    plot3(set(:,1),set(:,2),set(:,3),'-r');
    remainPtCloud = select(remainPtCloud,outlierIndices);
end


%Showing just out-of-plane points
remainPtCloud.Color = repmat(uint8([0, 255, 0]), length(remainPtCloud.Location), 1);
pcshow(remainPtCloud, 'MarkerSize', 30)
end