package edu.handong.csee.java.analyze;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
		ArrayList<String> inputList = new ArrayList<String>();
		HSSFWorkbook workbookWrite = null;
		HSSFSheet sheet = null;
		
		if(!studentId.equals("0001")) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(csvType));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		try {
			workbookWrite = new HSSFWorkbook(fileInputStream);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
			sheet = workbookWrite.getSheetAt(0);
		
		}
		else {
			workbookWrite = new HSSFWorkbook();
			sheet = workbookWrite.createSheet();
		}
		
		XSSFWorkbook workbookRead = null;
		try {
			
			workbookRead = new XSSFWorkbook(stream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Sheet datatypeSheet = workbookRead.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		if(!studentId.equals("0001")) {
			iterator.next();
		}
		
		boolean header = false;
		int rowNum = sheet.getLastRowNum()+1;
		
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
						writeCell.setCellValue((String)readCell.getStringCellValue());
						System.out.println(writeCell.getStringCellValue());
						break;
					
					case NUMERIC : 
						writeCell.setCellValue((Double)readCell.getNumericCellValue());
						System.out.println(writeCell.getNumericCellValue());
						break;
					
					case BLANK :
						break;
					
					default :
						break;
					
					
				}
			}
			
		}
		
		try {
			workbookRead.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		FileOutputStream outputStream = null;
		try {
			
			
				outputStream = new FileOutputStream(new File(csvType));
				workbookWrite.write(outputStream);
				
				workbookWrite.close();
				outputStream.close();
			
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	
	}
}
