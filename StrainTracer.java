import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
Java program for generating values for all tables in the database.
Each ArrayList object represent each table in the schema.
**/
class StrainTracer{
	int sekvens_id = 1, sekvens_meta = 1, bruker_id = 1, lokasjon_id = 1, snp_id = 1, punkter_meta = 1, analyse_id = 1, ana_seg_id = 1;
	int segDiff = 1, segment_id = 1, segmentMeta_id = 1, segmentType_id = 1, source_id = 1, segment_desc_id = 1, gen_id = 1, allele_id = 1;
	int sets_id = 1, st_id = 1, cc_id = 1, profile_id = 1;
	ArrayList<Lokasjon> lokasjoner;
	ArrayList<Bruker> brukere;
	ArrayList<Sekvenser> sekvenser;
	ArrayList<SekvensMeta> metaSekvenser;
	ArrayList<Punkter> snper;
	ArrayList<PunkterMeta> snpMeta;
	ArrayList<AnalyseSeg> analyseSeg;
	ArrayList<Analyse> analyser;
	ArrayList<SekvensDiff> sekvensDiff;
	ArrayList<Segement> segmenter;
	ArrayList<SegmentMeta> segmentMetaer;
	ArrayList<SegmentType> segmentTyper;
	ArrayList<Source> sources;
	ArrayList<Gen> gener;
	ArrayList<SegmentDesc> segment_descs;
	ArrayList<Allele> alleler;
	ArrayList<SetsDiff> setsDiffer;
	//ArrayList<Sets> setser;
	ArrayList<StProfile> stprofiler;
	ArrayList<CcProfile> ccprofiler;
	ArrayList<Profiles> profiles;
	double[] coords;
	//PsqlWriter pw;

	StrainTracer(){
		coords = new double[132];
		lokasjoner = new ArrayList<Lokasjon>();
		brukere = new ArrayList<Bruker>();
		sekvenser = new ArrayList<Sekvenser>();
		metaSekvenser = new ArrayList<SekvensMeta>();
		snper = new ArrayList<Punkter>();
		snpMeta = new ArrayList<PunkterMeta>();
		analyser = new ArrayList<Analyse>();
		analyseSeg = new ArrayList<AnalyseSeg>();
		sekvensDiff = new ArrayList<SekvensDiff>();
		segmenter = new ArrayList<Segement>();
		segmentMetaer = new ArrayList<SegmentMeta>();
		segmentTyper = new ArrayList<SegmentType>();
		sources = new ArrayList<Source>();
		gener = new ArrayList<Gen>();
		segment_descs = new ArrayList<SegmentDesc>();
		alleler = new ArrayList<Allele>();
		setsDiffer = new ArrayList<SetsDiff>();
		//setser = new ArrayList<Sets>();
		stprofiler = new ArrayList<StProfile>();
		ccprofiler = new ArrayList<CcProfile>();
		profiles = new ArrayList<Profiles>();
		getCoords();
		leggTilGaarder();
		addGenes();
		leggTilBrukere();
		leggTilSekvenser();
		//lagMetaForSekvenser();
		//leggTilFlereSekIMeta();
		lastInnMeta();
		analyseSegmenter(0, 1);
		analyseSegmenter(1, 2);
		analyseSegmenter(2, 0);
		analysePunkter();
		fyllProfiler();
		lagreMeta();

		//printLokasjoner();
		//printBrukere();
		//printSekvenser();
		//printMeta();
		//printSekDiff();
		//printPunkter();
		//printSegmenter();
		//printPunkterMeta();
		//printGen();
		//printDesc();
		//printSegmentTyper();
		//printAlleler();
		//printSets();
		//printSt();
		//printCc();
		//printSetsDiff();

		System.out.println("Lokajoner: " + lokasjoner.size());

		toFile();
	}

	/**
	Extracts all data from each class and converts it to SQL insertion code
	**/
	public void toFile(){
		try{
			PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
			for(Bruker b: brukere){
				writer.print("INSERT INTO contributor(firstname, lastname) ");
				writer.println("VALUES('" + b.navn + "', '" + b.navn + "');");
			}
			for(Lokasjon l: lokasjoner){
				writer.print("INSERT INTO location(latitude, longitude, location_name) ");
				writer.println("VALUES('" + l.latitude + "', '" + l.longitude + "', '" + l.gaard + "');");
			}
			for(Sekvenser s: sekvenser){
				writer.print("INSERT INTO sequence(seq, secondary_id) ");
				writer.println("VALUES('" + s.sekvens + "', '" + s.sekundær_id + "');");
			}
			for(Source sour: sources){
				writer.print("INSERT INTO source(source_name) ");
				writer.println("VALUES('" + sour.source_name + "');");
			}
			for(SekvensMeta sm: metaSekvenser){
				writer.print("INSERT INTO sequence_meta(source_id, location_id, contributor_id, date_sampled) ");
				writer.println("VALUES('" + sm.source.getId() + "', ' " + sm.lokasjon.getId() + "', '" + sm.contributor.getId() + "', '" + sm.date_created + "');");
			}
			for(SekvensDiff sd: sekvensDiff){
				writer.print("INSERT INTO sequence_diff(sequence_id, sequence_meta_id) ");
				writer.println("VALUES('" + sd.sekvens.getId() + "', '" + sd.meta.getId() + "');");
			}
			for(Analyse a: analyser){
				writer.print("INSERT INTO analysis(method, analysis_date) ");
				writer.println("VALUES('" + a.metode + "', '" + a.date_created + "');"); 
			}
			for(AnalyseSeg as: analyseSeg){
				writer.print("INSERT INTO analysis_sequences(analysis_id, sequence_diff_id) ");
				writer.println("VALUES('" + as.analyse.getId() + "', '" + as.sekDiff.getId() + "');");
			}
			for(Segement seg: segmenter){
				writer.print("INSERT INTO segment(start, stop, subsequence) ");
				writer.println("VALUES('" + seg.start + "', '" + seg.stop + "', '" + seg.subsekvens + "');");
			}
			for(PunkterMeta pm: snpMeta){
				writer.print("INSERT INTO point_meta(analysis_id, seq_diff_1, seq_diff_2) ");
				writer.println("VALUES('" + pm.analyse.getId() + "', '" + pm.sekvens[0].getId() + "', '" + pm.sekvens[1].getId() + "');");
			}
			for(Punkter p: snper){
				writer.print("INSERT INTO point(posision, point_meta_id, char1, char2) ");
				writer.println("VALUES('" + p.posisjon + "', '" + p.meta.getId() + "', '" + p.nuc1 + "', '" + p.nuc2 + "');");
			}
			for(SegmentMeta smeta: segmentMetaer){
				writer.print("INSERT INTO segment_meta(analysis_id) ");
				writer.println("VALUES('" + smeta.analyse.getId() + "');");
			}
			for(Gen g: gener){
				writer.print("INSERT INTO gen(gen_name, resistant_to) ");
				writer.println("VALUES('" + g.genName + "', '" + g.resistantTo + "');");
			}
			for(SegmentDesc desc: segment_descs){
				writer.print("INSERT INTO segment_description(gen_id, description) ");
				writer.println("VALUES('" + desc.gen.getId() + "', '" + desc.description + "');");
			}
			for(SegmentType st: segmentTyper){
				writer.print("INSERT INTO segment_type(segment_id, segment_meta_id, segment_description_id, sequence_diff_id) ");
				writer.println("VALUES('" + st.segment.getId() + "', '" + st.meta.getId() + "', '" + st.segmentDescription.getId() + "', '" + st.sekvens.getId() + "');");
			}
			for(Allele a: alleler){
				writer.print("INSERT INTO allele(gen_id, segment_type_id, date_created) ");
				writer.println("VALUES('" + a.gen.getId() + "', '" + a.segementType.getId() + "', '" + a.date_created + "');");
			}
			/*for(Sets sets: setser){
				writer.print("INSERT INTO sets(allele_id) ");
				writer.println("VALUES('" + sets.allele.getId() + "');");
			}*/
			/*for(StProfile stp: stprofiler){
				writer.println("INSERT INTO st_profile(number_of_genes) VALUES('" + stp.getAntallGener() + "');");
			}
			for(CcProfile ccp: ccprofiler){
				writer.println("INSERT INTO cc_profile(number_of_genes) VALUES('" + ccp.getAntallGener() + "');");
			}*/
			for(Profiles prof: profiles){
				writer.print("INSERT INTO profiles(profile_name, date_created) ");
				writer.println("VALUES('" + prof.profile_name + "', '" + prof.date_created + "');");
			}
			for(SetsDiff sdd: setsDiffer){
				writer.print("INSERT INTO sets_diff(allele_id, profile_id) ");
				writer.println("VALUES('" + sdd.allele.getId() + "', '" + sdd.profile.getId() + "');");
			}
			
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	Add coordinates to each location.
	coordinates.txt contains dummy values.
	**/
	public void getCoords(){
		try{
			Scanner inn = new Scanner(new File("coordinates.txt"));
			int coordsIndex = 0;
			while(inn.hasNext()){
				String[] lest = inn.nextLine().split("\t");
				double lat = Double.parseDouble(lest[0]);
				double lon = Double.parseDouble(lest[1]);
				lat = lat / 1000000;
				lon = lon / 1000000;
				coords[coordsIndex++] = lat;
				coords[coordsIndex++] = lon;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Kan ikke lese filen coords.txt.");
		}
	}

	/**
	Reads 'metasekvenser.txt' containing idenifiers from location, source and contributor.
	**/
	public void lastInnMeta(){
		try{
			Scanner inn = new Scanner(new File("metasekvenser.txt"));
			boolean leserMeta = true;
			inn.nextLine();
			Source kylling = new Source(source_id++, "Kylling");
			sources.add(kylling);
			while(inn.hasNext()){
				String[] lest = inn.nextLine().split(";");
				if(lest[0] != null){
					if(lest[0].equals("SekvensDiff")){
						leserMeta = false;
					}
					else if(leserMeta){
						int tmpBrukerId = Integer.parseInt(lest[1])-1;
						int tmpLokId = Integer.parseInt(lest[2])-1;
						int tmpSourceId = Integer.parseInt(lest[3])-1;
						Bruker tmpB = brukere.get(tmpBrukerId);
						Lokasjon tmpL = lokasjoner.get(tmpLokId);
						Source tmpS = sources.get(tmpSourceId);
						SekvensMeta sm = new SekvensMeta(sekvens_meta++, tmpB, tmpS, tmpL);
						metaSekvenser.add(sm);
					}else{
						int tmpSekvensId = Integer.parseInt(lest[1]) - 1;
						int tmpMetaId = Integer.parseInt(lest[2]) - 1;
						Sekvenser tmpSek = sekvenser.get(tmpSekvensId);
						SekvensMeta tmpSM = metaSekvenser.get(tmpMetaId);
						SekvensDiff sd = new SekvensDiff(segDiff++, tmpSek, tmpSM);
						sekvensDiff.add(sd);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	Stores metadata after it has been generated
	**/
	public void lagreMeta(){
		try{
			PrintWriter writer = new PrintWriter("metasekvenser.txt", "UTF-8");
			writer.println("SekvensMeta");
			for(SekvensMeta sm: metaSekvenser){
				writer.println(sm.getId() + ";" + sm.contributor.getId() + ";" + sm.lokasjon.getId() + ";" + sm.source.getId());
			}
			writer.println("SekvensDiff");
			for(SekvensDiff sd: sekvensDiff){
				writer.println(sd.getId() + ";" + sd.sekvens.getId() + ";" + sd.meta.getId());
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Kan ikke lagre metasekvenser.");
		}
	}

	/**
	Generates the different profiles and sets
	**/
	public void fyllProfiler(){
		int tmpGenId = 0;
		for(SegmentType st: segmentTyper){
			Allele a = new Allele(allele_id++, gener.get(tmpGenId++), st);
			alleler.add(a);
			if(tmpGenId > gener.size()){
				tmpGenId = 0;
			}
			Sets tmpSet = new Sets(sets_id++, a);
			//setser.add(tmpSet);
			Profiles prof = new Profiles(profile_id++, "ST");
			Profiles profCC = new Profiles(profile_id++, "CC");
			profiles.add(prof);
			profiles.add(profCC);
			//SetsDiff sdiff = new SetsDiff(tmpSet, stprof, ccprof);
			SetsDiff sdiff = new SetsDiff(a, prof);
			setsDiffer.add(sdiff);
		}
	}

	/**
	Prints all sets_diff
	**/
	public void printSetsDiff(){
		for(SetsDiff sd: setsDiffer){
			sd.print();
		}
	}

	/**
	Prints all ST-profiles
	Not used
	**/
	public void printSt(){
		for(StProfile st: stprofiler){
			st.print();
		}
	}

	/**
	Prints all CC-profiles
	not used
	**/
	public void printCc(){
		for(CcProfile cc: ccprofiler){
			cc.print();
		}
	}

	/**
	Prints all allele values
	**/
	public void printAlleler(){
		for(Allele a: alleler){
			a.print();
		}
	}

	/**
	Prints all profiles
	**/
	public void printProfiles(){
		for(Profiles p: profiles){
			p.print();
		}
	}

	/*public void printSets(){
		for(Sets s: setser){
			s.print();
		}
	}*/

	/**
	Prints all segment types
	**/
	public void printSegmentTyper(){
		for(SegmentType st: segmentTyper){
			st.print();
			System.out.println("");
		}
	}

	/**
	Adds genes to the system.
	**/
	public void addGenes(){
		try{
			Scanner inn = new Scanner(new File("bakterier_gen.txt"));
			while(inn.hasNext()){
				String navn = inn.nextLine();
				Gen gen = new Gen(gen_id++, navn, "something");
				gener.add(gen);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	Prints all point meta instances
	**/
	public void printPunkterMeta(){
		for(PunkterMeta pm: snpMeta){
			pm.print();
			System.out.println("");
		}
	}

	/**
	Simple pairwise aligner that generates segment, analysis and analyse_seg instances
	segment == multiple chars that match
	**/
	public void analyseSegmenter(int s1id, int s2id){
		SekvensDiff s1 = sekvensDiff.get(s1id);
		SekvensDiff s2 = sekvensDiff.get(s2id);
		Analyse a = new Analyse(analyse_id++);
		analyser.add(a);
		AnalyseSeg a1 = new AnalyseSeg(ana_seg_id++, a, s1);
		AnalyseSeg a2 = new AnalyseSeg(ana_seg_id++, a, s2);
		analyseSeg.add(a1);
		analyseSeg.add(a2);
		int start = 0, stop = 0;
		String t1 = s1.sekvens.sekvens;//Selve sekvensen
		String t2 = s2.sekvens.sekvens;//Selve sekvensen
		String sub = "";
		for(int i = 0; i < t1.length(); i++){
			if(t1.charAt(i) != t2.charAt(i) && start == stop){
				stop = i;
				sub += t1.charAt(i);
			}else if(t1.charAt(i) != t2.charAt(i) && start != stop){
				start = i;
				stop = i;
				sub += t1.charAt(i);
			}else{
				if(start == stop){
					sub = "";
					start = 0;
					stop = 0;
				}else if(sub.length() == 1){
					sub = "";
					start = 0;
					stop = 0;
				}else{
					if(!sub.equals("")){
						Segement segment = new Segement(segment_id++, sub, null, start, stop+1);
						SegmentMeta segment_meta = new SegmentMeta(segmentMeta_id++, a, null, s1);
						SegmentDesc segment_desc = new SegmentDesc(segment_desc_id++, "TESTTEST", gener.get(0));
						segment_descs.add(segment_desc);
						SegmentType segType = new SegmentType(segmentType_id++, segment, segment_meta, segment_desc, s1);
						segment.segmentType = segType;
						segment_meta.segmentType = segType;
						segmenter.add(segment);
						segmentMetaer.add(segment_meta);
						segmentTyper.add(segType);
						sub = "";
					}
				}
			}
		}
	}

	/**
	Prints all genes
	**/
	public void printGen(){
		for(Gen g: gener){
			g.print();
		}
	}

	/**
	Prints all segment descriptions
	**/
	public void printDesc(){
		for(SegmentDesc sd: segment_descs){
			sd.print();
		}
	}

	/**
	Prints all segments
	**/
	public void printSegmenter(){
		if(segmentTyper.size() != 0){
			for(SegmentType s: segmentTyper){
				s.print();
				System.out.println("");
			}
		}
	}

	/**
	Prints all sequence diff
	**/
	public void printSekDiff(){
		for(SekvensDiff sd : sekvensDiff){
			sd.print();
		}
	}

	/**
	Simple pairwise aligner for finding SNPs
	**/
	public void analysePunkter(){
		SekvensDiff s1 = sekvensDiff.get(0);
		SekvensDiff s2 = sekvensDiff.get(1);
		Analyse a = new Analyse(analyse_id++);
		analyser.add(a);
		AnalyseSeg analyse1 = new AnalyseSeg(ana_seg_id++, a, s1);
		AnalyseSeg analyse2 = new AnalyseSeg(ana_seg_id++, a, s2);
		analyseSeg.add(analyse1);
		analyseSeg.add(analyse2);
		for(int i = 0; i < s1.sekvens.sekvens.length(); i++){
			if(s1.sekvens.sekvens.charAt(i) != s2.sekvens.sekvens.charAt(i)){
				// SNP funnet. Ikke helt riktig metode altså...
				PunkterMeta pm = new PunkterMeta(punkter_meta++, a, s1, s2);
				Punkter p = new Punkter(snp_id++, s1.sekvens.sekvens.charAt(i), s2.sekvens.sekvens.charAt(i), i, pm);
				snper.add(p);
				snpMeta.add(pm);
			}
		}
	}

	/**
	Prints all SNPs
	**/
	public void printPunkter(){
		if(snper.size() != 0){
			for(Punkter p: snper){
				p.print();
			}
		}
	}

	/**
	Prints all sequence meta
	**/
	public void printMeta(){
		for(SekvensMeta sm: metaSekvenser){
			sm.print();
			System.out.println(" ");
		}
	}

	/**
	Generates metadata for the first 50% sequences.
	Should only be used in case of 'metasekvenser.txt' is corrupt/deleted
	**/
	public void lagMetaForSekvenser(){
		Source kylling = new Source(source_id++, "Kylling");
		sources.add(kylling);
		for(int i = 0; i < sekvenser.size()/2; i++){
			SekvensMeta sm = new SekvensMeta(sekvens_meta++, getRandomBruker(), kylling, getRandomLokasjon());
			Sekvenser s = sekvenser.get(i);
			SekvensDiff sd = new SekvensDiff(segDiff++, s, sm);
			sm.addSekvensTilMeta(sd);
			metaSekvenser.add(sm);
			sekvensDiff.add(sd);
		}
	}

	/**
	Genetares metadata for the last 50% sequences. Makes it possible to have one instance of metadata to be related to multiple instances of sequences
	Should only be used in case of 'metasekvenser.txt' is corrupt/deleted
	**/
	public void leggTilFlereSekIMeta(){
		for(int i = sekvenser.size()/2; i < sekvenser.size(); i++){
			SekvensMeta sm = getRandomMeta();
			Sekvenser s = sekvenser.get(i);
			SekvensDiff sd = new SekvensDiff(segDiff++, s, sm);
			sm.addSekvensTilMeta(sd);
			sekvensDiff.add(sd);
		}
	}

	/**
	Returns a random sequence meta object
	**/
	public SekvensMeta getRandomMeta(){
		Random r = new Random();
		int min = 0, max = metaSekvenser.size();
		return metaSekvenser.get(r.nextInt(max - min) + min);
	}

	/**
	Returns a random contributor object
	**/
	public Bruker getRandomBruker(){
		Random r = new Random();
		int min = 0, max = brukere.size();
		return brukere.get(r.nextInt(max - min) + min);
	}

	/**
	Returns a random location object
	**/
	public Lokasjon getRandomLokasjon(){
		Random r = new Random();
		int min = 0, max = lokasjoner.size();
		return lokasjoner.get(r.nextInt(max - min) + min);
	}

	/**
	Adds dummy sequences from 'sekvenser.txt' and generates new sequence objects
	**/
	public void leggTilSekvenser(){
		try{
			Scanner inn = new Scanner(new File("sekvenser.txt"));
			while(inn.hasNext()){
				String linje = inn.nextLine();
				if(linje.length() != 0){
					if(linje.charAt(0) == '>'){
						String sek = inn.nextLine();
						sek += inn.nextLine();
						Sekvenser s = new Sekvenser(sekvens_id++, sek);
						sekvenser.add(s);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Kan ikke legge til sekvenser");
		}
	}

	/**
	Prints all sequences
	**/
	public void printSekvenser(){
		for(Sekvenser s: sekvenser){
			s.print();
		}
	}

	/**
	Adds farm names from 'gaarder.txt' and generates new location objects
	**/
	public void leggTilGaarder(){
		try{
			Scanner inn = new Scanner(new File("gaarder.txt"));
			int i = 0;
			while(inn.hasNext()){
				String[] input = inn.nextLine().split(" ");
				Lokasjon l = new Lokasjon(lokasjon_id++, input[1], coords[i++], coords[i++]);
				lokasjoner.add(l);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Kan ikke lese filen gaarder.txt");
		}
	}

	/**
	Adds contributors from 'brukere.txt' and generates new contributor objects
	**/
	public void leggTilBrukere(){
		try{
			Scanner inn = new Scanner(new File("brukere.txt"));
			while(inn.hasNext()){
				String navn = inn.nextLine();
				Bruker b = new Bruker(bruker_id++, navn);
				brukere.add(b);
			}

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Kan ikke lese inn brukerene.");
		}
	}

	/**
	Prints all locations
	**/
	public void printLokasjoner(){
		for(Lokasjon l : lokasjoner){
			l.print();
		}
	}

	/**
	Prints all contributors
	**/
	public void printBrukere(){
		for(Bruker b: brukere){
			b.print();
		}
	}

	public static void main(String[] args){
		new StrainTracer();
	}
}
