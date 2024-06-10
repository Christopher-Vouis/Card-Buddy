import java.net.URL;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
	Button button;

	Gson gson;
	
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
		gson = new Gson();			
        primaryStage.setTitle("Card Buddy");       
        InitializeDeck();
        currentCard = currentDeck[0];
        currentImage = new Image(getClass().getResource("/images/" + currentCard.GetImagePath()).toExternalForm(), true);     
        InitializeDisplay();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void InitializeDisplay()
    {
    	imageView = new ImageView(currentImage);  
    	gridPane = new GridPane();
    	gridPane.setGridLinesVisible(true);
    	gridPane.add(imageView, 1, 1);    	
        scene = new Scene(gridPane, 500, 500);
        button = new Button("Next");
        button.setOnAction(e -> {
        	SwitchCard();
        	UpdateDisplay();
        	});
        gridPane.add(button, 1, 2);
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setHalignment(button, HPos.CENTER);        
        
        for(int i = 0; i < 3; ++i)
        {
        	ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(33.33);
            gridPane.getColumnConstraints().add(colConst);

            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(33.33);
            gridPane.getRowConstraints().add(rowConst);
        }
        
        gridPane.prefWidthProperty().bind(scene.widthProperty());
        gridPane.prefHeightProperty().bind(scene.heightProperty());        
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
	
    private void SwitchCard()
    {
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
    }
    
    private void UpdateDisplay()
    {
    	currentImage = new Image(getClass().getResource("/images/" + currentCard.GetImagePath()).toExternalForm(), true);   
    	imageView.setImage(currentImage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
