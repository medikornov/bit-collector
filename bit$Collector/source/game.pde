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

void setup() {
   surface.setResizable(true);
   changeLevel();
   size(1200, 800);
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

void draw() {
   if (MENU && !EDITOR && !GAME) drawMenu();
   else if (!MENU && GAME && !EDITOR) drawGame();
   else if (!MENU && !GAME && EDITOR) drawEditor();
}

void drawMenu() {
   surface.setSize(1200, 800);
   if (gameEnd) mn.displayEndScreen(gameWon);
   else {
      level = 0;
      mn.display();
   }
}

void drawEditor() {
   buttonCheck();
   edit.display();
}

void drawGame() {
   buttonCheck();
   checkPlayersPosition();
   background(240, 255, 200);
   pushMatrix();
   translate(px, 0);
   tm.display();
   popMatrix();
   pl.display(px);
}

void checkPlayersPosition() {
   
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

void lostGame (){
   gameEnd = true;
   gameWon = false;
   MENU = true;
   EDITOR = false;
   GAME = false;
   level = 0;
}

void buttonCheck() {
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

void keyPressed() {
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

void keyReleased() {
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


void mousePressed() {
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

void changeLevel() {
    if (level == 0) current_file = "map1.csv";
    else if (level == 1) current_file = "map2.csv";
    else if (level == 2) current_file = "map3.csv";
    else if (level == 3) current_file = "map4.csv";
}
