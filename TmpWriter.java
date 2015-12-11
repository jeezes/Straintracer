import java.util.*;
import java.io.*;
import java.text.*;

/**
Test for å lagre info fra brukeren og isolatet som blir lagt til.
TODO: Legg til filene og lagre de i mappe for isolater
**/
class TmpWriter{
	String name, location, source;
	Date date;
	

	TmpWriter(String name, String location, String source){
		this.name = name;
		this.location = location;
		this.source = source;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
		writeToFile();
	}

	public void writeToFile(){
		try{
			PrintWriter writer = new PrintWriter("info_fra_bruker.txt", "UTF-8");
			writer.println("Bruker: " + name + "\nlocation: " + location + "\nSource: " + source + "\nDate: " + date);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Kan ikke lagre brukerinfo.");
		}
	}

	/**
	args[0] = name
	args[1] = location
	args[2] = source
	**/
	public static void main(String[] args){
		new TmpWriter(args[0], args[1], args[2]);
	}
}