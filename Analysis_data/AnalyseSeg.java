package Analysis_data;

import Sequence_data.SekvensDiff;

public class AnalyseSeg{
	int analyseSeg_id;
	Analyse analyse;
	SekvensDiff sekDiff;

	public AnalyseSeg(int id, Analyse a, SekvensDiff s){
		this.analyseSeg_id = id;
		this.analyse = a;
		this.sekDiff = s;
	} 

	public Analyse getAnalyse(){ return analyse; }

	public int getId(){ return analyseSeg_id; }

	public SekvensDiff getSekDiff(){ return sekDiff; }
}