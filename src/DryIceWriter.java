import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DryIceWriter {

	private static DryIceWriter instance;
	final String fileName = "moisture-results.csv";
	private float temperature;
	private int moisture;
	private File outputFile;
    private BufferedWriter bufferedWriter;
	
	public static DryIceWriter getInstance() {
		if (instance == null) {
			instance = new DryIceWriter();

			instance.createFile();
	 	}
		return instance;
	}
	
	private void createFile() {
        outputFile = new File(fileName);
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void closeWriter(){
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void printValues() {
		System.out.println("Moisture; " + this.moisture + " | Temperature: " + this.temperature);
        String timeLog = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
        // This will output the full path where the file will be written to...
        writeToFile(timeLog + ";" + this.moisture + ";" + this.temperature + "\n");
 	}
	
	private void writeToFile(String data) {
        try {

            bufferedWriter.write(data);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	}
}
