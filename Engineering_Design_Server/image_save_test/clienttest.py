import socket
import os

TCP_IP = '218.150.181.230'
TCP_PORT = 9001
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

filename="C:\\Users\\AICT\\Desktop\\image_save_test\\test.jpg"
f = open(filename,'rb')

fsize = os.path.getsize(filename)
# 이미지를 서버로 전송
print('보내는 파일크기 : ' +  str(fsize/1024) + 'KB')

msg = str(fsize)
msg = msg.encode("utf-8")
s.send(msg)

while True:
    l = f.read(BUFFER_SIZE)
    print('Sendding...')
    while (l):
        s.send(l)
        l = f.read(BUFFER_SIZE)
    if not l:
        f.close()
        break
#전송받은 메세지 출력
print('waiting message')

data = s.recv(1024)
data = data.decode("utf-8","ignore")
print('data:', data)

#while True:
#    data = s.recv(1024)
#    print('data:', data)
#    if not data:
#        break
#    else:
#        data = data.decode("utf-8","ignore")
#        print(data)


print('Successfully image send')
s.close()
print('connection closed')
