package Segment_data;

import Analysis_data.Analyse;
import Sequence_data.*;

public class SegmentMeta{
	int meta_id;
	Analyse analyse;
	SegmentType segmentType;
	SekvensDiff sekvensDiff;

	public SegmentMeta(int id, Analyse a, SegmentType s, SekvensDiff d){ //OLD
		this.meta_id = id;
		this.analyse = a;
		this.segmentType = s;
		this.sekvensDiff = d;
	}

	public SegmentMeta(int id, Analyse a, SekvensDiff sd){
		this.meta_id = id;
		this.analyse = a;
		sekvensDiff = sd;
		segmentType = null; // OLD relation
	}

	public int getId(){ return meta_id; }

	public Analyse getAnalyse(){ return analyse; }

	public SegmentType getType(){ return segmentType; } //OLD

	public SekvensDiff getSekDiff(){ return sekvensDiff; }

	public void addType(SegmentType st){ //OLD
		segmentType = st;
	}

	public void print(){
		System.out.print("ID: " + meta_id + "\tAnalyseID: " + analyse.getId());
		System.out.println("\tSekvensDiffID: " + sekvensDiff.getId());
		System.out.println("Kommer fra analyse nr: " + analyse.getId());
	}
}