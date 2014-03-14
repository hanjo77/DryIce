import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DryIceWriter {

	private static DryIceWriter instance;
	private float temperature;
	private int moisture;
	private File outputFile;
	final String fileName = "moisture-results.csv";
	
	public static DryIceWriter getInstance() {
		if (instance == null) {
			instance = new DryIceWriter();
			instance.createFile();
	 	}
		return instance;
	}
	
	private void createFile() {
        outputFile = new File(fileName);
        writeToFile("Time;Moisture;temperature\n");
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int getMoisture() {
		return moisture;
	}

	public void setMoisture(int moisture) {
		this.moisture = moisture;
	}
	
	public void printValues() {
		System.out.println("Moisture; " + this.moisture + " | Temperature: " + this.temperature);
        String timeLog = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
        // This will output the full path where the file will be written to...
        writeToFile(timeLog + ";" + this.moisture + ";" + this.temperature + "\n");
 	}
	
	private void writeToFile(String data) {
        String line = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            while( ( line = bufferedReader.readLine() ) != null ) {
                bufferedWriter.write(line);
            }
            bufferedWriter.write(data);
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	}
}
