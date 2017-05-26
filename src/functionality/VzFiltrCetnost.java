package functionality;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VzFiltrCetnost implements Vzdalenost {

	Set<String> filtr = Stream.of("aby", "jakmile", "až", "než", "nežli", "zatímco", "když", "kdyby", "pokud",
			"protože", "poněvadž", "jelikož", "jestliže", "přestože", "ačkoli", "třebaže", "když", "ač", "že", "i", "a",
			"ani", "nebo", "ale", "avšak", "jak", "ba", "dokonce", "nebo", "bo", "anebo", "tudíž", "totiž", "neboť",
			"proto", "či", "však").collect(Collectors.toSet());

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
			if (!filtr.contains(key)) {
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

		}

		return Math.sqrt(vzdalenost);
	}

	@Override
	public int pravdepodobnost(Categorie categorie, String k) {
		if (filtr.contains(k)) {
			return 0;
		}
		return categorie.getCetnost().get(k);
	}
}
