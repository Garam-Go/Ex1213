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
			if(!e.select(".title").text().equals("")){ //데이터값에 title이 비어있지 않으면 add하도록 지시
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
		
		//사용하는 브라우저의 드라이버를 다운받아 그 드라이버를 가져와서 실행하기
		System.setProperty("webdriver.chrome.driver", "C:/spring/chromedriver.exe");
		ChromeOptions option = new ChromeOptions();
		option.addArguments("headless"); //셀레니움 쓸때 새로운 크롬 브라우저가 켜져서 셀레니움을 하는데 그걸 방지함.
		
		WebDriver driver = new ChromeDriver(option);
		
		driver.get("https://www.naver.com/");//네이버 사이트를 설정
		
		//겹치지 않는 div를 찾기위해 상위의 id가 붙은 div를 찾아서 그 안에 원하는 요소를 가져옴
		WebElement btnTheme = driver.findElement(By.id("PM_ID_themecastNavi")).findElement(By.className("ac_btn_cate")); 
		//내가 가져오고싶은 요소의 제일 가까운상위 id값, 그안에 원하는 class값
		
		btnTheme.click();//위에 지정한 요소를 클릭하는 이벤트 발생
		
		//버튼을 눌러 나오는 페이지에서 그안에 담긴 요소 가져옴
		WebElement theme = driver.findElement(By.id("PM_ID_themeEditItemList"));//상위 id검색
		
		List<WebElement> themes = theme.findElements(By.className("at_item"));//at_item의 정보들을 리스트에 반복해서 담아줌
		//반복문 돌려서 그안에 있는 값을 가져오쟈
		for(WebElement e:themes){
			WebElement item = e.findElement(By.className("PM_CL_themeItemSelect"));//a태그의 class검색
			if(item.getAttribute("data-id").equals("TRAVEL")){ //검색한 class의 data-id가 TRAVEL인 버튼을 찾아옴
				item.click(); //버튼클릭해서 들어감
				
				//클릭해서 나온 페이지에서 또 원하는 값을 가져옴
				WebElement travel = driver.findElement(By.id("PM_ID_themecastBody")).findElement(By.className("themecast_list"));
				List<WebElement> travels = travel.findElements(By.className("tl_default"));
				for(WebElement te:travels){
					WebElement title = te.findElement(By.className("td_t"));
					HashMap<String, Object> hash = new HashMap<String, Object>();
					hash.put("title", title.getText());
					list.add(hash);//우리가 담아줄 데이터를 리턴하는 리스트(가장 상위에 만들어놓음)에 넣어줌
				}
			}
		}
		driver.quit();
		return list;
	}
	}
