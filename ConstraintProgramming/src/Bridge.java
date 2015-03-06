
public class Bridge {

	public static final String JAVABRIDGE_PORT = "8080";
    static final php.java.bridge.JavaBridgeRunner runner =
            php.java.bridge.JavaBridgeRunner.getInstance(JAVABRIDGE_PORT);    
     
    public static void main(String[] args) throws InterruptedException
    {
        runner.waitFor();
        System.exit(0);
    }

}
