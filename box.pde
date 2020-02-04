class box{
    color cbox;
    float posx,posy;
    float size;
    
    box(float x, float y, float s,color c){
        cbox = c;
        posx = a1+x*delta_a;
        posy = b1+y*delta_b;
        size = s;
        update();
    }
    void update(){
        stroke(cbox);
        fill(cbox);
        rect(posx,posy,dx,dy);
    }
}
