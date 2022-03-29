package gui.Functions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.mysql.cj.jdbc.DatabaseMetaData;

public class DataFunctions {
	private String sWebURL;
	private String sBrowser;
	public String sReportName;

 	public String getWebURL()
	{
		return sWebURL;
	}
 	public String getReportName()
	{
		return sReportName;
	}
	public String getBrowser()
	{
		return sBrowser;
	}

		


  public void GetEnvironmentVariables(String sDefaultPath) throws IOException, ParseException
	{
		File f1=null;
		FileReader fr=null;

		JSONParser parser = new JSONParser();
		try {
			f1 = new File(sDefaultPath+"\\conf\\Environment.json");
			fr = new FileReader(f1);
			Object obj = parser.parse(fr);
			JSONObject jsonObject = (JSONObject) obj;
			sWebURL = (String) jsonObject.get("weburl");
			sBrowser = (String) jsonObject.get("Browser");
			sReportName = (String) jsonObject.get("ReportName");

		} finally {
			try{
			   fr.close();

			}catch(IOException ioe)

			{
				ioe.printStackTrace();
			}
		}

	}
}

