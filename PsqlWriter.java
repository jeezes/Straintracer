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
			
			
			//addTables();
			//populateDB();
		
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
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
		try{
			Scanner inn = new Scanner(new File("Straintraces.sql"));
			String query = "";
			while(inn.hasNext()){
				String lest = inn.nextLine();
				if(lest.startsWith("ALTER")){
					stmt.executeUpdate(lest);
					query = "";
				}else if(lest.startsWith("CREATE")){
					query += lest;
				}else if(lest.startsWith(");")){
					query += lest;
					stmt.executeUpdate(query);
					query = "";
				}else{
					query += lest;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGenesAndAlleles(){
		try{
			Scanner reader = new Scanner(new File("glnA.tfa"));
			String name = "";
			String seq = "";
			Gen gen = null;
			while(reader.hasNext()){
				String line = reader.nextLine();
				if(line.charAt(0) == '>'){
					if(!seq.equals("")){
						gen = addGene(name, seq);
					}
					name = line.split("_")[0];
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
	
	public Gen addGene(String name, String seq){
		Gen gen = getGen(name);
		if(gen == null){
			try{
				String sql = "insert into gen(gen_name, resistant_to) values('" + name + "', 'something');";
				stmt.executeUpdate(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return gen;
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
