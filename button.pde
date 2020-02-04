class button {
  color cbutton;
  color cBorder;
  String name;
  float posx, posy;
  float size;
  float dx=w/5;
  float dy=h/15;
  button(float x, float y, float s, String n, color c) {
    cbutton = c;
    posx = x;
    posy = y;
    size = s;
    name = n;
    update();
  }

  void update() {
    fill(cbutton);
    stroke(cBorder);
    rect(posx, posy, size*dx/100, size*dy/100, 10);
    fill(black);
    stroke(black);
    textSize(12);
    textAlign(CENTER, CENTER);
    text(name, posx+size*dx/200, posy+size*dy/200);
  }

  float[] getParams() {
    float[] p = {posx, posy, dx, dy, size};
    return(p);
  }
}
