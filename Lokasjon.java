class Lokasjon{
	int lokasjons_id;
	double latitude, longitude;
	String gaard;

	Lokasjon(int lokasjons_id, String gaard, double lat, double lon){
		this.lokasjons_id = lokasjons_id;
		this.latitude = lat;
		this.longitude = lon;
		this.gaard = gaard;
	}

/*
	public double[] getCoord(){
		double[] coord = new double[2];
		coord[0] = latitude;
		coord[1] = longitude;
		return coord;
	}
	*/

	public void print(){
		System.out.println("lokasjons_id " + lokasjons_id + "\tGårdsnavn " + gaard + "\tLatitude " + latitude + "\tLongitude " + longitude);
	}
/*
	public double randomGen(){
		Random r = new Random();
		double tmp = 10.0 + (100 - 10) * r.nextDouble();
		DecimalFormat df = new DecimalFormat("#.######");
		String a = df.format(tmp);
		System.out.println("Length " + a.length());
        tmp = Double.parseDouble(a);
        if(a.length() != 9){
        	if(a.length() == 8)
        		tmp += 0.0000001;
        	if(a.length() == 7)
        		tmp += 0.000011;
        	if(a.length() == 6)
        		tmp += 0.000111;
        	if(a.length() == 5)
        		tmp += 0.001111;
        	if(a.length() == 4)
        		tmp += 0.011111;
        }
        return tmp;
	}
*/
	public int getId(){ return lokasjons_id; }

	public String getGaard(){ return gaard; }

	public boolean compare(String g){
		if(gaard.equalsIgnoreCase(g)) return true;
		return false;
	}
}