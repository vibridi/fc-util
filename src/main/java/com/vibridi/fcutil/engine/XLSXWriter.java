package com.vibridi.fcutil.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vibridi.fcutil.exception.XLSXWriteException;
import com.vibridi.fcutil.model.Player;
import com.vibridi.fcutil.utils.AppContext;
import com.vibridi.fcutil.utils.AppOptions;

public class XLSXWriter {
	
	private String owner;
	private List<Player> players;
	private XSSFWorkbook wb;
	
	public XLSXWriter(String owner, List<Player> players) {
		this.owner = owner;
		this.players = players;
	}

	public String getOwner() {
		return owner;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public void prepareWorkbook() {
		if(owner.equals(AppContext.CONTENDED))
			prepareContended();
		else
			prepareList();
	}
	
	public void writeWorkbook(File directory) {
		try {
			File dest = new File(directory, owner);
			wb.write(new FileOutputStream(dest));
		} catch(Exception e) {
			throw new XLSXWriteException(e);
		}
	}
	
	private void prepareList() {
		wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Lista Giocatori");
		Row head = sheet.createRow(0);
		
		String[] headers = AppOptions.instance.getHeaders();
		for(int j = 0, col = headers.length; j < col; j++) {
			Cell cell = head.createCell(j, CellType.STRING);
			cell.setCellValue(headers[j]);
			cell.setCellStyle(createHeaderStyle());
		}
			
		for(int i = 0; i < players.size(); i++) {
			Row row = sheet.createRow(i+1);
			for(int j = 0, col = headers.length; j < col; j++) {
				switch(j) {
				case 0:
					row.createCell(j, CellType.STRING).setCellValue(players.get(i).getRole());
					break;
				case 1:
					row.createCell(j, CellType.STRING).setCellValue(players.get(i).getName());
					break;
				case 2:
					row.createCell(j, CellType.STRING).setCellValue(players.get(i).getTeam());
					break;
				case 3:
					row.createCell(j, CellType.NUMERIC).setCellValue(players.get(i).getCost());
					break;
				case 4:
					row.createCell(j, CellType.NUMERIC).setCellValue(players.get(i).getOffer());
					break;
				}
			}
		}
		
	}
	
	private void prepareContended() {
		wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Lista Giocatori Contesi");
		
		AtomicInteger idx = new AtomicInteger();
		players.forEach(p -> {
				sheet.createRow(idx.getAndIncrement()).createCell(0, CellType.STRING).setCellValue(p.getName());
				for(String contender : p.getContenders())
					sheet.createRow(idx.getAndIncrement()).createCell(1, CellType.STRING).setCellValue(contender);
		});
	}
	
	private CellStyle createHeaderStyle() {
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.index);
		return headerStyle;
	}

}
