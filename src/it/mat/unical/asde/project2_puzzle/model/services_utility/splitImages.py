#!/usr/bin/env python
# -*- coding:utf-8 -*-
import os
import sys
import glob
from PIL import Image

src_img: sys.argv[1]
dst_path = sys.argv[2]
k = 1;
os.chdir(src_img)
for file in glob.glob("*.*"):
    print(file)

    im = Image.open(file)
    im_w, im_h = im.size
    w, h = int(sys.argv[3]), int(sys.argv[4])
    w_num, h_num = int(im_w / w), int(im_h / h)
    k = int(k) + 1
    for wi in range(0, w_num):
        for hi in range(0, h_num):
            box = (wi * w, hi * h, (wi + 1) * w, (hi + 1) * h)
            piece = im.crop(box)
            tmp_img = Image.new('RGB', (w, h), 255)
            tmp_img.paste(piece)
            sPP = str(k)
            img_path = os.path.join(dst_path, sPP + "\%d-%d.png" % (wi, hi))
            tmp_img.save(img_path)

