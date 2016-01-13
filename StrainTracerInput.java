import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

import Input_data.*;
import Sequence_data.*;

/**
Henter input fra brukeren og lagrer dette i databasen. Sender så sekvensene til workflowen
Kjøres etter at SPAdes er ferdig
**/
class StrainTracerInput{
	private Lokasjon location;
	private Source source;
	private Bruker contributor;
	private ArrayList<Isolate> isolate;
	private Sekvenser sekvens;
	private SekvensMeta meta;
	private SekvensDiff diff;
	private PsqlWriter psql;

	//StrainTracerInput(String first, String last, String lat, String lon, String place, String sourceName, String seqFile){
	StrainTracerInput(String inputFile, String sequenceFile){
		psql = new PsqlWriter();
		isolate = new ArrayList<Isolate>();
		readFile(inputFile);
		readSequenceFile(sequenceFile);
	}
	
	public void readFile(String file){
		try{
			Scanner read = new Scanner(new File(file));
			while(read.hasNext()){
				String[] line = read.nextLine().split(";");
				String first = line[0];
				String last = line[1];
				double lat = Double.parseDouble(line[2]);
				double lon = Double.parseDouble(line[3]);
				String place = line[4];
				String sourceName = line[5];
				String isolateFile = line[6];
				findSource(sourceName);
				findContributor(first, last);
				findLocation(lat, lon, place);
				addIsolate(isolateFile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void readSequenceFile(String file){
		try{
			Scanner read = new Scanner(new File(file));
			while(read.hasNext()){
				String[] line = read.nextLine().split(";");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void findSource(String sourceName){
		try{
			ArrayList<Source> sources = psql.getSources();
			int index = 0;
			for(Source s : sources){
				index = s.getId();
				if(s.getName().equalsIgnoreCase(sourceName))
					source = s;
			}
			if(source == null){
				source = new Source(++index, sourceName);
				//TEST
				//psql.addSource(source);
				int i = psql.addSource(source);
				if(i != 1){
					System.out.println(source.getName() + " was not added to the DB. Possible wrong parameters.");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void findLocation(double lat, double lon, String farmName){
		ArrayList<Lokasjon> locations = psql.getLocations();
		int index = 0;
		for(Lokasjon l: locations){
			index = l.getId();
			if(l.getLatitude() == lat +- 5 && l.getLongitude() == lon +- 5 || l.getGaard().equalsIgnoreCase(farmName))
				location = l;
		}
		if(location == null){
			location = new Lokasjon(++index, farmName, lat, lon);
			psql.addLocation(location);
		}
	}
	

	public void findContributor(String firstname, String lastname){
		ArrayList<Bruker> brukere = psql.getContributors();
		int index = 0;
		for(Bruker b: brukere){
			index = b.getId();
			if(b.getLastName().equalsIgnoreCase(lastname)){
				if(b.getFirstName().equalsIgnoreCase(firstname))
					contributor = b;
			}
		}
		if(contributor == null){
			contributor = new Bruker(++index, firstname, lastname);
			psql.addContributor(contributor);
		}
	}

	public void addIsolate(String seq){
		int index = psql.getLastKey("isolate");
		String[] iso = seq.split(",");
		for(String s: iso){
			Isolate tmp = new Isolate(++index, s);
			psql.addIsolate(tmp);
			isolate.add(tmp);
		}
	}

	public static void main(String[] args){
		//firstname, lastname, latitude, longitude, location name, source, sequence fastq
		//new StrainTracerInput(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
		//input file, sequence meta file
		new StrainTracerInput(args[0], args[1]);
	}
}
