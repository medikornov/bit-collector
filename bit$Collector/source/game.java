import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class game extends PApplet {

boolean MENU = true;
boolean GAME = false;
boolean EDITOR = false;

int level = 0;
int timer;

player pl;
tiledMap tm;
menu mn;
editor edit;
int speed = 3;
int sizeOfPl = 32;
int g = 1;
int jumpSpeed = 6;

int playerSizeX = 32;
int playerSizeY = 64;

PImage img;

boolean pressedUp = false;
boolean pressedDown = false;
boolean pressedRight = false;
boolean pressedLeft = false;
boolean pressedSpace = false;

boolean gameEnd = false;
boolean gameWon = false;


int mapSizeByPixelsX;
//int mapSizeByPixelsY;
String current_file;

int px;
int py;

public void setup() {
   surface.setResizable(true);
   changeLevel();
   
   background(240, 255, 200);
   tm = new tiledMap();
   tm.getMapFromFile(current_file);
   //tm.display();
   mapSizeByPixelsX = tm.x * 32;
   //mapSizeByPixelsY = tm.y * 32;
   pl = new player();
   pl.mapx = tm.x;
   frameRate(60);
   img = loadImage("player.png");
   px = 0;
   py = 0;
   pl.coins_left = tm.coins_quantity;
   mn = new menu(); 
   edit = new editor();
   timer = 0;
}

public void draw() {
   if (MENU && !EDITOR && !GAME) drawMenu();
   else if (!MENU && GAME && !EDITOR) drawGame();
   else if (!MENU && !GAME && EDITOR) drawEditor();
}

public void drawMenu() {
   surface.setSize(1200, 800);
   if (gameEnd) mn.displayEndScreen(gameWon);
   else {
      level = 0;
      mn.display();
   }
}

public void drawEditor() {
   buttonCheck();
   edit.display();
}

public void drawGame() {
   buttonCheck();
   checkPlayersPosition();
   background(240, 255, 200);
   pushMatrix();
   translate(px, 0);
   tm.display();
   popMatrix();
   pl.display(px);
}

public void checkPlayersPosition() {
   
   int camera_x = width/2 - pl.posx;
   if (camera_x < 0) px = camera_x;
   else px = 0;
   if (pl.posx + width/2 >= mapSizeByPixelsX) px = width - mapSizeByPixelsX;
   if (width >= tm.x * 32) px = 0;
   //   pl.posx += px;
   
   int posCheckRight = (pl.posx + playerSizeX) / 32;
   int posCheckLeft = (pl.posx) / 32;
   int posCheckYMiddle = (pl.posy + (playerSizeY/2)) / 32;
   int posCheckDown = (pl.posy + playerSizeY) / 32;
   
   int posCheckMiddle = (pl.posx + (playerSizeX/2)) / 32;
   int posCheckUp = (pl.posy) / 32;
   
   if (pl.coins_left == 0) {
      //gameWon = true;
      //gameEnd = true;
      //MENU = true;
      //GAME = false;
      //EDITOR = false;
      level++;
      if (level == 4) {
         gameWon = true;
         gameEnd = true;
         MENU = true;
         GAME = false;
         EDITOR = false;
         level = 0;
         return;
      }
      setup();
      return;
   }
   
   if (posCheckDown * 32 <= (tm.y - 1) * 32  &&  posCheckRight * 32 <= (tm.x - 1) * 32) {
      timer = 0;
      if (tm.map[posCheckDown][posCheckRight] == -2) {
         tm.map[posCheckDown][posCheckRight] = -1;
         pl.coins_left--;
      } else if (tm.map[posCheckDown][posCheckLeft] == -2) {
         tm.map[posCheckDown][posCheckLeft] = -1;
         pl.coins_left--;
      } else if (tm.map[posCheckUp][posCheckLeft] == -2) {
         tm.map[posCheckUp][posCheckLeft] = -1;
         pl.coins_left--;
      } else if (tm.map[posCheckUp][posCheckRight] == -2) {
         tm.map[posCheckUp][posCheckRight] = -1;
         pl.coins_left--;
      } else if (tm.map[posCheckYMiddle][posCheckMiddle] == -2) {
         tm.map[posCheckYMiddle][posCheckMiddle] = -1;
         pl.coins_left--;
      }
      
      if (tm.map[posCheckDown][posCheckRight] == -3) {
         lostGame();
      } else if (tm.map[posCheckDown][posCheckLeft] == -3) {
         lostGame();
      } else if (tm.map[posCheckUp][posCheckLeft] == -3) {
         lostGame();
      } else if (tm.map[posCheckUp][posCheckRight] == -3) {
         lostGame();
      } else if (tm.map[posCheckYMiddle][posCheckMiddle] == -3) {
         lostGame();
      }
      
      if ((tm.map[posCheckDown][posCheckMiddle] > -1 && tm.map[posCheckDown][posCheckRight] > -1) || (tm.map[posCheckDown][posCheckMiddle] > -1 && tm.map[posCheckDown][posCheckLeft] > -1)) {
         pl.posy = posCheckUp * 32;
         pl.reachedGround = true;
         //pl.grounded = true;
         if (!pressedSpace) pl.grounded = true;
         else {
            pl.grounded = false;
            pl.jumpSpeed = 0;
         }
      }
      else if ((tm.map[posCheckUp][posCheckMiddle] > -1 && tm.map[posCheckUp][posCheckRight] > -1) || (tm.map[posCheckUp][posCheckMiddle] > -1 && tm.map[posCheckUp][posCheckLeft] > -1)) {
         pl.posy = (posCheckUp + 1) * 32;
         pl.grounded = false;
         pl.jumpSpeed = 0;
         
      }
      else pl.grounded = false;
      posCheckUp = (pl.posy + 1) / 32;
      posCheckDown = (pl.posy + playerSizeY - 1) / 32;
      if (tm.map[posCheckDown][posCheckRight] > -1 || tm.map[posCheckUp][posCheckRight] > -1 || tm.map[posCheckYMiddle][posCheckRight] > -1) {
          
         pl.posx = posCheckLeft * 32;
      }
      else if (tm.map[posCheckDown][posCheckLeft] > -1 || tm.map[posCheckUp][posCheckLeft] > -1 || tm.map[posCheckYMiddle][posCheckLeft] > -1) {
         pl.posx = posCheckRight * 32;
      }
   } else {
      timer++;
      if (timer >= 20) {
         gameEnd = true;
         gameWon = false;
         MENU = true;
         EDITOR = false;
         GAME = false;
         level = 0;
      }
   }
}

public void lostGame (){
   gameEnd = true;
   gameWon = false;
   MENU = true;
   EDITOR = false;
   GAME = false;
   level = 0;
}

public void buttonCheck() {
   if (GAME) {
      if (pressedUp) {
         pl.idle = false;
         pl.posy -= speed;
         //pl.posy += speed;
      }
      if (pressedDown) {
         pl.posy += speed;
      }
      
      if (pressedRight) {
         pl.posx += speed;
         pl.facingRight = true;
         pl.facingLeft = false;
         pl.idle = false;
      }
      if (pressedLeft) {
         pl.posx -= speed;
         pl.facingRight = false;
         pl.facingLeft = true;
         pl.idle = false;
      }
      if (pressedSpace && pl.grounded) {
         pl.grounded = false;
         pl.jumpSpeed = jumpSpeed;
         pl.posy -= speed;
         pl.idle = false;
         pl.reachedGround = false;
         pl.pressedUp = true;
      }
      if (!pressedRight && !pressedSpace && !pressedLeft)
         pl.idle = true;
   }
   if (EDITOR) {
      if (pressedRight) {
         edit.px -= 16;  
         if (edit.mapy * 32 < 1100) edit.px = 0;
         else if (edit.px < 1100 - edit.mapy * 32) edit.px = 1100 - edit.mapy * 32;
         else if (edit.px > 0) edit.px = 0;
      }
      if (pressedLeft) {
         edit.px += 16;
         if (edit.mapy * 32 < 1100) edit.px = 0;
         else if (edit.px < 1100 - edit.mapy * 32) edit.px = 1100 - edit.mapy * 32;
         else if (edit.px > 0) edit.px = 0;
      }
   }
}

public void keyPressed() {
   if (key == CODED) {
      if (keyCode == UP) { 
         pressedSpace = true;
         //pressedUp = true;
      }
      if (keyCode == DOWN) 
         pressedDown = true;
      if (keyCode == RIGHT) 
         pressedRight = true;
      if (keyCode == LEFT) 
         pressedLeft = true;
   }
}

public void keyReleased() {
   if (key == CODED) {
      if (keyCode == UP && !pl.grounded) { 
         pressedSpace = false;
         //pressedUp = false; 
         pl.pressedUp = false;
      }
      if (keyCode == DOWN) 
         pressedDown = false;
      if (keyCode == RIGHT) 
         pressedRight = false;
      if (keyCode == LEFT) 
         pressedLeft = false;
   }
   //if (keyCode == ' ' && !pl.grounded) 
         //pressedSpace = false;
}


public void mousePressed() {
   if (mouseButton == LEFT) {
   if (MENU) {
      if (!gameEnd) {
         if (mouseX >= mn.buttonX && mouseX <= mn.buttonX + mn.buttonSizeX && mouseY >= mn.buttonY && mouseY <= mn.buttonY + mn.buttonSizeY) {
            EDITOR = true;
            MENU = false;
            GAME = false;
            //println("eidtorius");
            edit.waitingResponse = false;
         }
         else if (mouseX >= 0 && mouseX <= width && mouseY >= 0 && mouseY <= height) {
            EDITOR = false;
            MENU = false;
            GAME = true;           
         }
      } else {
         if (mouseX >= mn.buttonExitX && mouseX <= mn.buttonExitX + mn.buttonEndSizeX && mouseY >= mn.buttonExitY && mouseY <= mn.buttonExitY + mn.buttonEndSizeY) {
            exit();
         }
         else if (mouseX >= mn.buttonMainMenuX && mouseX <= mn.buttonMainMenuX + mn.buttonSizeX + 85 && mouseY >= mn.buttonMainMenuY && mouseY <= mn.buttonMainMenuY + mn.buttonEndSizeY) {
            setup();
            MENU = true;
            EDITOR = false;
            GAME = false;
            
            gameEnd = false;
            gameWon = false;
         }   
      }
   }
   else if (EDITOR) {
      if (mouseX >= edit.buttonX && mouseX <= edit.buttonX + edit.buttonSizeX && mouseY >= edit.buttonY && mouseY <= edit.buttonY + edit.buttonSizeY) {
         EDITOR = false;
         MENU = true;
         GAME = false;
         tm.getMapFromFile("map1.csv");
         pl.coins_left = tm.coins_quantity;
         //level = 0;
         //edit.waitingResponse = false;
         //println("eidtorius");
      }
      if (mouseX >= 160 && mouseX <= 200 && mouseY >= height - 50 && mouseY <= height - 10) {
          //println("1");
          tm.getMapFromFile("map1.csv");
          edit.showMap = true;
          edit.map = tm.map;
          edit.imgArr = tm.tilesArray;
          edit.mapx = tm.mapx;
          edit.mapy = tm.mapy;
          edit.waitingResponse = false;
          edit.currentMap = "map1.csv";
      }
      if (mouseX >= 310 && mouseX <= 350 && mouseY >= height - 50 && mouseY <= height - 10) {
          //println("1");
          tm.getMapFromFile("map2.csv");
          edit.showMap = true;
          edit.map = tm.map;
          edit.imgArr = tm.tilesArray;
          edit.mapx = tm.mapx;
          edit.mapy = tm.mapy;
          edit.waitingResponse = false;
          edit.currentMap = "map2.csv";
      }
      if (mouseX >= 460 && mouseX <= 500 && mouseY >= height - 50 && mouseY <= height - 10) {
          //println("1");
          tm.getMapFromFile("map3.csv");
          edit.showMap = true;
          edit.map = tm.map;
          edit.imgArr = tm.tilesArray;
          edit.mapx = tm.mapx;
          edit.mapy = tm.mapy;
          edit.waitingResponse = false;
          edit.currentMap = "map3.csv";
      }
      if (mouseX >= 610 && mouseX <= 650 && mouseY >= height - 50 && mouseY <= height - 10) {
          //println("1");
          tm.getMapFromFile("map4.csv");
          edit.showMap = true;
          edit.map = tm.map;
          edit.imgArr = tm.tilesArray;
          edit.mapx = tm.mapx;
          edit.mapy = tm.mapy;
          edit.waitingResponse = false;
          edit.currentMap = "map4.csv";
      }
      if (mouseX >= 760 && mouseX <= 845 && mouseY >= height - 50 && mouseY <= height - 10) {
          //println("1");
          //tm.getMapFromFile("map4.csv");
          //edit.showMap = true;
          //edit.map = tm.map;
          //edit.imgArr = tm.tilesArray;
          //edit.mapx = tm.mapx;
          //edit.mapy = tm.mapy;
          //edit.waitingResponse = false;
          edit.saveMap();
      }
      edit.checkCursor();
   } 
   } else if (mouseButton == RIGHT) {
      edit.rightClick();
   }
    
}

public void changeLevel() {
    if (level == 0) current_file = "map1.csv";
    else if (level == 1) current_file = "map2.csv";
    else if (level == 2) current_file = "map3.csv";
    else if (level == 3) current_file = "map4.csv";
}
class editor {
   int buttonX;
   int buttonY;
   
   int buttonSizeX;
   int buttonSizeY;
   
   int mapSizeX;
   int mapSizeY;
   
   boolean showMap;
   int level;
   
   int [][] map;
   String currentMap;
   
   PImage [] imgArr;
   
   int mapx;
   int mapy;
   
   PImage coin;
   PImage spike;
   
   boolean waitingResponse;
   
   int px;
   
   int cursorPosX;
   int cursorPosY;
   
   boolean picTaken;
   PImage currentPic;
   int picNumber;
   
   editor() {
      buttonX = 10;
      buttonY = height - 50;
      buttonSizeX = 60;
      buttonSizeY = 40;   
      showMap = false;
      level = 0;
      coin = loadImage("coin.png");
      spike = loadImage("spikeball.png");
      waitingResponse = false;
      px = 0;
      picTaken = false;
   }
   
   public void display() {
      
      //if (!waitingResponse) {
      //if ()
      background(220, 220, 220);
      fill(255, 255, 255);
      rect(buttonX, buttonY, buttonSizeX, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("back", 20, height - 25);
      
      fill(255, 255, 255);
      rect(buttonX + 150, buttonY, buttonSizeX - 20, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("1", 170, height - 25);
      
      fill(255, 255, 255);
      rect(buttonX + 300, buttonY, buttonSizeX - 20, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("2", 320, height - 25);
      
      fill(255, 255, 255);
      rect(buttonX + 450, buttonY, buttonSizeX - 20, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("3", 470, height - 25);
      
      fill(255, 255, 255);
      rect(buttonX + 600, buttonY, buttonSizeX - 20, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("4", 620, height - 25);
      
      fill(255, 255, 255);
      rect(buttonX + 750, buttonY, buttonSizeX + 25, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("save map", 770, height - 25);
      
      fill(255, 255, 255);
      rect(buttonX + 945, buttonY, buttonSizeX + 25, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("new map", 965, height - 25);
      
      if (showMap) {
         surface.setSize(1376, 800);
         pushMatrix();
         translate(px, 0);
         show();
         popMatrix();
         fill(220, 220, 220);
         rect(1100, 0, 276, 800);
         
         image(coin, 1216, 416, 32, 32);
         image(spike, 1248, 416, 32, 32);
         
         for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
               image(imgArr[i * 8 + j], 1120 + i * 32, 480 + j * 32, 32, 32);
            }
         }
         enlargeButton();
      }
      
      //noStroke();
      //stroke(1);
      
      textSize(25);
      fill(0, 0, 0);
      text("map editor", 10, 20);
      //waitingResponse = true;
     //println("vaziojam");
      //}
   }
   
   public void show() {
      //surface.setResizable(true);
      //size(1200, 1000);
      for (int i = 0; i < mapx; i++) {
         for (int j = 0; j < mapy; j++) {
            if (map[i][j] > -1) {
               //if (j * 32 <= 1000) 
               image(imgArr[map[i][j]], j * 32, i * 32, 32, 32);
               stroke(1);
               noFill();
               rect(j * 32, i * 32, 32, 32);
            } else if (map[i][j] == -2) {
               //if (j * 32 <= 1000) 
               image(coin, j * 32, i * 32, 32, 32);
               stroke(1);
               noFill();
               rect(j * 32, i * 32, 32, 32);
            } else if (map[i][j] == -3) {
               image(spike, j * 32, i * 32, 32, 32);
               stroke(1);
               noFill();
               rect(j * 32, i * 32, 32, 32);
            } else if (map[i][j] == -1) {
               stroke(1);
               fill(240, 255, 200);
               rect(j * 32, i * 32, 32, 32);
            }
            //print(map[i][j]);
         }
         //print("\n");
      } 
   }
   
   public void checkCursor() {
       if (mouseX >= 1120 && mouseX <= 1376 && mouseY >= 480 && mouseY <= 736) {
          cursorPosX = (mouseX - 1120) / 32;
          cursorPosY = (mouseY - 480) / 32;
          //println(cursorPosX, cursorPosY);
          picTaken = true;
          currentPic = imgArr[cursorPosX * 8 + cursorPosY];
          picNumber = cursorPosX * 8 + cursorPosY;
       }
       if (mouseX >= 0 && mouseX <= 1100 && mouseY >= 0 && mouseY <= 736 && picTaken) {
          //image(currentPic, mouseX, mouseY, 32, 32);
          map[mouseY/32][(mouseX - px)/32] = picNumber;
       }
       if (mouseX >= buttonX + 945 && mouseX <= buttonSizeX + 25 + buttonX + 945 && mouseY >= height - 50 && mouseY <= height - 10) {
          newMap();
       }
       if (mouseX >= 1120 && mouseX <= buttonSizeX + 10 + 1120 && mouseY >= height - 50 && mouseY <= height - 10) {
          enlarge();
       }
       if (showMap && mouseX >= 1216 && mouseX <= 1248 && mouseY >= 416 && mouseY <= 448) {
          picTaken = true;
          picNumber = -2;
       }
       if (showMap && mouseX >= 1248 && mouseX <= 1280 && mouseY >= 416 && mouseY <= 448) {
          picTaken = true;
          picNumber = -3;
       }
       //image(coin, 1216, 416, 32, 32);
       //  image(spike, 1248, 416, 32, 32);
   }
   public void rightClick() {
      if (mouseX >= 0 && mouseX <= 1100 && mouseY >= 0 && mouseY <= 736) {
          //image(currentPic, mouseX, mouseY, 32, 32);
          map[mouseY/32][(mouseX - px)/32] = -1;
       }
   }
   
   public void saveMap() {
      if (showMap) {
         Table table = new Table();
         for (int i = 0; i < mapy; i++) {
           table.addColumn(str(map[0][i]));
         }
         for (int i = 1; i < mapx; i++) {
            TableRow newRow = table.addRow();
            for (int j = 0; j < mapy; j++) {
               //file.print(map[i][j]);
               newRow.setInt(j, map[i][j]);
            }
            //print("\n");
         }
         saveTable(table, currentMap);
      }
   }
   public void newMap() {
      if (showMap) {
         map = new int[23][1000];
         mapx = 23;
         mapy = 10;
         for (int i = 0; i < 23; i++)
            for (int j = 0; j < 10; j++)
               map[i][j] = -1;
      }
   }
   public void enlarge() {
      if (showMap) {
         mapy++;
         for (int i = 0; i < 23; i++) 
            map[i][mapy - 1] = -1;
      }
   }
   public void enlargeButton() {
      fill(255, 255, 255);
      rect(1120, buttonY, buttonSizeX + 10, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("enlarge", 1128, height - 25);
   }
}
class menu {
   
   int buttonX;
   int buttonY;
   
   int buttonSizeX;
   int buttonSizeY;
   
   int buttonExitX;
   int buttonExitY;
   
   int buttonMainMenuX;
   int buttonMainMenuY;
   
   int buttonEndSizeX;
   int buttonEndSizeY;
   
   menu() {
      buttonX = width - 350;
      buttonY = height - 200;
      buttonSizeX = 200;
      buttonSizeY = 100;
      
      buttonExitX = 10;
      buttonExitY = height - 50;
      buttonMainMenuX = 75;
      buttonMainMenuY = height - 50;
      buttonEndSizeX = 60;
      buttonEndSizeY = 40;
   }
   
   public void display() {
      background(220, 220, 220);
      textSize(30);
      fill(0, 0, 0);
      text("COLLECT ALL bitCOINS TO WIN THE GAME!", 100, 200);
      textSize(25);
      fill(0, 0, 0);
      text("click on screen to start the game", 100, 400);
      fill(255, 255, 255);
      rect(buttonX, buttonY, buttonSizeX, buttonSizeY);
      fill(0, 0, 0);
      text("edit map", width - 300, height - 150);
   }
   
   public void displayEndScreen(boolean gameWon){
      background(220, 220, 220);
      textSize(30);
      fill(0, 0, 0);
      
      if (gameWon) text("YOU WON!!", 100, 200);
      else text("you lost :(", 100, 200);
      
      fill(255, 255, 255);
      rect(buttonExitX, buttonExitY, buttonEndSizeX, buttonEndSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("exit", 20, height - 25);
      
      fill(255, 255, 255);
      rect(buttonMainMenuX, buttonMainMenuY, buttonEndSizeX + 85, buttonEndSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("go to main menu", 85, height - 25);
   }
   
}
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
   
   public void display(int px) {
      
      textSize(25);
      fill(0, 0, 0);
      text("BITcoins left: " + coins_left, 10, 25);
      
      //posy = posy + 
      if (!grounded) {
         posy -= jumpSpeed;
         if (posy < 0) {
            jumpSpeed = 0;
         }
         jumpSpeed -= 0.2f;
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
   
   public void runRight() {
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
   public void runLeft() {
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
   
   public void jumpRight() {
      if (jumpTimer >= 0 && jumpTimer < 10) {
         image(jumpSprite1, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else if (jumpTimer >= 10 && jumpTimer < 20) {
         image(jumpSprite2, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else image(jumpSprite3, imagePosX, posy, 32, 64);
   }
   public void jumpLeft() {
      if (jumpTimer >= 0 && jumpTimer < 10) {
         image(LeftjumpSprite1, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else if (jumpTimer >= 10 && jumpTimer < 20) {
         image(LeftjumpSprite2, imagePosX, posy, 32, 64);
         jumpTimer++;
      } else image(LeftjumpSprite3, imagePosX, posy, 32, 64);
   }
}
class tiledMap {
   PImage img;
   PImage[] tilesArray;
   
   PImage coin;
   PImage spike;
   
   int [][]map;
   
   int [][]collisionsMap;
   
   int mapx;
   int mapy;
   
   int x;
   int y;
   
   int coins_quantity;
   
   tiledMap() {
      img = new PImage();
      img = loadImage("mapTiles.png");
      tilesArray = new PImage[65];
      int temp = 0;
      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            tilesArray[temp] = img.get(j * 32, i * 32, 32, 32);
            temp++;
         }
         //temp++;
      }
      coin = loadImage("coin.png");
      spike = loadImage("spikeball.png");
   }
   
   public void display() {
      //image(img, 100, 100, 256, 256);
      for (int i = 0; i < mapx; i++) {
         for (int j = 0; j < mapy; j++) {
            if (map[i][j] > -1) {
               image(tilesArray[map[i][j]], j * 32, i * 32, 32, 32);
            } else if (map[i][j] == -2) {
               image(coin, j * 32, i * 32, 32, 32);
            } else if (map[i][j] == -3) {
               image(spike, j * 32, i * 32, 32, 32);
            }
            //print(map[i][j]);
         }
         //print("\n");
      }
   }
   
   public void getMapFromFile(String file_name) {
      coins_quantity = 0;
      Table tb;
      tb = new Table();
      tb = loadTable(file_name);
      mapx = tb.getRowCount();
      mapy = tb.getColumnCount();
      x = mapy;
      y = mapx;
      
      println("map from file: " + file_name);
      
      map = new int[23][1000];
      for (int i = 0; i < mapx; i++) {
      TableRow row = tb.getRow(i);
      for (int j = 0; j < mapy; j++) {
            map[i][j] = row.getInt(j);        
            if (map[i][j] == -2) coins_quantity++;
         }
      }
      
      //collisionsMap = new int[mapx * 10][mapy * 10];
      //for (int i = 0; i < mapx; i++) {
      //   for (int j = 0; j < mapy; j++) {
      //      if (map[i][j] != -1) {
      //         collisionsMap[i][j] = 1;
      //      }
      //   }
      //}
      
      //println(map[0]);
      //println(map[5]);
   }
   
}
   public void settings() {  size(1200, 800); }
   static public void main(String[] passedArgs) {
      String[] appletArgs = new String[] { "game" };
      if (passedArgs != null) {
        PApplet.main(concat(appletArgs, passedArgs));
      } else {
        PApplet.main(appletArgs);
      }
   }
}
