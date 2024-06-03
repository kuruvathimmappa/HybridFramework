package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtilities {
	
	XSSFWorkbook wb;
	public ExcelUtilities(String excelfile) throws Throwable
	{
		FileInputStream fi = new FileInputStream(excelfile);
		wb = new XSSFWorkbook(fi);
		
	}
	public int rowcount(String xlsheet)
	{
		return wb.getSheet(xlsheet).getLastRowNum();
	}
	public String getCellData(String xlsheet,int row,int column)
	{
		String data;
		if(wb.getSheet(xlsheet).getRow(row).getCell(column).getCellType()==CellType.NUMERIC)
		{
			int celldata=(int)wb.getSheet(xlsheet).getRow(row).getCell(column).getNumericCellValue();
			data=String.valueOf(celldata);
		}else
		{
			data = wb.getSheet(xlsheet).getRow(row).getCell(column).getStringCellValue();
		}
		return data;
	}	
	
	public void setCellData(String xlsheet,int row,int column,String status,String writeExcel) throws Throwable
	
	{
		XSSFSheet ws = wb.getSheet(xlsheet);
		XSSFRow rowNum = ws.getRow(row);
		XSSFCell cell = rowNum.createCell(column);
		cell.setCellValue(status);
		if(status.equalsIgnoreCase("pass"))
		{
			XSSFCellStyle cellstyle = wb.createCellStyle();
			XSSFFont font =  wb.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.GREEN.getIndex());
			cellstyle.setFont(font);
			rowNum.getCell(column).setCellStyle(cellstyle);	
		}else if(status.equalsIgnoreCase("fail"))
		{
			XSSFCellStyle cellstyle = wb.createCellStyle();
			XSSFFont font =  wb.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.RED.getIndex());
			cellstyle.setFont(font);
			rowNum.getCell(column).setCellStyle(cellstyle);
		}
		else if(status.equalsIgnoreCase("blocked"))
		{
			XSSFCellStyle cellstyle = wb.createCellStyle();
			XSSFFont font =  wb.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.BLACK.getIndex());
			cellstyle.setFont(font);
			rowNum.getCell(column).setCellStyle(cellstyle);	
		}
		FileOutputStream fo = new FileOutputStream(writeExcel);
		wb.write(fo);
		
	}
	
	
}
