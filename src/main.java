/* Author: Liam English-Birge
 * Date: September 29th 2020
 * Database program to create and analyze user generated database files
 * 
 * Sources:
 * https://www.w3schools.com/java/java_files_create.asp
 * https://www.w3schools.com/java/java_files_read.asp
 * https://www.geeksforgeeks.org/java-program-to-get-a-character-from-a-string/
 * https://www.journaldev.com/807/java-string-substring
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class main {
	static String[][] database;
	static boolean running = true;
	static String fileName;
	static boolean imported = false;
	
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		while (running) {//keeps the program running while user is navigating menus
			if (imported) {//if user has just imported a database, this menu will be displayed
				System.out.println("What would you like to do with your database?");
				System.out.println("1: Edit database");
				System.out.println("2: Analyze database");
				System.out.println("3: Exit");
				System.out.print("What would you like to do?: ");
				int input1 = Integer.parseInt(scan.nextLine());
				if (input1 == 1) {
					//allow user to edit specific entries in their database as well as expand or shrink the database size
				} else if (input1 == 2) {
					//perform statistical analysis on the database such as averages, sums, standard deviation, etc.
				} else if (input1 == 3) {//exit program
					System.out.println("Shutting down...");
					break;
				} else {
					System.out.println("Please enter a valid number.");
				}
			} else {//if user has not imported a database, this menu will be displayed
				System.out.println("Welcome to the database");
				System.out.println("1: Create new database");
				System.out.println("2: Load database");
				System.out.println("3: Exit");
				System.out.print("What would you like to do?: ");
				int input1 = Integer.parseInt(scan.nextLine());
				if (input1 == 1) {//creating new database
					System.out.print("How many rows would you like? (start count at 0): ");
					int x = scan.nextInt();
					System.out.print("How many collumns would you like? (start count at 0): ");
					int y = scan.nextInt();
					
					makeDatabase(x, y);
					System.out.print("What would you like the database to be called?: ");
					fileName = scan.next() + ".txt";
					createFile();
					boolean workingOnDatabase = true;
					while (workingOnDatabase) {
						System.out.println("\n1: Add entries to database\n2: Export database");
						int input2 = scan.nextInt();
						
						if (input2 == 1) {//adding entries
							System.out.print("What row would you like to add to?: ");
							int row = scan.nextInt();
							int column = -1;//set to arbitrarily low number to trigger error if no empty columns available
							for (int i = 0; i < database[row].length; i++) {//searches for next available column slot in current row
								if (database[row][i] == null) {
									column = i;
									break;
								}
							}
							if (column == -1) {
								System.out.println("The row you have selected is full, please expand your database or choose a different row");
							} else {
								System.out.println("You are editing row: " + row + " column: " + column);
								System.out.print("\nEnter data (No spaces, press enter when complete): ");//spaces in the data make it freak out
								String dataInput = scan.next();
								database[row][column] = dataInput;
							}
						} else if (input2 == 2) {//export the database to the specified file name
							writeToFile(x, y, database);
						} else {
							System.out.println("Please enter a valid number.");
						}
					}
				} else if (input1 == 2) {
					boolean running = true;
					while (running) {
						System.out.println();
						System.out.println();
						System.out.println("What is the name of the file you wish to import?");
						System.out.println("(The file must be in the root directory of this program's folder, exclude file type)");
						System.out.print("File name: ");
						fileName = scan.nextLine();
						System.out.println();
						ReadFile(fileName);
						break;
					}
					
				} else if (input1 == 3) {//exit program
					System.out.println("Shutting down...");
					break;
				} else {
					System.out.println("Please enter a valid number.");
				}
			}
		}
	}
	
	public static void makeDatabase(int x, int y) {
		database = new String[x][y];
		System.out.println("Database of size " + x + "x" + y + " has been created");
	}
	
	public static void createFile() {
		try {
			File file = new File(fileName);
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists and will be overwritten.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public static void writeToFile(int databaseX, int databaseY, String[][] database) {
		//The whole idea here is to export the data in a form readable by both humans and the computer
		try {
			FileWriter myWriter = new FileWriter(fileName);
			myWriter.write(databaseX + ":" + databaseY + "\n");//tell the computer how large the array was
			for (int i = 0; i < database.length; i++) {//print the contents of the array with spaces between entries and line breaks between rows
				for (int j = 0; j < database[i].length; j++) {
					myWriter.write(database[i][j] + " ");
				}
				myWriter.write("\n");
			}
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	public static void ReadFile(String fileName) {
		//imports specified case sensitive file name and places it in to database 2D array
		try {
			File myObj = new File(fileName + ".txt");
			Scanner myReader = new Scanner(myObj);
			int line = 0;//keeps track of what row the file reader is on
			int arrayRow = 0, arrayCol = 0;//stores the array size
			int x = 0, y = 0;//stores the position in the array
			int lastNumI = 0;// holds character index where the last number ends
			System.out.println("Importing database...");
			while (myReader.hasNextLine()) {//loops until theres no more lines in the file
				String data = myReader.nextLine();
				if (line == 0) {//determines the size of the array based off format row:col
					for (int i = 0; i < data.length(); i++) {
						if (data.substring(i, i+1).equals(":")) {
							arrayRow = Integer.parseInt(data.substring(0, i));
							arrayCol = Integer.parseInt(data.substring(i+1, data.length()));
						}
					}
					System.out.println("Number of rows: " + arrayRow);
					System.out.println("Number of columns: " + arrayCol);
					database = new String[arrayRow][arrayCol];//creates respective database size
				} else {//if file reader is on any line besides the one specifying the array sizes
					for (int i = 0; i < data.length(); i++) {//identifies database entries by searching for spaces between the entries
						//System.out.print(data.charAt(i));
						if (data.substring(i, i+1).equals(" ")) {
							database[x][y] = data.substring(lastNumI, i);//uses character index of last known entry and the character index before the space to identify entry
							lastNumI = i+1;
							y++;//increase the column index once the entry has been added
						}
					}
					//reset all relative variables for analysis of next row
					lastNumI = 0;
					y = 0;
					
					x++;//increase row index
				}
				line++;//increase line count, unnecessary after it is greater than 0 but whatevs
			}
			
			//print array for user to observe data has been entered correctly
			for (int i = 0; i < arrayRow; i++) {
				for (int j = 0; j < arrayCol; j++) {
					System.out.print(database[i][j] + " ");
				}
				System.out.println();
			}
			System.out.println();
			imported = true;//when loops back to beginning, display the appropriate menu
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
		e.printStackTrace();
		}
	}
}
