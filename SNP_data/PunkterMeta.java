package SNP_data;
import Sequence_data.*;
import Analysis_data.*;
import SNP_data.Punkter;

import java.util.ArrayList;

public class PunkterMeta{
	int punkterMeta_id;
	Analyse analyse;
	SekvensDiff[] sekvens;
	ArrayList<Punkter> punkter;

	public PunkterMeta(int id, Analyse a, SekvensDiff s1, SekvensDiff s2){
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

	public Analyse getAnalyse(){ return analyse; }

	public SekvensDiff getSequence1(){ return sekvens[0]; }

	public SekvensDiff getSequence2(){ return sekvens[1]; }

	public void print(){
		System.out.println("ID: " + punkterMeta_id + "\tAnalyse_id: " + analyse.getId());
		System.out.println("Fra sekvenser:");
		sekvens[0].print();
		sekvens[1].print();
	}
}