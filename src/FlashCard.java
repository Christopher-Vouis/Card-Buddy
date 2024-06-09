import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class FlashCard {
	private String targetWord = "", reading= "", definition= "",  imagePath = "";

	public FlashCard(String inWord, String inReading, String inDefinition, String inImagePath)
	{
		targetWord = inWord;
		reading = inReading;
		definition= inDefinition;
		imagePath = inImagePath;
	}
	
	public String GetWord()
	{
		return targetWord;
	}
	
	public String GetReading()
	{
		return reading;
	}
	
	public String GetDefinition()
	{
		return definition;
	}
	
	public String GetImagePath()
	{
		return imagePath;
	}
}
