function [g,w,r] = generateFinalBuilding2(vx,vy,vz,points,bfile)
    %% FOOTPRINT %%
    fpSize = size(vx,1);
    fpZ = ones(fpSize,1) * vz;
    fpVertices = [(1:fpSize)' ,vx,vy,fpZ];
    fpJoins = zeros(fpSize,2);
    
    %% Saving footprint as ground
    g = fliplr(fpVertices(:,2:4)');
    g = g';
    
    for i=2:fpSize
       fpJoins(i-1,:) = [i-1,i]; 
    end
    fpJoins(fpSize,:) = [fpSize,1];
    
    %% Let's triangulate something!
    idx = clusterdata(points(:,3),'criterion','distance','cutoff',0.05);
%     figure;
%     scatter3(points(:,1),points(:,2),points(:,3),100,idx,'filled');
    gS = size(points,1);
    theCells = {};
    idx = fuseClusters(idx,points);
    clustCt = 1;
    for i=1:max(idx)
       set = points(idx == i,:);
       s = size(set,1);
       pct = (s/gS) * 100;
       maxH = max(set(:,3));
       minH = min(set(:,3));
%        meanH = mean(set(:,3));
       stdH = std(set(:,3));
       difH = maxH - minH;
       
       if (pct > 20)
            idx2 = clusterdata(set(:,1:3),'criterion','distance','cutoff',2);
%             figure;
%             scatter3(set(:,1),set(:,2),set(:,3),100,idx2,'filled');
            if (max(idx2) > 1)
                for j=1:max(idx2)
                    set2 = set(idx2 == j,:);
                    s2 = size(set2,1);
                    pct2 = (s2/gS) * 100;
                    maxH2 = max(set2(:,3));
                    minH2 = min(set2(:,3));
%                     meanH2 = mean(set2(:,3));
                    stdH2 = std(set2(:,3));
                    difH2 = maxH2 - minH2;
                    if (pct2 > 10)
                        %% Outer roof polygon extraction
                         a = curves(set2,clustCt,1);
                         theCells = [theCells;a];
                         %% Add inner roof polygons whenever needed.
                         theCells = innerPolygons(theCells,set2,clustCt,difH2,stdH2,minH2,maxH2);
                         clustCt = clustCt + 1;
                    end
                end
            else
                %% Outer roof polygon extraction
                a = curves(set,clustCt,1);
                theCells = [theCells;a];
                %% Add inner roof polygons whenever needed.
                theCells = innerPolygons(theCells,set,clustCt,difH,stdH,minH,maxH);
                clustCt = clustCt + 1;
            end
            
       end
    end
    
    wVertices = []; wJoins = [];
    if (size(theCells,1) == 1)
        %% Only a single flat roof
        [wVertices,wJoins,w,r] = singleRoofs(theCells,fpVertices,true);
        bbox = generateBBOX(g,r);
        gCell = {g};
        rCell = {r};
        wCell = {w};
        cityGMLGenerator(bfile,bbox,gCell,wCell,rCell,'28 N');
    else
        example = zeros(size(theCells,1),1);
        for i=1:size(theCells,1)
            example(i) = theCells{i,2};
        end
        if (length(example(example == 1)) == 1)
            %% Only a single non-flat roof.
            [wVertices,wJoins,w,r] = singleComplexRoofs(theCells,fpVertices);
            bbox = generateBBOX(g,r);
            gCell = {g};
            rCell = {r};
            wCell = {w};
            cityGMLGenerator(bfile,bbox,gCell,wCell,rCell,'28 N');
        elseif (isempty(example(example > 1))) 
            %% More than one flat roof 
            [wVertices,wJoins,w,r] = multipleNormalRoofs(theCells,fpVertices,true);
        else
            %% Nemesis cases.
            [wVertices,wJoins,w,r] = multipleComplexRoofs(theCells,fpVertices);
        end
    end

    %% Here we paint what we get!
    vertices = [fpVertices; wVertices];
    joins = [fpJoins; wJoins];
    
    figure;
    hold on;
    for i=1:size(joins,1)
        v1 = vertices(joins(i,1),2:4);
        v2 = vertices(joins(i,2),2:4);
        v = [v1;v2];
        color = '-b';
%         if (buildingToBeGenerated)
%             color = '-g';
%         end
        plot3(v(:,1),v(:,2),v(:,3),color);
    end
    hold off;
    
end

function [bbox] = generateBBOX(g,r)
    %% TODO BBOX
    minX = min(g(:,1));
    maxX = max(g(:,1));
    minY = min(g(:,2));
    maxY = max(g(:,2));
    minZ = min(g(:,3));
    maxZ = max(r(:,4));
    bbox = [minX,minY,minZ;maxX,maxY,maxZ];
end

function [wV,wJ,w,r] = multipleComplexRoofs(theCells, fV)
    wV = []; wJ = []; w = []; r = [];
    %% TODO: completar w y r
    theProcessedRoofs = [];
    example = zeros(size(theCells,1),1);
    for i=1:size(theCells,1)
        example(i) = theCells{i,1};
    end
    lastVertex = size(fV,1);
    for i=1:max(unique(example))
        impliedCells = theCells(example == i,:);
        %% Techo inferior
        outerCell = impliedCells(1,:);
        jHull = hullTest(outerCell{1,3},outerCell{1,4});
        meanX = min(jHull(:,3));
        wSize = size(jHull,1);
        s = lastVertex + wSize;
        halfS = lastVertex + 1;
        
        wV2 = zeros(wSize,4);
        wV2(:,1) = halfS:s;
        wV2(:,2:3) = jHull(:,1:2);
        wV2(:,4) = ones(wSize,1)*meanX;
        %% Generating roof
        wJ2 = zeros(wSize,2);
        for j=2:wSize
            wJ2(j-1,:) = [wV2(j-1,1),wV2(j,1)];
        end
        wJ2(wSize,:) = [wV2(wSize,1),wV2(1,1)];
        
        a = cell(1,2);
        a{1,1} = wV2; a{1,2} = wJ2;
        theProcessedRoofs = [theProcessedRoofs; a];     
        
        %% Adding complexity to ecuation
        lastV = wV2;
        for j=2:size(impliedCells,1)
            [wV3,wJ3,r] = innerRoof(impliedCells(j,:),lastV,r);
            wV2 = [wV2; wV3]; wJ2 = [wJ2; wJ3];
            lastV = wV3;
        end
        wV = [wV;wV2]; wJ = [wJ; wJ2];
        lastVertex = wV(end,1);
    end
    %% Look for FP-near vertices
    wJ2 = zeros(size(fV,1),2);
    for i=1:size(fV,1)
        vx = fV(i,2);
        vy = fV(i,3);
        index = -1; gDt = Inf;
        for j=1:size(wV,1)
            dt = sqrt( ((vx - wV(j,2))^2) + ((vy - wV(j,3))^2) );
            if (dt < gDt)
               gDt = dt;
               index = j;
            end
        end
        %% Joining and correcting if needed.
        wJ2(i,:) = [wV(index,1);fV(i,1)];
        if isVertexNearFootprintPair(wV(index,2:4),fV(i,2:4))
            wV(index,2:3) = fV(i,2:3);
%             wJ2(i,:) = [wV(index,1);fV(i,1)];
        end
    end
    wJ3 = wJ2;
    wJ3(1:size(wJ3,1)-1,2) = wJ3(2:size(wJ3,1),1);
    wJ3(size(wJ3,1),2) = wJ3(1,1);
    
    %wJ = [wJ; wJ2(wJ2(:,1) > 0,:)];
    wJ = [wJ; wJ2; wJ3];
    %% Checking for fusioned roofs
    [wV, wJ] = fuseRoofsIfNeeded(theProcessedRoofs,wV,wJ);
            
end

function [wV,wJ,w,r] = multipleNormalRoofs(theCells, fV, useMean)
    wV = []; wJ = []; w = []; r = [];
    %% TODO: completar w y r
    lastVertex = fV(end,1);
    theProcessedRoofs = [];
    %% Process roofs
    for i=1:size(theCells,1)
        convexHull = theCells{i,3};
        concaveHull = theCells{i,4};
        jHull = hullTest(convexHull,concaveHull);
        if useMean
            meanX = mean(jHull(:,3));
        else
            meanX = min(jHull(:,3));
        end
        %% Generate roof vertices and add it into collection
        rSize = size(jHull,1);
        halfS = lastVertex+1; s = lastVertex + rSize;
        wV2 = zeros(rSize,4);
        wV2(:,1) = halfS:s;
        wV2(:,2:3) = jHull(:,1:2);
        wV2(:,4) = ones(rSize,1)*meanX;
        %% Generate roof joins and add it into collection
        wJ2 = zeros(rSize,2);
        for j=2:rSize
            wJ2(j-1,:) = [wV2(j-1,1),wV2(j,1)];
        end
        wJ2(rSize,:) = [wV2(rSize,1),wV2(1,1)];
        
        wV = [wV; wV2]; 
        wJ = [wJ; wJ2];
        a = cell(1,2);
        a{1,1} = wV2; a{1,2} = wJ2;
        theProcessedRoofs = [theProcessedRoofs; a];
        lastVertex = wV(end,1);
    end
    %% Look for FP-near vertices
    wJ2 = zeros(size(fV,1),2);
    for i=1:size(fV,1)
        vx = fV(i,2);
        vy = fV(i,3);
        index = -1; gDt = Inf;
        for j=1:size(wV,1)
            dt = sqrt( ((vx - wV(j,2))^2) + ((vy - wV(j,3))^2) );
            if (dt < gDt)
               gDt = dt;
               index = j;
            end
        end
        %% Joining and correcting if needed.
        wJ2(i,:) = [wV(index,1);fV(i,1)];
        if isVertexNearFootprintPair(wV(index,2:4),fV(i,2:4))
            wV(index,2:3) = fV(i,2:3);
        end
    end
    wJ = [wJ; wJ2];
    [wV, wJ] = fuseRoofsIfNeeded(theProcessedRoofs,wV,wJ);
end

function [wV,wJ] = fuseRoofsIfNeeded(theProcessedRoofs,wV,wJ)    
    for i=1:(size(theProcessedRoofs,1)-1)
        v1 = theProcessedRoofs{i,1};
        for j=i+1:size(theProcessedRoofs,1)
            v2 = theProcessedRoofs{j,1};
            z1 = v1(1,4);
            z2 = v2(1,4);
            if (z1 > z2)
                %% Superior: v1, inferior: v2
                in = pointsInPolygon(v2(:,2:4),v1(:,2:4));
                
                if (~isempty(in(in == 1)))
                    %% Things should be done;
                    set = v2(in == 1,:);
                    covered = false(size(wJ,1),1);
                    for x=1:size(set,1)
                       covered = covered | wJ(:,1) == set(x,1) | wJ(:,2) == set(x,1);
                    end
                    wJ = wJ(~covered,:);
                    
                    pVertex = v2(v2(:,1) == (set(1,1) - 1),:);
                    fVertex = v2(v2(:,1) == (set(end,1) + 1),:);
                    
                    phVertex = findNearestVertex(pVertex,v1);
                    fhVertex = findNearestVertex(fVertex,v1);
                    %% Generate new joinable vertices %%
                    wV2 = zeros(2,4);
                    s = wV(end,1);
                    wV2(:,1) = s+1:s+2;
                    wV2(:,2:3) = [phVertex(1,2:3);fhVertex(1,2:3)];
                    wV2(:,4) = [pVertex(1,4); fVertex(1,4)];
                    wV = [wV; wV2];
                   %% Generate extra joins
                    wJ2 = zeros(5,2);
                    wJ2(1,:) = [wV2(1,1);wV2(2,1)];
                    wJ2(2,:) = [wV2(1,1);phVertex(1,1)];
                    wJ2(3,:) = [wV2(1,1);pVertex(1,1)];
                    wJ2(4,:) = [wV2(2,1);fhVertex(1,1)];
                    wJ2(5,:) = [wV2(2,1);fVertex(1,1)];
                    wJ = [wJ;wJ2];
                end
            else
                %% Superior: v2, inferior: v1
                in = pointsInPolygon(v1(:,2:4),v2(:,2:4));
                
                if (~isempty(in(in == 1)))
                    set = v1(in == 1,:);
                    covered = false(size(wJ,1),1);
                    for x=1:size(set,1)
                       covered = covered | wJ(:,1) == set(x,1) | wJ(:,2) == set(x,1);
                    end
                    wJ = wJ(~covered,:);
                    
                    pVertex = v1(v1(:,1) == (set(1,1) - 1),:);
                    fVertex = v1(v1(:,1) == (set(end,1) + 1),:);
                    
                    phVertex = findNearestVertex(pVertex,v2);
                    fhVertex = findNearestVertex(fVertex,v2);
                    %% Generate new joinable vertices %%
                    wV2 = zeros(2,4);
                    s = wV(end,1);
                    wV2(:,1) = s+1:s+2;
                    wV2(:,2:3) = [phVertex(1,2:3);fhVertex(1,2:3)];
                    wV2(:,4) = [pVertex(1,4); fVertex(1,4)];
                    wV = [wV; wV2];
                   %% Generate extra joins
                    wJ2 = zeros(5,2);
                    wJ2(1,:) = [wV2(1,1);wV2(2,1)];
                    wJ2(2,:) = [wV2(1,1);phVertex(1,1)];
                    wJ2(3,:) = [wV2(1,1);pVertex(1,1)];
                    wJ2(4,:) = [wV2(2,1);fhVertex(1,1)];
                    wJ2(5,:) = [wV2(2,1);fVertex(1,1)];
                    wJ = [wJ;wJ2];
                end
                
            end
        end
    end
    
end

function v = findNearestVertex(vertex,fV)
    index = -1; gDt = Inf;
    for i=1:size(fV,1)
        vx = fV(i,2);
        vy = fV(i,3);
        dt = sqrt( ((vx - vertex(1,2))^2) + ((vy - vertex(1,3))^2) );
        if (dt < gDt)
            gDt = dt;
            index = i;
        end
    end
    v = fV(index,:);
end

function [wV,wJ,w,r] = singleRoofs(theCells, fV, useMean)
    %% TODO: completar w y r
    convexHull = theCells{1,3};
    concaveHull = theCells{1,4};
    theJoinedHulls = hullTest(convexHull,concaveHull);
    if useMean
        meanX = mean(theJoinedHulls(:,3));
    else
        meanX = min(theJoinedHulls(:,3));
    end
    s = size(fV,1) * 2;
    halfS = size(fV,1) + 1;
    wSize = size(fV,1);
    wV = fV;
    wV(:,1) = halfS:s;
    wV(:,4) = ones(wSize,1)*meanX;
    
    r = [ones(wSize,1),wV(:,2:4);];
    
    %% Generating roof
    wJ = zeros(s,2);
    for i=2:wSize
        wJ(i-1,:) = [wV(i-1,1),wV(i,1)];
    end
    wJ(wSize,:) = [wV(wSize,1),wV(1,1)];
    %% Generating walls 
    ct = wSize + 1;
    for i=1:wSize
        wJ(ct,:) = [wV(i,1),fV(i,1)];
        ct = ct+1;
    end
    %% Generate r polygons
    w = zeros(wSize*5,4); ct = 1;
    for i=2:wSize
       baseIndex = (i-1)*5;
       w(baseIndex+1,:) = [ct, wV(i-1,2:4)];
       w(baseIndex+2,:) = [ct, fV(i-1,2:4)];
       w(baseIndex+3,:) = [ct, fV(i,2:4)];
       w(baseIndex+4,:) = [ct, wV(i,2:4)];
       w(baseIndex+5,:) = [ct, wV(i-1,2:4)];
       ct = ct+1;
    end
    w(1,:) = [ct, wV(i-1,2:4)];
    w(2,:) = [ct, fV(i-1,2:4)];
    w(3,:) = [ct, fV(i,2:4)];
    w(4,:) = [ct, wV(i,2:4)];
    w(5,:) = [ct, wV(i-1,2:4)];
    
end

function [wV,wJ,w,r] = singleComplexRoofs(theCells, fV)
    theFirstCell = theCells(1,:);
    [wV,wJ,w,r] = singleRoofs(theFirstCell,fV,false);
    lastV = wV;
    for i=2:size(theCells,1)
        [wV2,wJ2,r] = innerRoof(theCells(i,:),lastV,r);
        wV = [wV; wV2]; wJ = [wJ; wJ2];
        lastV = wV2;
    end
end

function [wV, wJ,r] = innerRoof(theCell, fV,r)
    convexHull = theCell{1,3};
    concaveHull = theCell{1,4};
    jHull = hullTest(convexHull,concaveHull);
    
    %% Assigning ALL jHull vertices as official vertices
    wSize = size(jHull,1);
    startVertex = fV(end,1) + 1;
    endVertex = fV(end,1) + wSize;
    jSize = size(jHull,1) + size(fV,1);
    
    minZ =  min(jHull(:,3));
    wV = zeros(wSize,4);
    wV(:,1) = startVertex:endVertex;
    wV(:,2:3) = jHull(:,1:2);
    wV(:,4) = ones(wSize,1)*minZ;
    
    try
        ct = max(r(:,1)) + 1;
    catch
        ct = 1;
    end
    
    %% Inner roof polygon for CityGML.
    r2 = [ones(wSize,1)*ct,wV(:,2:4) ; ct,wV(1,2:4)];
    ct = ct + 1;
    
    %% Creating inner roof
    wJ = zeros(jSize,2);
    for i=2:wSize
        wJ(i-1,:) = [wV(i-1,1),wV(i,1)];
    end
    wJ(wSize,:) = [wV(wSize,1),wV(1,1)];
    
    indStr = zeros(size(fV,1),1);
    
    %% Look for closer inner point to outer point. Join them. Victory.
    for i=1:size(fV,1)
        vx = fV(i,2);
        vy = fV(i,3);
        index = -1; gDt = Inf;
        for j=1:size(jHull,1)
            dt = sqrt( ((vx - jHull(j,1))^2) + ((vy - jHull(j,2))^2));
            if (dt < gDt)
                gDt = dt;
                index = j;
            end
        end
        indStr(i) = index;
        wJ(wSize+i,:) = [wV(index,1);fV(i,1)];
    end
    
    %% The same for walls.
    r3 = zeros(size(fV,1)*5,4);
    for i=2:size(fV,1)
       baseIndex = (i-1)*5;
       r3(baseIndex+1,:) = [ct, fV(i-1,2:4)];
       r3(baseIndex+2,:) = [ct, wV(indStr(i-1),2:4)];
       r3(baseIndex+3,:) = [ct, wV(indStr(i),2:4)];
       r3(baseIndex+4,:) = [ct, fV(i,2:4)];
       r3(baseIndex+5,:) = [ct, fV(i-1,2:4)];
       ct = ct+1;
    end
    r3(1,:) = [ct, fV(i-1,2:4)];
    r3(2,:) = [ct, wV(indStr(i-1),2:4)];
    r3(3,:) = [ct, wV(indStr(i),2:4)];
    r3(4,:) = [ct, fV(i,2:4)];
    r3(5,:) = [ct, fV(i-1,2:4)];
    
    r = [r ; r2; r3];
    
end

function [in] = pointsInPolygon(points,p)
    [in,on] = inpolygon(points(:,1),points(:,2),p(:,1),p(:,2));
    in = in | on;
end

function b = isVertexNearFootprintPair(v,f)
    b = true;
    if (v(1) < f(1)-1.5) || (v(1) > f(1)+1.5) || (v(2) < f(2)-1.5) || (v(2) > f(2)+1.5)
        b = false;
    end
end

function theJoinedHulls = hullTest(convexHull,concaveHull)
    HULL_THRESHOLD = 15;
    theJoinedHulls = [];
%     fprintf('----------------------------------- \n')
    for i=2:size(convexHull,1)
        inferior = find(concaveHull(:,1) == convexHull(i-1,1) & ...
            concaveHull(:,2) == convexHull(i-1,2));
        superior = find(concaveHull(:,1) == convexHull(i,1) & ...
            concaveHull(:,2) == convexHull(i,2));
        hullPolygon = concaveHull(inferior:superior,:);
        hullPolygon = [hullPolygon;concaveHull(inferior,:)];
        theArea = polyarea(hullPolygon(:,1),hullPolygon(:,2));
%         fprintf('Area between %d and %d - %f \n',i-1,i,theArea);
        if (theArea > HULL_THRESHOLD)
            theJoinedHulls = [theJoinedHulls;concaveHull(inferior:superior-1,:)];
        else
            theJoinedHulls = [theJoinedHulls;convexHull(i-1,:)];
        end
    end
    theJoinedHulls = [theJoinedHulls;theJoinedHulls(1,:)];
%     fprintf('----------------------------------- \n')
    
end

function idx2 = fuseClusters(idx,points)
    idx2 = idx;
    gS = size(points,1);
    fused = zeros(max(idx),1);
    for i=1:max(idx)
       set = points(idx == i,:);
       s = size(set,1);
       pct = (s/gS) * 100;
       meanH = mean(set(:,3));
       if (pct > 20)
            for j=i+1:max(idx)
                if (~fused(j))
                    set2 = points(idx == j,:);
                    s2 = size(set2,1);
                    pct2 = (s2/gS) * 100;
                    meanH2 = mean(set2(:,3));
                    if (pct2 > 20 && abs(meanH - meanH2) < 0.5)
                       fused(j) = 1;
                       idx2(idx2 == j) = i;
                    end
                end
            end
       end
    end
       
end

function a = curves(set,index,subindex)
%             TRI = delaunay(set(:,1),set(:,2),set(:,3));
%             figure;
%             trimesh(TRI,set(:,1),set(:,2),set(:,3));
    k = convhull(set(:,1),set(:,2));
    k2 = boundary(set(:,1),set(:,2));
%             figure;
%             plot3(set(:,1),set(:,2),set(:,3),'.b',set(k,1),set(k,2),set(k,3),'-r',set(k2,1),set(k2,2),set(k2,3),'-g');
    k3 = alphaShape(set(:,1),set(:,2),5);
    k4 = boundaryFacets(k3);
            
    kV = zeros(size(k,1),3);
    k2V = zeros(size(k2,1),3);
    k4V = zeros(size(k4,1),6);
    kV(:,1:3) = set(k,1:3);
    k2V(:,1:3) = set(k2,1:3);
    k4V(:,1:3) = set(k4(:,1),1:3);
    k4V(:,4:6) = set(k4(:,2),1:3);
            
    a = cell(1,5);
    a{1} = index;
    a{2} = subindex;
    a{3} = kV;
    a{4} = k2V;
    a{5} = k4V;
end

function theCells = innerPolygons(theCells,set,index,difH,stdH,minH,maxH)
    if (difH < 2 || stdH < 0.4)
        return;
    end
    
    PROGRESS = 0.5;
    ct = 2;
    for i=minH:PROGRESS:maxH
        theLowerSet = set(set(:,3) >= i,:);
        a = curves(theLowerSet,index,ct);
        ct = ct+1;
        theCells = [theCells;a];
    end
end