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
    fsize = data.decode("utf-8","ignore")
    print('받아오는 파일크기 : ' + fsize)
    fsize = int(data.decode("utf-8","ignore"))

    total_buffer_size = 0
    with open(fname, 'wb') as f:
        print('receiving data...')
        while True:
            data = conn.recv(BUFFER_SIZE)
            total_buffer_size += BUFFER_SIZE
            #print('\ndata:', len(data))
            #if not data:
            print(fsize , '/' , total_buffer_size)
            if total_buffer_size >= fsize:
                print('not in data')
                f.close()
                break
            f.write(data)
        print('file received')

    msg = "송이"
    msg = msg.encode("utf-8")
    conn.send(msg)
    print("sended message")

    threads.append(newthread)


# 파일의 저장이 끝나면 저장된 이미지로 예측
# 예측 결과를 다시 클라이언트로 보내줌
# 파일의 전송이 끝났다는 구분 필요


#예측 결과 클라이언트로 전송(문자열)
#    msg = "송이"
#    msg = msg.encode("utf-8")
    #conn.send(msg)
    #print("sended message")
    #threads.append(newthread)

for t in threads:
    t.join()
