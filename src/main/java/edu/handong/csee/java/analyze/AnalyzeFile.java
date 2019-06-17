package edu.handong.csee.java.analyze;

import java.util.Enumeration;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AnalyzeFile<T> implements Runnable {
	private InputStream stream;
	private T studentId;
	private String csvType;
	
	public AnalyzeFile(String csvType, InputStream stream, T studentId ) {
		this.stream = stream;
		this.studentId = studentId;
		this.csvType = csvType;
	}
	public void run() {
		XSSFWorkbook workbookWrite  = new XSSFWorkbook();
		XSSFSheet sheet = workbookWrite.createSheet();
		
		
		Workbook workbookRead = null;
		try {
			
			workbookRead = new XSSFWorkbook(stream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Sheet datatypeSheet = workbookRead.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		
		int rowNum = 0;
		while(iterator.hasNext()) {
			Row currentRow = iterator.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			
			Row row = sheet.createRow(rowNum++);
			int columnNum = 0;
			
			Cell firstCell = row.createCell(columnNum++);
			firstCell.setCellValue((String)studentId);
			while(cellIterator.hasNext()) {
				Cell readCell = cellIterator.next();
				Cell writeCell = row.createCell(columnNum++);
				
				
				switch(readCell.getCellType()){
						
					case STRING :
		
						String inputString = (String)readCell.getStringCellValue();
					byte arrString[] = null;
					try {
						arrString = inputString.getBytes("utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						inputString = new String(arrString);
						writeCell.setCellValue(readCell.getStringCellValue());
						System.out.println(writeCell.getStringCellValue());
						break;
					
					case NUMERIC : 
						String inputDouble = Double.toString((Double)readCell.getNumericCellValue());
					byte arrDouble[] = null;
					try {
						arrDouble = inputDouble.getBytes("utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
						inputDouble = new String(arrDouble);
						writeCell.setCellValue(inputDouble);
						System.out.println(writeCell.getStringCellValue());
						break;
					
					case BLANK :
						break;
					
					default :
						break;
					
					
				}
			}
			
		}
		
		try {
			if(csvType.contains("Summary")) {
				FileOutputStream outputStream = new FileOutputStream(csvType,true);
				workbookWrite.write(outputStream);
				
				workbookWrite.close();
				outputStream.close();
			}
			else {
				FileOutputStream outputStream = new FileOutputStream(csvType,true);
				workbookWrite.write(outputStream);
				
				workbookWrite.close();
				outputStream.close();
			}
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	
	}
}
