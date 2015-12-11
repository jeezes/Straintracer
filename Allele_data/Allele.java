import java.util.*;
import java.io.*;
import java.text.*;

class Allele{
	int allele_id;
	Gen gen; //unique with allele_id
	SegmentType segementType;
	Date date_created;

	Allele(int id, Gen g, SegmentType s){
		this.allele_id = id;
		this.gen = g;
		this.segementType = s;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public int getId(){ return allele_id; }

	public void print(){
		System.out.println("AlleleID: " + allele_id + "\tDate created: " + date_created);
	}

}