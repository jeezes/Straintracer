package Segment_data;

import java.util.ArrayList;
import Allele_data.Gen;

public class SegmentDesc{
	int segdesc_id;
	String description;
	Gen gen;

	public SegmentDesc(int id, String description){
		this.segdesc_id = id;
		this.description = description;
		gen = null;
	}

	public SegmentDesc(int id, String description, Gen g){
		this.segdesc_id = id;
		this.description = description;
		this.gen = g;
	}

	public int getId(){ return segdesc_id; }

	public String getDesc(){ return description; }

	public Gen getGen(){ return gen; }

	public void setGen(Gen g){ gen = g; }

	public void print(){
		System.out.println("SegmentDescID: " + segdesc_id + "\tDescription: " + description);
	}	
}