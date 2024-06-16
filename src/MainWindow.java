import java.net.URL;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import com.google.gson.Gson;


public class MainWindow extends Application {
	Scene scene;
	FlashCard[] currentDeck;
	FlashCard currentCard;
	String deckFile = "resources/cards/test deck.txt";
	ImageView imageView;
	VBox imageContainer;
	Button flipButton, againButton, easyButton, normalButton, hardButton, audioButton;
	Text currentWord,
		currentDefinition,
		currentReading,
		currentSentence,
		currentTranslatedSentence;
	VBox scrollContainer, mainContainer, cardContainer;
	HBox answerButtons, buttonContainer;
	MediaPlayer mediaPlayer;
	Media currentAudio;
	Region spacerTop, spacerMid;
	ScrollPane scrollPane;
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
        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void InitializeDisplay()
    {   	
    	mainContainer = new VBox();
    	scrollContainer = new VBox();
    	scrollPane = new ScrollPane();
        scene = new Scene(scrollContainer, 1000, 1000);       
        spacerMid = new Region();
        spacerTop = new Region();
    	cardContainer = new VBox();
    	buttonContainer = new HBox();
    	imageView = new ImageView(); 
    	imageView.setPreserveRatio(true);
    	imageContainer = new VBox(imageView);
    	currentWord = new Text();
    	currentDefinition = new Text();
		currentReading = new Text();
		currentSentence = new Text();
		currentTranslatedSentence = new Text();
    	SetImage(currentCard.GetImagePath());
    	
    	SetFontStyles();
    	BuildCardDisplay();
    	
    	mainContainer.getChildren().add(spacerTop);
    	mainContainer.getChildren().add(cardContainer);
    	mainContainer.getChildren().add(spacerMid);
    	mainContainer.getChildren().add(buttonContainer);
    	VBox.setVgrow(spacerTop, Priority.ALWAYS);
    	VBox.setVgrow(spacerMid, Priority.ALWAYS);
    	buttonContainer.getChildren().add(flipButton); 	
    
        cardContainer.setAlignment(Pos.CENTER);

        mainContainer.setAlignment(Pos.CENTER);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setMinHeight(100);

        mainContainer.prefWidthProperty().bind(scene.widthProperty());
        mainContainer.prefHeightProperty().bind(scene.heightProperty());
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0; ");
        mainContainer.setStyle("-fx-background-color: #333333;");
        cardContainer.setStyle("-fx-border-width: 3; -fx-border-color: red; -fx-padding: 30px, 10px, 10px, 10px;");
        imageContainer.setStyle("-fx-border-width: 3; -fx-border-color: green; -fx-padding: 5%;");

        imageContainer.setAlignment(Pos.CENTER);
        
        scrollPane.setContent(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        scrollContainer.getChildren().add(scrollPane);
        
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
    
    private void SetFontStyles()
    {
        currentWord.setFill(Paint.valueOf("#DDDDDD"));
        currentDefinition.setFill(Paint.valueOf("#DDDDDD"));
        currentReading.setFill(Paint.valueOf("#DDDDDD"));
        currentSentence.setFill(Paint.valueOf("#DDDDDD"));
        currentTranslatedSentence.setFill(Paint.valueOf("#DDDDDD"));
        currentWord.setStyle("-fx-font-size: 65px;");
        currentDefinition.setStyle("-fx-font-size: 30px;");
        currentReading.setStyle("-fx-font-size: 25px;");
        currentSentence.setStyle("-fx-font-size: 25px;");
        currentTranslatedSentence.setStyle("-fx-font-size: 25px;");
    }
    
    private void InitializeButtons()
    {
        flipButton = new Button("Flip");
        againButton = new Button("Again");
        easyButton = new Button("Easy");
        normalButton = new Button("Normal");
        hardButton = new Button("Hard");
        audioButton = new Button("Play Audio");
        
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
        
        audioButton.setOnAction(e -> {
        	mediaPlayer.seek(Duration.ZERO);
        	mediaPlayer.play();
        	});
        
        answerButtons = new HBox(5);
        answerButtons.setAlignment(Pos.CENTER);
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
    	if(currentCard.GetAudioPath() != null)
    	{
    		audioButton.setVisible(true);
    	}
    	if(currentCard.GetReading() != null)
    	{
    		currentReading.setVisible(true);
    	}
    	if(currentCard.GetTranslatedSentence() != null)
    	{
    		currentTranslatedSentence.setVisible(true);
    	}
    	if(currentCard.GetDefinition() != null)
    	{
    		currentDefinition.setVisible(true);
    	}
    	
    	
    	buttonContainer.getChildren().remove(flipButton);
    	buttonContainer.getChildren().add(answerButtons);
    }
	
    private void SwitchCard()
    {
    	if(mediaPlayer != null && mediaPlayer.getStatus() == Status.PLAYING)
    	{
    		mediaPlayer.stop();
    	}

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
    	
    	buttonContainer.getChildren().remove(answerButtons);
    	buttonContainer.getChildren().add(flipButton);
    }
    
    private void BuildCardDisplay()
    {

    	if(cardContainer != null)
    	{
            cardContainer.getChildren().clear();
    	}

    	if(currentCard.GetImagePath() != null)
    	{
    		cardContainer.getChildren().add(imageContainer);
    		SetImage(currentCard.GetImagePath());
    	}
    	if(currentCard.GetAudioPath() != null)
    	{
    		currentAudio = new Media(getClass().getResource("/audio/" + currentCard.GetAudioPath()).toExternalForm());
    		mediaPlayer = new MediaPlayer(currentAudio);
    		cardContainer.getChildren().add(audioButton);
    		audioButton.setVisible(false);
    	}
    	if(currentCard.GetReading() != null)
    	{
    		cardContainer.getChildren().add(currentReading);
    		currentReading.setText(currentCard.GetReading());
    		currentReading.setVisible(false);
    	}
    	if(currentCard.GetWord() != null)
    	{
    		cardContainer.getChildren().add(currentWord);
    		currentWord.setText(currentCard.GetWord());
    	}
    	if(currentCard.GetDefinition() != null)
    	{
    		cardContainer.getChildren().add(currentDefinition);
    		currentDefinition.setText(currentCard.GetDefinition());
    		currentDefinition.setVisible(false);
    	}
    	if(currentCard.GetSentence() != null)
    	{
    		cardContainer.getChildren().add(currentSentence);
    		currentSentence.setText(currentCard.GetSentence());
    	}
    	if(currentCard.GetTranslatedSentence() != null)
    	{
    		cardContainer.getChildren().add(currentTranslatedSentence);
    		currentTranslatedSentence.setText(currentCard.GetTranslatedSentence());
    		currentTranslatedSentence.setVisible(false);
    	}
    }
    
    private void UpdateDisplay()
    {
    	BuildCardDisplay();
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
