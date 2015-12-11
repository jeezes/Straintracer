/**
Henter input fra brukeren og lagrer dette i databasen. Sender så sekvensene til workflowen
**/
class StrainTracerInput{
	Location location;
	Source source;
	Contributor contributor;
	Sekvenser sequence;
	SekvensMetaS meta;
	SekvensDiff diff;

	StrainTracerInput(String name, String latitude, String longitude, String farmName, String sourceName, String sequenceFastq){
		source = new Source(sourceName);
		contributor = new Contributor(name);
	}


	public static void main(String[] args){
		new StrainTracerInput(args[0], args[1], args[2], args[3], args[4], args[5])
		//Name, latitude, longitude, farm name, source, sequence fastq
	}
}