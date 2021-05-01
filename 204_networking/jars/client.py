import socket, threading, time, sys

class ClientThread(threading.Thread):
  def __init__(self,clientsocket):
    threading.Thread.__init__(self)
    self.csocket = clientsocket
    self.killed = False
  
  def listen(self):
    while True:
      msg = ''
      data = self.csocket.recv(2048)
      msg = data.decode()
      if msg:
        print(msg, end="")
      else:
        break

  def run(self):
    print("Hello.! Welcome to the chatroom.\nInstructions:\n1. Simply type the message to send broadcast to all active clients\n2. Type '@username<space>yourmessage' without quotes to send message to desired client\n3. Type 'WHOISIN' without quotes to see list of active clients\n4. Type 'LOGOUT' without quotes to logoff from server")
    connected = True
    listen_th = threading.Thread(target=self.listen,args=())
    listen_th.daemon = True
    listen_th.start()
    while connected:
      time.sleep(0.3)
      data = input("> ")
      if (data.lower() == "whoisin"):
        data = "users\n"
      elif (data.lower() == "logout"):
        data = "exit\n"
      else:
        data = "msg" + data + "\n"
      try:
        self.csocket.send(data.encode())
      except ConnectionAbortedError as e:
        connected = False
      if (data == "exit\n"):
        connected = False
    print("*** Server has closed the connection *** ")
    self.csocket.close()

# change host and port to whatever you want
host = '127.0.0.1'
port = 8081

if (len(sys.argv) != 3):
  print("argument error: python3 <file> <host> <port>")
  exit(0)
host = sys.argv[1]
port = int(sys.argv[2])

try:
  s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s.connect((host,port))
  name = input("input name: ")
  name = name + "\n"
  s.send(name.encode())
except ConnectionRefusedError as e:
  print(e)
  exit(0)

# start the threads
newthread = ClientThread(s)
newthread.start()