package com.vibridi.fcutil.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vibridi.fcutil.exception.XLSXLoadException;
import com.vibridi.fcutil.model.Player;
import com.vibridi.fcutil.utils.AppOptions;

public class XLSXReader {
	
	private final File xlsx;
	private final Set<String> head;
	private Map<String,Integer> fieldIndices; 
	private int headRow;
	private Map<String,Player> players;
	private List<String> duplicates;

	public XLSXReader(File xlsx) {
		this.xlsx = xlsx;
		this.head = new HashSet<String>(Arrays.asList(AppOptions.instance.getHeaders()));
		this.fieldIndices = new HashMap<String,Integer>();
		this.headRow = -1;
		this.players = new HashMap<String,Player>();
		this.duplicates = new ArrayList<String>();
		
	}
	
	public void load() {
		try {
			XSSFWorkbook xwb = new XSSFWorkbook(OPCPackage.open(xlsx));
			XSSFSheet sheet = xwb.getSheetAt(0);

			players = Stream.generate(sheet.iterator()::next).limit(sheet.getLastRowNum())
					.filter(this::isPlayer)
					.map(this::toPlayer)
					.collect(Collectors.toMap(Player::getName, Function.identity(), this::merge, LinkedHashMap::new));

			xwb.close();
		} catch(Throwable e) {
			throw new XLSXLoadException(xlsx.getName(), e);
		}
	}
	
	public int size() {
		return players.size();
	}
	
	public String getFileName() {
		return xlsx.getName();
	}
	
	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}
	
	public Set<String> getPlayerNames() {
		return players.keySet();
	}
	
	public Player getPlayer(int index) {
		return (Player) players.values().toArray()[index];
	}
	
	public Map<String,Player> getPlayersMap() {
		return players;
	}
	
	public List<String> getDuplicates() {
		return duplicates;
	}
	
	public long countOffers() {
		return players.values().stream()
			.filter(player -> player.getOffer() != 0.0)
			.count();
	}
	
	public double sumOffers() {
		return players.values().stream()
			.mapToDouble(Player::getOffer)
			.sum();
	}
	
	public boolean didFindTableHead() {
		return headRow != -1;
	}
	
	public boolean hasDuplicates() {
		return duplicates.size() > 0;
	}
	
	private Player merge(Player o, Player n) {
		duplicates.add(o.getName());
		return n;
	}
	
	private boolean isPlayer(Row row) {
		if(headRow == -1 && isTableHead(row)) {
			rememberTableHead(row);
			return false;
		}
		
		if(isBeforeTableHead(row))
			return false;
		
		return true;
	}
	
	private boolean isBeforeTableHead(Row row) {
		return headRow == -1 || row.getRowNum() <= headRow;
	}
	
	private boolean isTableHead(Row row) {
		Cell cell = row.getCell(0);
		if(cell != null && cell.getCellTypeEnum() == CellType.STRING &&
				head.contains(cell.getStringCellValue().trim())) { // possible table head
			
			for(int i = 0; i < row.getLastCellNum(); i++) {
				cell = row.getCell(i);
				if(cell == null || cell.getCellTypeEnum() != CellType.STRING || 
						!head.contains(cell.getStringCellValue().trim()))
					return false;	
			}
			
			headRow = row.getRowNum();
			return true;
		}
		return false;
	}
	
	private void rememberTableHead(Row row) {
		for(int i = 0; i < 5; i++) 
			fieldIndices.put(row.getCell(i).getStringCellValue(), i);
	}
	
	private Player toPlayer(Row row) {		
		return new Player(
				getFileName(),
				row.getCell(fieldIndices.get("Ruolo")).getStringCellValue(),
				row.getCell(fieldIndices.get("Calciatore")).getStringCellValue(),
				row.getCell(fieldIndices.get("Squadra")).getStringCellValue(),
				optionalDouble(row.getCell(fieldIndices.get("Costo Attuale"))),
				optionalDouble(row.getCell(fieldIndices.get("Costo Iniziale"))));		
	}
	
	private double optionalDouble(Cell cell) {
		if(cell == null)
			return 0.0;
		
		switch(cell.getCellTypeEnum()) {
		case _NONE:
		case BLANK:
		case ERROR:
			return 0.0;
		case STRING:
			return Double.parseDouble(cell.getStringCellValue());
		case BOOLEAN:
			return cell.getBooleanCellValue() ? 1.0 : 0.0;
		case FORMULA:
			FormulaEvaluator fe = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			return fe.evaluate(cell).getNumberValue();
		case NUMERIC:
			return cell.getNumericCellValue();
		default:
			return 0.0;
		}
	}
	
}
