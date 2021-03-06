import java.io.*;
import java.util.*;
import java.nio.channels.FileChannel;

/**
Takes all arguments and send these to the StrainTracerInput where it gets stored.
**/
class InputComp{
	private String first, last, lat, lon, location, date, source, fastqR, fastqL, output, right, left;
	
	public InputComp(String first, String last, String lat, String lon, String location, String dateSampled, String source, 
		String r, String l, String output, String right, String left){
		this.first = first;
		this.last = last;
		this.lat = lat;
		this.lon = lon;
		this.location = location;
		this.date = dateSampled;
		this.source = source;
		this.fastqR = r;
		this.fastqL = l;
		this.output = output;
		this.right = right;
		this.left = left;
		createEntry();
	}

	public void createEntry(){
		try{
			File file = new File(output);
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			String del = ";"; //delimiter
			writer.write(first + del + last + del + lat + del + lon + del + location + del + date + del + source + del + fastqR + del + fastqL + '\n');
			writer.close();

			File dest = new File(right);
			File source = new File(fastqR);
			InputStream input = new FileInputStream(source);
			OutputStream output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}

			dest = new File(left);
			source = new File(fastqL);
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf1 = new byte[1024];
			int bytesRead1;
			while ((bytesRead1 = input.read(buf1)) > 0) {
				output.write(buf1, 0, bytesRead1);
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		//firstname, lastname, latitude, longitude, location name, source, sequence fastq
		new InputComp(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11]);
	}
}
