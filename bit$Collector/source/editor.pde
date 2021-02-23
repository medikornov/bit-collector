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
   
   void display() {
      
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
   
   void show() {
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
   
   void checkCursor() {
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
   void rightClick() {
      if (mouseX >= 0 && mouseX <= 1100 && mouseY >= 0 && mouseY <= 736) {
          //image(currentPic, mouseX, mouseY, 32, 32);
          map[mouseY/32][(mouseX - px)/32] = -1;
       }
   }
   
   void saveMap() {
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
   void newMap() {
      if (showMap) {
         map = new int[23][1000];
         mapx = 23;
         mapy = 10;
         for (int i = 0; i < 23; i++)
            for (int j = 0; j < 10; j++)
               map[i][j] = -1;
      }
   }
   void enlarge() {
      if (showMap) {
         mapy++;
         for (int i = 0; i < 23; i++) 
            map[i][mapy - 1] = -1;
      }
   }
   void enlargeButton() {
      fill(255, 255, 255);
      rect(1120, buttonY, buttonSizeX + 10, buttonSizeY);
      fill(0, 0, 0);
      textSize(15);
      text("enlarge", 1128, height - 25);
   }
}
