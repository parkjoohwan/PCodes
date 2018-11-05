import socket
import os
import sys
import struct
import message

from message import Message
from message_header import Header
from message_body import BodyData, BodyRequest, BodyResponse, BodyResult
from message_util import MessageUtil

TCP_IP = '218.150.181.229'
TCP_PORT = 9001
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

filename='test.jpg'
f = open(filename,'rb')

requestMsg = Message()
filesize = os.path.getsize(filepath)

#요청메시지 바디 생성 - 파일전송요청 객체 생성
requestMsg.Body = BodyRequest(None)
requestMsg.Body.FILESIZE = filesize
requestMsg.Body.FILENAME = filepath[filepath.rindex("\\")+1:]

#요청메시지 헤더 생성
requestMsg.Header = Header(None)
requestMsg.Header.MSGID = 1
requestMsg.Header.MSGTYPE = message.REQ_FILE_SEND
requestMsg.Header.BODYLEN = requestMsg.Body.GetSize()
requestMsg.Header.FRAGMENTED = message.NOT_FRAGMENTED
requestMsg.Header.LASTMSG = message.LASTMSG
requestMsg.Header.SEQ = 0

#요청메시지 전송
MessageUtil.send(sock, requestMsg)

 #응답메시지 읽기
responseMsg = MessageUtil.receive(sock)
if responseMsg.Header.MSGTYPE != message.REP_FILE_SEND: #파일응답코드가 아니면 종료
    exit(0)
if responseMsg.Body.RESPONSE ==message.DENIED:  #서버가 거부하면 종료
    exit(0)



# 이미지를 서버로 전송

while True:
    l = f.read(filesize)
    print('Sendding...')
    while (l):
        s.send(l)
        l = f.read(filesize)
    if not l:
        f.close()
        print("sended or none file")
        break

#파일전송메시지 전송
MessageUtil.send(sock, fileMsg)




print('Successfully image send')
s.close()
print('connection closed')
