
public enum Color {
	WHITE (Params.albedo_white),
	BLACK (Params.albedo_black);
	
	private final double albedo;
	
	Color(double albedo){ 
		this.albedo = albedo; 
		}
	
	private double albedo() { return albedo; }
	

}
