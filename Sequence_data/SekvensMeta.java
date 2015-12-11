import java.util.*;
import java.io.*;
import java.text.*;

class SekvensMeta{
	int meta_id;
	Bruker contributor;
	ArrayList<SekvensDiff> sekDiff;
	Lokasjon lokasjon;
	Source source;
	Date date_created;

	SekvensMeta(int meta_id, Bruker contributor, Source source, Lokasjon lokasjon){
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

	public void print(){
		System.out.print("Meta_id: " + meta_id + "\tSource: " + source.source_name + "\tLokasjon: " + lokasjon.lokasjons_id + "\tBruker: " + contributor.bruker_id + "\tDate created: " + date_created);
	}
}