import Image, ImageDraw
import math as m
import numpy as np


""" MODULE images2gif

Provides a function (writeGif) to write animated gif from a series
of PIL images or numpy arrays.

This code is provided as is, and is free to use for all.

Almar Klein (June 2009)

- based on gifmaker (in the scripts folder of the source distribution of PIL)
- based on gif file structure as provided by wikipedia

"""

import PIL
from PIL import Image, ImageChops
from PIL.GifImagePlugin import getheader, getdata
import numpy as np


# getheader gives a 87a header and a color palette (two elements in a list).
# getdata()[0] gives the Image Descriptor up to (including) "LZW min code size".
# getdatas()[1:] is the image data itself in chuncks of 256 bytes (well
# technically the first byte says how many bytes follow, after which that
# amount (max 255) follows).


def intToBin(i):
    """ Integer to two bytes """
    # devide in two parts (bytes)
    i1 = i % 256
    i2 = int( i/256)
    # make string (little endian)
    return chr(i1) + chr(i2)


def getheaderAnim(im):
    """ Animation header. To replace the getheader()[0] """
    bb = "GIF89a"
    bb += intToBin(im.size[0])
    bb += intToBin(im.size[1])
    bb += "\x87\x00\x00"
    return bb


def getAppExt(loops=0):
    """ Application extention. Part that secifies amount of loops. 
    if loops is 0, if goes on infinitely.
    """
    bb = "\x21\xFF\x0B"  # application extension
    bb += "NETSCAPE2.0"
    bb += "\x03\x01"
    if loops == 0:
        loops = 2**16-1
    bb += intToBin(loops)
    bb += '\x00'  # end
    return bb


def getGraphicsControlExt(duration=0.1):
    """ Graphics Control Extension. A sort of header at the start of
    each image. Specifies transparancy and duration. """
    bb = '\x21\xF9\x04'
    bb += '\x08'  # no transparancy
    bb += intToBin( int(duration*100) ) # in 100th of seconds
    bb += '\x00'  # no transparant color
    bb += '\x00'  # end
    return bb


def _writeGifToFile(fp, images, durations, loops):
    """ Given a set of images writes the bytes to the specified stream.
    """
    
    # init
    frames = 0
    previous = False
    
    for im in images:        
        if not previous:
            palette = getheader(im)[1]
            data = getdata(im)
            imdes, data = data[0], data[1:]            
            header = getheaderAnim(im)
            appext = getAppExt(loops)
            graphext = getGraphicsControlExt(durations[0])
            
            # write global header
            fp.write(header)
            fp.write(palette)
            fp.write(appext)
            
            # write image
            fp.write(graphext)
            fp.write(imdes)
            for d in data:
                fp.write(d)
            
        else:
            # gather info (compress difference)              
            data = getdata(im) 
            imdes, data = data[0], data[1:]       
            graphext = getGraphicsControlExt(durations[frames])
            
            # write image
            fp.write(graphext)
            fp.write(imdes)
            for d in data:
                fp.write(d)
        previous = im.copy()        
        frames = frames + 1

    fp.write(";")  # end gif
    return frames


def writeGif(filename, images, duration=0.1, loops=0, dither=1):
    """ writeGif(filename, images, duration=0.1, loops=0, dither=1)
    Write an animated gif from the specified images. 
    images should be a list of numpy arrays of PIL images.
    Numpy images of type float should have pixels between 0 and 1.
    Numpy images of other types are expected to have values between 0 and 255.
    """
    
    if PIL is None:
        raise RuntimeError("Need PIL to write animated gif files.")
    
    images2 = []
    
    # convert to PIL
    for im in images:
        
        if isinstance(im,Image.Image):
            images2.append( im.convert('P',dither=dither) )
            
        elif np and isinstance(im, np.ndarray):
            if im.dtype == np.uint8:
                pass
            elif im.dtype in [np.float32, np.float64]:
                im = (im*255).astype(np.uint8)
            else:
                im = im.astype(np.uint8)
            # convert
            if len(im.shape)==3 and im.shape[2]==3:
                im = Image.fromarray(im,'RGB').convert('P',dither=dither)
            elif len(im.shape)==2:
                im = Image.fromarray(im,'L').convert('P',dither=dither)
            else:
                raise ValueError("Array has invalid shape to be an image.")
            images2.append(im)
            
        else:
            raise ValueError("Unknown image type.")
    
    # check duration
    if hasattr(duration, '__len__'):
        if len(duration) == len(images2):
            durations = [d for d in duration]
        else:
            raise ValueError("len(duration) doesn't match amount of images.")
    else:
        durations = [duration for im in images2]
        
    
    # open file
    fp = open(filename, 'wb')
    
    # write
    try:
        n = _writeGifToFile(fp, images2, durations, loops)
        print n, 'frames written'
    finally:
        fp.close()
    
    
if __name__ == '__main__':
    im = np.zeros((200,200), dtype=np.uint8)
    im[10:30,:] = 100
    im[:,80:120] = 255
    im[-50:-40,:] = 50
    

    

Axe_x = 0
Axe_y = 1
Axe_z = 2

class GTSParseur:
	def __init__(self,f):
	   self.sommets=[]
	   self.segments=[]
	   self.triangles=[]

	   ofi = open(f,'r')
	   lignes = ofi.readlines()
	   [nbsommets,nbsegments,nbtriangles]=[int(x) for x in lignes[0].split()]

	   for i in range(1,nbsommets+1):
	       l=lignes[i].split()
	       self.sommets += [(float(l[0]),float(l[1]),float(l[2]))]

	   for i in range(nbsommets+1,nbsegments+nbsommets+1):
	       l=lignes[i].split()
	       self.segments += [(int(l[0])-1,int(l[1])-1)]

	   for i in range(nbsegments+nbsommets+1,nbtriangles+nbsegments+nbsommets+1):
	       l=lignes[i].split()
	       self.triangles += [(int(l[0])-1,int(l[1])-1,int(l[2])-1)]


class RepereOrtho:
    def __init__(self, im, maxI=5):
        self.centre_x = im.size[0]/2
        self.centre_y = im.size[1]/2
        self.draw = ImageDraw.Draw(im)
        self.unite = im.size[0]/(2*maxI)

    def pixel_correspondant (self,(x,y)):
        x1 = self.centre_x + self.unite*x
        y1 = self.centre_y - self.unite*y
        return (int(x1),int(y1))


class Obj3D:
    def __init__(self, f):
        gtsp = GTSParseur(f)
        self.sommets = gtsp.sommets
        self.segments = gtsp.segments
        self.triangles = gtsp.triangles

    def dessine_fil_de_fer(self,repere):
        xyp = [repere.pixel_correspondant((x,y)) for [x,y,_] in self.sommets]
        for t in self.triangles:
            for s in t :
                repere.draw.line([xyp[self.segments[s][0]],xyp[self.segments[s][1]]],"white")

    def translation(self,Tx,Ty,Tz):
        self.sommets = [(Tx+x,Ty+y,Tz+z) for (x,y,z) in self.sommets]

    def rotation(self, alpha, Axe):
        if (Axe==Axe_x):
            rot = np.array([[1,0,0], [0, m.cos(alpha), -m.sin(alpha)],[0, m.sin(alpha), m.cos(alpha)]])
        elif (Axe==Axe_y):
            rot = np.array([[m.cos(alpha), 0, m.sin(alpha)], [0,1,0],[-m.sin(alpha), 0, m.cos(alpha)]])
        elif (Axe==Axe_z):
            rot = np.array([[m.cos(alpha), -m.sin(alpha), 0], [m.sin(alpha), m.cos(alpha),0],[0,0,1]])
        self.sommets = [np.dot(rot,[x,y,z]) for (x,y,z) in self.sommets ]


# # vaisseau = Obj3D("x_wing.gts")
# im = Image.new("L",(800,600))
# repere = RepereOrtho(im,50)
# # vaisseau.dessine_fil_de_fer(repere)
# vaisseau2 = Obj3D("x_wing.gts")
# vaisseau2.rotation(m.pi/4, Axe_z)
# vaisseau2.dessine_fil_de_fer(repere)

imgs = []
vaisseau = Obj3D("x_wing.gts")
for _ in range(40) :
    im = Image.new("L",(800,600))
    repere = RepereOrtho(im,50)
    vaisseau.rotation(0.05*np.pi, Axe_y )
    vaisseau.dessine_fil_de_fer(repere)
    imgs += [im]
writeGif('x_wing.gif',imgs)


im.show()





