import java.net.URL;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;


public class MainWindow extends Application {

	BorderPane cardDisplay;
	Scene scene;
	Image currentImage;
	FlashCard[] currentDeck;
	FlashCard currentCard;
	String deckFile = "resources/cards/test deck.txt";

	Gson gson;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
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
        cardDisplay = new BorderPane();
        cardDisplay.setCenter(new ImageView(currentImage));
        cardDisplay.setBottom(new Text(currentCard.GetWord()));
        scene = new Scene(cardDisplay, 500, 500);

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
	
    public static void main(String[] args) {
        launch(args);
    }
}
