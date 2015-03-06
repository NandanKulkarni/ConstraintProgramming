import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import choco.Choco;
import choco.Options;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.model.variables.scheduling.TaskVariable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TimeTable {
	private CPModel model;
	private CPSolver solver;
	private int noOfSubjects;
	private int totalNumberOfLessons;
	private List<TaskVariable> allLessons;
	private Connection connection;
	private ArrayList<Subject> subjects;
	
		public TimeTable(int noOfSubjects, int totalNumberOfHours)
		{
		    this.model = new CPModel();
		    this.solver = new CPSolver();
		    this.totalNumberOfLessons = totalNumberOfHours;
		    this.allLessons = new ArrayList<TaskVariable>();
		    this.noOfSubjects = noOfSubjects;
		    
		    for(int i=0; i<this.noOfSubjects; ++i){
		    	subjects.add(new Subject());
		    }
		    
		    initSubjects();
		}
		{
		    /*//pattern matching
		    String constraint = "ds.beginHour > 2";
		    String pattern = "(.*)(\\.)(.*)(>)(\\d+)";
		    
		    if(constraint.matches(pattern)){
		    	String[] res1 = constraint.split(">");
		    }
		    */
		    
		    
		    TaskVariable ds = this.generateLessonTask("DS", 0, totalNumberOfLessons, 1);
		   // TaskVariable ds = this.generateLessonTask("DS", 2, totalNumberOfLessons, 1);
		    //TaskVariable os = this.generateDoubleLessonTask("OS", 4, totalNumberOfLessons);
		    TaskVariable os = this.generateDoubleLessonTask("OS", 0, totalNumberOfLessons);
		    TaskVariable dbms = this.generateDoubleLessonTask("DBMS", 0, totalNumberOfLessons);
		 
		    TaskVariable fafl = this.generateLessonTask("FAFL", 0, totalNumberOfLessons,1);
		 
		    allLessons.add(ds);
		    allLessons.add(os);
		    allLessons.add(dbms);
		    allLessons.add(fafl);
		 
		    TaskVariable[] LessonsForClass1 = new TaskVariable[allLessons.size()];
		    LessonsForClass1 = (TaskVariable[]) allLessons.toArray(LessonsForClass1);
		 
		   model.addConstraint(Choco.disjunctive(LessonsForClass1));
		 
		    solver.read(model);
		    solver.solve();
		 
		    System.out.println("soln 1");
		    print();
		 
		    int i = 2;
		    while(solver.nextSolution())
		    {
		        System.out.println("soln "+i);
		        print();
		        i++;
		    }
		} 
		

		private void initSubjects() {
			connection = DBConnection.getConnection();
		    int i=0;
		    try {
				PreparedStatement preparedStatement = connection.
						prepareStatement("select * from subjects");
				ResultSet rs = preparedStatement.executeQuery();
				
				while(rs.next()){
			         //Retrieve by column name
			         subjects.get(i).setSname(rs.getString("sname"));
			         subjects.get(i).setBeginHour(rs.getInt("beginHour"));
			         subjects.get(i).setDuration(rs.getInt("duration"));
			         subjects.get(i).setBlockHour(rs.getInt("blockhour"));
			         ++i;      
			      }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		 
		private TaskVariable generateLessonTask(String subject, int i_start, int i_end, int i_duration)
		{
		    IntegerVariable start = Choco.makeIntVar("Start: " + subject, i_start, i_end - 1, Options.V_BOUND);
		    IntegerVariable end = Choco.makeIntVar("End: " + subject, i_start, i_end, Options.V_BOUND);
		    IntegerVariable duration = Choco.constant(i_duration);
		    TaskVariable task = Choco.makeTaskVar(subject, start, end, duration);
		 
		    return task;
		}
		 
		private TaskVariable generateDoubleLessonTask(String subject, int i_start, int i_end)
		{
		   IntegerVariable start = Choco.makeIntVar("Start: " + subject, this.getStartingValues(i_start), Options.V_BOUND);
		//IntegerVariable start = Choco.makeIntVar("Start: " + subject, i_start, i_end-1, Options.V_BOUND);
			IntegerVariable end = Choco.makeIntVar("End: " + subject, i_start, i_end, Options.V_BOUND);
		    IntegerVariable duration = Choco.constant(2);
		    TaskVariable task = Choco.makeTaskVar(subject, start, end, duration);
		 
		    return task;
		}
		 
		private List<Integer> getStartingValues(int i_start)
		{
		    List<Integer> values = new ArrayList<Integer>();
		 
		    for (int i = i_start; i < this.totalNumberOfLessons; i++)
		    {
		        if (i%2==0)
		            values.add(i);
		    }
		 
		    return values;
		 
		}
		 
		private void print()
		{
		    Map<Integer, String> lessons = new TreeMap<Integer, String>();
		 
		    for (int i = 0; i < this.allLessons.size(); i++)
		    {
		        String name = formatString(this.solver.getVar(this.allLessons.get(i)).getName(),12);
		        int start = this.solver.getVar(this.allLessons.get(i)).start().getVal() + 1;
		        int duration = this.solver.getVar(this.allLessons.get(i)).duration().getVal() + 1;
		        int end = this.solver.getVar(this.allLessons.get(i)).end().getVal() + 1;
		 
		        for (int e = start; e < end; e++)
		        {
		            String schedule = (start+". Subject:"+name);
		            lessons.put(start, schedule);
		            start++;
		        }
		    }
		 
		    for(Map.Entry<Integer,String> lesson : lessons.entrySet())
		    {
		        System.out.println(lesson.getValue());
		    }
		 
		    System.out.println("");
		}
		 
		public String formatString(String value, int len)
		{
		    while (value.length() < len)
		    {
		        value += " ";
		    }
		    return value;
		}

//	public static void main(String args[]){
//		TimeTable tt=new TimeTable();
//	}

}
