class player {
   
   int posx;
   int posy;
   
   boolean pressedUp;
   float jumpSpeed;  
   
   boolean grounded;
   boolean reachedGround;
   
   boolean idle;
   boolean running;
   boolean facingLeft;
   boolean facingRight;
   
   PImage playerImg;
   
   int timer;
   int jumpTimer;
   
   int imagePosX;
   
   PImage newImage;
   
   PImage sprite1;
   PImage sprite2;
   PImage sprite3;
   PImage sprite4;
   PImage sprite5;
   PImage sprite6;
   PImage sprite7;
   PImage sprite8;
   
   PImage Leftsprite1;
   PImage Leftsprite2;
   PImage Leftsprite3;
   PImage Leftsprite4;
   PImage Leftsprite5;
   PImage Leftsprite6;
   PImage Leftsprite7;
   PImage Leftsprite8;
   
   PImage jumpSprite1;
   PImage jumpSprite2;
   PImage jumpSprite3;
   PImage jumpSprite4;
   PImage jumpSprite5;
   
   PImage LeftjumpSprite1;
   PImage LeftjumpSprite2;
   PImage LeftjumpSprite3;
   PImage LeftjumpSprite4;
   PImage LeftjumpSprite5;
   
   PImage spriteIdle;
   PImage LeftspriteIdle;
   //PImage;
   
   
   int mapx;
   
   int coins_left;
   
   
   player(){
      playerImg = new PImage();
      playerImg = loadImage("ball.png");
      posx = 100;
      posy = 0;
      grounded = false;
      jumpSpeed = 4;
      facingRight = true;
      
      idle = true;
      
      newImage = loadImage("player.png");
      sprite1 = newImage.get(17, 5, 30, 55);
      sprite2 = newImage.get(81, 5, 30, 55);
      sprite3 = newImage.get(145, 5, 30, 55);
      sprite4 = newImage.get(209, 5, 30, 55);
      sprite5 = newImage.get(273, 5, 30, 55);
      sprite6 = newImage.get(337, 5, 30, 55);
      sprite7 = newImage.get(401, 5, 30, 55);
      sprite8 = newImage.get(465, 5, 30, 55);
      spriteIdle = newImage.get(209, 71, 30, 55);
      
      Leftsprite1 = newImage.get(17, 134, 30, 55);
      Leftsprite2 = newImage.get(81, 134, 30, 55);
      Leftsprite3 = newImage.get(145, 134, 30, 55);
      Leftsprite4 = newImage.get(209, 134, 30, 55);
      Leftsprite5 = newImage.get(273, 134, 30, 55);
      Leftsprite6 = newImage.get(337, 134, 30, 55);
      Leftsprite7 = newImage.get(401, 134, 30, 55);
      Leftsprite8 = newImage.get(465, 134, 30, 55);
      LeftspriteIdle = newImage.get(209, 199, 30, 55);
      playerImg = spriteIdle;
      
      jumpSprite1 = newImage.get(17, 71, 30, 55);
      jumpSprite2 = newImage.get(81, 71, 30, 55);
      jumpSprite3 = newImage.get(145, 71, 30, 55);
      
      LeftjumpSprite1 = newImage.get(17, 199, 30, 55);
      LeftjumpSprite2 = newImage.get(81, 199, 30, 55);
      LeftjumpSprite3 = newImage.get(145, 199, 30, 55);
  
      timer = 0;
      jumpTimer = 0;
   }
   
   void display(int px) {
      
      textSize(25);
      fill(0, 0, 0);
      text("BITcoins left: " + coins_left, 10, 25);
      
      //posy = posy + 
      if (!grounded) {
         posy -= jumpSpeed;
         if (posy < 0) {
            jumpSpeed = 0;
         }
         jumpSpeed -= 0.2;
      }
      if (grounded) { 
         jumpSpeed = 0;
         jumpTimer = 0;
      }
      if (posx < 0) posx = 0;
      if (posy < 0) posy = 0;
      if (posx > mapx * 32 - 40) posx = mapx * 32 - 40;
      
      
      imagePosX = posx + px;
      //image(playerImg, posx, posy, 32, 64);
      if (facingRight) {
         if (idle && grounded) image(spriteIdle, imagePosX, posy, 32, 64);
         else if (!grounded && !reachedGround) jumpRight();
         else if (pressedUp) image(spriteIdle, imagePosX, posy, 32, 64);
         else runRight();
         
      } else if (facingLeft) {
         if (idle && grounded) image(LeftspriteIdle, imagePosX, posy, 32, 64);
         else if (!grounded && !reachedGround) jumpLeft();
         else if (pressedUp) image(LeftspriteIdle, imagePosX, posy, 32, 64);
         else runLeft();
      }
      timer++;
      
      if (timer == 79) timer = 0;
      
   }
   
   void runRight() {
      if (timer >= 0 && timer < 10) {
         image(sprite1, imagePosX, posy, 32, 64);
      } else if (timer >= 10 && timer < 20) {
         image(sprite2, imagePosX, posy, 32, 64);
      } else if (timer >= 20 && timer < 30) {
         image(sprite3, imagePosX, posy, 32, 64);
      } else if (timer >= 30 && timer < 40) {
         image(sprite4, imagePosX, posy, 32, 64);
      } else if (timer >= 40 && timer < 50) {
         image(sprite5, imagePosX, posy, 32, 64);
      } else if (timer >= 50 && timer < 60) {
         image(sprite6, imagePosX, posy, 32, 64);
      } else if (timer >= 60 && timer < 70) {
         image(sprite7, imagePosX, posy, 32, 64);
      } else if (timer >= 70 && timer < 80) {
         image(sprite8, imagePosX, posy, 32, 64);
         if (timer == 79) timer = 0;
      }
   }
   void runLeft() {
      if (timer >= 0 && timer < 10) {
         image(Leftsprite1, imagePosX, posy, 32, 64);
      } else if (timer >= 10 && timer < 20) {
         image(Leftsprite2, imagePosX, posy, 32, 64);
      } else if (timer >= 20 && timer < 30) {
         image(Leftsprite3, imagePosX, posy, 32, 64);
      } else if (timer >= 30 && timer < 40) {
         image(Leftsprite4, imagePosX, posy, 32, 64);
      } else if (timer >= 40 && timer < 50) {
         image(Leftsprite5, imagePosX, posy, 32, 64);
      } else if (timer >= 50 && timer < 60) {
         image(Leftsprite6, imagePosX, posy, 32, 64);
      } else if (timer >= 60 && timer < 70) {
         image(Leftsprite7, imagePosX, posy, 32, 64);
      } else if (timer >= 70 && timer < 80) {
         image(Leftsprite8, imagePosX, posy, 32, 64);
         if (timer == 79) timer = 0;
      }
   }
   
   void jumpRight() {
      if (jumpTimer >= 0 && jumpTimer < 10) {
         image(jumpSprite1, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else if (jumpTimer >= 10 && jumpTimer < 20) {
         image(jumpSprite2, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else image(jumpSprite3, imagePosX, posy, 32, 64);
   }
   void jumpLeft() {
      if (jumpTimer >= 0 && jumpTimer < 10) {
         image(LeftjumpSprite1, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else if (jumpTimer >= 10 && jumpTimer < 20) {
         image(LeftjumpSprite2, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else image(LeftjumpSprite3, imagePosX, posy, 32, 64);
   }
}
