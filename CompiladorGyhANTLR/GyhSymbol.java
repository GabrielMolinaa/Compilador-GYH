
public  class GyhSymbol {

	private String name;
	public int type;
	private boolean utilizada;
	public static final int INT = 0;
	public static final int REAL = 1;
	
	public GyhSymbol(String name, String type) {
		this.name = name;
		
        if (type.equals("INT")) {
            this.type = INT;
        }else {
            this.type = REAL;
        }
    }
	
    public String getType() {
        // Retorna o tipo da variável
        if (type == 0)
            return "INT";
        else
        return "REAL";
    }
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
    public void setUtilizada() {
        this.utilizada = true;
    }

    public boolean foiUtilizada() {
        return utilizada;
    }
	
    @Override
    public String toString() {
        if (this.type == 0) {
            return ("Simbolo: (nome: " + name + ", tipo: INT)");
        } else {
            return ("Simbolo: (nome: " + name + ", tipo: REAL)");
        }
    }
    
    public String generateCode() {

    	if(type == INT ) {
    		return  "\tint " + name + ";\n";
    	}else {
    		return "\tdouble " + name + ";\n";
    	}
    	
    }
	
}
