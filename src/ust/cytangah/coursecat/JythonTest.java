package ust.cytangah.coursecat;

import java.io.*;
import org.python.util.PythonInterpreter;

public class JythonTest {

	public JythonTest(){
		PythonInterpreter py = new PythonInterpreter();
		py.exec("days=('mod','Tue','Wed','Thu','Fri','Sat','Sun'); ");
	}

}
