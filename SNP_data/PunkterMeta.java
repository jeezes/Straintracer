import java.util.ArrayList;

class PunkterMeta{
	int punkterMeta_id;
	Analyse analyse;
	SekvensDiff[] sekvens;
	ArrayList<Punkter> punkter;

	PunkterMeta(int id, Analyse a, SekvensDiff s1, SekvensDiff s2){
		this.punkterMeta_id = id;
		this.analyse = a;
		sekvens = new SekvensDiff[2];
		sekvens[0] = s1;
		sekvens[1] = s2;
		punkter = new ArrayList<Punkter>();
	}

	public void addPunkter(Punkter p){
		punkter.add(p);
	}

	public int getId(){ return punkterMeta_id; }

	public void print(){
		System.out.println("ID: " + punkterMeta_id + "\tAnalyse_id: " + analyse.analyse_id);
		System.out.println("Fra sekvenser:");
		sekvens[0].print();
		sekvens[1].print();
	}
}