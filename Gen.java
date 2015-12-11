class Gen{
	int gen_id;
	String resistantTo;
	String genName;


	Gen(int id, String n, String r){
		this.gen_id = id;
		this.genName = n;
		this.resistantTo = r;
	}

	public int getId(){ return gen_id; }

	public void print(){
		System.out.println("GenID: " + gen_id + "\tGenNavn: " + genName + "\tResistant to: " + resistantTo);
	}


}