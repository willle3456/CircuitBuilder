import cv2
#import numpy as np
from matplotlib import pyplot as plt
import collections

#helpers for removing components that are not the ones we want
def is_within(low, high, x):
    return low <= x <= high

def is_inside(rects, next_rect):
    for r in rects:
        if is_within(r[0], r[2], next_rect[0]) and is_within(r[0], r[2], next_rect[2]):
            if is_within(r[1], r[3], next_rect[1]) and is_within(r[1], r[3], next_rect[3]):
                return True
    return False

#invert binary image
def invert_image(img):
    for i in range(len(img)):
        for j in range(len(img[0])):
            img[i][j] = abs(255-img[i][j])

#load image
img = cv2.imread('test_images/test.jpg', cv2.IMREAD_UNCHANGED)
img = cv2.resize(img, (1600, 900))
#preprocessing
kernel = cv2.getStructuringElement(cv2.MORPH_CROSS, (5,5))
blurred_img = cv2.GaussianBlur(cv2.cvtColor(img, cv2.COLOR_RGB2GRAY), (13,13), 0)
thres_img = cv2.adaptiveThreshold(blurred_img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,\
        cv2.THRESH_BINARY, 11, 2)
invert_image(thres_img)
closed_img = cv2.morphologyEx(thres_img, cv2.MORPH_CLOSE, kernel)
#canny_img = cv2.Canny(closed_img, 100, 200)
cv2.imshow('test', closed_img)
#contouring and bounding boxes
img2, contours, hierarchy = cv2.findContours(closed_img, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
imgs = []
pts = []
elements = {}
for c in contours:
    x,y,w,h = cv2.boundingRect(c)
    tmp2 = [x,y,x+w, y+h]
    if not is_inside(pts, tmp2) and w > 10 and h > 10:
        pts.append(tmp2)
        tmp = closed_img[y:y+h, x:x+w]
        imgs.append(tmp)
        elements[x] = tmp
cv2.namedWindow('test', cv2.WINDOW_NORMAL)
#cv2.resizeWindow('image', 600, 600)
ordered_elements = collections.OrderedDict(sorted(elements.items()))
#resizing subimages for detection
for i in range(len(imgs)):
    imgs[i] = cv2.resize(imgs[i], (28, 28), cv2.INTER_AREA)
    plt.subplot((len(imgs)/3 +1), 3,i+1), plt.imshow(imgs[i], 'gray')
plt.show()
print(ordered_elements)
key = cv2.waitKey(0)
if key == 27:
    cv2.destroyAllWindows()
