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

public class Parser {
	private Document doc;
	private String SelectedCourseTitle;
	private String SelectedCourseCode;
	private Elements selectedCourse;
	public Parser(String code) throws IOException{
		
		doc = Jsoup.connect("https://w5.ab.ust.hk/wcq/cgi-bin/1210/subject/" + code).get();
		init();
		
		
	}
	
	private void init() {
		removeTrash();
		appendNonSection();
	}

	private void removeTrash() {
		doc.select(".quotadetail").remove();
	}

	public void setCourse(int index){
		Elements allCourses = doc.select(".course");
		selectedCourse = allCourses.eq(index);
		SelectedCourseTitle = selectedCourse.select("h2").text();
	}
	
	public Elements getRows(){
		Elements Rows = selectedCourse.select(".sections").select("tr");
		Rows.remove(0); //remove the first row
		return Rows;
	}
	
	private void appendNonSection(){
		Elements Sectiontables = doc.select(".sections");
		Elements NonNewSectEvenRows = Sectiontables.select("tr[class=secteven");
		Elements NonNewSectOddRows = Sectiontables.select("tr[class=sectodd]");
		
		for(Element row : NonNewSectEvenRows){
			Element preRow = row.previousElementSibling();
			int quota = Integer.parseInt(preRow.select("td").eq(4).text());
			int enrol = Integer.parseInt(preRow.select("td").eq(5).text());
			int avail = Integer.parseInt(preRow.select("td").eq(6).text());
			int wait = Integer.parseInt(preRow.select("td").eq(7).text());
			String remarks = preRow.select("td").eq(6).text();
			row.prepend("<td>" + row.previousElementSibling().select("td").first().text() + "</td>").append("<td>"+quota+"</td>" +
																											"<td>"+enrol+"</td>" +
																											"<td>"+avail+"</td>" +
																											"<td>"+wait+"</td>" +
																											"<td>"+remarks+"</td>");

		}
		for(Element row : NonNewSectOddRows){
			Element preRow = row.previousElementSibling();
			int quota = Integer.parseInt(preRow.select("td").eq(4).text());
			int enrol = Integer.parseInt(preRow.select("td").eq(5).text());
			int avail = Integer.parseInt(preRow.select("td").eq(6).text());
			int wait = Integer.parseInt(preRow.select("td").eq(7).text());
			String remarks = preRow.select("td").eq(6).text();
			row.prepend("<td>" + row.previousElementSibling().select("td").first().text() + "</td>").append("<td>"+quota+"</td>" +
																											"<td>"+enrol+"</td>" +
																											"<td>"+avail+"</td>" +
																											"<td>"+wait+"</td>" +
																											"<td>"+remarks+"</td>");

		}
		
		
	}
	public void printHTML(){
		System.out.println(doc.select(".sections"));
	}
	public String CourseTitle(){
		return SelectedCourseTitle;
	}
	public String ParasedCourseCode(){
		String[] ss = SelectedCourseTitle.split(" ");
		String s = ss[0] + ss[1];
		return s;
	}

	public List<String> getTdListFromRow(Elements row) {
		List<String> TdList = new ArrayList<String>();
		for(Element td : row.select("td")){
			TdList.add(td.text());
		}
		return TdList;
	}
	
	public int noOfCourses(){
		return doc.select(".course").size();
	}
	
	public String[] courseTitleList(){
		int i = 0;
		String[] titleList = {"hah","lolo"};
		/*Elements title = doc.select("h2");
		for(Element n : title){
			titleList[i] = n.toString();
			i++;
		}*/
		return titleList;
	
	}
}
