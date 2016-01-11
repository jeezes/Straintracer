import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import Input_data.*;
import Sequence_data.*;

/**
Henter input fra brukeren og lagrer dette i databasen. Sender så sekvensene til workflowen
**/
class StrainTracerInput{
	private Lokasjon location;
	private Source source;
	private Bruker contributor;
	private Sekvenser sequence;
	private SekvensMeta meta;
	private SekvensDiff diff;
	private PsqlWriter psql;

	StrainTracerInput(String file){
		//Edit this back to how it was. no need for readFile
		psql = new PsqlWriter();
		readFile(file);
		
	}

	public void readFile(String file){
		try{
			Scanner reader = new Scanner(new File(file));
			String[] line = reader.nextLine().split(";");
			for(String l: line)
				System.out.println(l);
			String firstname = line[0];
			String lastname = line[1];
			double lat = Double.parseDouble(line[2]);
			double lon = Double.parseDouble(line[3]);
			String farmName = line[4];
			String sourceName = line[5];
			String seqFile = line[6];
			
			findSource(sourceName);
			findContributor(firstname, lastname);
			findLocation(lat, lon, farmName);
			System.out.println("readFile: " + seqFile);
			addSequence(seqFile);
		
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
				psql.addSource(source);
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
		if(location == null)
			location = new Lokasjon(++index, farmName, lat, lon);
		System.out.println(location.getId() + "\t" + location.getGaard() + "\t" + location.getLatitude() + "\t" + location.getLongitude());
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

	public void addSequence(String seq){
		System.out.println("seq: " + seq);
		psql.addSequence(seq);
	}

	public static void main(String[] args){
		new StrainTracerInput(args[0]);
		// Input is a file .txt with info about this:
		//firstname, lastname, latitude, longitude, farm name, source, sequence fastq
		//delimiter = ;
	}
}
