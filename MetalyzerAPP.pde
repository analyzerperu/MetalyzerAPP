//required for BT enabling on startup
import android.content.Intent;
import android.os.Bundle;

import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;

import oscP5.*;

KetaiBluetooth bt;
String info = "";
KetaiList klist;
PVector remoteMouse = new PVector();

ArrayList<String> dispositivos_bluetooth;
ArrayList<String> devicesDiscovered = new ArrayList();
boolean isConfiguring = true;

int x = 0,i;
float w,h;
button ON,OFF;
graph G1;
gauge T1;
menu MENU;
Slider[] instances =  new Slider[3];

int sliderValue = 100;
int cronometro;
//********************************************************************
void setup()
{   
  fullScreen();
  orientation(PORTRAIT);
  background(fondoPrincipal);
  strokeWeight(2);
    w = displayWidth;
    h = displayHeight;
    a1 = w/20;
    b1 = h/20;
    delta_a = w/20;
    delta_b = h/4;
    dx = 9*w/10;
    dy = h/5;
    bigLetters = int(h+w)/60;
    smallLetters = int(h+w)/100;
    float a4 = w/8;
    float b4 = 16*h/20;
    float a5=  w/8+w/2;
    float b5 = 16*h/20;
    MENU = new menu(menuPrincipal,1);
    G1 = new graph(0,0,1);
    T1 = new gauge(0,1,100,morado,"uG");
    ON = new button(a4,b4,100,"INICIO",Cbutton);
    OFF = new button(a5,b5,100,"APAGAR",rosa);
    instances[0] = new Slider(6*w/8, 6*h/8, 40, 20);
    //ble_icon = loadImage("laDefense.jfif");
  
  stroke(255);
  if(!bt.isStarted())
  {
    bt.start();
  }
  cronometro=millis();
  bt.discoverDevices();
  searchBle();
}

void draw()
{
  /*if (isConfiguring)
  {
    ArrayList<String> names;
    background(78, 93, 75);

    //based on last key pressed lets display
    //  appropriately
    if (key == 'i')
      info = getBluetoothInformation();
    else
    {
      if (key == 'p')
      {
        info = "Paired Devices:\n";
        names = bt.getPairedDeviceNames();
      } else
      {
        info = "Discovered Devices:\n";
        names = bt.getDiscoveredDeviceNames();
      }

      for (int i=0; i < names.size(); i++)
      {
        info += "["+i+"] "+names.get(i).toString() + "\n";
      }
    }
    text(UIText + "\n\n" + info, 5, 90*displayDensity);
  } else
  {
    background(78, 93, 75);
    pushStyle();
    fill(255);
    ellipse(mouseX, mouseY, 20, 20);
    fill(0, 255, 0);
    stroke(0, 255, 0);
    ellipse(remoteMouse.x, remoteMouse.y, 20, 20);    
    popStyle();
  }*/
  
  drawUI();
}

void exit()
{
  bt.stop();
  super.exit();
}
