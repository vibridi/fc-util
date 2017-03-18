package com.vibridi.fcutil;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import com.vibridi.fcutil.engine.FCEngine;
import com.vibridi.fcutil.engine.XLSXReader;
import com.vibridi.fcutil.engine.XLSXWriter;
import com.vibridi.fcutil.exception.ValidatorException;
import com.vibridi.fcutil.model.Player;
import com.vibridi.fcutil.utils.AppOptions;

public class AppTest {
	
	private XLSXReader reader;
	
	public AppTest() throws InvalidFormatException, IOException, URISyntaxException {
		reader = testLoad();
	}

    public XLSXReader testLoad() throws InvalidFormatException, IOException, URISyntaxException {
    	XLSXReader reader = getReader("ListaSvincolatiShort.xlsx");
		
		assertTrue(reader.size() == 5);
		
		Player p = reader.getPlayer("FESTA");
		assertTrue(p.getRole().equals("P"));
		assertTrue(p.getName().equals("FESTA"));
		assertTrue(p.getTeam().equals("CROTONE"));
		assertTrue(p.getCost() == 3.25);
		assertTrue(p.getOffer() == 0.0);	
		
		p = reader.getPlayer(reader.size() - 1);
		//System.out.println(p.toString());
		assertTrue(p.getRole().equals("P"));
		assertTrue(p.getName().equals("PERISAN"));
		assertTrue(p.getTeam().equals("UDINESE"));
		assertTrue(p.getCost() == 1.0);
		assertTrue(p.getOffer() == 5.0);
		
		return reader;
    }
	
	@Test
	public void testPlayerToString() throws InvalidFormatException, IOException, URISyntaxException {
		Player p = reader.getPlayer(0);
		assertTrue(p.toString().equals("role=[P],name=[FESTA],team=[CROTONE],cost=[3.25],offer=[0.0]"));
	}
	
	@Test
	public void testBudget() {
		assertTrue(reader.sumOffers() == 7.0);
	}
	
	@Test
	public void testCount() {
		assertTrue(reader.countOffers() == 3.0);
	}
	
	@Test(expected = ValidatorException.class)
	public void testValidationFail() {
        FCEngine engine = new FCEngine(Arrays.asList(reader));
        engine.readOffers();
        engine.validateOffers();
	}
	
	@Test
	public void testValidation() {        
        AppOptions.instance.setOfferCount(3);
        AppOptions.instance.setRoundBudget(7.0);
                
        FCEngine engine = new FCEngine(Arrays.asList(reader));
        engine.readOffers();
        engine.validateOffers();
        
        AppOptions.instance.resetDefaults();
	}
	
	@Test(expected = ValidatorException.class)
	public void testHeaderValidationExtra() throws URISyntaxException, IOException { 
		XLSXReader reader = getReader("InvalidHeader-extravalue.xlsx");
                
        FCEngine engine = new FCEngine(Arrays.asList(reader));
        engine.readOffers();
        engine.validateOffers();
	}
	
	@Test(expected = ValidatorException.class)
	public void testHeaderValidationMistyped() throws URISyntaxException, IOException { 
		XLSXReader r2 = getReader("InvalidHeader-mistyped.xlsx");
                
        FCEngine engine = new FCEngine(Arrays.asList(r2));
        engine.readOffers();
        engine.validateOffers();
	}
	
	@Test(expected = ValidatorException.class)
	public void testHeaderValidationNotString() throws URISyntaxException, IOException { 
		XLSXReader r3 = getReader("InvalidHeader-notastring.xlsx");
                
        FCEngine engine = new FCEngine(Arrays.asList(r3));
        engine.readOffers();
        engine.validateOffers();
	}
	
	@Test
	public void testComputeLists() throws URISyntaxException {
		File f1 = new File(AppTest.class.getResource("/ListaSvincolatiShort.xlsx").toURI());
		File f2 = new File(AppTest.class.getResource("/ListaSvincolatiShort2.xlsx").toURI());
		File f3 = new File(AppTest.class.getResource("/ListaSvincolatiShort3.xlsx").toURI());
		
		XLSXReader r1 = new XLSXReader(f1);
		XLSXReader r2 = new XLSXReader(f2);
		XLSXReader r3 = new XLSXReader(f3);
		
		FCEngine engine = new FCEngine(Arrays.asList(r1,r2,r3));
        engine.readOffers();
        engine.computeLists();
        
        Map<String,XLSXWriter> wmap = engine.getWriters().stream()
        		.map(w -> {
        			System.out.println(w.getOwner());
                	for(Player p : w.getWonPlayers()) {
                		System.out.println("\t"+p.getName()+"\t"+p.getOffer()+"\t"+p.getOfferer());
                		if(w.getOwner().equals("GiocatoriContesi.xlsx")) {
                			for(String cont : p.getContenders())
                				System.out.println("\t\t"+cont);
                		}
                			
                	}
                	System.out.println();
        			return w; })
        		.collect(Collectors.toMap(XLSXWriter::getOwner, Function.identity()));

        XLSXWriter ls1 = wmap.get("ListaSvincolatiShort.xlsx");
        XLSXWriter ls2 = wmap.get("ListaSvincolatiShort2.xlsx");
        XLSXWriter ls3 = wmap.get("ListaSvincolatiShort3.xlsx");
        XLSXWriter na = wmap.get("NonAssegnati.xlsx");
        XLSXWriter gc = wmap.get("GiocatoriContesi.xlsx");
        
        
        assertTrue(ls1 != null);
        assertTrue(ls1.getWonPlayers().size() == 1);
        assertTrue(ls1.getWonPlayers().get(0).getName().equals("MARSON"));
        
        assertTrue(ls2!= null);
        assertTrue(ls2.getWonPlayers().size() == 1);
        assertTrue(ls2.getWonPlayers().get(0).getName().equals("RADU I"));
        
        assertTrue(ls3 != null);
        assertTrue(ls3.getWonPlayers().size() == 1);
        assertTrue(ls3.getWonPlayers().get(0).getName().equals("FESTA"));
        
        assertTrue(na != null);
        assertTrue(na.getWonPlayers().size() == 1);
        assertTrue(na.getWonPlayers().get(0).getName().equals("GOLLINI"));
        
        assertTrue(gc != null);
        assertTrue(gc.getWonPlayers().size() == 1);
        assertTrue(gc.getWonPlayers().get(0).getName().equals("PERISAN"));
	}
	
	private XLSXReader getReader(String fileName) throws URISyntaxException, IOException {
		File f = new File(AppTest.class.getResource("/" + fileName).toURI());
        if(!f.exists())
        	throw new IOException("File doesn't exist");

		XLSXReader reader = new XLSXReader(f);
		reader.load();
		return reader;
	}
	
}
