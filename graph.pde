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
  
  void update(){
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
  
  void setPoint(int n,float valueX, float valueY){
      pointsX[n] = valueX;
      pointsY[n] = valueY;
      pointsX[n+1] = valueX;
      pointsY[n+1] = valueY;
  }
}
