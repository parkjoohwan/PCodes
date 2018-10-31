import socket
from threading import Thread

TCP_IP = '218.150.181.230'
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

# 이미지 파일을 전송받음
while True:
    print ("Waiting for incoming connections...")
    (conn, (ip,port)) = tcpsock.accept()
    print ('   Got connection from ', (ip,port))
    newthread = ClientThread(ip,port,conn)
    newthread.start()

    fname = 'C:\\Users\\AICT\\Desktop\\image_save_test\\save\\recv_pic' + str(port) + '.jpg'
    data = conn.recv(1024)
    print('받아오는 파일크기 : ' + data.decode("utf-8","ignore") + '\n')

    threads.append(newthread)

#예측 결과 클라이언트로 전송(문자열)
#    msg = "송이"
#    msg = msg.encode("utf-8")
    #conn.send(msg)
    #print("sended message")
    #threads.append(newthread)

for t in threads:
    t.join()
