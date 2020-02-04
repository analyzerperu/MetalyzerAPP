package processing.test.bluetoothcursors;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.content.Intent; 
import android.os.Bundle; 
import ketai.net.bluetooth.*; 
import ketai.ui.*; 
import ketai.net.*; 
import oscP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BluetoothCursors extends PApplet {

/**
 * <p>Ketai Sensor Library for Android: http://Ketai.org</p>
 *
 * <p>KetaiBluetooth wraps the Android Bluetooth RFCOMM Features:
 * <ul>
 * <li>Enables Bluetooth for sketch through android</li>
 * <li>Provides list of available Devices</li>
 * <li>Enables Discovery</li>
 * <li>Allows writing data to device</li>
 * </ul>
 * <p>Updated: 2017-08-29 Daniel Sauter/j.duran</p>
 */

//required for BT enabling on startup









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
public void setup()
{   
  
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
    bigLetters = PApplet.parseInt(h+w)/60;
    smallLetters = PApplet.parseInt(h+w)/100;
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

public void draw()
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

public void exit()
{
  bt.stop();
  super.exit();
}

/*  UI-related functions */
int CONNECT_LIST = 0; 
int DISCONNECT_LIST = 1;
int listState = CONNECT_LIST;

public void mousePressed()
{
  //keyboard button -- toggle virtual keyboard
   if (overbutton(ON.getParams())) {
    i = 0;
    ON.cBorder = black; 
    ON.cbutton = verde;
    ON.update();
  }
  if (overbutton(OFF.getParams())) {
    i = 0;
    OFF.cBorder = black; 
    OFF.cbutton = rosa;
    OFF.update();
  }
  
  if (mouseY <= 50*displayDensity && mouseX > 0 && mouseX < width/3)
    KetaiKeyboard.toggle(this);
  else if (mouseY <= 50*displayDensity && mouseX > width/3 && mouseX < 2*(width/3)) //config button
  {
    isConfiguring=true;
  } else if (mouseY <= 50*displayDensity && mouseX >  2*(width/3) && mouseX < width) // draw button
  {
    if (isConfiguring)
    {
      //if we're entering draw mode then clear canvas
      background(78, 93, 75);
      isConfiguring=false;
    }
  }
}


public void mouseReleased() {
  if (overbutton(ON.getParams())) {
    i = 0;
    T1.set(10);
    ON.cBorder = white; 
    ON.cbutton = verde;
    ON.update();
  }
  if (overbutton(OFF.getParams())) {
    i = 0;
    OFF.cBorder = white; 
    OFF.cbutton = red;
    OFF.update();
  }
}

public void mouseDragged()
{
  if (isConfiguring)
    return;

  //send data to everyone
  //  we could send to a specific device through
  //   the writeToDevice(String _devName, byte[] data)
  //  method.
  OscMessage m = new OscMessage("/remoteMouse/");
  m.add(mouseX);
  m.add(mouseY);

  bt.broadcast(m.getBytes());
  ellipse(mouseX, mouseY, 20, 20);
}

public void keyPressed() {
  if (key =='c')
  {
    listState = CONNECT_LIST;
    //If we have not discovered any devices, try prior paired devices
    if (bt.getDiscoveredDeviceNames().size() > 0) {
      ArrayList<String> list = bt.getDiscoveredDeviceNames();
      list.add("CANCEL");
      klist = new KetaiList(this, list);
    } else if (bt.getPairedDeviceNames().size() > 0) {
      ArrayList<String> list = bt.getPairedDeviceNames();
      list.add("CANCEL");
      klist = new KetaiList(this, list);
    }
  } else   if (key =='x')
  {
    listState = DISCONNECT_LIST;
    //If we have not discovered any devices, try prior paired devices
    if (bt.getConnectedDeviceNames().size() > 0) {
      ArrayList<String> list = bt.getConnectedDeviceNames();
      list.add("CANCEL");
      klist = new KetaiList(this, list);
    } else {
      println("No devices to disconnect.");
    }
  } else if (key == 'd')
  {
    bt.discoverDevices();
  } 
  else if (key == 'b')
  {
    bt.makeDiscoverable();
  } else if (key == 's')
  {
    bt.start();
  }
}

int dato=0;
public void drawUI()
{
    instances[0].run();
    G1.update();
    T1.update();
    MENU.update();
    G1.setPoint(dato,dato,dy/2*sin(dato*PI/200));
    dato++;
    delay(20);
    T1.set(sliderValue);
    //T1.set(int(dato/3));
    if(dato == dx){
        dato=0;
    }
    //image(ble_icon, 0, 0);
    //textAlign(CENTER,CENTER);
    //text(str(w)+str(h),w/2,h/2);
}

public void onKetaiListSelection(KetaiList klist)
{
  String selection = klist.getSelection();

  if (listState == CONNECT_LIST)
  {
    if (!selection.equals("CANCEL"))
      bt.connectToDeviceByName(selection);
  }else if (listState == DISCONNECT_LIST)
  {
     bt.disconnectDevice(selection); 
  }
  //dispose of list for now
  klist = null;
}

public boolean overbutton(float[] p) {
  int posx=0, posy=1, dx=2, dy=3, size=4;
  if (mouseX > p[posx] && mouseX < p[posx]+ p[dx]*p[size]/100 && mouseY > p[posy] && mouseY < p[posy]+ p[dy]*p[size]/100  ) {
    return true;
  } else {
    return false;
  }
}
//********************************************************************
// The following code is required to enable bluetooth at startup.
//********************************************************************
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
  println("Creating KetaiBluetooth");
}

public void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

//Call back method to manage data received
public void onBluetoothDataEvent(String who, byte[] data)
{
  if (isConfiguring)
    return;

  //KetaiOSCMessage is the same as OscMessage
  //   but allows construction by byte array
  KetaiOSCMessage m = new KetaiOSCMessage(data);
  if (m.isValid())
  {
    if (m.checkAddrPattern("/remoteMouse/"))
    {
      if (m.checkTypetag("ii"))
      {
        remoteMouse.x = m.get(0).intValue();
        remoteMouse.y = m.get(1).intValue();
      }
    }
  }
}
int posText=700;
public void searchBle(){
  noLoop();
  bt.start();
  textSize(24);
  dispositivos_bluetooth=bt.getPairedDeviceNames();
  text("\nDispositivos emparejados:",500,600);
  for(String dispositivo:dispositivos_bluetooth)
  {
    posText= posText + 100;
    text(dispositivo+", MAC: "+bt.lookupAddressByName(dispositivo),500,posText);
  }
  bt.stop();
  textSize(12);
}


public String getBluetoothInformation()
{
  String btInfo = "Server Running: ";
  btInfo += bt.isStarted() + "\n";
  btInfo += "Discovering: " + bt.isDiscovering() + "\n";
  btInfo += "Device Discoverable: "+bt.isDiscoverable() + "\n";
  btInfo += "\nConnected Devices: \n";

  ArrayList<String> devices = bt.getConnectedDeviceNames();
  for (String device : devices)
  {
    btInfo+= device+"\n";
  }

  return btInfo;
}
class box{
    int cbox;
    float posx,posy;
    float size;
    
    box(float x, float y, float s,int c){
        cbox = c;
        posx = a1+x*delta_a;
        posy = b1+y*delta_b;
        size = s;
        update();
    }
    public void update(){
        stroke(cbox);
        fill(cbox);
        rect(posx,posy,dx,dy);
    }
}
class button {
  int cbutton;
  int cBorder;
  String name;
  float posx, posy;
  float size;
  float dx=w/5;
  float dy=h/15;
  button(float x, float y, float s, String n, int c) {
    cbutton = c;
    posx = x;
    posy = y;
    size = s;
    name = n;
    update();
  }

  public void update() {
    fill(cbutton);
    stroke(cBorder);
    rect(posx, posy, size*dx/100, size*dy/100, 10);
    fill(black);
    stroke(black);
    textSize(12);
    textAlign(CENTER, CENTER);
    text(name, posx+size*dx/200, posy+size*dy/200);
  }

  public float[] getParams() {
    float[] p = {posx, posy, dx, dy, size};
    return(p);
  }
}
int red = color(255,0,0);
int white = color(255,255,255);
int black = color(0,0,0);
int fondoPrincipal = color(36,49,58);
int menuPrincipal = color(30,39,48);
int morado = color(151,74,238);
int turquesa = color(17,225,193);
int Cbutton = color(151,150,238);
int verde = color(67,193,83);
int rosa = color(243,102,118);
int lineButton = color(46, 50, 75);
int letraSecundaria = color(51, 57, 71);
float a1;
float b1;
float delta_a;
float delta_b;
float dx;
float dy;
float bigLetters,smallLetters;
class gauge{
  box B;
  float A[] = {0,0};
  float R  ;
  float r,S;
  float x_g,y_g;
  int C = color(0,0,0);
  String unidades;
  float value=0;
  gauge(int posx,int posy,float size,int col,String unit){
      C = col;
      S = size;
      R = 1.0f*S/100*(w+h)/20;
      r = 1.0f*S/100*(w+h)/100;
      unidades = unit;
      B = new box(posx,posy,size,menuPrincipal);
      A[0]= a1+posx*delta_a;
      A[1]= b1+posy*delta_b;
      x_g = A[0] + dx/2;
      y_g = A[1] + dy/2;
      update();
      
}
  
  public void update(){
        stroke(lineButton);
        fill(lineButton);
        arc(x_g, y_g, 2*R+5, 2*R+5, 0, 2*PI, PIE);
        fill(fondoPrincipal);
        stroke(fondoPrincipal);
        arc(x_g, y_g, 2*R-5, 2*R-5, 0, 2*PI, PIE); 
        stroke(letraSecundaria);
        fill(letraSecundaria);
        textSize(smallLetters);
        textAlign(CENTER, CENTER);
        text("REMAINING", x_g, y_g+2*smallLetters);      
        move(value);
  }
  
  public void move(float value){
        float aux=93; 
        for (float i=0; i<value*2*PI/100; i=i+(PI/360)) {
          ellipse(x_g-R*cos(-(i+PI/2)), y_g+R*sin(-(i+PI/2)), r, r);
          aux=aux+0.4f;
          int C2 = C + color(-PApplet.parseInt(aux/20),PApplet.parseInt(aux/3),-PApplet.parseInt(aux/20)); 
          fill(C2);
          stroke(C2);
        }
        stroke(white);
        fill(white);
        textAlign(CENTER, CENTER);
        textSize(bigLetters);
        text(str(value)+ " "+ unidades, x_g, y_g-smallLetters);
    }

  public void set(int v) {
          value = v;
    }
}
class graph{
  box B;
  float pointsX[] = new float[5000];
  float pointsY[] = new float[5000];
  float A[] = {0,0};

  graph(int posx,int posy,float size){
    B = new box(posx,posy,size,menuPrincipal); 
    A[0]= a1+posx*delta_a;
    A[1]= b1+posy*delta_b;
    B.update();
    
}
  
  public void update(){
    B.update();
    stroke(white);
    strokeWeight(1);
    for(int i = 0 ; i<= 5 ;i=i+1){
      line(A[0],A[1]+i*dy/5,w-A[0],A[1]+i*dy/5);
    }
    strokeWeight(2);
    for(int i = 0 ; i<=5 ;i=i+1){
      line(A[0]+i*dx/5,A[1],A[0]+i*dx/5,A[1]+dy);
    }
    stroke(255,0,0);
    strokeWeight(3);
    for(int i=0;i< dx;i++){
          point(A[0]+pointsX[i],A[1]+dy/2-pointsY[i]);
      //  line(A[0]+pointsX[i],A[1]+dy/2-pointsY[i],A[0]+pointsX[i+1],A[1]+dy/2-pointsY[i+1]);  
    }
    stroke(white);
  }
  
  public void setPoint(int n,float valueX, float valueY){
      pointsX[n] = valueX;
      pointsY[n] = valueY;
      pointsX[n+1] = valueX;
      pointsY[n+1] = valueY;
  }
}
class menu{
  float iconX[] = {w/50,0};
  float iconY = 19*h/20;
  int fondo;
  menu(int c,float s){
    fondo = c;
    update();
    icon();
  }
  
  public void update(){
    fill(fondo);
    rect(0,9*h/10,w,h);
    icon();
  }
  public void icon(){
    fill(white);
    line(iconX[0],iconY,iconX[0]+w/10,iconY);
    line(iconX[0],iconY,iconX[0]+w/10,iconY);
  }
}
class Slider {
  //class vars
  float x;
  float y;
  float w, h;
  float initialY;
  boolean lock = false;
 
  //constructors
 
  //default
  Slider () {
  }
 
  Slider (float _x, float _y, float _w, float _h) {
 
    x=_x;
    y=_y;
    initialY = y;
    w=_w;
    h=_h;
  }
 
 
  public void run() {
 
    // bad practice have all stuff done in one method...
    float lowerY = height - h - initialY;
 
    // map value to change color..
    float value = map(y, initialY, lowerY, 120, 255);
 
    // map value to display
    float value2 = map(value, 120, 255, 100, 0);
 
    //set color as it changes
    int c = color(value);
    fill(c);
 
    // draw base line
    rect(x, initialY, 4, lowerY);
 
    // draw knob
    fill(200);
    rect(x, y, w, h);
 
    // display text
    fill(0);
    text(PApplet.parseInt(value2) +"%", x+5, y+15);
 
    //get mouseInput and map it
    float my = constrain(mouseY, initialY, height - h - initialY );
    if (lock) y = my;
  }
 
  // is mouse ove knob?
  public boolean isOver()
  {
    return (x+w >= mouseX) && (mouseX >= x) && (y+h >= mouseY) && (mouseY >= y);
  }
}
  public void settings() {  fullScreen(); }
}
