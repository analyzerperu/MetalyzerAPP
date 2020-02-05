/*  UI-related functions */
int CONNECT_LIST = 0; 
int DISCONNECT_LIST = 1;
int listState = CONNECT_LIST;

void mousePressed()
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


void mouseReleased() {
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

void mouseDragged()
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
void drawUI()
{
    byte[] data = {48,49};
    bt.write("30:AE:A4:97:67:6A",data);
    instances[0].run();
    G1.update();
    T1.update();
    MENU.update();
    G1.setPoint(dato,dato,dy/2*sin(dato*PI/200));
    dato++;
    delay(100);
    T1.set(sliderValue);
    //T1.set(int(dato/3));
    if(dato == dx){
        dato=0;
    }
    //image(ble_icon, 0, 0);
    //textAlign(CENTER,CENTER);
    //text(str(w)+str(h),w/2,h/2);
}

void onKetaiListSelection(KetaiList klist)
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

boolean overbutton(float[] p) {
  int posx=0, posy=1, dx=2, dy=3, size=4;
  if (mouseX > p[posx] && mouseX < p[posx]+ p[dx]*p[size]/100 && mouseY > p[posy] && mouseY < p[posy]+ p[dy]*p[size]/100  ) {
    return true;
  } else {
    return false;
  }
}