package com.vibridi.fcutil;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import com.vibridi.fcutil.engine.FCEngine;
import com.vibridi.fcutil.engine.XLSXReader;
import com.vibridi.fcutil.exception.ValidatorException;
import com.vibridi.fcutil.model.Player;
import com.vibridi.fcutil.utils.AppOptions;

public class AppTest {
	
	private XLSXReader reader;
	
	public AppTest() throws InvalidFormatException, IOException, URISyntaxException {
		reader = testLoad();
	}

    public XLSXReader testLoad() throws InvalidFormatException, IOException, URISyntaxException {
        File f = new File(AppTest.class.getResource("/ListaSvincolatiShort.xlsx").toURI());
        if(!f.exists())
        	throw new IOException("File doesn't exist");

		XLSXReader reader = new XLSXReader(f);
		reader.load();
		
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
	
}
