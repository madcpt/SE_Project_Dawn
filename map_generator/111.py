import numpy as np
import cv2

a=np.zeros((100,100))

with open("map.txt") as file:
    for i in range(100):
        b=file.readline()
        for j in range(100):
            a[i][j]=int(b[j])
print(a)

full_forest=cv2.imread("full_forest.png")
grass=cv2.imread("grass.png")
tree=cv2.imread("tree.png")
tmp1=np.hstack((full_forest[25:50,25:50],full_forest[25:50,0:25]))
tmp2=np.hstack((tree[25:50,25:50],tree[25:50,0:25]))
downside=np.vstack((tmp1,tmp2))
tmp1=np.hstack((full_forest[0:25,25:50],full_forest[0:25,0:25]))
tmp2=np.hstack((tree[0:25,25:50],tree[0:25,0:25]))
upside=np.vstack((tmp2,tmp1))
tmp1=np.vstack((full_forest[25:50,0:25],full_forest[0:25,0:25]))
tmp2=np.vstack((tree[25:50,0:25],tree[0:25,0:25]))
leftside=np.hstack((tmp2,tmp1))
tmp1=np.vstack((full_forest[25:50,25:50],full_forest[0:25,25:50]))
tmp2=np.vstack((tree[25:50,25:50],tree[0:25,25:50]))
rightside=np.hstack((tmp1,tmp2))

tmp1=grass[0:25,25:50]
tmp2=tree[0:25,0:25]
tmp3=np.vstack((tree[0:25,0:25],full_forest[0:25,0:25]))
lucorner=np.hstack((np.vstack((tmp1,tmp2)),tmp3))

tmp1=grass[0:25,25:50]
tmp2=tree[25:50,0:25]
tmp3=np.vstack((full_forest[0:25,25:50],tree[25:50,0:25]))
ldcorner=np.hstack((np.vstack((tmp2,tmp1)),tmp3))

tmp1=tree[0:25,25:50]
tmp2=full_forest[25:50,0:25]
tmp3=np.vstack((grass[0:25,0:25],tree[0:25,25:50]))
rucorner=np.hstack((np.vstack((tmp1,tmp2)),tmp3))

tmp1=full_forest[0:25,0:25]
tmp2=tree[25:50,25:50]
tmp3=np.vstack((tree[25:50,25:50],grass[0:25,0:25]))
rdcorner=np.hstack((np.vstack((tmp1,tmp2)),tmp3))

rows=[]
for i in range(100):
    cols=[]
    for j in range(100):
        if a[i][j]==0:
            tmp=grass
        else:
            if a[i-1][j]==1 and a[i][j-1]==1 and a[(i+1)%100][j]==1 and a[i][(j+1)%100]==1:
                tmp=full_forest
            elif a[i-1][j]==1 and a[i][(j+1)%100]==1 and a[(i+1)%100][j]==0 and a[i][j-1]==0:
                tmp=ldcorner
            elif a[i-1][j]==1 and a[i][j-1]==1 and a[(i+1)%100][j]==0 and a[i][(j+1)%100]==0:
                tmp=rdcorner
            elif a[(i+1)%100][j]==1 and a[i][j-1]==1 and a[i-1][j]==0 and a[i][(j+1)%100]==0:
                tmp=rucorner
            elif a[(i+1)%100][j]==1 and a[i][(j+1)%100]==1 and a[(i-1)][j]==0 and a[i][j-1]==0:
                tmp=lucorner

            elif a[i-1][j]==0:
                tmp=upside
            elif a[(i+1)%100][j]==0:
                tmp=downside
            elif a[i][j-1]==0:
                tmp=leftside
            elif a[i][(j+1)%100]==0:
                tmp=rightside
            else:
                tmp=tree

        cols.append(tmp)
    tmpr=np.hstack(tuple(cols))
    rows.append(tmpr)
res=np.vstack(tuple(rows))

cv2.imwrite("map.png",res)
cv2.imshow("q",res)
cv2.waitKey(0)


