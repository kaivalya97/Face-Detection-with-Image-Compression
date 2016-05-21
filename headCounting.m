imageName = 'rank492.jpg';
img = imread(imageName);
binarized = im2bw(img, 0.5);
disk = strel('disk',5);     
%creating morphological structuring element as dish

dilation1 = imdilate(binarized, disk);
erode1 = imerode(dilation1, disk);
dilation2 = imdilate(erode1, disk);


% imgUpper =  dilation2(1:size(dilation2,1)/3, 1:size(dilation2,2));
% subplot(2,1,1);
% imshow(imgUpper);
% 
% imgLower = dilation2((size(dilation2,1)/3):size(dilation2,1)...
%     ,1:size(dilation2,2));
% subplot(2,1,2);
% imshow(imgLower);

% [centers,radii] = imfindcircles(imgUpper,[9 15],'ObjectPolarity','dark','Sensitivity',0.931);
% k = size(centers);
% 
% disp(k);
% figure,imshow(imgUpper);
[centers,radii] = imfindcircles(dilation2,[9 15],'ObjectPolarity','dark','Sensitivity',0.932);
k = size(centers);
 
disp(k);
 figure,imshow(dilation2);
 h = viscircles(centers,radii);
 
 text(10, 10, strcat('\color{red} Heads Found = ', num2str(k)));
 hold on

