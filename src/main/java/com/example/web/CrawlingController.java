package com.example.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CrawlingController {

	@ResponseBody
	@RequestMapping("movie.json")
	public List<HashMap<String, Object>> movie()throws Exception{
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		Document doc = Jsoup.connect("http://www.cgv.co.kr/movies/").get();
		Elements es = doc.select(".sect-movie-chart ol");
		
		for(Element e:es.select("li")){
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("rank", e.select(".rank").text());
			hash.put("title", e.select(".title").text());
			hash.put("image", e.select("img").attr("src"));
			if(!e.select(".title").text().equals("")){ //�����Ͱ��� title�� ������� ������ add�ϵ��� ����
				list.add(hash);
			}
			
		}
		return list;
	}
	
	@RequestMapping("list")
	public String list(){
		return "list";
	}
	
	@ResponseBody
	@RequestMapping("naver.json")
	public List<HashMap<String, Object>> naver()throws Exception{
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		Document doc = Jsoup.connect("https://www.naver.com/").get();
		Elements es = doc.select(".section_navbar .ah_roll_area .ah_l");
		
		for(Element e:es.select(".ah_item")){
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("rank", e.select(".ah_r").text());
			hash.put("title", e.select(".ah_k").text());
			
			list.add(hash);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping("daum.json")
	public ArrayList<HashMap<String, Object>> daum()throws Exception{
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		Document doc = Jsoup.connect("https://www.daum.net/").get();
		Elements es = doc.select(".info_today .list_weather");
		
		for(Element e:es.select(".hide")){
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("region", e.select(".txt_part").text());
			hash.put("condition", e.select(".ico_ws").text());
			hash.put("temp", e.select(".txt_temper").text());
			
			list.add(hash);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping("travel.json")
	public ArrayList<HashMap<String, Object>> travel()throws Exception{
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		
		//����ϴ� �������� ����̹��� �ٿ�޾� �� ����̹��� �����ͼ� �����ϱ�
		System.setProperty("webdriver.chrome.driver", "C:/spring/chromedriver.exe");
		ChromeOptions option = new ChromeOptions();
		option.addArguments("headless"); //�����Ͽ� ���� ���ο� ũ�� �������� ������ �����Ͽ��� �ϴµ� �װ� ������.
		
		WebDriver driver = new ChromeDriver(option);
		
		driver.get("https://www.naver.com/");//���̹� ����Ʈ�� ����
		
		//��ġ�� �ʴ� div�� ã������ ������ id�� ���� div�� ã�Ƽ� �� �ȿ� ���ϴ� ��Ҹ� ������
		WebElement btnTheme = driver.findElement(By.id("PM_ID_themecastNavi")).findElement(By.className("ac_btn_cate")); 
		//���� ����������� ����� ���� �������� id��, �׾ȿ� ���ϴ� class��
		
		btnTheme.click();//���� ������ ��Ҹ� Ŭ���ϴ� �̺�Ʈ �߻�
		
		//��ư�� ���� ������ ���������� �׾ȿ� ��� ��� ������
		WebElement theme = driver.findElement(By.id("PM_ID_themeEditItemList"));//���� id�˻�
		
		List<WebElement> themes = theme.findElements(By.className("at_item"));//at_item�� �������� ����Ʈ�� �ݺ��ؼ� �����
		//�ݺ��� ������ �׾ȿ� �ִ� ���� ��������
		for(WebElement e:themes){
			WebElement item = e.findElement(By.className("PM_CL_themeItemSelect"));//a�±��� class�˻�
			if(item.getAttribute("data-id").equals("TRAVEL")){ //�˻��� class�� data-id�� TRAVEL�� ��ư�� ã�ƿ�
				item.click(); //��ưŬ���ؼ� ��
				
				//Ŭ���ؼ� ���� ���������� �� ���ϴ� ���� ������
				WebElement travel = driver.findElement(By.id("PM_ID_themecastBody")).findElement(By.className("themecast_list"));
				List<WebElement> travels = travel.findElements(By.className("tl_default"));
				for(WebElement te:travels){
					WebElement title = te.findElement(By.className("td_t"));
					HashMap<String, Object> hash = new HashMap<String, Object>();
					hash.put("title", title.getText());
					list.add(hash);//�츮�� ����� �����͸� �����ϴ� ����Ʈ(���� ������ ��������)�� �־���
				}
			}
		}
		driver.quit();
		return list;
	}
	}
