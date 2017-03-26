package com.vibridi.fcutil.utils;

public class AppMessages {

	public static final String STATUS_OK = "OK";
	public static final String STATUS_ERR = "Errore!";
	public static final String STATUS_VALID = "Valido";
	public static final String STATUS_INVALID = "Non valido";
	
	public static String FILE_LOAD_ERROR_HEADER;
	public static String FILE_LOAD_ERROR_BODY;
	public static String NOT_PROCESSABLE;
	public static String NO_FILE_LOADED;
	
	public static String READING_FILES;
	public static String VALIDATING_LISTS;
	
	public static String READ_SUCCESS;
	public static String COMPUTE_SUCCESS;
	public static String WRITE_SUCCESS;
	
	public static String COMPUTE_FAIL;
	
	static {		
		FILE_LOAD_ERROR_HEADER = "Ecco l'hai rotto!";
		FILE_LOAD_ERROR_BODY = "Sembra che questo file non sia stato caricato correttamente."
				+ System.getProperty("line.separator")
				+ "Copia i dettagli dell'errore che trovi qui sotto e "
				+ "inviali allo sviluppatore... prima che qualcuno si faccia del male.";
		
		NOT_PROCESSABLE = "Impossibile eseguire il calcolo. Uno o più file presentano degli errori."
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ "Assicurati che tutti i file vengano letti e validati e prova di nuovo.";
		
		NO_FILE_LOADED = "Non hai caricato nessun file.";
		
		READING_FILES = "Sto leggendo i file...";
		VALIDATING_LISTS = "Sto validando le liste...";
		
		READ_SUCCESS = "Caricamento file completato";
		COMPUTE_SUCCESS = "Calcolo liste completato";
		WRITE_SUCCESS = "Salvataggio file completato";
		
		COMPUTE_FAIL = "Errore durante il calcolo delle liste";
	}
	

}
