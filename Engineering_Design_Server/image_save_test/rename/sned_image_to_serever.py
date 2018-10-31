import socket

TCP_IP = '218.150.181.230'
TCP_PORT = 9001
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

filename='test.jpg'
f = open(filename,'rb')
while True:
    l = f.read(BUFFER_SIZE)
    print('Sendding...')
    while (l):
        s.send(l)
        #print('Sent ',repr(l))
        l = f.read(BUFFER_SIZE)
    if not l:
        f.close()
        s.close()
        break
print('Successfully image send the file')
s.close()
print('connection closed')
