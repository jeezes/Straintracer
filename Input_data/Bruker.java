class Bruker{
	int bruker_id;
	String navn;

	Bruker(int bruker_id, String navn){
		this.bruker_id = bruker_id;
		this.navn = navn;
	}

	public String getNavn(){ return navn; }

	public int getId(){ return bruker_id; }

	public void print(){
		System.out.println("Bruker_id " + bruker_id + "\tNavn " + navn);
	}
}