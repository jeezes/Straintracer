package Sequence_data;

import java.util.*;
import java.io.*;
import java.text.*;
import Input_data.*;

public class SekvensMeta{
	int meta_id;
	Bruker contributor;
	ArrayList<SekvensDiff> sekDiff;
	Lokasjon lokasjon;
	Source source;
	Date date_created;

	public SekvensMeta(int meta_id, Bruker contributor, Source source, Lokasjon lokasjon){
		this.meta_id = meta_id;
		this.contributor= contributor;
		this.source = source;
		sekDiff = new ArrayList<SekvensDiff>();
		this.lokasjon = lokasjon;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public void addSekvensTilMeta(SekvensDiff s){
		sekDiff.add(s);
	}

	public int getId(){ return meta_id; }

	public Bruker getContributor(){ return contributor; }

	public Lokasjon getLocation(){ return lokasjon; }

	public Source getSource(){ return source; }

	public Date getDate(){ return date_created;	}

	public void print(){
		System.out.print("Meta_id: " + meta_id + "\tSource: " + source.getName() + "\tLokasjon: " + lokasjon.getId() + "\tBruker: " + contributor.getId() + "\tDate created: " + date_created);
	}
}