package functionality;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PrepravkaSaveLoad implements Serializable {
	
	public Map<String, TextRep> testovaciData = new HashMap<String, TextRep>();
	public Map<String, TextRep> trenovaciData = new HashMap<String, TextRep>();
	public HashSet<String> testovaciSlova = new HashSet<String>();
	public HashSet<String> trenovaciSlova = new HashSet<String>();
	public ArrayList<TextRep> testovaciNN = new ArrayList<TextRep>();
	public ArrayList<TextRep> trenovaciNN = new ArrayList<TextRep>();
	public Map<String,Categorie> testovaciBayes = new HashMap<String,Categorie>();
	public Map<String,Categorie> trenovaciBayes = new HashMap<String,Categorie>();
	public NejblizsiSoused soused;
	public Bayes bayes;
	

	public PrepravkaSaveLoad(Prepravka p) {
		p.getTestovaciData().forEach((k,v)-> this.testovaciData.put(k.toString(),v));
		p.getTrenovaciData().forEach((k,v)-> this.trenovaciData.put(k.toString(),v));
		this.testovaciSlova = p.getTestovaciSlova();
		this.trenovaciSlova = p.getTrenovaciSlova();
		this.testovaciNN = p.getTestovaciCat();
		this.trenovaciNN = p.getTrenovaciCat();
		this.testovaciBayes = p.getTestovaciBayes();
		this.trenovaciBayes = p.getTrenovaciBayes();
	}


	private static String EXTENSION = ".prep";


    public static void save(File model, PrepravkaSaveLoad pr) throws IOException {

        FileOutputStream fos = new FileOutputStream(model);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream output = new ObjectOutputStream(bos);
        
        output.writeObject(pr);
       

        output.close();
        bos.close();
        fos.close();
    }


    public static Prepravka load(String ulozene) throws IOException, ClassNotFoundException {
        String modelName = ulozene;
        if (!modelName.endsWith(EXTENSION)) {
            modelName = modelName + EXTENSION;
        }

        File f = new File(modelName);
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream (bis);

        PrepravkaSaveLoad docasnep = (PrepravkaSaveLoad)ois.readObject();

        ois.close();
        bis.close();
        fis.close();
        
        Prepravka p = new Prepravka(docasnep);

        return p;
    }
}
