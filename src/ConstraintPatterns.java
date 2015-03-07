
public class ConstraintPatterns {
	
	    Tokenizer tokenizer = new Tokenizer();
	    
	    public void addConstraints(){
	    //Subject Constraints
	    tokenizer.add("[s][1-9]", 1);
	    tokenizer.add("\\.", 2);
	    tokenizer.add("beginHour|duration|endHour", 3);
	    tokenizer.add("<|>|=", 4);
	    tokenizer.add("[0-9]+",5);
	    }
	    
	    public Tokenizer getTokenizer()
	    {
	    	return tokenizer;
	    }
	    
	    
	    

	 
	
}
