I = imread('untitled.jpg');
%BW = im2bw(I,0.5);
%SE = strel('disk',5);

% I1=I(1:size(I,1)/2,1:size(I,2)/2,:);
% I2=I(size(I,1)/2+1:size(I,1),1:size(I,2)/2,:);
% I3=I(1:size(I,1)/2,size(I,2)/2+1:size(I,2),:);
% I4=I(size(I,1)/2+1:size(I,1),size(I,2)/2+1:size(I,2),:);
% 
BW = im2bw(I,0.5);
SE = strel('disk',5);
% 
% figure,imshow(I1);
% figure,imshow(I2);
% figure,imshow(I3);
% figure,imshow(I4);

BW2 = imdilate(BW,SE);
BW3 = imerode(BW2,SE);
BW4 = imdilate(BW3,SE);
I1=BW3(1:size(I,1)/2,1:size(I,2)/2,:);
%I2=I(size(I,1)/2+1:size(I,1),1:size(I,2)/2,:);
%I3=I(1:size(I,1)/2,size(I,2)/2+1:size(I,2),:);
% I4=I(size(I,1)/2+1:size(I,1),size(I,2)/2+1:size(I,2),:);
% BW5 = imdilate(BW4,SE);
% BW6 = imdilate(BW5,SE);
% BW7 = imdilate(BW6,SE);
% BW8 = imdilate(BW7,SE);   
% BW9 = imdilate(BW8,SE);
% BW10 = imdilate(BW9,SE);
% 
[centers,radii] = imfindcircles(I1,[9 15],'ObjectPolarity','dark','Sensitivity',0.94);
k = size(centers);
disp(k);
% %figure,imshow(BW4);
% %figure,imshow(BW5);
% %figure,imshow(BW6);
% %figure,imshow(BW7);
figure,imshow(BW3);
h = viscircles(centers,radii);