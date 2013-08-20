package ust.cytangah.coursecat;

import ust.cytangah.coursecat.GlobalData;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MiniParser {
	//private Document doc = null;
	private String SelectedCourseTitle;
	private String SelectedCourseCode;
	private Elements selectedCourse;
	public MiniParser(String code) throws IOException{
		
	//doc = Jsoup.connect("https://w5.ab.ust.hk/wcq/cgi-bin/1210/subject/" + code).get();
			
	}
	
	public static void genAllCourseList(String code){
		Document doc = null;
		try {
			doc = Jsoup.connect("https://w5.ab.ust.hk/wcq/cgi-bin/1210/subject/" + code).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
		
		
			Elements h2s = doc.select("h2");
			GlobalData.currentCourses.clear();
				for(Element n : h2s){
					GlobalData.currentCourses.add(n.text());
				}
			GlobalData.currentCourses.add("shit");
			
			System.out.println(GlobalData.currentCourses);
		}
		
	}
}
