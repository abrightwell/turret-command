import socket
import sys

def main():
    HOST, PORT = "localhost", 9999
    
    keep_running = True
    
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((HOST, PORT))
    
    while(keep_running):
        command = sys.stdin.readline().strip()
        if (command == "exit"):
            break;
        else:
            client.sendall(command + "\n")
    client.close()

if __name__ == "__main__":
    main()
        
