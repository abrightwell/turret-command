'''
Created on Aug 28, 2011

@author: abrightwell
'''

import usb.core
import time

class MissileLauncher(object):
   
    DOWN = 0x01
    UP = 0x02
    LEFT = 0x04
    RIGHT = 0x08
    FIRE = 0x10
    STOP = 0x20
    
    def __init__(self, ):
        self.device = usb.core.find(idVendor=0x2123, idProduct=0x1010)
        
        # make sure the kernel didn't grab it.
        if (self.device.is_kernel_driver_active(0)):
            self.device.detach_kernel_driver(0)
        
        self.device.set_configuration()
        
    def fire(self, count=1):
        for i in range(count):
		self.send_command(self.FIRE)
    
    def move(self, direction, duration):
        self.send_command(direction)
        time.sleep(duration/1000.0)
        self.send_command(self.STOP)
    
    def stop(self):
        self.send_command(self.STOP)
    
    def send_command(self, command):
        self.device.ctrl_transfer(0x21, 0x09, 0, 0, [0x02, command, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00])
        

