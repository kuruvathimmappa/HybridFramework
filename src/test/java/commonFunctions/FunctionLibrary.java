package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.Assert;


public class FunctionLibrary {


	public static WebDriver driver;
	public static Properties pro;

	public static WebDriver startBrowser()throws Throwable
	{
		pro = new Properties();
		pro.load(new FileInputStream("./ProperyFiles/Environment.properties"));
		if(pro.getProperty("browser").equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
		}
		if(pro.getProperty("browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
		}
		else
		{
			Reporter.log("browser value is matching",true);
		}
		return driver;

	}
	public static void openUrl()
	{
		driver.get(pro.getProperty("url"));
	}
	public static void waiteForElement(String LocatorType,String LocatorValue,String TestData)
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("id"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue	)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
	}
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);

		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
	}
	public static void clickAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
	}
	public static void validateTitle(String expected_title)
	{
		String actual_title = driver.getTitle();
		try {
			Assert.assertEquals(expected_title,actual_title,"title is not matching");
		}catch (AssertionError e) 
		{
			System.out.println(e.getMessage());
		}

	}
	public static void closeBrowser()
	{
		driver.quit();
	}
	
	public static String dateFormat()
	{
		Date date =  new Date();
		DateFormat df = new SimpleDateFormat("dd_MM_YYYY hh_mm_ss");
		return df.format(date);
		
		
	}
	public static void dropdownAction(String LocatorType,String LocatorValue ,String testData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			int value =Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			int value =Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value =Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.xpath(LocatorType)));
			element.selectByIndex(value);
		}
		
	}
	public static void captureStock(String LocatorType,String LocatorValue) throws Throwable
	{
		String stocknumber="";
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stocknumber = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stocknumber = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stocknumber = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stocknumber);
		
		bw.flush();
		bw.close();
		
	}
	public static void stockTable()throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String expData=br.readLine();
		if(!driver.findElement(By.xpath(pro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(pro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(pro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(pro.getProperty("search-textbox"))).sendKeys(expData);
		driver.findElement(By.xpath(pro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String actData = driver.findElement(By.xpath("//table[@id='tbl_a_stock_itemslist']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(actData+"    "+expData,true);
		try {
		Assert.assertEquals(expData, actData,"stock number should not match");
		}catch(AssertionError a)
		{
			Reporter.log(a.getMessage(),true);
	}
		
	}
	public static void supplierCap(String LocatorType,String LocatorValue) throws Throwable
	{
		String supplierCap="";
		if(LocatorType.equalsIgnoreCase("id"))
		{
			supplierCap = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			supplierCap = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			supplierCap = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/supplierCapNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(supplierCap);
		
		bw.flush();
		bw.close();
		
	}
	
	public static void supplierTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/supplierCapNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String expdata =br.readLine();
		if(!driver.findElement(By.xpath(pro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(pro.getProperty("search-panel"))).click();
			driver.findElement(By.xpath(pro.getProperty("search-textbox"))).clear();
			driver.findElement(By.xpath(pro.getProperty("search-textbox"))).sendKeys(expdata);
			driver.findElement(By.xpath(pro.getProperty("search-button"))).click();
			Thread.sleep(3000);
			String actdata = driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr[1]/td[6]/div/span/span")).getText();
			Reporter.log(actdata+"    "+expdata,true);
			try {
			Assert.assertEquals(expdata, actdata,"stock number should not match");
			}catch(AssertionError a)
			{
				Reporter.log(a.getMessage(),true);
		}
		
	}
	public static void costomerCap(String LocatorType,String LocatorValue) throws Throwable
	{
		String CustomerCap="";
		if(LocatorType.equalsIgnoreCase("id"))
		{
			CustomerCap = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			CustomerCap = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			CustomerCap = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/customerCapNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(CustomerCap);
		
		bw.flush();
		bw.close();
	}
	public static void customerTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/customerCapNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String expdata =br.readLine();
		if(!driver.findElement(By.xpath(pro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(pro.getProperty("search-panel"))).click();
			driver.findElement(By.xpath(pro.getProperty("search-textbox"))).clear();
			driver.findElement(By.xpath(pro.getProperty("search-textbox"))).sendKeys(expdata);
			driver.findElement(By.xpath(pro.getProperty("search-button"))).click();
			Thread.sleep(3000);
			String actdata = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
			Reporter.log(actdata+"    "+expdata,true);
			try {
			Assert.assertEquals(expdata, actdata,"stock number should not match");
			}catch(AssertionError a)
			{
				Reporter.log(a.getMessage(),true);
		}
	
}
	
}