import Analysis_data.*;

/**
Stores information from the workflow after it has been executed
**/
class StrainTracerAnalyse{
	private PsqlWriter psql = null;
	private String method, sequences_used;
	private Analyse analyse = null;
	private AnalyseSeg seq = null;
	
	StrainTracerAnalyse(String method, String sequences){
		psql = new PsqlWriter();
		this.method = method;
		this.sequences_used = sequences;
		
		addAnalysis();
		addAnalysisSeg();
	}

	public void addAnalysis(){
		int index = psql.getLastKey("analysis");
		analyse = new Analyse(++index);
		analyse.setMethod(method);
		psql.addAnalysis(analyse);
	}
	
	public void addAnalysisSeg(){
		/*int index = psql.getLastKey("analysis_sequences");
		seq = new AnalyseSeg(analyse.getId(), 2); //tmp. 2 should be sequens diffs id
		psql.addAnalysisSeg(seq);*/
	}
	
	
	public static void main(String[] args){
		// method for analysis, sequences used
		new StrainTracerAnalyse(args[0], args[1]);
	}
}