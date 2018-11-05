import socket
from threading import Thread

TCP_IP = '218.150.181.229'
TCP_PORT = 9001
BUFFER_SIZE = 1024

tcpsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcpsock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
tcpsock.bind((TCP_IP, TCP_PORT))
threads = []


class ClientThread(Thread):

    def __init__(self,ip,port,sock):
        Thread.__init__(self)
        self.ip = ip
        self.port = port
        self.sock = sock
        print ("   New thread started for "+ip+":"+str(port))


tcpsock.listen(5)

while True:
    print ("Waiting for incoming connections...")
    (conn, (ip,port)) = tcpsock.accept()
    print ('   Got connection from ', (ip,port))
    #newthread = ClientThread(ip,port,conn)
    #newthread.start()

    data = conn.recv(1024)
    print('data : ' + data.decode("utf-8","ignore"))
#예측 결과 클라이언트로 전송(문자열)
    conn.send(data)
    print("sended message")
    #threads.append(newthread)

for t in threads:
    t.join()
