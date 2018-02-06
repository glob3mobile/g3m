clear
close all;

%load('object3d.mat') %Test

load('aBuilding.mat');
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
    pcshow(planePoints, 'MarkerSize', 30) 
    remainPtCloud = select(remainPtCloud,outlierIndices);
end


%Showing just out-of-plane points
remainPtCloud.Color = repmat(uint8([0, 255, 0]), length(remainPtCloud.Location), 1);
pcshow(remainPtCloud, 'MarkerSize', 30)