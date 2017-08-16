/*----------------------------------------------------------------
 *  Author:        Rodrigo Guimaraes
 *  Written:       8/12/2017
 *  Last updated:  -
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     -
 *  
 *  % java Percolation
 *  
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private static int [][] percolationSiteIndex;
  private static boolean [][] percolationSiteStatus;
  private static WeightedQuickUnionUF wuf;
  private static int top = 0;
  private static int bottom;
  private static int numberOfOpenSites = 0;
  private static int gridSize;
  
  // create n-by-n grid, with all sites blocked
  public Percolation(int n) {
	  gridSize = n;
	  wuf = new WeightedQuickUnionUF(n*n+2); // add 2 because of virtual top and bottom nodes
	  percolationSiteIndex = new int[n+1][n+1]; // add 1 because we are considering i and j starting at 1
	  percolationSiteStatus = new boolean[n+1][n+1];
	  int initialValue = 1;
	  bottom = n*n+1;
	  for (int i = 1; i <= n; i++) {
		  for (int j = 1; j <= n; j++) {
			  percolationSiteIndex[i][j] = initialValue;
			  percolationSiteStatus[i][j] = false;
			  if (i == 1) {
				  wuf.union(top, percolationSiteIndex[i][j]);
			  }
			  if (i == n) {
				  wuf.union(bottom, percolationSiteIndex[i][j]);
			  }			
			  initialValue++;
		  }
	  }
  }
  
  //open site (row, col) if it is not open already
  public void open(int row, int col) {
    percolationSiteStatus[row][col] = true;
	int upperRow = row - 1;
	int downRow = row + 1;
	int rightCol = col + 1;
	int leftCol = col - 1;
    if(row > 1 && this.isOpen(upperRow, col)){
    	wuf.union(percolationSiteIndex[row][col], percolationSiteIndex[upperRow][col]);
    	numberOfOpenSites++;
    }
    if(row < gridSize && this.isOpen(downRow, col)){
    	wuf.union(percolationSiteIndex[row][col], percolationSiteIndex[downRow][col]);
    	numberOfOpenSites++;
    }
    if(col < gridSize && this.isOpen(row, rightCol)){
    	wuf.union(percolationSiteIndex[row][col], percolationSiteIndex[row][rightCol]);
    	numberOfOpenSites++;
    }
    if(col > 1 && this.isOpen(row, leftCol)){
    	wuf.union(percolationSiteIndex[row][col], percolationSiteIndex[row][leftCol]);
    	numberOfOpenSites++;
    }
  }
  
  //is site (row, col) open?
  public boolean isOpen(int row, int col) {
	  return percolationSiteStatus[row][col];
  }  
  
  //is site (row, col) full?
  public boolean isFull(int row, int col) {
	  return wuf.connected(percolationSiteIndex[row][col], top);
  }  
  
  //number of open sites
  public int numberOfOpenSites() {
    return numberOfOpenSites;  
  }       
  
  //does the system percolate?
  public boolean percolates() {
	  return wuf.connected(top,bottom);
  } 

  public static void main(String[] args) {
	int n = 3;
	int totalNumberOfSites = n*n;
    Percolation percolationObj = new Percolation(n);
    for (int i = 1; i <= n; i++) {
    	for (int j = 1; j <= n; j++) {
        	System.out.println("PercolationArray[" + i + "]["+ j +"]: " + percolationSiteIndex[i][j]);
        	System.out.println("Connected top: " + wuf.connected(top, percolationSiteIndex[i][j]));
        	System.out.println("Connected bottom: " + wuf.connected(bottom, percolationSiteIndex[i][j]));
        	System.out.println("Is full: " + percolationObj.isFull(i, j));
        	System.out.println("Percolation Status: " + percolationObj.isOpen(i,j));
    	}
    }
    System.out.println("Percolates: " + percolationObj.percolates());
    percolationObj.open(1,1);
    percolationObj.open(2,1);
    percolationObj.open(2,2);
    percolationObj.open(3,2);    
    System.out.println("Connected: " + wuf.connected(percolationSiteIndex[1][1], percolationSiteIndex[2][1]));
    for (int i = 1; i <= n; i++) {
    	for (int j = 1; j <= n; j++) {
        	System.out.println("PercolationArray[" + i + "]["+ j +"]: " + percolationSiteIndex[i][j]);
        	System.out.println("Percolation Status: " + percolationObj.isOpen(i,j));
        	System.out.println("Is full: " + percolationObj.isFull(i, j));
    	}
    }
    System.out.println("Percolates: " + percolationObj.percolates());
    System.out.println("Number of open sites: " + numberOfOpenSites);
    System.out.println("Number of total sites: " + totalNumberOfSites);
    int numberOfClosedSites = totalNumberOfSites - numberOfOpenSites;
    System.out.println("Number of closed sites: " + numberOfClosedSites);
    float p = (float) numberOfOpenSites / numberOfClosedSites;
    System.out.println("p: " + p);
  }

}