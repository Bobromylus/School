package functionality;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextRep implements Serializable{
	/** Výraz na třídění slov */
	private final Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);

	private HashMap<String, Integer> cetnost = new HashMap<String, Integer>();
	private CzechStemmerAgressive urezavac = new CzechStemmerAgressive();
	private boolean spravne;
	private String nazev;
	private String categorie;
	private String urceni;

	public TextRep(Path file) {
		this.spravne = false;
		this.nazev = file.getFileName().toString();

		String[] cat = nazev.split(" |-|_|/|\\.");

		this.categorie = cat[1];
		cteni(file);
	}

	public TextRep(String vstup) {
		this.spravne = false;
		this.nazev = "TextArea";

		cteni(vstup);
	}

	private void cteni(Path file) {
		BufferedReader read;
		try {
			// cteni souboru
			read = new BufferedReader(new InputStreamReader(new FileInputStream(file.toString()), "UTF8"));
			String radek;
			while ((radek = read.readLine()) != null) {
				radek = radek.toLowerCase();
				// rozdeleni radku podle znaku
				Matcher matcher = p.matcher(radek);
				while (matcher.find()) {
					String match = urezavac.stem(matcher.group());
					if (cetnost.containsKey(match)) {
						cetnost.put(match, cetnost.get(match) + 1);
					} else {
						cetnost.put(match, 1);
					}
				}
			}
			read.close();
			// osetreni podminek
		} catch (IOException | UncheckedIOException e) {
			System.out.println("Nastala chyba při čtení souboru s daty.");
		}
	}
	
	private void cteni(String read) {
			// cteni vstupu
			String radek;
				radek = read.toLowerCase();
				// rozdeleni radku podle znaku
				Matcher matcher = p.matcher(radek);
				while (matcher.find()) {
					String match = urezavac.stem(matcher.group());
					if (cetnost.containsKey(match)) {
						cetnost.put(match, cetnost.get(match) + 1);
					} else {
						cetnost.put(match, 1);
					}
				}
	}

	
	public HashMap<String, Integer> getCetnost() {
		return cetnost;
	}

	public void setCetnost(HashMap<String, Integer> cetnost) {
		this.cetnost = cetnost;
	}

	public boolean isSpravne() {
		return spravne;
	}

	public String getNazev() {
		return nazev;
	}

	public String getCategorie() {
		return categorie;
	}

	public String getUrceni() {
		return urceni;
	}

	public void setSpravne(boolean spravne) {
		this.spravne = spravne;
	}

	public void setNazev(String nazev) {
		this.nazev = nazev;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public void setUrceni(String urceni) {
		this.urceni = urceni;
	}
	
	@Override
	public String toString() {
		return "Název souboru: " + nazev + "\nKlasifikator určil spravně: " + spravne  + "\nSoubor patří do kategorie: " + categorie
				+ "\nKategorie z klasifikátoru: " + urceni + "\n-----------------------------------------------------------\nSlova v souboru s četnosti: " + cetnost + "\n";
	}
}
