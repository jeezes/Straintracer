import java.sql.*;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

import Input_data.*;
import Allele_data.*;
import Sequence_data.*;
import Analysis_data.*;
import Segment_data.*;
import SNP_data.*;
import MLST_data.*;

class PsqlWriter{
	Connection c = null;
	Statement stmt = null;
	
	/**
	Constructor that connects with the db
	PS: ?sslmode=required is needed
	**/
	PsqlWriter(){
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://dbpg-ifi-utv.uio.no:5432/bakt_infeksjoner?sslmode=require", "bakt_infeksjoner_user", "732b6187");
			stmt = c.createStatement();
			
			//getLocations();
			//getSegments();
			//getContributors();
			//printProfiles();
			//printSequences();
			//printAnalysis(); // and results from each
			//addGenesAndAlleles();
			printProfiles();
			//removeTables();
			//addTables();
			//populateDB();
		
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	Removes all tables from the DB. Sql code is found in removeTables.sql
	Added tables should have a drop code in this file.
	**/
	public void removeTables(){
		try{
			Scanner les = new Scanner(new File("removeTables.sql"));
			String sql = "";
			while(les.hasNext()){
				sql = les.nextLine();
				stmt.executeUpdate(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Prints all analysis and segments/points assosiated with each
	**/
	public void printAnalysis(){
		try{
			String sql = "select analysis_id, method, analysis_date from analysis;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("analysis_id");
				String method = rs.getString("method");
				String date = rs.getString("analysis_date");
				System.out.println("ANALYSIS ID: " + id + "\tmethod: " + method + "\tDate: " + date);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	Adds all tables, relations and foreign keys to a database
	The SQL script for the db can be found at Straintraces.sql
	**/
	public void addTables() throws Exception{
		int lineOn = 0;
		String lest = "";
		String query = "";
		try{
			Scanner inn = new Scanner(new File("StrainTracer.sql"));
			
			while(inn.hasNext()){
				lest = inn.nextLine();
				lineOn++;
				if(lest.startsWith("ALTER")){
					stmt.executeUpdate(lest);
					query = "";
				}else if(lest.startsWith("CREATE")){
					query += lest;
				}else if(lest.startsWith(");")){
					query += lest;
					stmt.executeUpdate(query);
					query = "";
				}else if(!lest.startsWith("#")){
					query += lest;
				}
			}
		}catch (Exception e) {
			System.out.println("Error at line: " + lineOn + " in StrainTracer.sql. \n" + query);
			e.printStackTrace();
		}
	}
	
	/**
	Returns all profiles with allele_ids 
	**/
	public String getProfilesAndWrite(){
		try{
			String output = "ST";
			ArrayList<Gen> genes = getAllGenes();
			for(Gen g: genes){
				output += "\t" + g.getName();
			}
			output += "\tclonal_complex";
			Profiles[] profiles = getAllProfiles();
			for(int i = 0; i < profiles.length; i++){
				output += "\n" + profiles[i].getId();
				output += getAlleleWithProfileId(profiles[i].getId());
				output += "\t" + profiles[i].getName();
			}
			return output;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public String getAllelesAndGenes() throws Exception{
		ArrayList<Gen> genes = getAllGenes();
		String output = "";
		for(Gen g: genes){
			String sql = "select a.allele_id, se.subsequence from allele a inner join segment_type st on a.segment_type_id = st.segment_type_id join segment se on se.segment_id = st.segment_id join segment_description sd on sd.segment_description_id = st.segment_description_id join gen g on sd.gen_id = g.gen_id where g.gen_name = '" + g.getName() + "';";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("allele_id");
				String sequence = rs.getString("subsequence");
				output += ">" + g.getName() + "_" + id + "\n" + sequence + "\n";
			}
		}
		return output;
		
	}
	
	public String getAlleleWithProfileId(int id) throws Exception{
		String sql = "select s.profile_id, g.gen_name, a.allele_id, a.secondary_id from allele a inner join segment_type st on st.segment_type_id = a.segment_type_id join segment_description sd on sd.segment_description_id = st.segment_description_id join gen g on g.gen_id = sd.gen_id join sets_diff s on s.allele_id = a.allele_id where s.profile_id = '" + id + "' order by g.gen_name;";
		ResultSet rs = stmt.executeQuery(sql);
		String output = "";
		
		while(rs.next()){
			//int profile_id = rs.getInt("s.profile_id");
			String gen = rs.getString("gen_name");
			int allele = rs.getInt("allele_id");
			int secId = rs.getInt("secondary_id");
			output += "\t" + allele;
		}
		return output;
	
	}
	
	public Profiles[] getAllProfiles() throws Exception{
		Profiles[] profiles = null;
		String sql = "select last_value from profiles_profile_id_seq";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		profiles = new Profiles[rs.getInt("last_value")];
		sql = "select profile_id, complex from profiles";
		rs = stmt.executeQuery(sql);
		int index = 0;
		while(rs.next()){
			int id = rs.getInt("profile_id");
			String complex = rs.getString("complex");
			profiles[index++] = new Profiles(id, complex);
		}
		return profiles;
	}

	public ArrayList<Gen> getAllGenes() throws Exception{
		ArrayList<Gen> genes = new ArrayList<Gen>();
		String sql = "select gen_id, gen_name from gen";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("gen_id");
			String name = rs.getString("gen_name");
			genes.add(new Gen(id, name, "nah"));
		}
		return genes;
	}
	
	public void addSequenceFromInput(int c_id, int s_id, int l_id, int i_id, String sequence_path, String rightReport, String leftReport, String assmeblyReport){
		try{
			String sql = "insert into insert_sequence(contributor_id, source_id, location_id, isolate_id, sequence_path, right_adapt_report, left_adapt_report, assembly_report values('" + c_id + "', '" + s_id + "', '" + l_id + "', '" + i_id + "', '" + sequence_path + "', '" + rightReport + "', '" + leftReport + "', '" + assmeblyReport + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println("FATAL ERROR: Sequences was not get added to the db.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printAllIsolates(){
		try{
			String sql = "select isolate_id, file_path from isolates;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("isolate_id");
				String path = rs.getString("file_path");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addInputFromUser(String inputfile){
		try{
			Scanner reader = new Scanner(new File(inputfile));
			while(reader.hasNext()){
				String[] inn = reader.nextLine().split(";");
				// 0 = firstname, 1 = lastname, 2 = latitude, 3 = longitude, 4 = location name, 5 = source, 6 = sample file location
				String sql = ""; // copy from DB. insert into view with rule
				//move files to right folder
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStProfile(String[] genes, String[] input, String importedFrom){
		try{
			String secId = input[0];
			String complex = "";
			if(input.length == 9)
				complex = input[input.length-1];
				
			String sql = "insert into profiles(profile_id, complex, secondary_id, date_created) values(default, '" + complex + "', '" + secId + "', CURRENT_TIMESTAMP) returning profile_id;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int id = rs.getInt("profile_id");
			Profiles tmp = new Profiles(id, complex);
			for(int i = 0; i < genes.length; i++){
				//Defines sets and alleles that contains in a profile.
				
				sql = "insert into sets_diff(allele_id, profile_id) values((select a.allele_id from allele a, segment_type st, segment_description sd, gen g where secondary_id = '" + input[i+1] + "' and g.gen_name = '" + genes[i] + "' and a.segment_type_id = st.segment_type_id and st.segment_description_id = sd.segment_description_id and sd.gen_id = g.gen_id and a.importedFrom = '" + importedFrom + "'), '" + id + "');";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addGenesAndAlleles(String alleleFile){
		try{
			Scanner reader = new Scanner(new File(alleleFile));
			String name = "";
			String seq = "";
			Gen gen = null;
			while(reader.hasNext()){
				String line = reader.nextLine();
				if(line.charAt(0) == '>'){
					if(!seq.equals("")){
						gen = addGene(name, seq, "", -1);
					}
					name = line.split("_")[0];
					int secondary_id = Integer.parseInt(line.split("_")[1]);
					name = name.substring(1, name.length());
					seq = "";
				}else{
					seq += reader.nextLine();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Test om dette fungerer
	public Gen addGene(String name, String seq, String importedFrom, int secId){
		int gen_id = 0;
		Gen newGen = null;
		try{
			//Add all alleles as segments and link with gen first
			Segment segment = addSegmentReturnId(seq, 0, seq.length()-1);
			SegmentMeta meta = addSegmentMetaReturnId(importedFrom);
			newGen = getGen(name);
			if(newGen == null){
				String sql = "insert into gen(gen_id, gen_name, resistant_to) values(default, '" + name + "', 'something') returning gen_id;";
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				gen_id = rs.getInt("gen_id");
				newGen = new Gen(gen_id, name, "something");
			}
			SegmentDesc desc = addSegmentDescriptionReturnId(newGen, "Allele");
			SegmentType type = addSegmentTypeReturnId(segment, meta, desc);
			Allele allele = addAlleleReturnId(type, secId, importedFrom);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return newGen;
	}
	
	public Allele addAlleleReturnId(SegmentType t, int secId, String importedFrom) throws Exception{
		String sql = "insert into allele(allele_id, segment_type_id, secondary_id, importedFrom, date_created) values(default, '" + t.getId() + "', '" + secId + "', '" + importedFrom + "', CURRENT_TIMESTAMP) returning allele_id;";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int id = rs.getInt("allele_id");
		return new Allele(id, t);
	}
	
	public SegmentType addSegmentTypeReturnId(Segment s, SegmentMeta m, SegmentDesc d) throws Exception{
		String sql = "insert into segment_type(segment_type_id, segment_id, segment_meta_id, segment_description_id) values(default, '" + s.getId() + "', '" + m.getId() + "', '" + d.getId() + "') returning segment_type_id;";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int id = rs.getInt("segment_type_id");
		return new SegmentType(id, s, m, d);
	}
	
	public SegmentDesc getSegmentDescription(Gen g, String desc) throws Exception{
		String sql = "select segment_description_id from segment_description where gen_id = '" + g.getId() + "' and description = '" + desc + "';";
		ResultSet rs = stmt.executeQuery(sql);
		int id = 0;
		while(rs.next()){
			id = rs.getInt("segment_description_id");
		}
		if(id == 0)
			return null;
		SegmentDesc tmp = new SegmentDesc(id, desc);
		tmp.setGen(g);
		return tmp;
	}
	
	public SegmentDesc addSegmentDescriptionReturnId(Gen g, String description) throws Exception{
		SegmentDesc tmp = getSegmentDescription(g, description);
		if(tmp != null)
			return tmp;
		String sql = "insert into segment_description(segment_description_id, gen_id, description) values(default, '" + g.getId() + "', '" + description + "') returning segment_description_id;";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int id = rs.getInt("segment_description_id");
		tmp = new SegmentDesc(id, description);
		tmp.setGen(g);
		return tmp;
	}
	
	public SegmentMeta addSegmentMetaReturnId(String importedFrom) throws Exception{
		SegmentMeta tmp = getSegmentMeta(importedFrom);
		if(tmp != null)
			return tmp;
		String sql = "insert into segment_meta(segment_meta_id, importData, importedFrom) values(default, 'yes', '" + importedFrom + "') returning segment_meta_id;";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int segment_meta_id = rs.getInt("segment_meta_id");
		return new SegmentMeta(segment_meta_id, null, null, null);
	}
	
	public SegmentMeta getSegmentMeta(String importedFrom) throws Exception{
		String sql = "select segment_meta_id from segment_meta where importedFrom = '" + importedFrom + "';";
		ResultSet rs = stmt.executeQuery(sql);
		int id = 0;
		while(rs.next()){
			id = rs.getInt("segment_meta_id");
		}
		if(id == 0)
			return null;
		return new SegmentMeta(id, null, null, null);
	}
	
	public Segment addSegmentReturnId(String seq, int start, int stop){
		Segment tmp = null;
		try{
			String sql = "insert into segment(segment_id, start, stop, subsequence) values(default, '" + start + "', '" + stop + "', '" + seq + "') returning segment_id;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int segment_id = rs.getInt("segment_id");
			tmp = new Segment(segment_id, seq, null, start, stop);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(tmp == null){
			System.out.println("Could not add segment");
		}
		return tmp;
	}
	
	public Gen getGen(String name){
		Gen gen = null;
		try{
			String sql = "select gen_id, gen_name from gen where gen_name = '" + name + "';";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("gen_id");
				String gen_name = rs.getString("gen_name");
				gen = new Gen(id, gen_name, "something");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return gen;
	}
	
	public void addContributor(Bruker bruker){
		try{
			String sql = "INSERT INTO contributor(firstname, lastname) values('" + bruker.getFirstName() + "', '" + bruker.getLastName() + "');";
			stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int addSource(Source source){
		int i = -1;
		try{
			String sql = "insert into source(source_name) values('" + source.getName() + "');";
			i = stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return i;
	}

	public void addIsolate(Isolate isolate){
		try{
			String sql = "insert into isolate(file_path) values('" + isolate.getPath() + "');";
			stmt.executeUpdate(sql);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPoint(Punkter p){
		try{
			String sql = "insert into point(posision, point_meta_id, char1, char2) values('" + p.getPosition() + "', '" + p.getMeta().getId() + "', '" + p.getNuc1() + "', '" + p.getNuc2() + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println(p.getId() + " did not get added to the db.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPointMeta(PunkterMeta p){
		try{
			String sql = "insert into point_meta(analysis_id, seq_diff1, seq_diff2) values('" + p.getAnalyse().getId() + "', '" + p.getSequence1() + "', '" + p.getSequence2() + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println(p.getId() + " was not stored in the db.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	Returns the last key for a table in the db.
	Useful for knowing the primary key of last entry in a table.
	@param table: The table name from the table we want to extract the last primary key from.
	@return int: the value of the last primary key, else -1 if something went wrong or table name did not exist.
	**/
	public int getLastKey(String table){
		try{
			String sql = "select last_value from " + table + "_" + table + "_id_seq;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				return rs.getInt("last_value");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	public void addSequence(String file){
		try{
			//System.out.println(file);
			String sql = "insert into sequence(seq, secondary_id) values('" + file + "', '10')";
			stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addSegment(Segment s){
		try{
			String sql = "insert into segment(start, stop, subsequence) values('" + s.getStart() + "', '" + s.getStop() + "', '" + s.getSequence() + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println(s.getId() + " was not added to the db. Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addSegmentMeta(SegmentMeta sm){
		try{
			String sql = "insert into segment_meta(analysis_id, sequence_diff_id) values('" + sm.getAnalyse().getId() + "', '" + sm.getType().getId() + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println(sm.getId() + " was not stored in the DB: Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addSegmentType(SegmentType st){
		try{
			String sql = "insert into segment_type(segment_id, segment_meta_id, segment_description_id) values('" + st.getSegment().getId() + "', '" + st.getMeta().getId() + "', '" + st.getDesc().getId() + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println(st.getId() + " was not stored in the DB: Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addSegmentDescription(SegmentDesc sd){
		try{
			String sql = "insert into segment_description(gen_id, description) values('" + sd.getGen().getId() + "', '" + sd.getDesc() + "');";
			int i = stmt.executeUpdate(sql);
			if(i != 1) System.out.println(sd.getId() + " was not stored in the DB: Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAllele(Allele a){
		try{
			String sql = "insert into allele(segment_type_id, date_created) values('" + a.getType().getId() + "', '" + a.getDate() + "');";
			int i = stmt.executeUpdate(sql);
			if(i != 1) System.out.println(a.getId() + " was not added to the DB. Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addProfiles(Profiles p){
		try{
			String sql = "insert into profiles(profile_name, date_created) values('" + p.getName() + "', '" + p.getDate() + "');";
			int i = stmt.executeUpdate(sql);
			if(i != 1) System.out.println(p.getId() + " was not added to the DB. Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addSets(SetsDiff s){
		try{
			String sql = "insert into sets_diff(allele_id, profile_id) values('" + s.getAllele().getId() + "', '" + s.getProfile().getId() + "');";
			int i = stmt.executeUpdate(sql);
			if(i != 1) System.out.println("Sets with AlleleID: " + s.getAllele().getId() + " and profileId: " + s.getProfile().getId() + " was not added to the DB. Error code: " + i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addAnalysis(Analyse a){
		try{
			String sql = "insert into analysis(method, analysis_date) values('" + a.getMethod() + "', '" + a.getDate() + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println("Analysis " + a.getId() + " was not able to store in the db");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addAnalysisSeg(AnalyseSeg a, SekvensDiff s){
		try{
			//String sql = "insert into analysis_sequences(analysis_id, sequence_diff_id) values('" + a.getId + "', '" + s.getId() + "');";
			String sql = "insert into analysis_sequences(analysis_id, sequence_diff_id) values('" + a.getId() + "', '" + 1 + "');";
			int i = stmt.executeUpdate(sql);
			if(i < 1) System.out.println("Analysis_sequences " + a.getId() + " was not able to store in the db");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addLocation(Lokasjon l){
		try{
			String sql = "insert into location(latitude, longitude, location_name) values('" + l.getLatitude() + "', '" + l.getLongitude() + "', '" + l.getGaard() + "');";
			stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	Populates the table with dummy data from 'the-file-name.txt'
	**/
	public void populateDB() throws Exception{
		try{
			Scanner inn = new Scanner(new File("the-file-name.txt"));
			String lest = "";
			while(inn.hasNext()){
				lest = inn.nextLine();
				stmt.executeUpdate(lest);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	Prints all locations stored in the db
	**/
	public void printLocations() throws Exception{
		String sql = "select location_id, source_name from source;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("location_id");
			String name = rs.getString("location_name");	
			System.out.println("id: " + id + "\tName: " + name);
		}
	}
	
	public ArrayList<Lokasjon> getLocations(){
		try{
			ArrayList<Lokasjon> locations = new ArrayList<Lokasjon>();
			String sql = "Select location_id, location_name, latitude, longitude from location;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("location_id");
				String name = rs.getString("location_name");
				double latitude = rs.getDouble("latitude");
				double longitude = rs.getDouble("longitude");
				locations.add(new Lokasjon(id, name, latitude, longitude));
			}
			return locations;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	Prints all contributors in the db
	**/
	public void printContributors() throws Exception{
		String sql = "select contributor_id, firstname, lastname from contributor;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("contributor_id");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			System.out.println("ID: " + id + "\tFirst name: " + firstname + "\tLast name: " + lastname);
		}
	}

	public ArrayList<Bruker> getContributors(){
		try{
			ArrayList<Bruker> brukere = new ArrayList<Bruker>();
			String sql = "select contributor_id, firstname, lastname from contributor;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("contributor_id");
				String firstname = rs.getString("firstname");
				String lastname = rs.getString("lastname");
				brukere.add(new Bruker(id, firstname, lastname));
			}
			return brukere;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	Prints all segments stored in the db
	**/
	public void getSegments() throws Exception{
		String sql = "select segment_id, start, stop, subsequence from segment;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("segment_id");
			int start = rs.getInt("start");
			int stop = rs.getInt("stop");
			String sub = rs.getString("subsequence");
			System.out.println("ID: " + id + "\tStart: " + start + "\tStop " + stop + "\nSubsequence: " + sub + "\n");
		}
	}

	/**
	Gets all unassemblyed sequences from the db
	**/
	public ArrayList<Sekvenser> getSequences(){
		ArrayList<Sekvenser> sequences = new ArrayList<Sekvenser>();
		try{
			String sql = "select sequence_id, seq, secondary_id from sequence;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				int id = rs.getInt("sequence_id");
				String file = rs.getString("seq");
				int sec = rs.getInt("secondary_id");
				Sekvenser sek = new Sekvenser(id, file, sec);
				sequences.add(sek);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sequences;
	}
	
	
	/**
	Prints all sources in the db
	**/
	public void printSources() throws Exception{
		String sql = "select source_id, source_name from source;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("source_id");
			String name = rs.getString("source_name");
			System.out.println("ID: " + id + "\tSource name: " + name);
		}
	}
	
	/**
	Gets all sources from the db
	**/
	public ArrayList<Source> getSources() throws Exception{
		ArrayList<Source> sources = new ArrayList<Source>();
		String sql = "select source_id, source_name from source;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("source_id");
			String name = rs.getString("source_name");
			sources.add(new Source(id, name));
		}
		return sources;
	}

	/**
	Prints all isolates
	**/
	public void printSequences() throws Exception{
		String sql = "select sequence_id, seq, secondary_id from sequence;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int id = rs.getInt("sequence_id");
			String text = rs.getString("seq");
			int sec_id = rs.getInt("secondary_id");
			System.out.println("ID: " + id + "\tSecondary id: " + sec_id + "\nSequence: " + text);
		}
	}

	/**
	Prints all sequence meta
	**/
	public void printSequenceMeta() throws Exception{
		String sql = "select sequence_meta_id, source_id, location_id, contributor_id, data_sampled from sequence_meta;";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int meta_id = rs.getInt("sequence_meta_id");
			int sou_id = rs.getInt("source_id");
			int loc_id = rs.getInt("location_id");
			int con_id = rs.getInt("contributor_id");
			String date = rs.getString("data_sampled");
			System.out.println("Meta id: " + meta_id + "\tsource id: " + sou_id + "\tlocation id: " + loc_id + "\tcontributor id: " + con_id + "\tDate: " + date);
		}
	}
	
	/**
	Prints all profiles with alleles and genes. 
	First try, just sayin...
	**/
	public void printProfiles() throws Exception{
		String sql = "select p.profile_id, sd.allele_id, g.gen_id from profiles p, sets_diff sd, gen g where sd.profile_id = p.profile_id and g.gen_id = (select a.gen_id from allele a where a.allele_id = sd.allele_id);";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			int pid = rs.getInt("profile_id");
			int aid = rs.getInt("allele_id");
			int gid = rs.getInt("gen_id");
			System.out.println("Profile id: " + pid + "\tAllele id: " + aid + "\tGen id: " + gid);
		}
	}

	public static void main(String[] args){
		new PsqlWriter();
	}

}
