import java.sql.*;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class AlleleGet{
	Connection c = null;
	Statement stmt = null;

	AlleleGet(){
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
			
			//removeTables();
			//addTables();
			//populateDB();
			getAlleleSecondId();
		
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
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

	public String getAlleleWithProfileId(int id) throws Exception{
		String sql = "select s.profile_id, g.gen_name, a.allele_id, a.secondary_id from allele a inner join segment_type st on st.segment_type_id = a.segment_type_id join segment_description sd on sd.segment_description_id = st.segment_description_id join gen g on g.gen_id = sd.gen_id join sets_diff s on s.allele_id = a.allele_id where s.profile_id = '" + id + "' order by g.gen_name;";
		ResultSet rs = stmt.executeQuery(sql);
		String output = "";
		
		while(rs.next()){
			//int profile_id = rs.getInt("s.profile_id");
			String gen = rs.getString("gen_name");
			int allele = rs.getInt("allele_id");
			int secId = rs.getInt("secondary_id");
			output += "\t" + secId;
		}
		return output;
	
	}

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
	}

	public static void main(String[] args){
		new AlleleGet();
	}
}