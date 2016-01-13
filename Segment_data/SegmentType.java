package Segment_data;

import Sequence_data.SekvensDiff;

public class SegmentType{
	int st_id;
	Segment segment;
	SegmentMeta meta;
	SegmentDesc segmentDescription;
	SekvensDiff sekvens;

	public SegmentType(int id, Segment s, SegmentMeta m, SegmentDesc sd, SekvensDiff diff){ //OLD
		this.st_id = id;
		this.segment = s;
		this.meta = m;
		this.segmentDescription = sd;
		this.sekvens = diff;
	}

	public SegmentType(int id, Segment s, SegmentMeta m, SegmentDesc sd){
		this.st_id = id;
		this.segment = s;
		this.meta = m;
		this.segmentDescription = sd;
		this.sekvens = null;
	}


	public int getId(){ return st_id; }

	public Segment getSegment(){ return segment; }

	public SegmentMeta getMeta(){ return meta; }

	public SegmentDesc getDesc(){ return segmentDescription; }

	public SekvensDiff getSekDiff(){ return sekvens; }

	public void print(){
		System.out.println("SegmentTypeID: " + st_id + "\tSegmentID: " + segment.segment_id + "\tSegmentMetaID: " + meta.meta_id + "\tSekvensDiff: " + sekvens.getId());
		segment.print();
		meta.print();
	}
}