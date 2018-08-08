/***
 * @author Magdalena Kurek
 * @version: 5.0
 * @appname: endberichte_win
 * @system: für Windows-Systeme
 * @erstellungsdatum: 05.06.2018
 * @letzteänderung: 13.07.2018
 * @was_wurde_geändert: Es werden nun Unterordner durchsucht und alles nach 1.5.18 in die CSV geschrieben
 * @ToDo: Usereingabe ab wann aufgelistet werden soll
 * @beschreibung: Diest ist ein Hilfsmittel für Personalberater für die Kontrolle ob alle Endberichte verschickt worden sind
 * Endberichtsdateibenennungsformat: Veranstaltungsnummer_Maßnahmennummer_SVNummer_Vorname_Nachname_AB.pdf
 * Es durchsucht den Ordner von welchen aus diese Applikation gestartet wird nach
 * PDF Dateien. Wenn es eine PDF ist, werden alle Teile des Dateinamens gelöscht, außer die SVNR.
 * Diese wird in eine CSV-Datei geschrieben. 
 * Sobald alle Dateien geprüft und bearbeitet wurden, wird die CSV Datei abgespeichert und das Programm beendet.
 */


package endberichte;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class endberichte {

	public static void main(String[] args) {
		
		System.out.println("Endberichte Begin");
		//setzen des Pfades für csvDatei
		Path csvDateiPfad = Paths.get("./exportierte_berichte_aus_diesem_ordner.csv");
		// Dateien in CSV Datei schreiben
		try(BufferedWriter schreibPuffer = Files.newBufferedWriter(csvDateiPfad)){		
			//setzen des aktuellen Pfades
			File rootDirectory = new File("./");

			// auflisten aller Files im rootDirectory
			String[] DirInRoot = rootDirectory.list();

	    
			// Alle Ordner aus Root durchgehen
			if(DirInRoot != null) {
			
				for ( int i=0; i<DirInRoot.length; i++ )
				{
					// Eintrag zum File machen
					File aktuelleDateiAusRoot = new File(DirInRoot[i]);
	    		
					//Wenn es ein Ordner ist
					if(aktuelleDateiAusRoot.isDirectory()) {
	    		
						
						// auflistung der Dateien im Unterordner
						String[] filesInDir = aktuelleDateiAusRoot.list(); 
	    		    
	    		    
						// Prüfen ob Unterordner leer
						if(filesInDir != null) {
							/*for ( int j=0; j<filesInDir.length; j++ )
	    		    		{
	    		    			System.out.println( "file: " + filesInDir[j] );
	    		    		}*/
						}
						else
						{
							System.err.println("Verzeichnis leer");
						}

						//Alle aufgelisteten Dateien durchgehen
		    			for(int z=0; z < filesInDir.length; z++) {
		    					//Ausgabe der Datei als String
		    					String zeile = String.format("%s;", filesInDir[z]);

		    					//Aus aktuellem Eintrag ein File machen -> für Datumsvergleich
		    					File aktuelle_datei = new File("./" + DirInRoot[i] + "/"+ filesInDir[z]);
		    					
		    					/* TODO DATUMSTEST */
		    					Calendar cal = Calendar.getInstance();
		    					cal.setTimeInMillis( aktuelle_datei.lastModified() );
		    					
		    					int tag;
		    					int monat;
		    					int jahr;

		    				
		    					tag = cal.get(Calendar.DAY_OF_MONTH);
		    					monat = (cal.get(Calendar.MONTH)+1);
		    					jahr = cal.get(Calendar.YEAR);

		    					if((tag >= 1) && (monat >= 5) && (jahr >= 2018)) {
		    						int position = 0;
		    				
		    						//Prüfung ob PDF
		    						position = zeile.indexOf(".");
		    						String dateiendung = zeile.substring(position+1,zeile.length());
		    						
		    						if(dateiendung.contains("pdf"))
		    						{
		    							
		    							//löschen von Veranstaltungsnummer und Maßnahmennummer
		    							for(int p = 0; p < 2; p++) {
		    								position = zeile.indexOf("_")+1;
		    								zeile = zeile.substring(position, zeile.length());
		    							}
		    					
		    							//löschen von allem was nach svnr kommt
		    							position = zeile.indexOf("_");
		    							zeile = zeile.substring(0,position);
		    						
		    							//Testausgabe
		    							System.out.println("Diese Zeile wird in die CSV geschrieben:" + zeile);
		    							//in Datei schreiben
		    							schreibPuffer.write(zeile);
		    							schreibPuffer.newLine();
		    						}//if dateiendung pdf
		    					}//ifdatumsprüfung
		    				}//for filesindir
					}//if ordner
				}//for dirinordner
			}//if ordner
		}//try csv
		catch(IOException ex) {
			System.err.println("CSV Writer fehlgeschlagen");
		}		
	}//main
}//class
