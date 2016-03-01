import java.util.Scanner;
import java.io.File;

/**
Sends all information from one execution of the program and stores it in the StrainTracer 
All comments should be included when galaxy is set on the invivo server
**/
class ResultComp{
	private String inputFile, assemblyFile, resultFile;

	public ResultComp(String input, String assembly, String result){ //add report files
		this.inputFile = input;
		this.assemblyFile = assembly;
		this.resultFile = result;
		printAllFiles(); //THIS IS FOR DEBUGGING. REMOVE WHEN IN PRODUCTION
	}

	public void printAllFiles(){
		try{
			System.out.println("INPUTFILE");
			Scanner inn = new Scanner(new File(inputFile));
			while(inn.hasNext()){
				sendToDB(inn.nextLine());

			}

			System.out.println("\nASSEMBLYFILE");

			inn = new Scanner(new File(assemblyFile));
			while(inn.hasNext()){
				System.out.println(inn.nextLine());
			}

			System.out.println("\nRESULTFILE");

			inn = new Scanner(new File(resultFile));
			while(inn.hasNext()){
				System.out.println(resultFile);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToDB(String s){
		if(s.length() > 1){
			StrainTracerInput input = new StrainTracerInput(inputFile, assemblyFile);
		}
	}

	public static void main(String[] args){
		if(args.length != 3){
			System.out.println("Not enough arugments. Needed 3, got " + args.length);
			System.exit(-1);
		}else{
			new ResultComp(args[0], args[1], args[2]);
		}
	}
}