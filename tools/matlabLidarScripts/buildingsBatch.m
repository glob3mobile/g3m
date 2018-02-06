
function buildingsBatch(name)
     s1 = sprintf('%s.las',name);
    s2 = sprintf('%s.mat',name);
    s3 = sprintf('%s-expected.mat',name);
    
%%USE THIS COMMENTED BLOCK IF YOU HAVE STILL A .LAS FILE INSTEAD OF A .MAT ONE    
%     readLas(s1,s3);
%     copyMat(s3,s2);

    
    footprintBasedWallClassification(s2,'polygons.mat');
end

% function splitBigLidarSet(s2,s21,s22,s23,s24)
%     load(s2);
    
%     [maxX,maxY,minX,minY] = generalStats(points);
%     meanX = minX + ((maxX - minX) / 2);
%     meanY = minY + ((maxY - minY) / 2);
%     ind1 = points(points(:,1) < meanX & points(:,2) < meanY,:);
%     ind2 = points(points(:,1) >= meanX & points(:,2) < meanY,:);
%     ind3 = points(points(:,1) < meanX & points(:,2) >= meanY,:);
%     ind4 = points(points(:,1) >= meanX & points(:,2) >= meanY,:);
%     theSave(s21,header,ind1);
%     theSave(s22,header,ind2);
%     theSave(s23,header,ind3);
%     theSave(s24,header,ind4);
% end

% function theSave(file,header,points)
%     save(file,'header','points');
% end

function copyMat(s3,s2)
    load(s3);
    %points(:,5) = 0;
    save(s2,'header','points');
end