package functionality;

import java.util.HashSet;
import java.util.Set;

public class VzCetnost implements Vzdalenost {
	


	@Override
	public double vzdalenost(TextRep prvni, TextRep druhy) {
		int cetnost1 = 0;
		int cetnost2 = 0;
		double vzdalenost = 0;
		int abs = 0;
		Set<String> slova = new HashSet<String>();
		slova.addAll(prvni.getCetnost().keySet());
		slova.addAll(druhy.getCetnost().keySet());
		
		for (String key : slova) {
			cetnost1 = 0;
			cetnost2 = 0;
			if (prvni.getCetnost().containsKey(key)) {
				cetnost1 = prvni.getCetnost().get(key);
			}
			if (druhy.getCetnost().containsKey(key)) {
				cetnost2 = druhy.getCetnost().get(key);
			}

			abs = cetnost1 - cetnost2;
			vzdalenost += abs * abs;
		}

		return Math.sqrt(vzdalenost);
	}

	@Override
	public int pravdepodobnost(Categorie categorie, String k) {
		return categorie.getCetnost().get(k);
	}
}
