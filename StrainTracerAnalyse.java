

/**
Stores information from the workflow after it has been executed
**/
class StrainTracerAnalyse{
	private PsqlWriter writer = null;
	
	StrainTracerAnalyse(){
		writer = new PsqlWriter();
		
	}


	public static void main(String[] args){
		new StrainTracerAnalyse();
	}
}