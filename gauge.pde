class gauge{
  box B;
  float A[] = {0,0};
  float R  ;
  float r,S;
  float x_g,y_g;
  color C = color(0,0,0);
  String unidades;
  float value=0;
  gauge(int posx,int posy,float size,color col,String unit){
      C = col;
      S = size;
      R = 1.0*S/100*(w+h)/20;
      r = 1.0*S/100*(w+h)/100;
      unidades = unit;
      B = new box(posx,posy,size,menuPrincipal);
      A[0]= a1+posx*delta_a;
      A[1]= b1+posy*delta_b;
      x_g = A[0] + dx/2;
      y_g = A[1] + dy/2;
      update();
      
}
  
  void update(){
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
  
  void move(float value){
        float aux=93; 
        for (float i=0; i<value*2*PI/100; i=i+(PI/360)) {
          ellipse(x_g-R*cos(-(i+PI/2)), y_g+R*sin(-(i+PI/2)), r, r);
          aux=aux+0.4;
          color C2 = C + color(-int(aux/20),int(aux/3),-int(aux/20)); 
          fill(C2);
          stroke(C2);
        }
        stroke(white);
        fill(white);
        textAlign(CENTER, CENTER);
        textSize(bigLetters);
        text(str(value)+ " "+ unidades, x_g, y_g-smallLetters);
    }

  void set(int v) {
          value = v;
    }
}
