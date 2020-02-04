//********************************************************************
// The following code is required to enable bluetooth at startup.
//********************************************************************
void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
  println("Creating KetaiBluetooth");
}

void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

//Call back method to manage data received
void onBluetoothDataEvent(String who, byte[] data)
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
void searchBle(){
  
  bt.start();
  textSize(24);
  dispositivos_bluetooth=bt.getPairedDeviceNames();
  text("\nDispositivos emparejados:",500,600);
  for(String dispositivo:dispositivos_bluetooth)
  {
    posText= posText + 100;
    text(dispositivo+", MAC: "+bt.lookupAddressByName(dispositivo),200,posText);
    if(dispositivo== "Metalyzer Black"){
        bt.connectDevice(bt.lookupAddressByName(dispositivo));
    }
}
  
  textSize(12);
}


String getBluetoothInformation()
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
