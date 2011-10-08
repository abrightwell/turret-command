'''
Created on Aug 28, 2011

@author: abrightwell
'''

import SocketServer
from MissileLauncher import MissileLauncher

class MissileHandler(SocketServer.BaseRequestHandler):
    
    def handle(self):
        launcher = self.server.getMissileLauncher()
        
        self.data = self.request.recv(1024).strip()
        print self.data
        
        while (len(self.data) != 0):
            if (self.data == "UP"):
                launcher.send_command(MissileLauncher.UP)
            elif (self.data == "DOWN"):
                launcher.send_command(MissileLauncher.DOWN)
            elif (self.data == "RIGHT"):
                launcher.send_command(MissileLauncher.RIGHT)
            elif (self.data == "LEFT"):
                launcher.send_command(MissileLauncher.LEFT)
            elif (self.data == "FIRE"):
                launcher.fire()
            elif (self.data == "STOP"):
                launcher.stop()
            self.data = self.request.recv(1024).strip()

class MissileServer(SocketServer.TCPServer):
    
    def __init__(self, server_address, RequestHandlerClass, launcher=None):
        SocketServer.TCPServer.__init__(self, server_address, RequestHandlerClass)
        self.launcher = launcher

    def getMissileLauncher(self):
        return self.launcher
    
if __name__ == "__main__":
    HOST, PORT = "192.168.1.108", 9999
    server = MissileServer((HOST, PORT), MissileHandler, MissileLauncher())
    server.serve_forever()
