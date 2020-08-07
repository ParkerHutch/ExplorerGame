import javafx.animation.*;
import javafx.scene.canvas.*;
import javafx.scene.*;
import javafx.application.*;
import javafx.util.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import java.util.HashMap; // HashMap utility
import java.util.ArrayList;

import javax.imageio.*;
//import java.awt.Image;
import java.io.*;

public class Explorer2D extends Application
{
   String windowTitle = "Explorer2D";
   // the title that will appear at the top of the window
   
   int tileSize = 20;
   int totalTilesHorizontal = 55;
   int totalTilesVertical = 25;
   // useful game dimensions
   
   // constants for tile types
   int dirt = 0;
   int grass = 1;
   int water = 2;
   int coal = 3;
   
   // textures
   // loadImage method loads the filename given
   
   public static void main(String[] args)
   {
      launch(args);
   }
   public void start(Stage theStage)
   {
      
      theStage.setTitle(windowTitle);
      Group root = new Group();
      Scene theScene = new Scene( root );
      
      theStage.setScene( theScene );
      
      Canvas canvas = new Canvas( 
      totalTilesHorizontal * tileSize, totalTilesVertical * tileSize );
      // set the window size
      
      root.getChildren().add(canvas);
      
      GraphicsContext gc = canvas.getGraphicsContext2D();
      
      // textures: stores the textures, bound to an integer key(tile names are variables)
      HashMap<Integer, Image> textures = new HashMap<Integer, Image>();
      textures.put(dirt, loadImage("dirt.png")); // add the dirt img to dirt key(0)
      textures.put(grass, loadImage("grass.png"));
      textures.put(water, loadImage("water.png"));
      textures.put(coal, loadImage("coal.png"));
      
      // resources: list of all the resources
      ArrayList<Integer> resources = new ArrayList<Integer>();
      resources.add(dirt);
      
      
      int[][] tileMap = new int[totalTilesVertical][totalTilesHorizontal];
      // set the tiles
      for (int i = 0; i < totalTilesVertical; i++)
      {
         for (int j = 0; j < totalTilesHorizontal; j++)
         {
            tileMap[i][j] = dirt;
         }
      }
      
      
      int randomNum;
      int tile;
      for (int k = 0; k < totalTilesVertical; k++)
      {
         for (int m = 0; m < totalTilesHorizontal; m++)
         {
            randomNum = (int) (Math.random() * 100);
            if (randomNum <= 60)
            {
               tile = dirt;
            }
            else if ((randomNum >= 61) && (randomNum <= 70))
            {
               tile = grass;
            }
            else if ((randomNum >= 71) && (randomNum <= 90) )
            {
               tile = water;
            }
            else
            {
               tile = coal;
            }
            tileMap[k][m] = tile;
            gc.drawImage( textures.get(tileMap[k][m]), (m * tileSize), (k * tileSize) ); 
         }
      }
      // tilemap has been set and drawn
      
      int [] playerPos = new int [2];
      playerPos[0] = 0; // y value
      playerPos[1] = 0; // x value
      // make an array for player coordinates, set the x and y values to 0
      gc.drawImage( loadImage("player.png"), (playerPos[1] * tileSize), (playerPos[0] * tileSize));
      final long startNanoTime = System.nanoTime();
      // the AnimationTimer below allows us to make the display dynamic
      // (like the game loop from python)
      theScene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->{
               if ( (key.getCode() == KeyCode.W) && (playerPos[0] > 0) )
               {
                  playerPos[0] -= 1; // decreases distance from top of screen
               }
               else if ((key.getCode() == KeyCode.A) && (playerPos[1] > 0))
               {
                  playerPos[1] -= 1; // decreases distance from left side of screen
               }
               else if ( (key.getCode() == KeyCode.S) && (playerPos[0] < totalTilesVertical - 1) )
               {
                  playerPos[0] += 1; // increases distance from top of screen
               }
               else if ( (key.getCode() == KeyCode.D) && (playerPos[1] < totalTilesHorizontal - 1) )
               {
                  playerPos[1] += 1; // increases distance from left side
               }
            });
      
      new AnimationTimer()
      {
      
         public void handle(long currentNanoTime)
         {
            double t = (currentNanoTime - startNanoTime) / 1000000000.0;

            
            
            // draw the board
            for (int n = 0; n < totalTilesVertical; n++)
            {
               for (int p = 0; p < totalTilesHorizontal; p++)
               {
                  gc.drawImage(
                  textures.get(tileMap[n][p]), (p * tileSize), (n * tileSize)
                  );
               }
            }
            
            // draw the player
            gc.drawImage(
            loadImage("player.png"), (playerPos[1] * tileSize), (playerPos[0] * tileSize)
            );
         }
      }.start();
      theStage.show();
   } // end of start method
   public Image loadImage(String fileName)
   {
      Image result = null;
      try
      {
         //result = ImageIO.read(new File(fileName)); <- doesn't work
         result = SwingFXUtils.toFXImage(ImageIO.read(new File(fileName)), null);
         // maybe if the file can't be found, set it to a question mark image
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
      return result;
   }
}