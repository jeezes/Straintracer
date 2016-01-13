import java.io.PrintWriter;

/**
Takes all arguments and send these to the StrainTracerInput where it gets stored.
**/
public class InputComp{
	private String first, last, lat, lon, location, source, fastq;
	
	public InputComp(String first, String last, String lat, String lon, String location, String source, String fastq){
		this.first = first;
		this.last = last;
		this.lat = lat;
		this.lon = lon;
		this.location = location;
		this.source = source;
		this.fastq = fastq;
		createEntry();
	}
	
	public void createEntry(){
		try{
			PrintWriter writer = new PrintWriter("input_data.txt", "UTF-8");
			String del = ";"; //delimiter
			writer.println(first + del + last + del + lat + del + lon + del + location + del + source + del + fastq);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		//firstname, lastname, latitude, longitude, location name, source, sequence fastq
		new InputComp(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
	}
}