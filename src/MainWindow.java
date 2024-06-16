import java.net.URL;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	Scene scene;
	FlashCard[] currentDeck;
	FlashCard currentCard;
	String deckFile = "resources/cards/test deck.txt";
	ImageView imageView;
	VBox imageContainer;
	Button flipButton, againButton, easyButton, normalButton, hardButton, currentButton;
	Text currentWord,
		currentDefinition,
		currentReading;
	VBox mainContainer, cardContainer;
	HBox answerButtons;

	Gson gson;
	
	double imageWidth, imageHeight;
	
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
		gson = new Gson();			
        primaryStage.setTitle("Card Buddy");             
        InitializeDeck();
        InitializeButtons();
        InitializeDisplay();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void InitializeDisplay()
    {
    	mainContainer = new VBox();
        scene = new Scene(mainContainer, 800, 800);    
    	cardContainer = new VBox();
    	imageView = new ImageView(); 
    	SetImage(currentCard.GetImagePath());
    	imageView.setPreserveRatio(true);
    	imageContainer = new VBox(imageView);

    	cardContainer.getChildren().add(imageContainer);
    	cardContainer.getChildren().add(currentReading);
    	cardContainer.getChildren().add(currentWord);
    	cardContainer.getChildren().add(currentDefinition);
    	mainContainer.getChildren().add(cardContainer);
    	mainContainer.getChildren().add(flipButton); 	
    
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setSpacing(50);

        mainContainer.setAlignment(Pos.CENTER);
        answerButtons.setAlignment(Pos.CENTER);
        
        mainContainer.prefWidthProperty().bind(scene.widthProperty());
        mainContainer.prefHeightProperty().bind(scene.heightProperty());
        cardContainer.setStyle("-fx-border-width: 3; -fx-border-color: red;");
        imageContainer.setStyle("-fx-border-width: 3; -fx-border-color: green; -fx-padding: 10px");
        imageContainer.setAlignment(Pos.CENTER);
        
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue();
            if (newWidth < imageWidth) {
            	imageView.setFitWidth(newWidth * 0.95f);
            } else {
            	imageView.setFitWidth(imageWidth);
            }
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue();
            if (newHeight < imageHeight) {
            	imageView.setFitHeight(newHeight * 0.95f);
            } else {
            	imageView.setFitHeight(imageHeight);
            }
        });
        
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
        currentCard = currentDeck[0];
        currentWord = new Text(currentCard.GetWord());
        currentDefinition = new Text(currentCard.GetDefinition());
        currentDefinition.setVisible(false);
        currentReading = new Text(currentCard.GetReading());
        currentReading.setVisible(false);
    }
    
    private void FlipCard()
    {
    	currentDefinition.setVisible(true);
    	currentReading.setVisible(true);
    	mainContainer.getChildren().remove(flipButton);
    	mainContainer.getChildren().add(answerButtons);
    }
	
    private void SwitchCard()
    {
    	currentDefinition.setVisible(false);
    	currentReading.setVisible(false);
    	if(currentCard == currentDeck[0])
    	{
    		currentCard = currentDeck[2];
    	}
    	else
    	{
        	{
        		currentCard = currentDeck[0];
        	}
    	}
    	mainContainer.getChildren().remove(answerButtons);
    	mainContainer.getChildren().add(flipButton);
    }
    
    private void UpdateDisplay()
    {
    	SetImage(currentCard.GetImagePath());
    	currentWord.setText(currentCard.GetWord());
        currentDefinition.setText(currentCard.GetDefinition());
        currentReading.setText(currentCard.GetReading());       
    }
    
    private void SetImage(String imgPath)
    {
    	Image newImage = new Image(getClass().getResource("/images/" + imgPath).toExternalForm(), true);

    	newImage.progressProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == 1.0) {
                imageWidth = newImage.getWidth();
                imageHeight = newImage.getHeight();
                System.out.println("Image loaded with dimensions: " + imageWidth + "x" + imageHeight);
                
                imageView.setImage(newImage);
                
                if(imageWidth >= 50)
                {
                	imageView.minWidth(50);
                }
                else
                {
                	imageView.minWidth(imageWidth);
                }
                
                if(imageHeight >= 50)
                {
                	imageView.minHeight(50);
                }
                else
                {
                	imageView.minHeight(imageHeight);
                }

            	if(imageWidth >= scene.getWidth())
            	{
            		imageView.setFitWidth(scene.getWidth() * 0.95f);        	
                } 
            	else 
                {
            		imageView.setFitWidth(imageWidth);
                }
            	
            	if(imageHeight >= scene.getHeight())
            	{
            		imageView.setFitHeight(scene.getHeight() * 0.95f);        	
                } 
            	else 
                {
            		imageView.setFitHeight(imageHeight);
                }
            }
        });



    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
