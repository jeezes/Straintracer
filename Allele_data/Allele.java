package Allele_data;

import java.util.*;
import java.io.*;
import java.text.*;
import Segment_data.SegmentType;

public class Allele{
	int allele_id;
	Gen gen; //unique with allele_id.... OLD
	SegmentType segmentType;
	Date date_created;

	public Allele(int id, Gen g, SegmentType s){ //OLD
		this.allele_id = id;
		this.gen = g;
		this.segmentType = s;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public Allele(int id, SegmentType s){
		this.allele_id = id;
		this.gen = null;
		this.segmentType = s;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public int getId(){ return allele_id; }

	public Gen getGen(){ return gen; } //OLD

	public SegmentType getType(){ return segmentType; }

	public Date getDate(){ return date_created; }

	public void print(){
		System.out.println("AlleleID: " + allele_id + "\tDate created: " + date_created);
	}

}