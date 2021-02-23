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
   
   void display() {
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
   
   void displayEndScreen(boolean gameWon){
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
