#-*-coding:utf-8-*-
from subprocess import call
import socket
import os
import time
import json
import sys
import datetime
import threading



reload(sys)
sys.setdefaultencoding('utf-8')
TCP_IP = '218.150.182.166'
TCP_PORT = 9001
BUFFER_SIZE = 1
SEND_BUF = 2

tcpsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcpsock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
tcpsock.bind((TCP_IP, TCP_PORT))


tcpsock.listen(5)


# 이미지 파일을 전송받음
while True:
    print ("Waiting for incoming connections...")
    (conn, (ip,port)) = tcpsock.accept()
    print ('   Got connection from ', (ip,port))

     # 사용자가 선택한 운동 종목에 대한 값을 가져옴
    num = conn.recv(4)
    time.sleep(1)
    h = num.decode("utf-8", "ignore")
    h = int(num)
    print('실행한 운동 종목 : '.decode("utf-8") + str(h) +'\n')
    
    # 이미지 크기 받기
    fname = './recvser/' + str(port) + '.jpg'
    data = conn.recv(1024)
    time.sleep(1)
    fsize = data.decode("utf-8","ignore")
    fsize = int(data)
    tnow = datetime.datetime.now()
    print (tnow)
    print( '받아오는 파일크기 : '.decode("utf-8") + str(round(fsize/1024,2)) + 'KB\n')


    # 이미지 받아오기
    total_buffer_size = 0
    with open(fname, 'wb') as f:
        print('receiving data...') 
        while True:
            data = conn.recv(BUFFER_SIZE)
            total_buffer_size += BUFFER_SIZE
            f.write(data)
            if total_buffer_size >= fsize:
                #print('not in data')
                f.close()
                break
        tnow = datetime.datetime.now()
        print (tnow)
        print('file received\n\n')
    
    #openpose 실행

    cmd = '''./build/examples/openpose/openpose.bin --image_dir ./recvser/ --write_images ./rendering_image/ --display 0'''
    cmd2 = '''./build/examples/openpose/openpose.bin --image_dir ./recvser/ --write_json ./forjson/ --display 0'''
    cmd_args = cmd.split()
    cmd2_args = cmd2.split()
    call(cmd_args)
    call(cmd2_args)

    #받은 원본 파일 삭제

    cmd = 'rm -rf ./recvser/' + str(port) +'.jpg'
    cmd2 = 'rm -rf ./recvser/' + str(port) +'.jpg'
    cmd_args = cmd.split()
    cmd2_args = cmd2.split()
 
    call(cmd_args)
    call(cmd2_args)
    	
    while h > 0 and h < 17 :
	if h == 1 : # 팔굽혀펴기 구분동작 1 
         with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'r') as f:
	  data = json.load(f)

	  test = data['people']

	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])
	   user_NeckToLHip = (t1[4] - t1[34]) / (t1[3] - t1[33])	
	   user_RShoulderToHand = (t1[7] - t1[13]) / (t1[6] - t1[12])
	   user_LShoulderToHand = (t1[16] - t1[22]) / (t1[15] - t1[21])

	  except ZeroDivisionError :

	   try :
	    user_NeckToRHip = (t2[4] - t2[25]) / (t2[3] - t2[24])
	    user_NeckToLHip = (t2[4] - t2[34]) /(t2[3] - t2[33])
	    user_RShoulderToHand = (t2[7] - t2[13]) / (t2[6] - t2[12])
	    user_LShoulderToHand = (t2[16] - t2[22]) / (t2[15] - t2[21])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_NeckToRHip = (t3[4] - t3[25]) / (t3[3] - t3[24])
	    user_NeckToLHip = (t3[4] - t3[34]) / (t3[3] - t3[33])
	    user_RShoulderToHand = (t3[7] - t3[13]) / (t3[6] - t3[12])
	    user_LShoulderToHand = (t3[16] - t3[22]) / (t3[15] - t3[21])

	 else :

	  for h in data['people']:
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	   user_NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])	
	   user_RShoulderToHand = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][13]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][12])
	   user_LShoulderToHand = (h['pose_keypoints_2d'][16] - h['pose_keypoints_2d'][22]) / (h['pose_keypoints_2d'][15] - h['pose_keypoints_2d'][21])
 
     	 with open('/home/aict/openpose/forjson/pushup1.json', 'rb') as f:
    	   dict = json.load(f)
	  
    	 for h in dict['people']:
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	  NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])
	  RShoulderToHand = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][13]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][12])
	  LShoulderToHand = (h['pose_keypoints_2d'][16] - h['pose_keypoints_2d'][22]) / (h['pose_keypoints_2d'][15] - h['pose_keypoints_2d'][21])

	 print(1, NeckToRHip, user_NeckToRHip)
	 print(2, RShoulderToHand, user_RShoulderToHand)

	 if abs(user_NeckToRHip - NeckToRHip) > 0.5 : 
	  if abs(user_NeckToRHip) > abs(NeckToRHip) : temp1 = "허리가 일직선으로 유지되야하기 때문에 엉덩이를 내리세요."
	  elif abs(user_NeckToRHip) < abs(NeckToRHip) : temp1 = "허리가 일직선으로 유지되야하기 때문에 엉덩이를 올리세요."
	 else : temp1 = "상체 뒷면은 준수한 자세입니다."
  	 if abs(RShoulderToHand - user_RShoulderToHand) > 30 : temp2 = "팔이 지면과 수직이 되도록 (상체를 앞으로) 이동하세요."
	 else : temp2 = "팔과 어깨는 준수한 자세입니다."
	
	if h == 2 : # 팔굽혀펴기 구분동작 2

	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']

	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])
	   user_NeckToLHip = (t1[4] - t1[34]) / (t1[3] - t1[33])	
	   user_RShoulderToElbow = (t1[7] - t1[10]) / (t1[6] - t1[9])

	  except ZeroDivisionError :

	   try :
	    user_NeckToRHip = (t2[4] - t2[25]) / (t2[3] - t2[24]) 
	    user_NeckToLHip = (t2[4] - t2[34]) / (t2[3] - t2[33])	
	    user_RShoulderToElbow = (t2[7] - t2[10]) / (t2[6] - t2[9])  

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_NeckToRHip = (t3[4] - t3[25]) / (t3[3] - t3[24]) 
	    user_NeckToLHip = (t3[4] - t3[34]) / (t3[3] - t3[33])
	    user_RShoulderToElbow = (t3[7] - t3[10]) / (t3[6] - t3[9])

	 else :

	  for h in data['people']:
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	   user_NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])		
	   user_RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	 
	 with open('/home/aict/openpose/forjson/pushup2.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	  NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])		
	  RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	 print(3, NeckToRHip, user_NeckToRHip) 
	 print(4, RShoulderToElbow, user_RShoulderToElbow)

	 if abs(user_NeckToRHip - NeckToRHip) > 0.5 :
	  if abs(user_NeckToRHip) > abs(NeckToRHip) : temp1 = "허리가 일직선으로 유지되야하기 때문에 엉덩이를 내리세요."
	  elif abs(user_NeckToRHip) < abs(NeckToRHip) : temp1 = "허리가 일직선으로 유지되야하기 때문에 엉덩이를 올리세요."
	 else : temp1 = "상체 뒷면은 준수한 자세입니다"

	 if abs(user_RShoulderToElbow - RShoulderToElbow) > 0.5 :
	  if user_RShoulderToElbow < RShoulderToElbow : temp2 = "상체와 팔근육에 힘이 들어가도록 팔을 더 굽히세요." 
	 else : temp2 = "팔과 어깨는 준수한 자세입니다"
	
        if h ==	3 : # 스쿼트
	  with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	   data = json.load(f)

	   test = data['people']

	  if len(test) > 1 :
	   t1 = data['people'][0]['pose_keypoints_2d']
	   t2 = data['people'][1]['pose_keypoints_2d']

	   try :
	    user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])
	    user_RHipToKnee = (t1[25] - t1[28]) / (t1[24] - t1[27])
	    user_LHipToKnee = (t1[34] - t1[37]) / (t1[33] - t1[36])

	   except ZeroDivisionError :

	    try :
	     user_NeckToRHip = (t2[4] - t2[25]) / (t2[3] - t2[24])
	     user_RHipToKnee = (t2[25] - t2[28]) / (t2[24] - t2[27])
	     user_LHipToKnee = (t2[34] - t2[37]) / (t2[33] - t2[36])

	    except ZeroDivisionError :
	     t3 = data['people'][2]['pose_keypoints_2d']
	     user_NeckToRHip = (t3[4] - t3[25]) / (t3[3] - t3[24])
	     user_RHipToKnee = (t3[25] - t3[28]) / (t3[24] - t3[27])
	     user_LHipToKnee = (t3[34] - t3[37]) / (t3[33] - t3[36])

	  else : 

	   for h in data['people']:
	    user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	    user_RHipToKnee = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][28]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][27])
	    #user_LHipToKnee = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][37]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][36])

	  with open('/home/aict/openpose/forjson/squat.json', 'rb') as f: # read json file
    	   dict = json.load(f)
	
          for h in dict['people']:
  	   NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	   RHipToKnee = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][28]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][27])
	   #LHipToKnee = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][37]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][36])
	  print(5, NeckToRHip, user_NeckToRHip)
	  print(6, RHipToKnee, user_RHipToKnee) 

	  if abs(user_NeckToRHip - NeckToRHip) > 2 :
	   if abs(user_NeckToRHip) > abs(NeckToRHip) : temp1 = "하체근육에 힘이 잘 들어가도록 엉덩이를 뒤로 빼세요."
	   elif abs(user_NeckToRHip) < abs(NeckToRHip) : temp1 = "하체근육에 힘이 잘 들어가도록 상체를 뒤로 세워주세요."
	  else : temp1 = "상체는 준수한 자세입니다."

	  if abs(user_RHipToKnee - RHipToKnee) > 0.4 :
	   if abs(user_RHipToKnee) < abs(RHipToKnee) : temp2 = "하체근육에 힘이 잘 들어가도록 무릎을 피세요."
	   elif abs(user_RHipToKnee) > abs(RHipToKnee) : temp2 = "하체근육에 자극이 느껴지도록 무릎을 굽히세요." 
	  else : temp2 = "하체는 준수한 자세입니다."

        if h == 4 : # 플랭크

	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)

	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']
	  

	  try :
	   user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])
	   user_NeckToLHip = (t1[4] - t1[34]) / (t1[3] - t1[33])	
	   user_RShoulderToElbow = (t1[7] - t1[10]) / (t1[6] - t1[9])

	  except ZeroDivisionError :

	   try :
	    user_NeckToRHip = (t2[4] - t2[25]) / (t2[3] - t2[24])
	    user_NeckToLHip = (t2[4] - t2[34]) / (t2[3] - t2[33])	
	    user_RShoulderToElbow = (t2[7] - t2[10]) / (t2[6] - t2[9])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_NeckToRHip = (t3[4] - t3[25]) / (t3[3] - t3[24])
	    user_NeckToLHip = (t3[4] - t3[34]) / (t3[3] - t3[33])	
	    user_RShoulderToElbow = (t3[7] - t3[10]) / (t3[6] - t3[9])

	 else :

	  for h in data['people']:
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	   user_NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])	
	   user_RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	  # user_LShoulderToElbow = (h['pose_keypoints_2d'][16] - h['pose_keypoints_2d'][19]) / (h['pose_keypoints_2d'][17] - h['pose_keypoints_2d'][20])
	  
	 with open('/home/aict/openpose/forjson/plank.json', 'rb') as f: # read json file
    	   dict = json.load(f)
	
         for h in dict['people']:
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
#	  NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])	
	  RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
#	  LShoulderToElbow = (h['pose_keypoints_2d'][16] - h['pose_keypoints_2d'][19]) / (h['pose_keypoints_2d'][15] - h['pose_keypoints_2d'][18])

	 print(7, NeckToRHip, user_NeckToRHip) 
	 print(8, RShoulderToElbow, user_RShoulderToElbow)

	 if abs(user_NeckToRHip - NeckToRHip) > 0.2 : 
	  if abs(user_NeckToRHip) > abs(NeckToRHip) : temp1 = "복부 근육에 힘이 들어가도록 엉덩이를 내리세요."
	  elif abs(user_NeckToRHip) < abs(NeckToRHip) : temp1 = "복부 근육에 힘이 들어가도록 엉덩이를 올리세요."
	 else : temp1 = "엉덩이와 허리는 준수한 자세입니다."

	 if abs(user_RShoulderToElbow - RShoulderToElbow) > 10 :
	  if abs(user_RShoulderToElbow) < abs(RShoulderToElbow) : temp2 = "지면과 팔이 수직이 되도록 상체를 뒤로 젖히세요."
	 else : temp2 = "팔과 어깨는 준수한 자세입니다."

	if h ==	5 : # 런지 구분동작 1(왼발 앞으로)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  user_LHipToKnee = (t1[34] - t1[37]) / (t1[33] - t1[36])
	  user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])

	 else :

	  for h in data['people']:
	   user_LHipToKnee = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][37]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][36])
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])

         with open('/home/aict/openpose/forjson/rungeright.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  LHipToKnee = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][37]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][36])
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	 print(11, LHipToKnee, user_LHipToKnee)
	 print(12, NeckToRHip, user_NeckToRHip)

	 if abs(user_LHipToKnee - LHipToKnee) > 8 :	
	  if abs(user_LHipToKnee) > abs(LHipToKnee) : temp1 = "허리와 허벅지가 최대한 직각이 되도록 무릎을 피세요."
	  elif abs(user_LHipToKnee) < abs(LHipToKnee) : temp1 = "허리와 허벅지가 최대한 직각이 되도록 무릎을 굽히세요."
	 else : temp1 = "하체는 준수한 자세입니다."

	 if abs(user_NeckToRHip - NeckToRHip) < 10 :
	   if abs(user_NeckToRHip) < abs(NeckToRHip) : temp2 = "하체근육에 힘이 잘 들어가도록 허리를 세워주세요."
	 else : temp2 = "상체는 준수한 자세입니다."

        if h ==	6 : # 런지 구분동작 2(오른발 앞으로)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']

	  user_RHipToKnee = (t1[25] - t1[28]) / (t1[26] - t1[29])
	  user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])

	 else :
 
	  for h in data['people']:
	   user_RHipToKnee = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][28]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][27])
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])

	 with open('/home/aict/openpose/forjson/rungeleft.json', 'rb') as f: # read json file
    	  dict = json.load(f)

         for h in dict['people']:
	  RHipToKnee = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][28]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][27])
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	 print(9, RHipToKnee, user_RHipToKnee)
	 print(10, NeckToRHip, user_NeckToRHip)
  
 	 if abs(user_RHipToKnee - RHipToKnee) > 8 :
	  if abs(user_RHipToKnee) < abs(RHipToKnee) : temp1 = "허리와 허벅지가 최대한 직각이 되도록 오른쪽 무릎을 굽히세요."
	  elif abs(user_RHipToKnee) > abs(RHipToKnee) : temp1 = "허리와 허벅지가 최대한 직각이 되도록 오른쪽 무릎을 피세요."
	 else : temp1 = "무릎을 굽혔을 때 무릎의 각도는 준수합니다."

	 if abs(user_NeckToRHip - NeckToRHip)  < 10 :
	   if abs(user_NeckToRHip) < abs(NeckToRHip) : temp2 = "하체근육에 힘이 잘 들어가도록 허리를 세워주세요."
	 else : temp2 = "무릎을 굽혔을 때 상체의 각도는 준수합니다."

	
        if h ==	7 : # 레그레이즈 구분동작 1 
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_RHipToFoot = (t1[25] - t1[31]) / (t1[24] - t1[30])
	   user_LHipToFoot = (t1[34] - t1[40]) / (t1[33] - t1[39])

	  except ZeroDivisionError :

	   try :
	    user_RHipToFoot = (t2[25] - t2[31]) / (t2[24] - t2[30])
	    user_LHipToFoot = (t2[34] - t2[40]) / (t2[33] - t2[39])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RHipToFoot = (t3[25] - t3[31]) / (t3[24] - t3[30])
	    user_LHipToFoot = (t3[34] - t3[40]) / (t3[33] - t3[39])

	 else :

	  for h in data['people']:	
	   user_RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	   user_LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])

	 with open('/home/aict/openpose/forjson/legraise1.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	  LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])
	 print(13, RHipToFoot, user_RHipToFoot)

	 if abs(user_RHipToFoot - RHipToFoot) > 0.1 : 
	  if abs(user_RHipToFoot) > abs(RHipToFoot) : temp1 = "바닥에 닿지 않고 하복부 근육에 힘이 들어가도록 다리를 더 내리세요"
	  elif abs(user_RHipToFoot) < abs(RHipToFoot) : temp1 = "발뒷꿈치가 바닥에 닿지 않도록 다리를 더 올리세요"
	 else : temp1 = "준수한 자세입니다."
	 temp2 = " "

	if h ==	8 : # 레그레이즈 구분동작 2
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_RHipToFoot = (t1[25] - t1[31]) / (t1[24] - t1[30])
	   user_LHipToFoot = (t1[34] - t1[40]) / (t1[33] - t1[39])

	  except ZeroDivisionError :

	   try :
	    user_RHipToFoot = (t2[25] - t2[31]) / (t2[24] - t2[30])
	    user_LHipToFoot = (t2[34] - t2[40]) / (t2[33] - t2[39])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RHipToFoot = (t3[25] - t3[31]) / (t3[24] - t3[30])
	    user_LHipToFoot = (t3[34] - t3[40]) / (t3[33] - t3[39])

	 else :

	  for h in data['people']:
	   user_RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
 	   user_LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])	 

	 with open('/home/aict/openpose/forjson/legraise2.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	  LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])

	 print(14, RHipToFoot, user_RHipToFoot)

	 if abs(user_RHipToFoot - RHipToFoot) > 0.5 :
	  if abs(user_RHipToFoot) > abs(RHipToFoot) : temp1 = "다리를 너무 올리면 복부에 힘이 안들어가기 때문에 다리를 조금만 내리세요"
	  elif abs(user_RHipToFoot) < abs(RHipToFoot) : temp1 = "복부 근육에 힘이 들어가도록 다리를 조금 더 올리세요"	
	 else : temp1 = "전체적으로 준수한 자세입니다."
	 temp2 = " "

        if h ==	9 : # 마운틴 클라이머 구분동작 1(오른발 앞)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']
	  
	  try :
	   user_RShoulderToElbow = (t1[7] - t1[10]) / (t1[6] - t1[9])
	   user_RHipToKnee = (t1[25] - t1[28]) / (t1[24] - t1[27])

	  except ZeroDivisionError :

	   try :
	    user_RShoulderToElbow = (t2[7] - t2[10]) / (t2[6] - t2[9])
	    user_RHipToKnee = (t2[25] - t2[28]) / (t2[24] - t2[27])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RShoulderToElbow = (t3[7] - t3[10]) / (t3[6] - t3[9])
	    user_RHipToKnee = (t3[25] - t3[28]) / (t3[24] - t3[27])

	 else :

	  for h in data['people']:
	   user_RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	   user_RHipToKnee = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][28]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][27])

	 with open('/home/aict/openpose/forjson/mountainclimberright.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	  RHipToKnee = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][28]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][27])
	 print(15, RShoulderToElbow, user_RShoulderToElbow)
	 print(16, RHipToKnee, user_RHipToKnee)

	 if abs(RShoulderToElbow - user_RShoulderToElbow) > 10 :
	  if user_RShoulderToElbow > RShoulderToElbow : temp1 = "최대한 팔과 지면이 일직선이 되도록 상체를 앞으로 기울이세요." 
	 else : temp1 = "상체는 준수한 자세입니다."

	 if abs(user_RHipToKnee - RHipToKnee) > 1 :
	  if abs(user_RHipToKnee) > abs(RHipToKnee) : temp2 = "복부에 힘이 들어가도록 오른쪽 무릎을 가슴에 최대한 닿도록 해주세요"
	 else : temp2 = "하체는 준수한 자세입니다."

	if h ==	10 : # 마운틴 클라이머 구분동작 2(왼발 앞)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']
	  
	  try :
	   user_RShoulderToElbow = (t1[7] - t1[10]) / (t1[6] - t1[9])
	   user_LHipToKnee = (t1[34] - t1[37]) / (t1[33] - t1[36])

	  except ZeroDivisionError :

	   try :
	    user_RShoulderToElbow = (t2[7] - t2[10]) / (t2[6] - t2[9])
	    user_LHipToKnee = (t2[34] - t2[37]) / (t2[33] - t2[36])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RShoulderToElbow = (t3[7] - t3[10]) / (t3[6] - t3[9])
	    user_LHipToKnee = (t3[34] - t3[37]) / (t3[33] - t3[36])

	 else :

	  for h in data['people']:
	   user_RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	   user_LHipToKnee = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][37]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][36])

	 with open('/home/aict/openpose/forjson/mountainclimberleft.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	  LHipToKnee = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][37]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][36])
	 print(17, RShoulderToElbow, user_RShoulderToElbow)
	 print(18, LHipToKnee, user_LHipToKnee) 

	 if abs(RShoulderToElbow - user_RShoulderToElbow) > 10 :
	  if user_RShoulderToElbow > RShoulderToElbow : temp1 = "최대한 팔과 지면이 일직선이 되도록 상체를 앞으로 기울이세요."
	 else : temp1 = "상체는 준수한 자세입니다"

	 if abs(user_LHipToKnee - LHipToKnee) > 1 :
	  if abs(user_LHipToKnee) > abs(LHipToKnee) : temp2 = "복부에 힘이 들어가도록 왼쪽 무릎을 가슴에 최대한 닿도록 해주세요"
	 else : temp2 = "하체는 준수한 자세입니다"

        if h ==	11 : # 브릿지
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)

	  test = data['people']

	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']
	
	  try :
	   user_RHipToFoot = (t1[25] - t1[31]) / (t1[24] - t1[30])
	   user_LHipToFoot = (t1[34] - t1[40]) / (t1[33] - t1[39])

	  except ZeroDivisionError :

	   try :
	    user_RHipToFoot = (t2[25] - t2[31]) / (t2[24] - t2[30])
	    user_LHipToFoot = (t2[34] - t2[40]) / (t2[33] - t2[39])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RHipToFoot = (t3[25] - t3[31]) / (t3[24] - t3[30])
	    user_LHipToFoot = (t3[34] - t3[40]) / (t3[33] - t3[39])

	 else :

	  for h in data['people']:
	   user_RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	   user_LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])
	
	 with open('/home/aict/openpose/forjson/bridge.json', 'rb') as f: # read json file
    	  dict = json.load(f)

         for h in dict['people']:
	  RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	  LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])
	 print(19, LHipToFoot, user_LHipToFoot)
	 if user_LHipToFoot - LHipToFoot < 0 :
	  if user_LHipToFoot < LHipToFoot : temp1 = "허리부터 무릎까지 최대한 일직선이 되도록 엉덩이를 위로 더 올리세요."
	 else : temp1 = "준수한 자세입니다.19"

        if h ==	12 : # 벤치딥스 구분동작 1
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_RShoulderToElbow = (t1[7] - t1[10]) / (t1[6] - t1[9])
	   user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])
	   user_NeckToLHip = (t1[4] - t1[34]) / (t1[3] - t1[33])

	  except ZeroDivisionError :

	   try :
	    user_RShoulderToElbow = (t2[7] - t2[10]) / (t2[6] - t2[9])
	    user_NeckToRHip = (t2[4] - t2[25]) / (t2[3] - t2[24])
	    user_NeckToLHip = (t2[4] - t2[34]) / (t2[3] - t2[33])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RShoulderToElbow = (t3[7] - t3[10]) / (t3[6] - t3[9])
	    user_NeckToRHip = (t3[4] - t3[25]) / (t3[3] - t3[24])
	    user_NeckToLHip = (t3[4] - t3[34]) / (t3[3] - t3[33])

	 else :

	  for h in data['people']:
  	   user_RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	   user_NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])

	 with open('/home/aict/openpose/forjson/benchdips1.json', 'rb') as f: 
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	  # NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])
	 print(20, RShoulderToElbow, user_RShoulderToElbow)
	 print(21, NeckToRHip, user_NeckToRHip)

	 if abs(RShoulderToElbow - user_RShoulderToElbow) < 2.5 :
	  if abs(user_RShoulderToElbow) < abs(RShoulderToElbow) : temp1 = "의자와 팔이 최대한 수직이 되도록 팔을 일직선으로 피세요"
	 else : temp1 = "팔이 지면과 수직으로 준수한 자세입니다."

	 if abs(user_NeckToRHip - NeckToRHip) > 0.2 :
	  if user_NeckToRHip > NeckToRHip : temp2 = "지면과 허리가 최대한 수직이 되도록 허리를 뒤로 붙이세요"
	 else : temp2 = "허리가 지면과 수직으로 준수한 자세입니다."

	if h ==	13 : # 벤치딥스 구분동작 2
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try : 
	   user_RShoulderToElbow = (t1[7] - t1[10]) / (t1[6] - t1[9])

	  except ZeroDivisionError :

	   try :
	    user_RShoulderToElbow = (t2[7] - t2[10]) / (t2[6] - t2[9])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RShoulderToElbow = (t3[7] - t3[10]) / (t3[6] - t3[9])

	 else :

	  for h in data['people']:
	   user_RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])

	 with open('/home/aict/openpose/forjson/benchdips2.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RShoulderToElbow = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][10]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][9])
	  # LShoulderToElbow = (h['pose_keypoints_2d'][16] - h['pose_keypoints_2d'][19]) / (h['pose_keypoints_2d'][17] - h['pose_keypoints_2d'][20])

	 if abs(user_RShoulderToElbow - RShoulderToElbow) > 1 :
	  if abs(user_RShoulderToElbow) > abs(RShoulderToElbow) : temp1 = "팔과 상체근육에 자극을 주기위해 팔을 최대한 직각이 되도록 굽히세요"
	  elif abs(user_RShoulderToElbow) < abs(RShoulderToElbow) : temp1 = "부상방지를 위해 팔을 너무 굽히지 마세요"
	 else : temp1 = "팔의 각도가 준수합니다."

	if h ==	14 : # 사이드 힙 레이즈 (오른쪽 골반 바닥방향)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_RHipToFoot = (t1[25] - t1[31]) / (t1[24] - t1[30])
	   user_LHipToFoot = (t1[34] - t1[40]) / (t1[33] - t1[39])
	   user_NeckToRHip = (t1[4] - t1[25]) / (t1[3] - t1[24])
	   user_NeckToLHip = (t1[4] - t1[34]) / (t1[3] - t1[33])

	  except ZeroDivisionError :

	   try : 
	    user_RHipToFoot = (t2[25] - t2[31]) / (t2[24] - t2[30])
	    user_LHipToFoot = (t2[34] - t2[40]) / (t2[33] - t2[39])
	    user_NeckToRHip = (t2[4] - t2[25]) / (t2[3] - t2[24])
	    user_NeckToLHip = (t2[4] - t2[34]) / (t2[3] - t2[33])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RHipToFoot = (t3[25] - t3[31]) / (t3[24] - t3[30])
	    user_LHipToFoot = (t3[34] - t3[40]) / (t3[33] - t3[39])
	    user_NeckToRHip = (t3[4] - t3[25]) / (t3[3] - t3[24])
	    user_NeckToLHip = (t3[4] - t3[34]) / (t3[3] - t3[33])

	 else :

	  for h in data['people']:
	   user_RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	   user_LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])
	   user_NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	   user_NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])

	 with open('/home/aict/openpose/forjson/sidehipraise.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	#  RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	#  LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])
	  NeckToRHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][25]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][24])
	  NeckToLHip = (h['pose_keypoints_2d'][4] - h['pose_keypoints_2d'][34]) / (h['pose_keypoints_2d'][3] - h['pose_keypoints_2d'][33])
	 print(23, NeckToLHip, user_NeckToLHip)

	 if user_NeckToLHip - NeckToLHip > -0.1 :
	  if user_NeckToLHip < NeckToLHip : temp1 = "옆구리 근육에 힘이 들어가도록 골반을 더 올리세요"
	 else : temp1 = "전체적으로 준수한 자세입니다."

        if h ==	15 : # 버드독(왼쪽 다리 올림)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:
	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try :
	   user_LHipToFoot = (t1[34] - t1[40]) / (t1[33] - t1[39])
	   user_RShoulderToHand = (t1[7] - t1[13]) / (t1[6] - t1[12])

	  except ZeroDivisionError : 

	   try :
	    user_LHipToFoot = (t2[34] - t2[40]) / (t2[33] - t2[39])
	    user_RShoulderToHand = (t2[7] - t2[13]) / (t2[6] - t2[12])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_LHipToFoot = (t3[34] - t3[40]) / (t3[33] - t3[39])
	    user_RShoulderToHand = (t3[7] - t3[13]) / (t3[6] - t3[12])

	 else :

	  for h in data['people']:
	   user_LHipToFoot = (h['pose_keypoints_2d'][34] - h['pose_keypoints_2d'][40]) / (h['pose_keypoints_2d'][33] - h['pose_keypoints_2d'][39])
	   user_RShoulderToHand = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][13]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][12])

 	 with open('/home/aict/openpose/forjson/birddogleft.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	  RShoulderToHand = (h['pose_keypoints_2d'][7] - h['pose_keypoints_2d'][13]) / (h['pose_keypoints_2d'][6] - h['pose_keypoints_2d'][12])

	 print(24, RHipToFoot, user_LHipToFoot)
	 print(25, RShoulderToHand, user_RShoulderToHand)

	 if abs(user_LHipToFoot - RHipToFoot) > 0.3 :
	  if abs(user_LHipToFoot) > abs(RHipToFoot) : temp1 = "대퇴부에 자극을 주기 위해 최대한 왼쪽 다리를 골반보다 높지 않게 들어주세요."
	  elif abs(user_LHipToFoot) < abs(RHipToFoot) : temp1 = "대퇴부에 자극을 주기 위해 최대한 왼쪽 다리를 골반의 높이만큼 내려주세요."
	 else : temp1 = "하체의 운동자세는 준수합니다."

	# if abs(user_RShoulderToHand - RShoulderToHand) > 0.5 :
	 # if user_RShoulderToHand < RShoulderToHand : temp2 = "등부분에 자극을 주기위해 팔을 더 올리세요"
	 #else : temp2 = "준수한 자세입니다.25"

	if h ==	16 : # 버드독(오른쪽 다리 올림)
	 with open('/home/aict/openpose/forjson/' + str(port) + '_keypoints.json', 'rb') as f:

	  data = json.load(f)
	  test = data['people']
	 
	 if len(test) > 1 :
	  t1 = data['people'][0]['pose_keypoints_2d']
	  t2 = data['people'][1]['pose_keypoints_2d']

	  try : 
	   user_RHipToFoot = (t1[25] - t1[31]) / (t1[24] - t1[30])

	  except ZeroDivisionError :

	   try :
	    user_RHipToFoot = (t2[25] - t2[31]) / (t2[24] - t2[30])

	   except ZeroDivisionError :
	    t3 = data['people'][2]['pose_keypoints_2d']
	    user_RHipToFoot = (t3[25] - t3[31]) / (t3[24] - t3[30])

	 else :

	  for h in data['people']:
	   user_RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	 

	 with open('/home/aict/openpose/forjson/birddogright.json', 'rb') as f: # read json file
    	  dict = json.load(f)
	
         for h in dict['people']:
	  RHipToFoot = (h['pose_keypoints_2d'][25] - h['pose_keypoints_2d'][31]) / (h['pose_keypoints_2d'][24] - h['pose_keypoints_2d'][30])
	 print(26, RHipToFoot, user_RHipToFoot)

	 if abs(user_RHipToFoot - RHipToFoot) > 0.3 :
	  if abs(user_RHipToFoot) > abs(RHipToFoot) : temp1 = "대퇴부에 자극을 주기 위해 오른쪽 다리를 골반보다 높지 않게 들어주세요"
	  elif abs(user_RHipToFoot) < abs(RHipToFoot) : temp1 = "대퇴부에 자극을 주기 위해 최대한 오른쪽 다리를 골반의 높이만큼 내려주세요."
 	 else : temp1 = "하체의 운동자세는 준수합니다."

	# if user_LShoulderToHand - LShoulderToHand > 0.2 :
	 # if user_LShoulderToHand < LShoulderToHand : temp = "등부분에 자극을 주기위해 팔을 더 올리세요"
	 #else : temp = "준수한 자세입니다.27"

    #json 파일 경로 './forjson/' +str(port) + '_keypoints.json'

    # 결과 클라이언트로 전송(이미지)
    filename='./rendering_image/' + str(port) + '_rendered.png'
    f = open(filename,'rb')
    ssize = os.path.getsize(filename)
   
    # 결과메시지 전송
    # temp="결과메시지"
    try :
     temp = temp1 + "\n" + temp2
    except NameError :
     temp = temp1
    print('보내는 결과 메세지 : '.decode("utf-8") + temp + '\n') 
    conn.send(unicode(temp))
    time.sleep(1)
 
    

    print('보내는 결과 파일크기 : '.decode("utf-8") +  str(round(ssize/1024,2)) + 'KB\n')

    temp = str(ssize)
    temp = temp.encode("utf-8")
    conn.send(temp)
    time.sleep(1)
    tnow = datetime.datetime.now()
    print (tnow)
    print('이미지 전송을 시작합니다.'.decode("utf-8"))

    total_size = 0
    while True:
        print('Sendding...')
        while True:
            l = f.read(SEND_BUF)
            conn.send(l)
            total_size += SEND_BUF
            #print('\ndata:', len(l))
            if total_size >= ssize:
                f.close()
                break
        break
    tnow = datetime.datetime.now()
    print (tnow)
    print('sended result\n')
