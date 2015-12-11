import java.util.*;
import java.io.*;
import java.text.*;

class Analyse{
	int analyse_id;
	ArrayList<AnalyseSeg> sekvenserBrukt;
	ArrayList<Punkter> punkter;
	ArrayList<Segement> segmenter;
	String metode;
	Date date_created;

	Analyse(int analyse_id){
		this.analyse_id = analyse_id;
		metode = "SNP";
		punkter = new ArrayList<Punkter>();
		segmenter = new ArrayList<Segement>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public ArrayList<Punkter> getPunkter(){
		return punkter;
	}

	public int getId(){ return analyse_id; }

	public ArrayList<Segement> getSegmenter(){
		return segmenter;
	}
}