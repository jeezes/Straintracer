package Allele_data;

public class Gen{
	int gen_id;
	String resistantTo;
	String genName;

	public Gen(int id, String n, String r){
		this.gen_id = id;
		this.genName = n;
		this.resistantTo = r;
	}

	public int getId(){ return gen_id; }

	public String getName(){ return genName; }

	public String getResistantTo(){ return resistantTo; }

	public void print(){
		System.out.println("GenID: " + gen_id + "\tGenNavn: " + genName + "\tResistant to: " + resistantTo);
	}


}