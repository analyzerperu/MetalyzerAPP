class menu{
  float iconX[] = {w/50,0};
  float iconY = 19*h/20;
  color fondo;
  menu(color c,float s){
    fondo = c;
    update();
    icon();
  }
  
  void update(){
    fill(fondo);
    rect(0,9*h/10,w,h);
    icon();
  }
  void icon(){
    fill(white);
    line(iconX[0],iconY,iconX[0]+w/10,iconY);
    line(iconX[0],iconY,iconX[0]+w/10,iconY);
  }
}
