package com.vibridi.fcutil.engine;

import java.io.File;
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
	
	private XSSFWorkbook xwb;
	private Map<String,Integer> fieldIndices; 
	private int headRow;
	
	private Map<String,Player> players;
	private Object[] array;

	public XLSXReader(File xlsx) {
		this.xlsx = xlsx;
		this.head = new HashSet<String>(Arrays.asList(AppOptions.instance.getHeaders()));
		this.fieldIndices = new HashMap<String,Integer>();
		this.headRow = -1;
	}
	
	public void load() {
		try {
			xwb = new XSSFWorkbook(OPCPackage.open(xlsx));
			XSSFSheet sheet = xwb.getSheetAt(0);

			players = Stream.generate(sheet.iterator()::next).limit(sheet.getLastRowNum())
					.filter(this::isPlayer)
					.map(this::toPlayer)
					.collect(Collectors.toMap(Player::getName, Function.identity(), (o,n) -> n, LinkedHashMap::new));

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
		if(array == null)
			array = players.values().toArray();
		return (Player) array[index];
	}
	
	public Map<String,Player> getPlayersMap() {
		return players;
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
			FormulaEvaluator fe = xwb.getCreationHelper().createFormulaEvaluator();
			return fe.evaluate(cell).getNumberValue();
		case NUMERIC:
			return cell.getNumericCellValue();
		default:
			return 0.0;
		}
	}
	
}
