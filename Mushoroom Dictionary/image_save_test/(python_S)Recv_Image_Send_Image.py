import socket
import os
import time

TCP_IP = '218.150.181.230'
TCP_PORT = 9001
BUFFER_SIZE = 1024

tcpsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcpsock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
tcpsock.bind((TCP_IP, TCP_PORT))


tcpsock.listen(5)

# 이미지 파일을 전송받음
while True:
    print ("Waiting for incoming connections...")
    (conn, (ip,port)) = tcpsock.accept()
    print ('   Got connection from ', (ip,port))

    fname = 'C:\\Users\\AICT\\Desktop\\Server_fin\\recv_image\\recv_pic' + str(port) + '.jpg'
    data = conn.recv(1024)
    fsize = data.decode("utf-8","ignore")
    fsize = int(data)
    print('받아오는 파일크기 : ' + str(round(fsize/1024,2)) + 'KB\n')

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
        print('file received\n')

    # 예측 결과 클라이언트로 전송(문자열)
    #msg = "송이버섯 :  균모는 육질이고 지름 5~15(30)cm이며 구형 또는 반구형에서 편평하게되며 중앙부가 둔하게 돌출된다."
    #msg = msg.encode("utf-8")
    #conn.send(msg)
    #print("sended message")

    # 예측 결과 클라이언트로 전송(이미지)
    filename="C:\\Users\\AICT\\Desktop\\Client_fin\\Client\\src\\test3.jpg";
    sf = open(filename,'rb')
    ssize = os.path.getsize(filename)
    # 이미지를 서버로 전송
    print('보내는 파일크기 : ' +  str(round(ssize/1024,2)) + 'KB')

    msg = str(ssize)
    msg = msg.encode("utf-8")
    conn.send(msg)
    time.sleep(1)

    total_size = 0
    while True:
        print('Sendding...')
        while True:
            l = sf.read(BUFFER_SIZE)
            conn.send(l)
            total_size += BUFFER_SIZE
            print('\ndata:', len(l))
            if total_size >= ssize:
                f.close()
                break
        break

    print('sended result\n')
