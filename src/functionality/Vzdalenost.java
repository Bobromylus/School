package functionality;

import java.io.Serializable;

public interface Vzdalenost extends Serializable {
	
	public double vzdalenost(TextRep prvni, TextRep druhy);

	public int pravdepodobnost(Categorie categorie, String k);

}
