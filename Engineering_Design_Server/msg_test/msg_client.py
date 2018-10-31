import socket
import os

TCP_IP = '218.150.181.229'
TCP_PORT = 9001
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

msg = "송이"
msg = msg.encode("utf-8")
s.send(msg)
print("sended message")
data = s.recv(1024)
data = data.decode("utf-8","ignore")
print('data:', data)


print('Successfully message send')
s.close()
print('connection closed')
