class SegmentType{
	int st_id;
	Segement segment;
	SegmentMeta meta;
	SegmentDesc segmentDescription;
	SekvensDiff sekvens;

	SegmentType(int id, Segement s, SegmentMeta m, SegmentDesc sd, SekvensDiff diff){
		this.st_id = id;
		this.segment = s;
		this.meta = m;
		this.segmentDescription = sd;
		this.sekvens = diff;
	}

	public int getId(){ return st_id; }

	public void print(){
		System.out.println("SegmentTypeID: " + st_id + "\tSegmentID: " + segment.segment_id + "\tSegmentMetaID: " + meta.meta_id + "\tSekvensDiff: " + sekvens.getId());
		segment.print();
		meta.print();
	}
}