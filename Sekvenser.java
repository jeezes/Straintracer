import java.util.ArrayList;

class Sekvenser{
	String sekvens;
	ArrayList<SekvensDiff> meta;
	int sekundær_id;
	int sekvens_id;

	Sekvenser(int sekvens_id, String sekvens){
		this.sekvens_id = sekvens_id;
		meta = new ArrayList<SekvensDiff>();
		this.sekvens = sekvens;
		sekundær_id = 100;
	}

	public void addMeta(SekvensDiff s){
		meta.add(s);
	}

	public int getId(){ return sekvens_id; }

	public void print(){
		System.out.println("Sekvens_id: " + sekvens_id + "\nSekvens: " + sekvens);
		for(SekvensDiff s: meta){
			s.print();
		}
	}
}