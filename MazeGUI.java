/* Program name: MazeGUI
 * Programmer name: Tim Chen
 * Date: 2023/4/3
 * Programming language: java
 * Program definition:
 *       This program helps users to find the shortest path of a maze.  The users have the options to import their
 *       own maze from a file to the program or let the program generates a maze with or without a valid path.  The
 *       program uses graphical user interface to interact with the users.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class MazeGUI extends Frame implements ActionListener{

    JTextField fileNameText = new JTextField("   Please enter your file name here");
    JTextField rowText = new JTextField("   Please enter the row number here");
    JTextField colText = new JTextField("   Please enter the column number here");

    Panel originalMazePan = new Panel();
    Panel pathMazePan = new Panel();

    /**
     * Constructor of class MazeGUI
     */
    public MazeGUI(){
        //frame settings
        setTitle("Maze");
        setSize(1500, 800);
        setLayout(new GridLayout(2,2));

        //welcome panel settings
        Panel welcomePan = new Panel(new GridLayout(6,1));
        JLabel welcomeMessage = new JLabel("   Welcome to this program. This is program can help you find the shortest path for a maze.");
        JLabel instructionLabel = new JLabel("   You can either choose a maze file or let this program to auto-generate a maze to solve.");
        JLabel instructionLabel2 = new JLabel("   The original maze will be at the bottom left of the window. The solved maze will be at the bottom right of the window.");
        JLabel instructionLabel3 = new JLabel("   Note: gray - starting point, green - exit, brown - wall, yellow - path, blue - path.");
        JLabel chooseLabel = new JLabel("   Step 1: Please choose below:");

        //setting of panel for choosing read file or generate maze
        Panel fileOrGenPan = new Panel(new GridLayout(1,2));
        JButton fileBtn = new JButton("Read from file");
        JButton genBtn = new JButton("Generate a maze");

        //fileOrGenPan settings
        fileOrGenPan.add(fileBtn);
        fileOrGenPan.add(genBtn);

        //weilcomePan settings
        welcomePan.add(welcomeMessage);
        welcomePan.add(instructionLabel2);
        welcomePan.add(instructionLabel3);
        welcomePan.add(instructionLabel);
        welcomePan.add(chooseLabel);
        welcomePan.add(fileOrGenPan);


        //input panel settings
        final CardLayout cardLayout = new CardLayout();
        final JPanel inputPan = new JPanel(cardLayout);

        //file input panel
        JPanel fileInputPanel = new JPanel(new GridLayout(6, 1));
        fileInputPanel.add(new JLabel("   Step 2: Please enter the file name and click the button to read the maze in this file:"));
        fileInputPanel.add(fileNameText);
        JLabel instructionLabel4 = new JLabel("   Step 3: After correctly entering the file name, please press the \"Import Maze\" button.");
        fileInputPanel.add(instructionLabel4);
        JButton importMazeBtn = new JButton("Import Maze");
        fileInputPanel.add(importMazeBtn);
        fileInputPanel.add(new JLabel("    Step 4: After the maze read from file is shown at the bottom left of the screen, please click \"Find Path\" button to show the path."));
        JButton findPathBtn1 = new JButton("Find Path");
        fileInputPanel.add(findPathBtn1);
        inputPan.add(fileInputPanel,"Read File");

        //generate maze panel
        JPanel genMazePanel = new JPanel(new GridLayout(7, 1));
        genMazePanel.add(new JLabel("    Step 2: Please enter the number of rows and columns and select if you want to generate a random maze or a maze with path."));
        genMazePanel.add(rowText);
        genMazePanel.add(colText);

        //maze with path or random maze panel
        JLabel instructionLabel5 = new JLabel("   Step 3: After correctly enter the row and column number, please choose from the two options and press the button.");
        genMazePanel.add(instructionLabel5);
        JPanel pathOrRanPanel = new JPanel(new GridLayout(1, 2));
        JButton ranBtn = new JButton("Generate random maze");
        pathOrRanPanel.add(ranBtn);
        JButton pathBtn = new JButton("Generate maze with path");
        pathOrRanPanel.add(pathBtn);
        genMazePanel.add(pathOrRanPanel);
        genMazePanel.add(new JLabel("    Step 4: After the maze is generated at the bottom left of the screen, please click \"Find Path\" button to show the path."));
        JButton findPathBtn2 = new JButton("Find Path");
        genMazePanel.add(findPathBtn2);
        inputPan.add(genMazePanel,"Generate Maze");

        //add function to the Generate maze button that displays genMazePanel if genBtn is pressed
        genBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(inputPan, "Generate Maze");
            }
        });
        //add action listener to read from file button that displays genMazePanel if fileBtn is pressed
        fileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(inputPan, "Read File");
            }
        });

        //add action listeners to other buttons
        importMazeBtn.addActionListener(this);
        ranBtn.addActionListener(this);
        pathBtn.addActionListener(this);

        //frame settings
        add(welcomePan);
        add(inputPan);
        add(originalMazePan);
        add(pathMazePan);

        setVisible(true);

        //close frame when click "x"
        addWindowListener(new WindowAdapter() {
            //when click "x"
            public void windowClosing(WindowEvent e){
                //close frame
                System.exit(0);
            }
        });
    }

    /**
     * Reads different text fields and calls different methods depending on the button the user clicks.
     *
     * @param	event	ActionEvent of the event that occurs
     * @return			non-return type
     */
    public void actionPerformed(ActionEvent event)//ACTION LISTENER: this method runs when an event occurs
    {
        String command = event.getActionCommand(); //the label of the button that was clicked when the event occurred
        String fileName; //the file name
        int row,col; //the number of rows and columns of the maze

        //clear previous maze or elements on originalMazePan and pathMazePan
        originalMazePan.removeAll();
        pathMazePan.removeAll();

        //when the user clicks "Import Maze" button
        if (command.equals("Import Maze")){
            fileName = fileNameText.getText(); //read file name from fileNameText text field
            try {
                readFileFunc(fileName); //call readFileFunc
            } catch (Exception e) { //catch exceptions
                e.printStackTrace(); //print exceptions
            }

        }
        //when the user clicks "Generate random maze" button
        else if (command.equals("Generate random maze")){
            row = Integer.parseInt(rowText.getText()); //read number of rows from rowText text field
            col = Integer.parseInt((colText.getText())); //read number of columns from rowText text field
            randMazeFunc(row, col); //call randMazeFunc

        }
        //when the user clicks "Generate random with path" button
        else if (command.equals("Generate maze with path")){
            row = Integer.parseInt(rowText.getText()); //read number of rows from rowText text field
            col = Integer.parseInt((colText.getText())); //read number of columns from rowText text field
            pathMazeFunc(row, col); //call pathMazeFunc
        }
    }

    /**
     * Reads the data from the file and assign them to variables, imports the maze, and
     * call methods to solve and display the maze.
     *
     * @param	fileName	String for the file name
     * @return				non-return type
     */
    public  void readFileFunc(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); //file reader reading from the given file name
        int numRow, numCol; //the number of rows, columns
        int[] iniPos = new int[2]; //the coordinate of the initial position in format [x,y]
        String[][] maze; //2D array of maze
        String block, road, mouse, exit; //Symbol for different components of the maze
        ArrayList<int[]> currentPath = new ArrayList<int[]>(); //list of coordinates of current path
        ArrayList<int[]> shortestPath = new ArrayList<int[]>(); //list of coordinates of the shortest path
        numRow = Integer.parseInt(br.readLine()); //read row
        numCol = Integer.parseInt(br.readLine()); //read col
        maze = new String[numRow][numCol]; //set size of maze with row and col
        //read and assign characters to the components
        block = br.readLine();
        road = br.readLine();
        mouse = br.readLine();
        exit = br.readLine();
        importMaze(maze, numRow, numCol, iniPos, mouse, br);
        br.close();
        findShortestPath(maze, block, exit, iniPos[0], iniPos[1], currentPath, shortestPath, 0);
        displayMaze(maze, shortestPath, numRow, numCol, block, road, mouse, exit);

    }

    /**
     * Declares a maze with the given rows and columns, and assigns the components with default characters,
     * then calls methods to generate a random maze, then solve and display.
     *
     * @param	numRow		int of the number of rows
     * @param	numCol		int of the number of columns
     * @return				non-return type
     */
    public  void randMazeFunc(int numRow, int numCol){
        ArrayList<int[]> currentPath = new ArrayList<int[]>(); //list of coordinates of current path
        ArrayList<int[]> shortestPath = new ArrayList<int[]>(); //list of coordinates of shortest path
        //assign default characters to components
        final String block = "B";
        final String road = "O";
        final String mouse = "S";
        final String exit = "X";
        String[][] maze = new String[numRow][numCol]; //declare maze and set size of maze with row and col
        int[] iniPos = new int[2]; //the coordinate of the initial position in format [x,y]
        int[] exitPos = new int[2]; //the coordinate of the exit position in format [x,y]
        generateExitPos(exitPos, numRow, numCol); //generate exit position
        generateIniPos(iniPos, numRow, numCol); //generate initial position
        generateMaze(maze, iniPos, exitPos, numRow, numCol, block, road, mouse, exit); //generate maze
        findShortestPath(maze, block, exit, iniPos[1], iniPos[0], currentPath, shortestPath, 0); //find shortest path
        displayMaze(maze, shortestPath, numRow, numCol, block, road, mouse, exit); //display maze

    }

    /**
     * Declares a maze with the given rows and columns, and assigns the components with default characters,
     * then calls methods to generate a maze with path, then solve and display.
     *
     * @param	numRow		int of the number of rows
     * @param   numCol		int of the number of columns
     * @return				non-return type
     */
    public  void pathMazeFunc(int numRow, int numCol){
        ArrayList<int[]> currentPath = new ArrayList<int[]>(); //list of coordinates of current path
        ArrayList<int[]> shortestPath = new ArrayList<int[]>(); //list of coordinates of shortest path
        //assign default characters to components
        final String block = "B";
        final String road = "O";
        final String mouse = "S";
        final String exit = "X";
        String[][] maze = new String[numRow][numCol]; //declare maze and set size of maze with row and col
        int[] iniPos = new int[2]; //the coordinate of the initial position in format [x,y]
        int[] exitPos = new int[2]; //the coordinate of the exit position in format [x,y]
        generateExitPos(exitPos, numRow, numCol); //generate exit position
        generateIniPos(iniPos, numRow, numCol); //generate initial position
        generatePathMaze(maze, iniPos, exitPos, numRow, numCol, block, road, mouse, exit, currentPath, shortestPath); //generate maze with path
        findShortestPath(maze, block, exit, iniPos[1], iniPos[0], currentPath, shortestPath, 0); //find shortest path
        displayMaze(maze, shortestPath, numRow, numCol, block, road, mouse, exit); //display the maze
    }

    /**
     * Displays the original maze and the solved maze on the frame
     *
     * @param   maze            the 2D array of the original maze
     * @param   shortestPath    the list containing all the coordinates of the shortest path
     * @param   row             the number of rows of the maze
     * @param   col             the number of columns of the maze
     * @param   block           the character representing block/wall
     * @param   road            the character representing road
     * @param   mouse           the character representing mouse
     * @param   exit            the character representing exit
     * @return                  non-return type
     */
    public void displayMaze(String[][] maze, ArrayList<int[]> shortestPath, int row, int col, String block, String road, String mouse, String exit) {
        //set the layout of panel for maze and solved maze
        originalMazePan.setLayout(new GridLayout(row, col));
        pathMazePan.setLayout(new GridLayout(row, col));
        for (int i=0; i<row; i++){ //run through the rows of the maze
            for (int j=0; j<col; j++){ //run through the columns of the maze
                Panel temp = new Panel(); //declare a temporary panel
                //set panel to brown and add the character for block if the element is block in maze
                if (maze[i][j].equals(block)){
                    temp.setBackground(new Color(139, 69, 19));
                    temp.add(new Label(block));
                }
                //set panel to yellow and add the character for road if the element is road in maze
                else if (maze[i][j].equals(road)){
                    temp.setBackground(Color.YELLOW);
                    temp.add(new Label(road));
                }
                //set panel to gray and add the character for mouse if the element is mouse in maze
                else if (maze[i][j].equals(mouse)){
                    temp.setBackground(Color.GRAY);
                    temp.add(new Label(mouse));
                }
                //set panel to green and add the character for exit if the element is exit in maze
                else if (maze[i][j].equals(exit)){
                    temp.setBackground(Color.GREEN);
                    temp.add(new Label(exit));
                }
                originalMazePan.add(temp); //add the panel to panel for original maze
            }
        }

        //if the maze does not have a path, show no path on the panel for solved maze
        if (shortestPath.isEmpty()) {
            pathMazePan.setLayout(new BorderLayout());
            pathMazePan.add(new Label("                                                                                         No Path Found in the Given Maze"), BorderLayout.CENTER);
        }
        //if the maze has a path
        else{
            String[][] mazeWithPath = new String[row][col]; //declare a 2D array storing path
            for (int[] node : shortestPath) { //run through all coordinates in shortestPath
                //add + to mazeWithPath from shortestPath if the point is not mouse or exit
                if (!maze[node[0]][node[1]].equals(mouse) && !maze[node[0]][node[1]].equals(exit)) {
                    mazeWithPath[node[0]][node[1]] = "+";
                }
            }
            for (int i = 0; i < row; i++) { //run through the rows of maze
                for (int j = 0; j < col; j++) { //run through the columns or maze
                    Panel temp = new Panel(); //declare a temporary panel
                    //set panel green and add + if path exists here
                    if (mazeWithPath[i][j] != null) {
                        temp.setBackground(Color.CYAN);
                        temp.add(new Label("+"));
                    //if no path exits here, set the panel according to the element at the place
                    } else {
                        if (maze[i][j].equals(block)){
                            temp.setBackground(new Color(139, 69, 19));
                            temp.add(new Label(block));
                        }
                        else if (maze[i][j].equals(road)){
                            temp.setBackground(Color.YELLOW);
                            temp.add(new Label(road));
                        }
                        else if (maze[i][j].equals(mouse)){
                            temp.setBackground(Color.GRAY);
                            temp.add(new Label(mouse));
                        }
                        else if (maze[i][j].equals(exit)){
                            temp.setBackground(Color.GREEN);
                            temp.add(new Label(exit));
                        }
                    }
                    pathMazePan.add(temp); //add the panel to pathMazePan
                }
            }
        }
        setVisible(true);
    }

    /**
     * Generates a maze with path.
     *
     * @param maze          2D array of the maze
     * @param iniPos        array of initial position
     * @param exitPos       array of exit position
     * @param numRow        integer of number of rows
     * @param numCol        integer of number of columns
     * @param block         String for character of block
     * @param road          String for character of road
     * @param mouse         String for character of mouse
     * @param exit          String for character of exit
     * @param currentPath   ArrayList of int array for coordinates of current path
     * @param shortestPath  ArrayList of int array for coordinates of the shortest path
     */
    public static void generatePathMaze(String[][] maze, int[] iniPos, int[] exitPos, int numRow,int numCol, String block, String road, String mouse, String exit, ArrayList<int[]> currentPath, ArrayList<int[]> shortestPath){
        do{
            generateMaze(maze, iniPos, exitPos, numRow, numCol, block, road, mouse, exit); //generate a random maze
            //clear currentPath and shortestPath
            currentPath.clear();
            shortestPath.clear();
            //find shortest path to see if the maze contains a path
            findShortestPath(maze, block, exit, iniPos[1], iniPos[0], currentPath, shortestPath, 0);
        }while(shortestPath.isEmpty()); //repeat until a maze with path is created
    }

    /**
     * Generates a random maze.
     *
     * @param maze          2D array of the maze
     * @param iniPos        array of initial position
     * @param exitPos       array of exit position
     * @param numRow        integer of number of rows
     * @param numCol        integer of number of columns
     * @param block         String for character of block
     * @param road          String for character of road
     * @param mouse         String for character of mouse
     * @param exit          String for character of exit
     */
    public static void generateMaze(String[][] maze, int[] iniPos, int[] exitPos, int numRow,int numCol, String block, String road, String mouse, String exit){
        //run through the whole maze
        for (int i = 0; i < numRow;i++){
            for(int j = 0; j < numCol;j++){
                int checkItem = (int)(Math.random()*2); //generate random number between 0 or 1
                //let the position be wall if checkItem is 0
                if(checkItem==0){
                    maze[i][j]=block;
                }
                //otherwise, the position is road
                else{
                    maze[i][j]=road;
                }
            }
        }

        //set the borders to wall/block
        for (int i=0; i<numRow; i++){
            maze[i][0] = block;
            maze[i][numCol-1] = block;
        }
        for (int i=0; i<numCol; i++){
            maze[0][i] = block;
            maze[numRow-1][i] = block;
        }

        //assign mouse and exit to the generated position
        maze[iniPos[1]][iniPos[0]]=mouse;
        maze[exitPos[1]][exitPos[0]]=exit;
    }

    /**
     * Generates the initial position of the mouse and stores in an array.
     *
     * @param iniPos    int array for the initial position
     * @param numRow    int for number of rows of the maze
     * @param numCol    int for number of columns of the maze
     */
    public static void generateIniPos(int[] iniPos, int numRow,int numCol){
        //generate initial position that it is not on the borders
        iniPos[0] = (int) (Math.random()*(numCol-2))+1;
        iniPos[1] = (int) (Math.random()*(numRow-2))+1;

    }

    /**
     * Generates the exit position of the mouse and stores in an array.
     *
     * @param exitPos    int array for the exit position
     * @param numRow    int for number of rows of the maze
     * @param numCol    int for number of columns of the maze
     */
    public static void generateExitPos(int[] exitPos,int numRow,int numCol){
        int checkPos = (int)(Math.random()*2); //generate random number between 0 and 1
        //set exitPos on horizontal borders if random number is 1
        if(checkPos==1){
            int checkWhichRow = (int)(Math.random()*2); //generate random number between 0 and 1
            //set exitPos on top if random number is 1
            if(checkWhichRow==1){
                exitPos[0] = (int)(Math.random()*(numCol-2))+1;
                exitPos[1] = 0;
            }
            //set exitPos to bottom
            else{
                exitPos[0] = (int)(Math.random()*(numCol-2))+1;
                exitPos[1] = numRow-1;
            }
        }
        //set exitPos on vertical borders if random number is 2
        else{
            int checkWhichCol = (int)(Math.random()*2); //generate random number between 0 and 1
            //set exitPos to left if random number is 1
            if(checkWhichCol==1){
                exitPos[1] = (int)(Math.random()*(numRow-2))+1;
                exitPos[0] = 0;
            }
            //set exitPos to right
            else{
                exitPos[1] = (int)(Math.random()*(numRow-2))+1;
                exitPos[0] = numCol-1;
            }
        }
    }

    /**
     * Read the maze from a file and stores in a 2D array.
     *
     * @param maze      2D String array for maze
     * @param row       int for number of rows
     * @param col       int for number of columns
     * @param iniPos    int array for initial position
     * @param mouse     String for character of mouse
     * @param br        BufferedReader that reads the file
     * @throws IOException
     */
    public static void importMaze(String[][] maze, int row, int col, int[] iniPos, String mouse, BufferedReader br)
            throws IOException {
        //run through the maze
        for (int i = 0; i < row; i++) {
            char[] line = br.readLine().toCharArray(); //read one line and parse it to charArray
            for (int j = 0; j < col; j++) {
                maze[i][j] = Character.toString(line[j]); //read characters from the charArray
                //store the position of the initial position of mouse
                if (Character.toString(line[j]).equals(mouse)) {
                    iniPos[0] = i;
                    iniPos[1] = j;
                }
            }
        }
    }

    /**
     * Finds the shortest path of the maze using a recursive algorithm and stores in a ArrayList of int array
     *
     * @param maze              2D String array for maze
     * @param barrier           String for character of block
     * @param exit              String for character of exit
     * @param x                 int for the x coordinate of initial position
     * @param y                 int for the y coordinate of initial position
     * @param currentPath       ArrayList of int array for the current path
     * @param shortestPath      ArrayList of int array for the shortest path
     * @param currentLength     int for length of current path
     */
    public static void findShortestPath(String[][] maze, String barrier, String exit, int x,
                                        int y, ArrayList<int[]> currentPath, ArrayList<int[]> shortestPath, int currentLength) {
        // If the current position is out of bounds, or at the wall, or is already in
        // the current path, then return
        if (x < 0 || y < 0 || x >= maze.length || y >= maze[0].length || maze[x][y].equals(barrier)
                || containsArray(currentPath, new int[] { x, y })) {
            return;
        }
        // If a path has been found, and the length of the current path is not less than
        // the length of the shortest path, also return
        if (!shortestPath.isEmpty() && currentLength >= shortestPath.size()) {
            return;
        }


        // Add the current location to the current path
        int[] temp = { x, y };
        currentPath.add(temp);


        // If the current position is an exit
        if (maze[x][y].equals(exit)) {
            // If the current path is shorter, or the path has not been found, update the
            // shortest path
            if (shortestPath.isEmpty() || currentPath.size() < shortestPath.size()) {
                shortestPath.clear();
                shortestPath.addAll(currentPath);
            }
        } else {
            // Otherwise, recursively try up, down, left, and right directions
            findShortestPath(maze, barrier, exit, x - 1, y, currentPath, shortestPath, currentLength + 1);
            findShortestPath(maze, barrier, exit, x + 1, y, currentPath, shortestPath, currentLength + 1);
            findShortestPath(maze, barrier, exit, x, y - 1, currentPath, shortestPath, currentLength + 1);
            findShortestPath(maze, barrier, exit, x, y + 1, currentPath, shortestPath, currentLength + 1);
        }


        // removing the current position from the current path
        currentPath.remove(currentPath.size() - 1);
    }

    /**
     * Checks if the ArrayList contains an array, returns true if yes, false if no.
     *
     * @param list      ArrayList of array used to check if it contains certain array
     * @param target    int array used to find if it exits in the ArrayList
     * @return          returns true if list contains target, returns false if list does not contain target
     */
    public static boolean containsArray(ArrayList<int[]> list, int[] target) {
        //run through all the arrays in the ArrayList
        for (int[] arr : list) {
            if (Arrays.equals(arr, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The main method of class MazeGUI.
     *
     * @param args  String array arguments
     */
    public static void main(String[] args) {
        new MazeGUI();
    }
}


