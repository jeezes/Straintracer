package Analysis_data;

import java.util.*;
import java.io.*;
import java.text.*;
import SNP_data.Punkter;
import Segment_data.Segment;

public class Analyse{
	int analyse_id;
	ArrayList<AnalyseSeg> sekvenserBrukt;
	ArrayList<Punkter> punkter;
	ArrayList<Segment> segmenter;
	String metode;
	Date date_created;

	public Analyse(int analyse_id){
		this.analyse_id = analyse_id;
		metode = "SNP";
		punkter = new ArrayList<Punkter>();
		segmenter = new ArrayList<Segment>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public ArrayList<Punkter> getPunkter(){
		return punkter;
	}

	public int getId(){ return analyse_id; }

	public String getMethod(){ return metode; }

	public Date getDate(){ return date_created; }

	public ArrayList<Segment> getSegmenter(){
		return segmenter;
	}
}