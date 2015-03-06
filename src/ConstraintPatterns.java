
public class ConstraintPatterns {
	public static void main(String[] args)
	  {
	    Tokenizer tokenizer = new Tokenizer();
	    
	    //Subject Constraints
	    tokenizer.add("[s]", 1);
	    tokenizer.add("[1-9]", 2);
	    tokenizer.add("\\.", 3);
	    tokenizer.add("beginHour|duration|endHour", 4);
	    tokenizer.add("<|>|=", 5);
	    tokenizer.add("[0-9]+",6);
	    int index=0,value=0;;
	    String property="";
	    try
	    {
	      tokenizer.tokenize("s1.beginHour<3 ");
	      int i=1;
	      for (Tokenizer.Token tok : tokenizer.getTokens())
	      {
	        System.out.println("" + tok.token + " " + tok.sequence);
	        if(tok.token == 2 && i==2)
	        	index=Integer.parseInt(tok.sequence);
	        else if(tok.token==2 && i==6)
	        	value=Integer.parseInt(tok.sequence);
	        else if(tok.token == 4)
	        	property = tok.sequence;   
	        
	        ++i;
	      }
	      
	    }
	    catch (ParserException e)
	    {
	      System.out.println(e.getMessage());
	    }
	    System.out.println("index is  "+index +" property is  "+property+" and value is "+value);

	  }
	
}
