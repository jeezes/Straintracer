class AnalyseSeg{
	int analyseSeg_id;
	Analyse analyse;
	SekvensDiff sekDiff;

	AnalyseSeg(int id, Analyse a, SekvensDiff s){
		this.analyseSeg_id = id;
		this.analyse = a;
		this.sekDiff = s;
	}

	public int getId(){ return analyseSeg_id; }
}