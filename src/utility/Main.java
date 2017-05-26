package utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import functionality.Bayes;
import functionality.Categorie;
import functionality.Klasifikator;
import functionality.NejblizsiSoused;
import functionality.Prepravka;
import functionality.PrepravkaSaveLoad;
import functionality.TextRep;
import functionality.VzBinarniCetnost;
import functionality.VzCetnost;
import functionality.VzFiltrCetnost;
import functionality.Vzdalenost;
import gui.Gui;
import javafx.collections.ObservableMap;

public class Main {
	static Gui startUpTest;
	private static Prepravka prepravka = new Prepravka();

	public static void main(String[] args) {
		if(args.length == 5){
			String cestaTren = args[0];
			String cestaTest = args[1];
			String param = args[2];
			String clasif = args[3];
			String nazev = args[4];
			
			Main.nacteni(prepravka.getTrenDataGui(), new File(cestaTren), 0);
			Main.nacteni(prepravka.getTestDataGui(), new File(cestaTest), 1);
			Vzdalenost v = new VzCetnost();
			if(param.equals("cetnost")){
				
			} else if(param.equals("bincetnost")){
				v = new VzBinarniCetnost();
			} else if (param.equals("filtrcetnost")){
				v = new VzFiltrCetnost();
			}else{
				System.out.println("Špatný parametr pro příznaky");
			}
			Klasifikator k = new NejblizsiSoused(prepravka, v);
			if(clasif.equals("nn")){
				prepravka.setSoused((NejblizsiSoused) k);
				prepravka.getSoused().klasifikuj();
				System.out.println("Přesnost je: "
						+ String.format("%.2f", prepravka.getSoused().getPresnost()) + " %\n");
			} else if(clasif.equals("bayes")){
				k = new Bayes(prepravka, v);
				prepravka.setBayes((Bayes) k);
				prepravka.getBayes().klasifikuj();
				System.out.println("Přesnost je: "
						+ String.format("%.2f", prepravka.getBayes().getPresnost()) + " %\n");
			}else{
				System.out.println("Špatný parametr pro klasifikator");
			}
			
			try {
				PrepravkaSaveLoad.save(new File(nazev), new PrepravkaSaveLoad(prepravka));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if(args.length == 1){
			try {
				prepravka = PrepravkaSaveLoad.load(args[0]);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		new Thread() {
			@Override
			public void run() {
				javafx.application.Application.launch(Gui.class);
			}
		}.start();
		startUpTest = Gui.waitForStartUpTest();
		
	}

	public static void cleanTren() {
		prepravka.getTrenDataGui().clear();
		prepravka.getTrenovaciCat().clear();
		prepravka.getTrenovaciBayes().clear();
	}

	public static void cleanTest() {
		prepravka.getTestDataGui().clear();
		prepravka.getTestovaciCat().clear();
		prepravka.getTestovaciBayes().clear();
	}


	public static void nacteni(ObservableMap<Path, TextRep> data, File fileDir, int testOrTren) {
		ArrayList<TextRep> categorie = new ArrayList<TextRep>();
		Path dir = fileDir.toPath();
		int depth = Integer.MAX_VALUE; // maximum depth to search, use
										// Integer.MAX_VALUE to search
										// everything
		try {
			Files.find(dir, depth, (path, attributes) -> path.getFileName().toString().toLowerCase().endsWith(".txt"))
					.forEach(elem -> {
						TextRep soubor = new TextRep(elem);
						data.put(elem, soubor);
						categorie.add(soubor);
					});

			if (testOrTren == 1) {
				for (TextRep value : data.values()) {
					prepravka.getTestovaciSlova().addAll(value.getCetnost().keySet());
					if (prepravka.getTestovaciBayes().containsKey(value.getCategorie())) {
						prepravka.getTestovaciBayes().get(value.getCategorie()).pridej(value);
					} else {
						Categorie druh = new Categorie(value.getCategorie());
						druh.pridej(value);
						prepravka.getTestovaciBayes().put(value.getCategorie(), druh);
					}
				}
				prepravka.setTestovaciCat(categorie);
			} else {
				for (TextRep value : data.values()) {
					prepravka.getTrenovaciSlova().addAll(value.getCetnost().keySet());
					if (prepravka.getTrenovaciBayes().containsKey(value.getCategorie())) {
						prepravka.getTrenovaciBayes().get(value.getCategorie()).pridej(value);
					} else {
						Categorie druh = new Categorie(value.getCategorie());
						druh.pridej(value);
						prepravka.getTrenovaciBayes().put(value.getCategorie(), druh);
					}
				}
				prepravka.setTrenovaciCat(categorie);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void nacteni(ObservableMap<Path, TextRep> data, String vstup) {
		ArrayList<TextRep> categorie = new ArrayList<TextRep>();
		TextRep soubor = new TextRep(vstup);
		data.put(Paths.get("TextArea"), soubor);
		categorie.add(soubor);

		for (TextRep value : data.values()) {
			prepravka.getTestovaciSlova().addAll(value.getCetnost().keySet());
			if (prepravka.getTestovaciBayes().containsKey(value.getCategorie())) {
				prepravka.getTestovaciBayes().get(value.getCategorie()).pridej(value);
			} else {
				Categorie druh = new Categorie(value.getCategorie());
				druh.pridej(value);
				prepravka.getTestovaciBayes().put(value.getCategorie(), druh);
			}
		}
		prepravka.setTestovaciCat(categorie);
	}
	
	public static Prepravka getPrepravka() {
		return prepravka;
	}

}
