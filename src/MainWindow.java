import java.net.URL;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javafx.scene.control.Button;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;


public class MainWindow extends Application {

	GridPane gridPane;
	Scene scene;
	Image currentImage;
	FlashCard[] currentDeck;
	FlashCard currentCard;
	String deckFile = "resources/cards/test deck.txt";
	ImageView imageView;
	Button flipButton, againButton, easyButton, normalButton, hardButton, currentButton;
	Text currentWord,
		currentDefinition,
		currentReading;
	VBox cardPane;
	HBox answerButtons;

	Gson gson;
	
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
		gson = new Gson();			
        primaryStage.setTitle("Card Buddy");       
        InitializeDeck();
        currentCard = currentDeck[0];
        currentImage = new Image(getClass().getResource("/images/" + currentCard.GetImagePath()).toExternalForm(), true);
        currentWord = new Text(currentCard.GetWord());
        currentDefinition = new Text(currentCard.GetDefinition());
        currentDefinition.setVisible(false);
        currentReading = new Text(currentCard.GetReading());
        currentReading.setVisible(false);
        InitializeDisplay();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void InitializeDisplay()
    {
    	cardPane = new VBox();
    	imageView = new ImageView(currentImage); 
    	cardPane.getChildren().add(imageView);
    	cardPane.getChildren().add(currentReading);
    	cardPane.getChildren().add(currentWord);
    	cardPane.getChildren().add(currentDefinition);
    	gridPane = new GridPane();
    	gridPane.setGridLinesVisible(true);
    	gridPane.add(cardPane, 1, 1);    	
        scene = new Scene(gridPane, 500, 500);
        InitializeButtons();       
        gridPane.add(flipButton, 1, 2);
        GridPane.setHalignment(cardPane, HPos.CENTER);
        GridPane.setValignment(cardPane, VPos.CENTER);
        GridPane.setHalignment(flipButton, HPos.CENTER);
        GridPane.setHalignment(answerButtons, HPos.CENTER);
        GridPane.setValignment(answerButtons, VPos.CENTER);
        
        float heightPercent, widthPercent;
        
        for(int i = 0; i < 3; ++i)
        {           
            if(i != 1)
            {
            	heightPercent = 10.00f;
            	widthPercent = 10.00f;
            }
            else
            {
            	heightPercent = 80.00f;
            	widthPercent = 80.00f;
            }
            
        	ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(widthPercent);
            gridPane.getColumnConstraints().add(colConst);

            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(heightPercent);
            gridPane.getRowConstraints().add(rowConst);
        }
        cardPane.setAlignment(Pos.CENTER);
        cardPane.setSpacing(50);
        
        gridPane.prefWidthProperty().bind(scene.widthProperty());
        gridPane.prefHeightProperty().bind(scene.heightProperty());
        gridPane.setAlignment(Pos.CENTER);
        answerButtons.setAlignment(Pos.CENTER);
    }
    
    private void InitializeButtons()
    {
        flipButton = new Button("Flip");
        againButton = new Button("Again");
        easyButton = new Button("Easy");
        normalButton = new Button("Normal");
        hardButton = new Button("Hard");
        
        againButton.setOnAction(e -> {
        	SwitchCard();
        	UpdateDisplay();
        	});
        
        hardButton.setOnAction(e -> {
        	SwitchCard();
        	UpdateDisplay();
        	});
        
        normalButton.setOnAction(e -> {
        	SwitchCard();
        	UpdateDisplay();
        	});
        
        easyButton.setOnAction(e -> {
        	SwitchCard();
        	UpdateDisplay();
        	});
        
        flipButton.setOnAction(e -> {
        	FlipCard();
        });
        
        answerButtons = new HBox(5);
        answerButtons.getChildren().add(againButton);
        answerButtons.getChildren().add(hardButton);
        answerButtons.getChildren().add(normalButton);
        answerButtons.getChildren().add(easyButton);
        
        answerButtons.getChildren().forEach(node -> 
        {
        	GridPane.setHalignment(node, HPos.CENTER);
        	GridPane.setValignment(node, VPos.CENTER);
        });
        
    }
    
    
    private void InitializeDeck()
    {
    	String jsonString = "";
    	StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(deckFile))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                jsonData.append(line);
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        jsonString = jsonData.toString();
        currentDeck = gson.fromJson(jsonString, FlashCard[].class);
    }
    
    private void FlipCard()
    {
    	currentDefinition.setVisible(true);
    	currentReading.setVisible(true);
    	gridPane.getChildren().remove(flipButton);
    	gridPane.add(answerButtons, 1, 2);
    }
	
    private void SwitchCard()
    {
    	currentDefinition.setVisible(false);
    	currentReading.setVisible(false);
    	if(currentCard == currentDeck[0])
    	{
    		currentCard = currentDeck[1];
    	}
    	else
    	{
        	{
        		currentCard = currentDeck[0];
        	}
    	}
    	gridPane.getChildren().remove(answerButtons);
    	gridPane.add(flipButton, 1, 2);
    }
    
    private void UpdateDisplay()
    {
    	currentImage = new Image(getClass().getResource("/images/" + currentCard.GetImagePath()).toExternalForm(), true);   
    	imageView.setImage(currentImage);
    	currentWord.setText(currentCard.GetWord());
        currentDefinition.setText(currentCard.GetDefinition());
        currentReading.setText(currentCard.GetReading());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
