package Segment_data;

public class Segment{
	int segment_id, start, stop;
	String subsekvens;
	SegmentType segmentType;

	public Segment(int id, String s, SegmentType st, int start, int stop){
		this.segment_id = id;
		this.subsekvens = s;
		this.segmentType = st;
		this.stop = stop;
		this.start = start;
	}

	public void addType(SegmentType st){
		segmentType = st;
	}

	public int getId(){ return segment_id; }

	public int getStart(){ return start; }

	public int getStop(){ return stop; }

	public String getSequence(){ return subsekvens; }

	public SegmentType getType(){ return segmentType; }

	public void print(){
		System.out.println("ID: " + segment_id + "\tSekvensen: " + subsekvens + "\tStart: " + start + "\tStop: " + stop);
	}
}
