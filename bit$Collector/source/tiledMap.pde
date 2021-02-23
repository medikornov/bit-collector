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
   
   void display() {
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
   
   void getMapFromFile(String file_name) {
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
