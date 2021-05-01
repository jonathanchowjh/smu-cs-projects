# change host and port to whatever you want
host = '127.0.0.1'
port = 8081

if (len(sys.argv) != 2):
  print("argument error: python3 <file> <port>")
  exit(0)
port = int(sys.argv[1])

try:
  s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s.connect((host,port))
  name = input("input name: ")
  name = name + "\n"
  s.send(name.encode())
except ConnectionRefusedError as e:
  print(e)
  exit(0