package com.mc2ads;


public class NodesStarter {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		int min = 128, max = 1024;
		String jarName = "backend-server.jar";
		if(args.length == 3){
			min = Integer.parseInt(args[0]);
			max = Integer.parseInt(args[1]);
			jarName = (args[2]);
		}		
		String cmd = "java -jar -Xms"+min+"m -Xmx"+max+"m -XX:MaxPermSize="+max+"m -XX:+CMSClassUnloadingEnabled -XX:-UseParallelGC "+jarName;
		Runtime.getRuntime().exec(cmd);
		System.out.println("exec job :" + cmd);
		
//		Runtime.getRuntime().exec( new String[] {
//                "java",
//                "-jar",
//                "-Xms512m",
//                "-Xmx2048m",
//                "-UseParallelGC",
//                "agent.jar",
//                "My Parser"});		
		
		
		System.out.println("started ...");
	}
}
