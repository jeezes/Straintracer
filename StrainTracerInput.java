import java.util.ArrayList;
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

	StrainTracerInput(String firstname, String lastname, String lat, String lon, String farmName, String sourceName, String sequenceFastq){
		psql = new PsqlWriter();		
		findSource(sourceName);
		findContributor(firstname, lastname);
		findLocation(Double.parseDouble(lat), Double.parseDouble(lon), farmName);
		addSequence(sequenceFastq);
		
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
			if(source == null)
				source = new Source(++index, sourceName);
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

	}

	public static void main(String[] args){
		new StrainTracerInput(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
		//firstname, lastname, latitude, longitude, farm name, source, sequence fastq
	}
}
