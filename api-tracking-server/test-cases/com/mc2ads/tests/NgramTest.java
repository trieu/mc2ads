package com.mc2ads.tests;

import java.util.ArrayList;
import java.util.List;

public class NgramTest {
	public static class Ngram
	{
	    private class result
	    {
	        private String theWord;
	        private int theCount;

	        public result(String w, int c)
	        {
	            theWord = w;
	            theCount = c;
	        }

	        public void setTheCount(int c)
	        {
	            theCount = c;
	        }

	        public String getTheWord()
	        {
	            return theWord;
	        }

	        public int getTheCount()
	        {
	            return theCount;
	        }
	    }

	    private List<result> results;

	    public Ngram()
	    {
	        results = new ArrayList<result>();
	    }
	    public Ngram(String str, int n)
	    {

	    }

	    public double getSimilarity(String wordOne, String wordTwo, int n)
	    {
	        List<result> res1 = processString(wordOne, n);
	        //displayResult(res1);
	        List<result> res2 = processString(wordTwo, n);
	        //displayResult(res2);
	        int c = common(res1,res2);
	        int u = union(res1,res2);
	        double sim = (double)c/(double)u;

	        return sim;
	    }

	    private int common(List<result> One, List<result> Two)
	    {
	        int res = 0;

	        for (int i = 0; i < One.size(); i++)
	        {
	            for (int j = 0; j < Two.size(); j++)
	            {
	                if (One.get(i).theWord.equalsIgnoreCase(Two.get(j).theWord)) res++;
	            }
	        }

	        return res;
	    }

	    private int union(List<result> One, List<result> Two)
	    {
	        List<result> t = One;

	        for (int i = 0; i < Two.size(); i++)
	        {
	            int pos = -1;
	            boolean found = false;
	            for (int j = 0; j < t.size() && !found; j++)
	            {
	                if (Two.get(i).theWord.equalsIgnoreCase(t.get(j).theWord))
	                {
	                    found = true;
	                }
	                pos = j;
	            }

	            if (!found)
	            {
	                result r = Two.get(i);
	                t.add(r);
	            }
	        }

	        return t.size();
	    }

	    private List<result> processString(String c, int n)
	    {
	        List<result> t = new ArrayList<result>();

	        String spacer = "";
	        for (int i = 0; i < n-1; i++)
	        {
	            spacer = spacer + "%";
	        }
	        c = spacer + c + spacer;
	        
	        for (int i = 0; i < c.length(); i++)
	        {
	            if (i <= (c.length() - n))
	            {
	                if (contains(c.substring(i, n+i)) > 0)
	                {
	                    t.get(i).setTheCount(results.get(i).getTheCount()+1);
	                }
	                else
	                {
	                    t.add(new result(c.substring(i,n+i),1));
	                }
	            }
	        }
	        return t;
	    }

	    private int contains(String c)
	    {
	        for (int i = 0; i < results.size(); i++)
	        {
	            if (results.get(i).theWord.equalsIgnoreCase(c))
	                return i;
	        }
	        return 0;
	    }

	    private void displayResult(List<result> d)
	    {
	        for (int i = 0; i < d.size(); i++)
	        {
	            System.out.println(d.get(i).theWord+" occurred "+d.get(i).theCount+" times");
	        }
	    }
	}

	
	
    
    public static List<String> getNgramsByLength(String s, int len) {
        String[] parts = s.split(" ");
        int n = parts.length - len + 1;
        List<String> ngrams = new ArrayList<String>(n);        
        for(int i = 0; i < n; i++) {
           StringBuilder sb = new StringBuilder();
           for(int k = 0; k < len; k++) {
               if(k > 0) sb.append(' ');
               sb.append(parts[i+k]);
           }           
           ngrams.add(sb.toString());
        }
        return ngrams;
    }
    
    public static List<String> ngrams(String s, int maxGramSize){    	
    	List<String> ngrams = new ArrayList<String>();
    	for (int n = 1; n <= maxGramSize; n++) {
             ngrams.addAll(getNgramsByLength(s,n));            
        }
    	return ngrams;
    }
    
   

    public static void main(String[] args) {
    	final String s = "A framework for a multilingual contextual and behavioral online advertising network.";        
        System.out.println(ngrams(s, 3));       
        Ngram ngram = new Ngram();
        double d = ngram.getSimilarity(s, s, 2);
        System.out.println(d);
    }
}
