import socket
import os

TCP_IP = '218.150.181.230'
TCP_PORT = 9001
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))


msg = "hi"
msg = msg.encode("utf-8")
s.send(msg)

print('Successfully image send')
s.close()
print('connection closed')
