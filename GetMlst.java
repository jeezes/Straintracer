import java.util.*;
import java.io.*;
import 

public class GetMlst{
	PsqlWriter psql;

	public GetMlst(String option, String bac, String importedFrom){
		psql = new PsqlWriter();
		//System.out.println("Option: " + option + "\tbac: " + bac + "\tImportedFrom: " + importedFrom);
		if(option.equalsIgnoreCase("-i")){ // i == only adds alleles
			addAllele(bac, importedFrom);
		}else if(option.equalsIgnoreCase("-ist")){ // ist == only adds profiles
			addStProfile(bac, importedFrom);
		}else if(option.equalsIgnoreCase("-g")){ // extracts profiles and alleles
			getAllele(bac, importedFrom);
		}else{
			System.out.println("Wrong parameters used. Use -i to add alleles and genes. Use -ist to add ST profiles. Genes and alleles should be added first.");
		}
	}

	public GetMlst(String option, String output){
		//System.out.println("Option: " + option + "\tOutput: " + output);
		psql = new PsqlWriter();
		
	}

	public void addStProfile(String bac, String importedFrom){
		try{
			Scanner les = new Scanner(new File(bac));
			String[] lest = les.nextLine().split("\t");
			String[] genes = new String[lest.length - 2];
			for(int i = 0; i < genes.length; i++){
				genes[i] = lest[i+1];
			}
			String complex = "";
			while(les.hasNext()){
				lest = les.nextLine().split("\t");
				psql.addStProfile(genes, lest, importedFrom);
				//System.out.println(lest + " added to the DB.");
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 
	* Multiple fasta file and adds genes and alleles to the DB
	**/
	public void addAllele(String bac, String importedFrom){
		try{
			Scanner inn = new Scanner(new File(bac));
			String name = "";
			String seq = "";
			boolean getSequence = false;
			int secondaryId = 0;
			while(inn.hasNext()){
				String line = inn.nextLine();
				if(line.charAt(0) == '>'){
					if(!seq.equals("")){
						psql.addGene(name, seq, importedFrom, secondaryId);
						// adds gene if it not exists
					}
					String[] tmp = line.split("_");
					name = tmp[0].substring(1);
					secondaryId = Integer.parseInt(tmp[1]);
					seq = "";
				}else{
					seq += line;
				}
			}
			psql.addGene(name, seq, importedFrom, secondaryId);
			//System.out.println("Added allele to the DB. secondaryId: " + secondaryId);
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getAllele(String output, String outputAlleles){
		// forst, skriv ut alle profiler. dernest skriv ut alle alleler for hvert genes
		try{
			File file = new File(output);
			FileWriter fileWriter = new FileWriter(file, false);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(psql.getProfilesAndWrite());
			writer.close();
			//System.out.println("Output path: " + output);
			
			file = new File(outputAlleles);
			fileWriter = new FileWriter(file, false);
			writer = new BufferedWriter(fileWriter);
			writer.write(psql.getAllelesAndGenes());
			writer.close();
			//System.out.println("Output path: " + outputAlleles);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	public static void main(String[] args){
		//System.out.println("Numb of arguments: " + args.length);
		if(args.length == 3)
			new GetMlst(args[0], args[1], args[2]);
		else
			System.out.println("Wrong number of arguments. Takes 2 or 3");
	}
}