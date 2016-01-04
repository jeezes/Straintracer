import java.sql.*;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import Input_data.*;

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
			String sql = "select";

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
	
	public void addContributor(Bruker bruker){
		try{
			String sql = "INSERT INTO contributor(firstname, lastname) values('" + bruker.getFirstName() + "', '" + bruker.getLastName() + "');";
			stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addSource(Source source){
		try{
			String sql 
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
/*
	public static void main(String[] args){
		new PsqlWriter();
	}*/

}
