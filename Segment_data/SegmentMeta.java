class SegmentMeta{
	int meta_id;
	Analyse analyse;
	SegmentType segmentType;
	SekvensDiff sekvensDiff;

	SegmentMeta(int id, Analyse a, SegmentType s, SekvensDiff d){
		this.meta_id = id;
		this.analyse = a;
		this.segmentType = s;
		this.sekvensDiff = d;
	}

	public int getId(){ return meta_id; }

	public void print(){
		System.out.print("ID: " + meta_id + "\tAnalyseID: " + analyse.analyse_id + "\tSegmentTypeID: " + segmentType.st_id);
		System.out.println("\tSekvensDiffID: " + sekvensDiff.sekDiff_id);
		System.out.println("Kommer fra analyse nr: " + analyse.analyse_id);
	}
}