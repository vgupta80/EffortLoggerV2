package mainClass;

//JavaUI
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

 
public class pokerUI extends Application{
	Button btn; 
	StackPane root;
	VBox layout;
	String newProject;
	String userName;
	int newScore;
	int Round = 0;
	boolean finish = false;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    //Jack's Hashing Method
    //Jacks hashing Method 
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the password string to a byte array
            byte[] passwordBytes = password.getBytes();

            // Generate the hashed bytes
            byte[] hashedBytes = digest.digest(passwordBytes);

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString(); // Return the hashed password as a string
        } catch (NoSuchAlgorithmException e) {
            // Handle algorithm exception
            e.printStackTrace();
            return null;
        }
    }

    
    
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Planning Poker"); //Stage
        
    	//Creating the Username and Password Inputs 
    	Label label1 = new Label("Username:"); TextField textField = new TextField ();
		HBox hb = new HBox(); hb.getChildren().addAll(label1, textField); hb.setSpacing(10);
		
		Label label2 = new Label("Password:"); TextField textField2 = new TextField ();
		HBox hb2 = new HBox(); hb2.getChildren().addAll(label2, textField2); hb2.setSpacing(10);
    	
		//Creating Login and Create New Account Buttons
    	btn = new Button("Login");
        Button newUser = new Button("Create New Account");
        
        
        //Appending them to the Page
        hb.setAlignment(Pos.CENTER); hb2.setAlignment(Pos.CENTER);
        layout = new VBox(10); layout.setAlignment(Pos.CENTER); layout.getChildren().clear();layout.getChildren().addAll(hb,hb2,btn,newUser);
        root = new StackPane(); root.getChildren().add(layout);
        
        //Launch the User Interface
        primaryStage.setScene(new Scene(root, 500, 550));
        primaryStage.show();
       
        //Creating All Buttons that Will Be Used
        //This is so I can create instances for when they become clicked
        Button newGame = new Button("New Game");
        Button start = new Button("Start");
        Button agreed = new Button("Agreed");
        Button modify = new Button("Modify Scores");
        Button submit = new Button("Submit");
        Button reveal = new Button("Reveal Score");
        Button gameOver = new Button("Close Program");
        
		//Creating ChoiceBoxes for Users to Select From
        ChoiceBox<String> nameChoicebox = new ChoiceBox<>();
		ChoiceBox<Integer> scoreChoicebox = new ChoiceBox<>();
        ChoiceBox<String> choicebox = new ChoiceBox<>();
        choicebox.setValue("--Select Project Score--");
		String[] projects = {"1","2","3","4","5","6","7","8","9","10"};
		choicebox.getItems().addAll(projects);
  
		
		//When The User Clicks the Login Button
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//get input from textFields 
				userName = textField.getText(); 
				String password = textField2.getText(); String hashed = hashPassword(password);
				
				//if username & password are valid it then sends an AlertBox 
				//it then appends the newGame button
				if(sheets.login(userName, hashed) == true) {
					AlertBox.display("Login", "Welcome Back! You Now Have Access to Your Former Projects");
					root.getChildren().clear(); root.getChildren().add(newGame);
				}
				else {AlertBox.display("Oops!", "Invalid Credentials. Please Retry or Create New User.");}
				textField.clear();textField2.clear();
			}
		});
		
		//When the User Selects the 'Create New User' Button
		newUser.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//getting values that is in the textfields from the user
				userName = textField.getText(); 
				String password = textField2.getText(); String hashed = hashPassword(password);
				
				//if user doesn't already exist it alerts Success & clears to textFields to login
				if(sheets.create(userName, hashed) == true) {
					AlertBox.display("Success", "User has been created. Now please Login.");
					textField.clear();textField2.clear();
				}
				else {AlertBox.display("Oops!", "Error has Occured. Unable to add User.");}
				//layout.getChildren().add(btn2); layout.setAlignment(Pos.CENTER);
				//primaryStage.setScene(new Scene(layout, 500, 500));
			}
		});
		
        //When newGame is Selected		
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//appends textField to enter new project
				//it then appends the start button for the game as well
				Label labelx = new Label("Enter Name of New Project"); textField.setPrefColumnCount(20);
				HBox hb = new HBox(10); hb.getChildren().addAll(textField, start);
				hb.setAlignment(Pos.CENTER); layout.getChildren().clear();layout.getChildren().addAll(labelx, hb); layout.setAlignment(Pos.CENTER);
				root.getChildren().clear(); root.getChildren().add(layout);
			}
		});
		
		//When the Game Stars
		start.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//getting text from User
				newProject = textField.getText();
				
				//Alert Round of Game
				Label projectLabel = new Label(newProject); Round+=1;
				AlertBox.display("Planning Poker", "Round: "+Round );
				
				//If not the First Round gives User the option to modify project 
				if(Round <= 1) {
					//modify former projects
					layout.getChildren().clear(); layout.getChildren().addAll(projectLabel,choicebox,reveal);
					root.getChildren().clear(); root.getChildren().add(layout);
				}
				else {
					//If first Round then no option to modify previous project
					layout.getChildren().clear(); layout.getChildren().addAll(modify,projectLabel,choicebox,reveal);
					root.getChildren().clear(); root.getChildren().add(layout);
				}
				//newProject = textField.getText();
			}
		});
		
		//When a User Wants to Modify their Score
		modify.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//Appends UI 
				Label change = new Label("Select Project to Change");
				Label changeScore = new Label("Select New Score");
				//currentProjects Returns a List of all the Current Projects a User Has
				String projectList[] = sheets.currentProjects(userName);
				//We then append each Project to a ChoiceBox for the User to Select From
				for(String name: projectList) {nameChoicebox.getItems().add(name);}
				//Appending Scores for User to Choose From
				for(int x = 0; x<11;x++) {scoreChoicebox.getItems().add(x);}
				//Appending UI
				VBox y = new VBox(10); y.getChildren().addAll(changeScore, scoreChoicebox); y.setAlignment(Pos.CENTER);
				VBox x = new VBox(10); x.getChildren().addAll(change,nameChoicebox); x.setAlignment(Pos.CENTER);
				HBox horizontal = new HBox(20); horizontal.getChildren().addAll(x,y,submit); horizontal.setAlignment(Pos.CENTER);
				root.getChildren().clear();root.getChildren().addAll(horizontal);
			}
		});
		//Once User Chooses Project to Change and Selects Submit
		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//finds the projectName in the database and changes the score
				boolean success = sheets.updateProject(userName, nameChoicebox.getValue(), scoreChoicebox.getValue());
				if(success == true) {
					//if SuccessFul we remove a round so our logic remains in the same round
					//we Also change the start button so we can go back to that round as well and append it to the UI
					AlertBox.display("Done!", "Score of Project is Modified");
					Round -= 1; start.setText("Select to Go Back to Game");
					layout.getChildren().clear();layout.getChildren().addAll(start);
					root.getChildren().clear();root.getChildren().add(layout);
				}
				else {AlertBox.display("Error", "Unable to Modify Score of User");}
			}
		});
		
		//Selects 'Reveal'
		reveal.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//When the user Reveals their Score we Are simply getting the Value from the User
				//Then We are Displaying it and Asking the Fellow Participants if they Agree or Disagree
				Label projectScore = new Label(choicebox.getValue()); //get Value 
				start.setText("Disagree");  
				//changing start Button because if they disagree we will have to go back to the start page and play another round
				//Appending UI
				Label projectLabel = new Label("Do we agree with the Project Score?");
				HBox hb = new HBox(10); hb.getChildren().addAll(projectLabel, agreed, start); hb.setAlignment(Pos.CENTER);
				layout.getChildren().clear(); layout.getChildren().addAll(projectScore,hb);
				root.getChildren().clear(); root.getChildren().add(layout);
			}
		});
		
		//All Users Have Agreed On A Score and Select 'Agree'
		agreed.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//inserts Project into database
				boolean success = sheets.insertProject(userName, newProject, Integer.parseInt(choicebox.getValue()));
				if(success == true) {
					//if Successful game is over and appends button to close the program
					AlertBox.display("Game Over", "This Project Has Been Added to Your Portfolio.\nPlease Close Program.");
					root.getChildren().clear(); root.getChildren().add(gameOver);
				}
				else {AlertBox.display("Error!", "Program Unable to Close.");}
			}
		});
		
		//Closes the Stage
		gameOver.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				primaryStage.close();
			}
		});
		
    }

}
