import java.util.*;
import java.io.*;
import java.awt.*;

public class MapDataDrawer
{

  private int[][] grid;

  public MapDataDrawer(String filename, int rows, int cols){
      // initialize grid
      grid = new int[rows][cols];

      //read the data from the file into the grid
      Scanner scanner = null;
      try {
        scanner = new Scanner(new File(filename));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          try {
            grid[i][j] = scanner.nextInt();
          } catch (NoSuchElementException e) {

            System.out.printf("%d\t %d\n", i, j);
            break;
          }
        }
      }

    }
  
  /**
   * @return the min value in the entire grid
   */
  public int findMinValue() {
      int min = grid[0][0];
      for (int row = 0; row < grid.length; row++) {
          for (int col = 0; col < grid[0].length; col++) {
              if (grid[row][col] < min) {
                  min = grid[row][col];
              }

          }
      }
      return min;
  }


  /**
   * @return the max value in the entire grid
   */

  public int findMaxValue(){
      int max = grid[0][0];
      for (int row = 0; row < grid.length; row++) {
          for (int col = 0; col < grid[0].length; col++) {
              if (grid[row][col] > max) {
                  max = grid[row][col];
              }

          }
      }
      return max;

  }
  
  /**
   * @param col the column of the grid to check
   * @return the index of the row with the lowest value in the given col for the grid
   */
  public  int indexOfMinInCol(int col){
      int min = grid[0][col];
      int rowval = -1;
      for (int row = 0; row < grid.length; row++) {
          if (grid[row][col] < min)
          {
              min = grid[row][col];
              rowval = row;
          }
      }

      return rowval;
  }
  
  /**
   * Draws the grid using the given Graphics object.
   * Colors should be grayscale values 0-255, scaled based on min/max values in grid
   */
  public void drawMap(Graphics g){
      int minval = findMinValue();
      int maxval = findMaxValue();
      double factor = (255.0 / (maxval - minval));

      for (int row = 0; row < grid.length; row++) {
          for (int col = 0; col < grid[0].length; col++) {
              int gray = (int)((grid[row][col] - minval) * factor);
              Color color = new Color(gray, gray, gray);
              g.setColor(color);
              g.drawLine(col, row, col, row);
          }
      }



  }

   /**
   * Find a path from West-to-East starting at given row.
   * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
   * @return the total change in elevation traveled from West-to-East
   */
  public int drawLowestElevPath(Graphics g, int row) {

      int total = 0;
      int val;
      int next1, next2, next3;
      int[] path = new int[grid[row].length];
      path[0] = row;
      int previous;
      for (int col = 1; col < grid[row].length; col++) {
          previous = path[col - 1];
          val = grid[previous][col - 1];
          if (previous == 0) {
              next1 = 0;
          } else {
              next1 = grid[previous - 1][col];
          }
          next2 = grid[previous][col];
          if (previous == grid.length - 1) {
              next3 = 0;
          } else {
              next3 = grid[previous + 1][col];
          }

          int change1, change2, change3;
          int min;
          change1 = Math.abs(next1 - val);
          change2 = Math.abs(next2 - val);
          change3 = Math.abs(next3 - val);
          min = Math.min(Math.min(change1, change2), change3);
          total += min;
          if (change2 == min) {
              path[col] = previous;
          } else if (change1 == change3) {
              if (Math.random() < 0.5) {
                  path[col] = previous - 1;
              } else {
                  path[col] = previous + 1;
              }
          } else {
              if (change1 == min) {
                  path[col] = previous - 1;
              } else {
                  path[col] = previous + 1;
              }
          }

      }

      for (int col = 0; col < path.length; col++) {
          g.drawLine(col, path[col], col, path[col]);
      }

      return total;
  }
  
  /**
   * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
   */
  public int indexOfLowestElevPath(Graphics g){
     int[]totals = new int[grid.length];
     for (int row = 0; row < grid.length; row++)
     {
         totals[row] = drawLowestElevPath(g, row);
     }
     int min = totals[0];
     int index = 0;
     for (int row = 1; row < grid.length; row++)
     {
         if (totals[row]< min)
         {
             index = row;
             min = totals[row];
         }
     }
     return index;
  
  }
  
  
}