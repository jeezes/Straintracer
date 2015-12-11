import java.util.ArrayList;
/**
	SekvensDiff lagrer en sekvens og tilhørende metaveriden for sekvensen. Slik at vi alltid vet hvilken sekvens som blir
	brukt i en analyse og vet hvilken sekvens som hører til et segment eller et punkt.
**/
class SekvensDiff{
	int sekDiff_id;
	Sekvenser sekvens;
	SekvensMeta meta;
	ArrayList<PunkterMeta> punktMeta;

	SekvensDiff(int id, Sekvenser s, SekvensMeta a){
		this.sekDiff_id = id;
		this.sekvens = s;
		this.meta = a;
		punktMeta = new ArrayList<PunkterMeta>();
	}

	public void addMeta(PunkterMeta pm){
		punktMeta.add(pm);
	}

	public void addPunktMeta(PunkterMeta pm){
		if(pm != null)
			punktMeta.add(pm);
	}

	public int getId(){ return sekDiff_id; }

	public void print(){
		System.out.println("ID: " + sekDiff_id + "\tSekvens_id: " + sekvens.sekvens_id + "\tMeta_id: " + meta.meta_id);
	}
}