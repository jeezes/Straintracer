

class Segement{
	int segment_id, start, stop;
	String subsekvens;
	SegmentType segmentType;

	Segement(int id, String s, SegmentType st, int start, int stop){
		this.segment_id = id;
		this.subsekvens = s;
		this.segmentType = st;
		this.stop = stop;
		this.start = start;
	}

	public int getId(){ return segment_id; }

	public void print(){
		System.out.println("ID: " + segment_id + "\tSekvensen: " + subsekvens + "\tStart: " + start + "\tStop: " + stop);
	}
}